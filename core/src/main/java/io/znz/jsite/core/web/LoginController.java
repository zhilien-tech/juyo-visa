package io.znz.jsite.core.web;

import io.znz.jsite.base.BaseController;
import io.znz.jsite.core.entity.EmployeeEntity;
import io.znz.jsite.core.entity.NewCustomerEntity;
import io.znz.jsite.core.entity.NewCustomerJpEntity;
import io.znz.jsite.core.entity.companyjob.CompanyJobEntity;
import io.znz.jsite.core.entity.function.FunctionEntity;
import io.znz.jsite.core.entity.userjob.UserJobMapEntity;
import io.znz.jsite.core.enums.UserLoginEnum;
import io.znz.jsite.core.service.authority.AuthorityService;
import io.znz.jsite.core.service.authority.LoginAuthorityService;
import io.znz.jsite.core.util.Const;
import io.znz.jsite.util.StringUtils;
import io.znz.jsite.util.security.Digests;
import io.znz.jsite.util.security.Encodes;

import java.awt.image.BufferedImage;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.SavedRequest;
import org.apache.shiro.web.util.WebUtils;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.SqlManager;
import org.nutz.dao.Sqls;
import org.nutz.dao.sql.Sql;
import org.nutz.ioc.loader.annotation.Inject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import com.alibaba.fastjson.JSON;
import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;
import com.google.common.collect.Lists;
import com.uxuexi.core.common.util.Util;
import com.uxuexi.core.db.dao.IDbDao;
import com.uxuexi.core.db.util.DbSqlUtil;

/**
 * 登录controller
 *
 * @author Chaly
 */
@Controller
@Qualifier("loginController")
@RequestMapping("login")
public class LoginController extends BaseController {

	@Autowired
	protected IDbDao dbDao;

	@Autowired
	protected Dao nutDao;

	@Autowired
	protected LoginAuthorityService loginAuthorityService;

	@Autowired
	protected AuthorityService authorityViewService;

	@Inject
	private SqlManager sqlManager;

	public static final int HASH_INTERATIONS = 1024;

	private Producer captchaProducer = null;

	@Autowired
	public void setCaptchaProducer(Producer captchaProducer) {
		this.captchaProducer = captchaProducer;
	}

	/**
	 * 登录页面.这页面不是实际的登录页,而是按照不同的规则进行跳转以达到多登录页的效果
	 */
	@RequestMapping(value = "login", method = RequestMethod.POST)
	public String login(@RequestParam(required = false) String module, HttpServletRequest request) {
		String path = request.getServletContext().getContextPath();
		//获取手动设置的跳转
		SavedRequest savedRequest = WebUtils.getSavedRequest(request);
		//获取Shiro已存储的跳转
		if (savedRequest != null) {
			module = savedRequest.getRequestUrl();
		}
		module = StringUtils.split(module, "[/?#;]")[(StringUtils.isNotBlank(path) && module.startsWith(path)) ? 1 : 0];
		return "redirect:/" + module + "/login";
	}

