/**
 * NewDeliveryJapanEntity.java
 * io.znz.jsite.visa.entity.delivery
 * Copyright (c) 2017, 北京科技有限公司版权所有.
*/

package io.znz.jsite.visa.entity.delivery;

import java.util.Date;

import lombok.Data;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

/**
 * TODO(这里用一句话描述这个类的作用)
 * <p>
 * TODO(这里描述这个类补充说明 – 可选)
 *
 * @author   孙斌
 * @Date	 2017年6月23日 	 
 */
@Data
@Table("visa_new_delivery_usa")
public class NewDeliveryUSAEntity {
	@Id(auto = true)
	private Integer id;

	@Column
	@Comment("客户id")
	private Integer customer_usa_id;

	@Column
	@Comment("最早时间")
	private Date earlydate;
	@Column
	@Comment("最晚时间")
	private Date latterdate;
	@Column
	@Comment("面签时段")
	private Integer interviewtime;
	@Column
	@Comment("面签时间要求")
	private String interviewtimerequest;
	@Column
	@Comment("护照递送方式")
	private Integer visasendtype;
	@Column
	@Comment("银行自取省份")
	private String prevince;
	@Column
	@Comment("银行自取详细地址")
	private String detailplace;
	@Column
	@Comment("护照快递地址")
	private String fastmailaddress;

}
