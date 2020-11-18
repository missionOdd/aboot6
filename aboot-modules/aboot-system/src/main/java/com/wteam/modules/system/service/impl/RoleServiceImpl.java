package com.wteam.modules.system.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.wteam.exception.BadRequestException;
import com.wteam.exception.EntityExistException;
import com.wteam.modules.security.service.UserCacheClean;
import com.wteam.modules.system.config.CacheKey;
import com.wteam.modules.system.domain.Menu;
import com.wteam.modules.system.domain.Permission;
import com.wteam.modules.system.domain.Role;
import com.wteam.modules.system.domain.User;
import com.wteam.modules.system.domain.criteria.RoleQueryCriteria;
import com.wteam.modules.system.domain.dto.RoleDTO;
import com.wteam.modules.system.domain.dto.RoleSmallDTO;
import com.wteam.modules.system.domain.mapper.RoleMapper;
import com.wteam.modules.system.domain.mapper.RoleSmallMapper;
import com.wteam.modules.system.repository.RoleRepository;
import com.wteam.modules.system.repository.UserRepository;
import com.wteam.modules.system.service.RoleService;
import com.wteam.utils.*;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author mission
 * @since 2019/07/09 15:42
 */
@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "role")
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class RoleServiceImpl implements RoleService {


    private final RoleRepository roleRepository;

    private final RoleMapper roleMapper;

    private final RoleSmallMapper roleSmallMapper;

    private final RedisUtils redisUtils;

    private final UserRepository userRepository;

    private final UserCacheClean userCacheClean;


    @Override
    @Cacheable(key = "'id:' + #p0")
    public RoleDTO findDTOById(long id) {
        Role role = roleRepository.findById(id).orElse(null);
        ValidUtil.notNull(role,Role.ENTITY_NAME,"id",id);
        return roleMapper.toDto(role);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RoleDTO create(Role resources) {
        if (roleRepository.findByName(resources.getName())!=null){
            throw new EntityExistException(Role.ENTITY_NAME,"name",resources.getName());
        }
        Role role = roleRepository.save(resources);
        return roleMapper.toDto(role);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Role resources) {
        Role role = roleRepository.findById(resources.getId()).orElse(null);
        ValidUtil.notNull(role,Role.ENTITY_NAME,"id",resources.getId());
        assert role != null;

        Role role1=roleRepository.findByName(resources.getName());

        if (role1!=null && !role1.getId().equals(role.getId())) {
            throw new EntityExistException(Role.ENTITY_NAME,"name",resources.getName());
        }

        role.setName(resources.getName());
        role.setRemark(resources.getRemark());
        role.setDataScope(resources.getDataScope());
        role.setDepts(resources.getDepts());
        role.setLevel(resources.getLevel());
        role.setAuthority(resources.getAuthority());

        // 更新相关缓存
        delCaches(role.getId(), null);
        roleRepository.save(role);


    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Set<Long> ids) {
        //验证关联用户
        verification(ids);
        for (Long id : ids) {
            roleRepository.deleteById(id);
            // 更新相关缓存
            delCaches(id, null);
        }
    }

    /**
     * 验证关联用户
     * @param ids /
     */
    public void verification(Set<Long> ids) {
        if (userRepository.countByRoles(ids) > 0) {
            throw new BadRequestException("所选角色存在用户关联，请解除关联再试！");
        }
    }

    @Override
    @Cacheable(key = "'user:' + #p0")
    public List<RoleSmallDTO> findByUsersId(Long id) {
        return roleSmallMapper.toDto(new ArrayList<>(roleRepository.findByUsers_Id(id)));
    }

    @Override
    public Integer findLevelByUser(User user) {
        Set<Role> roles=user.getRoles();
        //情况1: 编辑用户，角色无
        if (CollectionUtils.isEmpty(roles) && user.getId()!=null) {
            List<RoleSmallDTO> roleList = findByUsersId(user.getId());
            return Collections.min(roleList.stream().map(RoleSmallDTO::getLevel).collect(Collectors.toList()));
        }

        //情况2: 新增或编辑用户，角色必须有
        Set<RoleDTO> roleDTOS=new HashSet<>();
        for (Role role : roles) {
            roleDTOS.add(findDTOById(role.getId())) ;
        }
        return Collections.min(roleDTOS.stream().map(RoleDTO::getLevel).collect(Collectors.toList()));
    }

    @Override
    public List<RoleSmallDTO> findInMenuId(ArrayList<Long> menuIds) {
        return roleSmallMapper.toDto(roleRepository.findInMenuId(menuIds));
    }

    @Override
    public List<RoleSmallDTO> findInPermissionId(ArrayList<Long> permissionIds) {
        return roleSmallMapper.toDto(roleRepository.findInPermissionId(permissionIds));

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePermission(Role resources) {
        Role role = roleRepository.findById(resources.getId()).orElse(null);
        ValidUtil.notNull(role,Role.ENTITY_NAME,"id",resources.getId());
        assert role != null;

        role.setPermissions(resources.getPermissions());
        //清理缓存
        delCaches(role.getId(),null);
        roleRepository.save(role);

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateMenu(Role resources) {
        Role role = roleRepository.findById(resources.getId()).orElse(null);
        ValidUtil.notNull(role,Role.ENTITY_NAME,"id",resources.getId());
        assert role != null;
        role.setMenus(resources.getMenus());
        roleRepository.save(role);

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void untiedMenu(Menu menu) {
        Set<Role> roles = roleRepository.findByMenus_Id(menu.getId());
        for (Role role : roles) {

            menu.getRoles().remove(role);
            role.getMenus().remove(menu);
            //清理缓存
            delCaches(role.getId(),null);
            roleRepository.save(role);

        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void untiedPermission(Permission permission) {
        Set<Role> roles = roleRepository.findByMenus_Id(permission.getId());
        for (Role role : roles) {

            permission.getRoles().remove(role);
            role.getMenus().remove(permission);
            //清理缓存
            delCaches(role.getId(),null);
            roleRepository.save(role);


        }
    }

    @Override
    public Object queryAll(Pageable pageable) {
        return roleMapper.toDto(roleRepository.findAll(pageable).getContent());
    }

    @Override
    public Map<String, Object> queryAll(RoleQueryCriteria criteria, Pageable pageable) {
        Page<Role> page=roleRepository.findAll((root, cq, cb)-> QueryHelper.andPredicate(root,criteria,cb),pageable);
        return PageUtil.toPage(page.map(roleMapper::toDto));
    }

    @Override
    public List<RoleDTO> queryAll(RoleQueryCriteria criteria) {
        return roleMapper.toDto(roleRepository.findAll((root,cq,cb)-> QueryHelper.andPredicate(root,criteria,cb)));
    }

    @Override
    public void download(List<RoleDTO> roles, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (RoleDTO role : roles) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("角色名称", role.getName());
            map.put("数据权限", role.getDataScope());
            map.put("角色级别", role.getLevel());
            map.put("描述", role.getRemark());
            map.put("创建日期", role.getCreatedAt());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    /**
     * 清理缓存
     */
    public void delCaches(Long id, List<User> users) {
        users = CollectionUtil.isEmpty(users) ? userRepository.findByRoleId(id) : users;
        if (CollectionUtil.isNotEmpty(users)) {
            users.forEach(item -> userCacheClean.cleanUserCache(item.getUsername()));
            Set<Long> userIds = users.stream().map(User::getId).collect(Collectors.toSet());
            redisUtils.delByKeys(CacheKey.DATA_USER, userIds);
            redisUtils.delByKeys(CacheKey.ROLE_USER, userIds);
            redisUtils.delByKeys(CacheKey.MENU_USER, userIds);
            redisUtils.delByKeys(CacheKey.PERMISSION_USER, userIds);
        }
        redisUtils.del(CacheKey.ROLE_ID + id);
    }

}
