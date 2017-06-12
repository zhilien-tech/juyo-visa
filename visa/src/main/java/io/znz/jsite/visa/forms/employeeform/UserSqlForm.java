/**
 * UserSqlForm.java
 * io.znz.jsite.visa.forms.employeeform
 * Copyright (c) 2017, 北京科技有限公司版权所有.
*/

package io.znz.jsite.visa.forms.employeeform;

import io.znz.jsite.visa.form.KenDoParamForm;

import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.nutz.dao.Cnd;
import org.nutz.dao.SqlManager;
import org.nutz.dao.Sqls;
import org.nutz.dao.sql.Sql;

/**
 * 员工管理sqlForm
 * @author   崔建斌
 * @Date	 2017年6月11日 	 
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UserSqlForm extends KenDoParamForm {

	//主键
	private String id;
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

	private String keyword;

	@Override
	public Sql sql(SqlManager paramSqlManager) {
		/**
		 * 默认使用了当前form关联entity的单表查询sql,如果是多表复杂sql，
		 * 请使用sqlManager获取自定义的sql，并设置查询条件
		 */
		String sqlString = paramSqlManager.get("employee_list_data");
		Sql sql = Sqls.create(sqlString);
		sql.setCondition(cnd());
		return sql;
	}

	private Cnd cnd() {
		Cnd cnd = Cnd.NEW();
		cnd.orderBy("u.create_date", "DESC");
		return cnd;
	}
}
