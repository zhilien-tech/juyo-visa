package io.znz.jsite.core.web;

import io.znz.jsite.base.BaseController;
import io.znz.jsite.base.PropertyFilter;
import io.znz.jsite.core.bean.Dict;
import io.znz.jsite.core.service.DictService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * 字典controller
 * @author Chaly
 */
@Controller
@RequestMapping("sys/dict")
public class DictController extends BaseController {

    @Autowired
    private DictService dictService;

    /**
     * 默认页面
     */
    @RequestMapping(method = RequestMethod.GET)
    public String list() {
        return getTpl("sys/dict/dictList");
    }

    /**
     * 获取字典json
     */
    @RequiresPermissions("sys:dict:view")
    @RequestMapping(value = "json", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> dictList(HttpServletRequest request) {
        Pageable pageable = getPageable(request);
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request);
        Page<Dict> page = dictService.search(pageable, filters);
        return getEasyUIData(page);
    }

    /**
     * 获取字典json
     */
    @RequiresPermissions("sys:dict:view")
    @RequestMapping(value = "key/json", method = RequestMethod.GET)
    @ResponseBody
    public Object keyList() {
        return dictService.findAllKey();
    }

    /**
     * 添加字典
     */
    @RequiresPermissions("sys:dict:add")
    @RequestMapping(value = "create", method = RequestMethod.POST)
    @ResponseBody
    public Object create(@Valid Dict dict) {
        return dictService.save(dict);
    }

    /**
     * 修改字典
     */
    @RequiresPermissions("sys:dict:update")
    @RequestMapping(value = "update", method = RequestMethod.POST)
    @ResponseBody
    public Object update(@Valid Dict dict) {
        return dictService.update(dict);
    }

    /**
     * 删除字典
     */
    @RequiresPermissions("sys:dict:delete")
    @RequestMapping(value = "delete/{id}", method = RequestMethod.POST)
    @ResponseBody
    public String delete(@PathVariable("id") Integer id) {
        dictService.delete(id);
        return "success";
    }

    /**
     * 获取字典json
     */
    @RequiresPermissions("sys:dict:view")
    @RequestMapping(value = "keys/json", method = RequestMethod.GET)
    @ResponseBody
    public Object findByKeysList(@RequestParam(value = "keys") List<String> keys) {
        return dictService.findByKeys(keys);
    }
}
