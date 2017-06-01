package io.znz.jsite.core.dao;

import io.znz.jsite.base.HibernateDao;
import io.znz.jsite.core.bean.UserRole;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * 用户角色DAO
 * @author Chaly
 */
@Repository
public interface UserRoleDao extends HibernateDao<UserRole, Integer> {
    /**
     * 删除用户角色
     */
    @Transactional
    @Modifying
    @Query("DELETE FROM UserRole ur WHERE ur.user.id=?1 AND ur.role.id=?2")
    public void deleteUR(String userId, Integer roleId);

    /**
     * 查询用户拥有的角色id集合
     * @return 结果集合
     */
    @Query("SELECT ur.role.id FROM UserRole ur WHERE ur.user.id=?1")
    public List<Integer> findRoleIds(String userId);

}
