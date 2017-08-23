/**
 * SqlJpTotalForm.java
 * io.znz.jsite.visa.forms
 * Copyright (c) 2017, 北京科技有限公司版权所有.
*/

package io.znz.jsite.visa.forms;

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

	@Override
	public Sql sql(SqlManager paramSqlManager) {
		/**
		 * 默认使用了当前form关联entity的单表查询sql,如果是多表复杂sql，
		 * 请使用sqlManager获取自定义的sql，并设置查询条件
		 */
		String sqlString = paramSqlManager.get("sqsjptotal_list_data");
		Sql sql = Sqls.create(sqlString);
		sql.setCondition(cnd());
		return sql;
	}

	private Cnd cnd() {
		Cnd cnd = Cnd.NEW();
		if (!Util.isEmpty(keyword)) {
			cnd.and("vnoj.ordernumber", "like", "%" + keyword + "%").or("eee.fullName", "like", "%" + keyword + "%")
					.or("vnoj.completedNumber", "like", "%" + keyword + "%")
					.or("mainporposer", "like", "%" + keyword + "%");
		}
		cnd.and("vnoj.comId", "=", comId);
		//cnd.orderBy("vc.createTime", "DESC");
		return cnd;
	}
}
