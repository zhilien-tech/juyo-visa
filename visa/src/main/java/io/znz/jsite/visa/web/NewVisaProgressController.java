/**
 * NewVisaProgressController.java
 * io.znz.jsite.visa.web
 * Copyright (c) 2017, 北京科技有限公司版权所有.
*/

package io.znz.jsite.visa.web;

import io.znz.jsite.core.entity.EmployeeEntity;
import io.znz.jsite.core.util.Const;
import io.znz.jsite.util.DateUtils;
import io.znz.jsite.util.XORUtil;
import io.znz.jsite.visa.entity.japan.NewCustomerJpEntity;
import io.znz.jsite.visa.entity.japan.NewCustomerOrderJpEntity;
import io.znz.jsite.visa.entity.japan.NewOrderJpEntity;
import io.znz.jsite.visa.entity.usa.NewCustomerEntity;
import io.znz.jsite.visa.entity.usa.NewCustomerOrderEntity;
import io.znz.jsite.visa.entity.usa.NewOrderEntity;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.joda.time.DateTime;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.SqlManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.beust.jcommander.internal.Maps;
import com.google.common.collect.Lists;
import com.uxuexi.core.common.util.Util;
import com.uxuexi.core.db.dao.IDbDao;

/**
 * TODO(这里用一句话描述这个类的作用)
 * <p>
 * TODO(这里描述这个类补充说明 – 可选)
 *
 * @author   孙斌
 * @Date	 2017年6月27日 	 
 */
@Controller
@RequestMapping("visa/progress")
public class NewVisaProgressController {
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

	//48小时
	private static final long HOURS_48 = 48;

	/**
	 * 列表查看订单信息
	 */
	@RequestMapping(value = "country")
	@ResponseBody
	public Object list(HttpServletRequest request, String logintype, String secretMsg) {
		if ("5".equals(logintype)) {
			List<NewCustomerEntity> usalist = Lists.newArrayList();

			XORUtil xor = XORUtil.getInstance();
			String orderInfo = xor.encode(secretMsg, "我是秘钥");
			String[] infoList = orderInfo.split("&");

			if (infoList.length == 4) {

				String orderId = infoList[1];
				String datetime = infoList[3];

				//计算时间差
				DateTime now = DateUtils.nowDateTime();
				DateTime dateTime = DateUtils.string2DateTime(datetime, "");
				long hours = DateUtils.hoursOfTwoTime(now, dateTime);
				if (!Util.isEmpty(orderId) && hours <= HOURS_48) {

					List<NewCustomerOrderEntity> query = dbDao.query(NewCustomerOrderEntity.class,
							Cnd.where("orderid", "=", orderId), null);
					if (!Util.isEmpty(query) && query.size() > 0) {
						for (NewCustomerOrderEntity CustomerOrderEntity : query) {
							if (!Util.isEmpty(CustomerOrderEntity)) {
								NewCustomerEntity fetch = dbDao.fetch(NewCustomerEntity.class,
										CustomerOrderEntity.getCustomerid());
								usalist.add(fetch);
							}
						}

					}
				}

			}
			Map<String, Object> map = Maps.newHashMap();
			map.put("usa", usalist);
			map.put("japan", "");
			map.put("tourist", 1);

			return map;
		} else {

			EmployeeEntity user = (EmployeeEntity) request.getSession().getAttribute(Const.SESSION_NAME);
			//美国
			List<NewCustomerEntity> usalist = dbDao.query(NewCustomerEntity.class, Cnd
					.where("empid", "=", user.getId()).orderBy("createtime", "desc"), null);

			//日本
			List<NewCustomerJpEntity> japanlist = dbDao.query(NewCustomerJpEntity.class,
					Cnd.where("empid", "=", user.getId()).orderBy("createtime", "desc"), null);
			/*Map<String, Integer> map = Maps.newHashMap();
			if (!Util.isEmpty(japanlist) && !Util.isEmpty(usalist)) {
			map.put("usa", usalist.get(0).getId());
			map.put("japan", Integer.valueOf(japanlist.get(0).getId() + ""));

			} else if (Util.isEmpty(japanlist) && !Util.isEmpty(usalist)) {
			map.put("usa", usalist.get(0).getId());

			} else if (!Util.isEmpty(japanlist) && Util.isEmpty(usalist)) {
			map.put("japan", Integer.valueOf(japanlist.get(0).getId() + ""));

			}*/
			Map<String, Object> map = Maps.newHashMap();
			if (!Util.isEmpty(japanlist) && !Util.isEmpty(usalist)) {
				map.put("usa", usalist.get(0));
				map.put("japan", japanlist.get(0));

			} else if (Util.isEmpty(japanlist) && !Util.isEmpty(usalist)) {
				map.put("usa", usalist.get(0));

			} else if (!Util.isEmpty(japanlist) && Util.isEmpty(usalist)) {
				map.put("japan", japanlist.get(0));

			}

			return map;
		}
	}

	/**
	 * 查询订单号
	 */
	@RequestMapping(value = "ordernumber")
	@ResponseBody
	public Object ordernumber(long customerid, int countrystatus) {
		if (countrystatus == 0) {
			NewCustomerEntity fetch = dbDao.fetch(NewCustomerEntity.class, customerid);
			List<NewCustomerOrderEntity> query = dbDao.query(NewCustomerOrderEntity.class,
					Cnd.where("customerid", "=", customerid), null);

			if (!Util.isEmpty(query) && query.size() > 0) {
				long orderid = query.get(0).getOrderid();
				NewOrderEntity newOrder = dbDao.fetch(NewOrderEntity.class, orderid);
				newOrder.setWritebasicinfo(fetch.getWritebasicinfo());
				return newOrder;
			}
		} else if (countrystatus == 1) {
			NewCustomerJpEntity fetch = dbDao.fetch(NewCustomerJpEntity.class, customerid);
			List<NewCustomerOrderJpEntity> query = dbDao.query(NewCustomerOrderJpEntity.class,
					Cnd.where("customer_jp_id", "=", customerid), null);

			if (!Util.isEmpty(query) && query.size() > 0) {
				long orderid = query.get(0).getOrder_jp_id();
				NewOrderJpEntity newOrder = dbDao.fetch(NewOrderJpEntity.class, orderid);
				newOrder.setWritebasicinfo(fetch.getWritebasicinfo());
				return newOrder;
			}

		}
		return "";
	}

}
