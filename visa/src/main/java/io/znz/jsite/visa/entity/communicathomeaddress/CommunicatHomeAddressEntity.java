/**
 * CommunicatHomeAddressEntity.java
 * io.znz.jsite.visa.entity.communicathomeaddress
 * Copyright (c) 2017, 北京科技有限公司版权所有.
*/

package io.znz.jsite.visa.entity.communicathomeaddress;

import java.util.Date;

import lombok.Data;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

/**
 * 通信地址与家庭地址
 * @author   崔建斌
 * @Date	 2017年6月22日 	 
 */
@Data
@Table("visa_new_communicat_home_address")
public class CommunicatHomeAddressEntity {
	@Id(auto = true)
	@Column
	@Comment("主键")
	private long id;
	@Column
	@Comment("客户id")
	private long customerId;
	@Column
	@Comment("主要电话号码")
	private String mainPhoneNum;
	@Column
	@Comment("次要电话号码")
	private String minorPhoneNum;
	@Column
	@Comment("工作电话号码")
	private String workPhoneNum;
	@Column
	@Comment("电子邮箱")
	private String email;
	@Column
	@Comment("护照类型")
	private Integer passportType;
	@Column
	@Comment("护照号码")
	private String passportNumber;
	@Column
	@Comment("护照本号码")
	private String passportBookNum;
	@Column
	@Comment("签发国家")
	private String issuingCountry;
	@Column
	@Comment("签发地城市")
	private String issuingCity;
	@Column
	@Comment("签发地省份")
	private String issuingProvince;
	@Column
	@Comment("签发地国家")
	private String issuingLocalCountry;
	@Column
	@Comment("签发日期")
	private Date issueDate;
	@Column
	@Comment("有效期限")
	private Date expirationDate;
	@Column
	@Comment("创建时间")
	private Date createTime;
	@Column
	@Comment("更新时间")
	private Date updateTime;
	@Column
	@Comment("状态")
	private Integer status;
	@Column
	@Comment("备注")
	private String remark;
}
