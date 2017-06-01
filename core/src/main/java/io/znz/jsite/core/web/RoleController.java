package io.znz.jsite.core.web;

import io.znz.jsite.base.BaseController;
import io.znz.jsite.base.PropertyFilter;
import io.znz.jsite.core.bean.Role;
import io.znz.jsite.core.bean.User;
import io.znz.jsite.core.service.RolePermissionService;
import io.znz.jsite.core.service.RoleService;
import io.znz.jsite.core.util.Const;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

/**
 * 角色controller
 * @author Chaly
 */
@Controller
@RequestMapping("sys/role")
public class RoleController extends BaseController {

    @Autowired
    private RoleService roleService;

    @Autowired
    private RolePermissionService rolePermissionService;

    /**
     * 默认页面
     */
    @RequestMapping(method = RequestMethod.GET)
    public String list() {
        return getTpl("sys/role/roleList");
    }

    /**
     * 角色集合(JSON)
     */
    @RequiresPermissions("sys:role:view")
    @RequestMapping(value = "json", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getData(HttpServletRequest request) {
        Pageable pageable = getPageable(request);
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request);
        Page<Role> page = roleService.search(pageable, filters);
        return getEasyUIData(page);
    }

    /**
     * 获取角色拥有的权限ID集合
     */
    @RequiresPermissions("sys:role:permView")
    @RequestMapping("{id}/json")
    @ResponseBody
    public List<Integer> getRolePermissions(@PathVariable("id") Integer id) {
        List<Integer> permissionIdList = rolePermissionService.getPermissionIds(id);
        return permissionIdList;
    }

    /**
     * 修改角色权限
     */
    @RequiresPermissions("sys:role:permUpd")
    @RequestMapping(value = "{rid}/updatePermission")
    @ResponseBody
    public String updateRolePermission(@PathVariable("rid") Integer rid,
        @RequestParam(value = "ids", required = false) List<Integer> ids,
        HttpSession session
    ) {
        //获取application中的sessions
        HashSet sessions = (HashSet) session.getServletContext().getAttribute("sessions");
        if (null != sessions) {//当前如果有正在使用的用户，需要更新正在使用的用户的权限
            Iterator<Session> iterator = sessions.iterator();
            PrincipalCollection pc = null;
            //遍历sessions
            while (iterator.hasNext()) {
                HttpSession s = (HttpSession) iterator.next();
                User user = (User) s.getAttribute(Const.CURRENT_USER);
                if (user != null && user.getId().equals(id)) {
                    pc = (PrincipalCollection) s.getAttribute(String.valueOf(id));
                    //清空该用户权限缓存
                    rolePermissionService.clearUserPermCache(pc);
                    s.removeAttribute(String.valueOf(id));
                    break;
                }
            }
        }
        rolePermissionService.updateRolePermission(rid, ids);
        return "success";
    }

    /**
     * 添加角色
     */
    @RequiresPermissions("sys:role:add")
    @RequestMapping(value = "create", method = RequestMethod.POST)
    @ResponseBody
    public Object create(@Valid Role role) {
        return roleService.save(role);
    }

    /**
     * 修改角色
     */
    @RequiresPermissions("sys:role:update")
    @RequestMapping(value = "update", method = RequestMethod.POST)
    @ResponseBody
    public Object update(@Valid Role role) {
        return roleService.save(role);
    }

    /**
     * 删除角色
     */
    @RequiresPermissions("sys:role:delete")
    @RequestMapping(value = "delete/{id}")
    @ResponseBody
    public String delete(@PathVariable("id") Integer id) {
        roleService.delete(id);
        return "success";
    }
}
