package io.znz.jsite.core.dao;

import io.znz.jsite.core.bean.UserMsg;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

public class UserMsgDaoImpl implements UserMsgDaoExt {

    @PersistenceContext
    private EntityManager em;

    public UserMsg getLastMessageByUser(String uid) {
        String jpql = "SELECT um FROM UserMessage um WHERE um.user.id = :uid";
        Query query = em.createQuery(jpql);
        query.setParameter("uid", uid);
        query.setMaxResults(1);
        try {
            return (UserMsg) query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
}
