/**
 * VisaInfoController.java
 * io.znz.jsite.visa.web
 * Copyright (c) 2017, 北京科技有限公司版权所有.
*/

package io.znz.jsite.visa.web;

import io.znz.jsite.base.BaseController;
import io.znz.jsite.core.entity.EmployeeEntity;
import io.znz.jsite.exception.JSiteException;
import io.znz.jsite.visa.entity.communicathomeaddress.CommunicatHomeAddressEntity;
import io.znz.jsite.visa.entity.customer.CustomerManageEntity;
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
import io.znz.jsite.visa.entity.relationship.RelationShipEntity;
import io.znz.jsite.visa.entity.taxpayerauthenticationcode.TaxpayerAuthenticationCodeEntity;
import io.znz.jsite.visa.entity.travelplan.TravelPlanEntity;
import io.znz.jsite.visa.entity.travelpurpose.TravelPurposeEntity;
import io.znz.jsite.visa.entity.usa.NewCustomerEntity;
import io.znz.jsite.visa.entity.usa.NewCustomerOrderEntity;
import io.znz.jsite.visa.entity.usa.NewFastMailEntity;
import io.znz.jsite.visa.entity.usa.NewOrderEntity;
import io.znz.jsite.visa.entity.usa.NewPayCompanyEntity;
import io.znz.jsite.visa.entity.usa.NewPayPersionEntity;
import io.znz.jsite.visa.entity.usa.NewPeerPersionEntity;
import io.znz.jsite.visa.entity.usa.NewTrip;
import io.znz.jsite.visa.enums.IsDadOrMumEnum;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.uxuexi.core.common.util.Util;
import com.uxuexi.core.db.dao.IDbDao;

/**
 * 签证信息
 * @author   崔建斌
 * @Date	 2017年6月20日 	 
 */
@Controller
@RequestMapping("visa/visainfo")
public class VisaInfoController extends BaseController {
	@Autowired
	protected IDbDao dbDao;

	@Autowired
	protected Dao nutDao;

