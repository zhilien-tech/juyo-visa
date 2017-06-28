/**
 * TaxpayerJPAuthenticationCodeEntity.java
 * io.znz.jsite.visa.entity.taxpayerauthenticationcode
 * Copyright (c) 2017, 北京科技有限公司版权所有.
*/

package io.znz.jsite.visa.entity.taxpayerauthenticationcode;

import java.util.Date;

import lombok.Data;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

/**
 * 日本纳税人认证码
 * @author   崔建斌
 * @Date	 2017年6月27日 	 
 */
@Data
@Table("visa_new_taxpayer_authentication_code_jp")
public class TaxpayerJPAuthenticationCodeEntity {
	@Id(auto = true)
	@Column
	@Comment("主键")
	private long id;
	@Column
	@Comment("客户id")
	private long customerId;
	@Column
	@Comment("家庭地址")
	private String homeAddress;
	@Column
	@Comment("国家")
	private String country;
	@Column
	@Comment("省份")
	private String province;
	@Column
	@Comment("城市")
	private String city;
	@Column
	@Comment("邮编")
	private String postCode;
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
