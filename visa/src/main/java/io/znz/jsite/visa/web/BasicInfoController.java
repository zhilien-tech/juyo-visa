/**
 * BasicInfoController.java
 * io.znz.jsite.visa.web
 * Copyright (c) 2017, 北京科技有限公司版权所有.
*/

package io.znz.jsite.visa.web;

import io.znz.jsite.base.BaseController;
import io.znz.jsite.base.bean.ResultObject;
import io.znz.jsite.core.entity.EmployeeEntity;
import io.znz.jsite.core.util.Const;
import io.znz.jsite.exception.JSiteException;
import io.znz.jsite.visa.dto.NewCustomerJpDto;
import io.znz.jsite.visa.entity.communicathomeaddress.CommunicatHomeAddressEntity;
import io.znz.jsite.visa.entity.communicathomeaddress.CommunicatJPHomeAddressEntity;
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
import io.znz.jsite.visa.entity.japan.NewOldnameJpEntity;
import io.znz.jsite.visa.entity.japan.NewOldpassportJpEntity;
import io.znz.jsite.visa.entity.japan.NewOrthercountryJpEntity;
import io.znz.jsite.visa.entity.taxpayerauthenticationcode.TaxpayerAuthenticationCodeEntity;
import io.znz.jsite.visa.entity.taxpayerauthenticationcode.TaxpayerJPAuthenticationCodeEntity;
import io.znz.jsite.visa.entity.usa.NewCustomerEntity;
import io.znz.jsite.visa.entity.usa.NewCustomerOrderEntity;
import io.znz.jsite.visa.entity.usa.NewOrderEntity;
import io.znz.jsite.visa.enums.IsDadOrMumEnum;

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

import com.uxuexi.core.common.util.Util;
import com.uxuexi.core.db.dao.IDbDao;

/**
 * 基本信息
 * @author   崔建斌
 * @Date	 2017年6月20日 	 
 */
@Controller
@RequestMapping("visa/basicinfo")
public class BasicInfoController extends BaseController {
	@Autowired
	protected IDbDao dbDao;

	@Autowired
	protected Dao nutDao;

	/**
	 * 回显基本信息数据
	 * @param request
	 */
	@RequestMapping(value = "listBasicinfo")
	@ResponseBody
	public Object listBasicinfo(HttpServletRequest request) {
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
		return customer;
	}

