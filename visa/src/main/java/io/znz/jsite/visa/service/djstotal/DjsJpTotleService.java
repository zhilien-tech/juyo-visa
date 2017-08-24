/**
 * SqsJpTotleService.java
 * io.znz.jsite.visa.service.sqsjptotal
 * Copyright (c) 2017, 北京科技有限公司版权所有.
*/

package io.znz.jsite.visa.service.djstotal;

import io.znz.jsite.base.NutzBaseService;
import io.znz.jsite.core.entity.EmployeeEntity;
import io.znz.jsite.core.entity.companyjob.CompanyJobEntity;
import io.znz.jsite.core.util.Const;
import io.znz.jsite.visa.entity.company.CompanyEntity;
import io.znz.jsite.visa.entity.japan.NewComeBabyJpDjsEntity;
import io.znz.jsite.visa.entity.japan.NewComeBabyJpEntity;
import io.znz.jsite.visa.forms.djstotal.DjsJpTotalForm;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.nutz.dao.Cnd;
import org.nutz.dao.pager.Pager;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.uxuexi.core.common.util.Util;

/**
 * 日本送签社统计
 * @author   崔建斌
 * @Date	 2017年8月22日 	 
 */
@Service
public class DjsJpTotleService extends NutzBaseService {
	public Object djsjptotalList(DjsJpTotalForm sqlForm, Pager pager, final HttpSession session) {

		//当前登录用户
		EmployeeEntity user = (EmployeeEntity) session.getAttribute(Const.SESSION_NAME);
		int userType = user.getUserType();
		if (userType == 6) {
			//1507-001 账号登录
			sqlForm.setUsertype(userType);
		} else {
			//通过session获取公司的id
			CompanyJobEntity company = (CompanyJobEntity) session.getAttribute(Const.USER_COMPANY_KEY);
			long comId = company.getComId();//得到公司的id
			sqlForm.setComId(comId);
		}

		pager = new Pager();
		pager.setPageNumber(sqlForm.getPageNumber());
		pager.setPageSize(sqlForm.getPageSize());
		return this.listPage(sqlForm, pager);
	}

	//下拉列表
	public Object compSelectfind(int compType, final HttpSession session) {
		//当前登录用户
		EmployeeEntity user = (EmployeeEntity) session.getAttribute(Const.SESSION_NAME);
		int userType = user.getUserType();

		List<NewComeBabyJpEntity> sqsCompList = Lists.newArrayList();
		List<NewComeBabyJpDjsEntity> disCompList = Lists.newArrayList();
		List<CompanyEntity> companyList = Lists.newArrayList();

		//送签社
		if (compType == 1) {
			if (userType == 6) {
				//1507-001 账号登录
				companyList = dbDao.query(CompanyEntity.class, Cnd.where("comType", "=", compType), null);
				for (CompanyEntity comp : companyList) {
					if (!Util.isEmpty(comp)) {
						long id = Long.valueOf(comp.getId() + "");
						String comName = comp.getComName();
						NewComeBabyJpEntity comBaby = new NewComeBabyJpEntity();
						comBaby.setId(id);
						comBaby.setComFullName(comName);
						sqsCompList.add(comBaby);
					}
				}
				return sqsCompList;
			} else {
				//通过session获取公司的id
				CompanyJobEntity company = (CompanyJobEntity) session.getAttribute(Const.USER_COMPANY_KEY);
				long comId = company.getComId();//得到公司的id
				sqsCompList = dbDao.query(NewComeBabyJpEntity.class, Cnd.where("comId", "=", comId), null);
				return sqsCompList;
			}
		}
		//地接社
		if (compType == 2) {
			disCompList = dbDao.query(NewComeBabyJpDjsEntity.class, null, null);
			return disCompList;
		}
		return new ArrayList();
	}
}
