package io.znz.jsite.visa.dao;

import io.znz.jsite.base.HibernateDao;
import io.znz.jsite.visa.bean.History;
import org.springframework.stereotype.Repository;

/**
 * Created by Chaly on 2017/3/10.
 */
@Repository
public interface HistoryDao extends HibernateDao<History, Integer> {
}
