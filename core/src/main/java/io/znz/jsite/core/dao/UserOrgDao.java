package io.znz.jsite.core.dao;

import io.znz.jsite.base.HibernateDao;
import io.znz.jsite.core.bean.UserOrg;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * 用户机构Dao
 *
 */
@Repository
public interface UserOrgDao extends HibernateDao<UserOrg, Integer> {

    /**
     * 删除用户机构
     */
    @Transactional
    @Modifying
    @Query("DELETE UserOrg ur where ur.user.id=?1")
    public void deleteUO(String userId);

    /**
     * 查询用户拥有的机构id集合
     * @return 结果集合
     */
    @Query("select ur.org.id from UserOrg ur where ur.user.id=?1")
    public List<Integer> findOrgIds(String userId);

}
