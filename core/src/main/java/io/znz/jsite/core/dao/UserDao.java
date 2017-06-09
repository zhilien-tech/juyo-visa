package io.znz.jsite.core.dao;

import io.znz.jsite.base.HibernateDao;
import io.znz.jsite.core.bean.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;


/**
 * 用户DAO
 *
 * @author Chaly
 */
@Repository
public interface UserDao extends HibernateDao<User, String> {

    User findByLoginName(String loginName);

    @Transactional
    @Modifying
    @Query("UPDATE User user SET user.name = ?2 WHERE user.id = ?1")
    public int updateName(Integer id, String name);

    @Transactional
    @Modifying
    @Query("UPDATE User user SET user.loginName = ?2 WHERE user.id = ?1")
    public int updateLoginName(Integer id, String loginName);

    @Query("SELECT COUNT(user.id) FROM User user WHERE user.createDate >?1")
    public int countByDate(Date date);

    @Query("FROM User user WHERE user.createDate BETWEEN ?1 AND ?2")
    List<User> findByDate(Date start, Date end);

    @Query("SELECT COUNT(user.id) FROM User user WHERE user.loginName = ?1")
    public int existLoginName(String loginName);

    @Query("FROM User user WHERE user.id IN ?1 AND user.name LIKE ?2")
    List<User> searchByIdsAndName(List<String> ids, String keyword);
}
