package io.znz.jsite.core.web;

import io.znz.jsite.base.BaseController;
import io.znz.jsite.core.bean.Ad;
import io.znz.jsite.core.service.AdService;
import io.znz.jsite.core.service.DictService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Map;

/**
 * 广告信息类型controller
 * @author Chaly
 */
@Controller
@RequestMapping("${adminPath}/config/ad")
public class AdController extends BaseController {

    @Autowired
    private AdService adService;
    @Autowired
    private DictService dictService;

    /**
     * 默认页面
     */
    @RequiresPermissions("conf:ad:view")
    @RequestMapping(method = RequestMethod.GET)
    public String list() {
        return "config/adList";
    }

    /**
     * 获取所广告json
     */
    @RequiresPermissions("conf:ad:view")
    @RequestMapping("json")
    @ResponseBody
    public Map<String, Object> list(HttpServletRequest request) {
        Pageable pageable = getPageable(request);
        pageable = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(),
            Sort.Direction.DESC, "createDate");
        Page<Ad> page = adService.getAll(pageable);
        return getEasyUIData(page);
    }

    /**
     * 添加广告信息跳转
     */
    @RequestMapping(value = "create", method = RequestMethod.GET)
    public String createForm(Model model) {
        model.addAttribute("adInfo", new Ad());
        model.addAttribute("positions", dictService.findByKey("ad"));
        model.addAttribute("action", "create");
        return "config/adForm";
    }

    /**
     * 添加广告信息
     */
    @RequestMapping(value = "create", method = RequestMethod.POST)
    @ResponseBody
    public String create(@Valid Ad ad) {
        adService.save(ad);
        return "success";
    }

    /**
     * 修改广告信息跳转
     */
    @RequestMapping(value = "update/{id}", method = RequestMethod.GET)
    public String updateForm(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("adInfo", adService.get(id));
        model.addAttribute("positions", dictService.findByKey("ad"));
        model.addAttribute("action", "update");
        return "config/adForm";
    }

    /**
     * 保存广告信息
     */
    @RequestMapping(value = "save", method = RequestMethod.POST)
    @ResponseBody
    public String save(@Valid @ModelAttribute @RequestBody Ad ad) {
        adService.save(ad);
        return "success";
    }

    /**
     * 删除广告信息
     */
    @RequestMapping(value = "delete/{id}")
    @ResponseBody
    public Object delete(@PathVariable("id") Integer id) {
        adService.delete(id);
        return "success";
    }
}
