package io.znz.jsite.visa.service;

import io.znz.jsite.base.BaseService;
import io.znz.jsite.base.HibernateDao;
import io.znz.jsite.visa.bean.Together;
import io.znz.jsite.visa.dao.TogetherDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Chaly on 2017/3/7.
 */
@Service
@Transactional(readOnly = true)
public class TogetherService extends BaseService<Together, Integer> {

    @Autowired
    private TogetherDao togetherDao;

    @Override
    public HibernateDao<Together, Integer> getEntityDao() {
        return togetherDao;
    }

}
