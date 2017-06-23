/**
 * TravelPlanEntity.java
 * io.znz.jsite.visa.entity.travelplan
 * Copyright (c) 2017, 北京科技有限公司版权所有.
*/

package io.znz.jsite.visa.entity.travelplan;

import java.util.Date;

import lombok.Data;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

/**
 * 具体旅行计划
 * @author   崔建斌
 * @Date	 2017年6月22日 	 
 */
@Data
@Table("visa_new_travel_plan")
public class TravelPlanEntity {
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
	@Comment("预计抵达美国日期")
	private Date arrivaledDate;
	@Column
	@Comment("预计停留时间")
	private Date pauseTime;
	@Column
	@Comment("在美国停留地点的地址")
	private String pausePlace;
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
