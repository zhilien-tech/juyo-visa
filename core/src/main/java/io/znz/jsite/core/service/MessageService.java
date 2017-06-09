package io.znz.jsite.core.service;

import io.znz.jsite.base.BaseService;
import io.znz.jsite.base.HibernateDao;
import io.znz.jsite.core.bean.Dict;
import io.znz.jsite.core.bean.Message;
import io.znz.jsite.core.bean.User;
import io.znz.jsite.core.bean.UserMsg;
import io.znz.jsite.core.util.UserUtil;
import io.znz.jsite.core.dao.MessageDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 消息Service
 */
@Service
@Transactional(readOnly = true)
public class MessageService extends BaseService<Message, Integer> {
    @Autowired
    private MessageDao messageDao;
    @Autowired
    UserMsgService userMsgService;
    @Autowired
    DictService dictService;

    @Override
    public HibernateDao<Message, Integer> getEntityDao() {
        return messageDao;
    }

    public List<Message> findNewMessageByUserId(int uid){
        return null;
    }

    public void sendMessage(User to, String title, String content, Dict type){
        //产生录用消息
        Message message = new Message();
        message.setFrom(UserUtil.getUser());
        message.setCreateDate(new Date());
        message.setContent(content);
        message.setTitle(title);
        message.setType(type);
        messageDao.save(message);
        //发送到指定人
        UserMsg userMessage = new UserMsg();
        userMessage.setUser(to);
        userMessage.setMessage(message);
        userMessage.setDel(false);
        userMessage.setRead(false);
        userMessage.setState(0);
        userMsgService.save(userMessage);
    }
}
