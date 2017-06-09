package io.znz.jsite.core.dao;

import io.znz.jsite.base.HibernateDao;
import io.znz.jsite.core.bean.Ad;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * 广告DAO
 */
@Repository
public interface AdDao extends HibernateDao<Ad, Integer> {

    @Query("SELECT a FROM Ad a WHERE a.position.id = ?1 AND a.enabled = true AND a.startDate < NOW() AND a.endDate > NOW () ORDER BY a.weight DESC")
    public List<Ad> findByPositionAndAvailable(Integer did);

    @Query("SELECT a FROM Ad a WHERE a.position.key = ?1 AND a.position.category = ?2 AND a.enabled = true AND a.startDate < NOW () AND a.endDate > NOW () ORDER BY a.weight DESC")
    public List<Ad> findByKeyAndValueAndAvailable(String key, String category);

}
