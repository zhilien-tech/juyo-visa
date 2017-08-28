/**
 * NewSystemStatisticService.java
 * io.znz.jsite.visa.service
 * Copyright (c) 2017, 北京科技有限公司版权所有.
*/

package io.znz.jsite.visa.service;

import io.znz.jsite.base.NutzBaseService;
import io.znz.jsite.visa.entity.company.CompanyEntity;
import io.znz.jsite.visa.entity.japan.NewComeBabyJpDjsEntity;
import io.znz.jsite.visa.entity.japan.NewComeBabyJpEntity;

import java.util.ArrayList;
import java.util.List;

import org.nutz.dao.Cnd;
import org.nutz.dao.Sqls;
import org.nutz.dao.entity.Record;
import org.nutz.dao.sql.Sql;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;

/**
 * <p>
 *
 * @author   彭辉
 * @Date	 2017年8月22日 	 
 */
@Service
@Transactional(readOnly = true)
public class NewSystemStatisticService extends NutzBaseService {

	//下拉列表
	public Object compSelectfind(int compType) {
		List<CompanyEntity> companyList = Lists.newArrayList();
		List<NewComeBabyJpEntity> sqsCompList = Lists.newArrayList();
		List<NewComeBabyJpDjsEntity> disCompList = Lists.newArrayList();

		if (compType == 1) {
			//送签社
			//companyList = dbDao.query(CompanyEntity.class, Cnd.where("comType", "=", compType), null);
			Sql sql = Sqls.create(sqlManager.get("system_statistic_sqsbaby"));
			Cnd cnd = Cnd.NEW();
			cnd.and("vnoj.sendComId", ">", "0");
			cnd.and("vnoj.landComId", ">", "0");
			cnd.orderBy("vncj.id", null);

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
		//地接社
		if (compType == 2) {
			//地接社
			/*companyList = dbDao.query(CompanyEntity.class, Cnd.where("comType", "=", compType), null);
			CompanyEntity djsComp = new CompanyEntity();
			djsComp.setId(0);
			djsComp.setComName("株式会社金通商事");
			companyList.add(0, djsComp);*/

			disCompList = dbDao.query(NewComeBabyJpDjsEntity.class, null, null);
			return disCompList;
		}
		/*return companyList;*/
		return new ArrayList();
	}

	//招宝信息统计
	public Object compBabySelectfind(int compType) {
		List<NewComeBabyJpEntity> sqsCompList = Lists.newArrayList();
		List<NewComeBabyJpDjsEntity> disCompList = Lists.newArrayList();

		if (compType == 1) {
			sqsCompList = dbDao.query(NewComeBabyJpEntity.class, null, null);
			return sqsCompList;

		}
		if (compType == 2) {
			disCompList = dbDao.query(NewComeBabyJpDjsEntity.class, null, null);
			return disCompList;
		}
		return new ArrayList();
	}

}
