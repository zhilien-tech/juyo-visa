/**
 * NewCustomerJpEntity.java
 * io.znz.jsite.visa.service
 * Copyright (c) 2017, 北京科技有限公司版权所有.
*/

package io.znz.jsite.visa.service;

import io.znz.jsite.base.bean.ResultObject;
import io.znz.jsite.visa.entity.japan.NewCustomerJpEntity;
import io.znz.jsite.visa.entity.japan.NewCustomerOrderJpEntity;
import io.znz.jsite.visa.entity.japan.NewFinanceJpEntity;
import io.znz.jsite.visa.entity.japan.NewOldnameJpEntity;
import io.znz.jsite.visa.entity.japan.NewOldpassportJpEntity;
import io.znz.jsite.visa.entity.japan.NewOrderJpEntity;
import io.znz.jsite.visa.entity.japan.NewOrthercountryJpEntity;
import io.znz.jsite.visa.entity.japan.NewProposerInfoJpEntity;
import io.znz.jsite.visa.entity.japan.NewRecentlyintojpJpEntity;
import io.znz.jsite.visa.entity.japan.NewWorkinfoJpEntity;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.SqlManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ibm.icu.text.SimpleDateFormat;
import com.uxuexi.core.common.util.Util;
import com.uxuexi.core.db.dao.IDbDao;

/**
 * TODO(这里用一句话描述这个类的作用)
 * <p>
 * TODO(这里描述这个类补充说明 – 可选)
 *
 * @author   孙斌
 * @Date	 2017年7月3日 	 
 */
@Service
public class NewCustomerJpService {
	@Autowired
	protected IDbDao dbDao;

	/**nutz dao*/
	@Autowired
	protected Dao nutDao;

	/**
	 * 注入容器中的sqlManager对象，用于获取sql
	 */
	@Autowired
	protected SqlManager sqlManager;

