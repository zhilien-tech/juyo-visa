/**
 * CustomerManageEntity.java
 * io.znz.jsite.visa.bean.entity
 * Copyright (c) 2017, 北京科技有限公司版权所有.
*/

package io.znz.jsite.visa.entity.customer;

import java.util.Date;

import lombok.Data;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

/**
 * TODO(这里用一句话描述这个类的作用)
 * @author   崔建斌
 * @Date	 2017年6月8日 	 
 */
@Data
@Table("visa_customer_management")
public class CustomerManageEntity {
	@Column
	@Id(auto = true)
	@Comment("主键")
	private Integer id;
	@Column
	@Comment("序号")
	private Integer serialNumber;
	@Column
	@Comment("公司全名")
	private String fullComName;
	@Column
	@Comment("客户来源")
	private Integer customerSource;
	@Column
	@Comment("联系人")
	private String linkman;
	@Column
	@Comment("手机")
	private String telephone;
	@Column
	@Comment("邮箱")
	private String email;
	@Column
	@Comment("状态")
	private Integer status;
	@Column
	@Comment("创建时间")
	private Date createTime;
	@Column
	@Comment("更新时间")
	private Date updateTime;
	@Column
	@Comment("备注")
	private String remark;
	@Column
	@Comment("预留字段1")
	private String res1;
	@Column
	@Comment("预留字段2")
	private String res2;
	@Column
	@Comment("预留字段3")
	private String res3;
	@Column
	@Comment("预留字段4")
	private String res4;
	@Column
	@Comment("预留字段5")
	private String res5;
}