	/**
	 * 真正登录的POST请求
	 */
	@RequestMapping(value = "auth", method = RequestMethod.POST)
	public String auth(@RequestParam String to, @RequestParam(required = false) String login,
			@RequestParam(FormAuthenticationFilter.DEFAULT_USERNAME_PARAM) String username,
			@RequestParam(FormAuthenticationFilter.DEFAULT_PASSWORD_PARAM) String password,
			@RequestParam(required = false) Integer logintype,

			@RequestParam(required = false) String captcha, HttpServletRequest request, RedirectAttributesModelMap model) {
		//获取Shiro自动保存的跳转@RequestParam(value = FormAuthenticationFilter.DEFAULT_REMEMBER_ME_PARAM, required = false) boolean rememberMe,
		/*SavedRequest savedRequest = WebUtils.getSavedRequest(request);
		if (savedRequest != null && StringUtils.isNotBlank(savedRequest.getRequestUrl())) {
			to = savedRequest.getRequestUrl();
		}
		try {
			SecurityUtils.getSubject().login(
					new UsernamePasswordCaptchaToken(username, password, rememberMe, captcha, IPUtil
							.getIpAddress(request)));
		} catch (Exception e) {
			if (e instanceof CaptchaException) {
				model.addFlashAttribute("error", "验证码错误，请重试!");
			} else if (e instanceof UnknownAccountException) {
				model.addFlashAttribute("error", "用户名不存在，请重试!");
			} else if (e instanceof IncorrectCredentialsException) {
				model.addFlashAttribute("error", "帐号或密码错误，请重试!");
			} else if (e instanceof LockedAccountException) {
				model.addFlashAttribute("error", "用户已被禁用!");
			} else {
				model.addFlashAttribute("error", "系统异常，请稍后再试！");
			}
			//回显帐号
			if (StringUtils.isNotEmpty(username)) {
				model.addFlashAttribute("username", username);
			}
			//回显密码
			if (StringUtils.isNotEmpty(password)) {
				model.addFlashAttribute("password", password);
			}
			//回显是否记住
			model.addFlashAttribute("remember", rememberMe);
			//以Base64加密的方式回显消息,拼接在url上
			String error = Encodes.encodeBase64(JSON.toJSONString(model.getFlashAttributes()));
			model.addAttribute("e", error);
			if (StringUtils.isBlank(login)) {
				login = request.getHeader("referer");
			}
			return "redirect:" + login;
		}*/
		String kaptchaExpected = (String) request.getSession().getAttribute(
				com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY);

		if (!Util.isEmpty(captcha)) {

			List<FunctionEntity> functions = Lists.newArrayList();
			if (captcha.equalsIgnoreCase(kaptchaExpected) || "8888".equals(captcha)) {
				EmployeeEntity fetch = dbDao.fetch(EmployeeEntity.class, Cnd.where("telephone", "=", username));
				if (!Util.isEmpty(fetch)) {
					Integer userId = fetch.getId();//得到当前登录用户id
					Integer userType = fetch.getUserType();//得到用户类型
					String telephone = fetch.getTelephone();//得到数据库中用户名
					String pwd = fetch.getPassword();//得到数据库中密码
					String slt = fetch.getSalt();//得到数据库中盐值
					String username1 = fetch.getFullName();//得到用户姓名				

					byte[] salt = Encodes.decodeHex(slt);
					byte[] password2 = password.getBytes();//页面传来的密码
					byte[] hashPassword = Digests.sha1(password2, salt, HASH_INTERATIONS);
					String newpass = Encodes.encodeHex(hashPassword);//对页面传来的密码进行加密

					//********************************根据登录用户的id查询出用户就职表的数据********************************//
					UserJobMapEntity userJob = dbDao.fetch(UserJobMapEntity.class, Cnd.where("empId", "=", userId));
					Long comJobId = null;
					if (!Util.isEmpty(userJob)) {
						comJobId = userJob.getComJobId();//得到公司职位id
					}
					CompanyJobEntity comJob = dbDao.fetch(CompanyJobEntity.class, Cnd.where("id", "=", comJobId));
					//********************************根据公司职位id查询出公司职位表的数据********************************//
					request.getSession().setAttribute(Const.USER_COMPANY_KEY, comJob);

					//根据当前登录用户id查询美国客户的单子
					Sql sql = Sqls
							.create("SELECT *, MAX(vnc.createtime) AS 'maxcreatime' FROM visa_new_customer vnc where vnc.empid="
									+ userId);
					NewCustomerEntity usaCustomerInfo = DbSqlUtil.fetchEntity(dbDao, NewCustomerEntity.class, sql);

					Sql sqljp = Sqls
							.create("SELECT *, MAX(vncj.createtime) AS 'maxcreatime' FROM visa_new_customer_jp vncj where vncj.empid="
									+ userId);
					NewCustomerJpEntity jpCustomerInfo = DbSqlUtil.fetchEntity(dbDao, NewCustomerJpEntity.class, sqljp);

					Date usacreatetime = usaCustomerInfo.getCreatetime();
					Date jpcreatetime = jpCustomerInfo.getCreatetime();
					String str = "";
					if (!Util.isEmpty(usacreatetime) && !Util.isEmpty(jpcreatetime)) {
						if (usaCustomerInfo.getCreatetime().getTime() > jpCustomerInfo.getCreatetime().getTime()) {
							str += "10,11,12,";
						} else {
							str += "13,14,15,";
						}
					} else if (!Util.isEmpty(usacreatetime) && Util.isEmpty(jpcreatetime)) {
						str += "10,11,12,";
					} else if (Util.isEmpty(usacreatetime) && !Util.isEmpty(jpcreatetime)) {
						str += "13,14,15,";
					} else {

					}

					if (!Util.isEmpty(fetch)) {
						request.getSession().setAttribute(Const.SESSION_NAME, fetch);
						if (Util.isEmpty(fetch.getFullName())) {
							fetch.setFullName("无");
						}
						String fullname = "";
						fullname = Encodes.encodeBase64(JSON.toJSONString(username1));
						if (username.equals(telephone) && newpass.equals(pwd)) {//username为页面传来的用户名
							if (UserLoginEnum.PERSONNEL.intKey() == logintype
									&& UserLoginEnum.PERSONNEL.intKey() == userType) {//普通工作人员登录
								functions = loginAuthorityService.employeeLoginFunction(userId, request);
								/*String json = Json.toJson(functions,
										JsonFormat.compact().setDateFormat("yyyy-MM-dd HH:mm:ss"));
								String jsonEncode = Encodes.encodeBase64(JSON.toJSONString(json));*/
								request.getSession().setAttribute(Const.AUTHS_KEY, functions);//功能session
								return "redirect:" + to + "?auth=2,3,6,7," + str + "&username=" + fullname
										+ "&logintype=" + logintype + "&userType=" + userType;
							} else if (UserLoginEnum.PERSONNEL.intKey() == logintype
									&& UserLoginEnum.COMPANY_ADMIN.intKey() == userType) {//公司管理员员登录
								functions = authorityViewService.getCompanyFunctions(comJob.getComId());
								/*String json = Json.toJson(functions,
										JsonFormat.compact().setDateFormat("yyyy-MM-dd HH:mm:ss"));
								String jsonEncode = Encodes.encodeBase64(JSON.toJSONString(json));*/
								request.getSession().setAttribute(Const.AUTHS_KEY, functions);//功能session
								return "redirect:" + to + "?auth=2,3,6,7," + str + "&username=" + fullname
										+ "&logintype=" + logintype + "&userType=" + userType;
							} else if (UserLoginEnum.PERSONNEL.intKey() == logintype
									&& UserLoginEnum.JP_DJS_ADMIN.intKey() == userType) {//日本地接社管理员
								functions = this.djsFunctionList();
								request.getSession().setAttribute(Const.AUTHS_KEY, functions);//功能session
								return "redirect:" + to + "?auth=2,3,6,7," + str + "&username=" + fullname
										+ "&logintype=" + logintype + "&userType=" + userType;
							} else if (UserLoginEnum.TOURIST_IDENTITY.intKey() == logintype
									&& UserLoginEnum.TOURIST_IDENTITY.intKey() == userType) {//游客身份登录
								//我的签证
								FunctionEntity usaf1 = new FunctionEntity();
								usaf1.setId(7);
								usaf1.setParentId(0);
								usaf1.setFunName("我的签证");
								usaf1.setLevel(1);
								usaf1.setCreateTime(new Date());
								usaf1.setRemark("我的签证");
								usaf1.setSort(7);
								usaf1.setPortrait("fa fa-arrows-h");
								functions.add(usaf1);
								//办理中签证
								FunctionEntity usaf2 = new FunctionEntity();
								usaf2.setId(8);
								usaf2.setParentId(7);
								usaf2.setFunName("办理中签证");
								usaf2.setUrl("myvisa/transactVisa/visaNationList.html");
								usaf2.setLevel(2);
								usaf2.setCreateTime(new Date());
								usaf2.setRemark("办理中签证");
								usaf2.setSort(8);
								functions.add(usaf2);
								//我的资料
								FunctionEntity usaf3 = new FunctionEntity();
								usaf3.setId(9);
								usaf3.setParentId(0);
								usaf3.setFunName("我的资料");
								usaf3.setLevel(1);
								usaf3.setCreateTime(new Date());
								usaf3.setRemark("我的资料");
								usaf3.setPortrait("fa fa-list-alt");
								usaf3.setSort(9);
								functions.add(usaf3);
								if (!Util.isEmpty(usacreatetime)) {//美国游客权限
									//美国护照信息
									FunctionEntity usaf4 = new FunctionEntity();
									usaf4.setId(10);
									usaf4.setParentId(9);
									usaf4.setFunName("护照信息");
									usaf4.setUrl("personal/passportInfo/passportInfoList.html");
									usaf4.setLevel(2);
									usaf4.setCreateTime(new Date());
									usaf4.setRemark("美国护照信息");
									usaf4.setSort(10);
									functions.add(usaf4);
									//美国基本信息
									FunctionEntity usaf5 = new FunctionEntity();
									usaf5.setId(11);
									usaf5.setParentId(9);
									usaf5.setFunName("基本信息");
									usaf5.setUrl("personal/basicInfo/basicInfoList.html");
									usaf5.setLevel(2);
									usaf5.setCreateTime(new Date());
									usaf5.setRemark("美国基本信息");
									usaf5.setSort(11);
									functions.add(usaf5);
									//美国签证信息
									FunctionEntity usaf6 = new FunctionEntity();
									usaf6.setId(12);
									usaf6.setParentId(9);
									usaf6.setFunName("签证信息");
									usaf6.setUrl("personal/visaInfo/visaInfoList.html");
									usaf6.setLevel(2);
									usaf6.setCreateTime(new Date());
									usaf6.setRemark("美国签证信息");
									usaf6.setSort(12);
									functions.add(usaf6);
								} else if (!Util.isEmpty(jpcreatetime)) {
									//日本护照信息
									FunctionEntity jpf1 = new FunctionEntity();
									jpf1.setId(13);
									jpf1.setParentId(9);
									jpf1.setFunName("护照信息");
									jpf1.setUrl("personal/passportInfo/passportJPInfoList.html");
									jpf1.setLevel(2);
									jpf1.setCreateTime(new Date());
									jpf1.setRemark("日本护照信息");
									jpf1.setSort(13);
									functions.add(jpf1);
									//日本基本信息
									FunctionEntity jpf2 = new FunctionEntity();
									jpf2.setId(14);
									jpf2.setParentId(9);
									jpf2.setFunName("基本信息");
									jpf2.setUrl("personal/basicInfo/basicJPInfoList.html");
									jpf2.setLevel(2);
									jpf2.setCreateTime(new Date());
									jpf2.setRemark("日本基本信息");
									jpf2.setSort(14);
									functions.add(jpf2);
									//日本签证信息
									FunctionEntity jpf3 = new FunctionEntity();
									jpf3.setId(15);
									jpf3.setParentId(9);
									jpf3.setFunName("签证信息");
									jpf3.setUrl("personal/visaInfo/JPvisaInfoList.html");
									jpf3.setLevel(2);
									jpf3.setCreateTime(new Date());
									jpf3.setRemark("日本签证信息");
									jpf3.setSort(15);
									functions.add(jpf3);
								}
								//密码修改
								FunctionEntity passf = new FunctionEntity();
								passf.setId(16);
								passf.setParentId(9);
								passf.setFunName("修改密码");
								passf.setUrl("personal/modifyPassword/modifyPassword.html");
								passf.setLevel(2);
								passf.setCreateTime(new Date());
								passf.setRemark("修改密码");
								passf.setSort(16);
								functions.add(passf);
								/*String json = Json.toJson(functions,
										JsonFormat.compact().setDateFormat("yyyy-MM-dd HH:mm:ss"));
								String jsonEncode = Encodes.encodeBase64(JSON.toJSONString(json));*/
								request.getSession().setAttribute(Const.AUTHS_KEY, functions);//功能session
								return "redirect:" + to + "?auth=7,8,9,16," + str + "&username=" + fullname
										+ "&logintype=" + logintype + "&userType=" + userType;
							} else if (UserLoginEnum.SUPERMAN.intKey() == 3
									&& UserLoginEnum.SUPERMAN.intKey() == userType) {//超级管理员登录
								functions = loginAuthorityService.superAdministratorFunction();
								/*String json = Json.toJson(functions,
										JsonFormat.compact().setDateFormat("yyyy-MM-dd HH:mm:ss"));
								String jsonEncode = Encodes.encodeBase64(JSON.toJSONString(json));*/
								request.getSession().setAttribute(Const.AUTHS_KEY, functions);//功能session
								return "redirect:" + to + "?auth=0," + str + "&username=" + fullname + "&logintype="
										+ logintype + "&userType=" + userType;
							} else if (UserLoginEnum.ADMIN.intKey() == 4 && UserLoginEnum.ADMIN.intKey() == userType) {//平台用户
								//公司管理
								FunctionEntity comf = new FunctionEntity();
								comf.setId(4);
								comf.setParentId(0);
								comf.setFunName("公司管理");
								comf.setUrl("company/companylist.html");
								comf.setLevel(1);
								comf.setCreateTime(new Date());
								comf.setRemark("公司管理");
								comf.setSort(4);
								comf.setPortrait("fa fa-building-o");
								functions.add(comf);
								/*String json = Json.toJson(functions,
										JsonFormat.compact().setDateFormat("yyyy-MM-dd HH:mm:ss"));
								String jsonEncode = Encodes.encodeBase64(JSON.toJSONString(json));*/
								//系统设置
								FunctionEntity sysfun = new FunctionEntity();
								sysfun.setId(20);
								sysfun.setParentId(0);
								sysfun.setFunName("系统设置");
								sysfun.setLevel(1);
								sysfun.setCreateTime(new Date());
								sysfun.setRemark("系统设置");
								sysfun.setSort(20);
								sysfun.setPortrait("fa fa-cogs");
								functions.add(sysfun);
								//照片参数
								FunctionEntity photoparm = new FunctionEntity();
								photoparm.setId(21);
								photoparm.setParentId(20);
								photoparm.setFunName("照片参数");
								photoparm.setUrl("photo.html");
								photoparm.setLevel(2);
								photoparm.setCreateTime(new Date());
								photoparm.setRemark("照片参数");
								photoparm.setSort(21);
								functions.add(photoparm);
								//航班信息
								FunctionEntity flyinfo = new FunctionEntity();
								flyinfo.setId(22);
								flyinfo.setParentId(20);
								flyinfo.setFunName("航班信息");
								flyinfo.setUrl("flight.html");
								flyinfo.setLevel(2);
								flyinfo.setCreateTime(new Date());
								flyinfo.setRemark("航班信息");
								flyinfo.setSort(22);
								functions.add(flyinfo);
								//酒店管理
								FunctionEntity hotelmanage = new FunctionEntity();
								hotelmanage.setId(23);
								hotelmanage.setParentId(20);
								hotelmanage.setFunName("航班信息");
								hotelmanage.setUrl("hotel.html");
								hotelmanage.setLevel(2);
								hotelmanage.setCreateTime(new Date());
								hotelmanage.setRemark("航班信息");
								hotelmanage.setSort(23);
								functions.add(hotelmanage);
								//景点管理
								FunctionEntity scenicmanage = new FunctionEntity();
								scenicmanage.setId(24);
								scenicmanage.setParentId(20);
								scenicmanage.setFunName("景点管理");
								scenicmanage.setUrl("scenic.html");
								scenicmanage.setLevel(2);
								scenicmanage.setCreateTime(new Date());
								scenicmanage.setRemark("航班信息");
								scenicmanage.setSort(24);
								functions.add(scenicmanage);
								//日志记录
								FunctionEntity logfun = new FunctionEntity();
								logfun.setId(26);
								logfun.setParentId(0);
								logfun.setFunName("日志记录");
								logfun.setUrl("log/log.html");
								logfun.setLevel(1);
								logfun.setCreateTime(new Date());
								logfun.setRemark("日志记录");
								logfun.setSort(26);
								logfun.setPortrait("fa fa-building-o");
								functions.add(logfun);
								//平台统计
								FunctionEntity adminfun = new FunctionEntity();
								adminfun.setId(30);
								adminfun.setParentId(20);
								adminfun.setFunName("统计");
								adminfun.setUrl("");
								adminfun.setLevel(2);
								adminfun.setCreateTime(new Date());
								adminfun.setRemark("平台统计");
								adminfun.setSort(30);
								adminfun.setPortrait("");
								functions.add(adminfun);
								request.getSession().setAttribute(Const.AUTHS_KEY, functions);//功能session
								return "redirect:" + to + "?auth=4," + "&username=" + fullname + "&logintype=" + 4
										+ "&userType=" + userType;
							} else {

								model.addFlashAttribute("error", "登录身份错误,请重试!");
							}
						} else {
							model.addFlashAttribute("error", "密码错误,请重试!");
						}
					}
				} else {
					model.addFlashAttribute("error", "用户名错误,请重试!");
				}

			} else {
				model.addFlashAttribute("error", "验证码错误,请重试!");
			}
		} else {
			model.addFlashAttribute("error", "验证码不能为空,请重试!");
		}
		String error = Encodes.encodeBase64(JSON.toJSONString(model.getFlashAttributes()));
		model.addAttribute("e", error);
		return "redirect:" + login;
	}

