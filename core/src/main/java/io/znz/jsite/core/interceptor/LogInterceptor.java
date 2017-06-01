package io.znz.jsite.core.interceptor;

import com.alibaba.fastjson.JSON;
import eu.bitwalker.useragentutils.UserAgent;
import io.znz.jsite.core.bean.Log;
import io.znz.jsite.core.service.LogService;
import io.znz.jsite.core.util.UserUtil;
import io.znz.jsite.util.DateUtils;
import io.znz.jsite.util.IPUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 日志拦截器
 * @author Chaly
 */
public class LogInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private LogService logService;

    private Long beginTime;// 1、开始时间
    private Long endTime;// 2、结束时间

    public boolean preHandle(
        HttpServletRequest request,
        HttpServletResponse response,
        Object handler
    ) throws Exception {
        beginTime = System.currentTimeMillis();//计时
        return true;
    }

    public void postHandle(
        HttpServletRequest request,
        HttpServletResponse response,
        Object handler,
        ModelAndView modelAndView
    ) throws Exception {
    }

    public void afterCompletion(
        HttpServletRequest request,
        HttpServletResponse response,
        Object handler, Exception ex
    ) throws Exception {
        endTime = System.currentTimeMillis();
        String requestRri = request.getRequestURI();
        String uriPrefix = request.getContextPath();
        String operationCode = StringUtils.substringAfter(requestRri, uriPrefix);    //操作编码
        String requestParam = JSON.toJSONString(request.getParameterMap());    //请求参数
        //如果是GET请求，请求编码包含create，update(添加修改页)不记录日志
        if (request.getMethod().equals("GET")) {
            if (operationCode.contains("create") || operationCode.contains("update")) {
                return;
            }
        }
        Long executeTime = endTime - beginTime;
        UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
        String os = userAgent.getOperatingSystem().getName();    //获取客户端操作系统
        String browser = userAgent.getBrowser().getName();    //获取客户端浏览器
        Log log = new Log();
        log.setOs(os);
        log.setBrowser(browser);
        log.setIp(IPUtil.getIpAddress(request));
        log.setOperationCode(operationCode);
        log.setExecuteTime(Integer.valueOf(executeTime.toString()));
        if (UserUtil.getUser() != null) {
            log.setCreater(UserUtil.getUser().getName());
        }
        log.setCreateDate(DateUtils.getSysTimestamp());
        log.setRequestParam(requestParam);
        logService.save(log);
    }

}
