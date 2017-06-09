package io.znz.jsite.core.dao;

import io.znz.jsite.base.HibernateDao;
import io.znz.jsite.core.bean.Log;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * 日志DAO
 * @author Chaly
 */
@Repository
public interface LogDao extends HibernateDao<Log, Integer> {

    /**
     * 批量删除日志
     * @param idList 日志id列表
     */
    @Transactional
    @Modifying
    @Query("DELETE FROM Log log where log.id in :idList")
    public void deleteBatch(@Param("idList") List<Integer> idList);
}
