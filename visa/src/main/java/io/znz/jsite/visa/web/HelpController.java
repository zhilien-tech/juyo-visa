package io.znz.jsite.visa.web;

import io.znz.jsite.base.BaseController;
import io.znz.jsite.base.bean.ResultObject;
import io.znz.jsite.visa.bean.Help;
import io.znz.jsite.visa.service.HelpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Chaly on 2017/3/7.
 */
@Controller
@RequestMapping("visa/help")
public class HelpController extends BaseController {
    @Autowired
    private HelpService helpService;


    @RequestMapping(value = "get")
    @ResponseBody
    public Object get(String key, long cid) {
        Help help = helpService.findByKeyAndCid(key, cid);
        if (help == null) {
            return ResultObject.fail("暂无提示");
        }
        return ResultObject.success(helpService.save(help));
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    @ResponseBody
    public Object get(Help help) {
        return ResultObject.success(helpService.save(help));
    }

    @RequestMapping(value = "all")
    @ResponseBody
    public Object all(@RequestParam(defaultValue = "0") long cid) {
        return helpService.findByCid(cid);
    }
}