	/**
	 * 修改保存基本信息
	 * @param customer
	 */
	@RequestMapping(value = "updateBaseInfoData", method = RequestMethod.POST)
	@ResponseBody
	public Object updateBaseInfoData(@RequestBody NewCustomerEntity customer) {
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
		//美国纳税人认证码
		TaxpayerAuthenticationCodeEntity taxpayerauthenticat = customer.getTaxpayerauthenticat();
		if (!Util.isEmpty(taxpayerauthenticat)) {
			nutDao.update(taxpayerauthenticat);
		} else {
			taxpayerauthenticat.setCustomerId(customer.getId());
			dbDao.insert(taxpayerauthenticat);
		}
		//通信地址与家庭地址是否一致
		CommunicatHomeAddressEntity commhomeaddress = customer.getCommhomeaddress();
		if (!Util.isEmpty(commhomeaddress)) {
			nutDao.update(commhomeaddress);
		} else {
			commhomeaddress.setCustomerId(customer.getId());
			dbDao.insert(commhomeaddress);
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

	/**
	 * 日本基本信息回显
	 * @param request
	 */
	@RequestMapping(value = "basicJPInfoList")
	@ResponseBody
	public Object basicJPInfoList(HttpServletRequest request) {
		//从session中取出当前登录用户信息
		EmployeeEntity user = (EmployeeEntity) request.getSession().getAttribute("fetch");
		long userId = 0;
		if (user == null) {
			throw new JSiteException("请登录后再试!");
		}
		if (!Util.isEmpty(user)) {
			userId = user.getId();
		}
		NewCustomerJpEntity cusdto = dbDao.fetch(NewCustomerJpEntity.class, Cnd.where("empid", "=", userId));
		NewCustomerJpDto customer = new NewCustomerJpDto();
		long customerId = 0;
		if (!Util.isEmpty(cusdto)) {
			customerId = cusdto.getId();//得到客户id
		}
		if (!Util.isEmpty(cusdto)) {
			customer.setId(customerId);//主键
			customer.setNameTelegramCode(cusdto.getNameTelegramCode());//姓名电报码
			customer.setEmail(cusdto.getEmail());//电子邮箱
			customer.setPassporttype(cusdto.getPassporttype());//护照类型
			customer.setMarrystate(cusdto.getMarrystate());//婚姻状况
			customer.setBirthday(cusdto.getBirthday());//出生日期
			customer.setIdcard(cusdto.getIdcard());//身份证号
			customer.setCountrynum(cusdto.getCountrynum());//国家码
			customer.setChinesefullname(cusdto.getChinesefullname());//姓名
			customer.setPassport(cusdto.getPassport());//护照号
			customer.setChinesexingen(cusdto.getChinesexingen());//中文姓拼音
			customer.setGender(cusdto.getGender());//性别
			customer.setBirthcountry(cusdto.getDocountry());//国籍
			customer.setBirthprovince(cusdto.getBirthprovince());//出生地点（省份）
			customer.setPassportsenddate(cusdto.getPassportsenddate());//签发日期
			customer.setPassportsendprovice(cusdto.getPassportsendplace());//签发地点（省份）
			customer.setPassportsendcity(cusdto.getPassportsendcity());//签发地城市
			customer.setPassporteffectdate(cusdto.getPassporteffectdate());//有效期至
			customer.setVisaoffice(cusdto.getPassportsendoffice());//签发机关
			customer.setPassportbooknum(cusdto.getPassportbooknum());//护照本号码
			customer.setPassportreadnum(cusdto.getPassportreadnum());//护照机读码
		}
		//我有曾用名
		List<NewOldnameJpEntity> father = dbDao.query(NewOldnameJpEntity.class,
				Cnd.where("customer_jp_id", "=", customerId), null);
		if (!Util.isEmpty(father) && father.size() > 0) {
			customer.setOldname(father.get(0));
		} else {
			customer.setOldname(new NewOldnameJpEntity());
		}
		//是否是其它国家/地区的永久居民
		List<NewOrthercountryJpEntity> orthercountrylist = dbDao.query(NewOrthercountryJpEntity.class,
				Cnd.where("customer_jp_id", "=", customerId), null);
		if (!Util.isEmpty(orthercountrylist) && orthercountrylist.size() > 0) {
			customer.setOrthercountrylist(orthercountrylist);
		}
		//日本纳税人认证码
		List<TaxpayerJPAuthenticationCodeEntity> taxpayerauthenticat = dbDao.query(
				TaxpayerJPAuthenticationCodeEntity.class, Cnd.where("customerId", "=", customerId), null);
		if (!Util.isEmpty(taxpayerauthenticat) && taxpayerauthenticat.size() > 0) {
			customer.setTaxpayerauthenticat(taxpayerauthenticat.get(0));
		} else {
			customer.setTaxpayerauthenticat(new TaxpayerJPAuthenticationCodeEntity());
		}
		//日本通信地址与家庭地址是否一致
		List<CommunicatJPHomeAddressEntity> commhomeaddress = dbDao.query(CommunicatJPHomeAddressEntity.class,
				Cnd.where("customerId", "=", customerId), null);
		if (!Util.isEmpty(commhomeaddress) && commhomeaddress.size() > 0) {
			customer.setCommhomeaddress(commhomeaddress.get(0));
		} else {
			customer.setCommhomeaddress(new CommunicatJPHomeAddressEntity());
		}
		//护照是否遗失过或被偷过
		List<NewOldpassportJpEntity> passportlose = dbDao.query(NewOldpassportJpEntity.class,
				Cnd.where("customer_jp_id", "=", customerId), null);
		if (!Util.isEmpty(passportlose) && passportlose.size() > 0) {
			customer.setPassportlose(passportlose.get(0));
		} else {
			customer.setPassportlose(new NewOldpassportJpEntity());
		}
		return customer;
	}

	/**
	 * 日本基本信息编辑保存
	 * @param customer
	 */
	@RequestMapping(value = "updateBaseJPInfoData", method = RequestMethod.POST)
	@ResponseBody
	public Object updateBaseJPInfoData(@RequestBody NewCustomerJpDto customer, HttpServletRequest request) {
		//从session中取出当前登录用户信息
		EmployeeEntity user = (EmployeeEntity) request.getSession().getAttribute("fetch");
		long userId = 0;
		if (user == null) {
			throw new JSiteException("请登录后再试!");
		}
		if (!Util.isEmpty(user)) {
			userId = user.getId();
		}
		NewCustomerJpEntity cus = new NewCustomerJpEntity();
		if (!Util.isEmpty(customer)) {
			cus.setId(customer.getId());//主键
			cus.setNameTelegramCode(customer.getNameTelegramCode());//姓名电报码
			cus.setEmail(customer.getEmail());//电子邮箱
			cus.setPassporttype(customer.getPassporttype());//护照类型
			cus.setMarrystate(customer.getMarrystate());//婚姻状况
			cus.setBirthday(customer.getBirthday());//出生日期
			cus.setIdcard(customer.getIdcard());//身份证号
			cus.setCountrynum(customer.getCountrynum());//国家码
			cus.setChinesefullname(customer.getChinesefullname());//姓名
			cus.setPassport(customer.getPassport());//护照号
			cus.setChinesexingen(customer.getChinesexingen());//中文姓拼音
			cus.setGender(customer.getGender());//性别
			cus.setDocountry(customer.getBirthcountry());//国籍
			cus.setBirthprovince(customer.getBirthprovince());//出生地点（省份）
			cus.setPassportsenddate(customer.getPassportsenddate());//签发日期
			cus.setPassportsendplace(customer.getPassportsendprovice());//签发地点（省份）
			cus.setPassportsendcity(customer.getPassportsendcity());//签发地城市
			cus.setPassporteffectdate(customer.getPassporteffectdate());//有效期至
			cus.setPassportsendoffice(customer.getVisaoffice());//签发机关
			cus.setPassportbooknum(customer.getPassportbooknum());//护照本号码
			cus.setPassportreadnum(customer.getPassportreadnum());//护照机读码
			nutDao.updateIgnoreNull(cus);
		}
		//我有曾用名
		NewOldnameJpEntity oldname = customer.getOldname();
		if (!Util.isEmpty(oldname)) {
			if (!Util.isEmpty(oldname.getId()) && oldname.getId() > 0) {
				nutDao.updateIgnoreNull(oldname);
			} else {
				oldname.setCustomer_jp_id(customer.getId());
				dbDao.insert(oldname);
			}
		}
		//是否是其它国家/地区的永久居民
		List<NewOrthercountryJpEntity> orthercountrylist = customer.getOrthercountrylist();
		if (!Util.isEmpty(orthercountrylist) && orthercountrylist.size() > 0) {
			for (NewOrthercountryJpEntity newLanguageEntity : orthercountrylist) {
				if (!Util.isEmpty(newLanguageEntity.getId()) && newLanguageEntity.getId() > 0) {
					nutDao.updateIgnoreNull(newLanguageEntity);
				} else {
					newLanguageEntity.setCustomer_jp_id(customer.getId());
					dbDao.insert(newLanguageEntity);
				}
			}
		}
		//日本纳税人认证码
		TaxpayerJPAuthenticationCodeEntity taxpayerauthenticat = customer.getTaxpayerauthenticat();
		if (!Util.isEmpty(taxpayerauthenticat) && taxpayerauthenticat.getId() > 0) {
			nutDao.updateIgnoreNull(taxpayerauthenticat);
		} else {
			taxpayerauthenticat.setCustomerId(customer.getId());
			dbDao.insert(taxpayerauthenticat);
		}

		//日本通信地址与家庭地址是否一致
		CommunicatJPHomeAddressEntity commhomeaddress = customer.getCommhomeaddress();
		if (!Util.isEmpty(commhomeaddress) && commhomeaddress.getId() > 0) {
			nutDao.updateIgnoreNull(commhomeaddress);
		} else {
			commhomeaddress.setCustomerId(customer.getId());
			dbDao.insert(commhomeaddress);
		}
		//护照是否遗失过或被偷过
		NewOldpassportJpEntity passportlose = customer.getPassportlose();
		if (!Util.isEmpty(passportlose)) {
			if (!Util.isEmpty(passportlose.getId()) && passportlose.getId() > 0) {
				nutDao.updateIgnoreNull(passportlose);
			} else {
				passportlose.setCustomer_jp_id(customer.getId());
				dbDao.insert(passportlose);
			}
		}
		return ResultObject.success("修改成功");
	}
}
