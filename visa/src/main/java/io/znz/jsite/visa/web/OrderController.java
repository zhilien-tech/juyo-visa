package io.znz.jsite.visa.web;

import io.znz.jsite.base.BaseController;
import io.znz.jsite.base.bean.ResultObject;
import io.znz.jsite.core.service.MailService;
import io.znz.jsite.util.security.Digests;
import io.znz.jsite.util.security.Encodes;
import io.znz.jsite.visa.bean.Customer;
import io.znz.jsite.visa.entity.customer.CustomerManageEntity;
import io.znz.jsite.visa.entity.delivery.NewDeliveryUSAEntity;
import io.znz.jsite.visa.entity.usa.NewCustomerEntity;
import io.znz.jsite.visa.entity.usa.NewCustomerOrderEntity;
import io.znz.jsite.visa.entity.usa.NewCustomerresourceEntity;
import io.znz.jsite.visa.entity.usa.NewFastMailEntity;
import io.znz.jsite.visa.entity.usa.NewOrderEntity;
import io.znz.jsite.visa.entity.usa.NewPayCompanyEntity;
import io.znz.jsite.visa.entity.usa.NewPayPersionEntity;
import io.znz.jsite.visa.entity.usa.NewPeerPersionEntity;
import io.znz.jsite.visa.entity.usa.NewTrip;
import io.znz.jsite.visa.entity.user.EmployeeEntity;
import io.znz.jsite.visa.enums.InterviewTimeEnum;
import io.znz.jsite.visa.enums.OrderJapancustomersourceEnum;
import io.znz.jsite.visa.enums.OrderVisaApproStatusEnum;
import io.znz.jsite.visa.enums.SendPassportEnum;
import io.znz.jsite.visa.enums.UserTypeEnum;
import io.znz.jsite.visa.form.KenDoTestSqlForm;
import io.znz.jsite.visa.service.OrderService;
import io.znz.jsite.visa.service.PdfService;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

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

import com.uxuexi.core.common.util.Util;
import com.uxuexi.core.db.dao.IDbDao;
import com.uxuexi.core.db.util.DbSqlUtil;

/**
 * Created by Chaly on 2017/3/23.
 */
@Controller
@RequestMapping("visa/order")
public class OrderController extends BaseController {
	@Autowired
	private OrderService orderService;
	@Autowired
	private MailService mailService;
	@Autowired
	private PdfService pdfService;

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
	public Object list(@RequestBody KenDoTestSqlForm form) {
		/*User user = UserUtil.getUser();
		if (user == null) {
			throw new JSiteException("请登录后再试!");
		}
		PageFilter filter = null;
		if (filter == null)
			filter = new PageFilter();
		Pageable pageable = filter.getPageable();
		Junction filters = Restrictions.conjunction(filter.getFilter(Order.class));
		//如果不是管理员则按照登录人查看订单,如果是管理员则差看所有
		if (!SecurityUtils.getSubject().hasRole("admin")) {
			filters = filters.add(Restrictions.in("user.id", Lists.newArrayList(user.getId())));
		}
		return orderService.search(pageable, filters);*/
		Pager pager = new Pager();
		pager.setPageNumber(form.getPageNumber());
		pager.setPageSize(form.getPageSize());
		return orderService.listPage(form, pager);
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
		/*CustomerSqlForm form = new CustomerSqlForm();
		form.setOrderId(orderId);
		Pager pager = new Pager();
		return orderService.listPage(form, pager);*/
		//	dbDao.f
		Cnd cnd = Cnd.NEW();
		//List<CustomerEntity> query = dbDao.query(CustomerEntity.class, Cnd.where("id", "=",orderId ), null);
		String sqlString = sqlManager.get("customer_list");
		Sql sql = Sqls.create(sqlString);
		sql.setParam("orderId", orderId);
		List<NewCustomerEntity> query = DbSqlUtil.query(dbDao, NewCustomerEntity.class, sql);
		return query;
	}

	/**
	 * 
	 * 下单的时候客户信息中手机和邮箱查询的来源
	 * <p>
	 *
	 * @param orderId
	 * @return TODO(这里描述每个参数,如果有返回值描述返回值,如果有异常描述异常)
	 */
	@RequestMapping(value = "custominfo")
	@ResponseBody
	public Object custominfo() {
		List<CustomerManageEntity> query = dbDao.query(CustomerManageEntity.class, null, null);
		return query;
	}

