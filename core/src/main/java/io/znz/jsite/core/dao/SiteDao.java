package io.znz.jsite.core.dao;

import io.znz.jsite.base.HibernateDao;
import io.znz.jsite.core.bean.Ad;
import io.znz.jsite.core.bean.Site;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * 广告DAO
 */
@Repository
public interface SiteDao extends HibernateDao<Site, Integer> {

    @Query("SELECT a FROM Ad a WHERE a.position.id = ?1 AND a.enabled = true AND a.startDate < NOW () AND a.endDate > NOW () ORDER BY a.weight DESC")
    public List<Ad> findByPositionAndAvailable(Integer did);

    @Query("SELECT s FROM Site s WHERE s.domain like ?1 OR s.alias like ?1")
    Site findByDomainOrAlias(String domain);
}
