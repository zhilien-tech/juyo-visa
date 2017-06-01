package io.znz.jsite.core.web;

import com.google.common.collect.Maps;
import com.sun.management.OperatingSystemMXBean;
import io.znz.jsite.base.BaseController;
import io.znz.jsite.base.bean.ResultObject;
import io.znz.jsite.core.bean.Role;
import io.znz.jsite.core.bean.User;
import io.znz.jsite.core.bean.UserRole;
import io.znz.jsite.core.service.RoleService;
import io.znz.jsite.core.service.UserService;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.web.util.WebUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.lang.management.ManagementFactory;
import java.util.*;

/**
 * 后台管理员登录后的首页controller
 * @author Chaly
 */
@Controller
@RequestMapping("${adminPath}")
public class AdminController extends BaseController implements ServletContextAware {

    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;

    private ServletContext servletContext;

    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    /**
     * 默认页面
     */
    @RequestMapping
    public String home(HttpServletRequest request, RedirectAttributes attribute) {
        //判断用户角色是否正确,如果不正确则重新登录
        if (!SecurityUtils.getSubject().hasRole("admin")) {
            WebUtils.saveRequest(request);
            attribute.addFlashAttribute("error", "角色不匹配,请重试!");
            return "redirect:/a/login";
        }
        return getTpl("sys/index");
    }

    @RequestMapping(value = "login", method = RequestMethod.GET)
    public String login() {
        return getTpl("sys/login");
    }

    /**
     * 管理员控制台右侧部分首页
     */
    @RequestMapping(value = "main", method = RequestMethod.GET)
    public String main(Model model) {
        //用户注册情况
        Calendar calendar = Calendar.getInstance();
        //最近一个月的新增加数量
        calendar.add(Calendar.MONTH, -1);
        model.addAttribute("userIncrease", userService.countByDate(calendar.getTime()));
        //所有用户数量
        calendar.set(Calendar.YEAR, 1970);
        model.addAttribute("userTotal", userService.countByDate(calendar.getTime()));
        //内存使用情况
        OperatingSystemMXBean osmb = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        model.addAttribute("memoryTotal", osmb.getTotalPhysicalMemorySize());
        model.addAttribute("memoryUsed", osmb.getTotalPhysicalMemorySize() - osmb.getFreePhysicalMemorySize());
        //磁盘使用情况
        File[] roots = File.listRoots();
        long totalSpace = 0, freeSpace = 0;
        for (File file : roots) {
            totalSpace += file.getTotalSpace();
            freeSpace += file.getFreeSpace();
        }
        model.addAttribute("diskTotal", totalSpace);
        model.addAttribute("diskUsed", totalSpace - freeSpace);
        //JAVA环境信息
        model.addAttribute("javaVersion", System.getProperty("java.version"));
        model.addAttribute("javaHome", System.getProperty("java.home"));
        model.addAttribute("osName", System.getProperty("os.name"));
        model.addAttribute("osArch", System.getProperty("os.arch"));
        model.addAttribute("serverInfo", servletContext.getServerInfo());
        model.addAttribute("servletVersion", servletContext.getMajorVersion() + "." + servletContext.getMinorVersion());
        return getTpl("sys/main");
    }

    /**
     * 统计当前日期所在周的没人不同类型的人员注册情况
     */
    @ResponseBody
    @RequiresRoles("admin")
    @RequestMapping(value = "statistics", method = RequestMethod.GET)
    public Object statistics() {
        //按照人员的角色类型统计
        List<Role> roles = roleService.getAll();
        DateTime dateTime = new DateTime();
        Date start = dateTime.dayOfWeek().withMinimumValue().secondOfDay().withMinimumValue().toDate();
        Date end = dateTime.dayOfWeek().withMaximumValue().secondOfDay().withMaximumValue().toDate();
        List<User> users = userService.findByDate(start, end);
        String legend[] = new String[]{};
        Map<String, Object> series[] = new Map[]{};
        Map<String, int[]> tempMap = Maps.newConcurrentMap();
        for (Role role : roles) {
            legend = ArrayUtils.add(legend, role.getName());
            //每周有七天初始化每天为0
            tempMap.put(role.getName(), new int[]{0, 0, 0, 0, 0, 0, 0});
        }
        //遍历这周注册的人按照角色统计放入临时变量里
        for (User user : users) {
            Iterator<UserRole> iterator = user.getUserRoles().iterator();
            while (iterator.hasNext()) {
                UserRole userRole = iterator.next();
                int index = new DateTime(user.getCreateDate()).dayOfWeek().get();
                int array[] = tempMap.get(userRole.getRole().getName());
                array[index] += 1;
            }
        }
        Iterator<Map.Entry<String, int[]>> iterator = tempMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, int[]> entry = iterator.next();
            Map<String, Object> serie = Maps.newConcurrentMap();
            serie.put("name", entry.getKey());
            serie.put("type", "line");
            serie.put("data", entry.getValue());
            series = ArrayUtils.add(series, serie);
        }
        //封装数据
        Map<String, Object> result = Maps.newConcurrentMap();
        result.put("legend", legend);
        result.put("series", series);
        return ResultObject.success(result).setMsg("统计成功!");
    }
}
