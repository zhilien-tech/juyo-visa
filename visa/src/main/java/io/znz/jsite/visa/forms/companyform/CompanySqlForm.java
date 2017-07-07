/**
 * CustomerSqlForm.java
 * io.znz.jsite.visa.forms
 * Copyright (c) 2017, 北京科技有限公司版权所有.
*/

package io.znz.jsite.visa.forms.companyform;

import io.znz.jsite.visa.enums.UserDeleteStatusEnum;
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
public class CompanySqlForm extends KenDoParamForm {
	//主键
	private long id;
	//管理员账号
	private long adminId;
	//管理员姓名
	private String adminName;
	//公司名称
	private String comName;
	//公司类型
	private Integer comType;
	//备注
	private String remark;
	//联系人
	private String connect;
	//联系人手机号
	private String mobile;
	//邮箱
	private String email;
	//座机
	private String landLine;
	//地址
	private String address;
	//营业执照
	private String license;
	//操作人
	private long empId;
	//创建时间
	private Date createTime;
	//修改时间
	private Date updateTime;
	//删除标识
	private Integer deletestatus;

	private String keyword;

	@Override
	public Sql sql(SqlManager paramSqlManager) {
		/**
		 * 默认使用了当前form关联entity的单表查询sql,如果是多表复杂sql，
		 * 请使用sqlManager获取自定义的sql，并设置查询条件
		 */
		String sqlString = paramSqlManager.get("company_list");
		Sql sql = Sqls.create(sqlString);
		sql.setCondition(cnd());
		return sql;
	}

	private Cnd cnd() {
		Cnd cnd = Cnd.NEW();
		if (!Util.isEmpty(keyword)) {
			cnd.and("vc.comName", "like", "%" + keyword + "%").or("vc.connect", "like", "%" + keyword + "%")
					.or("vc.mobile", "like", "%" + keyword + "%");
		}
		cnd.and("vc.deletestatus", "=", UserDeleteStatusEnum.NO.intKey());
		cnd.orderBy("vc.createTime", "DESC");
		return cnd;
	}
}
