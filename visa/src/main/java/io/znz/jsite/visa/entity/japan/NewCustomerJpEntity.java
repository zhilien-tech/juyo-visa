package io.znz.jsite.visa.entity.japan;

import io.znz.jsite.visa.entity.communicathomeaddress.CommunicatJPHomeAddressEntity;
import io.znz.jsite.visa.entity.taxpayerauthenticationcode.TaxpayerJPAuthenticationCodeEntity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import lombok.Data;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

@Data
@Table("visa_new_customer_jp")
public class NewCustomerJpEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id(auto = true)
	private Long id;

	@Column
	@Comment("员工表id")
	private Integer empid;

	@Column
	@Comment("英文全名")
	private String chinesefullname;

	@Column
	@Comment("中文姓")
	private String chinesexing;

	@Column
	@Comment("中文姓拼音")
	private String chinesexingen;

	@Column
	@Comment("中文名字")
	private String chinesename;

	@Column
	@Comment("中文名字拼音")
	private String chinesenameen;

	@Column
	@Comment("护照号")
	private String passport;

	@Column
	@Comment("性别")
	private Integer gender;

	@Column
	@Comment("护照签发日期")
	private Date passportsenddate;

	@Column
	@Comment("护照有效日期")
	private Date passporteffectdate;
	@Column
	@Comment("出生日期")
	private Date birthday;

	@Column
	@Comment("护照类别")
	private Integer passporttype;

	@Column
	@Comment("护照签发地点")
	private String passportsendplace;

	@Column
	@Comment("护照签发地城市")
	private String passportsendcity;

	@Column
	@Comment("护照签发机关")
	private String passportsendoffice;

	@Column
	@Comment("身份证号")
	private String idcard;

	@Column
	@Comment("办理人国籍")
	private String docountry;

	@Column
	@Comment("出生省份")
	private String birthprovince;

	@Column
	@Comment("出生城市")
	private String birthcity;

	@Column
	@Comment("居住省份")
	private String nowprovince;

	@Column
	@Comment("居住城市")
	private String nowcity;

	@Column
	@Comment("手机号")
	private String phone;

	@Column
	@Comment("家庭电话")
	private String familyphone;

	@Column
	@Comment("邮箱")
	private String email;

	@Column
	@Comment("详细地址街道")
	private String addresssmall;

	@Column
	@Comment("婚姻状况")
	private Integer marrystate;

	@Column
	@Comment("详细地址英文")
	private String familywork;

	@Column
	@Comment("创建时间")
	private Date createtime;

	@Column
	@Comment("修改时间")
	private Date updatetime;

	@Column
	@Comment("状态")
	private Integer status;

	@Column
	@Comment("递送次数")
	private Integer sendcount = 0;
	@Column
	@Comment("国家码")
	private String countrynum;
	@Column
	@Comment("护照本号码")
	private String passportbooknum;
	@Column
	@Comment("护照机读码")
	private String passportreadnum;
	@Column
	@Comment("姓名电报码")
	private String nameTelegramCode;

	//是否是主申请人
	private boolean main = true;

	@Column
	@Comment("分享次数")
	private Integer sharecount = 0;
	@Column
	@Comment("错误信息")
	private String errorinfo;

	@Column
	@Comment("信息是否填写完毕")
	private int writebasicinfo;

	@Column
	@Comment("照片路径")
	private String phoneurl;

	@Column
	@Comment("照片名字")
	private String photoname;
	//名字的全拼
	private String fullnameen;

	private NewWorkinfoJpEntity workinfoJp;

	private List<NewFinanceJpEntity> financeJpList;

	private NewOldpassportJpEntity oldpassportJp;

	private NewOldnameJpEntity oldnameJp;

	private List<NewOrthercountryJpEntity> orthercountryJpList;

	private List<NewRecentlyintojpJpEntity> recentlyintojpJpList;

	private NewRecentlyintojpEntity recentlyintojp;

	public boolean isMain() {
		return this.main;
	}

	//日本纳税人认证码
	private TaxpayerJPAuthenticationCodeEntity taxpayerauthenticat;
	//日本通信地址与家庭地址是否一致
	private CommunicatJPHomeAddressEntity commhomeaddress;
}
