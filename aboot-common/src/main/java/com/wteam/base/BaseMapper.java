/*
 * Copyright © 2019-2020  Whale Cloud, Inc. All Rights Reserved.
 *
 * Notice: Whale Cloud Inc copyrights this specification.
 * No part of this specification may be reproduced in any form or means,
 *  without the prior written consent of Whale Cloud Inc.
 *
 */
package com.wteam.base;

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
    List<E> toEntity(List<D> dtoList);

    /**
     * Entity集合转DTO集合
     */
    List <D> toDto(List<E> entityList);
}
