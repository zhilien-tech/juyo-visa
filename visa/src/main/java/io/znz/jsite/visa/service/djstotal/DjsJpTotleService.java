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
import io.znz.jsite.visa.enums.OrderVisaApproStatusEnum;
import io.znz.jsite.visa.forms.djstotal.DjsJpTotalForm;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.nutz.dao.Cnd;
import org.nutz.dao.Sqls;
import org.nutz.dao.entity.Record;
import org.nutz.dao.pager.Pager;
import org.nutz.dao.sql.Sql;
import org.nutz.dao.util.cri.SqlExpressionGroup;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

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
			sqlForm.setUsertype(userType);
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
				/*companyList = dbDao.query(CompanyEntity.class, Cnd.where("comType", "=", compType), null);
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
				return sqsCompList;*/
				Sql sql = Sqls.create(sqlManager.get("djsjptotal_sqsbaby"));
				Cnd cnd = Cnd.NEW();
				//递送之后的单子
				SqlExpressionGroup e1 = Cnd.exps("vnoj.status", "=", OrderVisaApproStatusEnum.readySubmit.intKey());
				SqlExpressionGroup e2 = Cnd.exps("vnoj.status", ">", OrderVisaApproStatusEnum.japansend.intKey());
				cnd.and(e1.or(e2));
				//送签社 or 1507账号
				SqlExpressionGroup e3 = Cnd.exps("c.comType", "=", 1);
				SqlExpressionGroup e4 = Cnd.exps("vnoj.comId", "=", 0);
				cnd.and(e3.or(e4));
				//只展示有关系的单子
				cnd.and("vnoj.sendComId", ">", "0");
				cnd.and("vnoj.landComId", ">", "0");

				cnd.groupBy("vncj.id");
				sql.setCondition(cnd);
				List<Record> record = dbDao.query(sql, null, null);
				for (Record rec : record) {
					NewComeBabyJpEntity sqsEntity = new NewComeBabyJpEntity();
					String id = rec.getString("id");
					String comfullname = rec.getString("comfullname");
					sqsEntity.setId(Long.valueOf(id));
					sqsEntity.setComFullName(comfullname);
					sqsCompList.add(sqsEntity);
				}
				return sqsCompList;
			} else {
				//通过session获取公司的id
				CompanyJobEntity company = (CompanyJobEntity) session.getAttribute(Const.USER_COMPANY_KEY);
				long comId = company.getComId();//得到公司的id
				//sqsCompList = dbDao.query(NewComeBabyJpEntity.class, Cnd.where("comId", "=", comId), null);
				Sql sql = Sqls.create(sqlManager.get("djsjptotal_sqsbaby"));
				Cnd cnd = Cnd.NEW();
				cnd.and("vnoj.sendComId", ">", "0");
				cnd.and("vnoj.landComId", ">", "0");
				cnd.and("c.id", "=", comId);
				cnd.groupBy("vncj.id");
				sql.setCondition(cnd);
				List<Record> record = dbDao.query(sql, null, null);
				for (Record rec : record) {
					NewComeBabyJpEntity sqsEntity = new NewComeBabyJpEntity();
					String id = rec.getString("id");
					String comfullname = rec.getString("comfullname");
					sqsEntity.setId(Long.valueOf(id));
					sqsEntity.setComFullName(comfullname);
					sqsCompList.add(sqsEntity);
				}
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
