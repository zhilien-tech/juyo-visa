package io.znz.jsite.visa.entity.japan;

import java.io.Serializable;

import lombok.Data;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

@Data
@Table("visa_new_comebaby_jp")
public class NewComeBabyJpEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id(auto = true)
	private Long id;

	/**
	 * id	int	11	0	-1	0	0	0	0		0	主键				0	0
	comId	varchar	255	0	-1	0	0	0	0		0	公司id	utf8	utf8_general_ci		0	0
	comType	int	11	0	-1	0	0	0	0		0	公司类型				0	0
	comFullName	varchar	255	0	-1	0	0	0	0		0	公司全称	utf8	utf8_general_ci		0	0
	address	varchar	255	0	-1	0	0	0	0		0	地址	utf8	utf8_general_ci		0	0
	linkman	varchar	255	0	-1	0	0	0	0		0	联系人	utf8	utf8_general_ci		0	0
	phone	varchar	255	0	-1	0	0	0	0		0	手机号	utf8	utf8_general_ci		0	0
	telephone	varchar	255	0	-1	0	0	0	0		0	电话	utf8	utf8_general_ci		0	0
	Fax	varchar	255	0	-1	0	0	0	0		0	传真	utf8	utf8_general_ci		0	0
	sealUrl	varchar	255	0	-1	0	0	0	0		0	印章地址	utf8	utf8_general_ci		0	0
	sealName	varchar	255	0	-1	0	0	0	0		0	印章名字	utf8	utf8_general_ci		0	0

	 * 
	 * <p>
	 * 
	 * @author   孙斌
	 * @Date	 2017年7月25日
	 */

	@Column
	@Comment("公司id")
	private Long comId;

	@Column
	@Comment("公司类型")
	private Integer comType;

	@Column
	@Comment("公司全称")
	private String comFullName;

	@Column
	@Comment("公司地址")
	private String address;

	@Column
	@Comment("联系人")
	private String linkman;

	@Column
	@Comment("手机号")
	private String phone;

	@Column
	@Comment("电话")
	private String telephone;

	@Column
	@Comment("传真")
	private String Fax;

	@Column
	@Comment("印章地址")
	private String sealUrl;

	@Column
	@Comment("印章名字")
	private String sealName;

}
