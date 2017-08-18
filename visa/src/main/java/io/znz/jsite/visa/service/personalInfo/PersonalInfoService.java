/**
 * CompanyService.java
 * io.znz.jsite.visa.service.company
 * Copyright (c) 2017, 北京科技有限公司版权所有.
 */

package io.znz.jsite.visa.service.personalInfo;

import io.znz.jsite.base.NutzBaseService;
import io.znz.jsite.core.entity.EmployeeEntity;
import io.znz.jsite.core.util.Const;
import io.znz.jsite.visa.entity.company.CompanyEntity;
import io.znz.jsite.visa.forms.personalInfo.PersonalInfoSqlForm;
import io.znz.jsite.visa.forms.personalInfo.PersonalInfoUpdateForm;
import io.znz.jsite.visa.service.authority.PublicAuthorityService;

import javax.servlet.http.HttpSession;

import org.nutz.dao.Cnd;
import org.nutz.dao.pager.Pager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uxuexi.core.common.util.Util;
import com.uxuexi.core.web.base.page.Pagination;

/**
 * 公司
 * @author   崔建斌
 * @Date	 2017年7月6日 	 
 */
@Service
public class PersonalInfoService extends NutzBaseService<EmployeeEntity> {

	@Autowired
	private PublicAuthorityService publicAuthorityService;

	/**
	 * 个人信息列表展示
	 * @param sqlForm
	 */
	public Object personallist(PersonalInfoSqlForm sqlForm, HttpSession session) {
		//根据当前登录用户id查询出个人信息
		EmployeeEntity user = (EmployeeEntity) session.getAttribute(Const.SESSION_NAME);
		int uid = user.getId();
		Integer userType = user.getUserType();
		sqlForm.setUserType(userType);
		sqlForm.setId(uid);
		Pagination list = this.listPage(sqlForm, new Pager());
		return list.getList().get(0);
	}

	/**
	 * 回显公司信息
	 * @param comId
	 */
	public Object updatePersonal(PersonalInfoUpdateForm updateForm) {
		long uid = updateForm.getId();
		String qq = updateForm.getQq();
		String email = updateForm.getEmail();
		String landline = updateForm.getLandline();
		EmployeeEntity entity = dbDao.fetch(EmployeeEntity.class, Cnd.where("id", "=", uid));
		if (!Util.isEmpty(entity)) {
			entity.setQq(qq);
			entity.setEmail(email);
			entity.setLandline(landline);
		}
		return this.updateIgnoreNull(entity);//更新用户表中的数据;
	}

	/**
	 * 编辑保存公司信息
	 * @param updateForm
	 */
	public Object updateCompanySave(CompanyEntity updateForm) {
		return null;
	}
}
