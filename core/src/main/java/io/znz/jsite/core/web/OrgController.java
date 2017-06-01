package io.znz.jsite.core.web;

import io.znz.jsite.base.BaseController;
import io.znz.jsite.core.bean.Org;
import io.znz.jsite.core.service.OrgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 机构信息controller
 * @author Chaly
 */
@Controller
@RequestMapping("sys/org")
public class OrgController extends BaseController {

    @Autowired
    private OrgService orgService;

    /**
     * 默认页面
     */
    @RequestMapping(method = RequestMethod.GET)
    public String list() {
        return getTpl("sys/org/orgList");
    }

    /**
     * 获取机构信息json
     */
    @RequestMapping(value = "list/json", method = RequestMethod.GET)
    @ResponseBody
    public Object list(@RequestParam(value = "id", required = false) Integer exclude) {
        if (exclude != null && exclude > 0) {
            return orgService.getAllExclude(exclude);
        } else {
            return orgService.getAll();
        }
    }

    /**
     * 添加机构信息
     */
    @RequestMapping(value = "create", method = RequestMethod.POST)
    @ResponseBody
    public Object create(@Valid Org org) {
        if (org.getParent().getId() == null || org.getParent().getId() <= 0) {
            org.setParent(null);
        }
        return orgService.save(org);
    }

    /**
     * 修改机构信息
     */
    @RequestMapping(value = "update", method = RequestMethod.POST)
    @ResponseBody
    public Object update(@Valid Org org) {
        if (org.getParent().getId() == null || org.getParent().getId() <= 0) {
            org.setParent(null);
        }
        return orgService.update(org);
    }

    /**
     * 删除机构信息
     */
    @RequestMapping(value = "delete/{id}")
    @ResponseBody
    public String delete(@PathVariable("id") Integer id) {
        orgService.delete(id);
        return "success";
    }

}
