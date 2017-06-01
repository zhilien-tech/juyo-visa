package io.znz.jsite.util;

import javax.servlet.http.HttpServletRequest;
import java.io.File;

/**
 * Created by Chaly on 16/8/2.
 */
public class RequestUtil {
    public static StringBuilder getServerPath(HttpServletRequest request) {
        String url = request.getRequestURL().toString();
        String uri = request.getRequestURI();
        String domain = url.replace(uri, "");
        StringBuilder path = new StringBuilder(domain.replaceAll(":8000", ""));
        if (StringUtils.isNotBlank(request.getContextPath())) {
            path.append(request.getContextPath());
        }
        return path;
    }

    public static File getRealPath(HttpServletRequest request, String child) {
        String root = request.getServletContext().getRealPath(child.startsWith("/") ? child : "/" + child);
        return new File(root);
    }
}
