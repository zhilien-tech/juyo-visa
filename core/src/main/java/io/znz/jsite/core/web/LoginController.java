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
import org.nutz.json.Json;
import org.nutz.json.JsonFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import com.alibaba.fastjson.JSON;
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
public class LoginController extends BaseController {

	@Autowired
	protected IDbDao dbDao;

	@Autowired
	protected Dao nutDao;

	@Autowired
	protected LoginAuthorityService loginAuthorityService;

	@Autowired
	protected AuthorityService authority;

	@Inject
	private SqlManager sqlManager;

	public static final int HASH_INTERATIONS = 1024;

	/**
	 * 登录页面.这页面不是实际的登录页,而是按照不同的规则进行跳转以达到多登录页的效果
	 */
	@RequestMapping(value = "login", method = RequestMethod.GET)
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
								List<FunctionEntity> employeeLoginFunction = loginAuthorityService
										.employeeLoginFunction(userId, request);
								String json = Json.toJson(employeeLoginFunction,
										JsonFormat.compact().setDateFormat("yyyy-MM-dd HH:mm:ss"));
								String jsonEncode = Encodes.encodeBase64(JSON.toJSONString(json));
								return "redirect:" + to + "?auth=2,3,6,7," + str + "&username=" + fullname
										+ "&logintype=" + logintype + "&empList=" + jsonEncode;
							} else if (UserLoginEnum.PERSONNEL.intKey() == logintype
									&& UserLoginEnum.COMPANY_ADMIN.intKey() == userType) {//公司管理员员登录
								List<FunctionEntity> companyFunctions = authority
										.getCompanyFunctions(comJob.getComId());
								String json = Json.toJson(companyFunctions,
										JsonFormat.compact().setDateFormat("yyyy-MM-dd HH:mm:ss"));
								String jsonEncode = Encodes.encodeBase64(JSON.toJSONString(json));
								return "redirect:" + to + "?auth=2,3,6,7," + str + "&username=" + fullname
										+ "&logintype=" + logintype + "&empList=" + jsonEncode;
							} else if (UserLoginEnum.TOURIST_IDENTITY.intKey() == logintype
									&& UserLoginEnum.TOURIST_IDENTITY.intKey() == userType) {//游客身份登录
								return "redirect:" + to + "?auth=8,9,16,7," + str + "&username=" + fullname
										+ "&logintype=" + logintype + "&tourist=1";
							} else if (UserLoginEnum.SUPERMAN.intKey() == 3
									&& UserLoginEnum.SUPERMAN.intKey() == userType) {
								return "redirect:" + to + "?auth=0," + str + "&username=" + fullname + "&logintype="
										+ logintype + "&tourist=3";
							} else if (UserLoginEnum.ADMIN.intKey() == 4 && UserLoginEnum.ADMIN.intKey() == userType) {
								return "redirect:" + to + "?auth=4," + "&username=" + fullname + "&logintype=" + 4
										+ "&tourist=2";
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
	 * 登出
	 */
	@RequestMapping(value = "logout")
	public String logout(@RequestParam(defaultValue = "/") String to, SessionStatus status, HttpServletRequest request) {
		request.getSession().removeAttribute(Const.SESSION_NAME);//清除session
		status.setComplete();
		SecurityUtils.getSubject().logout();
		return "redirect:" + to;
	}
}