	/**
	 * 签证信息回显
	 * @param request
	 */
	@RequestMapping(value = "listvisainfo")
	@ResponseBody
	public Object listvisainfo(HttpServletRequest request) {
		//从session中取出当前登录用户信息
		EmployeeEntity user = (EmployeeEntity) request.getSession().getAttribute("fetch");
		long userId = 0;
		if (user == null) {
			throw new JSiteException("请登录后再试!");
		}
		if (!Util.isEmpty(user)) {
			userId = user.getId();
		}
		NewCustomerEntity customer = dbDao.fetch(NewCustomerEntity.class, Cnd.where("empid", "=", userId));
		long customerId = 0;
		if (!Util.isEmpty(customer)) {
			customerId = customer.getId();//得到客户id
		}
		List<NewPassportloseEntity> passportlose = dbDao.query(NewPassportloseEntity.class,
				Cnd.where("customerid", "=", customerId), null);
		if (!Util.isEmpty(passportlose) && passportlose.size() > 0) {
			customer.setPassportlose(passportlose.get(0));
		} else {
			customer.setPassportlose(new NewPassportloseEntity());

		}
		List<NewOldnameEntity> oldname = dbDao.query(NewOldnameEntity.class, Cnd.where("customerid", "=", customerId),
				null);
		if (!Util.isEmpty(oldname) && oldname.size() > 0) {
			customer.setOldname(oldname.get(0));
		} else {
			customer.setOldname(new NewOldnameEntity());
		}
		//美国纳税人认证码
		List<TaxpayerAuthenticationCodeEntity> taxpayerauthenticat = dbDao.query(
				TaxpayerAuthenticationCodeEntity.class, Cnd.where("customerId", "=", customerId), null);
		if (!Util.isEmpty(taxpayerauthenticat) && taxpayerauthenticat.size() > 0) {
			customer.setTaxpayerauthenticat(taxpayerauthenticat.get(0));
		} else {
			customer.setTaxpayerauthenticat(new TaxpayerAuthenticationCodeEntity());
		}
		//通信地址与家庭地址是否一致
		List<CommunicatHomeAddressEntity> commhomeaddress = dbDao.query(CommunicatHomeAddressEntity.class,
				Cnd.where("customerId", "=", customerId), null);
		if (!Util.isEmpty(commhomeaddress) && commhomeaddress.size() > 0) {
			customer.setCommhomeaddress(commhomeaddress.get(0));
		} else {
			customer.setCommhomeaddress(new CommunicatHomeAddressEntity());
		}
		//赴美国旅行目的列表
		List<TravelPurposeEntity> travelpurpose = dbDao.query(TravelPurposeEntity.class,
				Cnd.where("customerId", "=", customerId), null);
		if (!Util.isEmpty(travelpurpose) && travelpurpose.size() > 0) {
			customer.setTravelpurpose(travelpurpose.get(0));
		} else {
			customer.setTravelpurpose(new TravelPurposeEntity());
		}
		//是否制定了具体旅行计划
		List<TravelPlanEntity> travelplan = dbDao.query(TravelPlanEntity.class,
				Cnd.where("customerId", "=", customerId), null);
		if (!Util.isEmpty(travelplan) && travelplan.size() > 0) {
			customer.setTravelplan(travelplan.get(0));
		} else {
			customer.setTravelplan(new TravelPlanEntity());
		}
		//与你的关系
		List<RelationShipEntity> relationship = dbDao.query(RelationShipEntity.class,
				Cnd.where("customerId", "=", customerId), null);
		if (!Util.isEmpty(relationship) && relationship.size() > 0) {
			customer.setRelationship(relationship.get(0));
		} else {
			customer.setRelationship(new RelationShipEntity());
		}
		List<NewOrthercountryEntity> orthercountry = dbDao.query(NewOrthercountryEntity.class,
				Cnd.where("customerid", "=", customerId), null);
		if (!Util.isEmpty(orthercountry) && orthercountry.size() > 0) {
			customer.setOrthercountrylist(orthercountry);
		}
		List<NewParentsEntity> father = dbDao.query(NewParentsEntity.class, Cnd.where("customerid", "=", customerId)
				.and("dadormum", "=", IsDadOrMumEnum.dad.intKey()), null);
		if (!Util.isEmpty(father) && father.size() > 0) {
			customer.setFather(father.get(0));
		} else {
			customer.setFather(new NewParentsEntity());

		}
		List<NewParentsEntity> mother = dbDao.query(NewParentsEntity.class, Cnd.where("customerid", "=", customerId)
				.and("dadormum", "=", IsDadOrMumEnum.mum.intKey()), null);
		if (!Util.isEmpty(mother) && mother.size() > 0) {
			customer.setMother(mother.get(0));
		} else {
			customer.setMother(new NewParentsEntity());

		}
		List<NewRelationEntity> relation = dbDao.query(NewRelationEntity.class,
				Cnd.where("customerid", "=", customerId), null);
		if (!Util.isEmpty(relation) && relation.size() > 0) {
			customer.setRelation(relation);
		}
		List<NewSpouseEntity> spouse = dbDao.query(NewSpouseEntity.class, Cnd.where("customerid", "=", customerId),
				null);
		if (!Util.isEmpty(spouse) && spouse.size() > 0) {
			customer.setSpouse(spouse.get(0));
		} else {
			customer.setSpouse(new NewSpouseEntity());
		}
		List<NewUsainfoEntity> usainfo = dbDao.query(NewUsainfoEntity.class, Cnd.where("customerid", "=", customerId),
				null);
		if (!Util.isEmpty(usainfo) && usainfo.size() > 0) {
			customer.setUsainfo(usainfo.get(0));
		} else {
			customer.setUsainfo(new NewUsainfoEntity());
		}
		List<NewTeachinfoEntity> teachinfo = dbDao.query(NewTeachinfoEntity.class,
				Cnd.where("customerid", "=", customerId), null);
		if (!Util.isEmpty(teachinfo) && teachinfo.size() > 0) {
			customer.setTeachinfo(teachinfo);
		}
		List<NewRecentlyintousaEntity> recentlyintousa = dbDao.query(NewRecentlyintousaEntity.class,
				Cnd.where("customerid", "=", customerId), null);
		if (!Util.isEmpty(recentlyintousa) && recentlyintousa.size() > 0) {
			customer.setRecentlyintousalist(recentlyintousa);
		}
		List<NewWorkinfoEntity> workinfo = dbDao.query(NewWorkinfoEntity.class,
				Cnd.where("customerid", "=", customerId), null);
		if (!Util.isEmpty(workinfo) && workinfo.size() > 0) {
			customer.setWorkinfo(workinfo.get(0));
		} else {
			customer.setWorkinfo(new NewWorkinfoEntity());
		}
		List<NewOldworksEntity> oldworks = dbDao.query(NewOldworksEntity.class,
				Cnd.where("customerid", "=", customerId), null);
		if (!Util.isEmpty(oldworks) && oldworks.size() > 0) {
			customer.setOldworkslist(oldworks);
		}
		List<NewLanguageEntity> language = dbDao.query(NewLanguageEntity.class,
				Cnd.where("customerid", "=", customerId), null);
		if (!Util.isEmpty(language) && language.size() > 0) {
			customer.setLanguagelist(language);
		}
		List<NewVisitedcountryEntity> visitedcountry = dbDao.query(NewVisitedcountryEntity.class,
				Cnd.where("customerid", "=", customerId), null);
		if (!Util.isEmpty(visitedcountry) && visitedcountry.size() > 0) {
			customer.setVisitedcountrylist(visitedcountry);
		}
		List<NewWorkedplaceEntity> workedplace = dbDao.query(NewWorkedplaceEntity.class,
				Cnd.where("customerid", "=", customerId), null);
		if (!Util.isEmpty(workedplace) && workedplace.size() > 0) {
			customer.setWorkedplacelist(workedplace);
		}
		List<NewArmyEntity> army = dbDao.query(NewArmyEntity.class, Cnd.where("customerid", "=", customerId), null);
		if (!Util.isEmpty(army) && army.size() > 0) {
			customer.setArmy(army.get(0));
		} else {
			customer.setArmy(new NewArmyEntity());
		}
		/*********************************************查询订单下面的信息************************************************************************/
		//根据客户id查询客户订单关系表
		List<NewCustomerOrderEntity> customOrder = dbDao.query(NewCustomerOrderEntity.class,
				Cnd.where("customerid", "=", customerId), null);
		long orderid = 0;
		if (!Util.isEmpty(customOrder)) {
			for (NewCustomerOrderEntity co : customOrder) {
				orderid = co.getOrderid();
			}
		}
		NewOrderEntity order = dbDao.fetch(NewOrderEntity.class, orderid);
		CustomerManageEntity customerManageEntity = dbDao.fetch(CustomerManageEntity.class,
				order.getCus_management_id());
		if (!Util.isEmpty(customerManageEntity)) {
			order.setCustomermanage(customerManageEntity);
		}
		List<NewTrip> newTrips = dbDao.query(NewTrip.class, Cnd.where("orderid", "=", orderid), null);
		if (!Util.isEmpty(newTrips) && newTrips.size() > 0) {
			order.setTrip(newTrips.get(0));
			List<NewPeerPersionEntity> query = dbDao.query(NewPeerPersionEntity.class,
					Cnd.where("tripid", "=", newTrips.get(0).getId()), null);
			order.setPeerList(query);
		}
		List<NewPayPersionEntity> newPayPersionEntities = dbDao.query(NewPayPersionEntity.class,
				Cnd.where("orderid", "=", orderid), null);
		if (!Util.isEmpty(newPayPersionEntities) && newPayPersionEntities.size() > 0) {
			order.setPayPersion(newPayPersionEntities.get(0));
		}
		List<NewPayCompanyEntity> newPayCompanyEntities = dbDao.query(NewPayCompanyEntity.class,
				Cnd.where("orderid", "=", orderid), null);
		if (!Util.isEmpty(newPayCompanyEntities) && newPayCompanyEntities.size() > 0) {
			order.setPayCompany(newPayCompanyEntities.get(0));
		}
		List<NewFastMailEntity> newFastMailEntities = dbDao.query(NewFastMailEntity.class,
				Cnd.where("orderid", "=", orderid), null);
		if (!Util.isEmpty(newFastMailEntities) && newFastMailEntities.size() > 0) {
			order.setFastMail(newFastMailEntities.get(0));
		}
		return customer;
	}
}
