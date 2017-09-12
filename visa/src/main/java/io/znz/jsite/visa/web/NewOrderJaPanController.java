/**
 * NewOrderJaPanController.java
 * io.znz.jsite.visa.web
 * Copyright (c) 2017, 北京科技有限公司版权所有.
 */

package io.znz.jsite.visa.web;

import io.znz.jsite.base.bean.ResultObject;
import io.znz.jsite.core.entity.companyjob.CompanyJobEntity;
import io.znz.jsite.core.service.MailService;
import io.znz.jsite.core.util.Const;
import io.znz.jsite.download.UploadService;
import io.znz.jsite.exception.JSiteException;
import io.znz.jsite.util.SpringUtil;
import io.znz.jsite.util.security.Digests;
import io.znz.jsite.util.security.Encodes;
import io.znz.jsite.visa.bean.Flight;
import io.znz.jsite.visa.bean.Hotel;
import io.znz.jsite.visa.bean.Scenic;
import io.znz.jsite.visa.entity.customer.CustomerManageEntity;
import io.znz.jsite.visa.entity.delivery.NewDeliveryJapanEntity;
import io.znz.jsite.visa.entity.japan.NewComeBabyJpDjsEntity;
import io.znz.jsite.visa.entity.japan.NewComeBabyJpEntity;
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
import io.znz.jsite.visa.enums.CompanyTypeEnum;
import io.znz.jsite.visa.enums.GenderEnum;
import io.znz.jsite.visa.enums.OrderJapancustomersourceEnum;
import io.znz.jsite.visa.enums.OrderVisaApproStatusEnum;
import io.znz.jsite.visa.enums.UserTypeEnum;
import io.znz.jsite.visa.enums.VisaJapanApproStatusEnum;
import io.znz.jsite.visa.form.NewOrderJapanSqlForm;
import io.znz.jsite.visa.newpdf.NewPdfService;
import io.znz.jsite.visa.newpdf.NewTemplate;
import io.znz.jsite.visa.service.NewOrderJaPanService;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.google.common.collect.Lists;
import com.ibm.icu.util.Calendar;
import com.sun.star.lang.IllegalArgumentException;
import com.uxuexi.core.common.util.Util;
import com.uxuexi.core.db.dao.IDbDao;
import com.uxuexi.core.db.util.DbSqlUtil;

