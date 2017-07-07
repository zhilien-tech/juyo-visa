/**
 * VisaInfoController.java
 * io.znz.jsite.visa.web
 * Copyright (c) 2017, 北京科技有限公司版权所有.
*/

package io.znz.jsite.visa.web;

import io.znz.jsite.base.BaseController;
import io.znz.jsite.base.bean.ResultObject;
import io.znz.jsite.core.entity.EmployeeEntity;
import io.znz.jsite.core.util.Const;
import io.znz.jsite.exception.JSiteException;
import io.znz.jsite.visa.entity.applicantproducer.ApplicantProducerEntity;
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
import io.znz.jsite.visa.entity.japan.NewCustomerJpEntity;
import io.znz.jsite.visa.entity.japan.NewCustomerOrderJpEntity;
import io.znz.jsite.visa.entity.japan.NewFinanceJpEntity;
import io.znz.jsite.visa.entity.japan.NewOrderJpEntity;
import io.znz.jsite.visa.entity.japan.NewWorkinfoJpEntity;
import io.znz.jsite.visa.entity.placeinformation.PlaceInformationEntity;
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
import io.znz.jsite.visa.enums.OrderVisaApproStatusEnum;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;
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
	 * 美国签证信息回显
	 * @param request
	 */
	@RequestMapping(value = "listvisainfo")
	@ResponseBody
	public Object listvisainfo(HttpServletRequest request) {
		//从session中取出当前登录用户信息
		EmployeeEntity user = (EmployeeEntity) request.getSession().getAttribute(Const.SESSION_NAME);
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
		//地点信息
		List<PlaceInformationEntity> placeinformation = dbDao.query(PlaceInformationEntity.class,
				Cnd.where("customerId", "=", customerId), null);
		if (!Util.isEmpty(placeinformation) && placeinformation.size() > 0) {
			customer.setPlaceinformation(placeinformation.get(0));
		} else {
			customer.setPlaceinformation(new PlaceInformationEntity());
		}
		//申请的制作者
		List<ApplicantProducerEntity> applicantproducer = dbDao.query(ApplicantProducerEntity.class,
				Cnd.where("customerId", "=", customerId), null);
		if (!Util.isEmpty(applicantproducer) && applicantproducer.size() > 0) {
			customer.setApplicantproducer(applicantproducer.get(0));
		} else {
			customer.setApplicantproducer(new ApplicantProducerEntity());
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
			customer.setCustomermanage(customerManageEntity);
		}
		List<NewTrip> newTrips = dbDao.query(NewTrip.class, Cnd.where("orderid", "=", orderid), null);
		List<NewPeerPersionEntity> query = null;
		if (!Util.isEmpty(newTrips) && newTrips.size() > 0) {
			order.setTrip(newTrips.get(0));
			query = dbDao.query(NewPeerPersionEntity.class, Cnd.where("tripid", "=", newTrips.get(0).getId()), null);
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
		customer.setOrder(order);//订单管理
		customer.setCustomermanage(customerManageEntity);
		customer.setTrip(newTrips);
		if (Util.isEmpty(query)) {
			query = Lists.newArrayList();
		}
		customer.setPeerList(query);
		customer.setPayPersion(newPayPersionEntities);
		customer.setPayCompany(newPayCompanyEntities);
		customer.setFastMail(newFastMailEntities);
		return customer;
	}

	/**
	 * 更新保存美国签证信息数据
	 * @param customer
	 */
	@RequestMapping(value = "updatePassportSave", method = RequestMethod.POST)
	@ResponseBody
	public Object updatePassportSave(@RequestBody NewCustomerEntity customer) {
		//申请的制作者
		String producerXing = customer.getApplicantproducer().getProducerXing();
		String producerMing = customer.getApplicantproducer().getProducerMing();
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
		int orderid = query.get(0).getOrderid();
		dbDao.update(NewOrderEntity.class,
				Chain.make("updatetime", new Date()).add("status", OrderVisaApproStatusEnum.firstReview.intKey()),
				Cnd.where("id", "=", orderid));
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
		//所说语言列表
		List<NewLanguageEntity> languagelist = customer.getLanguagelist();
		List<NewLanguageEntity> query3 = dbDao.query(NewLanguageEntity.class,
				Cnd.where("customerid", "=", customer.getId()), null);
		if (!Util.isEmpty(query3)) {
			dbDao.delete(query3);
		}
		if (!Util.isEmpty(languagelist) && languagelist.size() > 0) {
			for (NewLanguageEntity newLanguageEntity : languagelist) {
				newLanguageEntity.setCustomerid(customer.getId());
				dbDao.insert(newLanguageEntity);
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
		//支付人公司/组织
		//NewPayCompanyEntity payCompany = customer.getOrder().getPayCompany();
		List<NewPayCompanyEntity> payCompany = customer.getPayCompany();
		/*if (!Util.isEmpty(payCompany)) {
			if (!Util.isEmpty(payCompany.getId()) && payCompany.getId() > 0) {
				nutDao.update(payCompany);
			} else {
				payCompany.setOrderid(orderid);
				dbDao.insert(payCompany);
			}
		}*/
		if (!Util.isEmpty(payCompany) && payCompany.size() > 0) {
			for (NewPayCompanyEntity newPayCompanyEntity : payCompany) {
				if (!Util.isEmpty(newPayCompanyEntity.getId()) && newPayCompanyEntity.getId() > 0) {
					nutDao.update(newPayCompanyEntity);
				} else {
					newPayCompanyEntity.setOrderid(orderid);
					dbDao.insert(newPayCompanyEntity);
				}
			}
		}
		//支付人---其他人
		NewPayPersionEntity payPersion = customer.getOrder().getPayPersion();
		if (!Util.isEmpty(payPersion)) {
			if (!Util.isEmpty(payPersion.getId()) && payPersion.getId() > 0) {
				nutDao.update(payPersion);
			} else {
				payPersion.setOrderid(orderid);
				dbDao.insert(payPersion);
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

		List<NewTrip> trip = customer.getTrip();
		Integer tripid = null;
		if (!Util.isEmpty(trip) && trip.size() > 0) {
			for (NewTrip newTrip : trip) {
				tripid = newTrip.getId();
				if (!Util.isEmpty(newTrip.getId()) && newTrip.getId() > 0) {
					nutDao.update(newTrip);
				} else {
					newTrip.setOrderid(orderid);
					dbDao.insert(newTrip);
				}
			}
		}
		//同行人员
		List<NewPeerPersionEntity> peerList = customer.getPeerList();
		List<NewPeerPersionEntity> query2 = dbDao.query(NewPeerPersionEntity.class, Cnd.where("tripid", "=", tripid),
				null);
		if (!Util.isEmpty(query2)) {
			dbDao.delete(query2);
		}
		if (!Util.isEmpty(peerList) && peerList.size() > 0) {
			for (NewPeerPersionEntity newPeerPersionEntity : peerList) {
				newPeerPersionEntity.setTripid(tripid);
				dbDao.insert(newPeerPersionEntity);
			}
		}
		//你是否隶属过或捐助过或曾就职于任何专业、社会、慈善组织
		List<NewWorkedplaceEntity> workedplacelist = customer.getWorkedplacelist();
		List<NewWorkedplaceEntity> query5 = dbDao.query(NewWorkedplaceEntity.class,
				Cnd.where("customerid", "=", customer.getId()), null);
		if (!Util.isEmpty(query5)) {
			dbDao.delete(query5);
		}
		if (!Util.isEmpty(workedplacelist) && workedplacelist.size() > 0) {
			for (NewWorkedplaceEntity newLanguageEntity : workedplacelist) {
				newLanguageEntity.setCustomerid(customer.getId());
				dbDao.insert(newLanguageEntity);
			}
		}
		//最近5年是否去过其它国家
		List<NewVisitedcountryEntity> visitedcountrylist = customer.getVisitedcountrylist();
		List<NewVisitedcountryEntity> query4 = dbDao.query(NewVisitedcountryEntity.class,
				Cnd.where("customerid", "=", customer.getId()), null);
		if (!Util.isEmpty(query4)) {
			dbDao.delete(query4);
		}
		if (!Util.isEmpty(visitedcountrylist) && visitedcountrylist.size() > 0) {
			for (NewVisitedcountryEntity newLanguageEntity : visitedcountrylist) {
				newLanguageEntity.setCustomerid(customer.getId());
				dbDao.insert(newLanguageEntity);
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
		//赴美国旅行目的列表
		TravelPurposeEntity travelpurpose = customer.getTravelpurpose();
		if (!Util.isEmpty(travelpurpose)) {
			if (!Util.isEmpty(travelpurpose.getId()) && travelpurpose.getId() > 0) {
				nutDao.update(travelpurpose);
			} else {
				travelpurpose.setCustomerId(customer.getId());
				dbDao.insert(travelpurpose);
			}
		}
		//是否制定了具体旅行计划
		TravelPlanEntity travelplan = customer.getTravelplan();
		if (!Util.isEmpty(travelplan)) {
			if (!Util.isEmpty(travelplan.getId()) && travelplan.getId() > 0) {
				nutDao.update(travelplan);
			} else {
				travelplan.setCustomerId(customer.getId());
				dbDao.insert(travelplan);
			}
		}
		//地点信息
		PlaceInformationEntity placeinformation = customer.getPlaceinformation();
		if (!Util.isEmpty(placeinformation)) {
			if (!Util.isEmpty(placeinformation.getId()) && placeinformation.getId() > 0) {
				nutDao.update(placeinformation);
			} else {
				placeinformation.setCustomerId(customer.getId());
				dbDao.insert(placeinformation);
			}
		}
		//申请的制作者
		ApplicantProducerEntity applicantproducer = customer.getApplicantproducer();
		if (!Util.isEmpty(applicantproducer)) {
			if (!Util.isEmpty(applicantproducer.getId()) && applicantproducer.getId() > 0) {
				nutDao.update(applicantproducer);
			} else {
				applicantproducer.setCustomerId(customer.getId());
				dbDao.insert(applicantproducer);
			}
		}
		return ResultObject.success("修改成功");
	}

	/**
	 * 日本签证信息回显
	 * @param request
	 */
	@RequestMapping(value = "listjpvisainfo")
	@ResponseBody
	public Object listjpvisainfo(HttpServletRequest request) {
		//从session中取出当前登录用户信息
		EmployeeEntity user = (EmployeeEntity) request.getSession().getAttribute(Const.SESSION_NAME);
		long userId = 0;
		if (user == null) {
			throw new JSiteException("请登录后再试!");
		}
		if (!Util.isEmpty(user)) {
			userId = user.getId();
		}

		NewCustomerJpEntity customer = dbDao.fetch(NewCustomerJpEntity.class, Cnd.where("empid", "=", userId));
		long customerId = 0;
		if (!Util.isEmpty(customer)) {
			customerId = customer.getId();//得到客户id
		}
		//工作信息
		List<NewWorkinfoJpEntity> passportlose = dbDao.query(NewWorkinfoJpEntity.class,
				Cnd.where("customer_jp_id", "=", customerId), null);
		if (!Util.isEmpty(passportlose) && passportlose.size() > 0) {
			customer.setWorkinfoJp(passportlose.get(0));
		} else {
			customer.setWorkinfoJp(new NewWorkinfoJpEntity());
		}
		//财务信息
		List<NewFinanceJpEntity> orthercountry = dbDao.query(NewFinanceJpEntity.class,
				Cnd.where("customer_jp_id", "=", customerId), null);
		if (!Util.isEmpty(orthercountry) && orthercountry.size() > 0) {
			customer.setFinanceJpList(orthercountry);
		}
		return customer;
	}

	/***
	 * 日本签证信息修改保存
	 *
	 * @param customer
	 * @return TODO(这里描述每个参数,如果有返回值描述返回值,如果有异常描述异常)
	 */
	@RequestMapping(value = "updateVisaInfoJPSave", method = RequestMethod.POST)
	@ResponseBody
	public Object updateVisaInfoJPSave(@RequestBody NewCustomerJpEntity customer) {

		List<NewCustomerOrderJpEntity> query = dbDao.query(NewCustomerOrderJpEntity.class,
				Cnd.where("customer_jp_id", "=", customer.getId()), null);
		long orderid = query.get(0).getOrder_jp_id();
		dbDao.update(NewOrderJpEntity.class,
				Chain.make("updatetime", new Date()).add("status", OrderVisaApproStatusEnum.firstReview.intKey()),
				Cnd.where("id", "=", orderid));

		//工作信息
		NewWorkinfoJpEntity workinfo = customer.getWorkinfoJp();
		if (!Util.isEmpty(workinfo)) {
			if (!Util.isEmpty(workinfo.getId()) && workinfo.getId() > 0) {
				nutDao.update(workinfo);
			} else {
				workinfo.setCustomer_jp_id(customer.getId());
				dbDao.insert(workinfo);
			}
		}
		//财务信息
		List<NewFinanceJpEntity> orthercountrylist = customer.getFinanceJpList();
		List<NewFinanceJpEntity> query2 = dbDao.query(NewFinanceJpEntity.class,
				Cnd.where("customer_jp_id", "=", customer.getId()), null);
		if (!Util.isEmpty(query2)) {
			dbDao.delete(query2);
		}
		if (!Util.isEmpty(orthercountrylist) && orthercountrylist.size() > 0) {
			for (NewFinanceJpEntity newLanguageEntity : orthercountrylist) {
				newLanguageEntity.setCustomer_jp_id(customer.getId());
				dbDao.insert(newLanguageEntity);
			}
		}
		return ResultObject.success("修改成功");
	}

}
