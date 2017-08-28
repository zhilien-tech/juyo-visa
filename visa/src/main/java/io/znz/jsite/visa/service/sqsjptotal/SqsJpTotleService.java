/**
 * SqsJpTotleService.java
 * io.znz.jsite.visa.service.sqsjptotal
 * Copyright (c) 2017, 北京科技有限公司版权所有.
*/

package io.znz.jsite.visa.service.sqsjptotal;

import io.znz.jsite.base.NutzBaseService;
import io.znz.jsite.core.entity.companyjob.CompanyJobEntity;
import io.znz.jsite.core.util.Const;
import io.znz.jsite.visa.entity.japan.NewComeBabyJpDjsEntity;
import io.znz.jsite.visa.entity.japan.NewComeBabyJpEntity;
import io.znz.jsite.visa.forms.sqstotal.SqlJpTotalForm;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.nutz.dao.Cnd;
import org.nutz.dao.Sqls;
import org.nutz.dao.entity.Record;
import org.nutz.dao.pager.Pager;
import org.nutz.dao.sql.Sql;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

/**
 * 日本送签社统计
 * @author   崔建斌
 * @Date	 2017年8月22日 	 
 */
@Service
public class SqsJpTotleService extends NutzBaseService {
	public Object sqsjptotalList(SqlJpTotalForm sqlForm, Pager pager, final HttpSession session) {
		//通过session获取公司的id
		CompanyJobEntity company = (CompanyJobEntity) session.getAttribute(Const.USER_COMPANY_KEY);
		long comId = company.getComId();//得到公司的id
		sqlForm.setComId(comId);
		pager = new Pager();
		pager.setPageNumber(sqlForm.getPageNumber());
		pager.setPageSize(sqlForm.getPageSize());
		//List<TotalJpBean> list = Lists.newArrayList();
		return this.listPage(sqlForm, pager);
	}

	//下拉列表
	public Object compSelectfind(int compType, final HttpSession session) {
		//通过session获取公司的id
		CompanyJobEntity company = (CompanyJobEntity) session.getAttribute(Const.USER_COMPANY_KEY);
		long comId = company.getComId();//得到公司的id
		/*List<CompanyEntity> sqsCompList = Lists.newArrayList();*/
		List<NewComeBabyJpEntity> sqsCompList = Lists.newArrayList();
		List<NewComeBabyJpDjsEntity> disCompList = Lists.newArrayList();
		if (compType == 1) {
			//sqsCompList = dbDao.query(CompanyEntity.class, Cnd.where("id", "=", comId), null);
			Sql sql = Sqls.create(sqlManager.get("sqsjptotal_sqsbaby"));
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
		if (compType == 2) {
			disCompList = dbDao.query(NewComeBabyJpDjsEntity.class, null, null);
			return disCompList;
		}
		return new ArrayList();
	}
}
