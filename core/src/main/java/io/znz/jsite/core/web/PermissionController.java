package io.znz.jsite.core.web;

import io.znz.jsite.base.BaseController;
import io.znz.jsite.core.bean.Permission;
import io.znz.jsite.core.bean.User;
import io.znz.jsite.core.service.PermissionService;
import io.znz.jsite.core.util.UserUtil;
import io.znz.jsite.util.BeanUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 权限controller
 *
 * @author Chaly
 */
@Controller
@RequestMapping("sys/perms")
public class PermissionController extends BaseController {

    @Autowired
    private PermissionService permissionService;

    /**
     * 默认页面
     */
    @RequestMapping(method = RequestMethod.GET)
    public String list() {
        return getTpl("sys/func/funcList");
    }

    /**
     * 菜单页面
     */
    @RequestMapping(value = "menu", method = RequestMethod.GET)
    public String menuList() {
        return getTpl("sys/menu/menuList");
    }

    /**
     * 菜单集合(JSON)
     */
    @RequiresPermissions("sys:func:menu:view")
    @RequestMapping(value = "menu/json", method = RequestMethod.GET)
    @ResponseBody
    public List<Permission> menuDate() {
        List<Permission> permissionList = permissionService.getMenus();
        return permissionList;
    }

    /**
     * 某用户的权限集合
     */
    @RequiresPermissions("sys:func:view")
    @RequestMapping("{uid}/json")
    @ResponseBody
    public List<Permission> otherPermission(@PathVariable("uid") String uid) {
        return permissionService.getPermissions(uid);
    }

    /**
     * 权限集合(JSON)
     */
    @RequiresPermissions("sys:func:view")
    @RequestMapping(value = "json", method = RequestMethod.GET)
    @ResponseBody
    public List<Permission> getData() {
        List<Permission> permissionList = permissionService.getAll();
        return permissionList;
    }

    /**
     * 获取菜单下的操作
     */
    @RequiresPermissions("sys:func:view")
    @RequestMapping("ope/json")
    @ResponseBody
    public Object menuOperation(Integer pid) {
        List<Permission> menuOperList = permissionService.getMenuOperation(pid);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("rows", menuOperList);
        map.put("total", menuOperList.size());
        return map;
    }

    /**
     * 当前登录用户的权限集合
     */
    @RequestMapping("i/json")
    @ResponseBody
    public List<Permission> myPermission(Integer pid) {
        User user = UserUtil.getUser();
        if (user != null) {
            return permissionService.getMenus(user.getId(), pid);
        }
        return null;
    }

    /**
     * 添加权限/菜单
     */
    @RequiresPermissions("sys:func:add")
    @RequestMapping(value = "create", method = RequestMethod.POST)
    @ResponseBody
    public Object create(@Valid Permission permission) {
        return permissionService.save(permission);
    }

    /**
     * 添加菜单基础操作
     */
    @RequiresPermissions("sys:func:add")
    @RequestMapping(value = "createBase/{pid}", method = RequestMethod.POST)
    @ResponseBody
    public String create(@PathVariable("pid") Integer pid) {
        permissionService.addBaseOpe(pid);
        return "success";
    }

    /**
     * 修改权限/菜单
     */
    @RequiresPermissions("sys:func:update")
    @RequestMapping(value = "update", method = RequestMethod.POST)
    @ResponseBody
    public Object update(@Valid Permission permission) {
        if (permission.getParent().getId() == null || permission.getParent().getId() <= 0) {
            permission.setParent(null);
        }
        Permission target = permissionService.get(permission.getId());
        BeanUtils.copyPropertiesIgnoreNull(permission, target);
        return permissionService.update(target);
    }

    /**
     * 删除权限
     */
    @RequiresPermissions("sys:func:delete")
    @RequestMapping(value = "delete/{id}")
    @ResponseBody
    public String delete(@PathVariable("id") Integer id) {
        permissionService.delete(id);
        return "success";
    }
}
