package io.znz.jsite.core.dao;

import io.znz.jsite.base.HibernateDao;
import io.znz.jsite.core.bean.Permission;
import org.springframework.stereotype.Repository;


/**
 * 权限DAO
 * @author Chaly
 */
@Repository
public interface PermissionDao extends HibernateDao<Permission, Integer>, PermissionDaoExt {

}
