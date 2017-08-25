/**
 * SqlJpTotalForm.java
 * io.znz.jsite.visa.forms
 * Copyright (c) 2017, 北京科技有限公司版权所有.
*/

package io.znz.jsite.visa.forms.sqstotal;

import io.znz.jsite.visa.form.KenDoParamForm;

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
 * 日本送签社统计
 * @author   崔建斌
 * @Date	 2017年8月22日 	 
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SqlJpTotalForm extends KenDoParamForm {
	//主键
	private long id;
	//用户id/
	private Integer userid;
	//公司id
	private long comId;
	//订单号
	private String ordernumber;
	//送签社名称
	private String comFullName;
	//操作人
	private String username;
	//地接社公司名称
	private String landcomFullName;
	//受付番号
	private String completedNumber;
	//主申请人
	private String fullname;
	//人数
	private Integer headnum;
	//签证类型
	private Integer visatype;
	//公司类型(1-送签社,2-地接社)
	private Integer comtype;
	//操作人id
	private Integer operatePersonId;
	//检索条件
	private String keyword;
	//开始时间
	private Date start_time;
	//结束时间
	private Date end_time;
	//送签社
	private String sqs_id;
	//地接社
	private String djs_id;

	@Override
	public Sql sql(SqlManager paramSqlManager) {
		/**
		 * 默认使用了当前form关联entity的单表查询sql,如果是多表复杂sql，
		 * 请使用sqlManager获取自定义的sql，并设置查询条件
		 */
		String sqlString = paramSqlManager.get("sqsjptotal_list_data_new");
		Sql sql = Sqls.create(sqlString);
		sql.setCondition(cnd());
		return sql;
	}

	private Cnd cnd() {
		Cnd cnd = Cnd.NEW();
		//送签社
		if (!Util.isEmpty(sqs_id) && !sqs_id.equals("-1")) {
			cnd.and("c.id", "=", comId);
			//cnd.and("vnoj.sendComId", "=", sqs_id);
		}
		//地接社
		if (!Util.isEmpty(djs_id) && !djs_id.equals("-1")) {
			cnd.and("vnoj.landComId", "=", djs_id);
		}
		//时间
		if (!Util.isEmpty(start_time) && !Util.isEmpty(end_time)) {
			SqlExpressionGroup e1 = Cnd.exps("vnoj.createtime", ">=", start_time)
					.and("vnoj.createtime", "<=", end_time);
			SqlExpressionGroup e2 = Cnd.exps("vnoj.createtime", ">=", start_time)
					.and("vnoj.createtime", "<=", end_time);
			cnd.and(e1.or(e2));
		} else if (Util.isEmpty(start_time) && !Util.isEmpty(end_time)) {
			SqlExpressionGroup e1 = Cnd.exps("vnoj.createtime", "<=", end_time);
			SqlExpressionGroup e2 = Cnd.exps("vnoj.createtime", "<=", end_time);
			cnd.and(e1.or(e2));
		} else if (!Util.isEmpty(start_time) && Util.isEmpty(end_time)) {
			SqlExpressionGroup e1 = Cnd.exps("vnoj.createtime", ">=", start_time);
			SqlExpressionGroup e2 = Cnd.exps("vnoj.createtime", ">=", start_time);
			cnd.and(e1.or(e2));
		}
		if (!Util.isEmpty(keyword)) {
			cnd.and("vnoj.ordernumber", "like", "%" + keyword + "%").or("eee.fullName", "like", "%" + keyword + "%")
					.or("vnoj.completedNumber", "like", "%" + keyword + "%")
					.or("mm.chinesefullname", "like", "%" + keyword + "%");
		}
		cnd.and("vnoj.comId", "=", comId);
		cnd.orderBy("vnoj.createTime", "DESC");
		return cnd;
	}
}
