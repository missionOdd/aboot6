/*
 * Copyright © 2019-2020  Whale Cloud, Inc. All Rights Reserved.
 *
 * Notice: Whale Cloud Inc copyrights this specification.
 * No part of this specification may be reproduced in any form or means,
 *  without the prior written consent of Whale Cloud Inc.
 *
 */
package com.wteam.base;


import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 通用业务层
 * @author mission
 * @since 2020/07/28 10:14
 */
public interface BaseService <T,D,C,ID extends Serializable>{

	public void create(T t);

	public D update(T t);

	public D findDTOById(ID id);

	public Map<String,Object> queryAll(C c, Pageable pageable);

	public List<D> queryAll(C c, Sort sort);

	public void deleteById(Set<ID> ids);

}
