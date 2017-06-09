package io.znz.jsite.core.service;

import io.znz.jsite.base.BaseService;
import io.znz.jsite.base.HibernateDao;
import io.znz.jsite.core.bean.Ad;
import io.znz.jsite.core.dao.AdDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 广告service
 */
@Service
@Transactional(readOnly = true)
public class AdService extends BaseService<Ad, Integer> {

    @Autowired
    private AdDao adDao;

    @Override
    public HibernateDao<Ad, Integer> getEntityDao() {
        return adDao;
    }

    public List<Ad> findByPositionAndAvailable(Integer did) {
        return adDao.findByPositionAndAvailable(did);
    }

    public List<Ad> findAvailable(String key, String category) {
        return adDao.findByKeyAndValueAndAvailable(key, category);
    }

}
