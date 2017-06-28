/**
 * NewCustomerJpDto.java
 * io.znz.jsite.visa.dto
 * Copyright (c) 2017, 北京科技有限公司版权所有.
*/

package io.znz.jsite.visa.dto;

import io.znz.jsite.visa.entity.communicathomeaddress.CommunicatJPHomeAddressEntity;
import io.znz.jsite.visa.entity.japan.NewFinanceJpEntity;
import io.znz.jsite.visa.entity.japan.NewOldnameJpEntity;
import io.znz.jsite.visa.entity.japan.NewOldpassportJpEntity;
import io.znz.jsite.visa.entity.japan.NewOrthercountryJpEntity;
import io.znz.jsite.visa.entity.japan.NewRecentlyintojpJpEntity;
import io.znz.jsite.visa.entity.japan.NewWorkinfoJpEntity;
import io.znz.jsite.visa.entity.taxpayerauthenticationcode.TaxpayerJPAuthenticationCodeEntity;

import java.util.Date;
import java.util.List;

import lombok.Data;

/**
 * 日本护照信息
 * @author   崔建斌
 * @Date	 2017年6月27日 	 
 */
@Data
public class NewCustomerJpDto {

	//主键
	private Long id;
	//员工表id
	private Integer empid;
	//英文全名
	private String chinesefullname;
	//中文姓
	private String chinesexing;
	//中文姓拼音
	private String chinesexingen;
	//中文名字
	private String chinesename;
	//中文名字拼音
	private String chinesenameen;
	//护照号
	private String passport;
	//性别
	private Integer gender;
	//护照签发日期
	private Date passportsenddate;
	//护照有效日期
	private Date passporteffectdate;
	//出生日期
	private Date birthday;
	//护照类别
	private Integer passporttype;
	//护照签发城市
	private String passportsendcity;
	//护照签发省份
	private String passportsendprovice;
	//护照签发机关
	private String passportsendoffice;
	//身份证号
	private String idcard;
	//办理人国籍
	private String docountry;
	//出生省份
	private String birthprovince;
	//出生城市
	private String birthcity;
	//居住省份
	private String nowprovince;
	//居住城市
	private String nowcity;
	//手机号
	private String phone;
	//出生国家(国籍)
	private String birthcountry;
	//家庭电话
	private String familyphone;
	//邮箱
	private String email;
	//详细地址街道
	private String addresssmall;
	//婚姻状况
	private Integer marrystate;
	//详细地址英文
	private String familywork;
	//创建时间
	private Date createtime;
	//修改时间
	private Date updatetime;
	//状态
	private Integer status;
	//递送次数
	private Integer sendcount = 0;
	//是否是主申请人
	private boolean main = true;
	//分享次数
	private Integer sharecount = 0;
	//国家码
	private String countrynum;
	//出生日期
	private Date birthdate;
	//签发机关
	private String visaoffice;
	//护照本号码
	private String passportbooknum;
	//护照机读码
	private String passportreadnum;
	//姓名电报码
	private String nameTelegramCode;

	private NewWorkinfoJpEntity workinfo;

	private List<NewFinanceJpEntity> financeJpList;

	private NewOldpassportJpEntity passportlose;

	private NewOldnameJpEntity oldname;

	private List<NewOrthercountryJpEntity> orthercountrylist;

	private List<NewRecentlyintojpJpEntity> recentlyintojpJpList;

	public boolean isMain() {
		return this.main;
	}

	//日本纳税人认证码
	private TaxpayerJPAuthenticationCodeEntity taxpayerauthenticat;
	//日本通信地址与家庭地址是否一致
	private CommunicatJPHomeAddressEntity commhomeaddress;
}
