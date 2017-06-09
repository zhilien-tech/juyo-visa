package io.znz.jsite.visa.service;

import io.znz.jsite.base.BaseService;
import io.znz.jsite.base.HibernateDao;
import io.znz.jsite.visa.bean.Photo;
import io.znz.jsite.visa.bean.School;
import io.znz.jsite.visa.dao.PhotoDao;
import io.znz.jsite.visa.dao.SchoolDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Chaly on 2017/3/7.
 */
@Service
@Transactional(readOnly = true)
public class SchoolService extends BaseService<School, Integer> {

    @Autowired
    private SchoolDao schoolDao;

    @Override
    public HibernateDao<School, Integer> getEntityDao() {
        return schoolDao;
    }

}
