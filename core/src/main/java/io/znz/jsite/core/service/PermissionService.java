package io.znz.jsite.core.service;

import io.znz.jsite.base.BaseService;
import io.znz.jsite.base.HibernateDao;
import io.znz.jsite.core.bean.Permission;
import io.znz.jsite.core.dao.PermissionDao;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 权限service
 *
 * @author Chaly
 */
@Service
@Transactional(readOnly = true)
public class PermissionService extends BaseService<Permission, Integer> {

    @Autowired
    private PermissionDao permissionDao;

    @Override
    public HibernateDao<Permission, Integer> getEntityDao() {
        return permissionDao;
    }

    /**
     * 添加菜单基础操作
     *
     * @param pid 菜单id
     */
    @Transactional(readOnly = false)
    public void addBaseOpe(Integer pid) {
        List<Permission> pList = new ArrayList<Permission>();
        Permission parent = permissionDao.getOne(pid);
        StringBuilder keyPath = new StringBuilder();
        Permission topPermission = parent;//ObjectUtils.clone(parent);
        while (topPermission != null && StringUtils.isNotEmpty(topPermission.getPath())) {
            keyPath.insert(0, ":").insert(0, topPermission.getPath());
            topPermission = topPermission.getParent();
        }
        pList.add(new Permission(parent, "添加", "O", "", keyPath.toString() + "add"));
        pList.add(new Permission(parent, "删除", "O", "", keyPath.toString() + "delete"));
        pList.add(new Permission(parent, "修改", "O", "", keyPath.toString() + "update"));
        pList.add(new Permission(parent, "查看", "O", "", keyPath.toString() + "view"));
        //添加没有的基本操作
        List<Permission> existPList = getMenuOperation(pid);
        for (Permission permission : pList) {
            boolean exist = false;
            for (Permission existPermission : existPList) {
                if (permission.getCode().equals(existPermission.getCode())) {
                    exist = true;
                    break;
                } else {
                    exist = false;
                }
            }
            if (!exist)
                save(permission);
        }
    }

    /**
     * 获取角色拥有的权限集合
     *
     * @return 结果集合
     */
    public List<Permission> getPermissions(String uid) {
        return permissionDao.findPermissions(uid);
    }


    public List<Permission> getMenus(String userId, Integer pid) {
        List<Permission> list = permissionDao.findMenus(userId, pid);
        return list;
    }

    /**
     * 获取角色拥有的菜单
     *
     * @return 菜单集合
     */
    public List<Permission> getMenus() {
        return permissionDao.findMenus();
    }

    /**
     * 获取菜单下的操作
     *
     * @return 操作集合
     */
    public List<Permission> getMenuOperation(Integer pid) {
        return permissionDao.findMenuOperation(pid);
    }

}
