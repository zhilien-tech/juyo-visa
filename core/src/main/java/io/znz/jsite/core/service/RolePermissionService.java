package io.znz.jsite.core.service;

import io.znz.jsite.base.BaseService;
import io.znz.jsite.base.HibernateDao;
import io.znz.jsite.core.bean.Permission;
import io.znz.jsite.core.bean.Role;
import io.znz.jsite.core.bean.RolePermission;
import io.znz.jsite.core.dao.RolePermissionDao;
import io.znz.jsite.ext.shiro.UserRealm;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.RealmSecurityManager;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 角色权限service
 * @author Chaly
 */
@Service
@Transactional(readOnly = true)
public class RolePermissionService extends BaseService<RolePermission, Integer> {

    @Autowired
    private RolePermissionDao rolePermissionDao;

    @Override
    public HibernateDao<RolePermission, Integer> getEntityDao() {
        return rolePermissionDao;
    }

    /**
     * 获取角色权限id集合
     * @return List
     */
    public List<Integer> getPermissionIds(Integer roleId) {
        return rolePermissionDao.findPermissionIds(roleId);
    }

    /**
     * 修改角色权限
     */
    @Transactional(readOnly = false)
    public void updateRolePermission(Integer roleId, List<Integer> newList) {
        rolePermissionDao.deleteByRoleId(roleId);
        if (newList != null) {
            for (Integer pid : newList) {
                rolePermissionDao.save(getRolePermission(roleId, pid));
            }
        }
    }

    /**
     * 清空该角色用户的权限缓存
     */
    public void clearUserPermCache(PrincipalCollection pc) {
        RealmSecurityManager securityManager = (RealmSecurityManager) SecurityUtils.getSecurityManager();
        UserRealm userRealm = (UserRealm) securityManager.getRealms().iterator().next();
        userRealm.clearCachedAuthorizationInfo(pc);
    }

    /**
     * 构造角色权限对象
     * @return RolePermission
     */
    private RolePermission getRolePermission(Integer roleId, Integer permissionId) {
        RolePermission rp = new RolePermission();
        rp.setRole(new Role(roleId));
        rp.setPermission(new Permission(permissionId));
        return rp;
    }

}
