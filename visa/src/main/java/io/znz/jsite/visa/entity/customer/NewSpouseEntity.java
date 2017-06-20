package io.znz.jsite.visa.entity.customer;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

@Data
@Table("visa_new_spouse")
public class NewSpouseEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id(auto = true)
	private Integer id;

	@Column
	@Comment("客户id")
	private Integer customerid;

	@Column
	@Comment("婚姻状况")
	private Integer marrystatus;

	@Column
	@Comment("结婚日期")
	private Date marrydate;

	@Column
	@Comment("配偶姓")
	private String spousexing;

	@Column
	@Comment("配偶性英文")
	private String spousexingen;

	@Column
	@Comment("配偶名字英文")
	private String spousenameen;

	@Column
	@Comment("配偶名字")
	private String spousename;

	@Column
	@Comment("配偶国籍")
	private String spousecountry;

	@Column
	@Comment("配偶出生日期")
	private Date spousebirthday;

	@Column
	@Comment("出生国家")
	private String spousebirthcountry;

	@Column
	@Comment("出生省份")
	private String spousebirthprevince;

	@Column
	@Comment("出生城市")
	private String spousebirthcity;

	@Column
	@Comment("配偶居住国家")
	private String spousenowcountry;

	@Column
	@Comment("配偶居住省份")
	private String spousenowprevince;

	@Column
	@Comment("配偶居住城市")
	private String spousenowcity;

	@Column
	@Comment("配偶联系地址")
	private String spouselinkaddress;

	@Column
	@Comment("配偶邮编")
	private String spousezipcode;

	@Column
	@Comment("配偶联系地址英文")
	private String spouselinkaddressen;

	@Column
	@Comment("配偶单位名字")
	private String spouseunitname;

	@Column
	@Comment("配偶单位电话")
	private String spouseunitphone;

	@Column
	@Comment("配偶单位名字英文")
	private String spouseunitnameen;

	@Column
	@Comment("离婚国家")
	private String splitcountry;

	@Column
	@Comment("离婚日期")
	private Date splitdate;

	@Column
	@Comment("离婚原因")
	private String splitreason;

	@Column
	@Comment("离婚原因英文")
	private String splitreasonen;

}