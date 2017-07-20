/**
 * AuthorityInterceptor.java
 * io.znz.jsite.core.interceptor
 * Copyright (c) 2017, 北京科技有限公司版权所有.
*/

package io.znz.jsite.core.interceptor;

import io.znz.jsite.core.entity.EmployeeEntity;
import io.znz.jsite.core.entity.function.FunctionEntity;
import io.znz.jsite.core.service.authority.AuthorityService;
import io.znz.jsite.core.util.Const;
import io.znz.jsite.util.SpringUtil;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.uxuexi.core.common.util.StringUtil;
import com.uxuexi.core.common.util.Util;

/**
 * 权限拦截器
 * @author   崔建斌
 * @Date	 2017年7月19日 	 
 */
public class AuthorityInterceptor extends HandlerInterceptorAdapter {

	private Logger logger = LoggerFactory.getLogger(AuthorityInterceptor.class);

	/**请求路径后缀标志符*/
	public static final String REQ_TAIL_FLAG = ".";

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		String requestUri = request.getRequestURL().toString();
		String contextPath = request.getContextPath();
		String url = requestUri.substring(contextPath.length());

		boolean isAllowed = true;
		//获取访问路径，去掉参数
		String requestPath = request.getServletPath();

		HttpSession session = request.getSession();

		//从session中取出当前登录用户信息
		EmployeeEntity user = (EmployeeEntity) session.getAttribute(Const.SESSION_NAME);
		if (!Util.isEmpty(user) && Const.PLAT_ADMIN.equals(user.getFullName())) {
			return true;
		}

		isAllowed = hasPermission(session, requestPath);

		AuthorityService authorityService = SpringUtil.getBean("authorityViewService");
		FunctionEntity function = authorityService.findFuctionByRequestPath(requestPath);

		if (Util.isEmpty(function)) {
			isAllowed = true;
			logger.info("注意:访问地址:[" + requestPath + "]未设置未功能.");
		} else {
			logger.info("当前访问路径:[" + requestPath + "].");
			logger.info((null == user ? "--游客" : user.getFullName()) + "--访问后台功能:[" + function.getFunName()
					+ (isAllowed == true ? "]--允许" : "]--权限不足"));
		}

		if (!isAllowed) {
			String msg = "对不起，你没有访问该功能的权限！";
			//判断访问类型
			if (isAjax(request)) {
				//输出json字符串
				//response.getWriter().write(msg);
				response.getWriter().println(msg);
			} else {
				//跳转，提示没有权限
				response.sendRedirect("/error/auth_msg.jsp");
				//response.getRequestDispatcher("/views/error/auth_msg.jsp").forward(request, response);
			}
		}
		return true;
	}

	public boolean hasPermission(final HttpSession session, final String requestPath) {

		String reqPath = StringUtil.trimRight(requestPath, REQ_TAIL_FLAG);

		@SuppressWarnings("unchecked")
		List<FunctionEntity> functions = (List<FunctionEntity>) session.getAttribute(Const.AUTHS_KEY);
		if (Util.isEmpty(functions)) {
			return false;
		}
		for (FunctionEntity f : functions) {
			String furl = f.getUrl();
			furl = StringUtil.trimRight(furl, REQ_TAIL_FLAG);
			if (!Util.isEmpty(furl)) {
				furl = furl.trim();
			}
			if (reqPath.equals(furl)) {
				return true;
			}
		}
		return false;
	}

	private boolean isAjax(HttpServletRequest request) {
		String xHeader = request.getHeader("X-Requested-With");
		return (xHeader != null && "XMLHttpRequest".equals(xHeader.toString()));
	}
}
