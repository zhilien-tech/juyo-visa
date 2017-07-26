/**
 * ComeBabySqlForm.java
 * io.znz.jsite.visa.forms.comebaby
 * Copyright (c) 2017, 北京科技有限公司版权所有.
*/

package io.znz.jsite.visa.forms.comebaby;

import io.znz.jsite.visa.form.KenDoParamForm;
import lombok.Data;
import lombok.EqualsAndHashCode;

import org.nutz.dao.Cnd;
import org.nutz.dao.SqlManager;
import org.nutz.dao.Sqls;
import org.nutz.dao.sql.Sql;

import com.uxuexi.core.common.util.Util;

/**
 * TODO(这里用一句话描述这个类的作用)
 * <p>
 * TODO(这里描述这个类补充说明 – 可选)
 *
 * @author   孙斌
 * @Date	 2017年7月25日 	 
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ComeBabySqlForm extends KenDoParamForm {
	private long comId;

	@Override
	public Sql sql(SqlManager paramSqlManager) {
		/**
		 * 默认使用了当前form关联entity的单表查询sql,如果是多表复杂sql，
		 * 请使用sqlManager获取自定义的sql，并设置查询条件
		 */
		String sqlString = paramSqlManager.get("comebaby_list");
		Sql sql = Sqls.create(sqlString);
		sql.setCondition(cnd());
		return sql;
	}

	private Cnd cnd() {
		Cnd cnd = Cnd.NEW();
		if (!Util.isEmpty(comId) && comId > 0) {
			cnd.and("comId", "=", comId);
		}

		return cnd;
	}
}
