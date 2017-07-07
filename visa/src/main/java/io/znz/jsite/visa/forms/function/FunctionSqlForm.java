/**
 * CustomerSqlForm.java
 * io.znz.jsite.visa.forms
 * Copyright (c) 2017, 北京科技有限公司版权所有.
*/

package io.znz.jsite.visa.forms.function;

import io.znz.jsite.visa.form.KenDoParamForm;

import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.nutz.dao.Cnd;
import org.nutz.dao.SqlManager;
import org.nutz.dao.Sqls;
import org.nutz.dao.sql.Sql;

import com.uxuexi.core.common.util.Util;

/**
 * 客户管理sqlForm
 * @author   崔建斌
 * @Date	 2017年6月9日 	 
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class FunctionSqlForm extends KenDoParamForm {
	//主键
	private Integer id;
	//上级功能
	private Integer parentId;
	//功能名称
	private String funName;
	//访问地止
	private String url;
	//功能等级
	private Integer level;
	//创建时间
	private Date createTime;
	//更新时间
	private Date updateTime;
	//备注
	private String remark;
	//序号
	private Integer sort;
	//菜单栏图标样式
	private String portrait;
	//检索条件
	private String keyword;

	@Override
	public Sql sql(SqlManager paramSqlManager) {
		/**
		 * 默认使用了当前form关联entity的单表查询sql,如果是多表复杂sql，
		 * 请使用sqlManager获取自定义的sql，并设置查询条件
		 */
		String sqlString = paramSqlManager.get("function_list");
		Sql sql = Sqls.create(sqlString);
		sql.setCondition(cnd());
		return sql;
	}

	private Cnd cnd() {
		Cnd cnd = Cnd.NEW();
		if (!Util.isEmpty(keyword)) {
			cnd.and("f.funName", "like", "%" + keyword + "%");
		}
		cnd.orderBy("f.createTime", "DESC");
		return cnd;
	}
}
