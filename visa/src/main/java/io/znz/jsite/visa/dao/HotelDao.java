package io.znz.jsite.visa.dao;

import io.znz.jsite.base.HibernateDao;
import io.znz.jsite.visa.bean.Hotel;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Chaly on 2017/4/11.
 */
@Repository
public interface HotelDao extends HibernateDao<Hotel, Integer> {
    @Query("SELECT h FROM Hotel h WHERE h.name LIKE %?1% OR h.city LIKE %?1% OR h.address LIKE %?1%")
    List<Hotel> findByFilter(String filter);
}