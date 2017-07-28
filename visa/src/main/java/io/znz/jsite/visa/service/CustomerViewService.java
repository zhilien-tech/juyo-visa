/**
 * CustomerViewService.java
 * io.znz.jsite.visa.service
 * Copyright (c) 2017, 北京科技有限公司版权所有.
*/

package io.znz.jsite.visa.service;

import io.znz.jsite.base.NutzBaseService;
import io.znz.jsite.core.entity.EmployeeEntity;
import io.znz.jsite.core.entity.companyjob.CompanyJobEntity;
import io.znz.jsite.core.util.Const;
import io.znz.jsite.visa.entity.customer.CustomerManageEntity;
import io.znz.jsite.visa.forms.customerform.CustomerAddForm;
import io.znz.jsite.visa.forms.customerform.CustomerUpdateForm;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.nutz.dao.Cnd;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.uxuexi.core.common.util.Util;
import com.uxuexi.core.web.util.FormUtil;

/**
 * 客户管理业务层
 * @author   崔建斌
 * @Date	 2017年6月8日 	 
 */
@Service
public class CustomerViewService extends NutzBaseService<CustomerManageEntity> {

	/**
	 * 客户管理添加操作
	 * @param addForm
	 */
	public Object addData(CustomerAddForm addForm, final HttpSession session) {
		//通过session获取公司的id
		CompanyJobEntity company = (CompanyJobEntity) session.getAttribute(Const.USER_COMPANY_KEY);
		long comId = company.getComId();//得到公司的id
		//从session中取出当前登录用户信息
		EmployeeEntity user = (EmployeeEntity) session.getAttribute(Const.SESSION_NAME);
		long pId = user.getId();
		addForm.setPId(pId);
		addForm.setComId(comId);
		addForm.setCreateTime(new Date());
		CustomerManageEntity cusdto = FormUtil.add(dbDao, addForm, CustomerManageEntity.class);
		return cusdto;
	}

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
		Long comId = null;
		if (!Util.isEmpty(before)) {
			for (CustomerManageEntity c : before) {
				comId = c.getComId();
			}
		}
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
				m.setComId(comId);
				after.add(m);
			}
		}
		dbDao.updateRelations(before, after);
		return null;
	}
}
