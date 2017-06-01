package io.znz.jsite.core.service;

import io.znz.jsite.base.BaseService;
import io.znz.jsite.base.HibernateDao;
import io.znz.jsite.core.bean.Role;
import io.znz.jsite.core.dao.RoleDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 角色service
 * @author Chaly
 */
@Service
@Transactional(readOnly = true)
public class RoleService extends BaseService<Role, Integer> {

    @Autowired
    private RoleDao roleDao;

    @Override
    public HibernateDao<Role, Integer> getEntityDao() {
        return roleDao;
    }

    public Role findByRoleCode(String code) {
        return roleDao.findByRoleCode(code);
    }
}
