package io.znz.jsite.visa.entity.customer;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

@Data
@Table("visa_new_army")
public class NewArmyEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id(auto = true)
	private Integer id;

	@Column
	@Comment("开始时间")
	private Date startdate;

	@Column
	@Comment("结束时间")
	private Date enddate;

	@Column
	@Comment("国家")
	private String country;

	@Column
	@Comment("军种")
	private Integer armytype;

	@Column
	@Comment("客户表的id")
	private Integer customerid;

	@Column
	@Comment("军衔")
	private String armyname;

	@Column
	@Comment("军事特长")
	private String armydo;

}