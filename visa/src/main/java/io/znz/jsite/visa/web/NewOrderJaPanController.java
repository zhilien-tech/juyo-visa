/**
 * NewOrderJaPanController.java
 * io.znz.jsite.visa.web
 * Copyright (c) 2017, 北京科技有限公司版权所有.
*/

package io.znz.jsite.visa.web;

import io.znz.jsite.visa.entity.usa.NewCustomerEntity;
import io.znz.jsite.visa.form.NewOrderJapanSqlForm;
import io.znz.jsite.visa.service.NewOrderJaPanService;

import java.util.List;

import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.SqlManager;
import org.nutz.dao.Sqls;
import org.nutz.dao.pager.Pager;
import org.nutz.dao.sql.Sql;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.uxuexi.core.db.dao.IDbDao;
import com.uxuexi.core.db.util.DbSqlUtil;

/**
 * TODO(这里用一句话描述这个类的作用)
 * <p>
 * TODO(这里描述这个类补充说明 – 可选)
 *
 * @author   孙斌
 * @Date	 2017年6月21日 	 
 */
@Controller
@RequestMapping("visa/neworderjp")
public class NewOrderJaPanController {
	@Autowired
	private NewOrderJaPanService newOrderJaPanService;
	@Autowired
	protected IDbDao dbDao;

	/**nutz dao*/
	@Autowired
	protected Dao nutDao;

	/**
	 * 注入容器中的sqlManager对象，用于获取sql
	 */
	@Autowired
	protected SqlManager sqlManager;

	/**
	 * 列表查看订单信息
	 */
	@RequestMapping(value = "list")
	@ResponseBody
	public Object list(@RequestBody NewOrderJapanSqlForm form) {

		Pager pager = new Pager();
		pager.setPageNumber(form.getPageNumber());
		pager.setPageSize(form.getPageSize());
		return newOrderJaPanService.listPage(form, pager);
	}

	/***
	 * 
	 * 查询子页面的相关信息
	 * <p>
	 *
	 * @param customers
	 * @param customer
	 * @return TODO(这里描述每个参数,如果有返回值描述返回值,如果有异常描述异常)
	 */
	@RequestMapping(value = "childList")
	@ResponseBody
	public Object childList(long orderId) {

		Cnd cnd = Cnd.NEW();
		String sqlString = sqlManager.get("newcustomerjapan_list");
		Sql sql = Sqls.create(sqlString);
		sql.setParam("orderId", orderId);
		List<NewCustomerEntity> query = DbSqlUtil.query(dbDao, NewCustomerEntity.class, sql);
		return query;
	}
}
