/**
 * SysUserEntity.java
 * io.znz.jsite.visa.bean.entity
 * Copyright (c) 2017, 北京科技有限公司版权所有.
*/

package io.znz.jsite.visa.entity.user;

import java.util.Date;

import lombok.Data;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Table;

/**
 * 用户实体
 * @author   崔建斌
 * @Date	 2017年6月7日 	 
 */
@Data
@Table("sys_user")
public class SysUserEntity {

	@Column
	@Comment("主键")
	private String id;
	@Column
	private String avatar;
	@Column
	private Date birthday;
	@Column
	private Date create_date;
	@Column
	private String del_flag;
	@Column
	private String description;
	@Column
	private String email;
	@Column
	private Integer gender;
	@Column
	private Date last_visit;
	@Column
	private Integer login_count;
	@Column
	private String login_name;
	@Column
	@Comment("用户姓名")
	private String name;
	@Column
	@Comment("密码")
	private String password;
	@Column
	@Comment("联系电话")
	private String phone;
	@Column
	private Date previous_visit;
	@Column
	private String salt;
	@Column
	private String state;
	@Column
	private String city;
	@Column
	private String brief;
	@Column
	private String extra;

	@Column
	@Comment("公司id")
	private Integer comId;
	@Column
	@Comment("用户类型")
	private Integer userType;
	@Column
	@Comment("联系QQ")
	private String qq;
	@Column
	@Comment("座机号码")
	private String landline;
	@Column
	@Comment("所属部门")
	private String department;
	@Column
	@Comment("用户职位")
	private String job;
	@Column
	@Comment("用户是否禁用")
	private Integer disableUserStatus;
	@Column
	@Comment("状态")
	private Integer status;
	@Column
	@Comment("修改时间")
	private Date updateTime;
	@Column
	@Comment("预留字段1")
	private String res1;
	@Column
	@Comment("预留字段2")
	private String res2;
	@Column
	@Comment("预留字段3")
	private String res3;
	@Column
	@Comment("预留字段4")
	private String res4;
	@Column
	@Comment("预留字段5")
	private String res5;
}
