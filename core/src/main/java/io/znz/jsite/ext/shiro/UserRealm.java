package io.znz.jsite.ext.shiro;

import com.google.common.base.Objects;
import io.znz.jsite.core.bean.Permission;
import io.znz.jsite.core.bean.User;
import io.znz.jsite.core.bean.UserRole;
import io.znz.jsite.core.util.Const;
import io.znz.jsite.core.util.UserUtil;
import io.znz.jsite.core.service.PermissionService;
import io.znz.jsite.core.service.UserService;
import io.znz.jsite.util.security.Encodes;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.Serializable;

/**
 * 用户登录授权service(shrioRealm)
 * @author Chaly
 */
@Service
@DependsOn({"userDao", "permissionDao", "rolePermissionDao"})
public class UserRealm extends AuthorizingRealm {

    @Autowired
    private UserService userService;

    @Autowired
    private PermissionService permissionService;

    /**
     * 认证回调函数, 登录时调用
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
        UsernamePasswordCaptchaToken token = (UsernamePasswordCaptchaToken) authcToken;
        if (doCaptchaValidate(token)) {
            User user = userService.getUser(token.getUsername());
            if (user != null) {
                byte[] salt = Encodes.decodeHex(user.getSalt());
                ShiroUser shiroUser = new ShiroUser(user.getId(), user.getLoginName(), user.getName());
                //设置用户session
                user.getRoleName();
                UserUtil.getSession().setAttribute(Const.CURRENT_USER, user);
                //设置登录次数、时间
                userService.updateUserLogin(user);
                return new SimpleAuthenticationInfo(shiroUser, user.getPassword(), ByteSource.Util.bytes(salt), getName());
            } else {
                throw new UnknownAccountException();
            }
        }
        throw new CaptchaException("验证码错误！");
    }


    /**
     * 授权查询回调函数, 进行鉴权但缓存中无用户的授权信息时调用
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        ShiroUser shiroUser = (ShiroUser) principals.getPrimaryPrincipal();
        User user = userService.getUser(shiroUser.loginName);
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        if (user != null) {
            //赋予角色
            for (UserRole userRole : user.getUserRoles()) {
                info.addRole(userRole.getRole().getRoleCode());
            }
            //赋予权限
            for (Permission permission : permissionService.getPermissions(user.getId())) {
                if (StringUtils.isNotBlank(permission.getCode()))
                    info.addStringPermission(permission.getCode());
            }
        }
        return info;
    }

    /**
     * 验证码校验
     * @return boolean
     */
    protected boolean doCaptchaValidate(UsernamePasswordCaptchaToken token) {
        //如果用户提交的验证码为空则不验证
        if (token.getCaptcha() == null) return true;
        String captcha = (String) SecurityUtils.getSubject().getSession().getAttribute(com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY);
        return token.getCaptcha().equalsIgnoreCase(captcha);
    }

    /**
     * 设定Password校验的Hash算法与迭代次数.
     */
    @SuppressWarnings("static-access")
    @PostConstruct
    public void initCredentialsMatcher() {
        SmartCredentialsMatcher matcher = new SmartCredentialsMatcher(UserService.HASH_ALGORITHM);
        matcher.setHashIterations(UserService.HASH_INTERATIONS);
        setCredentialsMatcher(matcher);
    }

    /**
     * 自定义Authentication对象，使得Subject除了携带用户的登录名外还可以携带更多信息.
     */
    public static class ShiroUser implements Serializable {

        public String id;
        public String loginName;
        public String name;

        public ShiroUser(String id, String loginName, String name) {
            this.id = id;
            this.loginName = loginName;
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        /**
         * 本函数输出将作为默认的<shiro:principal/>输出.
         */
        @Override
        public String toString() {
            return loginName;
        }

        /**
         * 重载hashCode,只计算loginName;
         */
        @Override
        public int hashCode() {
            return Objects.hashCode(loginName);
        }

        /**
         * 重载equals,只计算loginName;
         */
        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            ShiroUser other = (ShiroUser) obj;
            if (loginName == null) {
                if (other.loginName != null) {
                    return false;
                }
            } else if (!loginName.equals(other.loginName)) {
                return false;
            }
            return true;
        }
    }

    @Override
    public void clearCachedAuthorizationInfo(PrincipalCollection principals) {
        super.clearCachedAuthorizationInfo(principals);
    }

    @Override
    public void clearCachedAuthenticationInfo(PrincipalCollection principals) {
        super.clearCachedAuthenticationInfo(principals);
    }

    @Override
    public void clearCache(PrincipalCollection principals) {
        super.clearCache(principals);
    }

}