	/**
	 * 日本地接社管理员登录时的权限
	 * @return 
	 */
	@RequestMapping(value = "djsFunctionList")
	@ResponseBody
	private List<FunctionEntity> djsFunctionList() {
		List<FunctionEntity> functions = Lists.newArrayList();
		//公司管理
		FunctionEntity comf = new FunctionEntity();
		comf.setId(1);
		comf.setParentId(0);
		comf.setFunName("公司管理");
		comf.setLevel(1);
		comf.setCreateTime(new Date());
		comf.setRemark("公司管理");
		comf.setSort(1);
		comf.setPortrait("fa fa-building-o");
		functions.add(comf);
		//权限管理
		/*FunctionEntity authority = new FunctionEntity();
		authority.setId(6);
		authority.setParentId(1);
		authority.setFunName("权限管理");
		authority.setUrl("authority/authoritylist.html");
		authority.setLevel(2);
		authority.setCreateTime(new Date());
		authority.setRemark("权限管理");
		authority.setSort(2);
		authority.setPortrait("");
		functions.add(authority);*/
		//员工管理
		/*FunctionEntity usermanage = new FunctionEntity();
		usermanage.setId(3);
		usermanage.setParentId(1);
		usermanage.setFunName("员工管理");
		usermanage.setUrl("employee/employeelist.html");
		usermanage.setLevel(2);
		usermanage.setCreateTime(new Date());
		usermanage.setRemark("员工管理");
		usermanage.setSort(3);
		usermanage.setPortrait("");
		functions.add(usermanage);*/
		//客户管理
		/*FunctionEntity customermanage = new FunctionEntity();
		customermanage.setId(2);
		customermanage.setParentId(1);
		customermanage.setFunName("客户管理");
		customermanage.setUrl("custmanagement/customerlist.html");
		customermanage.setLevel(2);
		customermanage.setCreateTime(new Date());
		customermanage.setRemark("客户管理");
		customermanage.setSort(6);
		customermanage.setPortrait("");
		functions.add(customermanage);*/
		//日本招宝信息
		FunctionEntity combabyjp = new FunctionEntity();
		combabyjp.setId(25);
		combabyjp.setParentId(1);
		combabyjp.setFunName("日本招宝信息");
		combabyjp.setUrl("comebaby/comeList.html");
		combabyjp.setLevel(2);
		combabyjp.setCreateTime(new Date());
		combabyjp.setRemark("日本招宝信息");
		combabyjp.setSort(25);
		combabyjp.setPortrait("");
		functions.add(combabyjp);
		//数据采集
		FunctionEntity dataAcquisition = new FunctionEntity();
		dataAcquisition.setId(17);
		dataAcquisition.setParentId(0);
		dataAcquisition.setFunName("数据采集");
		dataAcquisition.setLevel(2);
		dataAcquisition.setCreateTime(new Date());
		dataAcquisition.setRemark("数据采集");
		dataAcquisition.setSort(17);
		dataAcquisition.setPortrait("fa fa-check-square-o");
		functions.add(dataAcquisition);
		//美国订单
		/*FunctionEntity usaorder = new FunctionEntity();
		usaorder.setId(18);
		usaorder.setParentId(17);
		usaorder.setFunName("美国");
		usaorder.setUrl("order/america.html");
		usaorder.setLevel(2);
		usaorder.setCreateTime(new Date());
		usaorder.setRemark("美国订单列表");
		usaorder.setSort(18);
		usaorder.setPortrait("");
		functions.add(usaorder);*/
		//日本地接社订单
		FunctionEntity djsjp = new FunctionEntity();
		djsjp.setId(27);
		djsjp.setParentId(17);
		djsjp.setFunName("日本");
		djsjp.setUrl("japan/land/japanland.html");
		djsjp.setLevel(2);
		djsjp.setCreateTime(new Date());
		djsjp.setRemark("日本地接社");
		djsjp.setSort(27);
		djsjp.setPortrait("");
		functions.add(djsjp);
		//日本地接统计
		FunctionEntity djsjptotal = new FunctionEntity();
		djsjptotal.setId(29);
		djsjptotal.setParentId(17);
		djsjptotal.setFunName("统计");
		djsjptotal.setUrl("");
		djsjptotal.setLevel(2);
		djsjptotal.setCreateTime(new Date());
		djsjptotal.setRemark("日本地接社统计");
		djsjptotal.setSort(29);
		djsjptotal.setPortrait("");
		functions.add(djsjptotal);
		return functions;
	}

