/*
 * copyleft © 2019-2021
 */
package com.wteam.base;

import java.util.Collection;
import java.util.List;

/**
 * mapstruct通用转换
 * @author mission
 * @since 2019/07/07 16:31
 */
public interface BaseMapper<D,E> {

    /**
     * DTO转Entity
     */
    E toEntity(D dto);

    /**
     * Entity转DTO
     */
    D toDto(E entity);

    /**
     * DTO集合转Entity集合
     */
    List<E> toEntity(Collection<D> dtoList);

    /**
     * Entity集合转DTO集合
     */
    List <D> toDto(Collection<E> entityList);
}
