package io.znz.jsite.visa.dao;

import io.znz.jsite.base.HibernateDao;
import io.znz.jsite.visa.bean.Order;
import org.springframework.stereotype.Repository;

/**
 * Created by Chaly on 2017/3/7.
 */
@Repository
public interface OrderDao extends HibernateDao<Order, Long> {

}
