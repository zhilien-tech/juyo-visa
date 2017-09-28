/**
 * DictInfoSqlForm.java
 * com.linyun.airline.forms
 * Copyright (c) 2016, 北京科技有限公司版权所有.
*/

package io.znz.jsite.visa.form;

import io.znz.jsite.core.enums.UserLoginEnum;

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
public class NewOrderJapanLandSqlForm extends KenDoParamForm {

	//字典信息
	private Date start_time;
	private Date end_time;
	private String keywords;
	private Integer state;
	private long comId;
	private long customerid;
	private int userType;
	private String comIdList;
	private String orderIds;

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

		String sqlString = paramSqlManager.get("newcustomerjapan_landlist_new");
		Sql sql = Sqls.create(sqlString);

		sql.setCondition(cnd());
		String string = sql.toString();
		if (!Util.isEmpty(keywords)) {
			String replaceAll = string.replaceAll("@aaa", this.keywords);
			Sql sql2 = Sqls.create(replaceAll);

			return sql2;
		}
		/*sql.setParam("ordernumber", keywords);
		sql.setParam("aaa", keywords);*/
		return sql;

	}

	private Cnd cnd() {
		Cnd cnd = Cnd.NEW();
		if (!Util.isEmpty(start_time) && !Util.isEmpty(end_time)) {
			SqlExpressionGroup e1 = Cnd.exps("vnoj.createtime", ">=", start_time)
					.and("vnoj.createtime", "<=", end_time);
			cnd.and(e1);
		} else if (Util.isEmpty(start_time) && !Util.isEmpty(end_time)) {
			SqlExpressionGroup e1 = Cnd.exps("vnoj.createtime", "<=", end_time);
			cnd.and(e1);
		} else if (!Util.isEmpty(start_time) && Util.isEmpty(end_time)) {
			SqlExpressionGroup e1 = Cnd.exps("vnoj.createtime", ">=", start_time);
			cnd.and(e1);
		}
		if (!Util.isEmpty(keywords)) {
			if (keywords.contains("@")) {
				keywords = keywords.replace("@", "@");
				/*SqlExpressionGroup e1 = Cnd.exps("vnoj.ordernumber", "like", "%" + keywords + "% escape /");
				SqlExpressionGroup e2 = null;
				if (!Util.isEmpty(this.orderIds)) {

					e2 = Cnd.exps("vnoj.id", "in", this.orderIds);
				}
							SqlExpressionGroup e2 = Cnd.exps("iii.chinesefullname", "like", "%" + keywords + "%");
				 SqlExpressionGroup e3 = Cnd.exps("qqq.telephone", "like", "%" + keywords + "% escape /");
				SqlExpressionGroup e4 = Cnd.exps("qqq.email", "like", "%" + keywords + "% escape /");
				SqlExpressionGroup e5 = Cnd.exps("qqq.linkman", "like", "%" + keywords + "% escape /");
				SqlExpressionGroup e6 = Cnd.exps("iii.phone", "like", "%" + keywords + "%");
				SqlExpressionGroup e7 = Cnd.exps("iii.familyphone", "like", "%" + keywords + "%");
				SqlExpressionGroup e8 = Cnd.exps("iii.email", "like", "%" + keywords + "%");
				if (!Util.isEmpty(e2)) {
					cnd.and(e1.or(e2).or(e3).or(e4).or(e5).or(e2));

				} else {

					cnd.and(e1.or(e2).or(e3).or(e4).or(e5));
				}*/
			} else {

			}
			SqlExpressionGroup e1 = Cnd.exps("vnoj.ordernumber", "like", "%" + "@aaa" + "%");
			SqlExpressionGroup e2 = null;
			if (!Util.isEmpty(this.orderIds)) {

				e2 = Cnd.exps("vnoj.id", "in", this.orderIds);
			}
			/*			SqlExpressionGroup e2 = Cnd.exps("iii.chinesefullname", "like", "%" + keywords + "%");
			 */SqlExpressionGroup e3 = Cnd.exps("qqq.telephone", "like", "%" + "@aaa" + "%");
			SqlExpressionGroup e4 = Cnd.exps("qqq.email", "like", "%" + "@aaa" + "%");
			SqlExpressionGroup e5 = Cnd.exps("qqq.linkman", "like", "%" + "@aaa" + "%");
			/*SqlExpressionGroup e6 = Cnd.exps("iii.phone", "like", "%" + keywords + "%");
			SqlExpressionGroup e7 = Cnd.exps("iii.familyphone", "like", "%" + keywords + "%");
			SqlExpressionGroup e8 = Cnd.exps("iii.email", "like", "%" + keywords + "%");*/
			if (!Util.isEmpty(e2)) {
				cnd.and(e1.or(e2).or(e3).or(e4).or(e5).or(e2));

			} else {

				cnd.and(e1.or(e2).or(e3).or(e4).or(e5));
			}
			//			cnd.and(e1);
		}
		if (!Util.isEmpty(state) && state > 0) {
			SqlExpressionGroup e1 = Cnd.exps("vnoj.status", "=", state);
			/*SqlExpressionGroup e2 = Cnd.exps("vncj.status", "=", state);*/
			//			cnd.and(e1).or(e2);
			//			cnd.and(e1.or(e2));
			cnd.and(e1);
		}
		if (UserLoginEnum.JP_DJS_ADMIN.intKey() == userType) {

			cnd.and("vnoj.comId", "in", comIdList);
		} else {

			if (!Util.isEmpty(comId) && comId > 0) {
				cnd.and("vnoj.comId", "=", comId);
			}
		}
		if (!Util.isEmpty(customerid) && customerid > 0) {
			cnd.and("vncj.id", "=", customerid);
		}
		SqlExpressionGroup e1 = Cnd.exps("vnoj.status", ">", 17);
		SqlExpressionGroup e3 = Cnd.exps("vnoj.status", "=", 8);
		SqlExpressionGroup e2 = Cnd.exps("vnoj.island", "=", 1);
		cnd.and(e1.or(e2).or(e3));

		cnd.orderBy("vnoj.updatetime", "desc");
		cnd.groupBy("vnoj.id");
		return cnd;
	}
}
