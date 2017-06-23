/**
 * SocialInsuranceNum.java
 * io.znz.jsite.visa.entity.socialinsurancenum
 * Copyright (c) 2017, 北京科技有限公司版权所有.
*/

package io.znz.jsite.visa.entity.socialinsurancenum;

import java.util.Date;

import lombok.Data;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

/**
 * 美国社会保险号码
 * @author   崔建斌
 * @Date	 2017年6月22日 	 
 */
@Data
@Table("visa_new_social_insuranceNum")
public class SocialInsuranceNum {
	@Id(auto = true)
	@Column
	@Comment("主键")
	private long id;
	@Column
	@Comment("客户id")
	private long customerId;
	@Column
	@Comment("社保号")
	private String socialSecurityNum;
	@Column
	@Comment("缴费基数")
	private double payBase;
	@Column
	@Comment("社保类型")
	private Integer socialSecurityType;
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
