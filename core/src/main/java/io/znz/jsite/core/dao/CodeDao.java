package io.znz.jsite.core.dao;

import io.znz.jsite.base.HibernateDao;
import io.znz.jsite.core.bean.Code;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * 验证码DAO
 */
@Repository
public interface CodeDao extends HibernateDao<Code, Integer> {
    @Transactional
    @Modifying
    @Query("UPDATE Code code SET code.state=?2 , code.updateDate = CURRENT_DATE WHERE code.id= ?1")
    public int updateState(long id, int state);

    @Query("SELECT COUNT(code.id) FROM Code code WHERE code.targets =?1 AND code.createDate BETWEEN ?2 AND ?3")
    public int todayCount(String targets, Date startDate, Date endDate);

    @Query("SELECT code FROM Code code WHERE code.targets=?1 AND code.code=?2 AND code.state=?3 AND code.createDate >?4")
    public Code findByTargetAndCodeAndStateAndCreateDateAfter(String targets, String code, int state, Date createDate);

}
