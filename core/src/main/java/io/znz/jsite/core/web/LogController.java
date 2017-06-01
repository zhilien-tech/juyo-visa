package io.znz.jsite.core.web;

import io.znz.jsite.base.BaseController;
import io.znz.jsite.base.PropertyFilter;
import io.znz.jsite.core.bean.Log;
import io.znz.jsite.core.service.LogService;
import io.znz.jsite.util.ExportExcel;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 日志controller
 * @author Chaly
 */
@Controller
@RequestMapping("sys/log")
public class LogController extends BaseController {

    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    @Autowired
    private LogService logService;
    @Autowired
    private ExportExcel<Log> exportExcel;

    /**
     * 默认页面
     */
    @RequestMapping(method = RequestMethod.GET)
    public String list() {
        return "sys/logList";
    }

    /**
     * 获取日志json
     */
    @RequiresPermissions("sys:log:view")
    @RequestMapping("json")
    @ResponseBody
    public Map<String, Object> list(HttpServletRequest request) {
        Pageable pageable = getPageable(request);
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request);
        if (filters.size() > 0) {
            pageable.first();
        }
        Page<Log> page = logService.search(pageable, filters);
        return getEasyUIData(page);
    }

    /**
     * 删除日志
     */
    @RequiresPermissions("sys:log:delete")
    @RequestMapping(value = "delete/{id}")
    @ResponseBody
    public String delete(@PathVariable("id") Integer id) {
        logService.delete(id);
        return "success";
    }

    /**
     * 批量删除日志
     */
    @RequiresPermissions("sys:log:delete")
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    @ResponseBody
    public String delete(@RequestBody List<Integer> idList) {
        logService.deleteLog(idList);
        return "success";
    }

    /**
     * 导出excel
     */
    @RequestMapping("exportExcel")
    public void exportExcel(HttpServletResponse response) throws Exception {
        response.setContentType("application/msexcel;charset=UTF-8");
        List<Log> list = logService.getAll();//获取数据
        String[] hearders = new String[]{"操作编码", "详细描述", "执行时间(mm)", "操作系统", "浏览器", "IP", "MAC", "操作者", "操作时间"};//表头数组
        String[] fields = new String[] {"operationCode", "description", "executeTime", "os", "browser", "ip","mac","creater","createDate"};//People对象属性数组
        String filename = "logs-"+format.format(new Date().getTime()) + ".xls";
        response.setContentType("application/ms-excel;charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=".concat(String.valueOf(URLEncoder.encode(filename, "UTF-8"))));
        OutputStream out = response.getOutputStream();
        try {
            exportExcel.exportExcel(filename, hearders, list, out,"yyyy-MM-dd HH:mm:ss",fields);
            System.out.println("success");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("error");
        } finally {
            out.close();
        }
    }
}