/**
 * 
 * <p>
 * 
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

	@Autowired
	private UploadService qiniuUploadService;//文件上传
	/**
	 * 注入容器中的sqlManager对象，用于获取sql
	 */
	@Autowired
	protected SqlManager sqlManager;

	/**
	 * 列表查看订单信息
	 */
	private int daynum = 1;//出行的天数

	private int daytotal = 0;

	private List<String> validateEmptyList = Lists.newArrayList();
	private List<String> validateMsgList = Lists.newArrayList();

	@RequestMapping(value = "list")
	@ResponseBody
	public Object list(@RequestBody NewOrderJapanSqlForm form, final HttpSession session) {
		CompanyJobEntity company = (CompanyJobEntity) session.getAttribute(Const.USER_COMPANY_KEY);
		if (!Util.isEmpty(company)) {
			long comId = company.getComId();
			form.setComId(comId);
		}
		io.znz.jsite.core.entity.EmployeeEntity user = (io.znz.jsite.core.entity.EmployeeEntity) session
				.getAttribute(Const.SESSION_NAME);
		if (!Util.isEmpty(user)) {
			int userType = user.getUserType();
			form.setUserType(userType);
		}
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
	 * @return 
	 */
	@RequestMapping(value = "childList")
	@ResponseBody
	public Object childList(long orderId) {

		Cnd cnd = Cnd.NEW();
		String sqlString = sqlManager.get("newcustomerjapan_list");
		Sql sql = Sqls.create(sqlString);
		sql.setParam("orderId", orderId);
		List<NewCustomerJpEntity> query = DbSqlUtil.query(dbDao, NewCustomerJpEntity.class, sql);

		return query;
	}

	/*****
	 * 
	 * 日本订单的保存
	 *
	 * @param customers
	 * @param customer
	 * @return 
	 */
	@RequestMapping(value = "orderJpsave")
	@ResponseBody
	@Transactional
	public Object orderJpsave(@RequestBody NewOrderJpEntity order, final HttpSession session) {

		CompanyJobEntity company = (CompanyJobEntity) session.getAttribute(Const.USER_COMPANY_KEY);
		if (!Util.isEmpty(company)) {

			long comId = company.getComId();
			order.setComId(comId);
		}

		io.znz.jsite.core.entity.EmployeeEntity user = (io.znz.jsite.core.entity.EmployeeEntity) session
				.getAttribute(Const.SESSION_NAME);
		if (!Util.isEmpty(user)) {
			order.setOperatePersonId(user.getId());
		}
		//根据他们的id是否存在判断是更新还是删除
		NewOrderJpEntity orderOld = order;
		if (!Util.isEmpty(order.getId()) && order.getId() > 0) {
			NewOrderJpEntity orderNew = dbDao.fetch(NewOrderJpEntity.class, order.getId());
			order.setUpdatetime(new Date());
			nutDao.update(order);
			//人数
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
			SimpleDateFormat smf = new SimpleDateFormat("yyMMdd");
			String format = smf.format(new Date());
			String sqlString = sqlManager.get("neworderjapan_ordernum");
			Sql sql = Sqls.create(sqlString);
			List<Record> query = dbDao.query(sql, null, null);
			int sum = 1;
			if (!Util.isEmpty(query) && query.size() > 0) {
				String string = query.get(0).getString("ordernumber");
				int a = Integer.valueOf(string.substring(9, string.length()));
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
			String ordernum = format + "-JP" + sum1;

			order.setOrdernumber(ordernum);
			//设置为日本
			order.setCountrytype(1);
			orderOld = dbDao.insert(order);
			//根据人数插入多个申请人的数据
			List<NewProposerInfoJpEntity> proposerInfoJpList = order.getProposerInfoJpList();
			if (Util.isEmpty(proposerInfoJpList) && proposerInfoJpList.size() == 0) {
				for (int i = 0; i < orderOld.getHeadnum(); i++) {

					NewProposerInfoJpEntity p = new NewProposerInfoJpEntity();
					proposerInfoJpList.add(p);
				}
				order.setProposerInfoJpList(proposerInfoJpList);
			}
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
				//单程
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

					if (proposerInfoJpList.get(j).getId().intValue() == list8.get(i).getId().intValue()) {
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
					boolean relation = newPeerPersionEntity.getIsMainProposer();
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
					boolean relation = newPeerPersionEntity.getIsMainProposer();
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
		//从客户端接收的多程的行程安排
		List<NewDateplanJpEntity> dateplanJpList = order.getDateplanJpList();
		//查库的信息
		List<NewDateplanJpEntity> list1 = dbDao.query(NewDateplanJpEntity.class,
				Cnd.where("trip_jp_id", "=", tripJp.getId()), null);

		//判断客户端传来的数据是否填写
		if (!Util.isEmpty(dateplanJpList) && dateplanJpList.size() > 0) {
			//判断数据库中如果有数据，删除
			if (!Util.isEmpty(list1) && list1.size() > 0) {

				dbDao.delete(list1);
			}
			//客户端传来的信息为多程
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
		//从客户端接收的单程的行程安排
		List<NewTripplanJpEntity> tripplanJpList = order.getTripplanJpList();
		//判断客户端是否填写行程安排
		if (!Util.isEmpty(tripplanJpList) && tripplanJpList.size() > 0) {
			List<NewTripplanJpEntity> list2 = dbDao.query(NewTripplanJpEntity.class,
					Cnd.where("order_jp_id", "=", orderOld.getId()), null);
			//查库是否有行程安排，如果有，删除
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
		} else {//客户端没填写行程安排的情况
			//获取客户端单程的出行信息
			NewTripJpEntity tripJp1 = order.getTripJp();
			if (!Util.isEmpty(tripJp1.getOneormore())) {
				int oneormore2 = tripJp1.getOneormore().intValue();
				//如果为单程
				if (oneormore2 == 0) {
					if (!Util.isEmpty(tripJp1.getStartdate())) {
						List<NewTripplanJpEntity> query = dbDao.query(NewTripplanJpEntity.class,
								Cnd.where("order_jp_id", "=", order.getId()), null);
						//如果库中有行程安排
						if (query.size() > 0) {

						} else {
							startdate = null;
							enddate = null;
							arrivecity = null;
							tripJp = order.getTripJp();
							tripplanJpList = order.getTripplanJpList();
							List<NewTripplanJpEntity> tripplanJpListnew = Lists.newArrayList();

							if (!Util.isEmpty(tripJp)) {

								if (tripJp.getOneormore() == 0) {
									startdate = tripJp.getStartdate();
									enddate = tripJp.getReturndate();
									arrivecity = tripJp.getArrivecity();
									this.daytotal = (int) ((enddate.getTime() - startdate.getTime()) / (24 * 60 * 60 * 1000)) + 1;
									newTripplan(order, startdate, enddate, arrivecity, tripplanJpListnew);
									this.daynum = 1;
								}

							}
							oneormore = tripJp.getOneormore();

							dateplanJpList = order.getDateplanJpList();
							if (!Util.isEmpty(dateplanJpList) && dateplanJpList.size() > 0) {

								if (oneormore == 1) {
									this.daytotal = (int) ((dateplanJpList.get(dateplanJpList.size() - 1)
											.getStartdate().getTime() - dateplanJpList.get(0).getStartdate().getTime()) / (24 * 60 * 60 * 1000)) + 1;
									for (int i = 0; i < dateplanJpList.size(); i++) {
										if (i < dateplanJpList.size() - 1) {

											startdate = dateplanJpList.get(i).getStartdate();
											enddate = dateplanJpList.get(i + 1).getStartdate();
											arrivecity = dateplanJpList.get(i).getArrivecity();
											if (i < dateplanJpList.size() - 2) {
												Calendar cal = Calendar.getInstance();
												cal.setTime(enddate);
												cal.add(Calendar.DATE, -1);
												enddate = cal.getTime();
											}
											newTripplan(order, startdate, enddate, arrivecity, tripplanJpListnew);
										}
									}
									this.daynum = 1;
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
							if (!Util.isEmpty(tripplanJpListnew) && tripplanJpListnew.size() > 0) {
								for (NewTripplanJpEntity newPeerPersionEntity : tripplanJpListnew) {
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
							}
						}
					}
				} else if (oneormore2 == 1) {

					List<NewDateplanJpEntity> query = dbDao.query(NewDateplanJpEntity.class,
							Cnd.where("trip_jp_id", "=", tripJp1.getId()), null);
					if (!Util.isEmpty(query) && query.size() > 0) {
						if (!Util.isEmpty(query.get(0).getStartdate())) {
							List<NewTripplanJpEntity> query1 = dbDao.query(NewTripplanJpEntity.class,
									Cnd.where("order_jp_id", "=", order.getId()), null);
							if (query1.size() > 0) {

							} else {
								startdate = null;
								enddate = null;
								arrivecity = null;
								tripJp = order.getTripJp();
								tripplanJpList = order.getTripplanJpList();
								List<NewTripplanJpEntity> tripplanJpListnew = Lists.newArrayList();

								if (!Util.isEmpty(tripJp)) {

									if (tripJp.getOneormore() == 0) {
										startdate = tripJp.getStartdate();
										enddate = tripJp.getReturndate();
										arrivecity = tripJp.getArrivecity();
										this.daytotal = (int) ((enddate.getTime() - startdate.getTime()) / (24 * 60 * 60 * 1000)) + 1;
										newTripplan(order, startdate, enddate, arrivecity, tripplanJpListnew);
										this.daynum = 1;
									}

								}
								oneormore = tripJp.getOneormore();

								dateplanJpList = order.getDateplanJpList();
								if (!Util.isEmpty(dateplanJpList) && dateplanJpList.size() > 0) {

									if (oneormore == 1) {
										this.daytotal = (int) ((dateplanJpList.get(dateplanJpList.size() - 1)
												.getStartdate().getTime() - dateplanJpList.get(0).getStartdate()
												.getTime()) / (24 * 60 * 60 * 1000)) + 1;
										for (int i = 0; i < dateplanJpList.size(); i++) {
											if (i < dateplanJpList.size() - 1) {

												startdate = dateplanJpList.get(i).getStartdate();
												enddate = dateplanJpList.get(i + 1).getStartdate();
												arrivecity = dateplanJpList.get(i).getArrivecity();
												if (i < dateplanJpList.size() - 2) {
													Calendar cal = Calendar.getInstance();
													cal.setTime(enddate);
													cal.add(Calendar.DATE, -1);
													enddate = cal.getTime();
												}
												newTripplan(order, startdate, enddate, arrivecity, tripplanJpListnew);
											}
										}
										this.daynum = 1;
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
								if (!Util.isEmpty(tripplanJpListnew) && tripplanJpListnew.size() > 0) {
									for (NewTripplanJpEntity newPeerPersionEntity : tripplanJpListnew) {
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
								}
							}

						}
					}

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
	 * @return 
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

					e.printStackTrace();

				}
			}
			if (!Util.isEmpty(n.getOuttime())) {

				try {
					n.setOuttime(df.parse(df1.format(n.getOuttime())));
				} catch (ParseException e) {

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
		boolean isexist = false;
		if (!Util.isEmpty(query) && query.size() > 0) {
			/*	employeeEntity.setId(query.get(0).getId());
				try {
					nutDao.update(employeeEntity);
				} catch (Exception e) {

					e.printStackTrace();

				}*/
			isexist = true;
		} else {

			employeeEntity = dbDao.insert(employeeEntity);
		}
		Integer gender = customer.getGender();
		String genderStr = "";
		if (gender.intValue() == GenderEnum.female.intKey()) {
			genderStr = "女士";
		} else if (gender.intValue() == GenderEnum.man.intKey()) {
			genderStr = "先生";

		}
		String logininfo = "";
		if (isexist) {

			logininfo += "用户名:" + phone;
		} else {
			logininfo += "用户名:" + phone + "密码:" + temp;
		}
		String html = tmp.toString().replace("${name}", customer.getChinesexing() + customer.getChinesename())
				.replace("${oid}", order.getOrdernumber()).replace("${href}", "http://218.244.148.21:9004/")
				.replace("${logininfo}", logininfo).replace("${gender}", genderStr);
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
		String phone = "";
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
		String nullEmail = "";
		List<NewCustomerJpEntity> customerList = Lists.newArrayList();
		boolean togetherLinkman = false;
		long togetherLinkmanId = 0;
		for (NewCustomerOrderJpEntity newCustomerOrderEntity : query) {
			NewCustomerJpEntity customer = dbDao.fetch(NewCustomerJpEntity.class,
					newCustomerOrderEntity.getCustomer_jp_id());

			customerList.add(customer);

		}
		List<NewProposerInfoJpEntity> proposerInfoJpList = dbDao.query(NewProposerInfoJpEntity.class,
				Cnd.where("order_jp_id", "=", orderid), null);
		for (NewProposerInfoJpEntity newProposerInfoJpEntity : proposerInfoJpList) {
			boolean istogetherlinkman = newProposerInfoJpEntity.isIstogetherlinkman();
			if (istogetherlinkman) {
				togetherLinkman = true;
				togetherLinkmanId = newProposerInfoJpEntity.getCustomer_jp_id();
			}
		}
		List<NewCustomerJpEntity> customerNew = Lists.newArrayList();
		//for (NewCustomerJpEntity customer : customerList) {
		if (togetherLinkman) {
			if (togetherLinkmanId > 0) {
				NewCustomerJpEntity customerLinkMan = dbDao.fetch(NewCustomerJpEntity.class, togetherLinkmanId);
				if (Util.isEmpty(customerLinkMan.getEmail())) {
					nullEmail += "统一联系人" + customerLinkMan.getChinesexing() + customerLinkMan.getChinesename()
							+ "邮箱为空,";
				}
				if (Util.isEmpty(customerLinkMan.getPhone())) {

					nullEmail += "手机为空。";
				}
				customerNew.add(customerLinkMan);
			}
		} else {
			for (NewCustomerJpEntity newProposerInfoJpEntity : customerList) {
				if (Util.isEmpty(newProposerInfoJpEntity.getEmail())) {
					nullEmail += newProposerInfoJpEntity.getChinesexing() + newProposerInfoJpEntity.getChinesename()
							+ "邮箱为空,";
				}
				if (Util.isEmpty(newProposerInfoJpEntity.getPhone())) {

					nullEmail += "手机为空。";
				}
			}
		}
		//}

		if (Util.isEmpty(nullEmail)) {

			//给统一联系人发所有人的信息
			if (togetherLinkman) {
				String customerinfo = "";
				NewCustomerJpEntity customerLinkMan = dbDao.fetch(NewCustomerJpEntity.class, togetherLinkmanId);
				for (NewCustomerJpEntity customer : customerList) {
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
						List<EmployeeEntity> query1 = dbDao.query(
								EmployeeEntity.class,
								Cnd.where("telephone", "=", phone).and("userType", "=",
										UserTypeEnum.TOURIST_IDENTITY.intKey()), null);
						boolean isexist = false;
						if (!Util.isEmpty(query1) && query1.size() > 0) {
							/*	employeeEntity.setId(query1.get(0).getId());
								nutDao.update(employeeEntity);*/
							isexist = true;
						} else {

							employeeEntity = dbDao.insert(employeeEntity);
							order.setUserid(employeeEntity.getId());
							dbDao.update(order, null);
						}

						if (isexist) {

							customerinfo += "用户名:" + phone;
						} else {
							customerinfo += "用户名:" + phone + "密码:" + temp + "<br\\>";
						}
						/*result = getMailContent(order, phone, customer, null);*/
						//if ("success".equalsIgnoreCase(result)) {
						//成功以后分享次数加1
						if (isexist) {

							dbDao.update(
									NewCustomerJpEntity.class,
									Chain.make("sharecount", customer.getSharecount() + 1).add("status",
											OrderVisaApproStatusEnum.writeInfo.intKey()),
									Cnd.where("id", "=", customer.getId()));
						} else {

							dbDao.update(
									NewCustomerJpEntity.class,
									Chain.make("sharecount", customer.getSharecount() + 1)
											.add("status", OrderVisaApproStatusEnum.writeInfo.intKey())
											.add("empid", employeeEntity.getId()),
									Cnd.where("id", "=", customer.getId()));
						}
						if (Util.isEmpty(order.getSharenum())) {
							order.setSharenum(0);
						}
						dbDao.update(
								NewOrderJpEntity.class,
								Chain.make("sharenum", order.getSharenum() + 1).add("status",
										OrderVisaApproStatusEnum.shared.intKey()), Cnd.where("id", "=", order.getId()));
						//}
					}
				}
				Integer gender = customerLinkMan.getGender();
				String genderStr = "";
				if (gender.intValue() == GenderEnum.female.intKey()) {
					genderStr = "女士";
				} else if (gender.intValue() == GenderEnum.man.intKey()) {
					genderStr = "先生";

				}
				String html = tmp.toString()
						.replace("${name}", customerLinkMan.getChinesexing() + customerLinkMan.getChinesename())
						.replace("${oid}", order.getOrdernumber()).replace("${href}", "http://218.244.148.21:9004/")
						.replace("${logininfo}", customerinfo).replace("${gender}", genderStr);
				String result = mailService.send(customerLinkMan.getEmail(), html, "签证资料录入", MailService.Type.HTML);
			} else {

				for (NewCustomerJpEntity customer : customerList) {
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
						List<EmployeeEntity> query1 = dbDao.query(
								EmployeeEntity.class,
								Cnd.where("telephone", "=", phone).and("userType", "=",
										UserTypeEnum.TOURIST_IDENTITY.intKey()), null);
						boolean isexist = false;//用来判断是更新还是新增
						if (!Util.isEmpty(query1) && query1.size() > 0) {
							isexist = true;
							/*employeeEntity.setId(query1.get(0).getId());
							nutDao.update(employeeEntity);*/
						} else {

							employeeEntity = dbDao.insert(employeeEntity);
							order.setUserid(employeeEntity.getId());
							dbDao.update(order, null);
						}

						Integer gender = customer.getGender();
						String genderStr = "";
						if (gender.intValue() == GenderEnum.female.intKey()) {
							genderStr = "女士";
						} else if (gender.intValue() == GenderEnum.man.intKey()) {
							genderStr = "先生";

						}
						String logininfo = "";
						if (isexist) {

							logininfo += "用户名:" + phone;
						} else {
							logininfo += "用户名:" + phone + "密码:" + temp;
						}
						String html = tmp.toString()
								.replace("${name}", customer.getChinesexing() + customer.getChinesename())
								.replace("${oid}", order.getOrdernumber())
								.replace("${href}", "http://218.244.148.21:9004/").replace("${logininfo}", logininfo)
								.replace("${gender}", genderStr);
						String result = mailService.send(customer.getEmail(), html, "签证资料录入", MailService.Type.HTML);

						/*result = getMailContent(order, phone, customer, null);*/
						if ("success".equalsIgnoreCase(result)) {
							//成功以后分享次数加1
							if (isexist) {

								dbDao.update(
										NewCustomerJpEntity.class,
										Chain.make("sharecount", customer.getSharecount() + 1).add("status",
												OrderVisaApproStatusEnum.writeInfo.intKey()),
										Cnd.where("id", "=", customer.getId()));
							} else {

								dbDao.update(
										NewCustomerJpEntity.class,
										Chain.make("sharecount", customer.getSharecount() + 1)
												.add("status", OrderVisaApproStatusEnum.writeInfo.intKey())
												.add("empid", employeeEntity.getId()),
										Cnd.where("id", "=", customer.getId()));
							}
							if (Util.isEmpty(order.getSharenum())) {
								order.setSharenum(0);
							}
							dbDao.update(
									NewOrderJpEntity.class,
									Chain.make("sharenum", order.getSharenum() + 1).add("status",
											OrderVisaApproStatusEnum.shared.intKey()),
									Cnd.where("id", "=", order.getId()));
						}
					}
				}
			}

		} else {
			return ResultObject.fail(nullEmail);
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
				.replace("${href}", "http://218.244.148.21:9004/")
				.replace("${logininfo}", "用户名:" + phone + "密码:" + pwd);
		String result = mailService.send(email, html, "签证资料录入", MailService.Type.HTML);
		return result;
	}

	/***
	 * 
	 *日本的递送回显
	 * @param orderid
	 * @return 
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
	 * @return 
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
		//		CustomerManageEntity customerManageEntity = null;
		//		if(){
		//			
		//		}
		//		dbDao.fetch(CustomerManageEntity.class,
		//				Long.valueOf(order.getCustomer_manager_id()));
		//		
		CustomerManageEntity customerManageEntity = null;
		int customerSource = order.getCustomerSource();
		if (!Util.isEmpty(customerSource) && customerSource > 0) {
			if (customerSource == OrderJapancustomersourceEnum.zhike.intKey()) {
				List<NewCustomerresourceJpEntity> customerresourceJp = dbDao.query(NewCustomerresourceJpEntity.class,
						Cnd.where("order_jp_id", "=", orderid), null);
				if (!Util.isEmpty(customerresourceJp) && customerresourceJp.size() > 0) {
					order.setCustomerresourceJp(customerresourceJp.get(0));
					customerManageEntity = new CustomerManageEntity();
					customerManageEntity.setTelephone(customerresourceJp.get(0).getTelephone());
					customerManageEntity.setLinkman(customerresourceJp.get(0).getLinkman());
					customerManageEntity.setEmail(customerresourceJp.get(0).getEmail());
					customerManageEntity.setFullComName(customerresourceJp.get(0).getFullComName());
					order.setCustomermanage(customerManageEntity);
				}
			} else {
				//if (!Util.isEmpty(customermanage)) {

				customerManageEntity = dbDao.fetch(CustomerManageEntity.class,
						Long.valueOf(order.getCustomer_manager_id()));
				if (!Util.isEmpty(customerManageEntity)) {
					order.setCustomermanage(customerManageEntity);
					NewCustomerresourceJpEntity customerresourceJp = new NewCustomerresourceJpEntity();
					order.setCustomerresourceJp(customerresourceJp);
				}

				//}

			}
		}

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

			//TODO
			List<NewRecentlyintojpJpEntity> teachinfo = dbDao.query(NewRecentlyintojpJpEntity.class,
					Cnd.where("customer_jp_id", "=", customer.getId()), null);
			if (!Util.isEmpty(teachinfo) && teachinfo.size() > 0) {
				customer.setRecentlyintojpJpList(teachinfo);
			}

			if (!Util.isEmpty(customer)) {
				customerJpList.add(customer);
			}
		}
		Collections.sort(customerJpList, new Comparator<NewCustomerJpEntity>() {

			@Override
			public int compare(NewCustomerJpEntity o1, NewCustomerJpEntity o2) {
				String timeO1 = o1.getChinesefullname();
				String timeO2 = o2.getChinesefullname();
				if (!Util.isEmpty(timeO1) && !Util.isEmpty(timeO2)) {
					return timeO2.compareTo(timeO1);
				}

				if (timeO1 == null && timeO2 == null) {
					return 0;
				}
				if (timeO1 == null) {
					return -1;
				}
				if (timeO2 == null) {
					return -1;
				}
				return 0;

			}

		});

		order.setCustomerJpList(customerJpList);
		if (order == null) {
			return ResultObject.fail("订单不存在!");
		}
		byte[] bytes = newPdfService.export(order).toByteArray();
		String str = "";
		customerSource = order.getCustomerSource();
		if (customerSource == OrderJapancustomersourceEnum.zhike.intKey()) {
			List<NewCustomerresourceJpEntity> query2 = dbDao.query(NewCustomerresourceJpEntity.class,
					Cnd.where("order_jp_id", "=", orderid), null);
			if (!Util.isEmpty(query2) && query2.size() > 0) {
				str = query2.get(0).getLinkman();
			}
		} else {
			if (!Util.isEmpty(customerManageEntity)) {
				str = customerManageEntity.getLinkman();
			}
		}
		String fileName = URLEncoder.encode(str + "-" + order.getOrdernumber() + ".zip", "UTF-8");
		resp.setContentType("application/zip");
		resp.setHeader("Set-Cookie", "fileDownload=true; path=/");
		resp.addHeader("Content-Disposition", "attachment;filename=" + fileName);// 设置文件名
		IOUtils.write(bytes, resp.getOutputStream());
		return ResultObject.fail("PDF生成失败!");
	}

	@RequestMapping(value = "autogenerate")
	@ResponseBody
	public Object autogenerate(@RequestBody NewOrderJpEntity order) throws IllegalArgumentException {
		//long startTime = System.currentTimeMillis(); //获取开始时间
		long orderId = order.getId();
		if (0 >= orderId) {
			//新增
			autoGenerateNew(order);
		} else {
			//编辑
			autoGenerateEdit(order);
		}
		//long endTime = System.currentTimeMillis(); //获取结束时间

		//System.out.println("程序运行时间：" + (endTime - startTime) + "ms");
		return order;
	}

	//add
	private void autoGenerateNew(NewOrderJpEntity order) {
		NewTripJpEntity tripJpClient = order.getTripJp();
		if (!Util.isEmpty(tripJpClient.getOneormore())) {
			//行程类型
			int tripTypeClient = tripJpClient.getOneormore().intValue();
			//单程
			if (tripTypeClient == 0) {

				Date startdate = null;
				Date enddate = null;
				String arrivecity = null;
				NewTripJpEntity tripJp = order.getTripJp();
				List<NewTripplanJpEntity> tripplanJpListnew = Lists.newArrayList();

				if (!Util.isEmpty(tripJp)) {
					if (tripJp.getOneormore() == 0) {
						startdate = tripJp.getStartdate();
						enddate = tripJp.getReturndate();
						arrivecity = tripJp.getArrivecity();
						this.daytotal = (int) ((enddate.getTime() - startdate.getTime()) / (24 * 60 * 60 * 1000)) + 1;
						newTripplan(order, startdate, enddate, arrivecity, tripplanJpListnew);
						this.daynum = 1;
					}
				}
				order.setTripplanJpList(tripplanJpListnew);

			} else if (tripTypeClient == 1) {

				Date startdate = null;
				Date enddate = null;
				String arrivecity = null;

				List<NewDateplanJpEntity> dateplanJpListClient = order.getDateplanJpList();
				//天数
				this.daytotal = (int) ((dateplanJpListClient.get(dateplanJpListClient.size() - 1).getStartdate()
						.getTime() - dateplanJpListClient.get(0).getStartdate().getTime()) / (24 * 60 * 60 * 1000)) + 1;

				List<NewTripplanJpEntity> tripplanJpListnew = Lists.newArrayList();
				for (int i = 0; i < dateplanJpListClient.size(); i++) {
					if (i < dateplanJpListClient.size() - 1) {

						startdate = dateplanJpListClient.get(i).getStartdate();
						enddate = dateplanJpListClient.get(i + 1).getStartdate();
						arrivecity = dateplanJpListClient.get(i).getArrivecity();

						if (i < dateplanJpListClient.size() - 2) {
							Calendar cal = Calendar.getInstance();
							cal.setTime(enddate);
							cal.add(Calendar.DATE, -1);
							enddate = cal.getTime();
						}
						newTripplan(order, startdate, enddate, arrivecity, tripplanJpListnew);
					}
				}
				this.daynum = 1;
				order.setTripplanJpList(tripplanJpListnew);

			}//行程类型判断结束
		}
	}

	private void autoGenerateEdit(NewOrderJpEntity order) throws IllegalArgumentException {
		long orderId = order.getId();
		NewOrderJpEntity orderDb = dbDao.fetch(NewOrderJpEntity.class, Cnd.where("id", "=", orderId));
		if (Util.isEmpty(orderDb)) {
			throw new IllegalArgumentException("订单不存在");
		} else {
			//编辑
			NewTripJpEntity tripJp_db = dbDao
					.fetch(NewTripJpEntity.class, Cnd.where("order_jp_id", "=", order.getId()));

			NewTripJpEntity tripJpClient = order.getTripJp();
			if (!Util.isEmpty(tripJpClient.getOneormore())) {
				//行程类型
				int tripTypeClient = tripJpClient.getOneormore().intValue();
				//单程
				if (tripTypeClient == 0) {
					//数据库中的行程类型
					if (!Util.isEmpty(tripJp_db)) {

						//如果行程条件没变 直接用数据库中的
						if (tripJpClient.equals(tripJp_db)) {
							//如果数据库中已经存在
							List<NewTripplanJpEntity> tripListDb = dbDao.query(NewTripplanJpEntity.class,
									Cnd.where("order_jp_id", "=", order.getId()), null);
							List<NewTripplanJpEntity> tripplanJpListnew = null;

							if (tripListDb.size() > 0) {
								tripplanJpListnew = tripListDb;
								String arrivecity = null;
								NewTripJpEntity tripJp = order.getTripJp();
								arrivecity = tripJp.getArrivecity();
								editTripplan(arrivecity, tripplanJpListnew);
							} else {
								//新增 do nothing
							}
							order.setTripplanJpList(tripplanJpListnew);
						} else {
							Date startdate = null;
							Date enddate = null;
							String arrivecity = null;
							NewTripJpEntity tripJp = order.getTripJp();
							List<NewTripplanJpEntity> tripplanJpListnew = Lists.newArrayList();

							if (!Util.isEmpty(tripJp)) {
								if (tripJp.getOneormore() == 0) {
									startdate = tripJp.getStartdate();
									enddate = tripJp.getReturndate();
									arrivecity = tripJp.getArrivecity();
									this.daytotal = (int) ((enddate.getTime() - startdate.getTime()) / (24 * 60 * 60 * 1000)) + 1;
									newTripplan(order, startdate, enddate, arrivecity, tripplanJpListnew);
									this.daynum = 1;
								}
							} else {
								autoGenerateNew(order);
							}
							order.setTripplanJpList(tripplanJpListnew);
						}

						//多程
					}
				} else if (tripTypeClient == 1) {
					//出行信息
					List<NewDateplanJpEntity> dateplanJpListInDb = dbDao.query(NewDateplanJpEntity.class,
							Cnd.where("trip_jp_id", "=", tripJpClient.getId()), null);

					//页面上的出行信息
					List<NewDateplanJpEntity> dateplanJpListClient = order.getDateplanJpList();
					if (!Util.isEmpty(dateplanJpListInDb)) {
						//只要条件改变全部新增，否则 取数据库中的数据使用
						if (dateplanJpListInDb.containsAll(dateplanJpListClient)) {
							if (!Util.isEmpty(dateplanJpListInDb) && dateplanJpListInDb.size() > 0) {
								if (!Util.isEmpty(dateplanJpListInDb.get(0).getStartdate())) {
									//已经保存的行程安排
									List<NewTripplanJpEntity> tripPlansInDb = dbDao.query(NewTripplanJpEntity.class,
											Cnd.where("order_jp_id", "=", order.getId()), null);

									List<NewTripplanJpEntity> tripplanJpList = null;

									tripplanJpList = tripPlansInDb;
									String arrivecity = null;
									NewTripJpEntity tripJp = order.getTripJp();
									arrivecity = tripJp.getArrivecity();
									editTripplan(arrivecity, tripplanJpList);
								}
							}
						} else {
							Date startdate = null;
							Date enddate = null;
							String arrivecity = null;

							//天数
							this.daytotal = (int) ((dateplanJpListClient.get(dateplanJpListClient.size() - 1)
									.getStartdate().getTime() - dateplanJpListClient.get(0).getStartdate().getTime()) / (24 * 60 * 60 * 1000)) + 1;

							List<NewTripplanJpEntity> tripplanJpListnew = Lists.newArrayList();
							for (int i = 0; i < dateplanJpListClient.size(); i++) {
								if (i < dateplanJpListClient.size() - 1) {

									startdate = dateplanJpListClient.get(i).getStartdate();
									enddate = dateplanJpListClient.get(i + 1).getStartdate();
									arrivecity = dateplanJpListClient.get(i).getArrivecity();

									if (i < dateplanJpListClient.size() - 2) {
										Calendar cal = Calendar.getInstance();
										cal.setTime(enddate);
										cal.add(Calendar.DATE, -1);
										enddate = cal.getTime();
									}
									newTripplan(order, startdate, enddate, arrivecity, tripplanJpListnew);
								}
							}
							this.daynum = 1;
							order.setTripplanJpList(tripplanJpListnew);
						}
					} else {
						autoGenerateNew(order);
					}
				} //行程类判断结束

			}
		}
	}

	private void newTripplan(NewOrderJpEntity order, Date startdate, Date enddate, String arrivecity,
			List<NewTripplanJpEntity> tripplanJpListnew) {
		Date nowdate = startdate;
		DateFormat df = new SimpleDateFormat("yyyyMMdd");
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
			t.setDaynum(this.daynum);//daynum 第几天
			t.setCity(arrivecity);
			t.setNowdate(nowdate);
			if (this.daynum == this.daytotal) {
				t.setHometype(-1);//hometyp 房间类型
				if (!Util.isEmpty(hotellist)) {
					int hotelFoot = new Random().nextInt(10000) % hotellist.size();
					t.setHotelid(hotellist.get(hotelFoot).getId());
				}
			} else {

				if (i < hotellist.size() && hotellist.size() > 0) {
					if (!Util.isEmpty(hotellist)) {
						int hotelFoot = new Random().nextInt(10000) % hotellist.size();
						t.setHotelid(hotellist.get(hotelFoot).getId());
					}
				}
				t.setHometype(0);
				if (!Util.isEmpty(order.getHeadnum())) {//headnum 人数
					int home = (int) Math.ceil(order.getHeadnum() / 2.0);
					t.setHomenum(home);
				} else {
					t.setHomenum(1);

				}
				t.setHomeday(1);//homeday住几晚
			}

			String viewIdStr = "";
			for (int f = 0; f < 4 && f < sceniclist.size(); f++) {
				int scenicFoot = new Random().nextInt(10000) % sceniclist.size();
				int scenicId = sceniclist.get(scenicFoot).getId();
				viewIdStr += scenicId + ",";
			}
			t.setViewid(viewIdStr);
			if (sceniclist.size() > 0) {
				String viewid = t.getViewid();
				if (!Util.isEmpty(viewid)) {
					String view[] = t.getViewid().split(",");
					List<Scenic> slist = Lists.newArrayList();
					for (int j = 0; j < view.length; j++) {
						if (!Util.isEmpty(view[j])) {
							Scenic fetch = dbDao.fetch(Scenic.class, Long.valueOf(view[j]));
							slist.add(fetch);
						}
					}
					t.setScenics(slist);
				}
			}

			//t.setIntime(nowdate);
			//cal1.set(Calendar.DAY_OF_MONTH, nowdate.getDate() + 1);
			/*t.setOrder_jp_id(orderOld.getId());*/
			//t.setOuttime(cal1.getTime());
			t.setBreakfast(1);
			t.setDinner(1);
			tripplanJpListnew.add(t);

			daynumnew++;
			this.daynum++;
			cal.add(Calendar.DATE, 1);
			nowdate = cal.getTime();
		}
	}

	private void editTripplan(String arrivecity, List<NewTripplanJpEntity> tripplanJpList) {
		List<Scenic> sceniclist = dbDao.query(Scenic.class, Cnd.where("city", "=", arrivecity), null);
		if (!Util.isEmpty(tripplanJpList)) {
			for (NewTripplanJpEntity t : tripplanJpList) {
				if (sceniclist.size() > 0) {
					String view[] = t.getViewid().split(",");
					List<Scenic> slist = Lists.newArrayList();
					for (int j = 0; j < view.length; j++) {
						if (!Util.isEmpty(view[j])) {
							Scenic fetch = dbDao.fetch(Scenic.class, Long.valueOf(view[j]));
							slist.add(fetch);
						}
					}
					t.setScenics(slist);
				}

				//t.setIntime(nowdate);
				//cal1.set(Calendar.DAY_OF_MONTH, nowdate.getDate() + 1);
				/*t.setOrder_jp_id(orderOld.getId());*/
				//t.setOuttime(cal1.getTime());
				t.setBreakfast(1);
				t.setDinner(1);
			}
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

	@RequestMapping(value = "downloadselect")
	@ResponseBody
	public Object downloadselect(String orderid) {
		if ("" == orderid || null == orderid || "null".equals(orderid)) {
			orderid = 0 + "";
		}
		long orderId = Long.valueOf(orderid);
		NewOrderJpEntity order = dbDao.fetch(NewOrderJpEntity.class, orderId);
		return order;
	}

	@RequestMapping(value = "downloadselectsave")
	@ResponseBody
	public Object downloadselectsave(@RequestBody NewOrderJpEntity order) {
		if (!Util.isEmpty(order)) {
			dbDao.update(order, null);
		}
		return ResultObject.success("保存成功");
	}

	@RequestMapping(value = "downloadselectsavenew")
	@ResponseBody
	public Object downloadselectsavenew(long orderid, final HttpSession session, long sendComId, long landComId) {
		NewOrderJpEntity order1 = dbDao.fetch(NewOrderJpEntity.class, orderid);
		if (!Util.isEmpty(order1)) {

			dbDao.update(NewOrderJpEntity.class, Chain.make("sendComId", sendComId).add("landComId", landComId),
					Cnd.where("id", "=", order1.getId()));
			order1 = dbDao.fetch(NewOrderJpEntity.class, order1.getId());
		}
		return ResultObject.success("保存成功");
	}

	@RequestMapping(value = "downloadselectfind")
	@ResponseBody
	public Object downloadselectfind(int a, final HttpSession session) {
		List<NewComeBabyJpEntity> comeList = Lists.newArrayList();
		List<NewComeBabyJpDjsEntity> comeList1 = Lists.newArrayList();

		CompanyJobEntity company = (CompanyJobEntity) session.getAttribute(Const.USER_COMPANY_KEY);
		if (!Util.isEmpty(company)) {
			long comId = company.getComId();
			if (a == 1) {
				comeList = dbDao.query(NewComeBabyJpEntity.class,
						Cnd.where("comType", "=", CompanyTypeEnum.send.intKey()).and("comId", "=", comId), null);
				return comeList;

			} else if (a == 2) {
				comeList1 = dbDao.query(NewComeBabyJpDjsEntity.class, null, null);

				return comeList1;
			}
		} else {
			if (a == 2) {
				comeList1 = dbDao.query(NewComeBabyJpDjsEntity.class, null, null);

				return comeList1;
			}
			if (a == 1) {
				comeList = dbDao.query(NewComeBabyJpEntity.class,
						Cnd.where("comType", "=", CompanyTypeEnum.send.intKey()).and("comId", "=", -1), null);
				return comeList;

			}
		}
		return new ArrayList();
	}

	@RequestMapping(value = "validate")
	@ResponseBody
	public Object validate(long orderid, final HttpSession session) {
		validateEmptyList.clear();
		validateMsgList.clear();

		/*	NewOrderJpEntity order1 = dbDao.fetch(NewOrderJpEntity.class, orderid);
			if (!Util.isEmpty(order1)) {

				dbDao.update(NewOrderJpEntity.class, Chain.make("sendComId", sendComId).add("landComId", landComId),
						Cnd.where("id", "=", order1.getId()));
				order1 = dbDao.fetch(NewOrderJpEntity.class, order1.getId());
			}*/

		if (!Util.isEmpty(orderid) && orderid > 0) {
			List<NewCustomerOrderJpEntity> customerList = dbDao.query(NewCustomerOrderJpEntity.class,
					Cnd.where("order_jp_id", "=", orderid), null);
			if (!Util.isEmpty(customerList) && customerList.size() > 0) {

				for (NewCustomerOrderJpEntity newCustomerOrderJpEntity : customerList) {

					addValidate(newCustomerOrderJpEntity);
				}

			}
		}
		//处理出行信息中出入境时间
		if (!Util.isEmpty(orderid) && orderid > 0) {
			NewOrderJpEntity order = dbDao.fetch(NewOrderJpEntity.class, orderid);
			List<NewTripJpEntity> list = dbDao.query(NewTripJpEntity.class, Cnd.where("order_jp_id", "=", orderid),
					null);
			if (!Util.isEmpty(list.get(0))) {
				NewTripJpEntity tripJpEntity = list.get(0);
				int oneormore = tripJpEntity.getOneormore();
				if (oneormore == 0) {
					Date startdate = tripJpEntity.getStartdate();
					if (!Util.isEmpty(startdate)) {

					} else {

						validateEmptyList.add("出发日期");
					}
					Date returndate = tripJpEntity.getReturndate();
					if (!Util.isEmpty(returndate)) {

					} else {

						validateEmptyList.add("返回日期");
					}
				} else if (oneormore == 1) {
					List<NewDateplanJpEntity> dateplanList = dbDao.query(NewDateplanJpEntity.class,
							Cnd.where("trip_jp_id", "=", tripJpEntity.getId()), null);
					if (!Util.isEmpty(dateplanList) && dateplanList.size() > 0) {
						Date startdate = dateplanList.get(0).getStartdate();
						if (!Util.isEmpty(startdate)) {

						} else {

							validateEmptyList.add("出发日期");
						}
						Date returndate = dateplanList.get(dateplanList.size() - 1).getStartdate();
						if (!Util.isEmpty(returndate)) {

						} else {

							validateEmptyList.add("返回日期");
						}
					}
				}
			}

		}

		String msg = "";
		if (!Util.isEmpty(validateEmptyList) && validateEmptyList.size() > 0) {
			if (!Util.isEmpty(validateEmptyList) && validateEmptyList.size() > 0) {
				for (String string : validateEmptyList) {
					msg += string + "、";
				}
				msg += "不能为空！";
			}

		}
		if (Util.isEmpty(validateMsgList) && validateMsgList.size() > 0) {
			for (String string : validateMsgList) {
				msg += string;
			}
		}
		if (Util.isEmpty(validateEmptyList) && Util.isEmpty(validateMsgList)) {
			List<NewCustomerOrderJpEntity> customerOrderList = dbDao.query(NewCustomerOrderJpEntity.class,
					Cnd.where("order_jp_id", "=", orderid), null);
			//增加excel的上传
			//=============================开始=========================================================
			NewOrderJpEntity order = dbDao.fetch(NewOrderJpEntity.class, orderid);

			//		CustomerManageEntity customerManageEntity = null;
			//		if(){
			//			
			//		}
			//		dbDao.fetch(CustomerManageEntity.class,
			//				Long.valueOf(order.getCustomer_manager_id()));
			//		
			CustomerManageEntity customerManageEntity = null;
			int customerSource = order.getCustomerSource();
			if (!Util.isEmpty(customerSource) && customerSource > 0) {
				if (customerSource == OrderJapancustomersourceEnum.zhike.intKey()) {
					List<NewCustomerresourceJpEntity> customerresourceJp = dbDao.query(
							NewCustomerresourceJpEntity.class, Cnd.where("order_jp_id", "=", orderid), null);
					if (!Util.isEmpty(customerresourceJp) && customerresourceJp.size() > 0) {
						order.setCustomerresourceJp(customerresourceJp.get(0));
						customerManageEntity = new CustomerManageEntity();
						customerManageEntity.setTelephone(customerresourceJp.get(0).getTelephone());
						customerManageEntity.setLinkman(customerresourceJp.get(0).getLinkman());
						customerManageEntity.setEmail(customerresourceJp.get(0).getEmail());
						customerManageEntity.setFullComName(customerresourceJp.get(0).getFullComName());
						order.setCustomermanage(customerManageEntity);
					}
				} else {
					//if (!Util.isEmpty(customermanage)) {

					customerManageEntity = dbDao.fetch(CustomerManageEntity.class,
							Long.valueOf(order.getCustomer_manager_id()));
					if (!Util.isEmpty(customerManageEntity)) {
						order.setCustomermanage(customerManageEntity);
						NewCustomerresourceJpEntity customerresourceJp = new NewCustomerresourceJpEntity();
						order.setCustomerresourceJp(customerresourceJp);
					}

					//}

				}
			}

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

				//TODO
				List<NewRecentlyintojpJpEntity> teachinfo = dbDao.query(NewRecentlyintojpJpEntity.class,
						Cnd.where("customer_jp_id", "=", customer.getId()), null);
				if (!Util.isEmpty(teachinfo) && teachinfo.size() > 0) {
					customer.setRecentlyintojpJpList(teachinfo);
				}

				if (!Util.isEmpty(customer)) {
					customerJpList.add(customer);
				}
			}
			Collections.sort(customerJpList, new Comparator<NewCustomerJpEntity>() {

				@Override
				public int compare(NewCustomerJpEntity o1, NewCustomerJpEntity o2) {
					String timeO1 = o1.getChinesefullname();
					String timeO2 = o2.getChinesefullname();
					if (!Util.isEmpty(timeO1) && !Util.isEmpty(timeO2)) {
						return timeO2.compareTo(timeO1);
					}

					if (timeO1 == null && timeO2 == null) {
						return 0;
					}
					if (timeO1 == null) {
						return -1;
					}
					if (timeO2 == null) {
						return -1;
					}
					return 0;

				}

			});

			order.setCustomerJpList(customerJpList);
			//设置日本excel的上传
			NewTemplate t = SpringUtil.getBean(order.getTemplate());
			File createTempFile = this.createTempFile(t.excel(order));
			InputStream is = null;
			try {
				is = new FileInputStream(createTempFile);
			} catch (FileNotFoundException e) {

				e.printStackTrace();

			}
			String uploadImage = qiniuUploadService.uploadImage(is, "xlsx", null);
			String url = io.znz.jsite.visa.util.Const.IMAGES_SERVER_ADDR + uploadImage;
			order.setExcelurl(url);
			order.setStatus(VisaJapanApproStatusEnum.readySubmit.intKey());
			dbDao.update(order, null);
			//================================结束======================================================

			/*	for (NewCustomerOrderJpEntity newCustomerOrderJpEntity : customerOrderList) {
					long customerid = newCustomerOrderJpEntity.getCustomer_jp_id();

					dbDao.update(
							NewCustomerJpEntity.class,
							Chain.make("updatetime", new Date()).add("status",
									OrderVisaApproStatusEnum.readySubmit.intKey()), Cnd.where("id", "=", customerid));

				}*/

			return ResultObject.success("无错误");
		} else {
			return ResultObject.fail(msg);

		}
		/*else {
			List<NewCustomerOrderJpEntity> query = dbDao.query(NewCustomerOrderJpEntity.class,
					Cnd.where("order_jp_id", "=", orderid), null);

			for (NewCustomerOrderJpEntity newCustomerOrderJpEntity : query) {
				long customerid = newCustomerOrderJpEntity.getCustomer_jp_id();

				dbDao.update(
						NewCustomerJpEntity.class,
						Chain.make("updatetime", new Date()).add("status",
								OrderVisaApproStatusEnum.readySubmit.intKey()), Cnd.where("id", "=", customerid));

			}

			return ResultObject.success("无错误");
		}*/

	}

	private void addValidate(NewCustomerOrderJpEntity newCustomerOrderJpEntity) {
		long customer_jp_id = newCustomerOrderJpEntity.getCustomer_jp_id();
		long orderid = newCustomerOrderJpEntity.getOrder_jp_id();
		//每个客户的其他验证
		if (!Util.isEmpty(customer_jp_id) && customer_jp_id > 0) {
			NewCustomerJpEntity customer = dbDao.fetch(NewCustomerJpEntity.class, customer_jp_id);
			String chinesename = customer.getChinesename();
			if (!Util.isEmpty(chinesename)) {

			} else {

				validateEmptyList.add("中文名");
			}
			String chinesexing = customer.getChinesexing();
			if (!Util.isEmpty(chinesexing)) {

			} else {

				validateEmptyList.add("中文姓");
			}
			String chinesenameen = customer.getChinesenameen();
			if (!Util.isEmpty(chinesenameen)) {

			} else {

				validateEmptyList.add("中文名英文");
			}
			String chinesexingen = customer.getChinesexingen();
			if (!Util.isEmpty(chinesexingen)) {

			} else {

				validateEmptyList.add("中文姓英文");
			}
			String nowcity = customer.getNowcity();

			if (!Util.isEmpty(nowcity)) {

			} else {

				validateEmptyList.add("居住地城市");
			}
			Integer gender = customer.getGender();
			if (!Util.isEmpty(gender)) {

			} else {

				validateEmptyList.add("性别");
			}
			Date birthdate = customer.getBirthday();
			if (!Util.isEmpty(birthdate)) {

			} else {

				validateEmptyList.add("出生日期");
			}
		}

	}

	//生成临时文件
	private File createTempFile(ByteArrayOutputStream out) {
		try {
			File tmp = new File(FileUtils.getTempDirectory() + "/" + UUID.randomUUID().toString() + ".tmp");
			FileUtils.writeByteArrayToFile(tmp, out.toByteArray());
			IOUtils.closeQuietly(out);
			return tmp;
		} catch (Exception e) {
			e.printStackTrace();
			throw new JSiteException("临时文件输出异常!");
		}
	}

	/**
	 * 上传文件
	 */
	@RequestMapping(value = "uploadFile")
	@ResponseBody
	public Object uploadFile(HttpServletRequest request) throws Exception {

		request.setCharacterEncoding(io.znz.jsite.visa.util.Const.CHARACTER_ENCODING_PROJECT);//字符编码为utf-8
		//		response.setCharacterEncoding(Const.CHARACTER_ENCODING_PROJECT);
		/*	FileInputStream is = new FileInputStream(Filedata);
		String ext = FileUtil.getSuffix(Filedata);
		String str = Filedata.getName();
		String shortUrl = qiniuUploadService.uploadImage(is, ext, null);
		String url = Const.IMAGES_SERVER_ADDR + shortUrl;*/
		String url = null;
		long startTime = System.currentTimeMillis();
		//将当前上下文初始化给  CommonsMutipartResolver （多部分解析器）
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession()
				.getServletContext());
		//检查form中是否有enctype="multipart/form-data"
		if (multipartResolver.isMultipart(request)) {
			//将request变成多部分request
			MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
			//获取multiRequest 中所有的文件名
			Iterator iter = multiRequest.getFileNames();

			while (iter.hasNext()) {
				//一次遍历所有文件
				MultipartFile file = multiRequest.getFile(iter.next().toString());
				if (file != null) {
					//					String contentType = file.getContentType();
					String originalFilename = file.getOriginalFilename();
					//					String name = file.getName();
					String ext = originalFilename.substring(originalFilename.indexOf(".") + 1,
							originalFilename.length());
					InputStream is = file.getInputStream();

					String shortUrl = qiniuUploadService.uploadImage(is, ext, null);
					url = io.znz.jsite.visa.util.Const.IMAGES_SERVER_ADDR + shortUrl;

					System.out.println(url);
				}

			}

		}
		return ResultObject.success(url);

	}

	/**
	 * 上传文件之后的保存
	 */
	@RequestMapping(value = "uploadFileSave")
	@ResponseBody
	public Object uploadFileSave(HttpServletRequest request, long orderid, String fileurl) throws Exception {
		if (!Util.isEmpty(orderid) && orderid > 0) {

			dbDao.update(NewOrderJpEntity.class, Chain.make("fileurl", fileurl), Cnd.where("id", "=", orderid));
			return ResultObject.success("上传成功");
		}
		/*	long endTime = System.currentTimeMillis();
			System.out.println("方法三的运行时间：" + String.valueOf(endTime - startTime) + "ms");*/
		return ResultObject.fail("上传失败");

	}
}
