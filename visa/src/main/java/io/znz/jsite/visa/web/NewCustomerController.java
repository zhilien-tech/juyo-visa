/**
 * NewCustomerController.java
 * io.znz.jsite.visa.web
 * Copyright (c) 2017, 北京科技有限公司版权所有.
*/

package io.znz.jsite.visa.web;

import io.znz.jsite.base.bean.ResultObject;
import io.znz.jsite.visa.entity.customer.NewArmyEntity;
import io.znz.jsite.visa.entity.customer.NewLanguageEntity;
import io.znz.jsite.visa.entity.customer.NewOldnameEntity;
import io.znz.jsite.visa.entity.customer.NewOldworksEntity;
import io.znz.jsite.visa.entity.customer.NewOrthercountryEntity;
import io.znz.jsite.visa.entity.customer.NewParentsEntity;
import io.znz.jsite.visa.entity.customer.NewPassportloseEntity;
import io.znz.jsite.visa.entity.customer.NewRecentlyintousaEntity;
import io.znz.jsite.visa.entity.customer.NewRelationEntity;
import io.znz.jsite.visa.entity.customer.NewSpouseEntity;
import io.znz.jsite.visa.entity.customer.NewTeachinfoEntity;
import io.znz.jsite.visa.entity.customer.NewUsainfoEntity;
import io.znz.jsite.visa.entity.customer.NewVisitedcountryEntity;
import io.znz.jsite.visa.entity.customer.NewWorkedplaceEntity;
import io.znz.jsite.visa.entity.customer.NewWorkinfoEntity;
import io.znz.jsite.visa.entity.usa.NewCustomerEntity;
import io.znz.jsite.visa.entity.usa.NewCustomerOrderEntity;
import io.znz.jsite.visa.entity.usa.NewOrderEntity;
import io.znz.jsite.visa.enums.IsDadOrMumEnum;
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
 * @Date	 2017年6月16日 	 
 */
