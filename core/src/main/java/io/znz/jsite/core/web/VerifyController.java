package io.znz.jsite.core.web;

import io.znz.jsite.base.BaseController;
import io.znz.jsite.core.bean.Dict;
import io.znz.jsite.core.bean.Site;
import io.znz.jsite.core.bean.User;
import io.znz.jsite.core.service.SiteService;
import io.znz.jsite.core.service.VerifyService;
import io.znz.jsite.core.util.UserUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 验证码controller
 * @author Chaly
 */
@Controller
@RequestMapping("sys/verify")
public class VerifyController extends BaseController {

    @Autowired
    private SiteService siteService;
    @Autowired
    private VerifyService verifyService;

    /**
     * 获取验证码
     */
    @RequestMapping(value = "get")
    @ResponseBody
    public String get(String targets) {
        return verifyService.getCode(targets,"全分享");
    }

    /**
     * 测试验证码
     */
    @RequestMapping(value = "test")
    @ResponseBody
    public String test() {
        User user = UserUtil.getUser();
        if (user == null) {
            return "请先登录再测试!";
        }
        Site site = siteService.getCurrSite();
        if (site == null) {
            return "请先配置站点再测试!";
        }
        Dict dict = site.getVerify();
        String targets = null;
        if ("MAIL".equalsIgnoreCase(dict.getCategory())) {
            if (StringUtils.isEmpty(user.getEmail())) {
                return "当前登录人的邮箱未设置!";
            }
            targets = user.getEmail();
        } else if ("SMS".equalsIgnoreCase(dict.getCategory())) {
            if (StringUtils.isEmpty(user.getPhone())) {
                return "当前登录人的电话未设置!";
            }
            targets = user.getPhone();
        }
        return verifyService.getCode(targets,"测试");
    }

    /**
     * 获取验证码
     */
    @RequestMapping(value = "check")
    @ResponseBody
    public Object check(String targets, String code) {
        return verifyService.checkCode(targets, code);
    }
}
