package io.znz.jsite.core.entity;

import java.util.Date;

import lombok.Data;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

@Data
@Table("visa_new_customer")
public class NewCustomerEntity {
	@Id(auto = true)
	@Column
	@Comment("主键")
	private Integer id;

	@Column
	@Comment("员工id")
	private long empid;

	@Column
	@Comment("中文姓")
	private String chinesexing;
	@Column
	@Comment("中文全名")
	private String chinesefullname;

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
	@Comment("护照签发省份")
	private String passportsendprovice;

	@Column
	@Comment("护照签发城市")
	private String passportsendcity;

	@Column
	@Comment("身份证号")
	private String idcard;

	@Column
	@Comment("出生国家")
	private String birthcountry;

	@Column
	@Comment("出生省份")
	private String birthprovince;

	@Column
	@Comment("出生城市")
	private String birthcity;

	@Column
	@Comment("居住国家")
	private String nowcountry;

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
	@Comment("地址所住省")
	private String addressbig;

	@Column
	@Comment("详细地址英文")
	private String addressmallen;

	@Column
	@Comment("地址英文")
	private String addressbigen;

	@Column
	@Comment("邮编")
	private String zipcode;

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
	private Integer sendcount;

	@Column
	@Comment("分享次数")
	private Integer sharecount;

	@Column
	@Comment("通知次数")
	private Integer noticecount;
	@Column
	@Comment("护照类型")
	private Integer passporttype;
	@Column
	@Comment("国家码")
	private String countrynum;
	@Column
	@Comment("出生日期")
	private Date birthdate;
	@Column
	@Comment("签发机关")
	private String visaoffice;
	@Column
	@Comment("护照本号码")
	private String passportbooknum;
	@Column
	@Comment("护照机读码")
	private String passportreadnum;
	@Column
	@Comment("姓名电报码")
	private String nameTelegramCode;
	@Column
	@Comment("错误信息")
	private String errorinfo;
}
