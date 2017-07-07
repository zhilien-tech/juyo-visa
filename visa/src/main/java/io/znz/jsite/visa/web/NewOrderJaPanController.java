/**
 * NewOrderJaPanController.java
 * io.znz.jsite.visa.web
 * Copyright (c) 2017, 北京科技有限公司版权所有.
*/

package io.znz.jsite.visa.web;

import io.znz.jsite.base.bean.ResultObject;
import io.znz.jsite.core.service.MailService;
import io.znz.jsite.util.security.Digests;
import io.znz.jsite.util.security.Encodes;
import io.znz.jsite.visa.bean.Flight;
import io.znz.jsite.visa.bean.Hotel;
import io.znz.jsite.visa.bean.Scenic;
import io.znz.jsite.visa.entity.customer.CustomerManageEntity;
import io.znz.jsite.visa.entity.delivery.NewDeliveryJapanEntity;
import io.znz.jsite.visa.entity.japan.NewCustomerJpEntity;
import io.znz.jsite.visa.entity.japan.NewCustomerOrderJpEntity;
import io.znz.jsite.visa.entity.japan.NewCustomerresourceJpEntity;
import io.znz.jsite.visa.entity.japan.NewDateplanJpEntity;
import io.znz.jsite.visa.entity.japan.NewFastmailJpEntity;
import io.znz.jsite.visa.entity.japan.NewFinanceJpEntity;
import io.znz.jsite.visa.entity.japan.NewOldnameJpEntity;
import io.znz.jsite.visa.entity.japan.NewOldpassportJpEntity;
import io.znz.jsite.visa.entity.japan.NewOrderJpEntity;
import io.znz.jsite.visa.entity.japan.NewOrthercountryJpEntity;
import io.znz.jsite.visa.entity.japan.NewProposerInfoJpEntity;
import io.znz.jsite.visa.entity.japan.NewRecentlyintojpJpEntity;
import io.znz.jsite.visa.entity.japan.NewTripJpEntity;
import io.znz.jsite.visa.entity.japan.NewTripplanJpEntity;
import io.znz.jsite.visa.entity.japan.NewWorkinfoJpEntity;
import io.znz.jsite.visa.entity.usa.NewCustomerEntity;
import io.znz.jsite.visa.entity.usa.NewOrderEntity;
import io.znz.jsite.visa.entity.user.EmployeeEntity;
import io.znz.jsite.visa.enums.OrderJapancustomersourceEnum;
import io.znz.jsite.visa.enums.OrderVisaApproStatusEnum;
import io.znz.jsite.visa.enums.UserTypeEnum;
import io.znz.jsite.visa.form.NewOrderJapanSqlForm;
import io.znz.jsite.visa.newpdf.NewPdfService;
import io.znz.jsite.visa.service.NewOrderJaPanService;

