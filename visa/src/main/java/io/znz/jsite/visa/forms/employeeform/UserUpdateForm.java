/**
 * UserUpdateForm.java
 * io.znz.jsite.visa.forms.employeeform
 * Copyright (c) 2017, 北京科技有限公司版权所有.
*/

package io.znz.jsite.visa.forms.employeeform;

import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.uxuexi.core.web.form.ModForm;

/**
 * 员工管理更新扩展类
 * @author   崔建斌
 * @Date	 2017年6月11日 	 
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UserUpdateForm extends ModForm {

	private String avatar;
	//出生日期
	private Date birthday;
	//创建时间
	private Date create_date;
	private String del_flag;
	private String description;
	private String email;
	private Integer gender;
	private Date last_visit;
	private Integer login_count;
	//登录名
	private String login_name;
	//用户姓名
	private String name;
	//密码
	private String password;
	//联系电话
	private String phone;
	private Date previous_visit;
	private String salt;
	private String state;
	private String city;
	private String brief;
	private String extra;
	//公司id
	private Integer comId;
	//用户类型
	private Integer userType;
	//联系QQ
	private String qq;
	//座机号码
	private String landline;
	//所属部门
	private String department;
	//用户职位
	private String job;
	//用户是否禁用
	private Integer disableUserStatus;
	//状态
	private Integer status;
	//修改时间
	private Date updateTime;
	//预留字段1
	private String res1;
	//预留字段2
	private String res2;
	//预留字段3
	private String res3;
	//预留字段4
	private String res4;
	//预留字段5
	private String res5;
}
