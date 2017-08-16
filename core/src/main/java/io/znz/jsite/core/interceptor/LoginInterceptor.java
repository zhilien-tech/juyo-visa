/**
 * LoginInterceptor.java
 * io.znz.jsite.core.interceptor
 * Copyright (c) 2017, 北京科技有限公司版权所有.
*/

package io.znz.jsite.core.interceptor;

import io.znz.jsite.core.entity.EmployeeEntity;
import io.znz.jsite.core.util.Const;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.uxuexi.core.common.util.Util;

/**
 * 登录拦截器
 * @author   孙斌
 * @Date	 2017年6月25日 	 
 */
@Component("loginInterceptor")
public class LoginInterceptor extends HandlerInterceptorAdapter {

	/**
	 * (non-Javadoc)
	 * @see org.springframework.web.servlet.handler.HandlerInterceptorAdapter#preHandle(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object)
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		String requestUri = request.getRequestURL().toString();
		String contextPath = request.getContextPath();
		String url = requestUri.substring(contextPath.length());
		String queryString = request.getQueryString();
		if (!Util.isEmpty(url)) {
			if (url.contains(".map")) {
				return true;
			}
		}
		if (!Util.isEmpty(url)) {
			if (url.contains(".ttf")) {
				return true;
			}
		}
		if (!Util.isEmpty(url)) {
			if (url.contains(".woff2")) {
				return true;
			}
		}
		if (!Util.isEmpty(queryString)) {
			boolean contains = queryString.contains("logintype=5");
			if (contains) {
				return true;
			}
		}
		//String url = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
		/*if (nowurl.equals(request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/")) {
			return true;
		}*/
		// 判断是否是首次登陆 
		//String requestURI = request.getRequestURI().toLowerCase();
		//boolean isLogin = requestURI.indexOf("login") >= 0;

		//从session中取出当前登录用户信息
		EmployeeEntity user = (EmployeeEntity) request.getSession().getAttribute(Const.SESSION_NAME);
		if (Util.isEmpty(user)) {
			//request.getRequestDispatcher("/login/logout").forward(request, response);
			response.sendRedirect("/index.html?aaa=1" + "&bbb=2");
			return false;
		}
		return true;
	}

}
