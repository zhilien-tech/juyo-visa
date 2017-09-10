package io.znz.jsite.visa.dao;

import io.znz.jsite.base.HibernateDao;
import io.znz.jsite.visa.bean.Scenic;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Created by Chaly on 2017/4/12.
 */
@Repository
public interface ScenicDao extends HibernateDao<Scenic, Integer> {
	@Query("SELECT s FROM Scenic s WHERE s.name LIKE %?1% OR s.city LIKE %?1%")
	List<Scenic> findByFilter(String filter);

	@Query("SELECT s FROM Scenic s WHERE s.city LIKE %?1%")
	List<Scenic> findByCity(String filter);
}
