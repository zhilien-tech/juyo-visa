package io.znz.jsite.visa.dao;

import io.znz.jsite.base.HibernateDao;
import io.znz.jsite.visa.bean.Flight;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Created by Chaly on 2017/4/11.
 */
@Repository
public interface FlightDao extends HibernateDao<Flight, Integer> {
	@Query("SELECT f FROM Flight f WHERE f.from LIKE %?1% OR f.to LIKE %?1% OR f.line LIKE %?1% OR f.fromCity LIKE %?1% OR f.toCity LIKE %?1%")
	List<Flight> findByFilter(String filter);

	@Query("SELECT f FROM Flight f WHERE f.fromCity LIKE %?1% AND f.toCity LIKE %?2%")
	List<Flight> filghtByCity(String fromCity, String toCity);

	@Query("SELECT f FROM Flight f WHERE f.fromCity LIKE %?1% AND f.toCity LIKE %?2% AND ((LOWER(f.from) LIKE %?3% OR UPPER(f.from) LIKE %?3%) OR (LOWER(f.to) LIKE %?3% OR UPPER(f.to) LIKE %?3%) OR (LOWER(f.line) LIKE %?3% OR UPPER(f.line) LIKE %?3%))")
	List<Flight> filghtByCityFilter(String fromCity, String toCity, String filterVal);

}
