package io.znz.jsite.visa.entity.japan;

import java.io.Serializable;

import lombok.Data;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

@Data
@Table("visa_new_workinfo_jp")
public class NewWorkinfoJpEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id(auto = true)
	private Integer id;

	@Column
	@Comment("客户id")
	private Long customer_jp_id;

	@Column
	@Comment("我的职业")
	private String myjob;

	@Column
	@Comment("单位名称")
	private String unitname;

	@Column
	@Comment("单位电话")
	private String unitphone;

	@Column
	@Comment("单位地址")
	private String unitaddress;

}