package io.znz.jsite.core.dao;

import io.znz.jsite.core.bean.UserMsg;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMsgDaoExt {
    public UserMsg getLastMessageByUser(String uid);
}
