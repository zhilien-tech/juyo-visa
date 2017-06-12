/**
 * CustomerViewService.java
 * io.znz.jsite.visa.service
 * Copyright (c) 2017, 北京科技有限公司版权所有.
*/

package io.znz.jsite.visa.service;

import io.znz.jsite.base.NutzBaseService;
import io.znz.jsite.visa.entity.customer.CustomerManageEntity;
import io.znz.jsite.visa.forms.customerform.CustomerUpdateForm;

import java.util.Date;
import java.util.List;

import org.nutz.dao.Cnd;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.uxuexi.core.common.util.Util;

/**
 * 客户管理业务层
 * @author   崔建斌
 * @Date	 2017年6月8日 	 
 */
@Service
public class CustomerViewService extends NutzBaseService<CustomerManageEntity> {

	/**
	 * 回显客户信息数据
	 * @param cid 
	 */
	public Object updateDate(long cid) {
		CustomerManageEntity one = dbDao.fetch(CustomerManageEntity.class, cid);
		return one;
	}

	/**
	 * 编辑保存
	 * @param updateForm
	 * @return 
	 */
	public Object updateDataSave(CustomerUpdateForm updateForm) {
		//查询出当前数据库中已存在的数据
		List<CustomerManageEntity> before = dbDao.query(CustomerManageEntity.class,
				Cnd.where("id", "=", updateForm.getId()), null);
		//欲更新为
		List<CustomerManageEntity> after = Lists.newArrayList();
		if (!Util.isEmpty(before)) {
			for (CustomerManageEntity t : before) {
				CustomerManageEntity m = new CustomerManageEntity();
				m.setFullComName(updateForm.getFullComName());
				m.setCustomerSource(updateForm.getCustomerSource());
				m.setLinkman(updateForm.getLinkman());
				m.setTelephone(updateForm.getTelephone());
				m.setEmail(updateForm.getEmail());
				m.setCreateTime(t.getCreateTime());
				m.setUpdateTime(new Date());
				after.add(m);
			}
		}
		dbDao.updateRelations(before, after);
		return null;
	}
}
