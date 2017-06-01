package io.znz.jsite.core.dao;

import io.znz.jsite.core.bean.Link;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;


/**
 * 友情链接的扩展DAO
 * @author Chaly
 */
@Repository
public class LinkDaoImpl implements LinkDaoExt<Link> {

    @PersistenceContext
    private EntityManager em;

    public List<Link> findByPositionAndEnabledTrueOrderBySort(String position, int limit) {
        StringBuilder jpql = new StringBuilder();
        jpql.append("FROM Link l WHERE l.position = :position AND l.enabled = true ORDER BY l.sort");
        Query sqlQuery = em.createQuery(jpql.toString(), Link.class);
        sqlQuery.setParameter("position",position);
        sqlQuery.setMaxResults(limit);
        return sqlQuery.getResultList();
    }
}
