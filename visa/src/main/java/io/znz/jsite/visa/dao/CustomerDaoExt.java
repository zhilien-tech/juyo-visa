package io.znz.jsite.visa.dao;

import io.znz.jsite.visa.bean.Customer;
import io.znz.jsite.visa.bean.Order;
import io.znz.jsite.visa.bean.helper.State;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Chaly on 2017/3/10.
 */
@Repository
public interface CustomerDaoExt<T> {

    public List<Customer> findByState(State state);

}
