/**
 * ApplicantProducerEntity.java
 * io.znz.jsite.visa.entity.applicantproducer
 * Copyright (c) 2017, 北京科技有限公司版权所有.
*/

package io.znz.jsite.visa.entity.applicantproducer;

import java.util.Date;

import lombok.Data;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

/**
 * 签证信息-申请的制作者
 * @author   崔建斌
 * @Date	 2017年6月25日 	 
 */
@Data
@Table("visa_new_applicant_producer")
public class ApplicantProducerEntity {
	@Id(auto = true)
	@Column
	@Comment("主键")
	private long id;
	@Column
	@Comment("客户id")
	private long customerId;
	@Column
	@Comment("制作者姓名")
	private String producerName;
	@Column
	@Comment("制作者姓")
	private String producerXing;
	@Column
	@Comment("制作者名")
	private String producerMing;
	@Column
	@Comment("组织名称")
	private String organizeName;
	@Column
	@Comment("详细地址")
	private String addressDetails;
	@Column
	@Comment("城市")
	private String city;
	@Column
	@Comment("省份")
	private String province;
	@Column
	@Comment("邮编")
	private String postCode;
	@Column
	@Comment("国家")
	private String country;
	@Column
	@Comment("与你的关系")
	private String relation;
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
