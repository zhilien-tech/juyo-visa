/**
 * SimulateService.java
 * io.znz.jsite.visa.simulator.service
 * Copyright (c) 2017, 北京科技有限公司版权所有.
*/

package io.znz.jsite.visa.simulator.service;

import io.znz.jsite.base.NutzBaseService;
import io.znz.jsite.base.bean.ResultObject;
import io.znz.jsite.exception.JSiteException;
import io.znz.jsite.util.DateUtils;
import io.znz.jsite.util.StringUtils;
import io.znz.jsite.visa.bean.Option;
import io.znz.jsite.visa.bean.helper.Gender;
import io.znz.jsite.visa.bean.helper.Period;
import io.znz.jsite.visa.bean.helper.Relation;
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
import io.znz.jsite.visa.entity.usa.NewPayCompanyEntity;
import io.znz.jsite.visa.entity.usa.NewPayPersionEntity;
import io.znz.jsite.visa.entity.usa.NewPeerPersionEntity;
import io.znz.jsite.visa.entity.usa.NewTrip;
import io.znz.jsite.visa.enums.ArmyEnum;
import io.znz.jsite.visa.enums.DayTypeEnum;
import io.znz.jsite.visa.enums.GenderEnum;
import io.znz.jsite.visa.enums.IsDadOrMumEnum;
import io.znz.jsite.visa.enums.IsusaEnum;
import io.znz.jsite.visa.enums.LanguageEnum;
import io.znz.jsite.visa.enums.OrderVisaApproStatusEnum;
import io.znz.jsite.visa.enums.PayPersionRelationEnum;
import io.znz.jsite.visa.enums.PeerPersionRelationWithMeEnum;
import io.znz.jsite.visa.enums.RelationInUSAIdentityEnum;
import io.znz.jsite.visa.enums.RelationRelativeWithMeEnum;
import io.znz.jsite.visa.enums.SpouseEnum;
import io.znz.jsite.visa.enums.USARelationWithMeEnum;
import io.znz.jsite.visa.service.TelecodeService;
import io.znz.jsite.visa.simulator.dto.ArmyDto;
import io.znz.jsite.visa.simulator.dto.CustomerDto;
import io.znz.jsite.visa.simulator.dto.FamilyDto;
import io.znz.jsite.visa.simulator.dto.HistoryDto;
import io.znz.jsite.visa.simulator.dto.OldNameDto;
import io.znz.jsite.visa.simulator.dto.PassportDto;
import io.znz.jsite.visa.simulator.dto.SchoolDto;
import io.znz.jsite.visa.simulator.dto.SpouseDto;
import io.znz.jsite.visa.simulator.dto.TogetherDto;
import io.znz.jsite.visa.simulator.dto.TravelDto;
import io.znz.jsite.visa.simulator.dto.UsaDto;
import io.znz.jsite.visa.simulator.dto.WorkDto;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.joda.time.DateTime;
import org.nutz.dao.Cnd;
import org.nutz.dao.pager.Pager;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.uxuexi.core.common.util.DateTimeUtil;
import com.uxuexi.core.common.util.DateUtil;
import com.uxuexi.core.common.util.EnumUtil;
import com.uxuexi.core.common.util.Util;

/**
 * 自动填表service
 * 注意:当前日期与出生日期的差，也就是客户年龄满14周岁才会出现[工作/教育/培训]
 * 	
 * @author   朱晓川
 * @Date	 2017年7月6日 	 
 */
@Service
public class SimulateViewService extends NutzBaseService<NewCustomerEntity> {

	private static final DateFormat dateFormat = new SimpleDateFormat("MMM", Locale.ENGLISH);
	private static final DecimalFormat decimalFormat = new DecimalFormat("00");

	//历史出行 目的地的国家 默认为美国
	private static final String defaultCountry = "USA";

	/**工作年龄，14周岁*/
	private static final int WORK_AGE = 14;

	private static Log log = Logs.get();

	@Autowired
	private TelecodeService telecodeService;

	/**查询第一个可提交签证网站的客户信息*/
	public ResultObject fetchCustomer4SimulatorUSA() {

		//1,客户基本信息,取第一个可用的
		List<NewCustomerEntity> cusLst = dbDao.query(NewCustomerEntity.class,
				Cnd.where("status", "=", OrderVisaApproStatusEnum.readySubmit.intKey()), new Pager());

		if (!Util.isEmpty(cusLst)) {
			NewCustomerEntity nCustomer = cusLst.get(0);
			Integer customerId = nCustomer.getId();
			CustomerDto customer = transfrom(nCustomer, customerId);
			ResultObject ro = ResultObject.success(formatJson(customer));
			ro.addAttribute("oid", nCustomer.getId());
			ro.addAttribute("avatar", nCustomer.getPhoneurl());

			//TODO  查询客户的白底免冠照片
			//			Photo photo = photoService.findByKeyAndCustomer("avatar", c.getId());
			//			if (photo != null) {
			//				ro.addAttribute("avatar", photo.getValue());
			//			} else {
			//				c.setState(State.REFUSE);
			//				String msg = "白底免冠照片不存在!";
			//				c.setReason(msg);
			//				return ResultObject.fail(msg);
			//			}
			return ro;
		}

		return ResultObject.fail("暂无任务");
	}

	/**设置基本信息*/
	private void transfromBasic(NewCustomerEntity n, CustomerDto c) {
		c.setId(Long.valueOf(n.getId()));

		/*-----------基本信息-----------*/
		c.setFirstName(n.getChinesename());
		c.setFirstNameEN(n.getChinesenameen());
		c.setLastName(n.getChinesexing());
		c.setLastNameEN(n.getChinesexingen());
		c.setIdCardNo(n.getIdcard());

		Integer gender = n.getGender();
		String genderVal = EnumUtil.getValue(GenderEnum.class, gender);
		c.setGender(genderVal);

		/*-----------护照信息-----------*/
		c.setPassport(n.getPassport());
		c.setIssueDate(n.getPassportsenddate()); //签发时间
		c.setIssueProvince(n.getPassportsendprovice());
		c.setIssueCity(n.getPassportsendcity());
		c.setExpiryDate(n.getPassporteffectdate()); //有效期

		/*----------出生信息------------*/
		c.setBirthday(n.getBirthdate());
		c.setBirthCountry(n.getBirthcountry());
		c.setBirthProvince(n.getBirthprovince());
		c.setBirthCity(n.getBirthcity());

		/*-----------现居地信息-----------*/
		c.setCountry(n.getNowcountry());//国籍
		c.setProvince(n.getNowprovince());
		c.setCity(n.getNowcity());

		c.setMobile(n.getPhone()); //手机
		c.setPhone(n.getFamilyphone()); //座机
		c.setEmail(n.getEmail()); //邮箱 

		c.setAddress(n.getAddressbig());
		c.setRoom(n.getAddresssmall());
		c.setAddressEN(n.getAddressbigen());
		c.setRoomEN(n.getAddressmallen());
		c.setZipCode(n.getZipcode()); //邮政编码
	}

