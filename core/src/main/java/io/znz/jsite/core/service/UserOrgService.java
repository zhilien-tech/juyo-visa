package io.znz.jsite.core.service;

import io.znz.jsite.base.BaseService;
import io.znz.jsite.base.HibernateDao;
import io.znz.jsite.core.bean.Org;
import io.znz.jsite.core.bean.User;
import io.znz.jsite.core.bean.UserOrg;
import io.znz.jsite.core.dao.UserOrgDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 用户机构Service
 */
@Service
@Transactional(readOnly = true)
public class UserOrgService extends BaseService<UserOrg, Integer> {

    @Autowired
    private UserOrgDao userOrgDao;

    @Override
    public HibernateDao<UserOrg, Integer> getEntityDao() {
        return userOrgDao;
    }

    /**
     * 添加修改用户机构
     */
    @Transactional(readOnly = false)
    public void updateUserOrg(String userId, List<Integer> orgIds) {
        // 删除老的机构关系
        userOrgDao.deleteUO(userId);
        // 添加新的机构关系
        if (orgIds != null && orgIds.size() > 0) {
            for (Integer orgId : orgIds) {
                userOrgDao.save(new UserOrg(new User(userId), new Org(orgId)));
            }
        }
    }

    /**
     * 获取用户拥有机构id集合
     * @return 结果集合
     */
    public List<Integer> getOrgIdList(String uid) {
        return userOrgDao.findOrgIds(uid);
    }

}
