/**
 * PublicAuthorityService.java
 * io.znz.jsite.visa.service.authority
 * Copyright (c) 2017, 北京科技有限公司版权所有.
*/

package io.znz.jsite.visa.service.authority;

import io.znz.jsite.base.NutzBaseService;
import io.znz.jsite.visa.entity.comfunmap.CompanyFunctionEntity;
import io.znz.jsite.visa.entity.company.CompanyEntity;

import java.util.List;

import org.nutz.dao.Cnd;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.uxuexi.core.common.util.Util;

/**
 * 权限管理对外接口
 * @author   崔建斌
 * @Date	 2017年7月11日 	 
 */
@Service
public class PublicAuthorityService extends NutzBaseService {
	//新增上游公司后给公司配置功能
	public boolean companyFunction(CompanyEntity addForm) {
		//通过session获取公司的id
		//TCompanyEntity company = (TCompanyEntity) session.getAttribute(LoginService.USER_COMPANY_KEY);
		long adminId = addForm.getAdminId();//得到公司管理员的id
		//根据公司id查询出是上游公司还是代理商
		CompanyEntity fetchType = dbDao.fetch(CompanyEntity.class, Cnd.where("adminId", "=", adminId));
		long comId = fetchType.getId();//得到公司id
		//上游公司功能ID
		int[] function = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24 };
		List<CompanyFunctionEntity> functionList = Lists.newArrayList();
		for (int i = 0; i < function.length; i++) {
			//向公司功能关系表中添加数据
			CompanyFunctionEntity one = new CompanyFunctionEntity();
			one.setComId(comId);
			one.setFunId(function[i]);
			functionList.add(one);
		}
		List<CompanyFunctionEntity> insert = dbDao.insert(functionList);
		return !Util.isEmpty(insert);
	}
}
