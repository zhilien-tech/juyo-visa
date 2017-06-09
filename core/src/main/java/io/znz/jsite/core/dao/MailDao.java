package io.znz.jsite.core.dao;

import io.znz.jsite.base.HibernateDao;
import io.znz.jsite.core.bean.Mail;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


/**
 * 邮箱DAO
 */
@Repository
public interface MailDao extends HibernateDao<Mail, Integer> {

    @Query("FROM Mail mail")
    public Mail getSingleOne();

    @Query("FROM Mail mail WHERE mail.available = true")
    public Mail getAvailableOne();

}
