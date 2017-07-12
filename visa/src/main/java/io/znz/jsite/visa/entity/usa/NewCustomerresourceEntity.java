package io.znz.jsite.visa.entity.usa;

import java.io.Serializable;

import lombok.Data;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

@Data
@Table("visa_new_customersource")
public class NewCustomerresourceEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	/**
	 * id	int	11	0	0	-1	0	0	0		0	主键				-1	0
	order_jp_id	int	11	0	-1	0	0	0	0		0	订单id				0	0
	fullComName	varchar	255	0	-1	0	0	0	0		0	公司全称	utf8	utf8_general_ci		0	0
	linkman	varchar	255	0	-1	0	0	0	0		0	联系人	utf8	utf8_general_ci		0	0
	telephone	varchar	255	0	-1	0	0	0	0		0	电话	utf8	utf8_general_ci		0	0
	email	varchar	255	0	-1	0	0	0	0		0	邮箱	utf8	utf8_general_ci		0	0

	 */
	@Id(auto = true)
	private Integer id;

	@Column
	@Comment("订单id")
	private Long order_id;

	@Column
	@Comment("公司全称")
	private String fullComName;
	@Column
	@Comment("联系人")
	private String linkman;
	@Column
	@Comment("电话")
	private String telephone;
	@Column
	@Comment("邮箱")
	private String email;

}