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

import com.uxuexi.core.common.util.DateUtil;
import com.uxuexi.core.common.util.Util;

/**
 * TODO(这里用一句话描述这个类的作用)
 * @author   崔建斌
 * @Date	 2016年11月24日 	 
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class NewOrderJapanSqlForm extends KenDoParamForm {

	//字典信息
	private Date start_time;
	private Date end_time;
	private String keywords;
	private Integer state;
	private long comId;
	private int userType;

	@Override
	public Sql sql(SqlManager paramSqlManager) {
		/**
		 * 默认使用了当前form关联entity的单表查询sql,如果是多表复杂sql，
		 * 请使用sqlManager获取自定义的sql，并设置查询条件
		 */
		if (!Util.isEmpty(end_time)) {
			if (end_time.getHours() == 23) {
				end_time = DateUtil.addDay(end_time, 1);
			}
			end_time.setHours(23);
			end_time.setMinutes(59);
			end_time.setSeconds(59);
		}
		if (!Util.isEmpty(start_time)) {
			if (start_time.getHours() == 23) {
				start_time = DateUtil.addDay(start_time, 1);
				start_time.setHours(0);
			}
		}
		String sqlString = paramSqlManager.get("neworderjapan_list");
		Sql sql = Sqls.create(sqlString);
		sql.setCondition(cnd());
		return sql;
	}

	private Cnd cnd() {
		Cnd cnd = Cnd.NEW();
		if (!Util.isEmpty(start_time) && !Util.isEmpty(end_time)) {
			SqlExpressionGroup e1 = Cnd.exps("vncoj.createtime", ">=", start_time).and("vncoj.createtime", "<=",
					end_time);
			cnd.and(e1);
		} else if (Util.isEmpty(start_time) && !Util.isEmpty(end_time)) {
			SqlExpressionGroup e1 = Cnd.exps("vncoj.createtime", "<=", end_time);
			cnd.and(e1);
		} else if (!Util.isEmpty(start_time) && Util.isEmpty(end_time)) {
			SqlExpressionGroup e1 = Cnd.exps("vncoj.createtime", ">=", start_time);
			cnd.and(e1);
		}
		if (!Util.isEmpty(keywords)) {
			SqlExpressionGroup e1 = Cnd.exps("vnoj.ordernumber", "like", "%" + keywords + "%");
			SqlExpressionGroup e2 = Cnd.exps("vncj.chinesefullname", "like", "%" + keywords + "%");
			SqlExpressionGroup e3 = Cnd.exps("vcm.telephone", "like", "%" + keywords + "%");
			SqlExpressionGroup e4 = Cnd.exps("vncj.email", "like", "%" + keywords + "%");
			SqlExpressionGroup e5 = Cnd.exps("vcm.linkman", "like", "%" + keywords + "%");
			SqlExpressionGroup e6 = Cnd.exps("vncj.phone", "like", "%" + keywords + "%");
			//			cnd.and(e1).or(e2).or(e3).or(e4).or(e5).or(e6);
			cnd.and(e1.or(e2).or(e3).or(e4).or(e5).or(e6));
		}
		if (!Util.isEmpty(state) && state > 0) {
			SqlExpressionGroup e1 = Cnd.exps("vnoj.status", "=", state);
			SqlExpressionGroup e2 = Cnd.exps("vncj.status", "=", state);
			//			cnd.and(e1).or(e2);
			cnd.and(e1.or(e2));
		}

		if (!Util.isEmpty(comId) && comId > 0) {
			cnd.and("vnoj.comId", "=", comId);
		} else {
			cnd.and("vnoj.comId", "=", -1);

		}

		SqlExpressionGroup e2 = Cnd.exps("vnoj.island", "<>", 1);
		SqlExpressionGroup e1 = Cnd.exps("vnoj.island", "is", null);
		cnd.and(e2.or(e1));
		cnd.orderBy("vnoj.updatetime", "desc");
		cnd.groupBy("vnoj.id");
		return cnd;
	}
}
