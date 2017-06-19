/**
 * EmployeeEntity.java
 * io.znz.jsite.visa.entity.user
 * Copyright (c) 2017, 北京科技有限公司版权所有.
*/

package io.znz.jsite.visa.entity.user;

import java.util.Date;

import lombok.Data;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

/**
 * 员工管理
 * @author   崔建斌
 * @Date	 2017年6月12日 	 
 */
@Data
@Table("visa_employee")
public class EmployeeEntity {
	@Column
	@Id(auto = true)
	@Comment("主键")
	private Integer id;
	@Column
	@Comment("公司id")
	private Integer comId;
	@Column
	@Comment("用户姓名")
	private String fullName;
	@Column
	@Comment("用户名/手机号码")
	private String telephone;
	@Column
	@Comment("密码")
	private String password;
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
	@Comment("电子邮箱")
	private String email;
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
	@Comment("创建时间")
	private Date createTime;
	@Column
	@Comment("更新时间")
	private Date updateTime;
	@Column
	@Comment("备注")
	private String remark;
	@Column
	@Comment("预留字段1")
	private String salt;
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
