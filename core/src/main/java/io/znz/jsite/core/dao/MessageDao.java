package io.znz.jsite.core.dao;

import io.znz.jsite.base.HibernateDao;
import io.znz.jsite.core.bean.Message;
import org.springframework.stereotype.Repository;



@Repository
public interface MessageDao extends HibernateDao<Message, Integer> {
}
