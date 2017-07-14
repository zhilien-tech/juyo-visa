package io.znz.jsite.visa.entity.usa;

import java.io.Serializable;

import lombok.Data;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

@Data
@Table("visa_new_pay_company")
public class NewPayCompanyEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id(auto = true)
	private Integer id;

	@Column
	@Comment("订单id")
	private Integer orderid;

	@Column
	@Comment("付费公司名字")
	private String comname;

	@Column
	@Comment("付费公司名字英文")
	private String comnameen;

	@Column
	@Comment("付费公司和我的关系")
	private String comrelation;

	@Column
	@Comment("和我的关系英文")
	private String comrelationen;

	@Column
	@Comment("付费公司国家")
	private String comcountry;
	@Column
	@Comment("付费公司省份")
	private String comprovince;

	@Column
	@Comment("付费公司城市")
	private String comcity;

	@Column
	@Comment("付费公司详细地址")
	private String comdetailaddress;

	@Column
	@Comment("付费公司大概地址")
	private String comaddress;

	@Column
	@Comment("付费公司详细地址英文")
	private String comdetailaddressen;

	@Column
	@Comment("付费公司大概地址英文")
	private String comaddressen;

	@Column
	@Comment("付费公司电话")
	private String comphone;

	@Column
	@Comment("付费公司邮编")
	private String comzipcode;

}