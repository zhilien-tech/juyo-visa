package io.znz.jsite.visa.dao;

import io.znz.jsite.base.HibernateDao;
import io.znz.jsite.visa.bean.Help;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Chaly on 2017/3/29.
 */
@Repository
public interface HelpDao extends HibernateDao<Help, Integer> {

    public Help findById_KeyAndId_Cid(String key, long cid);

    @Transactional
    @Modifying
    @Query("DELETE FROM Help h WHERE h.id.cid=?1")
    public void deleteByCustomer(long cid);

    public List<Help> findById_Cid(long cid);
}
