package io.znz.jsite.visa.entity.japan;

import io.znz.jsite.visa.zidingyienum.GetComment;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

@Data
@Table("visa_new_comebaby_jp_djs")
public class NewComeBabyJpDjsEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id(auto = true)
	private Long id;

	@Column
	@Comment("公司id")
	@GetComment(name = "公司id")
	private Long comId;

	@Column
	@Comment("公司类型")
	private Integer comType;

	@Column
	@Comment("公司全称")
	@GetComment(name = "公司全称")
	private String comFullName;

	@Column
	@Comment("公司地址")
	@GetComment(name = "公司地址")
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

	@Column
	@Comment("送签社印章地址")
	private String sealSQUrl;

	@Column
	@Comment("送签社印章名字")
	private String sealSQName;

	@Column
	@Comment("受付番号")
	private String completedNumber;

	@Column
	@Comment("地接社公司全称")
	private String landcomFullName;

	@Column
	@Comment("地接社公司地址")
	private String landaddress;

	@Column
	@Comment("地接社联系人")
	private String landlinkman;

	@Column
	@Comment("地接社电话")
	private String landtelephone;
	@Column
	@Comment("创建时间")
	private Date createTime;

	@Column
	@Comment("更新时间")
	private Date updateTime;

}
