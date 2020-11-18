package com.wteam.modules.system.service;

import com.wteam.domain.vo.JwtUser;
import com.wteam.modules.system.domain.User;
import com.wteam.modules.system.domain.criteria.UserQueryCriteria;
import com.wteam.modules.system.domain.dto.UserDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * 用户 业务层
 * @author mission
 * @since 2019/07/09 15:29
 */
public interface UserService {

    /**
     * get
     * @param id
     * @return
     */
    UserDTO findDTOById(long id);

    /**
     * create
     * @param resources
     * @return
     */
    UserDTO create(User resources);

    /**
     * update
     * @param resources
     */
    void update(User resources);

    /**
     * 编辑个人中心
     * @param resources
     */
    void updateCenter(User resources);
    /**
     * delete
     * @param ids
     */
    void delete(Set<Long> ids);

    /**
     * findByName
     * @param userName
     * @return
     */
    UserDTO findByName(String userName);

    /**
     * 修改密码
     * @param username
     * @param encryptPassword
     */
    void updatePass(String username, String encryptPassword);

    /**
     * 修改头像
     * @param username
     * @param url
     */
    void updateAvatar(String username, String url);

    /**
     * 修改邮箱
     * @param username
     * @param email
     */
    void updateEmail(String username, String email);

    /**
     * 查看用户列表
     * @param criteria
     * @param pageable
     * @return
     */
    Object queryAll(UserQueryCriteria criteria, Pageable pageable);


    Page<UserDTO> queryPage(UserQueryCriteria criteria, Pageable pageable);
    /**
     * 查看用户列表
     * @param criteria
     * @return
     */
    List<UserDTO> queryAll(UserQueryCriteria criteria);

    /**
     * 导出用户列表
     * @param queryAll /
     * @param response /
     * @throws IOException /
     */
    void download(List<UserDTO> queryAll, HttpServletResponse response) throws IOException;

    /**
     * 更新登录时间
     * @param jwtUser
     */
    void updateLoginTime(JwtUser jwtUser);



}
