/**
 * RelationShipEntity.java
 * io.znz.jsite.visa.entity.relationship
 * Copyright (c) 2017, 北京科技有限公司版权所有.
*/

package io.znz.jsite.visa.entity.relationship;

import java.util.Date;

import lombok.Data;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

/**
 * 与你的关系
 * @author   崔建斌
 * @Date	 2017年6月22日 	 
 */
@Data
@Table("visa_new_relationship")
public class RelationShipEntity {
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
	@Comment("美国联系地址")
	private String contactAddress;
	@Column
	@Comment("电话号码")
	private String phone;
	@Column
	@Comment("电子邮箱")
	private String email;
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
