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
import io.znz.jsite.visa.bean.Army;
import io.znz.jsite.visa.bean.Option;
import io.znz.jsite.visa.bean.Passport;
import io.znz.jsite.visa.bean.Together;
import io.znz.jsite.visa.bean.Travel;
import io.znz.jsite.visa.bean.Usa;
import io.znz.jsite.visa.bean.helper.Gender;
import io.znz.jsite.visa.bean.helper.Period;
import io.znz.jsite.visa.bean.helper.Relation;
import io.znz.jsite.visa.entity.customer.NewLanguageEntity;
import io.znz.jsite.visa.entity.customer.NewOrthercountryEntity;
import io.znz.jsite.visa.entity.customer.NewVisitedcountryEntity;
import io.znz.jsite.visa.entity.customer.NewWorkedplaceEntity;
import io.znz.jsite.visa.entity.usa.NewCustomerEntity;
import io.znz.jsite.visa.enums.GenderEnum;
import io.znz.jsite.visa.enums.OrderVisaApproStatusEnum;
import io.znz.jsite.visa.service.TelecodeService;
import io.znz.jsite.visa.simulator.dto.CustomerDto;
import io.znz.jsite.visa.simulator.dto.FamilyDto;
import io.znz.jsite.visa.simulator.dto.HistoryDto;
import io.znz.jsite.visa.simulator.dto.SchoolDto;
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

import com.google.common.collect.Lists;
import com.uxuexi.core.common.util.EnumUtil;
import com.uxuexi.core.common.util.Util;

/**
 * 自动填表service
 * 	
 * @author   朱晓川
 * @Date	 2017年7月6日 	 
 */
@Service
public class SimulateViewService extends NutzBaseService<NewCustomerEntity> {

	private static final DateFormat dateFormat = new SimpleDateFormat("MMM", Locale.ENGLISH);
	private static final DecimalFormat decimalFormat = new DecimalFormat("00");

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

		//TODO 出生地中文
		//TODO 出生城市中文

		/*-----------现居地信息-----------*/
		c.setCountry(n.getNowcountry());//国籍
		c.setProvince(n.getNowprovince());
		c.setCity(n.getNowcity());

		c.setAddress(n.getAddressbig());
		c.setRoom(n.getAddresssmall());

