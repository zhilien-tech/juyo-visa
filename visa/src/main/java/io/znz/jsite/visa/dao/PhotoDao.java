package io.znz.jsite.visa.dao;

import io.znz.jsite.base.HibernateDao;
import io.znz.jsite.visa.bean.Photo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Chaly on 2017/3/7.
 */
@Repository
public interface PhotoDao extends HibernateDao<Photo, Integer> {
    public Photo findById_Option_KeyAndId_Customer_Id(String key, long cid);

    public List<Photo> findById_Customer_Id(long cid);
}
