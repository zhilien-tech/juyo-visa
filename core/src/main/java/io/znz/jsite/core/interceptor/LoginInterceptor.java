/**
 * LoginInterceptor.java
 * io.znz.jsite.core.interceptor
 * Copyright (c) 2017, 北京科技有限公司版权所有.
*/

package io.znz.jsite.core.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * TODO(这里用一句话描述这个类的作用)
 * <p>
 * TODO(这里描述这个类补充说明 – 可选)
 *
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

		/*		HttpSession session = request.getSession();
				Object attribute = session.getAttribute(Const.SESSION_NAME);
				String nowurl = request.getRequestURL().toString();
				String url = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
				if (nowurl.equals(request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/")) {
					return true;
				}
				if (Util.isEmpty(attribute)) {
					response.sendRedirect(url);
					return false;
				}*/

		return true;

	}

}