		c.setAddressEN(n.getAddressbigen());
		c.setRoomEN(n.getAddressmallen());

	}

	private CustomerDto transfrom(NewCustomerEntity nCustomer, Integer customerId) {
		CustomerDto customer = new CustomerDto();
		transfromBasic(nCustomer, customer);

		//其他国家常住居民
		List<NewOrthercountryEntity> otherCountry = dbDao.query(NewOrthercountryEntity.class,
				Cnd.where("customerid", "=", customerId), null);

		//会说的语言
		List<NewLanguageEntity> languages = dbDao.query(NewLanguageEntity.class,
				Cnd.where("customerid", "=", customerId), null);

		//去过的国家
		List<NewVisitedcountryEntity> visitedCountry = dbDao.query(NewVisitedcountryEntity.class,
				Cnd.where("customerid", "=", customerId), null);

		//所属公益组织
		List<NewWorkedplaceEntity> commonweals = dbDao.query(NewWorkedplaceEntity.class,
				Cnd.where("customerid", "=", customerId), null);

		//财务证明  美国  没有
		List<Option> finances = Lists.newArrayList();

		//学历

		// 工作经历

		//  家人亲戚

		// 历史出行记录

		//护照信息

		//Travel info

		return customer;
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

		validator(map, "ctl00_SiteContentPlaceHolder_FormView1_ddlDOBDay_1",
				DateUtils.formatDate(customer.getBirthday(), "dd"));
		validator(map, "ctl00_SiteContentPlaceHolder_FormView1_ddlDOBMonth_1", dateFormatLocal(customer.getBirthday()));
		validator(map, "ctl00_SiteContentPlaceHolder_FormView1_tbxDOBYear_1",
				DateUtils.formatDate(customer.getBirthday(), "yyyy"));
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
			Passport passport = customer.getOldPassport();
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
		//不管
		validator(map, "ctl00_SiteContentPlaceHolder_FormView1_rblSpecificTravel_0", true);
		//不管
		//到达美国的相应信息
		Travel travel = customer.getTravel();
		{
			DateTime dt = new DateTime(travel.getArrivalDate());
			if (dt.isBeforeNow()) {
				log.error("入境时间不能在当前时间之前!");
				throw new JSiteException("入境时间不能在当前时间之前!");
			}
			validator(map, "ctl00_SiteContentPlaceHolder_FormView1_ddlARRIVAL_US_DTEDay",
					DateUtils.formatDate(dt.toDate(), "d"));
			validator(map, "ctl00_SiteContentPlaceHolder_FormView1_ddlARRIVAL_US_DTEMonth",
					DateUtils.formatDate(dt.toDate(), "M"));
			validator(map, "ctl00_SiteContentPlaceHolder_FormView1_tbxARRIVAL_US_DTEYear",
					DateUtils.formatDate(dt.toDate(), "yyyy"));
			validator(map, "ctl00_SiteContentPlaceHolder_FormView1_tbxArriveCity", travel.getEntryCity());

			//离开美国的相应信息
			Period period = travel.getPeriod();
			if (!Util.isEmpty(period)) {
				switch (travel.getPeriod()) {
				case DAY:
					dt = dt.plusDays(travel.getStay());
					break;
				case WEEK:
					dt = dt.plusWeeks(travel.getStay());
					break;
				case MONTH:
					dt = dt.plusMonths(travel.getStay());
					break;
				case YEAR:
					dt = dt.plusYears(travel.getStay());
					break;
				default:
					log.error("停留时间类型错误，只能是[年Y月M周W日D]");
				}
				validator(map, "ctl00_SiteContentPlaceHolder_FormView1_ddlDEPARTURE_US_DTEDay",
						DateUtils.formatDate(dt.toDate(), "d"));
				validator(map, "ctl00_SiteContentPlaceHolder_FormView1_ddlDEPARTURE_US_DTEMonth",
						DateUtils.formatDate(dt.toDate(), "M"));
				validator(map, "ctl00_SiteContentPlaceHolder_FormView1_tbxDEPARTURE_US_DTEYear",
						DateUtils.formatDate(dt.toDate(), "yyyy"));
				validator(map, "ctl00_SiteContentPlaceHolder_FormView1_tbxDepartCity", travel.getEntryCity());
			} else {
				log.error("停留时间类型错误，只能是[年Y月M周W日D]");
			}
		}

		//有几个城市就生成几个空对象
		validator(map, "ctl00_SiteContentPlaceHolder_FormView1_dtlTravelLoc_ctl_InsertButtonTravelLoc",
				Lists.newArrayList(new Object()));
		//将在美国访问那些城市
		validator(map, "ctl00_SiteContentPlaceHolder_FormView1_dtlTravelLoc_ctl00_tbxSPECTRAVEL_LOCATION",
				travel.getEntryCity());
		//在美国逗留的地址
		validator(map, "ctl00_SiteContentPlaceHolder_FormView1_tbxStreetAddress1", travel.getAddress());
		validator(map, "ctl00_SiteContentPlaceHolder_FormView1_tbxCity", travel.getEntryCity());
		validator(map, "ctl00_SiteContentPlaceHolder_FormView1_ddlTravelState", travel.getEntryProvince());
		validator(map, "ctl00_SiteContentPlaceHolder_FormView1_tbZIPCode", travel.getZipCode());
		//由谁支付,S,O,C
		validator(map, "ctl00_SiteContentPlaceHolder_FormView1_ddlWhoIsPaying", travel.getPayer().getValue());
		//如果是由个人S支付，则忽略以下字段
		switch (travel.getPayer()) {
		case COMPANY:
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
		case OTHER:
			//如果是别人O支付,相关信息
			validator(map, "ctl00_SiteContentPlaceHolder_FormView1_tbxPayerSurname", travel.getPayerLastNameEN());
			validator(map, "ctl00_SiteContentPlaceHolder_FormView1_tbxPayerGivenName", travel.getPayerFirstNameEN());
			validator(map, "ctl00_SiteContentPlaceHolder_FormView1_tbxPayerPhone", travel.getPayerPhone());
			validator(map, "ctl00_SiteContentPlaceHolder_FormView1_tbxPAYER_EMAIL_ADDR", travel.getPayerEmail());
			validator(map, "ctl00_SiteContentPlaceHolder_FormView1_ddlPayerRelationship", travel.getPayerRelation()
					.getValue());
			//选择，与本人关系
			validator(map, "ctl00_SiteContentPlaceHolder_FormView1_rblPayerAddrSameAsInd_0", true);
			break;
		case SELF:
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
					Together t = travel.getTogethers().get(i);
					validator(map,
							"ctl00_SiteContentPlaceHolder_FormView1_dlTravelCompanions_ctl" + decimalFormat.format(i)
									+ "_tbxSurname", t.getLastNameEN());
					validator(map,
							"ctl00_SiteContentPlaceHolder_FormView1_dlTravelCompanions_ctl" + decimalFormat.format(i)
									+ "_tbxGivenName", t.getFirstNameEN());

					//如果亲属关系为空，则使用'其他'
					Relation relation = t.getRelation();
					if (!Util.isEmpty(relation)) {
						validator(
								map,
								"ctl00_SiteContentPlaceHolder_FormView1_dlTravelCompanions_ctl"
										+ decimalFormat.format(i) + "_ddlTCRelationship", relation.getValue());
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
					//去过几次生成几个空对象
					HistoryDto h = histories.get(i);
					//过往到过美国的相关信息
					validator(map,
							"ctl00_SiteContentPlaceHolder_FormView1_dtlPREV_US_VISIT_ctl" + decimalFormat.format(i)
									+ "_ddlPREV_US_VISIT_DTEDay", DateUtils.formatDate(h.getArrivalDate(), "d"));
					validator(map,
							"ctl00_SiteContentPlaceHolder_FormView1_dtlPREV_US_VISIT_ctl" + decimalFormat.format(i)
									+ "_ddlPREV_US_VISIT_DTEMonth", DateUtils.formatDate(h.getArrivalDate(), "M"));
					validator(map,
							"ctl00_SiteContentPlaceHolder_FormView1_dtlPREV_US_VISIT_ctl" + decimalFormat.format(i)
									+ "_tbxPREV_US_VISIT_DTEYear", DateUtils.formatDate(h.getArrivalDate(), "yyyy"));
					validator(map,
							"ctl00_SiteContentPlaceHolder_FormView1_dtlPREV_US_VISIT_ctl" + decimalFormat.format(i)
									+ "_tbxPREV_US_VISIT_LOS", String.valueOf(h.getStay()));
					validator(map,
							"ctl00_SiteContentPlaceHolder_FormView1_dtlPREV_US_VISIT_ctl" + decimalFormat.format(i)
									+ "_ddlPREV_US_VISIT_LOS_CD", h.getPeriod().getValue());
					list.add(new Object());
				}
			}
		}
		//如果有，美国驾照的相关信息
		Usa usa = customer.getUsa();
		if (usa != null)
			usa = new Usa();
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
		Relation contactsRelation = travel.getContactsRelation();
		if (!Util.isEmpty(contactsRelation)) {
			validator(map, "ctl00_SiteContentPlaceHolder_FormView1_ddlUS_POC_REL_TO_APP", travel.getContactsRelation()
					.getValue());
		}

		//和我的关系
		validator(map, "ctl00_SiteContentPlaceHolder_FormView1_tbxUS_POC_ADDR_LN1", travel.getContactsAddress());
		validator(map, "ctl00_SiteContentPlaceHolder_FormView1_tbxUS_POC_ADDR_CITY", travel.getContactsCity());
		validator(map, "ctl00_SiteContentPlaceHolder_FormView1_ddlUS_POC_ADDR_STATE", travel.getContactsProvince());
		validator(map, "ctl00_SiteContentPlaceHolder_FormView1_tbxUS_POC_ADDR_POSTAL_CD", travel.getContactsZipCode());
		validator(map, "ctl00_SiteContentPlaceHolder_FormView1_tbxUS_POC_HOME_TEL", travel.getContactsPhone());
		validator(map, "ctl00_SiteContentPlaceHolder_FormView1_tbxUS_POC_EMAIL_ADDR", travel.getContactsEmail());

		//家庭相关信息
		if (customer.getFather() != null && StringUtils.isNotBlank(customer.getFather().getLastNameEN())
				&& StringUtils.isNotBlank(customer.getFather().getFirstNameEN())) {
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
					.getUsaStatus().getValue());
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
					.getUsaStatus().getValue());
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
					validator(map, "ctl00_SiteContentPlaceHolder_FormView1_dlUSRelatives_ctl" + decimalFormat.format(i)
							+ "_ddlUS_REL_TYPE", f.getRelation().getValue());
					validator(map, "ctl00_SiteContentPlaceHolder_FormView1_dlUSRelatives_ctl" + decimalFormat.format(i)
							+ "_ddlUS_REL_STATUS", f.getUsaStatus().getValue());
				}
			}
		}
		//是否有其他非直系亲属
		validator(map, "ctl00_SiteContentPlaceHolder_FormView1_rblUS_OTHER_RELATIVE_IND_0", customer.isFriendInUSA());

		//婚姻状况
		{
			validator(map, "ctl00_SiteContentPlaceHolder_FormView1_ddlAPP_MARITAL_STATUS", customer.getSpouse()
					.getState().getValue());

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
			//已婚M
			validator(map, "ctl00_SiteContentPlaceHolder_FormView1_tbxSpouseSurname", customer.getSpouse()
					.getLastNameEN());
			validator(map, "ctl00_SiteContentPlaceHolder_FormView1_tbxSpouseGivenName", customer.getSpouse()
					.getFirstNameEN());
			validator(map, "ctl00_SiteContentPlaceHolder_FormView1_ddlSpouseNatDropDownList", customer.getSpouse()
					.getNationality());
			validator(map, "ctl00_SiteContentPlaceHolder_FormView1_tbxSpousePOBCity", customer.getSpouse()
					.getBirthCity());
			validator(map, "ctl00_SiteContentPlaceHolder_FormView1_ddlSpousePOBCountry", customer.getSpouse()
					.getBirthCountry());
			boolean different = StringUtils.isNotBlank(customer.getSpouse().getZipCode())
					&& StringUtils.isNotBlank(customer.getSpouse().getAddressEN());
			validator(map, "ctl00_SiteContentPlaceHolder_FormView1_ddlSpouseAddressType", different ? "O" : "H");//如果配偶与我地址不同则为"O"
			validator(map, "ctl00_SiteContentPlaceHolder_FormView1_tbxSPOUSE_ADDR_LN1", customer.getSpouse()
					.getAddressEN());
			validator(map, "ctl00_SiteContentPlaceHolder_FormView1_tbxSPOUSE_ADDR_CITY", customer.getSpouse().getCity());
			validator(map, "ctl00_SiteContentPlaceHolder_FormView1_tbxSPOUSE_ADDR_STATE", customer.getSpouse()
					.getProvince());
			validator(map, "ctl00_SiteContentPlaceHolder_FormView1_tbxSPOUSE_ADDR_POSTAL_CD", customer.getSpouse()
					.getZipCode());
			validator(map, "ctl00_SiteContentPlaceHolder_FormView1_ddlSPOUSE_ADDR_CNTRY", customer.getSpouse()
					.getCountry());
			//丧偶W
			validator(map, "ctl00_SiteContentPlaceHolder_FormView1_tbxSURNAME", customer.getSpouse().getLastNameEN());
			validator(map, "ctl00_SiteContentPlaceHolder_FormView1_tbxGIVEN_NAME", customer.getSpouse()
					.getFirstNameEN());
			//离婚D
			validator(map, "ctl00_SiteContentPlaceHolder_FormView1_tbxNumberOfPrevSpouses", "1");
			//离异的配偶数量
			validator(map, "ctl00_SiteContentPlaceHolder_FormView1_DListSpouse_ctl00_tbxSURNAME", customer.getSpouse()
					.getLastNameEN());
			validator(map, "ctl00_SiteContentPlaceHolder_FormView1_DListSpouse_ctl00_tbxGIVEN_NAME", customer
					.getSpouse().getFirstNameEN());
			validator(map, "ctl00_SiteContentPlaceHolder_FormView1_DListSpouse_ctl00_ddlDOBDay",
					DateUtils.formatDate(customer.getSpouse().getBirthday(), "d"));
			validator(map, "ctl00_SiteContentPlaceHolder_FormView1_DListSpouse_ctl00_ddlDOBMonth",
					dateFormatLocal(customer.getSpouse().getBirthday()));
			validator(map, "ctl00_SiteContentPlaceHolder_FormView1_DListSpouse_ctl00_tbxDOBYear",
					DateUtils.formatDate(customer.getSpouse().getBirthday(), "yyyy"));
			validator(map, "ctl00_SiteContentPlaceHolder_FormView1_DListSpouse_ctl00_ddlSpouseNatDropDownList",
					customer.getSpouse().getNationality());
			validator(map, "ctl00_SiteContentPlaceHolder_FormView1_DListSpouse_ctl00_tbxSpousePOBCity", customer
					.getSpouse().getBirthCity());
			validator(map, "ctl00_SiteContentPlaceHolder_FormView1_DListSpouse_ctl00_ddlSpousePOBCountry", customer
					.getSpouse().getBirthCountry());

			DateTime start = new DateTime(customer.getSpouse().getWedDate());
			if (start.isAfterNow()) {

				log.error("结婚时间不能在当前时间之后!");
				throw new JSiteException("结婚时间不能在当前时间之后!");
			}
			DateTime end = new DateTime(customer.getSpouse().getDivorceDate());

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
		}
		validator(map, "ctl00_SiteContentPlaceHolder_FormView1_DListSpouse_ctl00_tbxHowMarriageEnded", customer
				.getSpouse().getDivorceReasonEN());
		validator(map, "ctl00_SiteContentPlaceHolder_FormView1_DListSpouse_ctl00_ddlMarriageEnded_CNTRY", customer
				.getSpouse().getDivorceCountry());

		//当前工作信息
		if (customer.getWork() != null) {
			validator(map, "ctl00_SiteContentPlaceHolder_FormView1_ddlPresentOccupation", customer.getWork()
					.getIndustry());
			//如果当前职业为N，有如下
			validator(map, "ctl00_SiteContentPlaceHolder_FormView1_tbxExplainOtherPresentOccupation",
					"the not employed reason is...");
			//如果当前职业为H或者RT，有如下
			//什么也不填
			//如果当前职业为所有其他正常职业
			validator(map, "ctl00_SiteContentPlaceHolder_FormView1_tbxEmpSchName", customer.getWork().getNameEN());
			validator(map, "ctl00_SiteContentPlaceHolder_FormView1_tbxEmpSchAddr1", customer.getWork().getAddressEN());
			validator(map, "ctl00_SiteContentPlaceHolder_FormView1_tbxEmpSchCity", customer.getWork().getCity());
			validator(map, "ctl00_SiteContentPlaceHolder_FormView1_tbxWORK_EDUC_ADDR_STATE", customer.getWork()
					.getProvince());
			validator(map, "ctl00_SiteContentPlaceHolder_FormView1_tbxWORK_EDUC_ADDR_POSTAL_CD", customer.getWork()
					.getZipCode());
			validator(map, "ctl00_SiteContentPlaceHolder_FormView1_tbxWORK_EDUC_TEL", customer.getWork().getPhone());
			validator(map, "ctl00_SiteContentPlaceHolder_FormView1_ddlEmpSchCountry", customer.getWork().getCountry());
			validator(map, "ctl00_SiteContentPlaceHolder_FormView1_tbxCURR_MONTHLY_SALARY",
					String.valueOf(Double.valueOf(customer.getWork().getSalary()).intValue()));
			validator(map, "ctl00_SiteContentPlaceHolder_FormView1_tbxDescribeDuties", customer.getWork().getDutyEN());
		}
		//曾经雇佣信息
		{
			boolean flag = customer.getWorks() != null && customer.getWorks().size() > 0;
			validator(map, "ctl00_SiteContentPlaceHolder_FormView1_rblPreviouslyEmployed_0", flag);
			List list = Lists.newArrayList();
			validator(map, "ctl00_SiteContentPlaceHolder_FormView1_dtlPrevEmpl_ctl_InsertButtonPrevEmpl", list);
			if (flag) {
				for (int i = 0; i < customer.getWorks().size(); i++) {
					//雇佣几次生成几个空对象
					list.add(new Object());
					//雇佣信息1
					WorkDto w = customer.getWorks().get(i);
					validator(map, "ctl00_SiteContentPlaceHolder_FormView1_dtlPrevEmpl_ctl" + decimalFormat.format(i)
							+ "_tbEmployerName", w.getNameEN());
					validator(map, "ctl00_SiteContentPlaceHolder_FormView1_dtlPrevEmpl_ctl" + decimalFormat.format(i)
							+ "_tbEmployerStreetAddress1", w.getRoomEN());
					validator(map, "ctl00_SiteContentPlaceHolder_FormView1_dtlPrevEmpl_ctl" + decimalFormat.format(i)
							+ "_tbEmployerStreetAddress2", w.getAddressEN());
					validator(map, "ctl00_SiteContentPlaceHolder_FormView1_dtlPrevEmpl_ctl" + decimalFormat.format(i)
							+ "_tbEmployerCity", w.getCity());
					validator(map, "ctl00_SiteContentPlaceHolder_FormView1_dtlPrevEmpl_ctl" + decimalFormat.format(i)
							+ "_tbxPREV_EMPL_ADDR_STATE", w.getProvince());
					validator(map, "ctl00_SiteContentPlaceHolder_FormView1_dtlPrevEmpl_ctl" + decimalFormat.format(i)
							+ "_tbxPREV_EMPL_ADDR_POSTAL_CD", w.getZipCode());
					validator(map, "ctl00_SiteContentPlaceHolder_FormView1_dtlPrevEmpl_ctl" + decimalFormat.format(i)
							+ "_DropDownList2", w.getCountry());
					validator(map, "ctl00_SiteContentPlaceHolder_FormView1_dtlPrevEmpl_ctl" + decimalFormat.format(i)
							+ "_tbEmployerPhone", w.getPhone());
					validator(map, "ctl00_SiteContentPlaceHolder_FormView1_dtlPrevEmpl_ctl" + decimalFormat.format(i)
							+ "_tbJobTitle", w.getJobEN());
					validator(map, "ctl00_SiteContentPlaceHolder_FormView1_dtlPrevEmpl_ctl" + decimalFormat.format(i)
							+ "_tbSupervisorSurname", w.getBossLastNameEN());
					validator(map, "ctl00_SiteContentPlaceHolder_FormView1_dtlPrevEmpl_ctl" + decimalFormat.format(i)
							+ "_tbSupervisorGivenName", w.getBossFirstNameEN());
					validator(map, "ctl00_SiteContentPlaceHolder_FormView1_dtlPrevEmpl_ctl" + decimalFormat.format(i)
							+ "_ddlEmpDateFromDay", DateUtils.formatDate(w.getStartDate(), "d"));
					validator(map, "ctl00_SiteContentPlaceHolder_FormView1_dtlPrevEmpl_ctl" + decimalFormat.format(i)
							+ "_ddlEmpDateFromMonth", DateUtils.formatDate(w.getStartDate(), "M"));
					validator(map, "ctl00_SiteContentPlaceHolder_FormView1_dtlPrevEmpl_ctl" + decimalFormat.format(i)
							+ "_tbxEmpDateFromYear", DateUtils.formatDate(w.getStartDate(), "yyyy"));
					validator(map, "ctl00_SiteContentPlaceHolder_FormView1_dtlPrevEmpl_ctl" + decimalFormat.format(i)
							+ "_ddlEmpDateToDay", DateUtils.formatDate(w.getEndDate(), "d"));
					validator(map, "ctl00_SiteContentPlaceHolder_FormView1_dtlPrevEmpl_ctl" + decimalFormat.format(i)
							+ "_ddlEmpDateToMonth", DateUtils.formatDate(w.getEndDate(), "M"));
					validator(map, "ctl00_SiteContentPlaceHolder_FormView1_dtlPrevEmpl_ctl" + decimalFormat.format(i)
							+ "_tbxEmpDateToYear", DateUtils.formatDate(w.getEndDate(), "yyyy"));
					validator(map, "ctl00_SiteContentPlaceHolder_FormView1_dtlPrevEmpl_ctl" + decimalFormat.format(i)
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
					//学校
					SchoolDto s = customer.getSchools().get(i);
					validator(map, "ctl00_SiteContentPlaceHolder_FormView1_dtlPrevEduc_ctl" + decimalFormat.format(i)
							+ "_tbxSchoolName", s.getNameEN());
					validator(map, "ctl00_SiteContentPlaceHolder_FormView1_dtlPrevEduc_ctl" + decimalFormat.format(i)
							+ "_tbxSchoolAddr1", s.getRoomEN());
					validator(map, "ctl00_SiteContentPlaceHolder_FormView1_dtlPrevEduc_ctl" + decimalFormat.format(i)
							+ "_tbxSchoolAddr2", s.getAddressEN());
					validator(map, "ctl00_SiteContentPlaceHolder_FormView1_dtlPrevEduc_ctl" + decimalFormat.format(i)
							+ "_tbxSchoolCity", s.getCity());
					validator(map, "ctl00_SiteContentPlaceHolder_FormView1_dtlPrevEduc_ctl" + decimalFormat.format(i)
							+ "_tbxEDUC_INST_ADDR_STATE", s.getProvince());
					validator(map, "ctl00_SiteContentPlaceHolder_FormView1_dtlPrevEduc_ctl" + decimalFormat.format(i)
							+ "_tbxEDUC_INST_POSTAL_CD", s.getZipCode());
					validator(map, "ctl00_SiteContentPlaceHolder_FormView1_dtlPrevEduc_ctl" + decimalFormat.format(i)
							+ "_ddlSchoolCountry", s.getCountry());
					validator(map, "ctl00_SiteContentPlaceHolder_FormView1_dtlPrevEduc_ctl" + decimalFormat.format(i)
							+ "_tbxSchoolCourseOfStudy", s.getSpecialtyEN());
					validator(map, "ctl00_SiteContentPlaceHolder_FormView1_dtlPrevEduc_ctl" + decimalFormat.format(i)
							+ "_ddlSchoolFromDay", DateUtils.formatDate(s.getStartDate(), "d"));
					validator(map, "ctl00_SiteContentPlaceHolder_FormView1_dtlPrevEduc_ctl" + decimalFormat.format(i)
							+ "_ddlSchoolFromMonth", DateUtils.formatDate(s.getStartDate(), "M"));
					validator(map, "ctl00_SiteContentPlaceHolder_FormView1_dtlPrevEduc_ctl" + decimalFormat.format(i)
							+ "_tbxSchoolFromYear", DateUtils.formatDate(s.getStartDate(), "yyyy"));
					validator(map, "ctl00_SiteContentPlaceHolder_FormView1_dtlPrevEduc_ctl" + decimalFormat.format(i)
							+ "_ddlSchoolToDay", DateUtils.formatDate(s.getEndDate(), "d"));
					validator(map, "ctl00_SiteContentPlaceHolder_FormView1_dtlPrevEduc_ctl" + decimalFormat.format(i)
							+ "_ddlSchoolToMonth", DateUtils.formatDate(s.getEndDate(), "M"));
					validator(map, "ctl00_SiteContentPlaceHolder_FormView1_dtlPrevEduc_ctl" + decimalFormat.format(i)
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
				validator(map, "ctl00_SiteContentPlaceHolder_FormView1_dtlLANGUAGES_ctl" + decimalFormat.format(i)
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
					validator(map,
							"ctl00_SiteContentPlaceHolder_FormView1_dtlCountriesVisited_ctl" + decimalFormat.format(i)
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
					validator(map,
							"ctl00_SiteContentPlaceHolder_FormView1_dtlORGANIZATIONS_ctl" + decimalFormat.format(i)
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
				Army a = customer.getArmy();
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
