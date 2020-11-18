package com.wteam.modules.system.repository;

import com.wteam.base.BaseRepository;
import com.wteam.modules.system.domain.Menu;

import java.util.LinkedHashSet;
import java.util.List;

/**
 * 菜单 存储层
 * @author mission
 * @since 2019/07/08 20:12
 */
public interface MenuRepository extends BaseRepository<Menu, Long> {

    /**
     * findByName
     * @param name
     * @return
     */
    Menu findByName(String name);

    /**
     * findByParentId
     * @param parentId
     * @return
     */
    List<Menu> findByParentId(long parentId);

    LinkedHashSet<Menu> findByRoles_IdOrderBySortAsc(Long roleId);
}