	private CustomerDto transfrom(NewCustomerEntity nCustomer, Integer customerId) {
		CustomerDto c = new CustomerDto();

		//1 基本信息
		transfromBasic(nCustomer, c);

		//2其他国家常住居民
		List<NewOrthercountryEntity> otherCountryLst = dbDao.query(NewOrthercountryEntity.class,
				Cnd.where("customerid", "=", customerId), null);
		List<Option> otherCountry = Lists.transform(otherCountryLst, new Function<NewOrthercountryEntity, Option>() {

			@Override
			public Option apply(NewOrthercountryEntity from) {
				Option to = new Option();
				to.setText(from.getCustomerid() + ""); //使用customerid作为text
				to.setValue(from.getCountry());
				return to;
			}
		});

		//3会说的语言
		List<NewLanguageEntity> languageLst = dbDao.query(NewLanguageEntity.class,
				Cnd.where("customerid", "=", customerId), null);

		List<Option> languages = Lists.transform(languageLst, new Function<NewLanguageEntity, Option>() {

			@Override
			public Option apply(NewLanguageEntity from) {
				Option to = new Option();
				to.setText(from.getCustomerid() + ""); //使用customerid作为text
				LanguageEnum language = EnumUtil.get(LanguageEnum.class, from.getLanguage());
				to.setValue(language.value());
				return to;
			}
		});

		//4去过的国家
		List<NewVisitedcountryEntity> visitedCountryLst = dbDao.query(NewVisitedcountryEntity.class,
				Cnd.where("customerid", "=", customerId), null);
		List<Option> visitedCountry = Lists.transform(visitedCountryLst,
				new Function<NewVisitedcountryEntity, Option>() {
					@Override
					public Option apply(NewVisitedcountryEntity from) {
						Option to = new Option();
						to.setText(from.getCustomerid() + ""); //使用customerid作为text
						to.setValue(from.getCountry());
						return to;
					}
				});

		//5所属公益组织
		List<NewWorkedplaceEntity> commonwealsLst = dbDao.query(NewWorkedplaceEntity.class,
				Cnd.where("customerid", "=", customerId), null);
		List<Option> commonweals = Lists.transform(commonwealsLst, new Function<NewWorkedplaceEntity, Option>() {
			@Override
			public Option apply(NewWorkedplaceEntity from) {
				Option to = new Option();
				to.setText(from.getCustomerid() + ""); //使用customerid作为text
				to.setValue(from.getWorkedplace());
				return to;
			}
		});

		c.setOtherCountry(otherCountry);
		c.setLanguages(languages);
		c.setVisitCountry(visitedCountry);
		c.setCharitable(commonweals);

		//财务证明  美国  没有
		/*--------------------------------------------------------*/

		//6学历   visa_new_teachinfo 
		List<NewTeachinfoEntity> techInfoLst = dbDao.query(NewTeachinfoEntity.class,
				Cnd.where("customerid", "=", customerId), null);
		List<SchoolDto> techInfo = Lists.transform(techInfoLst, new Function<NewTeachinfoEntity, SchoolDto>() {
			@Override
			public SchoolDto apply(NewTeachinfoEntity from) {
				SchoolDto to = new SchoolDto();
				to.setId(from.getId());

				to.setRoom(from.getSchooladdresssmall());
				to.setRoomEN(from.getSchooladdresssmallen());

				to.setAddress(from.getSchooladdressbig());
				to.setAddressEN(from.getSchooladdressbigen());

				to.setCity(from.getCity());
				to.setProvince(from.getPrevince());
				to.setCountry(from.getCountry());
				//美国签证网站现在不要求填写学位
				to.setDegree(from.getMajor());
				to.setDegreeEN(from.getMajoren());

				to.setStartDate(from.getStartdate());
				to.setEndDate(from.getEnddate());

				to.setName(from.getSchoolname());
				to.setNameEN(from.getSchoolnameen());
				to.setPhone(from.getSchoolphone());

				to.setSpecialty(from.getMajor());
				to.setSpecialtyEN(from.getMajoren());

				to.setZipCode(from.getZipcode());
				return to;
			}
		});
		c.setSchools(techInfo);

		// 7当前工作信息  visa_new_workinfo
		List<NewWorkinfoEntity> workInfoLst = dbDao.query(NewWorkinfoEntity.class,
				Cnd.where("customerid", "=", customerId), null);

		List<WorkDto> workInfo = Lists.transform(workInfoLst, new Function<NewWorkinfoEntity, WorkDto>() {
			@Override
			public WorkDto apply(NewWorkinfoEntity from) {
				WorkDto to = new WorkDto();
				to.setAddress(from.getUnitaddressbig());
				to.setAddressEN(from.getUnitaddressbigen());

				to.setBewrite(from.getJobdescription());
				to.setBewriteEN(from.getJobdescriptionen());

				//当前工作信息没有老板姓名
				to.setCity(from.getNowcity());
				to.setCountry(from.getNowcountry());
				to.setCurrent(true);

				to.setDuty(from.getJobduty());
				to.setDutyEN(from.getJobdutyen());
				to.setId(from.getId());

				to.setIndustry(from.getJobstatus());
				to.setJob(from.getNowjob());
				to.setJobEN(from.getNowjoben());

				to.setName(from.getUnitname());
				to.setNameEN(from.getUnitnameen());
				to.setPhone(from.getUnitphone());
				to.setProvince(from.getNowprevince());

				to.setRoom(from.getUnitaddresssmall());
				to.setRoomEN(from.getUnitaddresssmallen());
				to.setSalary(from.getMonthmoney());

				to.setZipCode(from.getZipcode());

				return to;
			}
		});

		if (!Util.isEmpty(workInfo)) {
			if (workInfo.size() == 1) {
				WorkDto work = workInfo.get(0);
				c.setWork(work);
			} else {
				log.error("当前工作信息只能有一个");
			}
		}

		//8过往工作信息   visa_new_oldworks
		List<NewOldworksEntity> prevWorkInfoLst = dbDao.query(NewOldworksEntity.class,
				Cnd.where("customerid", "=", customerId), null);

		List<WorkDto> prevWorkInfo = Lists.transform(prevWorkInfoLst, new Function<NewOldworksEntity, WorkDto>() {
			@Override
			public WorkDto apply(NewOldworksEntity from) {
				WorkDto to = new WorkDto();
				to.setAddress(from.getUnitaddressbig());
				to.setAddressEN(from.getUnitaddressbigen());

				//过往工作信息没有职务描述
				to.setBossFirstName(from.getChargename());
				to.setBossFirstNameEN(from.getChargenameen());
				to.setBossLastName(from.getChargexing());
				to.setBossLastNameEN(from.getChargexingen());

				to.setCity(from.getCity());
				to.setCountry(from.getCountry());
				to.setCurrent(true);

				to.setDuty(from.getWorkduty());
				to.setDutyEN(from.getWorkdutyen());
				to.setId(from.getId());

				//无行业
				to.setJob(from.getJob());
				to.setJobEN(from.getJoben());

				to.setName(from.getUnitname());
				to.setNameEN(from.getUnitnameen());
				to.setPhone(from.getPhone());
				to.setProvince(from.getPrevince());

				to.setRoom(from.getUnitaddresssmall());
				to.setRoomEN(from.getUnitaddresssmallen());

				//无月薪
				to.setStartDate(from.getStartdate());
				to.setEndDate(from.getEnddate());
				to.setZipCode(from.getZipcode());
				return to;
			}
		});
		c.setWorks(prevWorkInfo);

		//  9家人亲戚  visa_new_relation
		List<NewRelationEntity> familyRelations = dbDao.query(NewRelationEntity.class,
				Cnd.where("customerid", "=", customerId), null);

		List<FamilyDto> families = Lists.transform(familyRelations, new Function<NewRelationEntity, FamilyDto>() {
			@Override
			public FamilyDto apply(NewRelationEntity from) {
				FamilyDto to = new FamilyDto();

				//无出生日期
				to.setFirstName(from.getName());
				to.setFirstNameEN(from.getNameen());
				to.setLastName(from.getXing());
				to.setLastNameEN(from.getXingen());

				to.setId(from.getId());
				to.setInUsa(from.getDirect() == 1);

				//亲属关系
				String relation2Me = EnumUtil.getValue(RelationRelativeWithMeEnum.class, from.getRelationme());
				to.setRelation(relation2Me);
				to.setUsaAddress(from.getUsaaddress());
				to.setUsaPhone(from.getUsaphone());

				//在美身份 S，C，P，O  
				Integer usaidentifyKey = from.getUsaidentify();
				String usaStatus = EnumUtil.getValue(RelationInUSAIdentityEnum.class, usaidentifyKey);
				to.setUsaStatus(usaStatus);

				return to;
			}
		});
		c.setFamilies(families);

		// 10历史出行记录  visa_new_recentlyintousa
		List<NewRecentlyintousaEntity> travelHistories = dbDao.query(NewRecentlyintousaEntity.class,
				Cnd.where("customerid", "=", customerId), null);

		List<HistoryDto> travelHistory = Lists.transform(travelHistories,
				new Function<NewRecentlyintousaEntity, HistoryDto>() {
					@Override
					public HistoryDto apply(NewRecentlyintousaEntity from) {
						HistoryDto to = new HistoryDto();
						to.setArrivalDate(from.getIntousadate());
						to.setDestination(defaultCountry);
						to.setId(from.getId());

						String stayunit = from.getStayunit();
						Period period = EnumUtil.get(Period.class, stayunit);
						if (Util.isEmpty(period)) {
							log.error("历史出行美国记录,停留时间单位不正确");
						}
						log.info("历史出行美国记录,停留时间单位:" + stayunit);

						to.setPeriod(period);

						//无备注
						to.setStay(from.getStayday());
						return to;
					}
				});
		c.setHistories(travelHistory);

		//11 配偶信息 visa_new_spouse  NewSpouseEntity
		List<NewSpouseEntity> spouseLst = dbDao.query(NewSpouseEntity.class, Cnd.where("customerid", "=", customerId),
				null);

		List<SpouseDto> spouses = Lists.transform(spouseLst, new Function<NewSpouseEntity, SpouseDto>() {
			@Override
			public SpouseDto apply(NewSpouseEntity from) {
				SpouseDto to = new SpouseDto();
				to.setAddress(from.getSpouselinkaddress());
				to.setAddressEN(from.getSpouselinkaddressen());
				to.setBirthCity(from.getSpousebirthcity());
				to.setBirthCountry(from.getSpousebirthcountry());
				to.setBirthday(from.getSpousebirthday());
				to.setBirthProvince(from.getSpousebirthprevince());

				to.setCity(from.getSpousenowcity());
				to.setCompany(from.getSpouseunitname());
				to.setCompanyEN(from.getSpouseunitnameen());
				to.setCountry(from.getSpousecountry());
				to.setDivorceCountry(from.getSplitcountry());
				to.setDivorceDate(from.getSplitdate());
				to.setDivorceReason(from.getSplitreason());
				to.setDivorceReasonEN(from.getSplitreasonen());

				to.setFirstName(from.getSpousename());
				to.setFirstNameEN(from.getSpousenameen());
				to.setId(from.getId());

				to.setLastName(from.getSpousexing());
				to.setLastNameEN(from.getSpousexingen());
				to.setNationality(from.getSpousecountry());
				to.setPhone(from.getSpouseunitphone());
				to.setProvince(from.getSpousenowprevince());
				//配偶状态
				String spouseState = EnumUtil.getValue(SpouseEnum.class, from.getMarrystatus());
				to.setState(spouseState);
				to.setWedDate(from.getMarrydate());
				to.setZipCode(from.getSpousezipcode());
				return to;
			}
		});

		if (!Util.isEmpty(spouses)) {
			if (spouses.size() == 1) {
				SpouseDto spouse = spouses.get(0);
				c.setSpouse(spouse);
			} else {
				log.error("配偶只能有一个");
			}
		}

		//12 服役信息
		List<NewArmyEntity> armyLst = dbDao.query(NewArmyEntity.class, Cnd.where("customerid", "=", customerId), null);
		List<ArmyDto> armyInfo = Lists.transform(armyLst, new Function<NewArmyEntity, ArmyDto>() {
			@Override
			public ArmyDto apply(NewArmyEntity from) {
				ArmyDto to = new ArmyDto();
				//军种
				to.setType(EnumUtil.getValue(ArmyEnum.class, from.getArmytype()));
				to.setCountry(from.getCountry());
				to.setEndDate(from.getEnddate());
				to.setId(from.getId());
				to.setRank(from.getArmyname());
				to.setSpecialty(from.getArmydo());
				to.setStartDate(from.getStartdate());

				return to;
			}
		});

		if (!Util.isEmpty(armyInfo)) {
			if (armyInfo.size() == 1) {
				ArmyDto army = armyInfo.get(0);
				c.setArmy(army);
			} else {
				log.error("服役信息只能有一个");
			}
		}

		//13 美国相关信息
		NewUsainfoEntity usaEntity = dbDao.fetch(NewUsainfoEntity.class, Cnd.where("customerid", "=", customerId));
		if (!Util.isEmpty(usaEntity)) {
			UsaDto usa = new UsaDto();
			usa.setDrivingLicense(usaEntity.getUsadriveport());
			usa.setDrivingLicenseProvince(usaEntity.getUsavisastate());
			usa.setId(usaEntity.getId());
			usa.setImmigrant(usaEntity.getInstruction());
			usa.setImmigrantEN(usaEntity.getInstructionen());
			usa.setOldVisa(usaEntity.getVisaport());
			usa.setOldVisaCity(usaEntity.getSendprevince());
			usa.setOldVisaDate(usaEntity.getVisadate());
			usa.setOldVisaType(usaEntity.getVisatype());
			usa.setSameAsThis(usaEntity.isSameaslast());
			c.setUsa(usa);
		}

		//14 旧护照信息
		NewPassportloseEntity oldPassPortEntity = dbDao.fetch(NewPassportloseEntity.class,
				Cnd.where("customerid", "=", customerId));

		if (!Util.isEmpty(oldPassPortEntity)) {
			PassportDto oldPassport = new PassportDto();
			Option country = new Option();
			country.setText(oldPassPortEntity.getCustomerid() + "");
			country.setValue(oldPassPortEntity.getSendcountry());

			oldPassport.setCountry(country);
			oldPassport.setId(oldPassPortEntity.getId());
			oldPassport.setPassport(oldPassPortEntity.getPassport());
			oldPassport.setWhy(oldPassPortEntity.getReason());
			oldPassport.setWhyEN(oldPassPortEntity.getReasonen());
			c.setOldPassport(oldPassport);
		}

		//15 Travel info
		NewCustomerOrderEntity map = dbDao
				.fetch(NewCustomerOrderEntity.class, Cnd.where("customerid", "=", customerId));
		NewTrip travel = dbDao.fetch(NewTrip.class, Cnd.where("orderid", "=", map.getOrderid()));
		if (!Util.isEmpty(travel)) {
			TravelDto trDto = new TravelDto();
			trDto.setAddress(travel.getDetailaddress());
			trDto.setArrivalDate(travel.getArrivedate());

			trDto.setContactsAddress(travel.getLinkaddress());
			trDto.setContactsCity(travel.getLinkcity());
			trDto.setContactsEmail(travel.getEmail());
			trDto.setContactsFirstName(travel.getLinkname());
			trDto.setContactsFirstNameEN(travel.getLinknameen());
			trDto.setContactsLastName(travel.getLinkxing());
			trDto.setContactsLastNameEN(travel.getLinkxingen());
			trDto.setContactsPhone(travel.getPhone());
			trDto.setContactsProvince(travel.getLinkstate());

			//美国联系人关系
			String relation2Me = EnumUtil.getValue(USARelationWithMeEnum.class, travel.getLinkrelation());
			trDto.setContactsRelation(relation2Me);
			trDto.setContactsZipCode(travel.getLinkzipcode());

			trDto.setCountry(defaultCountry);
			trDto.setEntryCity(travel.getIntocity());
			trDto.setEntryProvince(travel.getIntostate());
			trDto.setHotel(travel.getUsahotel());
			trDto.setId(travel.getId());

			int staytypeKey = travel.getStaytype();
			String staytype = EnumUtil.getValue(DayTypeEnum.class, staytypeKey);
			Period period = EnumUtil.get(Period.class, staytype);
			trDto.setPeriod(period);
			trDto.setStay(travel.getStaytime());
			trDto.setTeam(travel.getTeamname());
			trDto.setZipCode(travel.getZipcode());

			//谁支付
			trDto.setPayer(travel.getPaypersion());

			//支付公司信息
			NewPayCompanyEntity company = dbDao.fetch(NewPayCompanyEntity.class,
					Cnd.where("orderid", "=", map.getOrderid()));
			if (!Util.isEmpty(company)) {
				trDto.setCompanyAddress(company.getComaddress());
				trDto.setCompanyAddressEN(company.getComaddressen());
				trDto.setCompanyCity(company.getComcity());
				trDto.setCompanyCountry(company.getComcountry());
				trDto.setCompanyName(company.getComname());
				trDto.setCompanyNameEN(company.getComnameen());
				trDto.setCompanyPhone(company.getComphone());

				//付费公司省份
				trDto.setCompanyProvince(company.getComprovince());

				trDto.setCompanyRelation(company.getComrelation());
				trDto.setCompanyRelationEN(company.getComrelationen());
				trDto.setCompanyRoom(company.getComdetailaddress());
				trDto.setCompanyRoomEN(company.getComdetailaddressen());
				trDto.setCompanyZipCode(company.getComzipcode());
			}

			//支付人信息
			NewPayPersionEntity payer = dbDao.fetch(NewPayPersionEntity.class,
					Cnd.where("orderid", "=", map.getOrderid()));

			if (!Util.isEmpty(payer)) {
				trDto.setPayerEmail(payer.getEmail());
				trDto.setPayerFirstName(payer.getName());
				trDto.setPayerFirstNameEN(payer.getNameen());
				trDto.setPayerLastName(payer.getXing());
				trDto.setPayerLastNameEN(payer.getXingen());
				trDto.setPayerPhone(payer.getPhone());

				//支付人关系
				String payerRelation = EnumUtil.getValue(PayPersionRelationEnum.class, payer.getRelation());
				trDto.setPayerRelation(payerRelation);
			}

			//查询同行人
			int tripid = travel.getId();
			List<NewPeerPersionEntity> companionLst = dbDao.query(NewPeerPersionEntity.class,
					Cnd.where("tripid", "=", tripid), null);
			List<TogetherDto> companions = Lists.transform(companionLst,
					new Function<NewPeerPersionEntity, TogetherDto>() {
						@Override
						public TogetherDto apply(NewPeerPersionEntity from) {
							TogetherDto to = new TogetherDto();
							to.setFirstName(from.getPeername());
							to.setFirstNameEN(from.getPeernameen());

							to.setId(from.getId());
							to.setLastName(from.getPeerxing());
							to.setLastNameEN(from.getPeerxingen());

							String relation2Me = EnumUtil.getValue(PeerPersionRelationWithMeEnum.class,
									from.getRelationme());
							to.setRelation(relation2Me);
							return to;
						}
					});
			trDto.setTogethers(companions);
			c.setTravel(trDto);
		}

		//16 曾用名信息
		NewOldnameEntity oldNameEntity = dbDao.fetch(NewOldnameEntity.class, Cnd.where("customerid", "=", customerId));
		if (!Util.isEmpty(oldNameEntity)) {
			OldNameDto oldName = new OldNameDto();
			oldName.setOldFirstName(oldNameEntity.getOldname());
			oldName.setOldFirstNameEN(oldNameEntity.getOldnameen());
			oldName.setOldLastName(oldNameEntity.getOldxing());
			oldName.setOldLastNameEN(oldNameEntity.getOldxingen());
			c.setOldName(oldName);
		}

		//17 父亲
		NewParentsEntity fatherEntity = dbDao.fetch(NewParentsEntity.class, Cnd.where("customerid", "=", customerId)
				.and("dadormum", "=", IsDadOrMumEnum.dad.intKey()));
		if (!Util.isEmpty(fatherEntity)) {
			FamilyDto father = new FamilyDto();

			father.setFirstName(fatherEntity.getName());
			father.setFirstNameEN(fatherEntity.getNameen());
			father.setLastName(fatherEntity.getXing());
			father.setLastNameEN(fatherEntity.getXingen());

			father.setBirthday(fatherEntity.getBirthday());

			father.setId(fatherEntity.getId());
			father.setInUsa(fatherEntity.getStayusa() == IsusaEnum.yes.intKey());
			father.setRelation(IsDadOrMumEnum.dad.value());
			c.setFather(father);
		}

		//18 母亲
		NewParentsEntity mumEntity = dbDao.fetch(NewParentsEntity.class,
				Cnd.where("customerid", "=", customerId).and("dadormum", "=", IsDadOrMumEnum.mum.intKey()));

		if (!Util.isEmpty(mumEntity)) {
			FamilyDto mum = new FamilyDto();

			mum.setFirstName(fatherEntity.getName());
			mum.setFirstNameEN(fatherEntity.getNameen());
			mum.setLastName(fatherEntity.getXing());
			mum.setLastNameEN(fatherEntity.getXingen());

			mum.setBirthday(mumEntity.getBirthday());

			mum.setId(fatherEntity.getId());
			mum.setInUsa(fatherEntity.getStayusa() == IsusaEnum.yes.intKey());
			mum.setRelation(IsDadOrMumEnum.mum.value());
			c.setMother(mum);
		}
		return c;
	}

