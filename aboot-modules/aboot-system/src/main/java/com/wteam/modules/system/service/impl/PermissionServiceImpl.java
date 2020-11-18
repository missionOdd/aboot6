package com.wteam.modules.system.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.wteam.exception.BadRequestException;
import com.wteam.exception.EntityExistException;
import com.wteam.modules.security.service.UserCacheClean;
import com.wteam.modules.system.config.CacheKey;
import com.wteam.modules.system.domain.Permission;
import com.wteam.modules.system.domain.User;
import com.wteam.modules.system.domain.criteria.PermissionQueryCriteria;
import com.wteam.modules.system.domain.dto.PermissionDTO;
import com.wteam.modules.system.domain.dto.RoleSmallDTO;
import com.wteam.modules.system.domain.mapper.PermissionMapper;
import com.wteam.modules.system.repository.PermissionRepository;
import com.wteam.modules.system.repository.UserRepository;
import com.wteam.modules.system.service.PermissionService;
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

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 权限 业务层实现类
 * @author mission
 * @since 2019/07/13 8:37
 */
@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "permission")
@Transactional( readOnly = true, rollbackFor = Exception.class)
public class PermissionServiceImpl implements PermissionService {

    private final PermissionRepository permissionRepository;

    private final PermissionMapper permissionMapper;

    private final RedisUtils redisUtils;

    private final RoleService roleService;

    private final UserRepository userRepository;

    private final UserCacheClean userCacheClean;


    @Override
    @Cacheable(key = "'id:' + #p0")
    public PermissionDTO findDTOById(long id) {
        Permission permission = permissionRepository.findById(id).orElse(null);
        ValidUtil.notNull(permission, Permission.ENTITY_NAME,"id",id);
        return permissionMapper.toDto(permission);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PermissionDTO create(Permission resources) {
        if (permissionRepository.findByName(resources.getName())!=null){
            throw  new EntityExistException(Permission.ENTITY_NAME,"name",resources.getName());
        }
        redisUtils.del("menu::pid:" + resources.getParentId());
        return permissionMapper.toDto(permissionRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Permission resources) {
        if (resources.getId().equals(resources.getParentId())) {
            throw new BadRequestException("上级不能是自己");
        }

        Permission permission = permissionRepository.findById(resources.getId()).orElse(null);
        ValidUtil.notNull(permission,Permission.ENTITY_NAME,"id",resources.getId());
        assert permission != null;

        Permission permission1 = permissionRepository.findByName(resources.getName());
        if (permission1 != null && !permission1.getId().equals(permission.getId())){
            throw new EntityExistException(Permission.ENTITY_NAME,"name",resources.getName());
        }
        Long oldPid = permission.getParentId();
        Long newPid = resources.getParentId();
        // 更新相关缓存
        delCaches(permission.getId(), oldPid,newPid,null);

        permission.setName(resources.getName());
        permission.setAlias(resources.getAlias());
        permission.setParentId(resources.getParentId());
        permissionRepository.save(permission);


    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long[] ids) {
        Set<Permission> permissionSet = new HashSet<>();
        for (Long id : ids) {
            List<Permission> permissionList = findByPid(id);
            permissionSet.add(permissionRepository.findById(id).orElse(null));
            permissionSet = getDeletePermissions(permissionList, permissionSet);
        }
        for (Permission permission : permissionSet) {
            //清空缓存
            delCaches(permission.getId(),permission.getId(),null, null);

            roleService.untiedPermission(permission);
            permissionRepository.deleteById(permission.getId());
        }


    }

    @Override
    public Set<Permission> getDeletePermissions(List<Permission> permissionList, Set<Permission> permissionSet) {
        // 递归找出待删除的菜单
        for (Permission permission: permissionList) {
            permissionSet.add(permission);
            List<Permission>  permissions= findByPid(permission.getId());
            if (CollectionUtil.isNotEmpty(permissions)){
                getDeletePermissions(permissions, permissionSet);
            }
        }
        return permissionSet;
    }

    @Override
    public Object getPermissionTree(List<Permission> permissions) {
        List<Map<String,Object>> list = new LinkedList<>();
        permissions.forEach(permission -> {
                if (permission!=null){
                    List<Permission> permissionList = permissionRepository.findByParentId(permission.getId());
                    Map<String,Object> map = new HashMap<>();
                    map.put("id",permission.getId());
                    map.put("label",permission.getAlias());
                    if(permissionList!=null && permissionList.size()!=0){
                        map.put("children",getPermissionTree(permissionList));
                    }
                    list.add(map);
                }
            }
        );
        return list;
    }

    @Override
    @Cacheable(key = "'pid:'+#p0")
    public List<Permission> findByPid(long pid) {
        return permissionRepository.findByParentId(pid);
    }

    @Override
    public Object buildTree(List<PermissionDTO> permissionDTOS) {
        List<PermissionDTO> trees=new ArrayList<>();

        for (PermissionDTO permissionDTO : permissionDTOS) {

            if ("0".equals(permissionDTO.getParentId().toString())) {
                trees.add(permissionDTO);
            }
            for (PermissionDTO dto : permissionDTOS) {
                if (dto.getParentId().equals(permissionDTO.getId())) {
                    if (permissionDTO.getChildren() == null) {
                        permissionDTO.setChildren(new ArrayList<>());
                    }
                    permissionDTO.getChildren().add(dto);
                }
            }
        }
        Integer totalElements = permissionDTOS.size();
        Map<String,Object> map=new HashMap<>(2);
        map.put("content",trees.size() == 0? permissionDTOS: trees);
        map.put("totalElements",totalElements);
        return map;
    }

    @Override
    public List<PermissionDTO> queryAll(PermissionQueryCriteria criteria) {
        return permissionMapper.toDto(permissionRepository.findAll((root,cq,cb)->
            QueryHelper.andPredicate(root,criteria,cb)));
    }

    @Override
    public void download(List<PermissionDTO> queryAll, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (PermissionDTO permissionDTO : queryAll) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("权限名", permissionDTO.getName());
            map.put("权限别名", permissionDTO.getAlias());
            map.put("创建日期", permissionDTO.getCreatedAt());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    /**
     * 清理缓存
     * @param id /
     */
    public void delCaches(Long id,Long oldPid, Long newPid, List<User> users) {
        users = CollectionUtil.isEmpty(users) ? userRepository.findByPermissionId(id) : users;
        if (CollectionUtil.isNotEmpty(users)) {
            users.forEach(item -> userCacheClean.cleanUserCache(item.getUsername()));
            Set<Long> userIds = users.stream().map(User::getId).collect(Collectors.toSet());
            redisUtils.delByKeys(CacheKey.PERMISSION_USER, userIds);

            redisUtils.del("permission::pid:" + (oldPid == null ? 0 : oldPid));
            redisUtils.del("permission::pid:" + (newPid == null ? 0 : newPid));
            // 清除 Role 缓存
            List<RoleSmallDTO> roles = roleService.findInPermissionId(new ArrayList<Long>(){{
                add(id);
                add(newPid == null ? 0 : newPid);
            }});
            redisUtils.delByKeys(CacheKey.ROLE_ID,roles.stream().map(RoleSmallDTO::getId).collect(Collectors.toSet()));

        }
        redisUtils.del(CacheKey.PERMISSION_ID + id);
    }
}