	@Transactional(readOnly = true)
	public Object save(NewCustomerJpEntity customer) {
		//身份证号的截取
		String idcard = customer.getIdcard();
		if (!Util.isEmpty(idcard)) {
			String birthday = idcard.substring(6, 14);
			SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
			try {
				Date parse = df.parse(birthday);
				if (!Util.isEmpty(parse)) {
					customer.setBirthday(parse);
				}
			} catch (ParseException e) {

				// TODO Auto-generated catch block
				e.printStackTrace();

			}
		}
		String xing = customer.getChinesexing();
		String name = customer.getChinesename();
		if (!Util.isEmpty(xing) && !Util.isEmpty(name)) {
			customer.setChinesefullname(xing + name);
		} else if (Util.isEmpty(xing) && !Util.isEmpty(name)) {
			customer.setChinesefullname(name);

		} else if (!Util.isEmpty(xing) && Util.isEmpty(name)) {
			customer.setChinesefullname(xing);

		}
		List<NewCustomerOrderJpEntity> query = dbDao.query(NewCustomerOrderJpEntity.class,
				Cnd.where("customer_jp_id", "=", customer.getId()), null);
		long orderid = query.get(0).getOrder_jp_id();
		//===============更改申请人表的信息=================
		List<NewProposerInfoJpEntity> proposerlist = dbDao.query(NewProposerInfoJpEntity.class,
				Cnd.where("customer_jp_id", "=", customer.getId()), null);
		if (!Util.isEmpty(proposerlist) && proposerlist.get(0).getId() > 0) {
			NewProposerInfoJpEntity newProposerInfoJpEntity = proposerlist.get(0);
			newProposerInfoJpEntity.setXing(xing);
			newProposerInfoJpEntity.setName(name);
			if (Util.isEmpty(xing)) {
				xing = "";
			}
			if (Util.isEmpty(name)) {
				name = "";
			}
			newProposerInfoJpEntity.setFullname(xing + name);
			dbDao.update(newProposerInfoJpEntity, null);
		}
		//================更改申请人表的信息结束================

		dbDao.update(NewOrderJpEntity.class, Chain.make("updatetime", new Date()), Cnd.where("id", "=", orderid));
		NewCustomerJpEntity newcustomer = customer;
		if (!Util.isEmpty(customer.getId()) && customer.getId() > 0) {
			customer.setUpdatetime(new Date());

			try {
				nutDao.update(customer);
			} catch (Exception e) {

				e.printStackTrace();

			}
		} else {
			customer.setUpdatetime(new Date());
			newcustomer = dbDao.insert(customer);
		}
		NewWorkinfoJpEntity army = customer.getWorkinfoJp();
		if (!Util.isEmpty(army)) {
			if (!Util.isEmpty(army.getId()) && army.getId() > 0) {
				nutDao.update(army);
			} else {

				army.setCustomer_jp_id(customer.getId());
				dbDao.insert(army);
			}
		}
		NewOldpassportJpEntity father = customer.getOldpassportJp();
		if (!Util.isEmpty(father)) {
			if (!Util.isEmpty(father.getId()) && father.getId() > 0) {
				nutDao.update(father);
			} else {

				father.setCustomer_jp_id(customer.getId());
				dbDao.insert(father);
			}
		}

		NewOldnameJpEntity mother = customer.getOldnameJp();
		if (!Util.isEmpty(mother)) {
			if (!Util.isEmpty(mother.getId()) && mother.getId() > 0) {
				nutDao.update(mother);
			} else {

				mother.setCustomer_jp_id(customer.getId());
				dbDao.insert(mother);
			}
		}

		List<NewRecentlyintojpJpEntity> oldworkslist = customer.getRecentlyintojpJpList();
		List<NewRecentlyintojpJpEntity> list1 = dbDao.query(NewRecentlyintojpJpEntity.class,
				Cnd.where("customer_jp_id", "=", customer.getId()), null);
		if (!Util.isEmpty(list1) && list1.size() > 0) {

			dbDao.delete(list1);
		}
		if (!Util.isEmpty(oldworkslist) && oldworkslist.size() > 0) {
			for (NewRecentlyintojpJpEntity newLanguageEntity : oldworkslist) {
				/*if (!Util.isEmpty(newLanguageEntity.getId()) && newLanguageEntity.getId() > 0) {
					nutDao.update(newLanguageEntity);
				} else {
				*/
				newLanguageEntity.setCustomer_jp_id(customer.getId());
				dbDao.insert(newLanguageEntity);
				//}
			}
		}
		List<NewFinanceJpEntity> orthercountrylist = customer.getFinanceJpList();
		List<NewFinanceJpEntity> list2 = dbDao.query(NewFinanceJpEntity.class,
				Cnd.where("customer_jp_id", "=", customer.getId()), null);
		if (!Util.isEmpty(list2) && list2.size() > 0) {

			dbDao.delete(list2);
		}
		if (!Util.isEmpty(orthercountrylist) && orthercountrylist.size() > 0) {
			for (NewFinanceJpEntity newLanguageEntity : orthercountrylist) {
				/*	if (!Util.isEmpty(newLanguageEntity.getId()) && newLanguageEntity.getId() > 0) {
						nutDao.update(newLanguageEntity);
					} else {
				*/
				newLanguageEntity.setCustomer_jp_id(customer.getId());
				dbDao.insert(newLanguageEntity);
				//	}
			}
		}

		List<NewOrthercountryJpEntity> workedplacelist = customer.getOrthercountryJpList();

		List<NewOrthercountryJpEntity> list3 = dbDao.query(NewOrthercountryJpEntity.class,
				Cnd.where("customer_jp_id", "=", customer.getId()), null);
		if (!Util.isEmpty(list3) && list3.size() > 0) {

			dbDao.delete(list3);
		}
		if (!Util.isEmpty(workedplacelist) && workedplacelist.size() > 0) {
			for (NewOrthercountryJpEntity newLanguageEntity : workedplacelist) {
				/*if (!Util.isEmpty(newLanguageEntity.getId()) && newLanguageEntity.getId() > 0) {
					nutDao.update(newLanguageEntity);
				} else {
				*/
				newLanguageEntity.setCustomer_jp_id(customer.getId());
				dbDao.insert(newLanguageEntity);
				//}
			}
		}

		return ResultObject.success("修改成功");

	}

}
