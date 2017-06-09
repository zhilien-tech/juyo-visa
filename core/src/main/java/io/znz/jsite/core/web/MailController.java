package io.znz.jsite.core.web;

import io.znz.jsite.base.BaseController;
import io.znz.jsite.core.bean.Mail;
import io.znz.jsite.core.bean.User;
import io.znz.jsite.core.service.MailService;
import io.znz.jsite.core.util.UserUtil;
import io.znz.jsite.util.ConvertUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 站点设置controller
 * @author Chaly
 */
@Controller
@RequestMapping("config/mail")
public class MailController extends BaseController {

    @Autowired
    private MailService mailService;

    /**
     * 默认页面
     */
    @RequiresPermissions("conf:mail:view")
    @RequestMapping(method = RequestMethod.GET)
    public String mail() {
        return "config/mailList";
    }

    @RequiresPermissions("conf:mail:view")
    @RequestMapping(value = "json", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> mailList() {
        List<Mail> mails = mailService.getAll();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("rows", mails);
        map.put("total", mails.size());
        return map;
    }

    /**
     * 修改邮箱服务器信息跳转
     */
    @RequiresPermissions("conf:mail:update")
    @RequestMapping(value = "edit", method = RequestMethod.GET)
    public String updateForm(Model model) {
        model.addAttribute("mailInfo", mailService.getSingleOne());
        return "config/mailForm";
    }

    @RequiresPermissions("conf:mail:update")
    @RequestMapping(value = "test", method = RequestMethod.POST)
    @ResponseBody
    public String test() {
        User user = UserUtil.getUser();
        if (user == null) {
            return "请先登录再测试!";
        }
        if (StringUtils.isEmpty(user.getEmail())) {
            return "当前登录人的邮箱未设置!";
        }
        String result = mailService.send(user.getEmail(),
            "这是一封管理员的测试邮件,证明你的邮箱服务器配置成功!", "测试邮件", MailService.Type.TEXT);
        if (StringUtils.isNotEmpty(result)) {
            return result;
        }
        return "success";
    }

    @RequiresPermissions("conf:mail:update")
    @RequestMapping(value = "save", method = RequestMethod.POST)
    @ResponseBody
    public String updateForm(@Valid @ModelAttribute @RequestBody Mail mailInfo) {
        if (mailInfo.getId() != null) {
            Mail existing = mailService.get(mailInfo.getId());
            ConvertUtil.copyNonNullProperties(mailInfo, existing);
            mailService.update(existing);
        } else {
            mailService.save(mailInfo);
        }
        return "success";
    }

}
