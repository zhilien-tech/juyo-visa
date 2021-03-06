/**
 * DictInfoSqlForm.java
 * com.linyun.airline.forms
 * Copyright (c) 2016, 北京科技有限公司版权所有.
*/

package io.znz.jsite.visa.form;

import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.nutz.dao.Cnd;
import org.nutz.dao.SqlManager;
import org.nutz.dao.Sqls;
import org.nutz.dao.sql.Sql;
import org.nutz.dao.util.cri.SqlExpressionGroup;

import com.uxuexi.core.common.util.Util;

/**
 * TODO(这里用一句话描述这个类的作用)
 * @author   崔建斌
 * @Date	 2016年11月24日 	 
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class KenDoTestSqlForm extends KenDoParamForm {

	//字典信息
	private Date start_time;
	private Date end_time;
	private String keywords;
	private Integer state;
	private long comId;

	@Override
	public Sql sql(SqlManager paramSqlManager) {
		/**
		 * 默认使用了当前form关联entity的单表查询sql,如果是多表复杂sql，
		 * 请使用sqlManager获取自定义的sql，并设置查询条件
		 */
		String sqlString = paramSqlManager.get("template_select_test");
		Sql sql = Sqls.create(sqlString);
		sql.setCondition(cnd());
		return sql;
	}

	private Cnd cnd() {
		Cnd cnd = Cnd.NEW();
		if (!Util.isEmpty(start_time) && !Util.isEmpty(end_time)) {
			SqlExpressionGroup e1 = Cnd.exps("vno.sendtime", ">=", start_time).and("vno.sendtime", "<=", end_time);
			SqlExpressionGroup e2 = Cnd.exps("vno.outtime", ">=", start_time).and("vno.outtime", "<=", end_time);
			//cnd.and(e1).or(e2);
			cnd.and(e1.or(e2));
		} else if (Util.isEmpty(start_time) && !Util.isEmpty(end_time)) {
			SqlExpressionGroup e1 = Cnd.exps("vno.sendtime", "<=", end_time);
			SqlExpressionGroup e2 = Cnd.exps("vno.outtime", "<=", end_time);
			//cnd.and(e1).or(e2);
			cnd.and(e1.or(e2));
		} else if (!Util.isEmpty(start_time) && Util.isEmpty(end_time)) {
			SqlExpressionGroup e1 = Cnd.exps("vno.sendtime", ">=", start_time);
			SqlExpressionGroup e2 = Cnd.exps("vno.outtime", ">=", start_time);
			//cnd.and(e1).or(e2);
			cnd.and(e1.or(e2));
		}
		if (!Util.isEmpty(keywords)) {
			SqlExpressionGroup e1 = Cnd.exps("vno.ordernumber", "like", "%" + keywords + "%");
			SqlExpressionGroup e2 = Cnd.exps("vnc.chinesefullname", "like", "%" + keywords + "%");
			SqlExpressionGroup e3 = Cnd.exps("vcm.telephone", "like", "%" + keywords + "%");
			SqlExpressionGroup e4 = Cnd.exps("vcm.email", "like", "%" + keywords + "%");
			SqlExpressionGroup e5 = Cnd.exps("vcm.linkman", "like", "%" + keywords + "%");
			SqlExpressionGroup e6 = Cnd.exps("vnc.phone", "like", "%" + keywords + "%");
			//SqlExpressionGroup e6 = Cnd.exps("vo.id", "like", keywords);
			//cnd.and(e1).or(e2).or(e3).or(e4).or(e5).or(e6);
			cnd.and(e1.or(e2).or(e3).or(e4).or(e5).or(e6));
		}
		if (!Util.isEmpty(state) && state > 0) {
			SqlExpressionGroup e1 = Cnd.exps("vno.status", "=", state);
			SqlExpressionGroup e2 = Cnd.exps("vnc.status", "=", state);
			//cnd.and(e1).or(e2);
			cnd.and(e1.or(e2));
		}
		if (!Util.isEmpty(comId) && comId > 0) {
			cnd.and("comId", "=", comId);
		} else {
			cnd.and("comId", "=", -1);

		}

		cnd.orderBy("vno.updatetime", "desc");
		cnd.groupBy("vno.id");
		return cnd;
	}
}
