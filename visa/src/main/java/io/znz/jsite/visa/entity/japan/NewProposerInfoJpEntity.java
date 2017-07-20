package io.znz.jsite.visa.entity.japan;

import java.io.Serializable;

import lombok.Data;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

@Data
@Table("visa_new_proposer_info_jp")
public class NewProposerInfoJpEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id(auto = true)
	private Integer id;
	/***
	 * id	int	11	0	0	0	0	0	0	0	0	主键				-1	0
	customer_jp_id	int	11	0	-1	0	0	0	0		0	客户id				0	0
	xing	varchar	255	0	-1	0	0	0	0		0	姓	utf8	utf8_general_ci		0	0
	name	varchar	255	0	-1	0	0	0	0		0	名	utf8	utf8_general_ci		0	0
	istogetherlinkman	tinyint	1	0	-1	0	0	0	0		0	统一联系人				0	0
	ismainproposer	tinyint	1	0	-1	0	0	0	0		0	主申请人				0	0
	relationproposer	int	11	0	-1	0	0	0	0		0	关联的主申请人				0	0
	relation	int	11	0	-1	0	0	0	0		0	与主申请人的关系				0	0
	order_jp_id	int	11	0	-1	0	0	0	0		0					0	0
	fullname	varchar	255	0	-1	0	0	0	0		0		utf8	utf8_general_ci		0	0

	 */
	@Column
	@Comment("客户管理id")
	private Long customer_jp_id;
	@Column
	@Comment("订单id")
	private Long order_jp_id;

	@Column
	@Comment("统一联系人")
	private boolean istogetherlinkman;
	@Column
	@Comment("主申请人")
	private boolean ismainproposer;
	@Column
	@Comment("关联的主申请人")
	private Integer relationproposer;
	@Column
	@Comment("与主申请人之间的关系")
	private Integer relation;

	@Column
	@Comment("姓")
	private String xing;
	@Column
	@Comment("名")
	private String name;
	@Column
	@Comment("姓名")
	private String fullname;

	public boolean getIsMainProposer() {
		return this.ismainproposer;
	}

}