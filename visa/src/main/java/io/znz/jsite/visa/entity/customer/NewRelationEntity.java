package io.znz.jsite.visa.entity.customer;

import java.io.Serializable;

import lombok.Data;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

@Data
@Table("visa_new_relation")
public class NewRelationEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id(auto = true)
	private Integer id;

	@Column
	@Comment("客户id")
	private Integer customerid;

	@Column
	@Comment("非直系亲属")
	private boolean indirect;

	@Column
	@Comment("直系亲属")
	private Integer direct;

	@Column
	@Comment("姓")
	private String xing;

	@Column
	@Comment("姓英文")
	private String xingen;

	@Column
	@Comment("名字")
	private String name;

	@Column
	@Comment("名字英文")
	private String nameen;

	@Column
	@Comment("和我的关系")
	private Integer relationme;

	@Column
	@Comment("在美身份")
	private Integer usaidentify;

	@Column
	@Comment("在美地址")
	private String usaaddress;

	@Column
	@Comment("在美电话")
	private String usaphone;

}