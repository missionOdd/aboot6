package com.wteam.modules.system.service.impl;


import cn.hutool.core.collection.CollectionUtil;
import com.wteam.exception.BadRequestException;
import com.wteam.exception.EntityExistException;
import com.wteam.modules.system.config.CacheKey;
import com.wteam.modules.system.domain.Menu;
import com.wteam.modules.system.domain.User;
import com.wteam.modules.system.domain.criteria.MenuQueryCriteria;
import com.wteam.modules.system.domain.dto.MenuDTO;
import com.wteam.modules.system.domain.dto.RoleSmallDTO;
import com.wteam.modules.system.domain.mapper.MenuMapper;
import com.wteam.modules.system.domain.vo.MenuMetaVo;
import com.wteam.modules.system.domain.vo.MenuVo;
import com.wteam.modules.system.repository.MenuRepository;
import com.wteam.modules.system.repository.UserRepository;
import com.wteam.modules.system.service.MenuService;
import com.wteam.modules.system.service.RoleService;
import com.wteam.utils.FileUtil;
import com.wteam.utils.QueryHelper;
import com.wteam.utils.RedisUtils;
import com.wteam.utils.ValidUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 菜单 业务层实现类
 * @author mission
 * @since 2019/07/08 21:17
 */
@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "menu")
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class MenuServiceImpl implements MenuService {


    private final MenuRepository menuRepository;

    private final MenuMapper menuMapper;

    private final RedisUtils redisUtils;

    private final RoleService roleService;

    private final UserRepository userRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MenuDTO create(Menu resources) {
        if (menuRepository.findByName(resources.getName())!=null){
            throw new EntityExistException(Menu.ENTITY_NAME,"name",resources.getName());
        }
        if (resources.getIFrame()){
            if (!(resources.getPath().toLowerCase().startsWith("http://")||resources.getPath().toLowerCase().startsWith("https://"))){
                throw new BadRequestException("外链必须以http://或者https://开头");
            }
        }
        Menu menu = menuRepository.save(resources);

        redisUtils.del("menu::pid:" + resources.getParentId());
        return menuMapper.toDto(menu);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Menu resources) {
        if (resources.getId().equals(resources.getParentId())){
            throw new BadRequestException("上级不能是自己");
        }
        Menu menu = menuRepository.findById(resources.getId()).orElse(null);
        ValidUtil.notNull(menu,Menu.ENTITY_NAME,"id",resources.getId());


        if(resources.getIFrame()) {
            if (!(resources.getPath().toLowerCase().startsWith("http://") || resources.getPath().toLowerCase().startsWith("https://"))) {
                throw new BadRequestException("外链必须以http://或者https://开头");
            }
        }

        Menu menu1=menuRepository.findByName(resources.getName());

        if (menu1!=null&&!menu1.getId().equals(menu.getId())) {
            throw new EntityExistException(Menu.ENTITY_NAME,"name",resources.getName());
        }
        // 记录的父节点ID

        Long oldPid = menu.getParentId();
        Long newPid = resources.getParentId();
        // 清理缓存
        delCaches(resources.getId(), oldPid, newPid);

        menuRepository.save(resources);

    }



    @Override
    public List<MenuDTO> queryAll(MenuQueryCriteria criteria) {
        return menuMapper.toDto(menuRepository.findAll((root, criteriaQuery,cb)-> QueryHelper.andPredicate(root,criteria,cb)));
    }

    @Override
    @Cacheable(key = "'id:' + #p0")
    public MenuDTO findById(long id) {
        Menu menu = menuRepository.findById(id).orElse(null);
        ValidUtil.notNull(menu, Menu.ENTITY_NAME,"id",id);
        return menuMapper.toDto(menu);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long[] ids) {
        Set<Menu> menuSet = new HashSet<>();
        for (Long id : ids) {
            List<Menu> menuList = findByPid(id);
            menuSet.add(findOne(id));
            menuSet =getDeleteMenus(menuList, menuSet);

        }
        // 对级联删除进行处理
        for (Menu menu : menuSet) {
            // 清理缓存
            delCaches(menu.getId(), menu.getParentId(), null);

            roleService.untiedMenu(menu);
            menuRepository.deleteById(menu.getId());
        }


    }

    @Override
    @SuppressWarnings("unchecked")
    @Cacheable(key = "'user:' + #p0")
    public List<MenuVo> findByUser(Long userId) {
        List<MenuDTO> menuDTOList = findByRoles(roleService.findByUsersId(userId));
        List<MenuDTO> menuDTOTree = (List<MenuDTO>) buildTree(menuDTOList).get("content");
        return buildMenus(menuDTOTree);
    }

    @Override
    public Object getMenuTree(List<Menu> menus) {
        LinkedList<Map<String,Object>> list = new LinkedList<>();
        menus.forEach(menu -> {
            if (menu!=null){
                List<Menu> menuList = menuRepository.findByParentId(menu.getId());
                Map<String,Object> map = new HashMap<>();
                map.put("id",menu.getId());
                map.put("label",menu.getName());
                if (menuList!=null&&menuList.size()!=0){
                    map.put("children",getMenuTree(menuList));
                }
                list.add(map);
            }
        });
        return list;
    }

    @Override
    @Cacheable(key = "'pid:'+#p0")
    public List<Menu> findByPid(long pid) {
        return menuRepository.findByParentId(pid);
    }

    @Override
    public Map<String,Object> buildTree(List<MenuDTO> menuDTOS) {
        List<MenuDTO> trees=new ArrayList<>();
        for (MenuDTO menuDTO : menuDTOS) {

            if ("0".equals(menuDTO.getParentId().toString())){
                trees.add(menuDTO);
            }

            for (MenuDTO dto : menuDTOS) {
                if (dto.getParentId().equals(menuDTO.getId())){
                    if (menuDTO.getChildren() == null){
                        menuDTO.setChildren(new ArrayList<>());
                    }
                    menuDTO.getChildren().add(dto);
                }
            }
        }
        Map<String,Object> map=new HashMap<>(2);
        map.put("content",trees.size()==0?menuDTOS:trees);
        map.put("totalElements", menuDTOS.size());
        return map;
    }

    @Override
    public List<MenuDTO> findByRoles(List<RoleSmallDTO> roles) {
        Set<Menu> menus=new LinkedHashSet<>();
        for (RoleSmallDTO role : roles) {
            List<Menu> menus1 = new ArrayList<>(menuRepository.findByRoles_IdOrderBySortAsc(role.getId()));
            menus.addAll(menus1);
        }
        return menus.stream().map(menuMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<MenuVo> buildMenus(List<MenuDTO> menuDTOS) {
        LinkedList<MenuVo> list = new LinkedList<>();
        menuDTOS.forEach(menuDTO -> {
                if (menuDTO!=null){
                    List<MenuDTO> menuDTOList=menuDTO.getChildren();
                    MenuVo menuVo= new MenuVo();
                    menuVo.setId(menuDTO.getId());
                    menuVo.setParentId(menuDTO.getParentId());
                    menuVo.setName(menuDTO.getName());
                    menuVo.setCache(menuDTO.getCache());
                    menuVo.setIFrame(menuDTO.getIFrame());
                    menuVo.setPath(menuDTO.getPath());
                    menuVo.setEnabled(menuDTO.getEnabled());
                    menuVo.setMeta(new MenuMetaVo(menuDTO.getName(),menuDTO.getIcon()));
                    //如果不是外链
                    if (!menuDTO.getIFrame()){
                        if (menuDTO.getParentId().equals(0L)){
                            //一级目录需要加斜杠, 不然访问 会跳转到404页面
                           if (menuDTO.getPath().startsWith("/")){
                               menuVo.setPath(menuDTO.getPath());
                           } else {
                               menuVo.setPath("/"+menuDTO.getPath());
                           }
                        }
                    }

                    if (menuDTOList!=null &&menuDTOList.size()!=0) {
                        menuVo.setAlwaysShow(true);
                        menuVo.setRedirect("noredirect");
                        menuVo.setChildren(buildMenus(menuDTOList));
                    }
                    menuVo.setComponent(StringUtils.isEmpty(menuDTO.getComponent())?"Layout":menuDTO.getComponent());
                    list.add(menuVo);
                }
            }

        );
        return list;
    }

    @Override
    public Menu findOne(Long id) {
        Menu menu = menuRepository.findById(id).orElse(null);
        ValidUtil.notNull(menu,Menu.ENTITY_NAME,"id",id);
        return menu;
    }

    @Override
    public Set<Menu> getDeleteMenus(List<Menu> menuList, Set<Menu> menuSet) {
        // 递归找出待删除的菜单
        for (Menu menu : menuList) {
            menuSet.add(menu);
            List<Menu> menus = menuRepository.findByParentId(menu.getId());
            if (CollectionUtil.isNotEmpty(menus)){
                getDeleteMenus(menus, menuSet);
            }
        }
        return menuSet;
    }

    @Override
    public void download(List<MenuDTO> queryAll, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (MenuDTO menuDTO : queryAll) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("菜单名称", menuDTO.getName());
            map.put("菜单主键", menuDTO.getComponent());
            map.put("外链菜单", menuDTO.getIFrame() ? "是" : "否");
            map.put("菜单可见", menuDTO.getEnabled() ? "否" : "是");
            map.put("是否缓存", menuDTO.getCache() ? "是" : "否");
            map.put("创建日期", menuDTO.getCreatedAt());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }


    /**
     * 清理缓存
     * @param id 菜单ID
     * @param oldPid 旧的菜单父级ID
     * @param newPid 新的菜单父级ID
     */
    public void delCaches(Long id, Long oldPid, Long newPid){
        List<User> users = userRepository.findByMenuId(id);
        redisUtils.del(CacheKey.MENU_ID +id);
        redisUtils.delByKeys(CacheKey.MENU_USER,users.stream().map(User::getId).collect(Collectors.toSet()));
        redisUtils.del("menu::pid:" + (oldPid == null ? 0 : oldPid));
        redisUtils.del("menu::pid:" + (newPid == null ? 0 : newPid));
        // 清除 Role 缓存
        List<RoleSmallDTO> roles = roleService.findInMenuId(new ArrayList<Long>(){{
            add(id);
            add(newPid == null ? 0 : newPid);
        }});
        redisUtils.delByKeys(CacheKey.ROLE_ID,roles.stream().map(RoleSmallDTO::getId).collect(Collectors.toSet()));
    }
}
