package io.znz.jsite.core.dao;

import io.znz.jsite.base.HibernateDao;
import io.znz.jsite.core.bean.UserMsg;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface UserMsgDao extends HibernateDao<UserMsg, Integer>, UserMsgDaoExt {

    @Query("FROM UserMsg um WHERE um.user.id=?1 and um.del = ?2 and um.read= ?3 and um.state = ?4")
    public List<UserMsg> findUserMessageByUser_id(String uid, boolean del, boolean read, int state);

    public Page<UserMsg> findByUser_idAndDelOrderByMessage_CreateDateDesc(String uid, boolean del, Pageable pageable);

}
