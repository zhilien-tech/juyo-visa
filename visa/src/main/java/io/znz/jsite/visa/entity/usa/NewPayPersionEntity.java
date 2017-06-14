package io.znz.jsite.visa.entity.usa;

import java.io.Serializable;

import lombok.Data;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

@Data
@Table("visa_new_pay_persion")
public class NewPayPersionEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	@Column
	@Comment("主键")
	@Id(auto = true)
	private Integer id;

	@Column
	@Comment("订单id")
	private Integer orderid;

	@Column
	@Comment("姓")
	private String xing;

	@Column
	@Comment("姓拼音")
	private String xingen;

	@Column
	@Comment("付费人名")
	private String name;

	@Column
	@Comment("名字拼音")
	private String nameen;

	@Column
	@Comment("电话")
	private String phone;

	@Column
	@Comment("邮箱")
	private String email;

	@Column
	@Comment("付费人和我的关系")
	private Integer relation;

}