	/***
	 * 
	 * 
	 * 订单列表下单中的客户信息的查询
	 * <p>
	 *
	 * @return TODO(这里描述每个参数,如果有返回值描述返回值,如果有异常描述异常)
	 */
	@RequestMapping(value = "custominfowrite")
	@ResponseBody
	public Object custominfowrite(String id) {
		if (Util.isEmpty(id)) {
			return null;
		}
		Long idNew = Long.valueOf(id);
		CustomerManageEntity customerManageEntity = dbDao.fetch(CustomerManageEntity.class, idNew);
		return customerManageEntity;
	}

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
	@RequestMapping(value = "orderSave")
	@ResponseBody
	@Transactional
	public Object orderSave(@RequestBody NewOrderEntity order) {
		/*		CustomerManageEntity customermanage = order.getCustomermanage();
				if (!Util.isEmpty(customermanage)) {
					order.setCus_management_id(customermanage.getId());
					Chain chain = Chain.make("updateTime", new Date());
					chain.add("fullComName", customermanage.getFullComName());
					chain.add("customerSource", customermanage.getCustomerSource());
					chain.add("linkman", customermanage.getLinkman());
					chain.add("telephone", customermanage.getTelephone());
					chain.add("email", customermanage.getEmail());
					dbDao.update(CustomerManageEntity.class, chain, Cnd.where("id", "=", customermanage.getId()));
				}*/
		//根据他们的id是否存在判断是更新还是删除
		NewOrderEntity orderOld = order;
		if (!Util.isEmpty(order.getId()) && order.getId() > 0) {
			NewOrderEntity orderNew = dbDao.fetch(NewOrderEntity.class, order.getId());
			order.setUpdatetime(new Date());
			nutDao.update(order);
			int a = order.getHeadcount() - orderNew.getHeadcount();
			if (a > 0) {

				//根据人数插入多个申请人的数据
				for (int i = a; i > 0; i--) {
					NewCustomerEntity c = new NewCustomerEntity();
					/*c.setStatus(OrderVisaApproStatusEnum.writeInfo.intKey());*/
					c.setCreatetime(new Date());
					c.setUpdatetime(new Date());
					NewCustomerEntity insert = dbDao.insert(c);
					NewCustomerOrderEntity customerOrderEntity = new NewCustomerOrderEntity();
					customerOrderEntity.setCustomerid(insert.getId());
					customerOrderEntity.setOrderid(orderOld.getId());
					customerOrderEntity.setCreatetime(new Date());
					customerOrderEntity.setUpdatetime(new Date());
					dbDao.insert(customerOrderEntity);
				}
			} else if (a < 0) {
				int n = -a;
				List<NewCustomerOrderEntity> query = dbDao.query(NewCustomerOrderEntity.class,
						Cnd.where("orderid", "=", order.getId()).orderBy("updatetime", "asc"), null);
				for (int i = 0; i < n; i++) {
					NewCustomerOrderEntity c = query.get(i);
					dbDao.delete(c);
					dbDao.delete(NewCustomerEntity.class, c.getCustomerid());
				}
			}

		} else {
			order.setStatus(OrderVisaApproStatusEnum.placeOrder.intKey());
			order.setCreatetime(new Date());
			order.setUpdatetime(new Date());

			//生成订单号
			SimpleDateFormat smf = new SimpleDateFormat("yyyyMMdd");
			String format = smf.format(new Date());
			String sqlString = sqlManager.get("orderlist_ordernum");
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
			String ordernum = format + "US" + sum1;

			order.setOrdernumber(ordernum);

			orderOld = dbDao.insert(order);
			//根据人数插入多个申请人的数据
			for (int i = orderOld.getHeadcount(); i > 0; i--) {
				NewCustomerEntity c = new NewCustomerEntity();
				c.setCreatetime(new Date());
				c.setUpdatetime(new Date());
				/*c.setStatus(OrderVisaApproStatusEnum.writeInfo.intKey());*/
				NewCustomerEntity insert = dbDao.insert(c);
				NewCustomerOrderEntity customerOrderEntity = new NewCustomerOrderEntity();
				customerOrderEntity.setCustomerid(insert.getId());
				customerOrderEntity.setOrderid(orderOld.getId());

				customerOrderEntity.setCreatetime(new Date());
				customerOrderEntity.setUpdatetime(new Date());
				dbDao.insert(customerOrderEntity);
			}

			//	dbDao.update(NewOrderEntity.class, Chain.make("ordernumber", ordernum), Cnd.where("id", "=", orderOld.getId()));
		}
		//客户来源

		CustomerManageEntity customermanage = order.getCustomermanage();
		int customerSource = order.getCustomerSource();
		if (!Util.isEmpty(customerSource) && customerSource > 0) {
			if (customerSource == OrderJapancustomersourceEnum.zhike.intKey()) {
				NewCustomerresourceEntity customerresourceusa = order.getCustomerresource();
				List<NewCustomerresourceEntity> customerresource = dbDao.query(NewCustomerresourceEntity.class,
						Cnd.where("order_id", "=", orderOld.getId()), null);

				customerresourceusa.setOrder_id(Long.valueOf(orderOld.getId()));
				if (!Util.isEmpty(customerresource) && customerresource.size() > 0) {
					customerresourceusa.setId(customerresource.get(0).getId());
					nutDao.update(customerresourceusa);

				} else {

					dbDao.insert(customerresourceusa);
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

					orderOld.setCus_management_id(customermanage.getId());
					dbDao.update(orderOld, null);
					Chain chain = Chain.make("updateTime", new Date());
					chain.add("fullComName", customermanage.getFullComName());
					chain.add("customerSource", customerSource);
					chain.add("linkman", customermanage.getLinkman());
					chain.add("telephone", customermanage.getTelephone());
					chain.add("email", customermanage.getEmail());
					dbDao.update(CustomerManageEntity.class, chain, Cnd.where("id", "=", customermanage.getId()));
					//在客户来源不是直客的状态下也存在这个表中，以便用于列表展示

					NewCustomerresourceEntity customerresourceusa = new NewCustomerresourceEntity();
					List<NewCustomerresourceEntity> customerresource = dbDao.query(NewCustomerresourceEntity.class,
							Cnd.where("order_id", "=", orderOld.getId()), null);
					customerresourceusa.setEmail(customermanage.getEmail());
					customerresourceusa.setFullComName(customermanage.getFullComName());
					customerresourceusa.setLinkman(customermanage.getLinkman());
					customerresourceusa.setTelephone(customermanage.getTelephone());
					customerresourceusa.setOrder_id(Long.valueOf(orderOld.getId()));
					if (!Util.isEmpty(customerresource) && customerresource.size() > 0) {
						customerresourceusa.setId(customerresource.get(0).getId());
						nutDao.update(customerresourceusa);

					} else {

						dbDao.insert(customerresourceusa);
					}
				}

			}
		}

		NewFastMailEntity fastMail = order.getFastMail();
		if (!Util.isEmpty(fastMail)) {
			if (!Util.isEmpty(fastMail.getId()) && fastMail.getId() > 0) {
				nutDao.update(fastMail);
			} else {

				fastMail.setOrderid(orderOld.getId());
				dbDao.insert(fastMail);
			}
		}
		NewPayCompanyEntity payCompany = order.getPayCompany();
		if (!Util.isEmpty(payCompany)) {
			if (!Util.isEmpty(payCompany.getId()) && payCompany.getId() > 0) {
				nutDao.update(payCompany);
			} else {

				payCompany.setOrderid(orderOld.getId());
				dbDao.insert(payCompany);
			}
		}
		NewPayPersionEntity payPersion = order.getPayPersion();
		if (!Util.isEmpty(payPersion)) {
			if (!Util.isEmpty(payPersion.getId()) && payPersion.getId() > 0) {
				nutDao.update(payPersion);
			} else {

				payPersion.setOrderid(orderOld.getId());
				dbDao.insert(payPersion);
			}
		}
		NewTrip trip = order.getTrip();
		if (!Util.isEmpty(trip)) {
			if (!Util.isEmpty(trip.getId()) && trip.getId() > 0) {
				nutDao.update(trip);
			} else {

				trip.setOrderid(orderOld.getId());
				trip = dbDao.insert(trip);
			}
		}
		List<NewPeerPersionEntity> peerList = order.getPeerList();
		long orderid = orderOld.getId();
		List<NewPeerPersionEntity> list1 = dbDao.query(NewPeerPersionEntity.class,
				Cnd.where("tripid", "=", trip.getId()), null);
		if (!Util.isEmpty(list1) && list1.size() > 0) {

			dbDao.delete(list1);
		}
		if (!Util.isEmpty(peerList) && peerList.size() > 0) {

			for (NewPeerPersionEntity newPeerPersionEntity : peerList) {
				/*	if (!Util.isEmpty(newPeerPersionEntity.getId()) && newPeerPersionEntity.getId() > 0) {
						nutDao.update(newPeerPersionEntity);
					} else {*/
				newPeerPersionEntity.setTripid(trip.getId());
				dbDao.insert(newPeerPersionEntity);
				/*	}*/
			}
			//List<NewPeerPersionEntity> insert = dbDao.insert(peerList);
		}
		return ResultObject.success("添加成功");
	}

