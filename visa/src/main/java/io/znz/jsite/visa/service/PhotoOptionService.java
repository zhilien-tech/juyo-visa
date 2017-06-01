package io.znz.jsite.visa.service;

import io.znz.jsite.base.BaseService;
import io.znz.jsite.base.HibernateDao;
import io.znz.jsite.visa.bean.PhotoOption;
import io.znz.jsite.visa.dao.PhotoOptionDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Chaly on 2017/3/13.
 */
@Service
@Transactional(readOnly = true)
public class PhotoOptionService extends BaseService<PhotoOption, Integer> {

    @Autowired
    private PhotoOptionDao photoOptionDao;

    @Override
    public HibernateDao<PhotoOption, Integer> getEntityDao() {
        return photoOptionDao;
    }
}