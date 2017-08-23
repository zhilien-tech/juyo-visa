/**
 * PublicAuthorityService.java
 * io.znz.jsite.visa.service.authority
 * Copyright (c) 2017, 北京科技有限公司版权所有.
*/

package io.znz.jsite.visa.service.authority;

import io.znz.jsite.base.NutzBaseService;
import io.znz.jsite.visa.entity.comfunmap.CompanyFunctionEntity;
import io.znz.jsite.visa.entity.company.CompanyEntity;
import io.znz.jsite.visa.enums.CompanyTypeEnum;

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
	/**
	 * 新增公司后给公司配置功能
	 * @param addForm
	 */
	public boolean companyFunction(CompanyEntity addForm) {
		//通过session获取公司的id
		//TCompanyEntity company = (TCompanyEntity) session.getAttribute(LoginService.USER_COMPANY_KEY);
		long comId = addForm.getId();//得到公司的id
		//根据公司id查询出是上游公司还是代理商
		CompanyEntity fetchType = null;
		Long comType = null;
		if (!Util.isEmpty(comId)) {
			fetchType = dbDao.fetch(CompanyEntity.class, Cnd.where("id", "=", comId));
			comType = fetchType.getComType();//得到公司类型(送签社或者地接社)
		}
		List<CompanyFunctionEntity> functionList = Lists.newArrayList();
		List<CompanyFunctionEntity> insert = Lists.newArrayList();
		if (comType == CompanyTypeEnum.send.intKey()) {//送签社
			//送签社功能ID
			int[] function1 = { 1, 2, 3, 6, 17, 18, 19, 25, 28 };
			for (int i = 0; i < function1.length; i++) {
				//向公司功能关系表中添加数据
				CompanyFunctionEntity one = new CompanyFunctionEntity();
				one.setComId(comId);
				one.setFunId(function1[i]);
				functionList.add(one);
			}
			insert = dbDao.insert(functionList);
		} else if (comType == CompanyTypeEnum.land.intKey()) {//地接社
			//送签社功能ID
			int[] function2 = { 1, 2, 3, 6, 17, 25, 27, 29 };
			for (int i = 0; i < function2.length; i++) {
				//向公司功能关系表中添加数据
				CompanyFunctionEntity one = new CompanyFunctionEntity();
				one.setComId(comId);
				one.setFunId(function2[i]);
				functionList.add(one);
			}
			insert = dbDao.insert(functionList);
		}
		return !Util.isEmpty(insert);
	}

	/**
	 * 美国单子点击分享时给游客配置功能
	 * @param userId 用户id
	 * @param userType 用户类型  此处需要游客类型
	 */
	public Object shareAddFunction(long userId, int userType) {
		return null;
	}
}
