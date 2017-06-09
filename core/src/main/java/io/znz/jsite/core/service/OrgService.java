package io.znz.jsite.core.service;

import io.znz.jsite.base.BaseService;
import io.znz.jsite.base.HibernateDao;
import io.znz.jsite.core.bean.Org;
import io.znz.jsite.core.dao.OrgDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 区域service
 */
@Service
@Transactional(readOnly = true)
public class OrgService extends BaseService<Org, Integer> {

    @Autowired
    private OrgDao orgDao;

    @Override
    public HibernateDao<Org, Integer> getEntityDao() {
        return orgDao;
    }

    public List<Org> findByParent(Integer pid) {
        if (pid == null) {
            return orgDao.findByParent_IdIsNull();
        } else {
            return orgDao.findByParent_Id(pid);
        }
    }

    public List<Org> getAllExclude(Integer id) {
        return orgDao.findByIdNot(id);
    }
}
