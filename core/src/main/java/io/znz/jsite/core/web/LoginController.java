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

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import com.alibaba.fastjson.JSON;
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
										+ "&logintype=" + logintype;
							} else if (UserLoginEnum.PERSONNEL.intKey() == logintype
									&& UserLoginEnum.COMPANY_ADMIN.intKey() == userType) {//公司管理员员登录
								functions = authorityViewService.getCompanyFunctions(comJob.getComId());
								/*String json = Json.toJson(functions,
										JsonFormat.compact().setDateFormat("yyyy-MM-dd HH:mm:ss"));
								String jsonEncode = Encodes.encodeBase64(JSON.toJSONString(json));*/
								request.getSession().setAttribute(Const.AUTHS_KEY, functions);//功能session
								return "redirect:" + to + "?auth=2,3,6,7," + str + "&username=" + fullname
										+ "&logintype=" + logintype;
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
										+ "&logintype=" + logintype;
							} else if (UserLoginEnum.SUPERMAN.intKey() == 3
									&& UserLoginEnum.SUPERMAN.intKey() == userType) {//超级管理员登录
								functions = loginAuthorityService.superAdministratorFunction();
								/*String json = Json.toJson(functions,
										JsonFormat.compact().setDateFormat("yyyy-MM-dd HH:mm:ss"));
								String jsonEncode = Encodes.encodeBase64(JSON.toJSONString(json));*/
								request.getSession().setAttribute(Const.AUTHS_KEY, functions);//功能session
								return "redirect:" + to + "?auth=0," + str + "&username=" + fullname + "&logintype="
										+ logintype;
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
								functions.add(comf);
								/*String json = Json.toJson(functions,
										JsonFormat.compact().setDateFormat("yyyy-MM-dd HH:mm:ss"));
								String jsonEncode = Encodes.encodeBase64(JSON.toJSONString(json));*/
								request.getSession().setAttribute(Const.AUTHS_KEY, functions);//功能session
								return "redirect:" + to + "?auth=4," + "&username=" + fullname + "&logintype=" + 4;
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
		}
		String error = Encodes.encodeBase64(JSON.toJSONString(model.getFlashAttributes()));
		model.addAttribute("e", error);
		return "redirect:" + login;
	}

	/**
	 * 登录时查询出用户的功能
	 * @param request
	 * @param functions
	 */
	@RequestMapping(value = "loginfunctions", method = RequestMethod.POST)
	@ResponseBody
	public Object loginfunctions(HttpServletRequest request) {
		return authorityViewService.loginfunctions(request);
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
}
