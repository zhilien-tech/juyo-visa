/**
 * EmployeeUpdateForm.java
 * io.znz.jsite.visa.forms.employeeform
 * Copyright (c) 2017, 北京科技有限公司版权所有.
*/

package io.znz.jsite.visa.forms.employeeform;

import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.uxuexi.core.web.form.ModForm;

/**
 * 更新员工扩展类
 * @author   崔建斌
 * @Date	 2017年6月12日 	 
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class EmployeeUpdateForm extends ModForm {
	//公司id
	private Integer comId;
	//用户姓名
	private String fullName;
	//用户名/手机号码
	private String telephone;
	//密码
	private String password;
	//用户类型
	private Integer userType;
	//联系QQ
	private String qq;
	//座机号码
	private String landline;
	//电子邮箱
	private String email;
	//所属部门
	private String department;
	//用户职位
	private String job;
	//用户是否禁用
	private Integer disableUserStatus;
	//状态
	private Integer status;
	//创建时间
	private Date createTime;
	//更新时间
	private Date updateTime;
	//备注
	private String remark;
	//预留字段1
	private String salt;
	//预留字段2
	private String res2;
	//预留字段3
	private String res3;
	//预留字段4
	private String res4;
	//预留字段5
	private String res5;
}
