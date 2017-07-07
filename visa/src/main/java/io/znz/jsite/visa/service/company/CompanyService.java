/**
 * CompanyService.java
 * io.znz.jsite.visa.service.company
 * Copyright (c) 2017, 北京科技有限公司版权所有.
*/

package io.znz.jsite.visa.service.company;

import io.znz.jsite.base.NutzBaseService;
import io.znz.jsite.core.entity.EmployeeEntity;
import io.znz.jsite.util.security.Digests;
import io.znz.jsite.util.security.Encodes;
import io.znz.jsite.visa.entity.company.CompanyEntity;
import io.znz.jsite.visa.enums.UserDeleteStatusEnum;
import io.znz.jsite.visa.forms.companyform.CompanySqlForm;

import java.util.Date;

import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.pager.Pager;
import org.nutz.ioc.aop.Aop;
import org.springframework.stereotype.Service;

import com.uxuexi.core.common.util.Util;
import com.uxuexi.core.web.chain.support.JsonResult;

/**
 * 公司
 * @author   崔建斌
 * @Date	 2017年7月6日 	 
 */
@Service
public class CompanyService extends NutzBaseService<CompanyEntity> {

	public static final int HASH_INTERATIONS = 1024;
	private static final int SALT_SIZE = 8; //盐长度
	private static final String INIT_PASSWORD = "000000"; //初始密码

	/**
	 * 公司列表展示
	 * @param sqlForm
	 */
	public Object companylist(CompanySqlForm sqlForm, Pager pager) {
		pager = new Pager();
		pager.setPageNumber(sqlForm.getPageNumber());
		pager.setPageSize(sqlForm.getPageSize());
		return this.listPage(sqlForm, pager);
	}

	/**
	 * 添加公司操作
	 * @param addForm
	 */
	public Object addcompany(CompanyEntity addForm, EmployeeEntity empForm) {
		//#########################添加用户信息###############################//
		byte[] salt = Digests.generateSalt(SALT_SIZE);
		empForm.setSalt(Encodes.encodeHex(salt));
		byte[] password = INIT_PASSWORD.getBytes();
		byte[] hashPassword = Digests.sha1(password, salt, HASH_INTERATIONS);
		empForm.setPassword(Encodes.encodeHex(hashPassword));//初始密码
		empForm.setStatus(1);
		empForm.setCreateTime(new Date());
		empForm.setUserType(1);
		empForm.setTelephone(addForm.getAdminName());
		empForm.setFullName(addForm.getAdminName());
		EmployeeEntity userAdd = dbDao.insert(empForm);
		Integer empId = userAdd.getId();//得到用户id
		//#########################添加公司信息###############################//
		addForm.setCreateTime(new Date());
		addForm.setAdminId(empId);
		if (Util.isEmpty(addForm.getDeletestatus())) {
			addForm.setDeletestatus(0);
		}
		CompanyEntity companyAdd = dbDao.insert(addForm);
		Integer comId = companyAdd.getId();//得到公司id
		return JsonResult.success("添加成功");
	}

	/**
	 * 回显公司信息
	 * @param comId
	 */
	public Object updatecompany(long comId) {
		return dbDao.fetch(CompanyEntity.class, comId);
	}

	/**
	 * 编辑保存公司信息
	 * @param updateForm
	 */
	public Object updateCompanySave(CompanyEntity updateForm) {
		//修改管理员用户名
		EmployeeEntity userEntity = dbDao.fetch(EmployeeEntity.class, updateForm.getAdminId());
		userEntity.setTelephone(updateForm.getAdminName());
		userEntity.setFullName(updateForm.getAdminName());
		userEntity.setUpdateTime(new Date());
		nutDao.updateIgnoreNull(updateForm);
		//修改公司信息
		CompanyEntity company = this.fetch(updateForm.getId());
		company.setUpdateTime(new Date());
		return nutDao.updateIgnoreNull(company);
	}

	/**
	 * 根据用户id删除单条数据
	 * @param userId
	 */
	@Aop("txDb")
	public boolean deleteCompany(Integer comId) {
		if (!Util.isEmpty(comId)) {
			dbDao.update(CompanyEntity.class, Chain.make("deletestatus", UserDeleteStatusEnum.YES.intKey()),
					Cnd.where("id", "=", comId));
			return true;
		}
		return false;
	}
}
