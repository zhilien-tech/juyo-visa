/**
 * CustomerSqlForm.java
 * io.znz.jsite.visa.forms
 * Copyright (c) 2017, 北京科技有限公司版权所有.
*/

package io.znz.jsite.visa.forms.authority;

import io.znz.jsite.visa.form.KenDoParamForm;
import lombok.Data;
import lombok.EqualsAndHashCode;

import org.nutz.dao.Cnd;
import org.nutz.dao.SqlManager;
import org.nutz.dao.Sqls;
import org.nutz.dao.sql.Sql;

/**
 * 权限管理sqlForm
 * @author   崔建斌
 * @Date	 2017年6月9日 	 
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AuthoritySqlForm extends KenDoParamForm {
	//主键
	private long id;
	//公司id
	private long comId;
	//部门id
	private long deptId;
	//部门名称
	private String deptName;
	//职位id
	private long jobId;
	//职位名称
	private String jobName;

	private String keyword;

	@Override
	public Sql sql(SqlManager paramSqlManager) {
		/**
		 * 默认使用了当前form关联entity的单表查询sql,如果是多表复杂sql，
		 * 请使用sqlManager获取自定义的sql，并设置查询条件
		 */
		String sqlString = paramSqlManager.get("authority_list");
		Sql sql = Sqls.create(sqlString);
		sql.setCondition(cnd());
		return sql;
	}

	private Cnd cnd() {
		Cnd cnd = Cnd.NEW();
		cnd.and("c.id", "=", comId);
		cnd.and("f.parentId", "=", 0);
		cnd.groupBy("d.id");
		cnd.orderBy("d.createTime", "DESC");
		return cnd;
	}
}
