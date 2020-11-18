package com.wteam.modules.system.service;


import com.wteam.modules.system.domain.Menu;
import com.wteam.modules.system.domain.criteria.MenuQueryCriteria;
import com.wteam.modules.system.domain.dto.MenuDTO;
import com.wteam.modules.system.domain.dto.RoleSmallDTO;
import com.wteam.modules.system.domain.vo.MenuVo;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 菜单 业务层
 * @author mission
 * @since 2019/07/08 21:16
 */
public interface MenuService {

    /**
     * create
     * @param resources
     * @return
     */
    MenuDTO create(Menu resources);

    /**
     * update
     * @param resources
     */
    void update(Menu resources);

    /**
     * queryAll
     * @param criteria
     * @return
     */
    List<MenuDTO> queryAll(MenuQueryCriteria criteria);

    /**
     * get
     * @param id /
     * @return /
     */
    MenuDTO findById(long id);



    /**
     * delete
     * @param ids /
     */
    void delete(Long[] ids);

    /**
     * 构建用户菜单
     * @param userId 当前登录用户
     */
    List<MenuVo> findByUser(Long userId);

    /**
     * permission tree
     * @return
     */
    Object getMenuTree(List<Menu> menus);

    /**
     * findByPid
     * @param pid
     * @return
     */
    List<Menu> findByPid(long pid);

    /**
     * build Tree
     * @param menuDTOS
     * @return
     */
    Map<String,Object> buildTree(List<MenuDTO> menuDTOS);

    /**
     * findByRoles
     * @param roles /
     * @return /
     */
    List<MenuDTO> findByRoles(List<RoleSmallDTO> roles);

    /**
     * buildMenus
     * @param byRoles /
     * @return /
     */
    List<MenuVo> buildMenus(List<MenuDTO> byRoles);

    /**
     * 获取菜单
     * @param id
     * @return
     */
    Menu findOne(Long id);

    /**
     * 获取待删除的菜单
     * @param menuList /
     * @param menuSet /
     * @return /
     */
    Set<Menu> getDeleteMenus(List<Menu> menuList, Set<Menu> menuSet);
    /**
     * 导出
     * @param queryAll 待导出的数据
     * @param response /
     * @throws IOException /
     */
    void download(List<MenuDTO> queryAll, HttpServletResponse response) throws IOException;


}
