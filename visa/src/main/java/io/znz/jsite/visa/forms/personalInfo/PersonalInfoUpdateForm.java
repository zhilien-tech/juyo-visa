/**
 * EmployeeUpdateForm.java
 * io.znz.jsite.visa.forms.employeeform
 * Copyright (c) 2017, 北京科技有限公司版权所有.
*/

package io.znz.jsite.visa.forms.personalInfo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.uxuexi.core.web.form.ModForm;

/**
 * 个人信息 扩展类
 * @author   彭辉
 * @Date	 2017年8月9日 	 
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PersonalInfoUpdateForm extends ModForm {
	//联系QQ")
	private String qq;
	//"座机号码")
	private String landline;
	//"电子邮箱")
	private String email;
	//公司id
	private Integer comId;
	//用户姓名
	private String fullName;
	//用户名/手机号码")
	private String telephone;
	//"所属部门")
	private String deptname;
	//"用户职位")
	private String jobname;

}
