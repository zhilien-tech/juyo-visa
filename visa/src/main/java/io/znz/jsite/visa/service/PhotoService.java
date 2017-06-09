package io.znz.jsite.visa.service;

import io.znz.jsite.base.BaseService;
import io.znz.jsite.base.HibernateDao;
import io.znz.jsite.visa.bean.Photo;
import io.znz.jsite.visa.bean.PhotoOption;
import io.znz.jsite.visa.bean.helper.Range;
import io.znz.jsite.visa.dao.PhotoDao;
import io.znz.jsite.visa.dao.PhotoOptionDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Chaly on 2017/3/7.
 */
@Service
@Transactional(readOnly = true)
public class PhotoService extends BaseService<Photo, Integer> {

    @Autowired
    private PhotoDao photoDao;
    @Autowired
    private PhotoOptionDao photoOptionDao;

    @Override
    public HibernateDao<Photo, Integer> getEntityDao() {
        return photoDao;
    }

    public Photo findByKeyAndCustomer(String key, long cid) {
        return photoDao.findById_Option_KeyAndId_Customer_Id(key, cid);
    }

    public List<PhotoOption> listOptionsByRange(Range range) {
        return photoOptionDao.listOptionsByRange(range);
    }

    @Transactional(readOnly = false)
    public List<Photo> saveAll(List<Photo> photos) {
        return photoDao.save(photos);
    }

    public List<Photo> findByCustomer(long cid) {
        return photoDao.findById_Customer_Id(cid);
    }
}