	private boolean isContainChinese(String str) {
		Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
		Matcher m = p.matcher(str);
		if (m.find()) {
			return true;
		}
		return false;
	}

	private String dateFormatLocal(Date date) {
		if (date == null)
			date = new Date();
		return dateFormat.format(date).toUpperCase();
	}

	/**过滤特殊字符s*/
	private String filter(String str) throws PatternSyntaxException {
		// 清除掉所有特殊字符
		String regEx = "[`~!#$%^&*()+=|{}':;'\\[\\]<>/?~！#￥%……&*（）——+|{}【】‘；：”“’。，,、？.@]";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		return m.replaceAll("").trim();
	}

	private void validator(Map<String, Object> map, String key, Object value) {
		if (value == null) {
			value = "";
		} else if (value instanceof String) {

			//校验中文
			Assert.isTrue(!isContainChinese((String) value), "数据错误不能包含中文，key:" + key + "\n" + value);
			//过滤去除特殊字符
			if (!key.contains("EMAIL"))
				value = filter((String) value);
		}
		map.put(key, value);
	}

	/*
	 * 在处理页面多个值的时候，json中使用空对象List即可，脚本只使用了集合的size判断有多少个值
	 */
	private Map<String, Object> formatJson(CustomerDto customer) {
		Map<String, Object> map = new TreeMap<String, Object>();
		//第一页，填写保密问题
		validator(map, "ctl00_SiteContentPlaceHolder_ucLocation_ddlLocation", "BEJ");
		validator(map, "ctl00_SiteContentPlaceHolder_txtAnswer", "Mother Name");
		//个人信息1
		//名字

		/*姓名电码,一般必须填写*/
		/*姓，电码4432*/
		Map<String, String> codes = telecodeService.getTelecode(customer.getLastName() + customer.getFirstName());
		StringBuilder lastName = new StringBuilder();
		for (String s : customer.getLastName().split("|")) {
			lastName.append(codes.get(s));
		}
		validator(map, "ctl00_SiteContentPlaceHolder_FormView1_tbxAPP_TelecodeSURNAME", lastName.toString());
		/*名,电码4421*/
		StringBuilder firstName = new StringBuilder();
		for (String s : customer.getFirstName().split("|")) {
			firstName.append(codes.get(s));
		}
		validator(map, "ctl00_SiteContentPlaceHolder_FormView1_tbxAPP_TelecodeGIVEN_NAME", firstName.toString());
		validator(map, "ctl00_SiteContentPlaceHolder_FormView1_rblTelecodeQuestion_0", true);//不用管

		validator(map, "ctl00_SiteContentPlaceHolder_FormView1_tbxAPP_SURNAME", customer.getLastNameEN());
		validator(map, "ctl00_SiteContentPlaceHolder_FormView1_tbxAPP_GIVEN_NAME", customer.getFirstNameEN());
		validator(map, "ctl00_SiteContentPlaceHolder_FormView1_tbxAPP_FULL_NAME_NATIVE", customer.getFirstNameEN()
				+ customer.getLastNameEN());
		validator(map, "ctl00_SiteContentPlaceHolder_FormView1_cbxAPP_FULL_NAME_NATIVE_NA", true);

		//出生日期
		Date birthday = customer.getBirthday();
		validator(map, "ctl00_SiteContentPlaceHolder_FormView1_ddlDOBDay_1",
				DateUtils.formatDate(customer.getBirthday(), "dd"));
		validator(map, "ctl00_SiteContentPlaceHolder_FormView1_ddlDOBMonth_1", dateFormatLocal(customer.getBirthday()));
		validator(map, "ctl00_SiteContentPlaceHolder_FormView1_tbxDOBYear_1",
				DateUtils.formatDate(customer.getBirthday(), "yyyy"));

		//是否已经工作
		Date workDate = DateUtil.addYear(birthday, WORK_AGE);
		Date now = DateTimeUtil.nowDate();
		boolean worked = workDate.before(now);
		validator(map, "ctl00_SiteContentPlaceHolder_FormView1_tbxDOB_Worked", worked);

		//这个字段不用管
		//曾用名信息
		//有就是true，没有false
		//有几个曾用名就生成几个空对象即可
		if (customer.getOldName() != null) {
			validator(map, "ctl00_SiteContentPlaceHolder_FormView1_rblOtherNames_0", true);
			validator(map, "ctl00_SiteContentPlaceHolder_FormView1_DListAlias_ctl_InsertButtonAlias",
					Lists.newArrayList(new Object()));

			//曾用名1
			validator(map, "ctl00_SiteContentPlaceHolder_FormView1_DListAlias_ctl00_tbxSURNAME", customer.getOldName()
					.getOldLastNameEN());//姓
			validator(map, "ctl00_SiteContentPlaceHolder_FormView1_DListAlias_ctl00_tbxGIVEN_NAME", customer
					.getOldName().getOldFirstNameEN());//名
		} else {
			validator(map, "ctl00_SiteContentPlaceHolder_FormView1_rblOtherNames_0", false);
		}

		//性别，婚姻状况MSDW，出生年月日,出生城市，省，国家
		/*
		 * 美国签证网站，性别是单选钮，男在前女在后，脚本选择单选钮的时候需要传递是否选中第一个
		 */
		validator(map, "ctl00_SiteContentPlaceHolder_FormView1_rblAPP_GENDER_0",
				customer.getGender() == Gender.MALE.name());

		//如果没有省就是true_POB_CNTRY", "CHIN");
		validator(map, "ctl00_SiteContentPlaceHolder_FormView1_ddlAPP_POB_CNTRY", "CHIN");
		//个人信息2：国籍,是否拥有别国国籍（不用管），是否是其他国家永久居民，身份证号码
		validator(map, "ctl00_SiteContentPlaceHolder_FormView1_ddlAPP_NATL", "CHIN");
		validator(map, "ctl00_SiteContentPlaceHolder_FormView1_rblAPP_OTH_NATL_IND_0", false);
		//不用管，不会有双国籍
		validator(map, "ctl00_SiteContentPlaceHolder_FormView1_rblPermResOtherCntryInd_0", false);
		//是否是别国永久居民
		{
			List<Object> list = Lists.newArrayList();
			validator(map, "ctl00_SiteContentPlaceHolder_FormView1_dtlOthPermResCntry_ctl_InsertButtonOTHER_PERM_RES",
					list);
			if (customer.getOtherCountry() != null) {
				int size = customer.getOtherCountry().size();
				for (int i = 0; i < size; i++) {
					//几个国家就生成几个空对象
					list.add(new Object());
					//如果是多国居民，按照顺序填入所有国家
					validator(map,
							"ctl00_SiteContentPlaceHolder_FormView1_dtlOthPermResCntry_ctl" + decimalFormat.format(i)
									+ "_ddlOthPermResCntry", customer.getOtherCountry().get(i).toString());
				}
			}
		}
		validator(map, "ctl00_SiteContentPlaceHolder_FormView1_tbxAPP_NATIONAL_ID", customer.getIdCardNo());
		validator(map, "ctl00_SiteContentPlaceHolder_FormView1_cbxAPP_SSN_NA", true);
		//不管
		validator(map, "ctl00_SiteContentPlaceHolder_FormView1_cbxAPP_TAX_ID_NA", true);
		//不管
		//地址与电话信息,家庭地址
		validator(map, "ctl00_SiteContentPlaceHolder_FormView1_tbxAPP_ADDR_LN1", customer.getRoomEN());
		validator(map, "ctl00_SiteContentPlaceHolder_FormView1_tbxAPP_ADDR_LN2", customer.getAddressEN());
		validator(map, "ctl00_SiteContentPlaceHolder_FormView1_tbxAPP_ADDR_CITY", customer.getCity());
		validator(map, "ctl00_SiteContentPlaceHolder_FormView1_tbxAPP_ADDR_STATE", customer.getProvince());
		validator(map, "ctl00_SiteContentPlaceHolder_FormView1_tbxAPP_ADDR_POSTAL_CD", customer.getZipCode());
		validator(map, "ctl00_SiteContentPlaceHolder_FormView1_ddlCountry", "CHIN");
		//邮件地址与家庭地址相同,不用管
		validator(map, "ctl00_SiteContentPlaceHolder_FormView1_rblMailingAddrSame_0", true);
		//不管
		//电话信息
		validator(map, "ctl00_SiteContentPlaceHolder_FormView1_tbxAPP_HOME_TEL", customer.getPhone());
		validator(map, "ctl00_SiteContentPlaceHolder_FormView1_cbxAPP_BUS_TEL_NA", true);
		//不管,没有办公电话
		validator(map, "ctl00_SiteContentPlaceHolder_FormView1_tbxAPP_MOBILE_TEL", customer.getMobile());
		validator(map, "ctl00_SiteContentPlaceHolder_FormView1_tbxAPP_EMAIL_ADDR", customer.getEmail());
		//护照信息
		validator(map, "ctl00_SiteContentPlaceHolder_FormView1_ddlPPT_TYPE", "R");
		//类型RODT
		validator(map, "ctl00_SiteContentPlaceHolder_FormView1_tbxPPT_NUM", customer.getPassport());
		validator(map, "ctl00_SiteContentPlaceHolder_FormView1_cbxPPT_BOOK_NUM_NA", true);
		//不管
		//发照相关信息

		validator(map, "ctl00_SiteContentPlaceHolder_FormView1_ddlPPT_ISSUED_CNTRY", "CHIN");
		validator(map, "ctl00_SiteContentPlaceHolder_FormView1_tbxPPT_ISSUED_IN_CITY", customer.getIssueCity());
		validator(map, "ctl00_SiteContentPlaceHolder_FormView1_tbxPPT_ISSUED_IN_STATE", customer.getIssueProvince());
		validator(map, "ctl00_SiteContentPlaceHolder_FormView1_ddlPPT_ISSUED_IN_CNTRY", "CHIN");

		validator(map, "ctl00_SiteContentPlaceHolder_FormView1_ddlPPT_ISSUED_DTEDay",
				DateUtils.formatDate(customer.getIssueDate(), "dd"));
		validator(map, "ctl00_SiteContentPlaceHolder_FormView1_ddlPPT_ISSUED_DTEMonth",
				DateUtils.formatDate(customer.getIssueDate(), "MM"));
		validator(map, "ctl00_SiteContentPlaceHolder_FormView1_tbxPPT_ISSUEDYear",
				DateUtils.formatDate(customer.getIssueDate(), "yyyy"));
		validator(map, "ctl00_SiteContentPlaceHolder_FormView1_ddlPPT_EXPIRE_DTEDay",
				DateUtils.formatDate(customer.getExpiryDate(), "dd"));
		validator(map, "ctl00_SiteContentPlaceHolder_FormView1_ddlPPT_EXPIRE_DTEMonth",
				DateUtils.formatDate(customer.getExpiryDate(), "MM"));
		validator(map, "ctl00_SiteContentPlaceHolder_FormView1_tbxPPT_EXPIREYear",
				DateUtils.formatDate(customer.getExpiryDate(), "yyyy"));

		//是否丢失过护照
		if (customer.getOldPassport() != null) {
			validator(map, "ctl00_SiteContentPlaceHolder_FormView1_rblLOST_PPT_IND_0", true);
			//几条数据生成几个空对象
			validator(map, "ctl00_SiteContentPlaceHolder_FormView1_dtlLostPPT_ctl_InsertButtonLostPPT",
					Lists.newArrayList(new Object()));
			//丢失护照的相关信息
			PassportDto passport = customer.getOldPassport();
			validator(map, "ctl00_SiteContentPlaceHolder_FormView1_dtlLostPPT_ctl00_tbxLOST_PPT_NUM",
					passport.getPassport());
			validator(map, "ctl00_SiteContentPlaceHolder_FormView1_dtlLostPPT_ctl00_ddlLOST_PPT_NATL", passport
					.getCountry().getValue());
			validator(map, "ctl00_SiteContentPlaceHolder_FormView1_dtlLostPPT_ctl00_tbxLOST_PPT_EXPL",
					passport.getWhyEN());
		} else {
			validator(map, "ctl00_SiteContentPlaceHolder_FormView1_rblLOST_PPT_IND_0", false);
		}
		//本次旅游的相关信息
		validator(map, "ctl00_SiteContentPlaceHolder_FormView1_dlPrincipalAppTravel_ctl00_ddlPurposeOfTrip", "B");
		//不管，基本都是B签
		validator(map, "ctl00_SiteContentPlaceHolder_FormView1_dlPrincipalAppTravel_ctl00_ddlOtherPurpose", "B1-B2");

		/**
		 * 是否有具体的travel计划，默认为false
		 * 填写抵达日期，抵达城市，停留多久即可
		 */
		validator(map, "ctl00_SiteContentPlaceHolder_FormView1_rblSpecificTravel_0", false);
		TravelDto travel = customer.getTravel();
		{
			DateTime arrivalDate = new DateTime(travel.getArrivalDate());
			if (arrivalDate.isBeforeNow()) {
				log.error("入境时间不能在当前时间之前!");
				throw new JSiteException("入境时间不能在当前时间之前!");
			}

			//预计抵达年月日,抵达城市
			validator(map, "ctl00_SiteContentPlaceHolder_FormView1_ddlTRAVEL_DTEDay",
					DateUtils.formatDate(arrivalDate.toDate(), "d"));
			validator(map, "ctl00_SiteContentPlaceHolder_FormView1_ddlTRAVEL_DTEMonth",
					DateUtils.formatDate(arrivalDate.toDate(), "M"));
			validator(map, "ctl00_SiteContentPlaceHolder_FormView1_tbxTRAVEL_DTEYear",
					DateUtils.formatDate(arrivalDate.toDate(), "yyyy"));
			validator(map, "ctl00_SiteContentPlaceHolder_FormView1_tbxArriveCity", travel.getEntryCity());

			//停留时间是否为24小时内
			boolean not24h = true;
			Period period = travel.getPeriod();
			if (!Util.isEmpty(period)) {
				switch (period) {
				case DAY:
					arrivalDate = arrivalDate.plusDays(travel.getStay());
					break;
				case WEEK:
					arrivalDate = arrivalDate.plusWeeks(travel.getStay());
					break;
				case MONTH:
					arrivalDate = arrivalDate.plusMonths(travel.getStay());
					break;
				case YEAR:
					arrivalDate = arrivalDate.plusYears(travel.getStay());
					break;
				case H: {
					not24h = false;
					arrivalDate = arrivalDate.plusDays(1);
					break;
				}
				default:
					log.error("停留时间类型错误，只能是[年Y月M周W日D24小时内H]");
				}
				validator(map, "ctl00_SiteContentPlaceHolder_FormView1_ddlTRAVEL_LOS_NOT24H", not24h);

				//选择停留时间单位(下拉列表)
				validator(map, "ctl00_SiteContentPlaceHolder_FormView1_ddlTRAVEL_LOS_CD", period.getValue());
				//停留数量
				validator(map, "ctl00_SiteContentPlaceHolder_FormView1_tbxTRAVEL_LOS", travel.getStay() + "");

				/**
				 * 注意:只有当选择了有具体travel计划的时候才要求同时填写抵达以及离开的日期和城市，此时页面元素的id会变化 
				 */

				//离开美国的相应信息(根据逗留时间和抵达时间自动计算)
				//				validator(map, "ctl00_SiteContentPlaceHolder_FormView1_ddlDEPARTURE_US_DTEDay",
				//						DateUtils.formatDate(dt.toDate(), "d"));
				//				validator(map, "ctl00_SiteContentPlaceHolder_FormView1_ddlDEPARTURE_US_DTEMonth",
				//						DateUtils.formatDate(dt.toDate(), "M"));
				//				validator(map, "ctl00_SiteContentPlaceHolder_FormView1_tbxDEPARTURE_US_DTEYear",
				//						DateUtils.formatDate(dt.toDate(), "yyyy"));
				//				validator(map, "ctl00_SiteContentPlaceHolder_FormView1_tbxDepartCity", travel.getEntryCity());

			} else {
				log.error("停留时间类型错误，只能是[年Y月M周W日D24小时内H]");
			}
		}

		//有几个城市就生成几个空对象
		validator(map, "ctl00_SiteContentPlaceHolder_FormView1_dtlTravelLoc_ctl_InsertButtonTravelLoc",
				Lists.newArrayList(new Object()));

		//计划拜访的地点，字符串，这里使用入境城市，只有当选择了有具体travel计划的时候才要求填写
		validator(map, "ctl00_SiteContentPlaceHolder_FormView1_dtlTravelLoc_ctl00_tbxSPECTRAVEL_LOCATION",
				travel.getEntryCity());
		//在美国逗留的地址1(必填)
		validator(map, "ctl00_SiteContentPlaceHolder_FormView1_tbxStreetAddress1", travel.getAddress());

		//ctl00_SiteContentPlaceHolder_FormView1_tbxStreetAddress2,地址2(可选)

		//在美国逗留的城市
		validator(map, "ctl00_SiteContentPlaceHolder_FormView1_tbxCity", travel.getEntryCity());
		validator(map, "ctl00_SiteContentPlaceHolder_FormView1_ddlTravelState", travel.getEntryProvince());
		validator(map, "ctl00_SiteContentPlaceHolder_FormView1_tbZIPCode", travel.getZipCode());
		//由谁支付,S,O,C
		validator(map, "ctl00_SiteContentPlaceHolder_FormView1_ddlWhoIsPaying", travel.getPayer());
		//如果是由个人S支付，则忽略以下字段
		switch (travel.getPayer()) {
		case "C":
			//如果是由公司C支付
			validator(map, "ctl00_SiteContentPlaceHolder_FormView1_tbxPayingCompany", travel.getCompanyNameEN());
			validator(map, "ctl00_SiteContentPlaceHolder_FormView1_tbxPayerPhone", travel.getCompanyPhone());
			validator(map, "ctl00_SiteContentPlaceHolder_FormView1_tbxCompanyRelation", travel.getCompanyRelationEN());
			validator(map, "ctl00_SiteContentPlaceHolder_FormView1_tbxPayerStreetAddress1", travel.getCompanyRoomEN());
			validator(map, "ctl00_SiteContentPlaceHolder_FormView1_tbxPayerStreetAddress2",
					travel.getCompanyAddressEN());
			validator(map, "ctl00_SiteContentPlaceHolder_FormView1_tbxPayerCity", travel.getCompanyCity());
			validator(map, "ctl00_SiteContentPlaceHolder_FormView1_tbxPayerStateProvince", travel.getCompanyProvince());
			validator(map, "ctl00_SiteContentPlaceHolder_FormView1_tbxPayerPostalZIPCode", travel.getCompanyZipCode());
			validator(map, "ctl00_SiteContentPlaceHolder_FormView1_ddlPayerCountry", travel.getCompanyCountry());
			break;
		case "O":
			//如果是别人O支付,相关信息
			validator(map, "ctl00_SiteContentPlaceHolder_FormView1_tbxPayerSurname", travel.getPayerLastNameEN());
			validator(map, "ctl00_SiteContentPlaceHolder_FormView1_tbxPayerGivenName", travel.getPayerFirstNameEN());
			validator(map, "ctl00_SiteContentPlaceHolder_FormView1_tbxPayerPhone", travel.getPayerPhone());
			validator(map, "ctl00_SiteContentPlaceHolder_FormView1_tbxPAYER_EMAIL_ADDR", travel.getPayerEmail());
			validator(map, "ctl00_SiteContentPlaceHolder_FormView1_ddlPayerRelationship", travel.getPayerRelation());
			//选择，与本人关系
			validator(map, "ctl00_SiteContentPlaceHolder_FormView1_rblPayerAddrSameAsInd_0", true);
			break;
		case "S":
		default:
			break;
		}
		//是否团队
		if (StringUtils.isNotBlank(travel.getTeam())) {
			validator(map, "ctl00_SiteContentPlaceHolder_FormView1_rblGroupTravel_0", true);
			//如果有团队，团队名称
			validator(map, "ctl00_SiteContentPlaceHolder_FormView1_tbxGroupName", travel.getTeam());
		} else {
			validator(map, "ctl00_SiteContentPlaceHolder_FormView1_rblGroupTravel_0", false);
		}
		//是否有同行人
		{
			boolean flag = (travel.getTogethers() != null && travel.getTogethers().size() > 0);
			validator(map, "ctl00_SiteContentPlaceHolder_FormView1_rblOtherPersonsTravelingWithYou_0", flag);
			List list = Lists.newArrayList();
			validator(map, "ctl00_SiteContentPlaceHolder_FormView1_dlTravelCompanions_ctl_InsertButtonPrincipalPOT",
					list);
			if (flag) {
				for (int i = 0; i < travel.getTogethers().size(); i++) {
					//有几个同行人，生成几个空对象
					list.add(new Object());
					//如果不是团队，同行人相关信息
					TogetherDto t = travel.getTogethers().get(i);
					validator(map,
							"ctl00_SiteContentPlaceHolder_FormView1_dlTravelCompanions_ctl" + decimalFormat.format(i)
									+ "_tbxSurname", t.getLastNameEN());
					validator(map,
							"ctl00_SiteContentPlaceHolder_FormView1_dlTravelCompanions_ctl" + decimalFormat.format(i)
									+ "_tbxGivenName", t.getFirstNameEN());

					//如果关系为空，则使用'其他'
					String relation = t.getRelation();
					if (!Util.isEmpty(relation)) {
						validator(
								map,
								"ctl00_SiteContentPlaceHolder_FormView1_dlTravelCompanions_ctl"
										+ decimalFormat.format(i) + "_ddlTCRelationship", relation);
					} else {
						validator(
								map,
								"ctl00_SiteContentPlaceHolder_FormView1_dlTravelCompanions_ctl"
										+ decimalFormat.format(i) + "_ddlTCRelationship", Relation.OTHER.getValue());
					}
				}// end of for loop
			}
		}

		//过往的美国旅行经历
		{
			List<HistoryDto> histories = customer.getHistories();
			boolean flag = (histories != null && !histories.isEmpty());
			//是否到过美国
			validator(map, "ctl00_SiteContentPlaceHolder_FormView1_rblPREV_US_TRAVEL_IND_0", flag);
			List list = Lists.newArrayList();
			validator(map, "ctl00_SiteContentPlaceHolder_FormView1_dtlPREV_US_VISIT_ctl_InsertButtonPREV_US_VISIT",
					list);
			if (flag) {
				for (int i = 0; i < histories.size(); i++) {

					String indx = decimalFormat.format(i);

					//去过几次生成几个空对象
					HistoryDto h = histories.get(i);
					//过往到过美国的相关信息
					validator(map, "ctl00_SiteContentPlaceHolder_FormView1_dtlPREV_US_VISIT_ctl" + indx
							+ "_ddlPREV_US_VISIT_DTEDay", DateUtils.formatDate(h.getArrivalDate(), "d"));
					validator(map, "ctl00_SiteContentPlaceHolder_FormView1_dtlPREV_US_VISIT_ctl" + indx
							+ "_ddlPREV_US_VISIT_DTEMonth", DateUtils.formatDate(h.getArrivalDate(), "M"));
					validator(map, "ctl00_SiteContentPlaceHolder_FormView1_dtlPREV_US_VISIT_ctl" + indx
							+ "_tbxPREV_US_VISIT_DTEYear", DateUtils.formatDate(h.getArrivalDate(), "yyyy"));
					validator(map, "ctl00_SiteContentPlaceHolder_FormView1_dtlPREV_US_VISIT_ctl" + indx
							+ "_tbxPREV_US_VISIT_LOS", String.valueOf(h.getStay()));

					Period hPeriod = h.getPeriod();
					if (!Util.isEmpty(hPeriod)) {
						switch (hPeriod) {
						case DAY:
							break;
						case WEEK:
							break;
						case MONTH:
							break;
						case YEAR:
							break;
						case H: {
							break;
						}
						default:
							log.error("停留时间类型错误，只能是[年Y月M周W日D24小时内H]");
						}
					} else {
						log.error("停留时间类型错误，只能是[年Y月M周W日D24小时内H]");
					}

					validator(map, "ctl00_SiteContentPlaceHolder_FormView1_dtlPREV_US_VISIT_ctl" + indx
							+ "_ddlPREV_US_VISIT_LOS_CD", hPeriod.getValue());
					list.add(new Object());
				}
			}
		}
		//如果有，美国驾照的相关信息
		UsaDto usa = customer.getUsa();
		if (Util.isEmpty(usa)) {
			usa = new UsaDto();
		}
		validator(map, "ctl00_SiteContentPlaceHolder_FormView1_rblPREV_US_DRIVER_LIC_IND_0",
				StringUtils.isNotBlank(usa.getDrivingLicense()));
		validator(map, "ctl00_SiteContentPlaceHolder_FormView1_dtlUS_DRIVER_LICENSE_ctl00_tbxUS_DRIVER_LICENSE",
				usa.getDrivingLicense());
		validator(map, "ctl00_SiteContentPlaceHolder_FormView1_dtlUS_DRIVER_LICENSE_ctl00_ddlUS_DRIVER_LICENSE_STATE",
				usa.getDrivingLicenseProvince());
		{
			//是否有过美国Visa
			validator(map, "ctl00_SiteContentPlaceHolder_FormView1_rblPREV_VISA_IND_0",
					StringUtils.isNotBlank(usa.getOldVisa()));
			validator(map, "ctl00_SiteContentPlaceHolder_FormView1_ddlPREV_VISA_ISSUED_DTEDay",
					DateUtils.formatDate(usa.getOldVisaDate(), "d"));
			validator(map, "ctl00_SiteContentPlaceHolder_FormView1_ddlPREV_VISA_ISSUED_DTEMonth",
					DateUtils.formatDate(usa.getOldVisaDate(), "M"));
			validator(map, "ctl00_SiteContentPlaceHolder_FormView1_tbxPREV_VISA_ISSUED_DTEYear",
					DateUtils.formatDate(usa.getOldVisaDate(), "yyyy"));
			validator(map, "ctl00_SiteContentPlaceHolder_FormView1_tbxPREV_VISA_FOIL_NUMBER", usa.getOldVisa());
			validator(map, "ctl00_SiteContentPlaceHolder_FormView1_rblPREV_VISA_SAME_TYPE_IND_0", true);
			//是否签发类型一致
		}
		validator(map, "ctl00_SiteContentPlaceHolder_FormView1_rblPREV_VISA_SAME_CNTRY_IND_0", true);
		//不管
		validator(map, "ctl00_SiteContentPlaceHolder_FormView1_rblPREV_VISA_TEN_PRINT_IND_0", true);
		//不管
		validator(map, "ctl00_SiteContentPlaceHolder_FormView1_rblPREV_VISA_LOST_IND_0", false);
		//不管
		validator(map, "ctl00_SiteContentPlaceHolder_FormView1_rblPREV_VISA_CANCELLED_IND_0", false);
		//不管
		validator(map, "ctl00_SiteContentPlaceHolder_FormView1_rblPREV_VISA_REFUSED_IND_0", false);
		//不管

		validator(map, "ctl00_SiteContentPlaceHolder_FormView1_rblIV_PETITION_IND_0",
				StringUtils.isNotBlank(usa.getImmigrantEN()));
		//是否提出过移民申请
		validator(map, "ctl00_SiteContentPlaceHolder_FormView1_tbxIV_PETITION_EXPL", usa.getImmigrantEN());
		//移民申请说明

		//美国联系人信息
		validator(map, "ctl00_SiteContentPlaceHolder_FormView1_cbxUS_POC_ORG_NA_IND", true);
		//不管，这个是指定美国只能有联系人，不能是个组织的

		//联系人相关信息
		validator(map, "ctl00_SiteContentPlaceHolder_FormView1_tbxUS_POC_SURNAME", travel.getContactsLastNameEN());
		validator(map, "ctl00_SiteContentPlaceHolder_FormView1_tbxUS_POC_GIVEN_NAME", travel.getContactsFirstNameEN());

		validator(map, "ctl00_SiteContentPlaceHolder_FormView1_tbxUS_POC_ADDR_LN1", travel.getContactsAddress());
		validator(map, "ctl00_SiteContentPlaceHolder_FormView1_tbxUS_POC_ADDR_CITY", travel.getContactsCity());
		validator(map, "ctl00_SiteContentPlaceHolder_FormView1_ddlUS_POC_ADDR_STATE", travel.getContactsProvince());
		validator(map, "ctl00_SiteContentPlaceHolder_FormView1_tbxUS_POC_ADDR_POSTAL_CD", travel.getContactsZipCode());
		validator(map, "ctl00_SiteContentPlaceHolder_FormView1_tbxUS_POC_HOME_TEL", travel.getContactsPhone());
		validator(map, "ctl00_SiteContentPlaceHolder_FormView1_tbxUS_POC_EMAIL_ADDR", travel.getContactsEmail());

		//和我的关系
		String contactsRelation = travel.getContactsRelation();
		if (!Util.isEmpty(contactsRelation)) {
			validator(map, "ctl00_SiteContentPlaceHolder_FormView1_ddlUS_POC_REL_TO_APP", contactsRelation);
		}

		//家庭相关信息
		FamilyDto father = customer.getFather();
		if (!Util.isEmpty(father) && StringUtils.isNotBlank(father.getLastNameEN())
				&& StringUtils.isNotBlank(father.getFirstNameEN())) {
			validator(map, "ctl00_SiteContentPlaceHolder_FormView1_cbxFATHER_SURNAME_UNK_IND", false);//知道父亲的姓氏
			validator(map, "ctl00_SiteContentPlaceHolder_FormView1_cbxFATHER_GIVEN_NAME_UNK_IND", false);//知道父亲的名字

			validator(map, "ctl00_SiteContentPlaceHolder_FormView1_tbxFATHER_SURNAME", customer.getFather()
					.getLastNameEN());
			validator(map, "ctl00_SiteContentPlaceHolder_FormView1_tbxFATHER_GIVEN_NAME", customer.getFather()
					.getFirstNameEN());
			validator(map, "ctl00_SiteContentPlaceHolder_FormView1_ddlFathersDOBDay",
					DateUtils.formatDate(customer.getFather().getBirthday(), "dd"));
			validator(map, "ctl00_SiteContentPlaceHolder_FormView1_ddlFathersDOBMonth", dateFormatLocal(customer
					.getFather().getBirthday()));
			validator(map, "ctl00_SiteContentPlaceHolder_FormView1_tbxFathersDOBYear",
					DateUtils.formatDate(customer.getFather().getBirthday(), "yyyy"));
			validator(map, "ctl00_SiteContentPlaceHolder_FormView1_rblFATHER_LIVE_IN_US_IND_0", customer.getFather()
					.isInUsa());
			//父亲是否在美国
			validator(map, "ctl00_SiteContentPlaceHolder_FormView1_ddlFATHER_US_STATUS", customer.getFather()
					.getUsaStatus());
			//父亲在美国属于什么身份SCPO，前端似乎没这个字段，这是后端填表必须要的信息
		} else {
			validator(map, "ctl00_SiteContentPlaceHolder_FormView1_cbxFATHER_SURNAME_UNK_IND", true);//不知道父亲的姓氏
			validator(map, "ctl00_SiteContentPlaceHolder_FormView1_cbxFATHER_GIVEN_NAME_UNK_IND", true);//不知道父亲的名字
		}

		if (customer.getMother() != null && StringUtils.isNotBlank(customer.getMother().getLastNameEN())
				&& StringUtils.isNotBlank(customer.getMother().getFirstNameEN())) {
			validator(map, "ctl00_SiteContentPlaceHolder_FormView1_cbxMOTHER_SURNAME_UNK_IND", false);//知道母亲的姓氏
			validator(map, "ctl00_SiteContentPlaceHolder_FormView1_cbxMOTHER_GIVEN_NAME_UNK_IND", false);//知道母亲的名字

			validator(map, "ctl00_SiteContentPlaceHolder_FormView1_tbxMOTHER_SURNAME", customer.getMother()
					.getLastNameEN());
			validator(map, "ctl00_SiteContentPlaceHolder_FormView1_tbxMOTHER_GIVEN_NAME", customer.getMother()
					.getFirstNameEN());
			validator(map, "ctl00_SiteContentPlaceHolder_FormView1_ddlMothersDOBDay",
					DateUtils.formatDate(customer.getMother().getBirthday(), "dd"));
			validator(map, "ctl00_SiteContentPlaceHolder_FormView1_ddlMothersDOBMonth", dateFormatLocal(customer
					.getMother().getBirthday()));
			validator(map, "ctl00_SiteContentPlaceHolder_FormView1_tbxMothersDOBYear",
					DateUtils.formatDate(customer.getMother().getBirthday(), "yyyy"));
			validator(map, "ctl00_SiteContentPlaceHolder_FormView1_rblMOTHER_LIVE_IN_US_IND_0", customer.getMother()
					.isInUsa());
			validator(map, "ctl00_SiteContentPlaceHolder_FormView1_ddlMOTHER_US_STATUS", customer.getMother()
					.getUsaStatus());
			//母亲在美国属于什么身份SCPO，前端似乎没这个字段，这是后端填表必须要的信息
		} else {
			validator(map, "ctl00_SiteContentPlaceHolder_FormView1_cbxMOTHER_SURNAME_UNK_IND", true);//不知道母亲的姓氏
			validator(map, "ctl00_SiteContentPlaceHolder_FormView1_cbxMOTHER_GIVEN_NAME_UNK_IND", true);//不知道母亲的名字
		}

		//直系亲属信息
		{
			boolean flag = (customer.getFamilies() != null && !customer.getFamilies().isEmpty());
			validator(map, "ctl00_SiteContentPlaceHolder_FormView1_rblUS_IMMED_RELATIVE_IND_0", flag);
			List list = Lists.newArrayList();
			validator(map, "ctl00_SiteContentPlaceHolder_FormView1_dlUSRelatives_ctl_InsertButtonUSRelative", list);
			if (flag) {
				for (int i = 0; i < customer.getFamilies().size(); i++) {
					//有几个直系亲属生成几个空对象
					list.add(new Object());
					//是否有直系亲属
					FamilyDto f = customer.getFamilies().get(i);
					validator(map, "ctl00_SiteContentPlaceHolder_FormView1_dlUSRelatives_ctl" + decimalFormat.format(i)
							+ "_tbxUS_REL_SURNAME", f.getLastNameEN());
					validator(map, "ctl00_SiteContentPlaceHolder_FormView1_dlUSRelatives_ctl" + decimalFormat.format(i)
							+ "_tbxUS_REL_GIVEN_NAME", f.getFirstNameEN());

					String fRelation = f.getRelation();
					if (!Util.isEmpty(fRelation)) {
						validator(map,
								"ctl00_SiteContentPlaceHolder_FormView1_dlUSRelatives_ctl" + decimalFormat.format(i)
										+ "_ddlUS_REL_TYPE", fRelation);
					}

					String fUsaStatus = f.getUsaStatus();
					if (!Util.isEmpty(fUsaStatus)) {
						validator(map,
								"ctl00_SiteContentPlaceHolder_FormView1_dlUSRelatives_ctl" + decimalFormat.format(i)
										+ "_ddlUS_REL_STATUS", fUsaStatus);
					}
				}
			}
		}
		//是否有其他非直系亲属
		validator(map, "ctl00_SiteContentPlaceHolder_FormView1_rblUS_OTHER_RELATIVE_IND_0", customer.isFriendInUSA());

		{
			validator(map, "ctl00_SiteContentPlaceHolder_FormView1_ddlDOBDay_2",
					DateUtils.formatDate(customer.getBirthday(), "dd"));
			validator(map, "ctl00_SiteContentPlaceHolder_FormView1_ddlDOBMonth_2",
					dateFormatLocal(customer.getBirthday()));
			validator(map, "ctl00_SiteContentPlaceHolder_FormView1_tbxDOBYear_2",
					DateUtils.formatDate(customer.getBirthday(), "yyyy"));
			validator(map, "ctl00_SiteContentPlaceHolder_FormView1_ddlDOBDay_3",
					DateUtils.formatDate(customer.getBirthday(), "dd"));
			validator(map, "ctl00_SiteContentPlaceHolder_FormView1_ddlDOBMonth_3",
					dateFormatLocal(customer.getBirthday()));
			validator(map, "ctl00_SiteContentPlaceHolder_FormView1_tbxDOBYear_3",
					DateUtils.formatDate(customer.getBirthday(), "yyyy"));

			validator(map, "ctl00_SiteContentPlaceHolder_FormView1_tbxAPP_POB_CITY", customer.getBirthCity());
			validator(map, "ctl00_SiteContentPlaceHolder_FormView1_tbxAPP_POB_ST_PROVINCE", customer.getBirthProvince());
			validator(map, "ctl00_SiteContentPlaceHolder_FormView1_cbxAPP_POB_ST_PROVINCE_NA", false);

			//配偶信息，当ctl00_SiteContentPlaceHolder_FormView1_ddlAPP_MARITAL_STATUS 为S之外的值时，有如下情况
			SpouseDto spouse = customer.getSpouse();
			if (!Util.isEmpty(spouse)) {

				//婚姻状态
				String spState = spouse.getState();
				if (!Util.isEmpty(spState)) {
					validator(map, "ctl00_SiteContentPlaceHolder_FormView1_ddlAPP_MARITAL_STATUS", spState);
				}

				validator(map, "ctl00_SiteContentPlaceHolder_FormView1_tbxSpouseSurname", spouse.getFirstNameEN());
				validator(map, "ctl00_SiteContentPlaceHolder_FormView1_tbxSpouseGivenName", spouse.getLastNameEN());
				validator(map, "ctl00_SiteContentPlaceHolder_FormView1_ddlSpouseNatDropDownList",
						spouse.getNationality());
				validator(map, "ctl00_SiteContentPlaceHolder_FormView1_tbxSpousePOBCity", spouse.getBirthCity());
				validator(map, "ctl00_SiteContentPlaceHolder_FormView1_ddlSpousePOBCountry", spouse.getBirthCountry());
				boolean different = StringUtils.isNotBlank(spouse.getZipCode())
						&& StringUtils.isNotBlank(spouse.getAddressEN());
				validator(map, "ctl00_SiteContentPlaceHolder_FormView1_ddlSpouseAddressType", different ? "O" : "H");//如果配偶与我地址不同则为"O"
				validator(map, "ctl00_SiteContentPlaceHolder_FormView1_tbxSPOUSE_ADDR_LN1", spouse.getAddressEN());
				validator(map, "ctl00_SiteContentPlaceHolder_FormView1_tbxSPOUSE_ADDR_CITY", spouse.getCity());
				validator(map, "ctl00_SiteContentPlaceHolder_FormView1_tbxSPOUSE_ADDR_STATE", spouse.getProvince());
				validator(map, "ctl00_SiteContentPlaceHolder_FormView1_tbxSPOUSE_ADDR_POSTAL_CD", spouse.getZipCode());
				validator(map, "ctl00_SiteContentPlaceHolder_FormView1_ddlSPOUSE_ADDR_CNTRY", spouse.getCountry());
				//丧偶W
				validator(map, "ctl00_SiteContentPlaceHolder_FormView1_tbxSURNAME", spouse.getLastNameEN());
				validator(map, "ctl00_SiteContentPlaceHolder_FormView1_tbxGIVEN_NAME", spouse.getFirstNameEN());
				//离婚D
				validator(map, "ctl00_SiteContentPlaceHolder_FormView1_tbxNumberOfPrevSpouses", "1");
				//离异的配偶数量
				validator(map, "ctl00_SiteContentPlaceHolder_FormView1_DListSpouse_ctl00_tbxSURNAME",
						spouse.getLastNameEN());
				validator(map, "ctl00_SiteContentPlaceHolder_FormView1_DListSpouse_ctl00_tbxGIVEN_NAME",
						spouse.getFirstNameEN());
				validator(map, "ctl00_SiteContentPlaceHolder_FormView1_DListSpouse_ctl00_ddlDOBDay",
						DateUtils.formatDate(spouse.getBirthday(), "d"));
				validator(map, "ctl00_SiteContentPlaceHolder_FormView1_DListSpouse_ctl00_ddlDOBMonth",
						dateFormatLocal(spouse.getBirthday()));
				validator(map, "ctl00_SiteContentPlaceHolder_FormView1_DListSpouse_ctl00_tbxDOBYear",
						DateUtils.formatDate(spouse.getBirthday(), "yyyy"));
				validator(map, "ctl00_SiteContentPlaceHolder_FormView1_DListSpouse_ctl00_ddlSpouseNatDropDownList",
						spouse.getNationality());
				validator(map, "ctl00_SiteContentPlaceHolder_FormView1_DListSpouse_ctl00_tbxSpousePOBCity",
						spouse.getBirthCity());
				validator(map, "ctl00_SiteContentPlaceHolder_FormView1_DListSpouse_ctl00_ddlSpousePOBCountry",
						spouse.getBirthCountry());

				DateTime start = new DateTime(spouse.getWedDate());
				if (start.isAfterNow()) {
					log.error("结婚时间不能在当前时间之后!");
					throw new JSiteException("结婚时间不能在当前时间之后!");
				}
				DateTime end = new DateTime(spouse.getDivorceDate());

				validator(map, "ctl00_SiteContentPlaceHolder_FormView1_DListSpouse_ctl00_ddlDomDay",
						DateUtils.formatDate(start.toDate(), "d"));
				validator(map, "ctl00_SiteContentPlaceHolder_FormView1_DListSpouse_ctl00_ddlDomMonth",
						DateUtils.formatDate(start.toDate(), "M"));
				validator(map, "ctl00_SiteContentPlaceHolder_FormView1_DListSpouse_ctl00_txtDomYear",
						DateUtils.formatDate(start.toDate(), "yyyy"));

				validator(map, "ctl00_SiteContentPlaceHolder_FormView1_DListSpouse_ctl00_ddlDomEndDay",
						DateUtils.formatDate(end.toDate(), "d"));
				validator(map, "ctl00_SiteContentPlaceHolder_FormView1_DListSpouse_ctl00_ddlDomEndMonth",
						DateUtils.formatDate(end.toDate(), "M"));
				validator(map, "ctl00_SiteContentPlaceHolder_FormView1_DListSpouse_ctl00_txtDomEndYear",
						DateUtils.formatDate(end.toDate(), "yyyy"));

				validator(map, "ctl00_SiteContentPlaceHolder_FormView1_DListSpouse_ctl00_tbxHowMarriageEnded",
						spouse.getDivorceReasonEN());
				validator(map, "ctl00_SiteContentPlaceHolder_FormView1_DListSpouse_ctl00_ddlMarriageEnded_CNTRY",
						spouse.getDivorceCountry());
			}
		}

		//当前工作信息
		WorkDto work = customer.getWork();
		if (work != null) {
			validator(map, "ctl00_SiteContentPlaceHolder_FormView1_ddlPresentOccupation", work.getIndustry());
			//如果当前职业为N，有如下
			validator(map, "ctl00_SiteContentPlaceHolder_FormView1_tbxExplainOtherPresentOccupation",
					"the not employed reason is...");
			//如果当前职业为H或者RT，有如下
			//什么也不填
			//如果当前职业为所有其他正常职业
			validator(map, "ctl00_SiteContentPlaceHolder_FormView1_tbxEmpSchName", work.getNameEN());
			validator(map, "ctl00_SiteContentPlaceHolder_FormView1_tbxEmpSchAddr1", work.getAddressEN());
			validator(map, "ctl00_SiteContentPlaceHolder_FormView1_tbxEmpSchCity", work.getCity());
			validator(map, "ctl00_SiteContentPlaceHolder_FormView1_tbxWORK_EDUC_ADDR_STATE", work.getProvince());
			validator(map, "ctl00_SiteContentPlaceHolder_FormView1_tbxWORK_EDUC_ADDR_POSTAL_CD", work.getZipCode());
			validator(map, "ctl00_SiteContentPlaceHolder_FormView1_tbxWORK_EDUC_TEL", work.getPhone());
			validator(map, "ctl00_SiteContentPlaceHolder_FormView1_ddlEmpSchCountry", work.getCountry());
			validator(map, "ctl00_SiteContentPlaceHolder_FormView1_tbxCURR_MONTHLY_SALARY",
					String.valueOf(Double.valueOf(work.getSalary()).intValue()));
			validator(map, "ctl00_SiteContentPlaceHolder_FormView1_tbxDescribeDuties", work.getDutyEN());
		}
		//曾经雇佣信息
		{
			boolean flag = customer.getWorks() != null && customer.getWorks().size() > 0;
			validator(map, "ctl00_SiteContentPlaceHolder_FormView1_rblPreviouslyEmployed_0", flag);
			List workLst = Lists.newArrayList();
			validator(map, "ctl00_SiteContentPlaceHolder_FormView1_dtlPrevEmpl_ctl_InsertButtonPrevEmpl", workLst);
			if (flag) {
				for (int i = 0; i < customer.getWorks().size(); i++) {
					//雇佣几次生成几个空对象
					workLst.add(new Object());
					String stringIdx = decimalFormat.format(i);
					//雇佣信息1
					WorkDto w = customer.getWorks().get(i);
					validator(map, "ctl00_SiteContentPlaceHolder_FormView1_dtlPrevEmpl_ctl" + stringIdx
							+ "_tbEmployerName", w.getNameEN());
					validator(map, "ctl00_SiteContentPlaceHolder_FormView1_dtlPrevEmpl_ctl" + stringIdx
							+ "_tbEmployerStreetAddress1", w.getRoomEN());
					validator(map, "ctl00_SiteContentPlaceHolder_FormView1_dtlPrevEmpl_ctl" + stringIdx
							+ "_tbEmployerStreetAddress2", w.getAddressEN());
					validator(map, "ctl00_SiteContentPlaceHolder_FormView1_dtlPrevEmpl_ctl" + stringIdx
							+ "_tbEmployerCity", w.getCity());
					validator(map, "ctl00_SiteContentPlaceHolder_FormView1_dtlPrevEmpl_ctl" + stringIdx
							+ "_tbxPREV_EMPL_ADDR_STATE", w.getProvince());
					validator(map, "ctl00_SiteContentPlaceHolder_FormView1_dtlPrevEmpl_ctl" + stringIdx
							+ "_tbxPREV_EMPL_ADDR_POSTAL_CD", w.getZipCode());
					validator(map, "ctl00_SiteContentPlaceHolder_FormView1_dtlPrevEmpl_ctl" + stringIdx
							+ "_DropDownList2", w.getCountry());
					validator(map, "ctl00_SiteContentPlaceHolder_FormView1_dtlPrevEmpl_ctl" + stringIdx
							+ "_tbEmployerPhone", w.getPhone());
					validator(map,
							"ctl00_SiteContentPlaceHolder_FormView1_dtlPrevEmpl_ctl" + stringIdx + "_tbJobTitle",
							w.getJobEN());
					validator(map, "ctl00_SiteContentPlaceHolder_FormView1_dtlPrevEmpl_ctl" + stringIdx
							+ "_tbSupervisorSurname", w.getBossLastNameEN());
					validator(map, "ctl00_SiteContentPlaceHolder_FormView1_dtlPrevEmpl_ctl" + stringIdx
							+ "_tbSupervisorGivenName", w.getBossFirstNameEN());
					validator(map, "ctl00_SiteContentPlaceHolder_FormView1_dtlPrevEmpl_ctl" + stringIdx
							+ "_ddlEmpDateFromDay", DateUtils.formatDate(w.getStartDate(), "d"));
					validator(map, "ctl00_SiteContentPlaceHolder_FormView1_dtlPrevEmpl_ctl" + stringIdx
							+ "_ddlEmpDateFromMonth", DateUtils.formatDate(w.getStartDate(), "M"));
					validator(map, "ctl00_SiteContentPlaceHolder_FormView1_dtlPrevEmpl_ctl" + stringIdx
							+ "_tbxEmpDateFromYear", DateUtils.formatDate(w.getStartDate(), "yyyy"));
					validator(map, "ctl00_SiteContentPlaceHolder_FormView1_dtlPrevEmpl_ctl" + stringIdx
							+ "_ddlEmpDateToDay", DateUtils.formatDate(w.getEndDate(), "d"));
					validator(map, "ctl00_SiteContentPlaceHolder_FormView1_dtlPrevEmpl_ctl" + stringIdx
							+ "_ddlEmpDateToMonth", DateUtils.formatDate(w.getEndDate(), "M"));
					validator(map, "ctl00_SiteContentPlaceHolder_FormView1_dtlPrevEmpl_ctl" + stringIdx
							+ "_tbxEmpDateToYear", DateUtils.formatDate(w.getEndDate(), "yyyy"));
					validator(map, "ctl00_SiteContentPlaceHolder_FormView1_dtlPrevEmpl_ctl" + stringIdx
							+ "_tbDescribeDuties", w.getDutyEN());
				}
			}
		}
		//教育信息
		{
			boolean flag = customer.getSchools() != null && customer.getSchools().size() > 0;
			//有高中以上学历，如果没有，后面信息就都没了
			validator(map, "ctl00_SiteContentPlaceHolder_FormView1_rblOtherEduc_0", flag);
			List list = Lists.newArrayList();
			validator(map, "ctl00_SiteContentPlaceHolder_FormView1_dtlPrevEduc_ctl_InsertButtonPrevEduc", list);
			if (flag) {
				for (int i = 0; i < customer.getSchools().size(); i++) {
					//每个学校生成一个空对象
					list.add(new Object());
					String stringIdx = decimalFormat.format(i);

					//学校
					SchoolDto s = customer.getSchools().get(i);
					validator(map, "ctl00_SiteContentPlaceHolder_FormView1_dtlPrevEduc_ctl" + stringIdx
							+ "_tbxSchoolName", s.getNameEN());
					validator(map, "ctl00_SiteContentPlaceHolder_FormView1_dtlPrevEduc_ctl" + stringIdx
							+ "_tbxSchoolAddr1", s.getAddressEN());
					//地址2 可选使用room地址
					validator(map, "ctl00_SiteContentPlaceHolder_FormView1_dtlPrevEduc_ctl" + stringIdx
							+ "_tbxSchoolAddr2", s.getRoomEN());
					validator(map, "ctl00_SiteContentPlaceHolder_FormView1_dtlPrevEduc_ctl" + stringIdx
							+ "_tbxSchoolCity", s.getCity());
					validator(map, "ctl00_SiteContentPlaceHolder_FormView1_dtlPrevEduc_ctl" + stringIdx
							+ "_tbxEDUC_INST_ADDR_STATE", s.getProvince());
					validator(map, "ctl00_SiteContentPlaceHolder_FormView1_dtlPrevEduc_ctl" + stringIdx
							+ "_tbxEDUC_INST_POSTAL_CD", s.getZipCode());
					validator(map, "ctl00_SiteContentPlaceHolder_FormView1_dtlPrevEduc_ctl" + stringIdx
							+ "_ddlSchoolCountry", s.getCountry());
					validator(map, "ctl00_SiteContentPlaceHolder_FormView1_dtlPrevEduc_ctl" + stringIdx
							+ "_tbxSchoolCourseOfStudy", s.getSpecialtyEN());
					validator(map, "ctl00_SiteContentPlaceHolder_FormView1_dtlPrevEduc_ctl" + stringIdx
							+ "_ddlSchoolFromDay", DateUtils.formatDate(s.getStartDate(), "d"));
					validator(map, "ctl00_SiteContentPlaceHolder_FormView1_dtlPrevEduc_ctl" + stringIdx
							+ "_ddlSchoolFromMonth", DateUtils.formatDate(s.getStartDate(), "M"));
					validator(map, "ctl00_SiteContentPlaceHolder_FormView1_dtlPrevEduc_ctl" + stringIdx
							+ "_tbxSchoolFromYear", DateUtils.formatDate(s.getStartDate(), "yyyy"));
					validator(map, "ctl00_SiteContentPlaceHolder_FormView1_dtlPrevEduc_ctl" + stringIdx
							+ "_ddlSchoolToDay", DateUtils.formatDate(s.getEndDate(), "d"));
					validator(map, "ctl00_SiteContentPlaceHolder_FormView1_dtlPrevEduc_ctl" + stringIdx
							+ "_ddlSchoolToMonth", DateUtils.formatDate(s.getEndDate(), "M"));
					validator(map, "ctl00_SiteContentPlaceHolder_FormView1_dtlPrevEduc_ctl" + stringIdx
							+ "_tbxSchoolToYear", DateUtils.formatDate(s.getEndDate(), "yyyy"));
				}
			}
		}
		validator(map, "ctl00_SiteContentPlaceHolder_FormView1_rblCLAN_TRIBE_IND_0", false);
		//会说的语言种类
		{
			//至少要有一种会说的语言
			if (customer.getLanguages() == null || customer.getLanguages().size() <= 0) {
				customer.setLanguages(Lists.newArrayList(new Option("中文", "CHINESE")));
			}

			List list = Lists.newArrayList();
			validator(map, "ctl00_SiteContentPlaceHolder_FormView1_dtlLANGUAGES_ctl_InsertButtonLANGUAGE", list);
			int size = customer.getLanguages().size();
			for (int i = 0; i < size; i++) {
				list.add(new Object());
				String stringIdx = decimalFormat.format(i);
				validator(map, "ctl00_SiteContentPlaceHolder_FormView1_dtlLANGUAGES_ctl" + stringIdx
						+ "_tbxLANGUAGE_NAME", customer.getLanguages().get(i).toString());
			}
		}
		//曾经去过的国家
		{
			boolean flag = customer.getVisitCountry() != null && customer.getVisitCountry().size() > 0;
			validator(map, "ctl00_SiteContentPlaceHolder_FormView1_rblCOUNTRIES_VISITED_IND_0", flag);
			List list = Lists.newArrayList();
			validator(map,
					"ctl00_SiteContentPlaceHolder_FormView1_dtlCountriesVisited_ctl_InsertButtonCountriesVisited", list);
			if (flag) {
				int size = customer.getVisitCountry().size();
				for (int i = 0; i < size; i++) {
					list.add(new Object());
					String stringIdx = decimalFormat.format(i);
					validator(map, "ctl00_SiteContentPlaceHolder_FormView1_dtlCountriesVisited_ctl" + stringIdx
							+ "_ddlCOUNTRIES_VISITED", customer.getVisitCountry().get(i).toString());
				}
			}
		}
		//曾经加入的职业协会，社会团体
		{
			boolean flag = customer.getCharitable() != null && customer.getCharitable().size() > 0;
			validator(map, "ctl00_SiteContentPlaceHolder_FormView1_rblORGANIZATION_IND_0", flag);
			List list = Lists.newArrayList();
			validator(map, "ctl00_SiteContentPlaceHolder_FormView1_dtlORGANIZATIONS_ctl_InsertButtonORGANIZATION", list);
			if (flag) {
				int size = customer.getCharitable().size();
				for (int i = 0; i < size; i++) {
					list.add(new Object());
					String stringIdx = decimalFormat.format(i);
					validator(map, "ctl00_SiteContentPlaceHolder_FormView1_dtlORGANIZATIONS_ctl" + stringIdx
							+ "_tbxORGANIZATION_NAME", customer.getCharitable().get(i).toString());
				}
			}
		}
		//额外的信息
		validator(map, "ctl00_SiteContentPlaceHolder_FormView1_rblSPECIALIZED_SKILLS_IND_0", false);
		//不管
		//是否参军
		{
			boolean flag = customer.getArmy() != null;
			validator(map, "ctl00_SiteContentPlaceHolder_FormView1_rblMILITARY_SERVICE_IND_0", flag);
			//每参加过一个兵种就加一个空对象
			validator(map,
					"ctl00_SiteContentPlaceHolder_FormView1_dtlMILITARY_SERVICE_ctl_InsertButtonMILITARY_SERVICE",
					Lists.newArrayList(new Object()));
			if (flag) {//如果参军过，有如下信息
				ArmyDto a = customer.getArmy();
				validator(map,
						"ctl00_SiteContentPlaceHolder_FormView1_dtlMILITARY_SERVICE_ctl00_ddlMILITARY_SVC_CNTRY",
						a.getCountry());
				validator(map,
						"ctl00_SiteContentPlaceHolder_FormView1_dtlMILITARY_SERVICE_ctl00_tbxMILITARY_SVC_BRANCH",
						a.getType());
				validator(map, "ctl00_SiteContentPlaceHolder_FormView1_dtlMILITARY_SERVICE_ctl00_tbxMILITARY_SVC_RANK",
						a.getRank());
				validator(map,
						"ctl00_SiteContentPlaceHolder_FormView1_dtlMILITARY_SERVICE_ctl00_tbxMILITARY_SVC_SPECIALTY",
						a.getSpecialty());
				validator(map,
						"ctl00_SiteContentPlaceHolder_FormView1_dtlMILITARY_SERVICE_ctl00_ddlMILITARY_SVC_FROMDay",
						DateUtils.formatDate(a.getStartDate(), "d"));
				validator(map,
						"ctl00_SiteContentPlaceHolder_FormView1_dtlMILITARY_SERVICE_ctl00_ddlMILITARY_SVC_FROMMonth",
						DateUtils.formatDate(a.getStartDate(), "M"));
				validator(map,
						"ctl00_SiteContentPlaceHolder_FormView1_dtlMILITARY_SERVICE_ctl00_tbxMILITARY_SVC_FROMYear",
						DateUtils.formatDate(a.getStartDate(), "yyyy"));
				validator(map,
						"ctl00_SiteContentPlaceHolder_FormView1_dtlMILITARY_SERVICE_ctl00_ddlMILITARY_SVC_TODay",
						DateUtils.formatDate(a.getEndDate(), "d"));
				validator(map,
						"ctl00_SiteContentPlaceHolder_FormView1_dtlMILITARY_SERVICE_ctl00_ddlMILITARY_SVC_TOMonth",
						DateUtils.formatDate(a.getEndDate(), "M"));
				validator(map,
						"ctl00_SiteContentPlaceHolder_FormView1_dtlMILITARY_SERVICE_ctl00_tbxMILITARY_SVC_TOYear",
						DateUtils.formatDate(a.getEndDate(), "yyyy"));
			}
		}

		//Security Background 全部勾选否
		validator(map, "ctl00_SiteContentPlaceHolder_FormView1_rblINSURGENT_ORG_IND_0", false);
		validator(map, "ctl00_SiteContentPlaceHolder_FormView1_rblDisease_0", false);
		validator(map, "ctl00_SiteContentPlaceHolder_FormView1_rblDisorder_0", false);
		validator(map, "ctl00_SiteContentPlaceHolder_FormView1_rblDruguser_0", false);
		validator(map, "ctl00_SiteContentPlaceHolder_FormView1_rblArrested_0", false);
		validator(map, "ctl00_SiteContentPlaceHolder_FormView1_rblControlledSubstances_0", false);
		validator(map, "ctl00_SiteContentPlaceHolder_FormView1_rblProstitution_0", false);
		validator(map, "ctl00_SiteContentPlaceHolder_FormView1_rblMoneyLaundering_0", false);
		validator(map, "ctl00_SiteContentPlaceHolder_FormView1_rblHumanTrafficking_0", false);
		validator(map, "ctl00_SiteContentPlaceHolder_FormView1_rblAssistedSevereTrafficking_0", false);
		validator(map, "ctl00_SiteContentPlaceHolder_FormView1_rblHumanTraffickingRelated_0", false);
		validator(map, "ctl00_SiteContentPlaceHolder_FormView1_rblIllegalActivity_0", false);
		validator(map, "ctl00_SiteContentPlaceHolder_FormView1_rblTerroristActivity_0", false);
		validator(map, "ctl00_SiteContentPlaceHolder_FormView1_rblTerroristSupport_0", false);
		validator(map, "ctl00_SiteContentPlaceHolder_FormView1_rblTerroristOrg_0", false);
		validator(map, "ctl00_SiteContentPlaceHolder_FormView1_rblGenocide_0", false);
		validator(map, "ctl00_SiteContentPlaceHolder_FormView1_rblTorture_0", false);
		validator(map, "ctl00_SiteContentPlaceHolder_FormView1_rblExViolence_0", false);
		validator(map, "ctl00_SiteContentPlaceHolder_FormView1_rblChildSoldier_0", false);
		validator(map, "ctl00_SiteContentPlaceHolder_FormView1_rblReligiousFreedom_0", false);
		validator(map, "ctl00_SiteContentPlaceHolder_FormView1_rblPopulationControls_0", false);
		validator(map, "ctl00_SiteContentPlaceHolder_FormView1_rblTransplant_0", false);
		validator(map, "ctl00_SiteContentPlaceHolder_FormView1_rblRemovalHearing_0", false);
		validator(map, "ctl00_SiteContentPlaceHolder_FormView1_rblImmigrationFraud_0", false);
		validator(map, "ctl00_SiteContentPlaceHolder_FormView1_rblFailToAttend_0", false);
		validator(map, "ctl00_SiteContentPlaceHolder_FormView1_rblVisaViolation_0", false);
		validator(map, "ctl00_SiteContentPlaceHolder_FormView1_rblChildCustody_0", false);
		validator(map, "ctl00_SiteContentPlaceHolder_FormView1_rblVotingViolation_0", false);
		validator(map, "ctl00_SiteContentPlaceHolder_FormView1_rblRenounceExp_0", false);
		validator(map, "ctl00_SiteContentPlaceHolder_FormView1_rblAttWoReimb_0", false);
		//不管
		validator(map, "ctl00_cphMain_imageFileUpload", "./tmp/" + customer.getId() + ".jpg");
		//上传的照片文件名字，最好用绝对路径消除操作系统间的差异
		validator(map, "ctl00_SiteContentPlaceHolder_FormView3_rblPREP_IND_0", false);
		//不管
		//护照号码
		validator(map, "ctl00_SiteContentPlaceHolder_PPTNumTbx", customer.getPassport());

		return map;
	}
}
