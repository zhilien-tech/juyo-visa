package io.znz.jsite.core.dao;

import io.znz.jsite.base.HibernateDao;
import io.znz.jsite.core.bean.RolePermission;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * 角色权限DAO
 * @author Chaly
 */
@Repository
public interface RolePermissionDao extends HibernateDao<RolePermission, Integer> {

    /**
     * 查询角色拥有的权限id
     * @return 结果集合
     */
    @Query("SELECT rp.permission.id from RolePermission rp WHERE rp.role.id=?1")
    List<Integer> findPermissionIds(Integer roleId);

    /**
     * 删除角色权限
     */
    @Transactional
    @Modifying
    @Query("DELETE RolePermission rp WHERE rp.role.id=?1")
    void deleteByRoleId(Integer roleId);
}