@Controller
@RequestMapping("visa/newcustomer")
public class NewCustomerController {
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
	public Object customerSave(@RequestBody NewCustomerEntity customer) {
		String xing = customer.getChinesexing();
		String name = customer.getChinesename();
		if (!Util.isEmpty(xing) && !Util.isEmpty(name)) {
			customer.setChinesefullname(xing + name);
		} else if (Util.isEmpty(xing) && !Util.isEmpty(name)) {
			customer.setChinesefullname(name);

		} else if (!Util.isEmpty(xing) && Util.isEmpty(name)) {
			customer.setChinesefullname(xing);

		}
		List<NewCustomerOrderEntity> query = dbDao.query(NewCustomerOrderEntity.class,
				Cnd.where("customerid", "=", customer.getId()), null);
		long orderid = query.get(0).getOrderid();
		dbDao.update(NewOrderEntity.class, Chain.make("updatetime", new Date()), Cnd.where("id", "=", orderid));
		if (!Util.isEmpty(customer.getId()) && customer.getId() > 0) {
			customer.setUpdatetime(new Date());

			try {
				nutDao.update(customer);
			} catch (Exception e) {

				e.printStackTrace();

			}
		} else {
			customer.setUpdatetime(new Date());
			dbDao.insert(customer);
		}
		NewArmyEntity army = customer.getArmy();
		if (!Util.isEmpty(army)) {
			if (!Util.isEmpty(army.getId()) && army.getId() > 0) {
				nutDao.update(army);
			} else {

				army.setCustomerid(customer.getId());
				dbDao.insert(army);
			}
		}
		NewParentsEntity father = customer.getFather();
		if (!Util.isEmpty(father)) {
			if (!Util.isEmpty(father.getId()) && father.getId() > 0) {
				nutDao.update(father);
			} else {

				father.setCustomerid(customer.getId());
				father.setDadormum(IsDadOrMumEnum.dad.intKey());
				dbDao.insert(father);
			}
		}
		List<NewLanguageEntity> languagelist = customer.getLanguagelist();
		if (!Util.isEmpty(languagelist) && languagelist.size() > 0) {
			for (NewLanguageEntity newLanguageEntity : languagelist) {
				if (!Util.isEmpty(newLanguageEntity.getId()) && newLanguageEntity.getId() > 0) {
					nutDao.update(newLanguageEntity);
				} else {

					newLanguageEntity.setCustomerid(customer.getId());
					dbDao.insert(newLanguageEntity);
				}
			}
		}
		NewParentsEntity mother = customer.getMother();
		if (!Util.isEmpty(mother)) {
			if (!Util.isEmpty(mother.getId()) && mother.getId() > 0) {
				nutDao.update(mother);
			} else {

				mother.setCustomerid(customer.getId());
				mother.setDadormum(IsDadOrMumEnum.mum.intKey());
				dbDao.insert(mother);
			}
		}
		NewOldnameEntity oldname = customer.getOldname();
		if (!Util.isEmpty(oldname)) {
			if (!Util.isEmpty(oldname.getId()) && oldname.getId() > 0) {
				nutDao.update(oldname);
			} else {

				oldname.setCustomerid(customer.getId());
				dbDao.insert(oldname);
			}
		}
		List<NewOldworksEntity> oldworkslist = customer.getOldworkslist();
		if (!Util.isEmpty(oldworkslist) && oldworkslist.size() > 0) {
			for (NewOldworksEntity newLanguageEntity : oldworkslist) {
				if (!Util.isEmpty(newLanguageEntity.getId()) && newLanguageEntity.getId() > 0) {
					nutDao.update(newLanguageEntity);
				} else {

					newLanguageEntity.setCustomerid(customer.getId());
					dbDao.insert(newLanguageEntity);
				}
			}
		}
		List<NewOrthercountryEntity> orthercountrylist = customer.getOrthercountrylist();
		if (!Util.isEmpty(orthercountrylist) && orthercountrylist.size() > 0) {
			for (NewOrthercountryEntity newLanguageEntity : orthercountrylist) {
				if (!Util.isEmpty(newLanguageEntity.getId()) && newLanguageEntity.getId() > 0) {
					nutDao.update(newLanguageEntity);
				} else {

					newLanguageEntity.setCustomerid(customer.getId());
					dbDao.insert(newLanguageEntity);
				}
			}
		}
		NewPassportloseEntity passportlose = customer.getPassportlose();
		if (!Util.isEmpty(passportlose)) {
			if (!Util.isEmpty(passportlose.getId()) && passportlose.getId() > 0) {
				nutDao.update(passportlose);
			} else {

				passportlose.setCustomerid(customer.getId());
				dbDao.insert(passportlose);
			}
		}
		NewWorkinfoEntity workinfo = customer.getWorkinfo();
		if (!Util.isEmpty(workinfo)) {
			if (!Util.isEmpty(workinfo.getId()) && workinfo.getId() > 0) {
				nutDao.update(workinfo);
			} else {

				workinfo.setCustomerid(customer.getId());
				dbDao.insert(workinfo);
			}
		}
		List<NewWorkedplaceEntity> workedplacelist = customer.getWorkedplacelist();
		if (!Util.isEmpty(workedplacelist) && workedplacelist.size() > 0) {
			for (NewWorkedplaceEntity newLanguageEntity : workedplacelist) {
				if (!Util.isEmpty(newLanguageEntity.getId()) && newLanguageEntity.getId() > 0) {
					nutDao.update(newLanguageEntity);
				} else {

					newLanguageEntity.setCustomerid(customer.getId());
					dbDao.insert(newLanguageEntity);
				}
			}
		}
		List<NewVisitedcountryEntity> visitedcountrylist = customer.getVisitedcountrylist();
		if (!Util.isEmpty(visitedcountrylist) && visitedcountrylist.size() > 0) {
			for (NewVisitedcountryEntity newLanguageEntity : visitedcountrylist) {
				if (!Util.isEmpty(newLanguageEntity.getId()) && newLanguageEntity.getId() > 0) {
					nutDao.update(newLanguageEntity);
				} else {

					newLanguageEntity.setCustomerid(customer.getId());
					dbDao.insert(newLanguageEntity);
				}
			}
		}
		NewUsainfoEntity usainfo = customer.getUsainfo();
		if (!Util.isEmpty(usainfo)) {
			if (!Util.isEmpty(usainfo.getId()) && usainfo.getId() > 0) {
				nutDao.update(usainfo);
			} else {

				usainfo.setCustomerid(customer.getId());
				dbDao.insert(usainfo);
			}
		}
		List<NewTeachinfoEntity> teachinfo = customer.getTeachinfo();
		if (!Util.isEmpty(teachinfo) && teachinfo.size() > 0) {
			for (NewTeachinfoEntity newLanguageEntity : teachinfo) {
				if (!Util.isEmpty(newLanguageEntity.getId()) && newLanguageEntity.getId() > 0) {
					nutDao.update(newLanguageEntity);
				} else {

					newLanguageEntity.setCustomerid(customer.getId());
					dbDao.insert(newLanguageEntity);
				}
			}
		}
		NewSpouseEntity spouse = customer.getSpouse();
		if (!Util.isEmpty(spouse)) {
			if (!Util.isEmpty(spouse.getId()) && spouse.getId() > 0) {
				nutDao.update(spouse);
			} else {

				spouse.setCustomerid(customer.getId());
				dbDao.insert(spouse);
			}
		}
		List<NewRelationEntity> relation = customer.getRelation();
		if (!Util.isEmpty(relation) && relation.size() > 0) {
			for (NewRelationEntity newLanguageEntity : relation) {
				if (!Util.isEmpty(newLanguageEntity.getId()) && newLanguageEntity.getId() > 0) {
					nutDao.update(newLanguageEntity);
				} else {

					newLanguageEntity.setCustomerid(customer.getId());
					dbDao.insert(newLanguageEntity);
				}
			}
		}
		List<NewRecentlyintousaEntity> recentlyintousalist = customer.getRecentlyintousalist();
		if (!Util.isEmpty(recentlyintousalist) && recentlyintousalist.size() > 0) {
			for (NewRecentlyintousaEntity newLanguageEntity : recentlyintousalist) {
				if (!Util.isEmpty(newLanguageEntity.getId()) && newLanguageEntity.getId() > 0) {
					nutDao.update(newLanguageEntity);
				} else {

					newLanguageEntity.setCustomerid(customer.getId());
					dbDao.insert(newLanguageEntity);
				}
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
		NewCustomerEntity customer = dbDao.fetch(NewCustomerEntity.class, customerid);

		List<NewPassportloseEntity> passportlose = dbDao.query(NewPassportloseEntity.class,
				Cnd.where("customerid", "=", customer.getId()), null);
		if (!Util.isEmpty(passportlose) && passportlose.size() > 0) {
			customer.setPassportlose(passportlose.get(0));
		} else {
			customer.setPassportlose(new NewPassportloseEntity());

		}
		List<NewOldnameEntity> oldname = dbDao.query(NewOldnameEntity.class,
				Cnd.where("customerid", "=", customer.getId()), null);
		if (!Util.isEmpty(oldname) && oldname.size() > 0) {
			customer.setOldname(oldname.get(0));
		} else {
			customer.setOldname(new NewOldnameEntity());
		}
		List<NewOrthercountryEntity> orthercountry = dbDao.query(NewOrthercountryEntity.class,
				Cnd.where("customerid", "=", customer.getId()), null);
		if (!Util.isEmpty(orthercountry) && orthercountry.size() > 0) {
			customer.setOrthercountrylist(orthercountry);
		}
		List<NewParentsEntity> father = dbDao.query(NewParentsEntity.class,
				Cnd.where("customerid", "=", customer.getId()).and("dadormum", "=", IsDadOrMumEnum.dad.intKey()), null);
		if (!Util.isEmpty(father) && father.size() > 0) {
			customer.setFather(father.get(0));
		} else {
			customer.setFather(new NewParentsEntity());

		}
		List<NewParentsEntity> mother = dbDao.query(NewParentsEntity.class,
				Cnd.where("customerid", "=", customer.getId()).and("dadormum", "=", IsDadOrMumEnum.mum.intKey()), null);
		if (!Util.isEmpty(mother) && mother.size() > 0) {
			customer.setMother(mother.get(0));
		} else {
			customer.setMother(new NewParentsEntity());

		}
		List<NewRelationEntity> relation = dbDao.query(NewRelationEntity.class,
				Cnd.where("customerid", "=", customer.getId()), null);
		if (!Util.isEmpty(relation) && relation.size() > 0) {
			customer.setRelation(relation);
		}
		List<NewSpouseEntity> spouse = dbDao.query(NewSpouseEntity.class,
				Cnd.where("customerid", "=", customer.getId()), null);
		if (!Util.isEmpty(spouse) && spouse.size() > 0) {
			customer.setSpouse(spouse.get(0));
		} else {
			customer.setSpouse(new NewSpouseEntity());
		}
		List<NewUsainfoEntity> usainfo = dbDao.query(NewUsainfoEntity.class,
				Cnd.where("customerid", "=", customer.getId()), null);
		if (!Util.isEmpty(usainfo) && usainfo.size() > 0) {
			customer.setUsainfo(usainfo.get(0));
		} else {
			customer.setUsainfo(new NewUsainfoEntity());
		}
		List<NewTeachinfoEntity> teachinfo = dbDao.query(NewTeachinfoEntity.class,
				Cnd.where("customerid", "=", customer.getId()), null);
		if (!Util.isEmpty(teachinfo) && teachinfo.size() > 0) {
			customer.setTeachinfo(teachinfo);
		}
		List<NewRecentlyintousaEntity> recentlyintousa = dbDao.query(NewRecentlyintousaEntity.class,
				Cnd.where("customerid", "=", customer.getId()), null);
		if (!Util.isEmpty(recentlyintousa) && recentlyintousa.size() > 0) {
			customer.setRecentlyintousalist(recentlyintousa);
		}
		List<NewWorkinfoEntity> workinfo = dbDao.query(NewWorkinfoEntity.class,
				Cnd.where("customerid", "=", customer.getId()), null);
		if (!Util.isEmpty(workinfo) && workinfo.size() > 0) {
			customer.setWorkinfo(workinfo.get(0));
		} else {
			customer.setWorkinfo(new NewWorkinfoEntity());
		}
		List<NewOldworksEntity> oldworks = dbDao.query(NewOldworksEntity.class,
				Cnd.where("customerid", "=", customer.getId()), null);
		if (!Util.isEmpty(oldworks) && oldworks.size() > 0) {
			customer.setOldworkslist(oldworks);
		}
		List<NewLanguageEntity> language = dbDao.query(NewLanguageEntity.class,
				Cnd.where("customerid", "=", customer.getId()), null);
		if (!Util.isEmpty(language) && language.size() > 0) {
			customer.setLanguagelist(language);
		}
		List<NewVisitedcountryEntity> visitedcountry = dbDao.query(NewVisitedcountryEntity.class,
				Cnd.where("customerid", "=", customer.getId()), null);
		if (!Util.isEmpty(visitedcountry) && visitedcountry.size() > 0) {
			customer.setVisitedcountrylist(visitedcountry);
		}
		List<NewWorkedplaceEntity> workedplace = dbDao.query(NewWorkedplaceEntity.class,
				Cnd.where("customerid", "=", customer.getId()), null);
		if (!Util.isEmpty(workedplace) && workedplace.size() > 0) {
			customer.setWorkedplacelist(workedplace);
		}
		List<NewArmyEntity> army = dbDao.query(NewArmyEntity.class, Cnd.where("customerid", "=", customer.getId()),
				null);
		if (!Util.isEmpty(army) && army.size() > 0) {
			customer.setArmy(army.get(0));
		} else {
			customer.setArmy(new NewArmyEntity());
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
		NewCustomerEntity newCustomer = dbDao.fetch(NewCustomerEntity.class, customerid);

		if ("agree".equals(flag)) {
			dbDao.update(NewCustomerEntity.class, Chain.make("status", OrderVisaApproStatusEnum.agree.intKey()),
					Cnd.where("id", "=", newCustomer.getId()));

			List<NewCustomerOrderEntity> query = dbDao.query(NewCustomerOrderEntity.class,
					Cnd.where("customerid", "=", customerid), null);
			long orderid = query.get(0).getOrderid();
			dbDao.update(NewOrderEntity.class,
					Chain.make("updatetime", new Date()).add("status", OrderVisaApproStatusEnum.waitingSend.intKey()),
					Cnd.where("id", "=", orderid));

		} else if ("refuse".equals(flag)) {
			dbDao.update(NewCustomerEntity.class, Chain.make("status", OrderVisaApproStatusEnum.refuse.intKey()),
					Cnd.where("id", "=", newCustomer.getId()));

		}
		return ResultObject.success("操作成功");
	}

}