import java.io.IOException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;
import com.ibm.icu.util.Calendar;
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
	@Autowired
	private MailService mailService;
	/**nutz dao*/
	@Autowired
	protected Dao nutDao;
	@Autowired
	private NewPdfService newPdfService;
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
	@Transactional
	public Object orderJpsave(@RequestBody NewOrderJpEntity order) {

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
					List<NewProposerInfoJpEntity> proposerInfoJpList1 = order.getProposerInfoJpList();
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
					NewProposerInfoJpEntity n = new NewProposerInfoJpEntity();
					n.setCustomer_jp_id(insert.getId());
					n.setOrder_jp_id(orderOld.getId());
					proposerInfoJpList1.add(n);
					order.setProposerInfoJpList(proposerInfoJpList1);
				}
			} else if (a < 0) {
				int n = -a;
				List<NewCustomerOrderJpEntity> query = dbDao.query(NewCustomerOrderJpEntity.class,
						Cnd.where("order_jp_id", "=", order.getId()).orderBy("updatetime", "asc"), null);
				for (int i = 0; i < n; i++) {
					NewCustomerOrderJpEntity c = query.get(i);
					dbDao.delete(c);
					dbDao.delete(NewCustomerJpEntity.class, c.getCustomer_jp_id());
					List<NewProposerInfoJpEntity> query2 = dbDao.query(NewProposerInfoJpEntity.class,
							Cnd.where("customer_jp_id", "=", c.getCustomer_jp_id()), null);
					dbDao.delete(query2);
					List<NewProposerInfoJpEntity> proposerInfoJpList1 = order.getProposerInfoJpList();
					/*	for (NewProposerInfoJpEntity newProposerInfoJpEntity : query2) {
							for (NewProposerInfoJpEntity newProposerInfoJpEntity1 : proposerInfoJpList1) {

								if (newProposerInfoJpEntity1.getId() == newProposerInfoJpEntity.getId()) {
									proposerInfoJpList1.remove(newProposerInfoJpEntity1);
								}
							}
						}*/
					proposerInfoJpList1.removeAll(query2);
					order.setProposerInfoJpList(proposerInfoJpList1);

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
			//设置为日本
			order.setCountrytype(1);
			orderOld = dbDao.insert(order);
			//根据人数插入多个申请人的数据
			List<NewProposerInfoJpEntity> proposerInfoJpList = order.getProposerInfoJpList();
			if (!Util.isEmpty(orderOld.getHeadnum())) {

				for (int i = orderOld.getHeadnum(); i > 0; i--) {
					NewCustomerJpEntity c = new NewCustomerJpEntity();
					c.setCreatetime(new Date());
					c.setUpdatetime(new Date());
					String xing = proposerInfoJpList.get(i - 1).getXing();
					String name = proposerInfoJpList.get(i - 1).getName();
					if (!Util.isEmpty(xing)) {
						c.setChinesexing(xing);
					}
					if (!Util.isEmpty(name)) {
						c.setChinesexing(name);
					}
					NewCustomerJpEntity insert = dbDao.insert(c);
					proposerInfoJpList.get(i - 1).setCustomer_jp_id(insert.getId());

					NewCustomerOrderJpEntity customerOrderEntity = new NewCustomerOrderJpEntity();
					customerOrderEntity.setCustomer_jp_id(insert.getId());
					customerOrderEntity.setOrder_jp_id(orderOld.getId());

					customerOrderEntity.setCreatetime(new Date());
					customerOrderEntity.setUpdatetime(new Date());
					dbDao.insert(customerOrderEntity);
				}
			}

			//	dbDao.update(NewOrderJpEntity.class, Chain.make("ordernumber", ordernum), Cnd.where("id", "=", orderOld.getId()));
		}

		CustomerManageEntity customermanage = order.getCustomermanage();
		int customerSource = order.getCustomerSource();
		if (!Util.isEmpty(customerSource) && customerSource > 0) {
			if (customerSource == OrderJapancustomersourceEnum.zhike.intKey()) {
				NewCustomerresourceJpEntity customerresourceJp = order.getCustomerresourceJp();
				List<NewCustomerresourceJpEntity> customerresource = dbDao.query(NewCustomerresourceJpEntity.class,
						Cnd.where("order_jp_id", "=", orderOld.getId()), null);

				customerresourceJp.setOrder_jp_id(orderOld.getId());
				if (!Util.isEmpty(customerresource) && customerresource.size() > 0) {
					customerresourceJp.setId(customerresource.get(0).getId());
					nutDao.update(customerresourceJp);

				} else {

					dbDao.insert(customerresourceJp);
				}
				/*	if (!Util.isEmpty(customerresourceJp)) {
						if (!Util.isEmpty(customerresourceJp.getId()) && customerresourceJp.getId() > 0) {
							nutDao.update(customerresourceJp);
						} else {

							customerresourceJp.setOrder_jp_id(orderOld.getId());
							dbDao.insert(customerresourceJp);
						}
					}*/
			} else {
				if (customerSource == OrderJapancustomersourceEnum.zhike.intKey()) {

				} else {

					orderOld.setCustomer_manager_id(Long.valueOf(customermanage.getId()));
					dbDao.update(orderOld, null);
					Chain chain = Chain.make("updateTime", new Date());
					chain.add("fullComName", customermanage.getFullComName());
					chain.add("customerSource", customerSource);
					chain.add("linkman", customermanage.getLinkman());
					chain.add("telephone", customermanage.getTelephone());
					chain.add("email", customermanage.getEmail());
					dbDao.update(CustomerManageEntity.class, chain, Cnd.where("id", "=", customermanage.getId()));
					//在客户来源不是直客的状态下也存在这个表中，以便用于列表展示

					NewCustomerresourceJpEntity customerresourceJp = new NewCustomerresourceJpEntity();
					List<NewCustomerresourceJpEntity> customerresource = dbDao.query(NewCustomerresourceJpEntity.class,
							Cnd.where("order_jp_id", "=", orderOld.getId()), null);
					customerresourceJp.setEmail(customermanage.getEmail());
					customerresourceJp.setFullComName(customermanage.getFullComName());
					customerresourceJp.setLinkman(customermanage.getLinkman());
					customerresourceJp.setTelephone(customermanage.getTelephone());
					customerresourceJp.setOrder_jp_id(orderOld.getId());
					if (!Util.isEmpty(customerresource) && customerresource.size() > 0) {
						customerresourceJp.setId(customerresource.get(0).getId());
						nutDao.update(customerresourceJp);

					} else {

						dbDao.insert(customerresourceJp);
					}
				}

			}
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
		Date startdate = null;
		Date enddate = null;
		String arrivecity = null;
		NewTripJpEntity tripJp = order.getTripJp();

		if (!Util.isEmpty(tripJp)) {

			if (tripJp.getOneormore() == 0) {
				Flight gofilght = tripJp.getGofilght();
				Flight returnfilght = tripJp.getReturnfilght();
				if (!Util.isEmpty(gofilght)) {
					tripJp.setFlightnum(gofilght.getId() + "");
				}
				if (!Util.isEmpty(returnfilght)) {
					tripJp.setReturnflightnum(returnfilght.getId() + "");
				}
				if (!Util.isEmpty(tripJp.getId()) && tripJp.getId() > 0) {
					nutDao.update(tripJp);
				} else {

					tripJp.setOrder_jp_id(orderOld.getId());
					tripJp = dbDao.insert(tripJp);
				}
				startdate = tripJp.getStartdate();
				enddate = tripJp.getReturndate();
				arrivecity = tripJp.getArrivecity();
			} else {
				if (!Util.isEmpty(tripJp.getId()) && tripJp.getId() > 0) {
					nutDao.update(tripJp);
				} else {

					tripJp.setOrder_jp_id(orderOld.getId());
					tripJp = dbDao.insert(tripJp);
				}
			}

		}
		Integer oneormore = tripJp.getOneormore();
		//================================
		List<NewProposerInfoJpEntity> proposerInfoJpList = order.getProposerInfoJpList();
		long orderid = orderOld.getId();
		List<NewProposerInfoJpEntity> list8 = dbDao.query(NewProposerInfoJpEntity.class,
				Cnd.where("order_jp_id", "=", orderOld.getId()), null);
		/*if (!Util.isEmpty(list8) && list8.size() > 0) {

			dbDao.delete(list8);
		}*/
		if (!Util.isEmpty(proposerInfoJpList) && proposerInfoJpList.size() > 0) {
			for (int i = 0; i < list8.size(); i++) {

				boolean flag = true;
				for (int j = 0; j < proposerInfoJpList.size(); j++) {

					if (proposerInfoJpList.get(j).getId() == list8.get(i).getId()) {
						flag = false;
					}
					if (Util.isEmpty(proposerInfoJpList.get(j).getId())) {
						flag = false;
					}

				}

				if (flag) {
					dbDao.delete(list8.get(i));
				}
			}

			for (NewProposerInfoJpEntity newPeerPersionEntity : proposerInfoJpList) {
				/*	if (!Util.isEmpty(newPeerPersionEntity.getId()) && newPeerPersionEntity.getId() > 0) {
						nutDao.update(newPeerPersionEntity);
					} else {*/
				if (!Util.isEmpty(newPeerPersionEntity.getId()) && newPeerPersionEntity.getId() > 0) {
					boolean relation = newPeerPersionEntity.isIsmainproposer();
					newPeerPersionEntity.setOrder_jp_id(orderOld.getId());
					String xing = newPeerPersionEntity.getXing();
					String name = newPeerPersionEntity.getName();
					if (Util.isEmpty(xing)) {
						xing = "";
					}
					if (Util.isEmpty(name)) {
						name = "";
					}
					newPeerPersionEntity.setFullname(xing + name);
					if (!Util.isEmpty(newPeerPersionEntity.getCustomer_jp_id())
							&& newPeerPersionEntity.getCustomer_jp_id() > 0) {
						NewCustomerJpEntity customer = dbDao.fetch(NewCustomerJpEntity.class,
								newPeerPersionEntity.getCustomer_jp_id());
						customer.setChinesexing(xing);
						customer.setChinesename(name);
						customer.setChinesefullname(xing + name);
						nutDao.update(customer);
					}

					if (!Util.isEmpty(relation) && relation == true) {
						newPeerPersionEntity.setRelationproposer(newPeerPersionEntity.getId());

					}
					nutDao.update(newPeerPersionEntity);
				} else {

					newPeerPersionEntity.setOrder_jp_id(orderOld.getId());
					String xing = newPeerPersionEntity.getXing();
					String name = newPeerPersionEntity.getName();
					if (Util.isEmpty(xing)) {
						xing = "";
					}
					if (Util.isEmpty(name)) {
						name = "";
					}
					newPeerPersionEntity.setFullname(xing + name);
					if (!Util.isEmpty(newPeerPersionEntity.getCustomer_jp_id())
							&& newPeerPersionEntity.getCustomer_jp_id() > 0) {
						NewCustomerJpEntity customer = dbDao.fetch(NewCustomerJpEntity.class,
								newPeerPersionEntity.getCustomer_jp_id());
						customer.setChinesexing(xing);
						customer.setChinesename(name);
						customer.setChinesefullname(xing + name);
						nutDao.update(customer);
					}
					newPeerPersionEntity = dbDao.insert(newPeerPersionEntity);
					boolean relation = newPeerPersionEntity.isIsmainproposer();
					if (!Util.isEmpty(relation) && relation == true) {
						newPeerPersionEntity.setRelationproposer(newPeerPersionEntity.getId());

					}
					nutDao.update(newPeerPersionEntity);
				}

				/*	}*/
			}
			//List<NewPeerPersionEntity> insert = dbDao.insert(peerList);
		}
		//================================
		List<NewDateplanJpEntity> dateplanJpList = order.getDateplanJpList();
		List<NewDateplanJpEntity> list1 = dbDao.query(NewDateplanJpEntity.class,
				Cnd.where("trip_jp_id", "=", tripJp.getId()), null);
		if (!Util.isEmpty(list1) && list1.size() > 0) {

			dbDao.delete(list1);
		}
		if (!Util.isEmpty(dateplanJpList) && dateplanJpList.size() > 0) {

			if (oneormore == 1) {
				for (NewDateplanJpEntity newPeerPersionEntity : dateplanJpList) {
					newPeerPersionEntity.setFlightnum(newPeerPersionEntity.getFlight().getId() + "");

					/*if (!Util.isEmpty(newPeerPersionEntity.getId()) && newPeerPersionEntity.getId() > 0) {
					nutDao.update(newPeerPersionEntity);
					} else {*/
					newPeerPersionEntity.setTrip_jp_id(tripJp.getId());

					dbDao.insert(newPeerPersionEntity);
					//}
				}
				startdate = dateplanJpList.get(0).getStartdate();
				enddate = dateplanJpList.get(dateplanJpList.size() - 1).getStartdate();
				arrivecity = dateplanJpList.get(0).getArrivecity();
			}
		}
		List<NewTripplanJpEntity> tripplanJpList = order.getTripplanJpList();
		if (!Util.isEmpty(tripplanJpList) && tripplanJpList.size() > 0) {
			List<NewTripplanJpEntity> list2 = dbDao.query(NewTripplanJpEntity.class,
					Cnd.where("order_jp_id", "=", orderOld.getId()), null);
			if (!Util.isEmpty(list2) && list2.size() > 0) {

				dbDao.delete(list2);
			}
			for (NewTripplanJpEntity newPeerPersionEntity : tripplanJpList) {
				List<Scenic> scenics = newPeerPersionEntity.getScenics();
				String viewid = "";
				for (Scenic scenic : scenics) {
					viewid += scenic.getId() + ",";
				}
				newPeerPersionEntity.setViewid(viewid);
				/*	if (!Util.isEmpty(newPeerPersionEntity.getId()) && newPeerPersionEntity.getId() > 0) {
						nutDao.update(newPeerPersionEntity);
					} else {*/
				newPeerPersionEntity.setOrder_jp_id(orderOld.getId());

				dbDao.insert(newPeerPersionEntity);
				//}
			}
		} else {
			/*Calendar cNow = Calendar.getInstance();
			 Calendar cReturnDate = Calendar.getInstance();
			 cNow.setTime(startdate);
			 cReturnDate.setTime(enddate);
			 
			 long todayMs = cNow.getTimeInMillis();
			 long returnMs = cReturnDate.getTimeInMillis();
			 long intervalMs = todayMs - returnMs;*/
			/*
						Date nowdate = startdate;
						Calendar cal = Calendar.getInstance();
						cal.setTime(nowdate);
						int daynum = (int) ((enddate.getTime() - startdate.getTime()) / (24 * 60 * 60 * 1000)) + 1;
						List<Hotel> hotellist = dbDao.query(Hotel.class, Cnd.where("city", "=", arrivecity), null);
						List<Scenic> sceniclist = dbDao.query(Scenic.class, Cnd.where("city", "=", arrivecity), null);
						for (int i = 0; i < daynum; i++) {
							NewTripplanJpEntity t = new NewTripplanJpEntity();
							Calendar cal1 = Calendar.getInstance();
							cal1.setTime(nowdate);
							t.setDaynum(i + 1);
							t.setCity(arrivecity);
							t.setNowdate(nowdate);
							if (i < hotellist.size()) {

								t.setHotelid(hotellist.get(i).getId());
							}
							if (i < sceniclist.size()) {

								t.setViewid(sceniclist.get(i).getId() + ",");
							}

							t.setHometype(0);
							t.setHomenum(1);
							t.setHomeday(1);
							t.setIntime(nowdate);
							cal1.set(Calendar.DAY_OF_MONTH, nowdate.getDate() + 1);
							t.setOrder_jp_id(orderOld.getId());
							t.setOuttime(cal1.getTime());
							t.setBreakfast(1);
							t.setDinner(1);

							dbDao.insert(t);
							cal.set(Calendar.DAY_OF_MONTH, nowdate.getDate() + 1);
							nowdate = cal.getTime();
						}*/

			Date startdate1 = null;
			Date enddate1 = null;
			String arrivecity1 = null;
			NewTripJpEntity tripJp1 = orderOld.getTripJp();
			List<NewTripplanJpEntity> tripplanJpList1 = orderOld.getTripplanJpList();
			List<NewTripplanJpEntity> tripplanJpListnew1 = Lists.newArrayList();

			if (!Util.isEmpty(tripJp1)) {

				if (tripJp1.getOneormore() == 0) {
					startdate1 = tripJp1.getStartdate();
					enddate1 = tripJp1.getReturndate();
					arrivecity1 = tripJp1.getArrivecity();
					tripplan(orderOld, startdate1, enddate1, arrivecity1, tripplanJpListnew1);
				}

			}
			Integer oneormore1 = tripJp1.getOneormore();

			List<NewDateplanJpEntity> dateplanJpList1 = orderOld.getDateplanJpList();
			if (!Util.isEmpty(dateplanJpList1) && dateplanJpList1.size() > 0) {

				if (oneormore1 == 1) {
					for (int i = 0; i < dateplanJpList1.size(); i++) {
						if (i < dateplanJpList1.size() - 1) {

							startdate1 = dateplanJpList1.get(i).getStartdate();
							enddate1 = dateplanJpList1.get(i + 1).getStartdate();
							arrivecity1 = dateplanJpList1.get(i).getArrivecity();
							tripplan(orderOld, startdate1, enddate1, arrivecity1, tripplanJpListnew1);
						}
					}
				}
			}
			for (NewTripplanJpEntity newDateplanJpEntity : tripplanJpListnew1) {
				newDateplanJpEntity.setOrder_jp_id(orderOld.getId());
			}
			dbDao.insert(tripplanJpListnew1);
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

		int customerSource = order.getCustomerSource();
		if (!Util.isEmpty(customerSource) && customerSource > 0) {
			if (customerSource == OrderJapancustomersourceEnum.zhike.intKey()) {
				List<NewCustomerresourceJpEntity> customerresourceJp = dbDao.query(NewCustomerresourceJpEntity.class,
						Cnd.where("order_jp_id", "=", orderid), null);
				if (!Util.isEmpty(customerresourceJp) && customerresourceJp.size() > 0) {
					order.setCustomerresourceJp(customerresourceJp.get(0));
					CustomerManageEntity customerManageEntity = new CustomerManageEntity();
					customerManageEntity.setTelephone(customerresourceJp.get(0).getTelephone());
					customerManageEntity.setLinkman(customerresourceJp.get(0).getLinkman());
					customerManageEntity.setEmail(customerresourceJp.get(0).getEmail());
					customerManageEntity.setFullComName(customerresourceJp.get(0).getFullComName());
					order.setCustomermanage(customerManageEntity);
				}
			} else {
				//if (!Util.isEmpty(customermanage)) {

				CustomerManageEntity customerManageEntity = dbDao.fetch(CustomerManageEntity.class,
						Long.valueOf(order.getCustomer_manager_id()));
				if (!Util.isEmpty(customerManageEntity)) {
					order.setCustomermanage(customerManageEntity);
					NewCustomerresourceJpEntity customerresourceJp = new NewCustomerresourceJpEntity();
					order.setCustomerresourceJp(customerresourceJp);
				}

				//}

			}
		}

		List<NewTripJpEntity> newTrips = dbDao.query(NewTripJpEntity.class, Cnd.where("order_jp_id", "=", orderid),
				null);
		if (!Util.isEmpty(newTrips) && newTrips.size() > 0) {
			NewTripJpEntity newTripJpEntity = newTrips.get(0);
			String gofilght = newTripJpEntity.getFlightnum();
			String returnfilght = newTripJpEntity.getReturnflightnum();
			if (!Util.isEmpty(gofilght)) {
				Flight fetch = dbDao.fetch(Flight.class, Long.valueOf(gofilght));
				newTripJpEntity.setGofilght(fetch);
			}
			if (!Util.isEmpty(returnfilght)) {
				Flight fetch = dbDao.fetch(Flight.class, Long.valueOf(returnfilght));
				newTripJpEntity.setReturnfilght(fetch);
			}
			order.setTripJp(newTripJpEntity);
			List<NewDateplanJpEntity> query = dbDao.query(NewDateplanJpEntity.class,
					Cnd.where("trip_jp_id", "=", newTrips.get(0).getId()), null);

			for (NewDateplanJpEntity newDateplanJpEntity : query) {
				if (!Util.isEmpty(newDateplanJpEntity)) {
					String flightstr = newDateplanJpEntity.getFlightnum();
					if (!Util.isEmpty(flightstr)) {
						Flight fetch = dbDao.fetch(Flight.class, Long.valueOf(flightstr));
						newDateplanJpEntity.setFlight(fetch);
					}

				}
			}
			order.setDateplanJpList(query);
		}
		DateFormat df = new SimpleDateFormat("HH:mm");
		DateFormat df1 = new SimpleDateFormat("yyyy:MM:dd HH:mm");
		List<NewTripplanJpEntity> newPayPersionEntities = dbDao.query(NewTripplanJpEntity.class,
				Cnd.where("order_jp_id", "=", orderid), null);
		//给计划表中的每个对象相应的风景集合
		for (int i = 0; i < newPayPersionEntities.size(); i++) {
			NewTripplanJpEntity n = newPayPersionEntities.get(i);
			String viewid = n.getViewid();
			String view[] = viewid.split(",");
			List<Scenic> slist = Lists.newArrayList();
			for (int j = 0; j < view.length; j++) {
				if (!Util.isEmpty(view[j])) {
					Scenic fetch = dbDao.fetch(Scenic.class, Long.valueOf(view[j]));
					slist.add(fetch);
				}
			}
			n.setScenics(slist);
			if (!Util.isEmpty(n.getIntime())) {

				try {
					n.setIntime(df.parse(df1.format(n.getIntime())));
				} catch (ParseException e) {

					// TODO Auto-generated catch block
					e.printStackTrace();

				}
			}
			if (!Util.isEmpty(n.getOuttime())) {

				try {
					n.setOuttime(df.parse(df1.format(n.getOuttime())));
				} catch (ParseException e) {

					// TODO Auto-generated catch block
					e.printStackTrace();

				}
			}
		}

		if (!Util.isEmpty(newPayPersionEntities) && newPayPersionEntities.size() > 0) {
			order.setTripplanJpList(newPayPersionEntities);
		}
		List<NewFastmailJpEntity> newPayCompanyEntities = dbDao.query(NewFastmailJpEntity.class,
				Cnd.where("order_jp_id", "=", orderid), null);
		if (!Util.isEmpty(newPayCompanyEntities) && newPayCompanyEntities.size() > 0) {
			order.setFastMail(newPayCompanyEntities.get(0));
		}
		//============================
		String string = sqlManager.get("neworderjapan_porposerorder");
		Sql sql = Sqls.create(string);
		sql.setParam("orderid", orderid);
		List<NewProposerInfoJpEntity> proposerInfoJpList = DbSqlUtil.query(dbDao, NewProposerInfoJpEntity.class, sql);
		if (!Util.isEmpty(proposerInfoJpList) && proposerInfoJpList.size() > 0) {
			order.setProposerInfoJpList(proposerInfoJpList);
		}
		return order;
	}

	/**
	//	 * 将订单完善连接分享到用户邮箱或者手机
	//	 *
	//	 * @param request
	//	 * @param oid     订单ID
	//	 */
	@RequestMapping(value = "share")
	@ResponseBody
	public Object share(long customerid, String type) throws IOException {

		/*String href = RequestUtil.getServerPath(request) + "/m/delivery.html?oid=" + oid;*/
		NewCustomerJpEntity customer = null;
		NewOrderJpEntity order = null;
		long orderid = 0;
		if ("customer".equals(type)) {

			customer = dbDao.fetch(NewCustomerJpEntity.class, customerid);
			List<NewCustomerOrderJpEntity> query = dbDao.query(NewCustomerOrderJpEntity.class,
					Cnd.where("customer_jp_id", "=", customerid), null);
			orderid = query.get(0).getOrder_jp_id();
			order = dbDao.fetch(NewOrderJpEntity.class, orderid);
		}
		List<String> lines = IOUtils.readLines(getClass().getClassLoader().getResourceAsStream("mail.html"));
		StringBuilder tmp = new StringBuilder();
		for (String line : lines) {
			tmp.append(line);
		}
		//发送邮件前进行游客信息的注册
		String phone = customer.getPhone();

		EmployeeEntity employeeEntity = new EmployeeEntity();
		employeeEntity.setTelephone(phone);
		employeeEntity.setUserType(UserTypeEnum.TOURIST_IDENTITY.intKey());
		//生成六位数的随机密码
		String pwd = "";
		for (int i = 0; i < 6; i++) {
			int a = (int) (Math.random() * 10);
			pwd += a;

		}
		String temp = pwd;
		byte[] salt = Digests.generateSalt(8);
		employeeEntity.setSalt(Encodes.encodeHex(salt));
		byte[] password = pwd.getBytes();
		byte[] hashPassword = Digests.sha1(password, salt, 1024);
		pwd = Encodes.encodeHex(hashPassword);
		List<EmployeeEntity> query = dbDao.query(EmployeeEntity.class,
				Cnd.where("telephone", "=", phone).and("userType", "=", UserTypeEnum.TOURIST_IDENTITY.intKey()), null);
		employeeEntity.setPassword(pwd);
		if (!Util.isEmpty(query) && query.size() > 0) {
			employeeEntity.setId(query.get(0).getId());
			try {
				nutDao.update(employeeEntity);
			} catch (Exception e) {

				// TODO Auto-generated catch block
				e.printStackTrace();

			}
		} else {

			employeeEntity = dbDao.insert(employeeEntity);
		}

		String html = tmp.toString().replace("${name}", customer.getChinesexing() + customer.getChinesename())
				.replace("${oid}", order.getOrdernumber()).replace("${href}", "http://www.baidu.com")
				.replace("${logininfo}", "用户名:" + phone + "密码:" + temp);
		String result = mailService.send(customer.getEmail(), html, "签证资料录入", MailService.Type.HTML);
		if ("success".equalsIgnoreCase(result)) {
			//成功以后分享次数加1
			dbDao.update(
					NewCustomerJpEntity.class,
					Chain.make("sharecount", customer.getSharecount() + 1)
							.add("status", OrderVisaApproStatusEnum.writeInfo.intKey())
							.add("empid", employeeEntity.getId()), Cnd.where("id", "=", customer.getId()));

			return ResultObject.success(result);
		} else {
			return ResultObject.fail(result);
		}
	}

	/**
	//	 * 将订单完善连接分享到用户邮箱或者手机
	//	 *批量分享每个人看见自己的
	//	 * @param request
	//	 * @param oid     订单ID
	//	 */
	@RequestMapping(value = "shareall")
	@ResponseBody
	public Object shareall(long orderid, String type) throws IOException {
		List<String> lines = IOUtils.readLines(getClass().getClassLoader().getResourceAsStream("mail.html"));
		StringBuilder tmp = new StringBuilder();
		for (String line : lines) {
			tmp.append(line);
		}
		NewOrderJpEntity order = dbDao.fetch(NewOrderJpEntity.class, orderid);
		CustomerManageEntity customerManage = dbDao.fetch(CustomerManageEntity.class, order.getCustomer_manager_id());
		//发送邮件前进行游客信息的注册
		String phone = customerManage.getTelephone();
		//给订单的第一个人也发送
		/*String result = getMailContent(order, phone, null, customerManage);
		if ("success".equalsIgnoreCase(result)) {
			dbDao.update(NewOrderEntity.class, Chain.make("sharecountmany", order.getSharecountmany() + 1),
					Cnd.where("id", "=", orderid));
			//成功以后分享次数加1
		}
		*/
		List<NewCustomerOrderJpEntity> query = dbDao.query(NewCustomerOrderJpEntity.class,
				Cnd.where("order_jp_id", "=", orderid), null);

		for (NewCustomerOrderJpEntity newCustomerOrderEntity : query) {
			NewCustomerJpEntity customer = dbDao.fetch(NewCustomerJpEntity.class,
					newCustomerOrderEntity.getCustomer_jp_id());

			phone = customer.getPhone();
			if (!Util.isEmpty(phone)) {

				EmployeeEntity employeeEntity = new EmployeeEntity();
				employeeEntity.setTelephone(phone);
				employeeEntity.setUserType(UserTypeEnum.TOURIST_IDENTITY.intKey());
				employeeEntity.setFullName(customer.getChinesexing() + customer.getChinesename());
				//生成六位数的随机密码
				String pwd = "";
				for (int i = 0; i < 6; i++) {
					int a = (int) (Math.random() * 10);
					pwd += a;

				}
				String temp = pwd;
				byte[] salt = Digests.generateSalt(8);
				employeeEntity.setSalt(Encodes.encodeHex(salt));
				byte[] password = pwd.getBytes();
				byte[] hashPassword = Digests.sha1(password, salt, 1024);
				pwd = Encodes.encodeHex(hashPassword);

				employeeEntity.setPassword(pwd);
				List<EmployeeEntity> query1 = dbDao
						.query(EmployeeEntity.class,
								Cnd.where("telephone", "=", phone).and("userType", "=",
										UserTypeEnum.TOURIST_IDENTITY.intKey()), null);
				if (!Util.isEmpty(query1) && query1.size() > 0) {
					employeeEntity.setId(query1.get(0).getId());
					nutDao.update(employeeEntity);
				} else {

					employeeEntity = dbDao.insert(employeeEntity);
				}
				order.setUserid(employeeEntity.getId());
				dbDao.update(order, null);
				String html = tmp.toString().replace("${name}", customer.getChinesexing() + customer.getChinesename())
						.replace("${oid}", order.getOrdernumber()).replace("${href}", "http://www.baidu.com")
						.replace("${logininfo}", "用户名:" + phone + "密码:" + temp);
				String result = mailService.send(customer.getEmail(), html, "签证资料录入", MailService.Type.HTML);

				/*result = getMailContent(order, phone, customer, null);*/
				if ("success".equalsIgnoreCase(result)) {
					//成功以后分享次数加1
					dbDao.update(
							NewCustomerJpEntity.class,
							Chain.make("sharecount", customer.getSharecount() + 1)
									.add("status", OrderVisaApproStatusEnum.writeInfo.intKey())
									.add("empid", employeeEntity.getId()), Cnd.where("id", "=", customer.getId()));
					if (Util.isEmpty(order.getSharenum())) {
						order.setSharenum(0);
					}
					dbDao.update(
							NewOrderJpEntity.class,
							Chain.make("sharenum", order.getSharenum() + 1).add("status",
									OrderVisaApproStatusEnum.shared.intKey()), Cnd.where("id", "=", order.getId()));
				}
			}

		}
		return ResultObject.success("发送成功");
	}

	private String getMailContent(NewOrderEntity order, String phone, NewCustomerEntity customer,
			CustomerManageEntity customerManage) throws IOException {
		List<String> lines = IOUtils.readLines(getClass().getClassLoader().getResourceAsStream("mail.html"));
		StringBuilder tmp = new StringBuilder();
		for (String line : lines) {
			tmp.append(line);
		}

		EmployeeEntity employeeEntity = new EmployeeEntity();
		employeeEntity.setTelephone(phone);
		employeeEntity.setUserType(UserTypeEnum.TOURIST_IDENTITY.intKey());
		//生成六位数的随机密码
		String pwd = "";
		for (int i = 0; i < 6; i++) {
			int a = (int) (Math.random() * 10);
			pwd += a;

		}
		byte[] salt = Digests.generateSalt(8);
		employeeEntity.setSalt(Encodes.encodeHex(salt));
		byte[] password = pwd.getBytes();
		byte[] hashPassword = Digests.sha1(password, salt, 1024);
		pwd = Encodes.encodeHex(hashPassword);

		employeeEntity.setPassword(pwd);
		List<EmployeeEntity> query = dbDao.query(EmployeeEntity.class,
				Cnd.where("telephone", "=", phone).and("userType", "=", UserTypeEnum.TOURIST_IDENTITY.intKey()), null);
		if (!Util.isEmpty(query) && query.size() > 0) {
			employeeEntity.setId(query.get(0).getId());
			nutDao.update(employeeEntity);
		} else {

			dbDao.insert(employeeEntity);
		}
		String name = null;
		String email = null;
		if (Util.isEmpty(customer)) {
			name = customerManage.getLinkman();
			email = customerManage.getEmail();
		} else {
			name = customer.getChinesexing() + customer.getChinesename();
			email = customer.getEmail();
		}
		String html = tmp.toString().replace("${name}", name).replace("${oid}", order.getOrdernumber())
				.replace("${href}", "http://www.baidu.com").replace("${logininfo}", "用户名:" + phone + "密码:" + pwd);
		String result = mailService.send(email, html, "签证资料录入", MailService.Type.HTML);
		return result;
	}

	/***
	 * 
	 *日本的递送回显
	 * @param orderid
	 * @return TODO(这里描述每个参数,如果有返回值描述返回值,如果有异常描述异常)
	 */

	@RequestMapping(value = "deliveryusa")
	@ResponseBody
	public Object deliveryusa(long orderid) {
		NewDeliveryJapanEntity deliveryUSA = dbDao.fetch(NewDeliveryJapanEntity.class,
				Cnd.where("order_jp_id", "=", orderid));

		return deliveryUSA;
	}

	/***
	 * 
	 *日本的递送保存
	 * @param orderid
	 * @return TODO(这里描述每个参数,如果有返回值描述返回值,如果有异常描述异常)
	 */

	@RequestMapping(value = "deliveryJpsave")
	@ResponseBody
	public Object deliveryJpsave(@RequestBody NewDeliveryJapanEntity deliveryJapan, int orderid) {
		deliveryJapan.setOrder_jp_id(orderid);
		Integer id = deliveryJapan.getId();
		if (!Util.isEmpty(id) && id > 0) {
			dbDao.update(NewOrderJpEntity.class,
					Chain.make("updatetime", new Date()).add("status", OrderVisaApproStatusEnum.DS.intKey()),
					Cnd.where("id", "=", orderid));
			List<NewCustomerOrderJpEntity> query = dbDao.query(NewCustomerOrderJpEntity.class,
					Cnd.where("order_jp_id", "=", orderid), null);

			for (NewCustomerOrderJpEntity newCustomerOrderJpEntity : query) {
				long customerid = newCustomerOrderJpEntity.getCustomer_jp_id();

				dbDao.update(
						NewCustomerJpEntity.class,
						Chain.make("updatetime", new Date()).add("status",
								OrderVisaApproStatusEnum.readySubmit.intKey()), Cnd.where("id", "=", customerid));

			}
			dbDao.update(deliveryJapan, null);
		} else {

			dbDao.update(NewOrderJpEntity.class,
					Chain.make("updatetime", new Date()).add("status", OrderVisaApproStatusEnum.DS.intKey()),
					Cnd.where("id", "=", orderid));
			List<NewCustomerOrderJpEntity> query = dbDao.query(NewCustomerOrderJpEntity.class,
					Cnd.where("order_jp_id", "=", orderid), null);

			for (NewCustomerOrderJpEntity newCustomerOrderJpEntity : query) {
				long customerid = newCustomerOrderJpEntity.getCustomer_jp_id();

				dbDao.update(
						NewCustomerJpEntity.class,
						Chain.make("updatetime", new Date()).add("status",
								OrderVisaApproStatusEnum.readySubmit.intKey()), Cnd.where("id", "=", customerid));

			}

			dbDao.insert(deliveryJapan);
		}
		return ResultObject.success("添加成功");
	}

	/**
	 * 导出日本所需资料的pdf
	 *
	 * @param oid
	 */
	@RequestMapping(value = "export")
	@ResponseBody
	public Object export(HttpServletResponse resp, long orderid) throws IOException {
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
		List<NewCustomerJpEntity> customerJpList = Lists.newArrayList();
		List<NewCustomerOrderJpEntity> query = dbDao.query(NewCustomerOrderJpEntity.class,
				Cnd.where("order_jp_id", "=", orderid), null);
		for (NewCustomerOrderJpEntity newCustomerOrderJpEntity : query) {
			NewCustomerJpEntity customer = dbDao.fetch(NewCustomerJpEntity.class,
					newCustomerOrderJpEntity.getCustomer_jp_id());

			List<NewWorkinfoJpEntity> passportlose = dbDao.query(NewWorkinfoJpEntity.class,
					Cnd.where("customer_jp_id", "=", customer.getId()), null);
			if (!Util.isEmpty(passportlose) && passportlose.size() > 0) {
				customer.setWorkinfoJp(passportlose.get(0));
			} else {
				customer.setWorkinfoJp(new NewWorkinfoJpEntity());

			}
			List<NewOldpassportJpEntity> oldname = dbDao.query(NewOldpassportJpEntity.class,
					Cnd.where("customer_jp_id", "=", customer.getId()), null);
			if (!Util.isEmpty(oldname) && oldname.size() > 0) {
				customer.setOldpassportJp(oldname.get(0));
			} else {
				customer.setOldpassportJp(new NewOldpassportJpEntity());
			}
			List<NewFinanceJpEntity> orthercountry = dbDao.query(NewFinanceJpEntity.class,
					Cnd.where("customer_jp_id", "=", customer.getId()), null);
			if (!Util.isEmpty(orthercountry) && orthercountry.size() > 0) {
				customer.setFinanceJpList(orthercountry);
			}
			List<NewOldnameJpEntity> father = dbDao.query(NewOldnameJpEntity.class,
					Cnd.where("customer_jp_id", "=", customer.getId()), null);
			if (!Util.isEmpty(father) && father.size() > 0) {
				customer.setOldnameJp(father.get(0));
			} else {
				customer.setOldnameJp(new NewOldnameJpEntity());

			}

			List<NewOrthercountryJpEntity> relation = dbDao.query(NewOrthercountryJpEntity.class,
					Cnd.where("customer_jp_id", "=", customer.getId()), null);
			if (!Util.isEmpty(relation) && relation.size() > 0) {
				customer.setOrthercountryJpList(relation);
			}

			List<NewRecentlyintojpJpEntity> teachinfo = dbDao.query(NewRecentlyintojpJpEntity.class,
					Cnd.where("customer_jp_id", "=", customer.getId()), null);
			if (!Util.isEmpty(teachinfo) && teachinfo.size() > 0) {
				customer.setRecentlyintojpJpList(teachinfo);
			}

			if (!Util.isEmpty(customer)) {
				customerJpList.add(customer);
			}
		}
		order.setCustomerJpList(customerJpList);
		if (order == null) {
			return ResultObject.fail("订单不存在!");
		}
		byte[] bytes = newPdfService.export(order).toByteArray();
		String fileName = URLEncoder.encode(customerManageEntity.getLinkman() + "-" + orderid + ".zip", "UTF-8");
		resp.setContentType("application/zip");
		resp.addHeader("Content-Disposition", "attachment;filename=" + fileName);// 设置文件名
		IOUtils.write(bytes, resp.getOutputStream());
		return ResultObject.fail("PDF生成失败!");
	}

	@RequestMapping(value = "autogenerate")
	@ResponseBody
	public Object autogenerate(@RequestBody NewOrderJpEntity order) {
		if (!Util.isEmpty(order.getId()) && order.getId() > 0) {

		} else {
			Date startdate = null;
			Date enddate = null;
			String arrivecity = null;
			NewTripJpEntity tripJp = order.getTripJp();
			List<NewTripplanJpEntity> tripplanJpList = order.getTripplanJpList();
			List<NewTripplanJpEntity> tripplanJpListnew = Lists.newArrayList();

			if (!Util.isEmpty(tripJp)) {

				if (tripJp.getOneormore() == 0) {
					startdate = tripJp.getStartdate();
					enddate = tripJp.getReturndate();
					arrivecity = tripJp.getArrivecity();
					tripplan(order, startdate, enddate, arrivecity, tripplanJpListnew);
				}

			}
			Integer oneormore = tripJp.getOneormore();

			List<NewDateplanJpEntity> dateplanJpList = order.getDateplanJpList();
			if (!Util.isEmpty(dateplanJpList) && dateplanJpList.size() > 0) {

				if (oneormore == 1) {
					for (int i = 0; i < dateplanJpList.size(); i++) {
						if (i < dateplanJpList.size() - 1) {

							startdate = dateplanJpList.get(i).getStartdate();
							enddate = dateplanJpList.get(i + 1).getStartdate();
							arrivecity = dateplanJpList.get(i).getArrivecity();
							tripplan(order, startdate, enddate, arrivecity, tripplanJpListnew);
						}
					}
				}
			}
			/*	if (!Util.isEmpty(tripplanJpList) && tripplanJpList.size() > 0) {
				
			} else {
				Calendar cNow = Calendar.getInstance();
				 Calendar cReturnDate = Calendar.getInstance();
				 cNow.setTime(startdate);
				 cReturnDate.setTime(enddate);
				 
				 long todayMs = cNow.getTimeInMillis();
				 long returnMs = cReturnDate.getTimeInMillis();
				 long intervalMs = todayMs - returnMs;*/

			order.setTripplanJpList(tripplanJpListnew);
		}
		return order;
	}

	private void tripplan(NewOrderJpEntity order, Date startdate, Date enddate, String arrivecity,
			List<NewTripplanJpEntity> tripplanJpListnew) {
		Date nowdate = startdate;
		Calendar cal = Calendar.getInstance();
		cal.setTime(nowdate);
		int daynum = (int) ((enddate.getTime() - startdate.getTime()) / (24 * 60 * 60 * 1000)) + 1;
		List<Hotel> hotellist = dbDao.query(Hotel.class, Cnd.where("city", "=", arrivecity), null);
		List<Scenic> sceniclist = dbDao.query(Scenic.class, Cnd.where("city", "=", arrivecity), null);
		int daynumnew = 1;
		for (int i = 0; i < daynum; i++) {
			NewTripplanJpEntity t = new NewTripplanJpEntity();
			//Calendar cal1 = Calendar.getInstance();
			cal.setTime(nowdate);
			t.setDaynum(daynumnew);
			t.setCity(arrivecity);
			t.setNowdate(nowdate);
			if (i < hotellist.size()) {

				t.setHotelid(hotellist.get(0).getId());
			}
			if (i < sceniclist.size()) {

				t.setViewid(sceniclist.get(i).getId() + ",");
			}
			String view[] = t.getViewid().split(",");
			List<Scenic> slist = Lists.newArrayList();
			for (int j = 0; j < view.length; j++) {
				if (!Util.isEmpty(view[j])) {
					Scenic fetch = dbDao.fetch(Scenic.class, Long.valueOf(view[j]));
					slist.add(fetch);
				}
			}
			t.setScenics(slist);
			t.setHometype(0);
			if (!Util.isEmpty(order.getHeadnum())) {
				int home = (int) Math.ceil(order.getHeadnum() / 2.0);
				t.setHomenum(home);
			} else {
				t.setHomenum(1);

			}
			t.setHomeday(1);
			//t.setIntime(nowdate);
			//cal1.set(Calendar.DAY_OF_MONTH, nowdate.getDate() + 1);
			/*t.setOrder_jp_id(orderOld.getId());*/
			//t.setOuttime(cal1.getTime());
			t.setBreakfast(1);
			t.setDinner(1);
			tripplanJpListnew.add(t);

			daynumnew++;
			cal.set(Calendar.DAY_OF_MONTH, nowdate.getDate() + 1);
			nowdate = cal.getTime();
		}
	}

	/*	
		 //根据地点筛选航班
		
		@RequestMapping(value = "deliveryJpsave")
		@ResponseBody
		public Object deliveryJpsave() {
			deliveryJapan.setOrder_jp_id(orderid);
			Integer id = deliveryJapan.getId();
			if (!Util.isEmpty(id) && id > 0) {

				dbDao.update(deliveryJapan, null);
			} else {
				dbDao.insert(deliveryJapan);
			}
			return ResultObject.success("添加成功");
		}*/

	@RequestMapping(value = "autothree")
	@ResponseBody
	public Object autothree(@RequestBody NewOrderJpEntity order) {

		NewTripJpEntity tripJp = order.getTripJp();
		if (!Util.isEmpty(tripJp)) {
			Integer oneormore = tripJp.getOneormore();
			if (!Util.isEmpty(oneormore)) {

				List<NewDateplanJpEntity> dateplanJpList = Lists.newArrayList();
				if (oneormore == 1) {
					for (int i = 0; i < 3; i++) {
						NewDateplanJpEntity n = new NewDateplanJpEntity();
						dateplanJpList.add(n);
					}
				} else if (oneormore == 0) {
					dateplanJpList.clear();
				}
				order.setDateplanJpList(dateplanJpList);

			}

		}

		return order;
	}

	@RequestMapping(value = "autoporposer")
	@ResponseBody
	public Object autoporposer(@RequestBody NewOrderJpEntity order) {

		List<NewProposerInfoJpEntity> proposerInfoJpList = Lists.newArrayList();
		Integer headnum = order.getHeadnum();
		if (!Util.isEmpty(headnum) && headnum > 0) {
			for (int i = 0; i < headnum; i++) {
				NewProposerInfoJpEntity np = new NewProposerInfoJpEntity();
				np.setRelation(1);
				proposerInfoJpList.add(np);
			}
		}
		order.setProposerInfoJpList(proposerInfoJpList);
		return order;
	}

	@RequestMapping(value = "porposerdatasource")
	@ResponseBody
	public Object porposerdatasource(String orderid) {
		if ("" == orderid || null == orderid || "null".equals(orderid)) {
			orderid = 0 + "";
		}
		long orderId = Long.valueOf(orderid);
		List<NewProposerInfoJpEntity> query = dbDao.query(NewProposerInfoJpEntity.class,
				Cnd.where("ismainproposer", "=", 1).and("order_jp_id", "=", orderId), null);
		return query;
		/*	return query;*/
	}
}
