/**
 * SysUserSqlForm.java
 * io.znz.jsite.visa.form
 * Copyright (c) 2017, 北京科技有限公司版权所有.
*/

package io.znz.jsite.visa.form;

import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.nutz.dao.Cnd;
import org.nutz.dao.SqlManager;
import org.nutz.dao.Sqls;
import org.nutz.dao.sql.Sql;

/**
 * TODO(这里用一句话描述这个类的作用)
 * <p>
 * TODO(这里描述这个类补充说明 – 可选)
 *
 * @author   孙斌
 * @Date	 2017年6月9日 	 
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysUserSqlForm extends KenDoParamForm {
	private String id;
	private String avatar;
	private Date birthday;
	private Date create_date;
	private String del_flag;
	private String description;
	private String email;
	private Integer gender;
	private Date last_visit;
	private Integer login_count;
	private String login_name;
	private String name;
	private String password;
	private String phone;
	private Date previous_visit;
	private String salt;
	private String state;
	private String city;
	private String brief;
	private String extra;

	@Override
	public Sql sql(SqlManager paramSqlManager) {
		/**
		 * 默认使用了当前form关联entity的单表查询sql,如果是多表复杂sql，
		 * 请使用sqlManager获取自定义的sql，并设置查询条件
		 */
		String sqlString = paramSqlManager.get("sysuser_list");
		Sql sql = Sqls.create(sqlString);
		sql.setCondition(cnd());
		return sql;
	}

	private Cnd cnd() {
		Cnd cnd = Cnd.NEW();
		//TODO 添加自定义查询条件（可选）

		return cnd;
	}
}
