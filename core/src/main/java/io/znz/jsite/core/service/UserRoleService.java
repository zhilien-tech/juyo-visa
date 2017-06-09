package io.znz.jsite.core.service;

import io.znz.jsite.base.BaseService;
import io.znz.jsite.base.HibernateDao;
import io.znz.jsite.core.bean.Role;
import io.znz.jsite.core.bean.User;
import io.znz.jsite.core.bean.UserRole;
import io.znz.jsite.core.dao.UserRoleDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 用户角色service
 * @author Chaly
 */
@Service
@Transactional(readOnly = true)
public class UserRoleService extends BaseService<UserRole, Integer> {

    @Autowired
    private UserRoleDao userRoleDao;

    @Override
    public HibernateDao<UserRole, Integer> getEntityDao() {
        return userRoleDao;
    }

    /**
     * 添加修改用户角色
     */
    @Transactional(readOnly = false)
    public void updateUserRole(String userId, List<Integer> oldList, List<Integer> newList) {
        // 是否删除
        for (int i = 0, j = oldList.size(); i < j; i++) {
            if (!newList.contains(oldList.get(i))) {
                userRoleDao.deleteUR(userId, oldList.get(i));
            }
        }

        // 是否添加
        for (int m = 0, n = newList.size(); m < n; m++) {
            if (!oldList.contains(newList.get(m))) {
                userRoleDao.save(getUserRole(userId, newList.get(m)));
            }
        }
    }

    /**
     * 构建UserRole
     * @return UserRole
     */
    private UserRole getUserRole(String userId, Integer roleId) {
        UserRole ur = new UserRole();
        ur.setUser(new User(userId));
        ur.setRole(new Role(roleId));
        return ur;
    }

    /**
     * 获取用户拥有角色id集合
     * @return 结果集合
     */
    public List<Integer> getRoleIdList(String userId) {
        return userRoleDao.findRoleIds(userId);
    }

}
