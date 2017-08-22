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

	//领区
	private Integer area;
	//是否加急
	private Integer ishurry;
	//行程
	private Integer triptype;
	//收款方式
	private Integer paytype;
	//金额
	private Double money;
	//送签时间
	private Date senddate;
	//出签时间
	private Date outdate;
	//备注
	private String remark;
	//国家类型
	private Integer countrytype;
	//状态
	private Integer status;
	//更新时间
	private Date updatetime;
	//创建时间
	private Date createtime;
	//分享次数
	private Integer sharenum;
	//递送次数
	private Integer sendnum;
	//客户来源
	private Integer customerSource;
	//客户来源
	private long sendComId;
	//操作人id
	private Integer operatePersonId;
	//判断是否为日本地接社添加的
	private Integer island;
	//错误码
	private Integer errorCode;
	//错误信息
	private String errorMsg;

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
		cnd.and("ojp.comId", "=", comId);
		/*if (!Util.isEmpty(keyword)) {
			cnd.and("vc.comName", "like", "%" + keyword + "%").or("vc.connect", "like", "%" + keyword + "%")
					.or("vc.mobile", "like", "%" + keyword + "%");
		}
		cnd.and("vc.deletestatus", "=", UserDeleteStatusEnum.NO.intKey());
		cnd.orderBy("vc.createTime", "DESC");*/
		return cnd;
	}
}
