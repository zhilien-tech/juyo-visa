/**
 * NewCustomerController.java
 * io.znz.jsite.visa.web
 * Copyright (c) 2017, 北京科技有限公司版权所有.
*/

package io.znz.jsite.visa.web;

import io.znz.jsite.base.bean.ResultObject;
import io.znz.jsite.visa.entity.customer.CustomerManageEntity;
import io.znz.jsite.visa.entity.usa.NewCustomerEntity;
import io.znz.jsite.visa.entity.usa.NewCustomerOrderEntity;
import io.znz.jsite.visa.entity.usa.NewFastMailEntity;
import io.znz.jsite.visa.entity.usa.NewOrderEntity;
import io.znz.jsite.visa.entity.usa.NewPayCompanyEntity;
import io.znz.jsite.visa.entity.usa.NewPayPersionEntity;
import io.znz.jsite.visa.entity.usa.NewPeerPersionEntity;
import io.znz.jsite.visa.entity.usa.NewTrip;
import io.znz.jsite.visa.enums.OrderVisaApproStatusEnum;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.SqlManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.uxuexi.core.common.util.Util;
import com.uxuexi.core.db.dao.IDbDao;

/**
 * TODO(这里用一句话描述这个类的作用)
 * <p>
 * TODO(这里描述这个类补充说明 – 可选)
 *
 * @author   孙斌
 * @Date	 2017年6月16日 	 
 */
@Controller
@RequestMapping("visa/newcustomer")
public class NewCustomerController {
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

	/*****
	 * 
	 * TODO(这里用一句话描述这个方法的作用)
	 * <p>
	 * TODO(这里描述这个方法详情– 可选)
	 *
	 * @param customers
	 * @param customer
	 * @return TODO(这里描述每个参数,如果有返回值描述返回值,如果有异常描述异常)
	 */
	@RequestMapping(value = "customerSave")
	@ResponseBody
	public Object orderSave(@RequestBody NewOrderEntity order) {
		CustomerManageEntity customermanage = order.getCustomermanage();
		if (!Util.isEmpty(customermanage)) {
			order.setCus_management_id(customermanage.getId());
			Chain chain = Chain.make("updateTime", new Date());
			chain.add("fullComName", customermanage.getFullComName());
			chain.add("customerSource", customermanage.getCustomerSource());
			chain.add("linkman", customermanage.getLinkman());
			chain.add("telephone", customermanage.getTelephone());
			chain.add("email", customermanage.getEmail());
			dbDao.update(CustomerManageEntity.class, chain, Cnd.where("id", "=", customermanage.getId()));
		}
		order.setStatus(OrderVisaApproStatusEnum.draft.intKey());
		order.setCreatetime(new Date());
		order.setUpdatetime(new Date());
		NewOrderEntity orderOld = dbDao.insert(order);
		//根据人数插入多个申请人的数据
		for (int i = orderOld.getHeadcount(); i > 0; i--) {
			NewCustomerEntity c = new NewCustomerEntity();

			NewCustomerEntity insert = dbDao.insert(c);
			NewCustomerOrderEntity customerOrderEntity = new NewCustomerOrderEntity();
			customerOrderEntity.setCustomerid(insert.getId());
			customerOrderEntity.setOrderid(orderOld.getId());
			dbDao.insert(customerOrderEntity);
		}
		//生成订单号
		Date date = new Date();
		Calendar now = Calendar.getInstance();
		now.setTime(new Date());
		String ordernum = "" + now.get(Calendar.YEAR) + (now.get(Calendar.MONTH) + 1) + now.get(Calendar.DAY_OF_MONTH)
				+ "000" + orderOld.getId();

		String random = "";
		for (int i = 0; i < 16 - ordernum.length(); i++) {
			int a = (int) (Math.random() * 10);
			random = random + a;
		}
		ordernum += random;
		orderOld.setOrdernumber(ordernum);
		dbDao.update(NewOrderEntity.class, Chain.make("ordernumber", ordernum), Cnd.where("id", "=", orderOld.getId()));
		NewFastMailEntity fastMail = order.getFastMail();
		if (!Util.isEmpty(fastMail)) {

			fastMail.setOrderid(orderOld.getId());
			dbDao.insert(fastMail);
		}
		NewPayCompanyEntity payCompany = order.getPayCompany();
		if (!Util.isEmpty(payCompany)) {

			payCompany.setOrderid(orderOld.getId());
			dbDao.insert(payCompany);
		}
		NewPayPersionEntity payPersion = order.getPayPersion();
		if (!Util.isEmpty(payPersion)) {

			payPersion.setOrderid(orderOld.getId());
			dbDao.insert(payPersion);
		}
		NewTrip trip = order.getTrip();
		if (!Util.isEmpty(trip)) {

			trip.setOrderid(orderOld.getId());
			trip = dbDao.insert(trip);
		}
		List<NewPeerPersionEntity> peerList = order.getPeerList();
		if (!Util.isEmpty(peerList) && peerList.size() > 0) {

			for (NewPeerPersionEntity newPeerPersionEntity : peerList) {
				newPeerPersionEntity.setTripid(trip.getId());
			}
			List<NewPeerPersionEntity> insert = dbDao.insert(peerList);
		}
		return ResultObject.success("添加成功");
	}
}
