package io.znz.jsite.visa.dao;

import io.znz.jsite.visa.bean.Customer;
import io.znz.jsite.visa.bean.helper.State;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

/**
 * Created by Chaly on 2017/3/10.
 */
@Repository
public class CustomerDaoImpl implements CustomerDaoExt<Customer> {

    @PersistenceContext
    private EntityManager em;

    public List<Customer> findByState(State state) {
        StringBuilder jpql = new StringBuilder();
        jpql.append("FROM Customer c WHERE c.state = :state");
        Query sqlQuery = em.createQuery(jpql.toString(), Customer.class);
        sqlQuery.setParameter("state", state);
        sqlQuery.setMaxResults(1);
        return sqlQuery.getResultList();
    }
}
