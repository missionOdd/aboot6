package com.wteam.modules.system.service.impl;


import com.wteam.domain.vo.JwtUser;
import com.wteam.exception.BadRequestException;
import com.wteam.exception.EntityExistException;
import com.wteam.exception.EntityNotFoundException;
import com.wteam.modules.miniapp.service.WxUserService;
import com.wteam.modules.security.service.OnlineUserService;
import com.wteam.modules.security.service.UserCacheClean;
import com.wteam.modules.system.config.CacheKey;
import com.wteam.modules.system.config.LoginType;
import com.wteam.modules.system.domain.User;
import com.wteam.modules.system.domain.criteria.UserQueryCriteria;
import com.wteam.modules.system.domain.dto.RoleSmallDTO;
import com.wteam.modules.system.domain.dto.UserDTO;
import com.wteam.modules.system.domain.mapper.UserMapper;
import com.wteam.modules.system.repository.UserRepository;
import com.wteam.modules.system.service.UserService;
import com.wteam.utils.*;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 用户 业务实现层
 * @author mission
 * @since 2019/07/09 15:42
 */
@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "user")
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final RedisUtils redisUtils;

    private final UserCacheClean userCacheClean;

    private final OnlineUserService onlineUserService;

    private final WxUserService wxUserService;


    @Override
    @Cacheable(key = "'id:' + #p0")
    public UserDTO findDTOById(long id) {
        User user = userRepository.findById(id).orElse(null);
        ValidUtil.notNull(user,User.ENTITY_NAME,"id",id);
        return userMapper.toDto(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserDTO create(User resources) {
        //账号校验
        if (userRepository.findByUsername(resources.getUsername())!=null) {
            throw new EntityExistException(User.ENTITY_NAME,"username",resources.getUsername());
        }
        //邮箱校验
        if (StringUtils.isNotBlank(resources.getEmail())&&userRepository.findByEmail(resources.getEmail())!=null) {
            throw new EntityExistException("邮箱","email",resources.getEmail());
        }
        //默认激活
        if (resources.getEnabled()==null) {
            resources.setEnabled(true);
        }

        resources.setLastPasswordResetTime(Timestamp.valueOf(LocalDateTime.now()));
        return userMapper.toDto(userRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(User resources) {
        User user=userRepository.findById(resources.getId()).orElse(null);
        ValidUtil.notNull(user,User.ENTITY_NAME,"id",resources.getId());

        //账号校验
        if (StringUtils.isNotBlank(resources.getUsername())) {
            User user1=userRepository.findByUsername(resources.getUsername());
            if (user1 !=null && !user.getId().equals(user1.getId())) {
                throw new EntityExistException(User.ENTITY_NAME,"username",resources.getUsername());
            }
        }

        //邮箱校验
        if (StringUtils.isNotBlank(resources.getEmail())) {
            User user2 = userRepository.findByEmail(resources.getEmail());
            if (user2 != null && !user.getId().equals(user2.getId())) {
                throw new EntityExistException("邮箱", "email", resources.getEmail());
            }
        }

        // 清理缓存
        delCaches(user.getId(), user.getUsername());
        //如果用户的角色改变了, 需要手动清理下缓存
        if (resources.getRoles()!=null&&!resources.getRoles().equals(user.getRoles())) {
            redisUtils.del(CacheKey.DATA_USER + resources.getId());
            redisUtils.del(CacheKey.ROLE_USER + resources.getId());
            redisUtils.del(CacheKey.MENU_USER + resources.getId());
            redisUtils.del(CacheKey.PERMISSION_USER + resources.getId());
        }


        // 如果用户被禁用，则清除用户登录信息
        if(resources.getEnabled()!=null&&!resources.getEnabled()){
            onlineUserService.kickOutForUsername(resources.getUsername());
        }
        User updateUser = new User();
        updateUser.setId(user.getId());
        updateUser.setUsername(resources.getUsername());
        updateUser.setNickname(resources.getNickname());
        updateUser.setAvatar(resources.getAvatar());
        updateUser.setEnabled(resources.getEnabled());
        updateUser.setRoles(resources.getRoles());
        updateUser.setDept(resources.getDept());
        updateUser.setJob(resources.getJob());
        updateUser.setPhone(resources.getPhone());
        updateUser.setSex(resources.getSex());
        userRepository.save(updateUser);

    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateCenter(User resources) {
        User user = userRepository.findById(resources.getId()).orElseGet(User::new);

        // 清理缓存
        delCaches(user.getId(), user.getUsername());

        User updateUser = new User();
        updateUser.setId(user.getId());
        updateUser.setNickname(resources.getNickname());
        updateUser.setAvatar(resources.getAvatar());
        updateUser.setPhone(resources.getPhone());
        updateUser.setSex(resources.getSex());
        userRepository.save(updateUser);


    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Set<Long> ids) {
        Set<Long> uids = new HashSet<>();
        for (Long id : ids) {
            // 清理缓存
            UserDTO user = findDTOById(id);
            if (user.getLoginType().equals(LoginType.LOGIN_WX)) {
                uids.add(user.getId());
            }
            delCaches(user.getId(), user.getUsername());
            onlineUserService.kickOutForUsername(user.getUsername());
        }
        wxUserService.deleteAll(uids.toArray(new Long[0]));
        userRepository.logicDeleteInBatchById(ids);
    }

    @Override
    @Cacheable(key = "'loadUserByUsername:'+#p0")
    public UserDTO findByName(String userName) {
        User user= null;
        if (ValidUtil.isEmail(userName)) {
            user=userRepository.findByEmail(userName);
        }else {
            user =userRepository.findByUsername(userName);
        }

        if (user == null){
            throw  new EntityNotFoundException(User.ENTITY_NAME,"name",userName);
        }else {
            return userMapper.toDto(user);
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePass(String username, String pass) {
        redisUtils.del(CacheKey.USER_NAME+username);
        flushCache(username);
        userRepository.updatePass(username,pass, Timestamp.valueOf(LocalDateTime.now()));

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateAvatar(String username, String url) {
        redisUtils.del(CacheKey.USER_NAME+username);
        flushCache(username);
        userRepository.updateAvatar(username,url);

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateEmail(String username, String email) {
        if (userRepository.findByEmail(email)!=null) {
            throw new BadRequestException("该邮箱已经被注册");
        }
        flushCache(username);
        userRepository.updateEmail(username,email);

    }

    @Override
    public Object queryAll(UserQueryCriteria criteria, Pageable pageable){
        return PageUtil.toPage(userRepository.findAll((root, cq, cb) ->
            QueryHelper.andPredicate(root, criteria, cb),pageable)
        .map(userMapper::toDto));
    }

    @Override
    public Page<UserDTO> queryPage(UserQueryCriteria criteria, Pageable pageable) {
        return userRepository.findAll((root, cq, cb) ->
                QueryHelper.andPredicate(root, criteria, cb),pageable)
                .map(userMapper::toDto);
    }


    @Override
    public List<UserDTO> queryAll(UserQueryCriteria criteria) {
        List<User> users = userRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelper.andPredicate(root,criteria,criteriaBuilder));
        return userMapper.toDto(users);
    }

    @Override
    public void download(List<UserDTO> queryAll, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (UserDTO userDTO : queryAll) {
            List<String> roles = userDTO.getRoles().stream().map(RoleSmallDTO::getName).collect(Collectors.toList());
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("用户名", userDTO.getUsername());
            map.put("昵称", userDTO.getNickname());
            map.put("头像", userDTO.getAvatar());
            map.put("邮箱", userDTO.getEmail());
            map.put("状态", userDTO.getEnabled() ? "启用" : "禁用");
            map.put("手机号码", userDTO.getPhone());
            map.put("角色", roles);
            map.put("部门", userDTO.getDept().getName());
            map.put("岗位", userDTO.getJob().getName());
            map.put("最后修改密码的时间", userDTO.getLastPasswordResetTime());
            map.put("创建日期", userDTO.getCreatedAt());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateLoginTime(JwtUser jwtUser) {
        userRepository.updateLoginTime(jwtUser.getUsername(), Timestamp.valueOf(LocalDateTime.now()));
    }


    /**
     * 清理缓存
     *
     * @param id /
     */
    public void delCaches(Long id, String username) {
        redisUtils.del(CacheKey.USER_ID + id);
        redisUtils.del(CacheKey.USER_NAME + username);
        redisUtils.del("wxUser::openId:"+ username);
        flushCache(username);
    }

    /**
     * 清理 登陆时 用户缓存信息
     *
     * @param username /
     */
    private void flushCache(String username) {
        userCacheClean.cleanUserCache(username);
    }

}