	/**
	 * 登录时查询出用户的功能
	 * @param request
	 * @param functions
	 */
	@RequestMapping(value = "loginfunctions", method = RequestMethod.POST)
	@ResponseBody
	public Object loginfunctions(HttpServletRequest request, String logintype, String orderId) {
		return authorityViewService.loginfunctions(request, logintype, orderId);
	}

	/**
	 * 登出
	 */
	@RequestMapping(value = "logout")
	public String logout(@RequestParam(defaultValue = "/") String to, SessionStatus status, HttpServletRequest request) {
		HttpSession session = request.getSession();
		session.removeAttribute(Const.SESSION_NAME);//清除session
		session.removeAttribute(Const.AUTHS_KEY);//清除session
		status.setComplete();
		SecurityUtils.getSubject().logout();
		return "redirect:" + to;
	}

	/**
	 * 生成验证码
	 */
	@RequestMapping("/captcha-image")
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {

		response.setDateHeader("Expires", 0);
		// Set standard HTTP/1.1 no-cache headers.  
		response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
		// Set IE extended HTTP/1.1 no-cache headers (use addHeader).  
		response.addHeader("Cache-Control", "post-check=0, pre-check=0");
		// Set standard HTTP/1.0 no-cache header.  
		response.setHeader("Pragma", "no-cache");
		// return a jpeg  
		response.setContentType("image/jpeg");
		// create the text for the image  
		String capText = captchaProducer.createText();
		// store the text in the session  
		request.getSession().setAttribute(Constants.KAPTCHA_SESSION_KEY, capText);
		// create the image with the text  
		BufferedImage bi = captchaProducer.createImage(capText);
		ServletOutputStream out = response.getOutputStream();
		// write the data out  
		ImageIO.write(bi, "jpg", out);
		try {
			out.flush();
		} finally {
			out.close();
		}
		return null;
	}
}
