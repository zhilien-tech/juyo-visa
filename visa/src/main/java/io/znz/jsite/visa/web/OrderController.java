package io.znz.jsite.visa.web;

import io.znz.jsite.base.BaseController;
import io.znz.jsite.core.service.MailService;
import io.znz.jsite.visa.bean.Customer;
import io.znz.jsite.visa.entity.customer.CustomerEntity;
import io.znz.jsite.visa.form.KenDoTestSqlForm;
import io.znz.jsite.visa.service.OrderService;
import io.znz.jsite.visa.service.PdfService;

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
		List<CustomerEntity> query = DbSqlUtil.query(dbDao, CustomerEntity.class, sql);
		return query;
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
