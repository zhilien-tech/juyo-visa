package io.znz.jsite.base;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import io.znz.jsite.base.bean.ResultObject;
import io.znz.jsite.core.util.Const;
import io.znz.jsite.exception.JSiteException;
import io.znz.jsite.util.DateUtils;
import io.znz.jsite.util.StringUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.beans.PropertyEditorSupport;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.*;


/**
 * 基础控制器
 * 其他控制器继承此控制器获得日期字段类型转换和防止XSS攻击的功能
 *
 * @author Chaly
 */
public class BaseController {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        // String类型转换，将所有传递进来的String进行HTML编码，防止XSS攻击
        binder.registerCustomEditor(String.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                setValue(text == null ? null : StringEscapeUtils.escapeHtml4(text.trim()));
            }

            @Override
            public String getAsText() {
                Object value = getValue();
                return value != null ? value.toString().trim() : "";
            }
        });

        // Date 类型转换
        binder.registerCustomEditor(Date.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                Object value = DateUtils.parseDate(text);
                if (value == null) {
                    value = DateUtils.parseDate(text, Locale.ENGLISH);
                }
                setValue(value);
            }
        });

        // Timestamp 类型转换
        binder.registerCustomEditor(Timestamp.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                Date date = DateUtils.parseDate(text);
                setValue(date == null ? null : new Timestamp(date.getTime()));
            }
        });
    }

    protected String getTpl(String tpl) {
        return tpl + Const.SUFFIX_FTL;
    }

    /**
     * 异常处理
     */
    @ExceptionHandler(Exception.class)
    public void exceptionHandler(HttpServletRequest request, HttpServletResponse response, Exception e) throws Exception {
        e.printStackTrace();
        response.setContentType("application/json;charset=UTF-8");
        String msg = e.getMessage();
        if (!(e instanceof JSiteException) && !(e instanceof IllegalArgumentException)) {
            msg = "系统异常,如一直出现出现此问题请联系管理员!";
        }
        String json = JSON.toJSONString(ResultObject.error(msg), SerializerFeature.DisableCircularReferenceDetect);
        request.setAttribute("msg", msg);
        PrintWriter out = response.getWriter();
        out.print(json);
        out.close();
    }

    /**
     * 获取page对象
     *
     * @return page对象
     */
    public Pageable getPageable(HttpServletRequest request) {
        int pageNo = 1;    //当前页码
        int pageSize = 20;    //每页行数
        String orderBy = "id";    //排序字段
        String order = "asc";    //排序顺序
        String page = request.getParameter("page");
        if (StringUtils.isNotEmpty(page)) pageNo = Integer.valueOf(page);
        String size = request.getParameter("pageSize");
        if (StringUtils.isNotEmpty(size)) pageSize = Integer.valueOf(size);

        String sort = request.getParameter("sort");
        if (StringUtils.isNotEmpty(sort)) orderBy = sort;
        String filter = request.getParameter("filter");
        if (StringUtils.isNotEmpty(filter)) order = filter;
        Pageable pageable = new PageRequest(pageNo - 1, pageSize, Sort.Direction.fromString(order), orderBy);
        return pageable;
    }

    /**
     * 获取easyui分页数据
     *
     * @return map对象
     */
    public <T> Map<String, Object> getEasyUIData(Page<T> page) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("rows", page.getContent());
        map.put("total", page.getTotalElements());
        return map;
    }

}
