package io.znz.jsite.core.filter;

import com.thetransactioncompany.cors.CORSFilter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Chaly on 16/9/22.
 */
//@WebFilter(urlPatterns = {"api/*"}, asyncSupported = true,
//    initParams = {
//        @WebInitParam(name = "cors.allowOrigin", value = "*"),
//        @WebInitParam(name = "cors.supportedMethods", value = "CONNECT, DELETE, GET, HEAD, OPTIONS, POST, PUT, TRACE"),
//        @WebInitParam(name = "cors.supportedHeaders", value = "token,Accept, Origin, X-Requested-With, Content-Type, Last-Modified"),//注意，如果token字段放在请求头传到后端，这里需要配置
//        @WebInitParam(name = "cors.exposedHeaders", value = "Set-Cookie"),
//        @WebInitParam(name = "cors.supportsCredentials", value = "true")
//    })
public class CrossOriginFilter extends CORSFilter implements Filter {
    public void init(FilterConfig config) throws ServletException {
        System.out.println("跨域资源处理过滤器初始化了");
        super.init(config);
    }

    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain
    ) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) res;
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "x-requested-with");
        chain.doFilter(req, res);
    }

}
