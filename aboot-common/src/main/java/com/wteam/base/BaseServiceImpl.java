/*
 * copyleft © 2019-2021
 */
package com.wteam.base;

import com.wteam.utils.PageUtil;
import com.wteam.utils.QueryHelper;
import com.wteam.utils.RedisUtils;
import com.wteam.utils.ValidUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 通用业务实现层
 * @author mission
 * @since 2020/07/28 10:14
 */
@SuppressWarnings("uncheck")
@Slf4j
@Transactional
public class BaseServiceImpl<T,D,C,ID extends Serializable,BR extends BaseRepository<T,ID>,BM extends BaseMapper<D,T>> {


	@Resource
	public BR br;

	@Resource
	public BM bm;

	@Resource
	public RedisUtils redisUtils;

	public Class<?> clazz;

	@Transactional(rollbackFor = Exception.class)
	public void create(T t){
		br.save(t);
	}

	@Transactional(rollbackFor = Exception.class)
	public D update(T t){
		return bm.toDto(br.update(t,false));
	}

	public D findDTOById(ID id){
		T t = br.findById(id).orElse(null);
		ValidUtil.notNull(t,"查询","id",id);
		return bm.toDto(t);
	}

	public Map<String,Object> queryAll(C c, Pageable pageable){
		Page<T> page = br.findAll((root, cq, cb) -> QueryHelper.andPredicate(root, c, cb), pageable);
		return PageUtil.toPage(page.map(bm::toDto));
	}

	public List<D> queryAll(C c, Sort sort){
		return bm.toDto(br.findAll((root, cq, cb) -> QueryHelper.andPredicate(root, c, cb), sort));
	}

	@Transactional(rollbackFor = Exception.class)
	public void deleteById(Set<ID> ids){
		br.logicDeleteInBatchById(ids);
	}
}
