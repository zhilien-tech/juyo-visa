/**
 * NewOrderJaPanController.java
 * io.znz.jsite.visa.web
 * Copyright (c) 2017, 北京科技有限公司版权所有.
*/

package io.znz.jsite.visa.web;

import io.znz.jsite.base.bean.ResultObject;
import io.znz.jsite.visa.entity.customer.CustomerManageEntity;
import io.znz.jsite.visa.entity.japan.NewCustomerJpEntity;
import io.znz.jsite.visa.entity.japan.NewCustomerOrderJpEntity;
import io.znz.jsite.visa.entity.japan.NewDateplanJpEntity;
import io.znz.jsite.visa.entity.japan.NewFastmailJpEntity;
import io.znz.jsite.visa.entity.japan.NewOrderJpEntity;
import io.znz.jsite.visa.entity.japan.NewTripJpEntity;
import io.znz.jsite.visa.entity.japan.NewTripplanJpEntity;
import io.znz.jsite.visa.entity.usa.NewCustomerEntity;
import io.znz.jsite.visa.enums.OrderVisaApproStatusEnum;
import io.znz.jsite.visa.form.NewOrderJapanSqlForm;
import io.znz.jsite.visa.service.NewOrderJaPanService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.SqlManager;
import org.nutz.dao.Sqls;
import org.nutz.dao.entity.Record;
import org.nutz.dao.pager.Pager;
import org.nutz.dao.sql.Sql;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.uxuexi.core.common.util.Util;
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

	/*****
	 * 
	 * 日本订单的保存
	 *
	 * @param customers
	 * @param customer
	 * @return TODO(这里描述每个参数,如果有返回值描述返回值,如果有异常描述异常)
	 */
	@RequestMapping(value = "orderJpsave")
	@ResponseBody
	public Object orderJpsave(@RequestBody NewOrderJpEntity order) {
		CustomerManageEntity customermanage = order.getCustomermanage();
		if (!Util.isEmpty(customermanage)) {
			order.setCustomer_manager_id(customermanage.getId());
			Chain chain = Chain.make("updateTime", new Date());
			chain.add("fullComName", customermanage.getFullComName());
			chain.add("customerSource", customermanage.getCustomerSource());
			chain.add("linkman", customermanage.getLinkman());
			chain.add("telephone", customermanage.getTelephone());
			chain.add("email", customermanage.getEmail());
			dbDao.update(CustomerManageEntity.class, chain, Cnd.where("id", "=", customermanage.getId()));
		}
		//根据他们的id是否存在判断是更新还是删除
		NewOrderJpEntity orderOld = order;
		if (!Util.isEmpty(order.getId()) && order.getId() > 0) {
			NewOrderJpEntity orderNew = dbDao.fetch(NewOrderJpEntity.class, order.getId());
			order.setUpdatetime(new Date());
			nutDao.update(order);
			int a = order.getHeadnum() - orderNew.getHeadnum();
			if (a > 0) {

				//根据人数插入多个申请人的数据
				for (int i = a; i > 0; i--) {
					NewCustomerJpEntity c = new NewCustomerJpEntity();
					c.setCreatetime(new Date());
					c.setUpdatetime(new Date());
					NewCustomerJpEntity insert = dbDao.insert(c);
					NewCustomerOrderJpEntity customerOrderEntity = new NewCustomerOrderJpEntity();
					customerOrderEntity.setCustomer_jp_id(insert.getId());
					customerOrderEntity.setOrder_jp_id(orderOld.getId());
					customerOrderEntity.setCreatetime(new Date());
					customerOrderEntity.setUpdatetime(new Date());
					dbDao.insert(customerOrderEntity);
				}
			} else if (a < 0) {
				int n = -a;
				List<NewCustomerOrderJpEntity> query = dbDao.query(NewCustomerOrderJpEntity.class,
						Cnd.where("orderJpId", "=", order.getId()).orderBy("updatetime", "asc"), null);
				for (int i = 0; i < n; i++) {
					NewCustomerOrderJpEntity c = query.get(i);
					dbDao.delete(c);
					dbDao.delete(NewCustomerJpEntity.class, c.getCustomer_jp_id());

				}
			}

		} else {
			order.setStatus(OrderVisaApproStatusEnum.placeOrder.intKey());
			order.setCreatetime(new Date());
			order.setUpdatetime(new Date());

			//生成订单号
			SimpleDateFormat smf = new SimpleDateFormat("yyyyMMdd");
			String format = smf.format(new Date());
			String sqlString = sqlManager.get("neworderjapan_ordernum");
			Sql sql = Sqls.create(sqlString);
			List<Record> query = dbDao.query(sql, null, null);
			int sum = 1;
			if (!Util.isEmpty(query) && query.size() > 0) {
				String string = query.get(0).getString("ordernumber");
				int a = Integer.valueOf(string.substring(11, string.length()));
				sum += a;
			}
			String sum1 = "";
			if (sum / 10 == 0) {
				sum1 = "000" + sum;
			} else if (sum / 100 == 0) {
				sum1 = "00" + sum;

			} else if (sum / 1000 == 0) {
				sum1 = "0" + sum;
			} else {
				sum1 = "" + sum;

			}
			String ordernum = format + "JP" + sum1;

			order.setOrdernumber(ordernum);

			orderOld = dbDao.insert(order);
			//根据人数插入多个申请人的数据
			for (int i = orderOld.getHeadnum(); i > 0; i--) {
				NewCustomerJpEntity c = new NewCustomerJpEntity();
				c.setCreatetime(new Date());
				c.setUpdatetime(new Date());
				NewCustomerJpEntity insert = dbDao.insert(c);
				NewCustomerOrderJpEntity customerOrderEntity = new NewCustomerOrderJpEntity();
				customerOrderEntity.setCustomer_jp_id(insert.getId());
				customerOrderEntity.setOrder_jp_id(orderOld.getId());

				customerOrderEntity.setCreatetime(new Date());
				customerOrderEntity.setUpdatetime(new Date());
				dbDao.insert(customerOrderEntity);
			}

			//	dbDao.update(NewOrderJpEntity.class, Chain.make("ordernumber", ordernum), Cnd.where("id", "=", orderOld.getId()));
		}

		NewFastmailJpEntity fastMail = order.getFastMail();
		if (!Util.isEmpty(fastMail)) {
			if (!Util.isEmpty(fastMail.getId()) && fastMail.getId() > 0) {
				nutDao.update(fastMail);
			} else {

				fastMail.setOrder_jp_id(orderOld.getId());
				dbDao.insert(fastMail);
			}
		}
		NewTripJpEntity tripJp = order.getTripJp();
		if (!Util.isEmpty(tripJp)) {
			if (!Util.isEmpty(tripJp.getId()) && tripJp.getId() > 0) {
				nutDao.update(tripJp);
			} else {

				tripJp.setOrder_jp_id(orderOld.getId());
				tripJp = dbDao.insert(tripJp);
			}
		}

		List<NewDateplanJpEntity> dateplanJpList = order.getDateplanJpList();
		if (!Util.isEmpty(dateplanJpList) && dateplanJpList.size() > 0) {

			for (NewDateplanJpEntity newPeerPersionEntity : dateplanJpList) {
				if (!Util.isEmpty(newPeerPersionEntity.getId()) && newPeerPersionEntity.getId() > 0) {
					nutDao.update(newPeerPersionEntity);
				} else {
					newPeerPersionEntity.setTrip_jp_id(tripJp.getId());

					dbDao.insert(newPeerPersionEntity);
				}
			}
		}
		List<NewTripplanJpEntity> tripplanJpList = order.getTripplanJpList();
		if (!Util.isEmpty(tripplanJpList) && tripplanJpList.size() > 0) {

			for (NewTripplanJpEntity newPeerPersionEntity : tripplanJpList) {
				if (!Util.isEmpty(newPeerPersionEntity.getId()) && newPeerPersionEntity.getId() > 0) {
					nutDao.update(newPeerPersionEntity);
				} else {
					newPeerPersionEntity.setOrder_jp_id(orderOld.getId());

					dbDao.insert(newPeerPersionEntity);
				}
			}
		}
		return ResultObject.success("添加成功");
	}

	/***
	 * 
	 *信息编辑界面的数据回显
	 *
	 * @param orderid
	 * @return TODO(这里描述每个参数,如果有返回值描述返回值,如果有异常描述异常)
	 */
	@RequestMapping(value = "showDetail")
	@ResponseBody
	public Object showDetail(long orderid) {
		NewOrderJpEntity order = dbDao.fetch(NewOrderJpEntity.class, orderid);
		CustomerManageEntity customerManageEntity = dbDao.fetch(CustomerManageEntity.class,
				Long.valueOf(order.getCustomer_manager_id()));
		if (!Util.isEmpty(customerManageEntity)) {
			order.setCustomermanage(customerManageEntity);
		}
		List<NewTripJpEntity> newTrips = dbDao.query(NewTripJpEntity.class, Cnd.where("order_jp_id", "=", orderid),
				null);
		if (!Util.isEmpty(newTrips) && newTrips.size() > 0) {
			order.setTripJp(newTrips.get(0));
			List<NewDateplanJpEntity> query = dbDao.query(NewDateplanJpEntity.class,
					Cnd.where("trip_jp_id", "=", newTrips.get(0).getId()), null);
			order.setDateplanJpList(query);
		}
		List<NewTripplanJpEntity> newPayPersionEntities = dbDao.query(NewTripplanJpEntity.class,
				Cnd.where("order_jp_id", "=", orderid), null);
		if (!Util.isEmpty(newPayPersionEntities) && newPayPersionEntities.size() > 0) {
			order.setTripplanJpList(newPayPersionEntities);
		}
		List<NewFastmailJpEntity> newPayCompanyEntities = dbDao.query(NewFastmailJpEntity.class,
				Cnd.where("order_jp_id", "=", orderid), null);
		if (!Util.isEmpty(newPayCompanyEntities) && newPayCompanyEntities.size() > 0) {
			order.setFastMail(newPayCompanyEntities.get(0));
		}
		return order;
	}

}
