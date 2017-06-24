/**
 * TravelPurposeEntity.java
 * io.znz.jsite.visa.entity.travelpurpose
 * Copyright (c) 2017, 北京科技有限公司版权所有.
*/

package io.znz.jsite.visa.entity.travelpurpose;

import java.util.Date;

import lombok.Data;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

/**
 * 赴美旅行目的
 * @author   崔建斌
 * @Date	 2017年6月22日 	 
 */
@Data
@Table("visa_new_usa_travel_purpose")
public class TravelPurposeEntity {
	@Id(auto = true)
	@Column
	@Comment("主键")
	private long id;
	@Column
	@Comment("客户id")
	private long customerId;
	@Column
	@Comment("订单id")
	private long orderId;
	@Column
	@Comment("赴美国旅行目的")
	private String travelPurpose;
	@Column
	@Comment("赴美国旅行具体目的")
	private String travelSpecificPurpose;
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
