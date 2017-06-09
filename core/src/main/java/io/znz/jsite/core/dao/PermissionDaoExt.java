package io.znz.jsite.core.dao;

import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * 权限DAO
 * @author Chaly
 */
@Repository
public interface PermissionDaoExt<T> {

    /**
     * 查询用户拥有的权限
     * @param uid 用户id
     */
    public List<T> findPermissions(String uid);

    /**
     * 查询用户拥有菜单
     * @param uid 用户id
     * @param pid 父级权限ID;
     */
    public List<T> findMenus(String uid, Integer pid);

    /**
     * 查询所有的菜单
     * @return 菜单集合
     */
    public List<T> findMenus();

    /**
     * 查询菜单下的操作权限
     */
    public List<T> findMenuOperation(Integer pid);

}
