package io.znz.jsite.core.web;

import io.znz.jsite.base.BaseController;
import io.znz.jsite.core.bean.Link;
import io.znz.jsite.core.service.LinkService;
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
@RequestMapping("config/link")
public class LinkController extends BaseController {

    @Autowired
    private LinkService linkService;

    /**
     * 默认页面
     */
    @RequiresPermissions("conf:link:view")
    @RequestMapping(method = RequestMethod.GET)
    public String site(Model model) {
        return "config/linkList";
    }

    @RequiresPermissions("conf:link:view")
    @RequestMapping(value = "json", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> linkList() {
        List<Link> sites = linkService.getAll();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("rows", sites);
        map.put("total", sites.size());
        return map;
    }

    /**
     * 添加友情链接跳转
     */
    @RequestMapping(value = "create", method = RequestMethod.GET)
    public String createForm(Model model) {
        model.addAttribute("linkInfo", new Link());
        model.addAttribute("action", "create");
        return "config/linkForm";
    }

    /**
     * 添加友情链接
     */
    @RequestMapping(value = "create", method = RequestMethod.POST)
    @ResponseBody
    public String create(@Valid Link linkInfo, Model model) {
        linkService.save(linkInfo);
        return "success";
    }

    /**
     * 修友情链接跳转
     */
    @RequestMapping(value = "update/{id}", method = RequestMethod.GET)
    public String updateForm(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("linkInfo", linkService.get(id));
        model.addAttribute("action", "update");
        return "config/linkForm";
    }

    /**
     * 修改友情链接
     */
    @RequestMapping(value = "update", method = RequestMethod.POST)
    @ResponseBody
    public String update(@Valid @ModelAttribute @RequestBody Link linkInfo) {
        linkService.update(linkInfo);
        return "success";
    }

    /**
     * 删除友情链接
     */
    @RequestMapping(value = "delete/{id}")
    @ResponseBody
    public String delete(@PathVariable("id") Integer id) {
        linkService.delete(id);
        return "success";
    }

    /**
     * 查看友情链接,直接新页面跳转到连接网站
     */
    @RequestMapping(value = "view/{id}")
    public String view(@PathVariable("id") Integer id) {
        Link link = linkService.updateViews(id);
        return "redirect:" + link.getUrl();
    }

}
