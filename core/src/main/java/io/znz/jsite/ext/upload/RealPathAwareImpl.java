package io.znz.jsite.ext.upload;

import org.springframework.stereotype.Component;
import org.springframework.web.context.ServletContextAware;

import javax.servlet.ServletContext;

@Component("realPathAware")
public class RealPathAwareImpl implements RealPathAware, ServletContextAware {

    private ServletContext context;

    public void setServletContext(ServletContext servletContext) {
        this.context = servletContext;
    }

    public String getRealPath(String path) {
        String realpath = context.getRealPath(path);
        //tomcat8.0获取不到真实路径，通过/获取路径
        if (realpath == null) {
            realpath = context.getRealPath("/") + path;
        }
        return realpath;
    }

}
