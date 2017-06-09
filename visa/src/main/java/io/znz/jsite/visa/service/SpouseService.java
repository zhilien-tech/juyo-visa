package io.znz.jsite.visa.service;

import io.znz.jsite.base.BaseService;
import io.znz.jsite.base.HibernateDao;
import io.znz.jsite.visa.bean.Spouse;
import io.znz.jsite.visa.dao.SpouseDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Chaly on 2017/3/7.
 */
@Service
@Transactional(readOnly = true)
public class SpouseService extends BaseService<Spouse, Integer> {

    @Autowired
    private SpouseDao spouseDao;

    @Override
    public HibernateDao<Spouse, Integer> getEntityDao() {
        return spouseDao;
    }

}
