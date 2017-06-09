package io.znz.jsite.visa.service;

import io.znz.jsite.base.BaseService;
import io.znz.jsite.base.HibernateDao;
import io.znz.jsite.visa.bean.Army;
import io.znz.jsite.visa.dao.ArmyDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Chaly on 2017/3/7.
 */
@Service
@Transactional(readOnly = true)
public class ArmyService extends BaseService<Army, Integer> {

    @Autowired
    private ArmyDao armyDao;

    @Override
    public HibernateDao<Army, Integer> getEntityDao() {
        return armyDao;
    }

}
