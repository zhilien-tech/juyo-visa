package io.znz.jsite.visa.dao;

import io.znz.jsite.base.HibernateDao;
import io.znz.jsite.visa.bean.Flight;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Chaly on 2017/4/11.
 */
@Repository
public interface FlightDao extends HibernateDao<Flight, Integer> {
    @Query("SELECT f FROM Flight f WHERE f.from LIKE %?1% OR f.to LIKE %?1% OR f.line LIKE %?1% OR f.fromCity LIKE %?1% OR f.toCity LIKE %?1%")
    List<Flight> findByFilter(String filter);
}