	@RequestMapping(value = "showDetail")
	@ResponseBody
	public Object showDetail(long orderid) {
		NewOrderEntity order = dbDao.fetch(NewOrderEntity.class, orderid);
		/*		CustomerManageEntity customerManageEntity = dbDao.fetch(CustomerManageEntity.class,
						order.getCus_management_id());
				if (!Util.isEmpty(customerManageEntity)) {
					order.setCustomermanage(customerManageEntity);
				}*/

		int customerSource = order.getCustomerSource();
		if (!Util.isEmpty(customerSource) && customerSource > 0) {
			if (customerSource == OrderJapancustomersourceEnum.zhike.intKey()) {
				List<NewCustomerresourceEntity> customerresourceusa = dbDao.query(NewCustomerresourceEntity.class,
						Cnd.where("order_id", "=", orderid), null);
				if (!Util.isEmpty(customerresourceusa) && customerresourceusa.size() > 0) {
					order.setCustomerresource(customerresourceusa.get(0));
					CustomerManageEntity customerManageEntity = new CustomerManageEntity();
					customerManageEntity.setTelephone(customerresourceusa.get(0).getTelephone());
					customerManageEntity.setLinkman(customerresourceusa.get(0).getLinkman());
					customerManageEntity.setEmail(customerresourceusa.get(0).getEmail());
					customerManageEntity.setFullComName(customerresourceusa.get(0).getFullComName());
					order.setCustomermanage(customerManageEntity);
				}
			} else {
				//if (!Util.isEmpty(customermanage)) {

				CustomerManageEntity customerManageEntity = dbDao.fetch(CustomerManageEntity.class,
						Long.valueOf(order.getCus_management_id()));
				if (!Util.isEmpty(customerManageEntity)) {
					order.setCustomermanage(customerManageEntity);
					NewCustomerresourceEntity customerresourceJp = new NewCustomerresourceEntity();
					order.setCustomerresource(customerresourceJp);
				}

				//}

			}
		}

		List<NewTrip> newTrips = dbDao.query(NewTrip.class, Cnd.where("orderid", "=", orderid), null);
		if (!Util.isEmpty(newTrips) && newTrips.size() > 0) {
			order.setTrip(newTrips.get(0));
			List<NewPeerPersionEntity> query = dbDao.query(NewPeerPersionEntity.class,
					Cnd.where("tripid", "=", newTrips.get(0).getId()), null);
			order.setPeerList(query);
		}
		List<NewPayPersionEntity> newPayPersionEntities = dbDao.query(NewPayPersionEntity.class,
				Cnd.where("orderid", "=", orderid), null);
		if (!Util.isEmpty(newPayPersionEntities) && newPayPersionEntities.size() > 0) {
			order.setPayPersion(newPayPersionEntities.get(0));
		}
		List<NewPayCompanyEntity> newPayCompanyEntities = dbDao.query(NewPayCompanyEntity.class,
				Cnd.where("orderid", "=", orderid), null);
		if (!Util.isEmpty(newPayCompanyEntities) && newPayCompanyEntities.size() > 0) {
			order.setPayCompany(newPayCompanyEntities.get(0));
		}
		List<NewFastMailEntity> newFastMailEntities = dbDao.query(NewFastMailEntity.class,
				Cnd.where("orderid", "=", orderid), null);
		if (!Util.isEmpty(newFastMailEntities) && newFastMailEntities.size() > 0) {
			order.setFastMail(newFastMailEntities.get(0));
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
	public Object share(HttpServletRequest request, long customerid, String type) throws IOException {

		/*String href = RequestUtil.getServerPath(request) + "/m/delivery.html?oid=" + oid;*/
		NewCustomerEntity customer = null;
		NewOrderEntity order = null;
		long orderid = 0;
		if ("customer".equals(type)) {

			customer = dbDao.fetch(NewCustomerEntity.class, customerid);
			List<NewCustomerOrderEntity> query = dbDao.query(NewCustomerOrderEntity.class,
					Cnd.where("customerid", "=", customerid), null);
			orderid = query.get(0).getOrderid();
			order = dbDao.fetch(NewOrderEntity.class, orderid);
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
		employeeEntity.setFullName(customer.getChinesexing() + customer.getChinesename());
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
				.replace("${oid}", order.getOrdernumber()).replace("${href}", "http://218.244.148.21:9004/")
				.replace("${logininfo}", "用户名:" + phone + "密码:" + temp);
		String result = mailService.send(customer.getEmail(), html, "签证资料录入", MailService.Type.HTML);
		if ("success".equalsIgnoreCase(result)) {
			//成功以后分享次数加1
			dbDao.update(
					NewCustomerEntity.class,
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
	public Object shareall(HttpServletRequest request, long orderid, String type) throws IOException {

		NewOrderEntity order = dbDao.fetch(NewOrderEntity.class, orderid);
		CustomerManageEntity customerManage = dbDao.fetch(CustomerManageEntity.class, order.getCus_management_id());
		//发送邮件前进行游客信息的注册
		String phone = customerManage.getTelephone();
		String result = null;
		/*
		String result = getMailContent(order, phone, null, customerManage);
		if ("success".equalsIgnoreCase(result)) {
			dbDao.update(NewOrderEntity.class, Chain.make("sharecountmany", order.getSharecountmany() + 1),
					Cnd.where("id", "=", orderid));
			//成功以后分享次数加1
		}
		*/
		List<NewCustomerOrderEntity> query = dbDao.query(NewCustomerOrderEntity.class,
				Cnd.where("orderid", "=", orderid), null);

		for (NewCustomerOrderEntity newCustomerOrderEntity : query) {
			NewCustomerEntity customer = dbDao.fetch(NewCustomerEntity.class, newCustomerOrderEntity.getCustomerid());

			phone = customer.getPhone();

			if (!Util.isEmpty(phone)) {

				result = getMailContent(order, phone, customer, null);

			}

		}
		return ResultObject.success(result);
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
		employeeEntity.setPassword(pwd);
		employeeEntity.setFullName(customer.getChinesexing() + customer.getChinesename());
		byte[] salt = Digests.generateSalt(8);
		employeeEntity.setSalt(Encodes.encodeHex(salt));
		byte[] password = pwd.getBytes();
		byte[] hashPassword = Digests.sha1(password, salt, 1024);
		pwd = Encodes.encodeHex(hashPassword);

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
				.replace("${logininfo}", "用户名:" + phone + "密码:" + employeeEntity.getPassword());
		String result = mailService.send(email, html, "签证资料录入", MailService.Type.HTML);

		if ("success".equalsIgnoreCase(result)) {
			//成功以后分享次数加1
			dbDao.update(
					NewCustomerEntity.class,
					Chain.make("sharecount", customer.getSharecount() + 1)
							.add("status", OrderVisaApproStatusEnum.writeInfo.intKey())
							.add("empid", employeeEntity.getId()), Cnd.where("id", "=", customer.getId()));
			dbDao.update(
					NewOrderEntity.class,
					Chain.make("sharecountmany", order.getSharecountmany() + 1).add("status",
							OrderVisaApproStatusEnum.shared.intKey()), Cnd.where("id", "=", order.getId()));
		}
		return result;
	}

	/**
	//	 * 将订单完善连接分享到用户邮箱或者手机
	//	 *批量分享都可以看见
	//	 * @param request
	//	 * @param oid     订单ID
	//	 */
	@RequestMapping(value = "shareallothers")
	@ResponseBody
	public Object shareallothers(HttpServletRequest request, long orderid, String type) throws IOException {
		List<String> lines = IOUtils.readLines(getClass().getClassLoader().getResourceAsStream("mail.html"));
		StringBuilder tmp = new StringBuilder();
		for (String line : lines) {
			tmp.append(line);
		}

		NewOrderEntity order = dbDao.fetch(NewOrderEntity.class, orderid);
		CustomerManageEntity customerManage = dbDao.fetch(CustomerManageEntity.class, order.getCus_management_id());
		//发送邮件前进行游客信息的注册
		String phone = customerManage.getTelephone();
		String info = "";
		EmployeeEntity employeeEntity = new EmployeeEntity();
		employeeEntity.setTelephone(phone);
		info += customerManage.getLinkman() + ":" + phone;
		employeeEntity.setUserType(UserTypeEnum.TOURIST_IDENTITY.intKey());
		//生成六位数的随机密码
		String pwd = "";
		for (int i = 0; i < 6; i++) {
			int a = (int) (Math.random() * 10);
			pwd += a;

		}

		info += "密码:" + pwd + "<br/>";
		byte[] salt = Digests.generateSalt(8);
		employeeEntity.setSalt(Encodes.encodeHex(salt));
		byte[] password = pwd.getBytes();
		byte[] hashPassword = Digests.sha1(password, salt, 1024);
		pwd = Encodes.encodeHex(hashPassword);
		employeeEntity.setPassword(pwd);
		List<EmployeeEntity> query2 = dbDao.query(EmployeeEntity.class,
				Cnd.where("telephone", "=", phone).and("userType", "=", UserTypeEnum.TOURIST_IDENTITY.intKey()), null);
		if (!Util.isEmpty(query2) && query2.size() > 0) {
			employeeEntity.setId(query2.get(0).getId());
			nutDao.update(employeeEntity);
		} else {

			dbDao.insert(employeeEntity);
		}
		//客户表开始

		List<NewCustomerOrderEntity> query = dbDao.query(NewCustomerOrderEntity.class,
				Cnd.where("orderid", "=", orderid), null);
		long num[] = new long[query.size()];
		for (int j = 0; j < query.size(); j++) {
			NewCustomerOrderEntity newCustomerOrderEntity = query.get(j);
			NewCustomerEntity customer = dbDao.fetch(NewCustomerEntity.class, newCustomerOrderEntity.getCustomerid());

			phone = customer.getPhone();
			employeeEntity = new EmployeeEntity();
			employeeEntity.setTelephone(phone);
			employeeEntity.setUserType(UserTypeEnum.TOURIST_IDENTITY.intKey());
			//生成六位数的随机密码
			info += customer.getChinesexing() + customer.getChinesefullname() + ":" + phone;
			pwd = "";
			for (int i = 0; i < 6; i++) {
				int a = (int) (Math.random() * 10);
				pwd += a;

			}
			info += "密码:" + pwd + "<br/>";
			byte[] salt1 = Digests.generateSalt(8);
			employeeEntity.setSalt(Encodes.encodeHex(salt1));
			byte[] password1 = pwd.getBytes();
			byte[] hashPassword1 = Digests.sha1(password1, salt1, 1024);
			pwd = Encodes.encodeHex(hashPassword1);
			employeeEntity.setPassword(pwd);
			List<EmployeeEntity> query1 = dbDao.query(EmployeeEntity.class,
					Cnd.where("telephone", "=", phone).and("userType", "=", UserTypeEnum.TOURIST_IDENTITY.intKey()),
					null);
			if (!Util.isEmpty(query1) && query1.size() > 0) {
				employeeEntity.setId(query1.get(0).getId());
				nutDao.update(employeeEntity);
			} else {

				employeeEntity = dbDao.insert(employeeEntity);

			}
			num[j] = employeeEntity.getId();

		}
		//订单表联系人的发送

		String html = tmp.toString().replace("${name}", customerManage.getLinkman())
				.replace("${oid}", order.getOrdernumber()).replace("${href}", "http://218.244.148.21:9004/")
				.replace("${logininfo}", info);

		String result = mailService.send(customerManage.getEmail(), html, "签证资料录入", MailService.Type.HTML);
		if ("success".equalsIgnoreCase(result)) {
			dbDao.update(
					NewOrderEntity.class,
					Chain.make("sharecountmany", order.getSharecountmany() + 1).add("status",
							OrderVisaApproStatusEnum.shared.intKey()), Cnd.where("id", "=", orderid));
			//成功以后分享次数加1
		}

		//客户联系人的发送
		for (int j = 0; j < query.size(); j++) {
			NewCustomerOrderEntity newCustomerOrderEntity = query.get(j);
			NewCustomerEntity customer = dbDao.fetch(NewCustomerEntity.class, newCustomerOrderEntity.getCustomerid());
			html = tmp.toString().replace("${name}", customer.getChinesexing() + customer.getChinesename())
					.replace("${oid}", order.getOrdernumber()).replace("${href}", "http://218.244.148.21:9004/")
					.replace("${logininfo}", info);

			result = mailService.send(customer.getEmail(), html, "签证资料录入", MailService.Type.HTML);

			if ("success".equalsIgnoreCase(result)) {
				//成功以后分享次数加1
				dbDao.update(
						NewCustomerEntity.class,
						Chain.make("sharecount", customer.getSharecount() + 1)
								.add("status", OrderVisaApproStatusEnum.shared.intKey()).add("empid", num[j]),
						Cnd.where("id", "=", customer.getId()));
			}
		}

		return ResultObject.success(result);
	}

	/**
	//	 * 第二次发邮件将面签时间通知（单个通知）
	//	 *
	//	 * @param request
	//	 * @param oid     订单ID
	//	 */
	@RequestMapping(value = "notice")
	@ResponseBody
	public Object notice(long customerid, String type) throws IOException {

		/*String href = RequestUtil.getServerPath(request) + "/m/delivery.html?oid=" + oid;*/
		NewCustomerEntity customer = null;
		NewOrderEntity order = null;
		long orderid = 0;
		if ("customer".equals(type)) {

			customer = dbDao.fetch(NewCustomerEntity.class, customerid);
			List<NewCustomerOrderEntity> query = dbDao.query(NewCustomerOrderEntity.class,
					Cnd.where("customerid", "=", customerid), null);
			orderid = query.get(0).getOrderid();
			order = dbDao.fetch(NewOrderEntity.class, orderid);
		}
		List<String> lines = IOUtils.readLines(getClass().getClassLoader().getResourceAsStream("notice.html"));
		StringBuilder tmp = new StringBuilder();
		for (String line : lines) {
			tmp.append(line);
		}
		//发送邮件前进行游客信息的注册
		String phone = customer.getPhone();
		NewDeliveryUSAEntity fetch = dbDao.fetch(NewDeliveryUSAEntity.class,
				Cnd.where("customer_usa_id", "=", customerid));
		String str = "";
		if (!Util.isEmpty(fetch)) {
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

			if (!Util.isEmpty(fetch.getEarlydate())) {
				str += "最早时间:" + df.format(fetch.getEarlydate()) + ";";
			}

			if (!Util.isEmpty(fetch.getLatterdate())) {
				str += "最晚时间:" + df.format(fetch.getLatterdate()) + ";";
			}

			if (!Util.isEmpty(fetch.getInterviewtime())) {
				str += "面签时段:" + InterviewTimeEnum.get(fetch.getInterviewtime()) + ";";
			}

			if (!Util.isEmpty(fetch.getVisasendtype())) {
				str += "护照递送方式:" + SendPassportEnum.get(fetch.getVisasendtype()) + ";";
			}

			if (!Util.isEmpty(fetch.getPrevince())) {
				str += "   " + fetch.getPrevince() + "   ";
			}

			if (!Util.isEmpty(fetch.getDetailplace())) {
				str += "   " + fetch.getDetailplace() + "   ";
			}

			if (!Util.isEmpty(fetch.getFastmailaddress())) {
				str += "   " + fetch.getFastmailaddress() + "   ";
			}

		}
		String html = tmp.toString().replace("${name}", customer.getChinesexing() + customer.getChinesename())
				.replace("${oid}", order.getOrdernumber()).replace("${href}", "http://218.244.148.21:9004/")
				.replace("${interview}", str);
		String result = mailService.send(customer.getEmail(), html, "签证信息通知", MailService.Type.HTML);
		if ("success".equalsIgnoreCase(result)) {
			//成功以后分享次数加1
			dbDao.update(
					NewCustomerEntity.class,
					Chain.make("noticecount", customer.getSharecount() + 1)
							.add("status", OrderVisaApproStatusEnum.yueVisa.intKey()).add("updatetime", new Date()),
					Cnd.where("id", "=", customer.getId()));
			dbDao.update(
					NewOrderEntity.class,
					Chain.make("sharecountmany", order.getSharecountmany() + 1)
							.add("status", OrderVisaApproStatusEnum.yueVisa.intKey()).add("updatetime", new Date()),
					Cnd.where("id", "=", orderid));

			return ResultObject.success(result);
		} else {
			return ResultObject.fail(result);
		}
	}

	/**
	//	 * 批量通知
	//	 * @param request
	//	 * @param oid     订单ID
	//	 */
	@RequestMapping(value = "noticeall")
	@ResponseBody
	public Object noticeall(long orderid, String type) throws IOException {
		List<String> lines = IOUtils.readLines(getClass().getClassLoader().getResourceAsStream("notice.html"));
		StringBuilder tmp = new StringBuilder();
		for (String line : lines) {
			tmp.append(line);
		}

		NewOrderEntity order = dbDao.fetch(NewOrderEntity.class, orderid);
		CustomerManageEntity customerManage = dbDao.fetch(CustomerManageEntity.class, order.getCus_management_id());
		//发送邮件前进行游客信息的注册
		String phone = customerManage.getTelephone();

		List<NewCustomerOrderEntity> query = dbDao.query(NewCustomerOrderEntity.class,
				Cnd.where("orderid", "=", orderid), null);

		/*for (NewCustomerOrderEntity newCustomerOrderEntity : query) {
			NewCustomerEntity customer = dbDao.fetch(NewCustomerEntity.class, newCustomerOrderEntity.getCustomerid());

			phone = customer.getPhone();

		}*/
		//订单表联系人的发送

		/*String html = tmp.toString().replace("${name}", customerManage.getLinkman())
				.replace("${oid}", order.getOrdernumber()).replace("${href}", "http://www.baidu.com")
				.replace("${interview}", (new SimpleDateFormat("yyyy-MM-dd")).format(new Date()));

		String result = mailService.send(customerManage.getEmail(), html, "签证信息通知", MailService.Type.HTML);
		if ("success".equalsIgnoreCase(result)) {
			dbDao.update(NewOrderEntity.class, Chain.make("noticecountmany", order.getSharecountmany() + 1),
					Cnd.where("id", "=", orderid));
			//成功以后分享次数加1
		}
		*/

		dbDao.update(
				NewOrderEntity.class,
				Chain.make("sharecountmany", order.getSharecountmany() + 1)
						.add("status", OrderVisaApproStatusEnum.yueVisa.intKey()).add("updatetime", new Date()),
				Cnd.where("id", "=", orderid));
		//客户联系人的发送
		for (NewCustomerOrderEntity newCustomerOrderEntity : query) {
			NewCustomerEntity customer = dbDao.fetch(NewCustomerEntity.class, newCustomerOrderEntity.getCustomerid());

			NewDeliveryUSAEntity fetch = dbDao.fetch(NewDeliveryUSAEntity.class,
					Cnd.where("customer_usa_id", "=", customer.getId()));
			String str = "";
			if (!Util.isEmpty(fetch)) {
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

				if (!Util.isEmpty(fetch.getEarlydate())) {
					str += "最早时间:" + df.format(fetch.getEarlydate()) + ";";
				}

				if (!Util.isEmpty(fetch.getLatterdate())) {
					str += "最晚时间:" + df.format(fetch.getLatterdate()) + ";";
				}

				if (!Util.isEmpty(fetch.getInterviewtime())) {
					str += "面签时段:" + InterviewTimeEnum.get(fetch.getInterviewtime()) + ";";
				}

				if (!Util.isEmpty(fetch.getVisasendtype())) {
					str += "护照递送方式:" + SendPassportEnum.get(fetch.getVisasendtype()) + ";";
				}

				if (!Util.isEmpty(fetch.getPrevince())) {
					str += "   " + fetch.getPrevince() + "   ";
				}

				if (!Util.isEmpty(fetch.getDetailplace())) {
					str += "   " + fetch.getDetailplace() + "   ";
				}

				if (!Util.isEmpty(fetch.getFastmailaddress())) {
					str += "   " + fetch.getFastmailaddress() + "   ";
				}

			}

			String html = tmp.toString().replace("${name}", customer.getChinesexing() + customer.getChinesename())
					.replace("${oid}", order.getOrdernumber()).replace("${href}", "http://218.244.148.21:9004/")
					.replace("${interview}", str);

			String result = mailService.send(customer.getEmail(), html, "签证信息通知", MailService.Type.HTML);

			if ("success".equalsIgnoreCase(result)) {
				//成功以后分享次数加1
				/*dbDao.update(NewCustomerEntity.class, Chain.make("noticecount", customer.getSharecount() + 1),
						Cnd.where("id", "=", customer.getId()));
				*/
				dbDao.update(
						NewCustomerEntity.class,
						Chain.make("noticecount", customer.getSharecount() + 1)
								.add("status", OrderVisaApproStatusEnum.yueVisa.intKey()).add("updatetime", new Date()),
						Cnd.where("id", "=", customer.getId()));
			}
		}

		return ResultObject.success("通知成功");
	}

	@RequestMapping(value = "deliveryusa")
	@ResponseBody
	public Object deliveryusa(long customerid) {
		NewDeliveryUSAEntity deliveryUSA = dbDao.fetch(NewDeliveryUSAEntity.class,
				Cnd.where("customer_usa_id", "=", customerid));
		return deliveryUSA;
	}

	/***
	 * 
	 *美国的递送保存
	 * @param orderid
	 * @return TODO(这里描述每个参数,如果有返回值描述返回值,如果有异常描述异常)
	 */

	@RequestMapping(value = "deliveryUSAsave")
	@ResponseBody
	public Object deliveryUSAsave(@RequestBody NewDeliveryUSAEntity deliveryJapan, int customerid) {
		deliveryJapan.setCustomer_usa_id(customerid);
		Integer id = deliveryJapan.getId();
		if (!Util.isEmpty(id) && id > 0) {

			dbDao.update(deliveryJapan, null);
		} else {

			dbDao.update(NewCustomerEntity.class,
					Chain.make("updatetime", new Date()).add("status", OrderVisaApproStatusEnum.readySubmit.intKey()),
					Cnd.where("id", "=", customerid));
			List<NewCustomerOrderEntity> query = dbDao.query(NewCustomerOrderEntity.class,
					Cnd.where("customerid", "=", customerid), null);
			long orderid = query.get(0).getOrderid();
			dbDao.update(NewOrderEntity.class,
					Chain.make("updatetime", new Date()).add("status", OrderVisaApproStatusEnum.DS.intKey()),
					Cnd.where("id", "=", orderid));
			dbDao.insert(deliveryJapan);
		}
		return ResultObject.success("添加成功");
	}

	private Customer contains(List<Customer> customers, Customer customer) {
		for (Customer c : customers) {
			if (c.getId().equals(customer.getId())) {
				return c;
			}
		}
		return null;
	}

	//	/**
	//	 * 按照订单获取关联客户
	//	 *
	//	 * @param oid 订单ID
	//	 */
	//	@RequestMapping(value = "team/{oid}")
	//	@ResponseBody
	//	public Object team(@PathVariable long oid) {
	//		Order order = orderService.get(oid);
	//		return order.getCustomers();
	//	}
	//
	//	/**
	//	 * 将订单完善连接分享到用户邮箱或者手机
	//	 *
	//	 * @param request
	//	 * @param oid     订单ID
	//	 */
	//	@RequestMapping(value = "share/{oid}")
	//	@ResponseBody
	//	public Object share(HttpServletRequest request, @PathVariable long oid) throws IOException {
	//		Order order = orderService.get(oid);
	//		if (StringUtils.isBlank(order.getEmail())) {
	//			return ResultObject.fail("邮件不能为空");
	//		}
	//		String href = RequestUtil.getServerPath(request) + "/m/delivery.html?oid=" + oid;
	//		List<String> lines = IOUtils.readLines(getClass().getClassLoader().getResourceAsStream("mail.html"));
	//		StringBuilder tmp = new StringBuilder();
	//		for (String line : lines) {
	//			tmp.append(line);
	//		}
	//		String html = tmp.toString().replace("${name}", order.getContact()).replace("${oid}", order.getId().toString())
	//				.replace("${href}", href);
	//		String result = mailService.send(order.getEmail(), html, "签证资料录入", MailService.Type.HTML);
	//		if ("success".equalsIgnoreCase(result)) {
	//			return ResultObject.success(href);
	//		} else {
	//			return ResultObject.fail(result);
	//		}
	//	}
	//
	//	/**
	//	 * 展示订单详情
	//	 *
	//	 * @param oid
	//	 * @return
	//	 */
	//	@RequestMapping(value = "show/{oid}")
	//	@ResponseBody
	//	public Object show(@PathVariable long oid, @RequestParam(defaultValue = "order") String type) {
	//		Order order = orderService.get(oid);
	//		//主动将懒加载的数据加载出来
	//		for (Customer c : order.getCustomers()) {
	//			CollectionUtils.size(c.getWorks());
	//			CollectionUtils.size(c.getFinances());
	//			CollectionUtils.size(c.getFamilies());
	//			CollectionUtils.size(c.getHistories());
	//			CollectionUtils.size(c.getVisitCountry());
	//			CollectionUtils.size(c.getSchools());
	//			CollectionUtils.size(c.getPhotos());
	//			CollectionUtils.size(c.getCharitable());
	//			CollectionUtils.size(c.getLanguages());
	//			CollectionUtils.size(c.getOtherCountry());
	//			if (c.getTravel() != null)
	//				CollectionUtils.size(c.getTravel().getTogethers());
	//		}
	//		if ("customer".equalsIgnoreCase(type)) {
	//			return order.getCustomers();
	//		} else {
	//			CollectionUtils.size(order.getTrips());
	//			return order;
	//		}
	//	}
	//
	//	/**
	//	 * 创建或编辑订单
	//	 */
	//	@RequestMapping(value = "save/{action}", method = RequestMethod.POST)
	//	@ResponseBody
	//	public Object save(@PathVariable String action, @RequestBody Order order,
	//			@RequestBody(required = false) PageFilter filter) {
	//		if (order.getId() != null) {
	//			Order oldOrder = orderService.get(order.getId());
	//			if ("edit".equalsIgnoreCase(action)) {
	//				BeanUtils.copyPropertiesInclude(order, oldOrder, "contact", "mobile", "email", "template", "source",
	//						"useFor", "travelDate", "sendDate", "estimateDate", "tripType", "dataFrom", "postid",
	//						"address", "invoice", "invoiceSum", "invoiceContent", "invoiceTitle", "visaType", "urgent");
	//				//复制客户属性
	//				Iterator<Customer> iterator = oldOrder.getCustomers().iterator();
	//				while (iterator.hasNext()) {
	//					Customer c = iterator.next();
	//					Customer tmp = contains(order.getCustomers(), c);
	//					if (tmp != null) {
	//						BeanUtils.copyPropertiesInclude(tmp, c, "lastName", "firstName", "passport", "main",
	//								"depositSource", "depositMethod", "depositSum", "depositCount", "receipt", "insurance",
	//								"outDistrict", "home");
	//					} else {
	//						iterator.remove();
	//					}
	//				}
	//				//处理新增的用户
	//				for (Customer customer : order.getCustomers()) {
	//					if (customer.getId() == null) {
	//						oldOrder.getCustomers().add(customer);
	//					}
	//				}
	//			} else if ("patch".equalsIgnoreCase(action)) {
	//				BeanUtils.copyPropertiesInclude(order, oldOrder, "entry", "depart", "trips", "aim");
	//			} else if ("delivery".equalsIgnoreCase(action)) {
	//				BeanUtils.copyPropertiesInclude(order, oldOrder, "startDate", "period", "endDate", "remark",
	//						"delivery", "city");
	//			}
	//			order = oldOrder;
	//		}
	//		if (StringUtils.isBlank(order.getMobile()) && StringUtils.isBlank(order.getEmail())) {
	//			return ResultObject.fail("至少要有一种联系方式");
	//		}
	//		if (order.getCustomers() == null || order.getCustomers().size() == 0)
	//			return ResultObject.fail("人数必须大于0人");
	//		if (order.getId() == null) {//ID为空则代表新建
	//			order.setPwd(PasswordUtil.randomNum(6));
	//			//如果当前登录人不为空则吧订单分配给当前登录人
	//			if (UserUtil.getUser() != null) {
	//				order.setUser(UserUtil.getUser());
	//			}
	//		}
	//		return orderService.save(order);
	//		/*orderService.save(order);
	//		User user = UserUtil.getUser();
	//		if (filter == null)
	//			filter = new PageFilter();
	//		Pageable pageable = filter.getPageable();
	//		Junction filters = Restrictions.conjunction(filter.getFilter(Order.class));
	//		//如果不是管理员则按照登录人查看订单,如果是管理员则差看所有
	//		if (!SecurityUtils.getSubject().hasRole("admin")) {
	//			filters = filters.add(Restrictions.in("user.id", Lists.newArrayList(user.getId())));
	//		}*/
	//		//return orderService.search(pageable, filters);
	//
	//	}
	//
	//	/**
	//	 * 导出日本所需资料的pdf
	//	 *
	//	 * @param oid
	//	 */
	//	@RequestMapping(value = "export/{oid}")
	//	@ResponseBody
	//	public Object export(HttpServletResponse resp, @PathVariable long oid) throws IOException {
	//		Order order = orderService.get(oid);
	//		if (order == null) {
	//			return ResultObject.fail("订单不存在!");
	//		}
	//		byte[] bytes = pdfService.export(order).toByteArray();
	//		String fileName = URLEncoder.encode(order.getContact().toString() + "-" + oid + ".zip", "UTF-8");
	//		resp.setContentType("application/zip");
	//		resp.addHeader("Content-Disposition", "attachment;filename=" + fileName);// 设置文件名
	//		IOUtils.write(bytes, resp.getOutputStream());
	//		return ResultObject.fail("PDF生成失败!");
	//	}

}
