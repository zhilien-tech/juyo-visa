package io.znz.jsite.core.web;

import io.znz.jsite.base.BaseController;
import io.znz.jsite.core.bean.Area;
import io.znz.jsite.core.service.AreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;

/**
 * 区域信息类型controller
 * @author Chaly
 */
@Controller
@RequestMapping("sys/area")
public class AreaController extends BaseController {

    @Autowired
    private AreaService areaService;

    /**
     * 默认页面
     */
    @RequestMapping(method = RequestMethod.GET)
    public String list() {
        return getTpl("sys/area/areaList");
    }

    /**
     * 获取区域信息类型json
     */
    @RequestMapping(value = "json", method = RequestMethod.GET)
    @ResponseBody
    public Object areaList(Integer id) {
        return areaService.findByParent(id);
    }

    /**
     * 添加区域信息
     */
    @RequestMapping(value = "create", method = RequestMethod.POST)
    @ResponseBody
    public Object create(@Valid Area area) {
        return areaService.save(area);
    }

    /**
     * 修改区域信息
     */
    @RequestMapping(value = "update", method = RequestMethod.POST)
    @ResponseBody
    public Object update(@Valid Area area) {
        return areaService.update(area);
    }

    /**
     * 删除区域信息
     */
    @RequestMapping(value = "delete/{id}")
    @ResponseBody
    public String delete(@PathVariable("id") Integer id) {
        areaService.delete(id);
        return "success";
    }

    /**
     * 搜索区域信息类型json
     */
    @RequestMapping(value = "json/get", method = RequestMethod.GET)
    @ResponseBody
    public Object areaInfo(Integer id) {
        return areaService.get(id);
    }
}
