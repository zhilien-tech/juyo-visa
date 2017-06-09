/**
 * CustomerViewService.java
 * io.znz.jsite.visa.service
 * Copyright (c) 2017, 北京科技有限公司版权所有.
*/

package io.znz.jsite.visa.service;

import io.znz.jsite.base.NutzBaseService;
import io.znz.jsite.visa.bean.entity.CustomerManageEntity;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.google.common.collect.Maps;

/**
 * 客户管理业务层
 * @author   崔建斌
 * @Date	 2017年6月8日 	 
 */
@Service
public class CustomerViewService extends NutzBaseService<CustomerManageEntity> {

	/**
	 * 查询出客户管理的数据
	 */
	public Object customerListData() {
		Map<String, Object> map = Maps.newHashMap();
		List<CustomerManageEntity> queryList = dbDao.query(CustomerManageEntity.class, null, null);
		map.put("queryList", queryList);
		return map;
	}

	/**
	 * 添加客户信息
	 * @param addForm 
	 */
	public Object addSaveDate(CustomerManageEntity addForm) {
		Map<String, Object> obj = Maps.newHashMap();

		return obj;
	}

	/**
	 * 修改客户信息
	 * @param updateForm 
	 */
	public Object updateSaveDate(CustomerManageEntity updateForm) {
		Map<String, Object> obj = Maps.newHashMap();

		return obj;
	}
}
