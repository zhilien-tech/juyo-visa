/**
 * NewCustomerJpController.java
 * io.znz.jsite.visa.web
 * Copyright (c) 2017, 北京科技有限公司版权所有.
*/

package io.znz.jsite.visa.web;

import io.znz.jsite.base.bean.ResultObject;
import io.znz.jsite.visa.entity.japan.NewCustomerJpEntity;
import io.znz.jsite.visa.entity.japan.NewCustomerOrderJpEntity;
import io.znz.jsite.visa.entity.japan.NewFinanceJpEntity;
import io.znz.jsite.visa.entity.japan.NewOldnameJpEntity;
import io.znz.jsite.visa.entity.japan.NewOldpassportJpEntity;
import io.znz.jsite.visa.entity.japan.NewOrderJpEntity;
import io.znz.jsite.visa.entity.japan.NewOrthercountryJpEntity;
import io.znz.jsite.visa.entity.japan.NewRecentlyintojpJpEntity;
import io.znz.jsite.visa.entity.japan.NewWorkinfoJpEntity;
import io.znz.jsite.visa.enums.OrderVisaApproStatusEnum;

import java.util.Date;
import java.util.List;

import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.SqlManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.uxuexi.core.common.util.Util;
import com.uxuexi.core.db.dao.IDbDao;

/**
 * TODO(这里用一句话描述这个类的作用)
 * <p>
 * TODO(这里描述这个类补充说明 – 可选)
 *
 * @author   孙斌
 * @Date	 2017年6月22日 	 
 */
@Controller
@RequestMapping("visa/newcustomerjp")
public class NewCustomerJpController {
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

	/*****
	 * 
	 * 客户管理修改所用
	 * <p>
	 * TODO(这里描述这个方法详情– 可选)
	 *
	 * @param customers
	 * @param customer
	 * @return TODO(这里描述每个参数,如果有返回值描述返回值,如果有异常描述异常)
	 */
	@RequestMapping(value = "customerSave", method = RequestMethod.POST)
	@ResponseBody
	public Object customerSave(@RequestBody NewCustomerJpEntity customer) {
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

	/***
	 * 
	 * 客户信息编辑的数据回显
	 * <p>
	 * TODO(这里描述这个方法详情– 可选)
	 *
	 * @param orderid
	 * @return TODO(这里描述每个参数,如果有返回值描述返回值,如果有异常描述异常)
	 */

	@RequestMapping(value = "showDetail")
	@ResponseBody
	public Object showDetail(long customerid) {
		NewCustomerJpEntity customer = dbDao.fetch(NewCustomerJpEntity.class, customerid);

		List<NewWorkinfoJpEntity> passportlose = dbDao.query(NewWorkinfoJpEntity.class,
				Cnd.where("customer_jp_id", "=", customer.getId()), null);
		if (!Util.isEmpty(passportlose) && passportlose.size() > 0) {
			customer.setWorkinfoJp(passportlose.get(0));
		} else {
			customer.setWorkinfoJp(new NewWorkinfoJpEntity());

		}
		List<NewOldpassportJpEntity> oldname = dbDao.query(NewOldpassportJpEntity.class,
				Cnd.where("customer_jp_id", "=", customer.getId()), null);
		if (!Util.isEmpty(oldname) && oldname.size() > 0) {
			customer.setOldpassportJp(oldname.get(0));
		} else {
			customer.setOldpassportJp(new NewOldpassportJpEntity());
		}
		List<NewFinanceJpEntity> orthercountry = dbDao.query(NewFinanceJpEntity.class,
				Cnd.where("customer_jp_id", "=", customer.getId()), null);
		if (!Util.isEmpty(orthercountry) && orthercountry.size() > 0) {
			customer.setFinanceJpList(orthercountry);
		}
		List<NewOldnameJpEntity> father = dbDao.query(NewOldnameJpEntity.class,
				Cnd.where("customer_jp_id", "=", customer.getId()), null);
		if (!Util.isEmpty(father) && father.size() > 0) {
			customer.setOldnameJp(father.get(0));
		} else {
			customer.setOldnameJp(new NewOldnameJpEntity());

		}

		List<NewOrthercountryJpEntity> relation = dbDao.query(NewOrthercountryJpEntity.class,
				Cnd.where("customer_jp_id", "=", customer.getId()), null);
		if (!Util.isEmpty(relation) && relation.size() > 0) {
			customer.setOrthercountryJpList(relation);
		}

		List<NewRecentlyintojpJpEntity> teachinfo = dbDao.query(NewRecentlyintojpJpEntity.class,
				Cnd.where("customer_jp_id", "=", customer.getId()), null);
		if (!Util.isEmpty(teachinfo) && teachinfo.size() > 0) {
			customer.setRecentlyintojpJpList(teachinfo);
		}

		return customer;
	}

	/****
	 * 拒绝或者同意
	 * 
	 */

	@RequestMapping(value = "agreeOrRefuse")
	@ResponseBody
	public Object agreeOrRefuse(String flag, long customerid) {
		NewCustomerJpEntity newCustomer = dbDao.fetch(NewCustomerJpEntity.class, customerid);

		if ("agree".equals(flag)) {
			dbDao.update(NewCustomerJpEntity.class, Chain.make("status", OrderVisaApproStatusEnum.agree.intKey()),
					Cnd.where("id", "=", newCustomer.getId()));

			List<NewCustomerOrderJpEntity> query = dbDao.query(NewCustomerOrderJpEntity.class,
					Cnd.where("customer_jp_id", "=", customerid), null);
			long orderid = query.get(0).getOrder_jp_id();
			dbDao.update(NewOrderJpEntity.class,
					Chain.make("updatetime", new Date()).add("status", OrderVisaApproStatusEnum.waitingSend.intKey()),
					Cnd.where("id", "=", orderid));
		} else if ("refuse".equals(flag)) {
			dbDao.update(NewCustomerJpEntity.class, Chain.make("status", OrderVisaApproStatusEnum.refuse.intKey()),
					Cnd.where("id", "=", newCustomer.getId()));

		}
		return ResultObject.success("操作成功");
	}

}
