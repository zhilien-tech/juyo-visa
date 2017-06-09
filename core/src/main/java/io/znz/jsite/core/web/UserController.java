package io.znz.jsite.core.web;

import io.znz.jsite.base.BaseController;
import io.znz.jsite.base.PropertyFilter;
import io.znz.jsite.core.bean.User;
import io.znz.jsite.core.bean.UserRole;
import io.znz.jsite.core.service.RoleService;
import io.znz.jsite.core.service.UserOrgService;
import io.znz.jsite.core.service.UserRoleService;
import io.znz.jsite.core.service.UserService;
import io.znz.jsite.core.util.UserUtil;
import io.znz.jsite.util.BeanUtils;
import io.znz.jsite.util.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户controller
 *
 * @author Chaly
 */
@Controller
@RequestMapping("sys/user")
public class UserController extends BaseController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserOrgService userOrgService;
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private RoleService roleService;

    /**
     * 获取用户json
     */
    @RequiresPermissions("sys:user:view")
    @RequestMapping(value = "json", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getData(HttpServletRequest request) {
        Pageable pageable = getPageable(request);
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request);
        Page<User> page = userService.search(pageable, filters);
        return getEasyUIData(page);
    }

    /**
     * 添加用户
     */
    @RequiresPermissions("sys:user:add")
    @RequestMapping(value = "create", method = RequestMethod.POST)
    @ResponseBody
    public Object create(User user) {
        return userService.save(user);
    }

    /**
     * 修改用户
     */
    @RequiresPermissions("sys:user:update")
    @RequestMapping(value = "update", method = RequestMethod.POST)
    @ResponseBody
    public Object update(User user) {
        User target = userService.get(user.getId());
        BeanUtils.copyPropertiesIgnoreNull(user, target);
        return userService.update(target);
    }

    /**
     * 删除用户
     */
    @RequiresPermissions("sys:user:delete")
    @RequestMapping(value = "delete/{id}")
    @ResponseBody
    public Object delete(@PathVariable("id") String id) {
        User user = userService.get(id);
        userService.delete(id);
        return user;
    }

    /**
     * 修改密码
     */
    @RequestMapping(value = "updatePwd", method = RequestMethod.POST)
    @ResponseBody
    public String updatePwd(String password, String id) {
        User user = userService.get(id);
        user.setPassword(password);
        userService.updatePwd(user);
        return "success";
    }

    /**
     * 修改用户拥有的角色
     */
    @RequiresPermissions("sys:user:roleUpd")
    @RequestMapping(value = "{uid}/updateRole")
    @ResponseBody
    public String updateUserRole(@PathVariable("uid") String id,
                                 @RequestParam(value = "ids", required = false) List<Integer> ids
    ) {
        userRoleService.updateUserRole(id, userRoleService.getRoleIdList(id), ids);
        return "success";
    }

    /**
     * 获取用户拥有的角色ID集合
     */
    @RequiresPermissions("sys:user:roleView")
    @RequestMapping(value = "{id}/role")
    @ResponseBody
    public List<Integer> getRoleIdList(@PathVariable("id") String id) {
        return userRoleService.getRoleIdList(id);
    }

    /**
     * 获取用户拥有的机构ID集合
     */
    @RequiresPermissions("sys:user:orgView")
    @RequestMapping(value = "{id}/org")
    @ResponseBody
    public List<Integer> getOrgIdList(@PathVariable("id") String id) {
        return userOrgService.getOrgIdList(id);
    }

    /**
     * 修改用户所在的部门
     */
    @RequiresPermissions("sys:user:orgUpd")
    @RequestMapping(value = "{uid}/updateOrg")
    @ResponseBody
    public String updateUserOrg(@PathVariable("uid") String uid,
                                @RequestParam(value = "ids", required = false) List<Integer> ids
    ) {
        userOrgService.updateUserOrg(uid, ids);
        return "success";
    }

    /**
     * 默认页面
     */
    @RequestMapping(method = RequestMethod.GET)
    public String list() {
        return getTpl("sys/user/userList");
    }


    /////////////////////////////////////////
    //待确认接口
    /////////////////////////////////////////

    /**
     * 添加用户跳转
     */
    @RequiresPermissions("sys:user:add")
    @RequestMapping(value = "create", method = RequestMethod.GET)
    public String createForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("action", "create");
        return "sys/userForm";
    }

    /**
     * 修改用户跳转
     */
    @RequiresPermissions("sys:user:update")
    @RequestMapping(value = "update/{id}", method = RequestMethod.GET)
    public String updateForm(@PathVariable("id") String id, Model model) {
        model.addAttribute("user", userService.get(id));
        model.addAttribute("action", "update");
        return "sys/userForm";
    }

    /**
     * 弹窗页-用户拥有的角色
     */
    @RequiresPermissions("sys:user:roleView")
    @RequestMapping(value = "{userId}/userRole")
    public String getUserRole(@PathVariable("userId") String id, Model model) {
        model.addAttribute("userId", id);
        return "sys/userRoleList";
    }

    /**
     * 弹窗页-用户所在机构
     */
    @RequiresPermissions("sys:user:orgView")
    @RequestMapping(value = "{userId}/userOrg")
    public String getUserOrg(@PathVariable("userId") String id, Model model) {
        model.addAttribute("userId", id);
        return "sys/userOrgList";
    }

    /**
     * 修改密码跳转
     */
    @RequestMapping(value = "updatePwd/{id}", method = RequestMethod.GET)
    public String updatePwdForm(Model model, @PathVariable("id") String id) {
        model.addAttribute("user", userService.get(id));
        return "sys/userPassword";
    }

    /**
     * Ajax请求校验loginName是否唯一。
     */
    @RequestMapping(value = "checkLoginName")
    @ResponseBody
    public Boolean checkLoginName(String loginName) {
        if (userService.getUser(loginName) == null) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * ajax请求校验原密码是否正确
     */
    @RequiresPermissions("sys:user:update")
    @RequestMapping(value = "checkPwd")
    @ResponseBody
    public Boolean checkPwd(String oldPassword, HttpSession session) {
        if (userService.checkPassword((User) session.getAttribute("user"), oldPassword)) {
            return true;
        } else {
            return false;
        }
    }

    @RequestMapping(value = "changepwd")
    @ResponseBody
    public Object changepwd(String oldpwd, String newpwd) {
        User user = UserUtil.getUser();
        if (user == null || user.getId() == null) {
            return "登录过期请重新登录!";
        }
        if (!userService.checkPassword(user, oldpwd)) {
            return "原密码不正确!";
        }
        user.setPassword(newpwd);
        userService.updatePwd(user);
        return true;
    }

    @RequestMapping(value = "exist")
    @ResponseBody
    public Object exist(String loginName) {
        return userService.existLoginName(loginName);
    }

    @RequestMapping(value = "curr")
    @ResponseBody
    public Object curr() {
        Map<String, String> map = new HashMap<String, String>();
        User user = UserUtil.getUser();
        map.put("name", user.getLoginName());
        StringBuffer sb = new StringBuffer();
        for (UserRole userRole : user.getUserRoles()) {
            sb.append(userRole.getRole().getName()).append("/");
        }
        if (sb.length() == 0) {
            sb.append("普通用户");
        }
        map.put("role", StringUtils.removeEnd(sb.toString(),"/"));
        return UserUtil.getUser();
    }
}
