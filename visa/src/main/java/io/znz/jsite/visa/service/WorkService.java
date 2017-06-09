package io.znz.jsite.visa.service;

import io.znz.jsite.base.BaseService;
import io.znz.jsite.base.HibernateDao;
import io.znz.jsite.visa.bean.Work;
import io.znz.jsite.visa.dao.WorkDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Chaly on 2017/3/7.
 */
@Service
@Transactional(readOnly = true)
public class WorkService extends BaseService<Work, Integer> {

    @Autowired
    private WorkDao workDao;

    @Override
    public HibernateDao<Work, Integer> getEntityDao() {
        return workDao;
    }

}
