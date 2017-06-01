package io.znz.jsite.visa.service;

import io.znz.jsite.base.BaseService;
import io.znz.jsite.base.HibernateDao;
import io.znz.jsite.visa.bean.Family;
import io.znz.jsite.visa.dao.FamilyDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Chaly on 2017/3/7.
 */
@Service
@Transactional(readOnly = true)
public class FamilyService extends BaseService<Family, Integer> {

    @Autowired
    private FamilyDao familyDao;

    @Override
    public HibernateDao<Family, Integer> getEntityDao() {
        return familyDao;
    }

    public List<Family> saveAll(List<Family> list) {
        return familyDao.save(list);
    }

}
