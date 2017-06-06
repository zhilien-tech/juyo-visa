package io.znz.jsite.visa.service;

import io.znz.jsite.base.BaseService;
import io.znz.jsite.base.HibernateDao;
import io.znz.jsite.visa.bean.Travel;
import io.znz.jsite.visa.bean.Usa;
import io.znz.jsite.visa.dao.TravelDao;
import io.znz.jsite.visa.dao.UsaDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Chaly on 2017/3/7.
 */
@Service
@Transactional(readOnly = true)
public class UsaService extends BaseService<Usa, Integer> {

    @Autowired
    private UsaDao usaDao;

    @Override
    public HibernateDao<Usa, Integer> getEntityDao() {
        return usaDao;
    }

}
