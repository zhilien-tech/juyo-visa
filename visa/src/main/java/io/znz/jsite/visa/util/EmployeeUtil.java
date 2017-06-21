package io.znz.jsite.visa.util;

import io.znz.jsite.visa.entity.user.EmployeeEntity;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.SessionKey;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.uxuexi.core.common.util.Util;

public class EmployeeUtil {

	/**
	 * 获取当前用户session
	 *
	 * @return session
	 */
	public static Session getSession() {
		Session session = null;
		try {
			HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
					.getRequest();
			session = SecurityUtils.getSecurityManager().getSession(new TokenSessionKey(request.getHeader("token")));
		} catch (Exception e) {
		}
		if (session == null) {
			session = SecurityUtils.getSubject().getSession(true);
		}
		return session;
	}

	/**
	 * 获取当前用户对象
	 *
	 * @return user
	 */
	public static EmployeeEntity getUser() {
		Session session = getSession();
		EmployeeEntity user = (EmployeeEntity) session.getAttribute(Const.CURRENT_USER);
		if (user != null && !Util.isEmpty(user.getId())) {
			user = null;
			session.removeAttribute(Const.CURRENT_USER);
		}
		return user;
	}

	static class TokenSessionKey implements SessionKey {
		String token;

		public TokenSessionKey(String token) {
			this.token = token;
		}

		public Serializable getSessionId() {
			return token;
		}
	}
}
