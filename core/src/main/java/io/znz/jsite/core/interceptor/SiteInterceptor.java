package io.znz.jsite.core.interceptor;

import io.znz.jsite.core.bean.Site;
import io.znz.jsite.core.service.SiteService;
import io.znz.jsite.core.util.ThreadLocalUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 上下文信息拦截器
 * 站点信息
 */
public class SiteInterceptor extends HandlerInterceptorAdapter {

    /**
     * 默认忽略URL
     */
    private static final String[] DEFAULT_IGNORE_URL_PATTERNS = new String[]{"/a/**"};
    /**
     * 忽略URL
     */
    private String[] ignoreUrlPatterns = DEFAULT_IGNORE_URL_PATTERNS;
    /**
     * antPathMatcher
     */
    private static AntPathMatcher antPathMatcher = new AntPathMatcher();

    /**
     * 重定向URL
     */
    private String redirectUrl = "/common/site_close.jhtml";

    public static final String KEY_SITE = "site";

    @Autowired
    private SiteService siteService;

    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse resp, Object handler
    ) throws ServletException, IOException {
        Site site = null;
        List<Site> list = siteService.getAll();
        int size = list.size();
        if (size == 1) {
            site = list.get(0);
        } else {
            String server = req.getServerName();
            for (Site s : list) {
                // 检查域名
                if (server.equals(s.getDomain())) {
                    site = s;
                    break;
                }
                // 检查域名别名
                String alias = s.getAlias();
                if (!StringUtils.isBlank(alias)) {
                    for (String a : StringUtils.split(alias, ',')) {
                        if (a.equals(server)) {
                            site = s;
                            break;
                        }
                    }
                }
            }
        }
        if (site != null) {
            req.setAttribute(KEY_SITE, site);
            ThreadLocalUtil.setSite(site);
            if (site.isClose()) {
                String path = req.getServletPath();
                if (!redirectUrl.equals(path)) {
                    if (ignoreUrlPatterns != null) {
                        for (String ignoreUrlPattern : ignoreUrlPatterns) {
                            if (!antPathMatcher.match(ignoreUrlPattern, path)) {
                                resp.sendRedirect(req.getContextPath() + redirectUrl);
                                return false;
                            }
                        }
                    }
                }
            }
        }
        return true;
    }
}
