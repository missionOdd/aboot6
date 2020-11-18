package com.wteam.modules.system.service.impl;


import cn.hutool.core.collection.CollectionUtil;
import com.wteam.exception.BadRequestException;
import com.wteam.modules.system.config.CacheKey;
import com.wteam.modules.system.domain.Dept;
import com.wteam.modules.system.domain.User;
import com.wteam.modules.system.domain.criteria.DeptQueryCriteria;
import com.wteam.modules.system.domain.dto.DeptDTO;
import com.wteam.modules.system.domain.mapper.DeptMapper;
import com.wteam.modules.system.repository.DeptRepository;
import com.wteam.modules.system.repository.RoleRepository;
import com.wteam.modules.system.repository.UserRepository;
import com.wteam.modules.system.service.DeptService;
import com.wteam.utils.FileUtil;
import com.wteam.utils.QueryHelper;
import com.wteam.utils.RedisUtils;
import com.wteam.utils.ValidUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 部门 业务实现层
 * @author mission
 * @since 2019/07/13 10:18
 */
@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "dept")
@Transactional( readOnly = true, rollbackFor = Exception.class)
public class DeptServiceImpl  implements DeptService {

    private final DeptRepository deptRepository;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final DeptMapper deptMapper;
    private final RedisUtils redisUtils;

    @Override
    public List<DeptDTO> queryAll(DeptQueryCriteria criteria) {
        return deptMapper.toDto(deptRepository.findAll((root,cq,cb)-> QueryHelper.andPredicate(root,criteria,cb)));
    }

    @Override
    @Cacheable(key = "'id:' + #p0")
    public DeptDTO findDTOById(Long id) {
        Dept dept = deptRepository.findById(id).orElse(null);
        ValidUtil.notNull(dept, Dept.ENTITY_NAME,"id",id);
        return deptMapper.toDto(dept);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public DeptDTO create(Dept resources) {
        Dept dept = deptRepository.save(resources);
        // 清理缓存
        redisUtils.del("dept::pid:" + resources.getParentId());

        return deptMapper.toDto(dept);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Dept resources) {
        if (resources.getId().equals(resources.getParentId())){
            throw new BadRequestException("上级不能为自己");
        }
        Dept dept = deptRepository.findById(resources.getId()).orElse(null);
        ValidUtil.notNull(dept,Dept.ENTITY_NAME,"id",resources.getId());
        assert dept != null;
        resources.setId(dept.getId());
        // 旧的部门
        Long oldPid = dept.getParentId();
        Long newPid = resources.getParentId();
        // 清理缓存
        delCaches(resources.getId(), oldPid, newPid);

        deptRepository.save(resources);


    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long[] ids) {
        Set<Dept> deptSet = new HashSet<>();
        for (Long id : ids) {
            List<Dept> deptList = findByPid(id);
            deptSet.add(deptRepository.findById(id).orElse(null));
            if(CollectionUtil.isNotEmpty(deptList)){
                deptSet = getDeleteDepts(deptList, deptSet);
            }
        }
        //验证是否被角色或用户关联
        verification(deptSet);
        for (Dept dept : deptSet) {
            //清理缓存
            delCaches(dept.getId(), dept.getParentId(), null);

            deptRepository.logicDelete(dept.getId());
        }

    }

    /**
     * 验证是否被角色或用户关联
     */
    private void verification(Set<Dept> deptSet) {
        Set<Long> deptIds = deptSet.stream().map(Dept::getId).collect(Collectors.toSet());
        if(userRepository.countByDepts(deptIds) > 0){
            throw new BadRequestException("所选部门存在用户关联，请解除后再试！");
        }
        if(roleRepository.countByDepts(deptIds) > 0){
            throw new BadRequestException("所选部门存在角色关联，请解除后再试！");
        }
    }

    @Override
    public Object buildTree(List<DeptDTO> deptDTOS) {
        Set<DeptDTO> trees =new LinkedHashSet<>();
        Set<DeptDTO> depts=new LinkedHashSet<>();
        List<String> deptNames = deptDTOS.stream().map(DeptDTO::getName).collect(Collectors.toList());
        boolean isChild;
        for (DeptDTO deptDTO : deptDTOS) {
            isChild =false;
            if ("0".equals(deptDTO.getParentId().toString())){
                trees.add(deptDTO);
            }
            for (DeptDTO it : deptDTOS) {
                if (it.getParentId().equals(deptDTO.getId())) {
                    isChild=true;
                    if (deptDTO.getChildren() == null){
                        deptDTO.setChildren(new ArrayList<>());
                    }
                    deptDTO.getChildren().add(it);
                }
            }
            if (isChild){
                depts.add(deptDTO);
            }else if (!deptNames.contains(deptRepository.findNameById(deptDTO.getParentId()))){
                depts.add(deptDTO);
            }
        }
        if (CollectionUtils.isEmpty(trees)) {
            trees = depts;
        }

        Map<String,Object> map = new HashMap<>(2);
        map.put("totalElements",deptDTOS.size());
        map.put("content", CollectionUtils.isEmpty(trees)?deptDTOS:trees);
        return map;
    }

    @Override
    @Cacheable(key = "'pid:' + #p0")
    public List<Dept> findByPid(long pid) {
        return deptRepository.findByParentId(pid);
    }

    @Override
    public Set<Dept> findByRoleIds(Long id) {
        return deptRepository.findByRoles_Id(id);
    }

    @Override
    public Set<Dept> getDeleteDepts(List<Dept> deptList, Set<Dept> deptSet) {
        for (Dept dept : deptList) {
            deptSet.add(dept);
            List<Dept> depts = deptRepository.findByParentId(dept.getId());
            if (CollectionUtil.isNotEmpty(depts)) {
                getDeleteDepts(depts,deptSet);
            }
        }
        return deptSet;
    }

    @Override
    public void download(List<DeptDTO> queryAll, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (DeptDTO deptDTO : queryAll) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("部门名称", deptDTO.getName());
            map.put("部门状态", deptDTO.getEnabled() ? "启用" : "停用");
            map.put("创建日期", deptDTO.getCreatedAt());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);

    }



    /**
     * 清理缓存
     * @param id /
     * @param oldPid /
     * @param newPid /
     */
    public void delCaches(Long id, Long oldPid, Long newPid){
        List<User> users = userRepository.findByDeptRoleId(id);
        // 删除数据权限
        redisUtils.delByKeys(CacheKey.DATA_USER,users.stream().map(User::getId).collect(Collectors.toSet()));
        redisUtils.del("dept::id:" + id);
        redisUtils.del("dept::pid:" + (oldPid == null ? 0 : oldPid));
        redisUtils.del("dept::pid:" + (newPid == null ? 0 : newPid));
    }
}
