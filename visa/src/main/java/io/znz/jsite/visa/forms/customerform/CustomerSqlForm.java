/**
 * CustomerSqlForm.java
 * io.znz.jsite.visa.forms
 * Copyright (c) 2017, 北京科技有限公司版权所有.
*/

package io.znz.jsite.visa.forms.customerform;

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
public class CustomerSqlForm extends KenDoParamForm {
	//主键
	private long id;
	//序号
	private long comId;
	//公司全名
	private String fullComName;
	//客户来源
	private Integer customerSource;
	//联系人
	private String linkman;
	//手机
	private String telephone;
	//邮箱
	private String email;
	//状态
	private Integer status;
	//创建时间
	private Date createTime;
	//更新时间
	private Date updateTime;
	//备注
	private String remark;

	//父id
	private long pId;

	private String keyword;

	@Override
	public Sql sql(SqlManager paramSqlManager) {
		/**
		 * 默认使用了当前form关联entity的单表查询sql,如果是多表复杂sql，
		 * 请使用sqlManager获取自定义的sql，并设置查询条件
		 */
		String sqlString = paramSqlManager.get("customermanage_list_data");
		Sql sql = Sqls.create(sqlString);
		sql.setCondition(cnd());
		return sql;
	}

	private Cnd cnd() {
		Cnd cnd = Cnd.NEW();
		if (!Util.isEmpty(keyword)) {
			cnd.and("vcm.fullComName", "like", "%" + keyword + "%").or("vcm.linkman", "like", "%" + keyword + "%")
					.or("vcm.telephone", "like", "%" + keyword + "%");
		}
		if (!Util.isEmpty(pId) && pId != 0) {
			cnd.and("vcm.pId", "=", pId);
		}
		cnd.and("vcm.comId", "=", comId);
		cnd.orderBy("vcm.createTime", "DESC");
		return cnd;
	}
}
