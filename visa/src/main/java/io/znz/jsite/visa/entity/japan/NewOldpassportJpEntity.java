package io.znz.jsite.visa.entity.japan;

import java.io.Serializable;

import lombok.Data;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

@Data
@Table("visa_new_oldpassport_jp")
public class NewOldpassportJpEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id(auto = true)
	private Integer id;

	@Column
	@Comment("签发国家")
	private String sendcountry;

	@Column
	@Comment("护照号")
	private String passport;

	@Column
	@Comment("原因")
	private String reason;

	@Column
	@Comment("原因英文")
	private String reasonen;

	@Column
	@Comment("客户管理id")
	private Long customer_jp_id;

}