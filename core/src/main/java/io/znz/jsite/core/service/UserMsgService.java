package io.znz.jsite.core.service;


import io.znz.jsite.base.BaseService;
import io.znz.jsite.base.HibernateDao;
import io.znz.jsite.core.bean.User;
import io.znz.jsite.core.bean.UserMsg;
import io.znz.jsite.core.dao.UserMsgDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional(readOnly = true)
public class UserMsgService extends BaseService<UserMsg, Integer> {
    @Autowired
    private UserMsgDao userMessageDao;

    @Override
    public HibernateDao<UserMsg, Integer> getEntityDao() {
        return userMessageDao;
    }

    /**
     * 根据用户ID查询消息
     */
    public List<UserMsg> findUserMessageByUserId(String uid) {
        return userMessageDao.findUserMessageByUser_id(uid, false, false, 0);
    }

    public Page<UserMsg> findByUser(String uid, int page, int size) {
        return userMessageDao.findByUser_idAndDelOrderByMessage_CreateDateDesc(uid, false, new PageRequest(page, size));
    }

    public UserMsg getLastMessageByUser(User user) {
        if (user == null || user.getId() == null) return null;
        return userMessageDao.getLastMessageByUser(user.getId());
    }

}
