package io.znz.jsite.core.filter;

import io.znz.jsite.core.bean.User;
import io.znz.jsite.core.service.UserService;
import io.znz.jsite.core.util.Const;
import io.znz.jsite.core.util.UserUtil;
import io.znz.jsite.ext.shiro.UserRealm;
import io.znz.jsite.util.SpringUtil;
import io.znz.jsite.util.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Chaly on 16/8/6.
 */
public class SessionFilter extends OncePerRequestFilter {

    private UserService userService;

    public UserService getUserService() {
        if (userService == null) {
            userService = SpringUtil.getBean("userService", UserService.class);
        }
        return userService;
    }

    protected void doFilterInternal(final HttpServletRequest request, HttpServletResponse response, FilterChain chain
    ) throws ServletException, IOException {
        String url = request.getRequestURL().toString();
        if (!StringUtils.containsAny(url, "/res/", "logout", "\\.ico")) {
            Subject subject = SecurityUtils.getSubject();
            User currentUser = UserUtil.getUser();
            //判断session是否失效，若失效刷新之
            if (currentUser == null && (subject.isRemembered())) {
                UserRealm.ShiroUser shiroUser = (UserRealm.ShiroUser) subject.getPrincipal();
                User user = getUserService().getUser(shiroUser.toString());
                user.getRoleName();
                UserUtil.getSession().setAttribute(Const.CURRENT_USER, user);
            }
        }
        // 把处理发送到下一个过滤器
        try {
            chain.doFilter(request, response);
        } catch (Exception e) {
        }
    }
}
