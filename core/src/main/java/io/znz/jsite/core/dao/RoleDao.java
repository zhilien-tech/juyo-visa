package io.znz.jsite.core.dao;

import io.znz.jsite.base.HibernateDao;
import io.znz.jsite.core.bean.Role;
import org.springframework.stereotype.Repository;


/**
 * 角色DAO
 * @author Chaly
 */
@Repository
public interface RoleDao extends HibernateDao<Role, Integer> {

    public Role findByRoleCode(String code);

}
