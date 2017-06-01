package io.znz.jsite.visa.service;

import io.znz.jsite.base.BaseService;
import io.znz.jsite.base.HibernateDao;
import io.znz.jsite.visa.bean.Travel;
import io.znz.jsite.visa.dao.TravelDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Chaly on 2017/3/7.
 */
@Service
@Transactional(readOnly = true)
public class TravelService extends BaseService<Travel, Integer> {

    @Autowired
    private TravelDao travelDao;

    @Override
    public HibernateDao<Travel, Integer> getEntityDao() {
        return travelDao;
    }

}
