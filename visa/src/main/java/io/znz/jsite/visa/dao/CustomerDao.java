package io.znz.jsite.visa.dao;

import io.znz.jsite.base.HibernateDao;
import io.znz.jsite.visa.bean.Customer;
import io.znz.jsite.visa.bean.helper.State;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Created by Chaly on 2017/3/7.
 */
@Repository
public interface CustomerDao extends HibernateDao<Customer, Long>,CustomerDaoExt {

    @Query("UPDATE Customer c SET c.state = :state , c.files = :files WHERE c.id = :cid")
    @Modifying
    public int updateState(@Param("cid") long cid, @Param("state") State state, @Param("files") String files);

}
