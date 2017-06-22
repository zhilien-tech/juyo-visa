package io.znz.jsite.visa.entity.japan;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

@Data
@Table("visa_new_customer_order_jp")
public class NewCustomerOrderJpEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id(auto = true)
	private Integer id;

	@Column
	@Comment("客户信息的id")
	private Integer customer_jp_id;

	@Column
	@Comment("订单id")
	private Long order_jp_id;

	@Column
	@Comment("创建时间")
	private Date createtime;

	@Column
	@Comment("更新时间")
	private Date updatetime;

}