package io.znz.jsite.visa.entity.usa;

import java.io.Serializable;

import lombok.Data;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

@Data
@Table("visa_new_fastmail")
public class NewFastMailEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	@Column
	@Comment("主键")
	@Id(auto = true)
	private Integer id;

	@Column
	@Comment("资料来源")
	private Integer datasource;

	@Column
	@Comment("快递单号")
	private String fastmailnum;

	@Column
	@Comment("回邮方式")
	private Integer mailmethod;

	@Column
	@Comment("回邮地址")
	private String mailaddress;

	@Column
	@Comment("联系人")
	private String linkpeople;

	@Column
	@Comment("电话")
	private String phone;
	@Column
	@Comment("发票内容")
	private String invoicecontent;

	@Column
	@Comment("发票头")
	private String invoicehead;

	@Column
	@Comment("备注")
	private String remaker;

	@Column
	@Comment("订单id")
	private Integer orderid;

}