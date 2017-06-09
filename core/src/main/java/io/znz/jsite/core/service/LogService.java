package io.znz.jsite.core.service;

import io.znz.jsite.base.BaseService;
import io.znz.jsite.base.HibernateDao;
import io.znz.jsite.core.bean.Log;
import io.znz.jsite.core.dao.LogDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 日志service
 * @author Chaly
 */
@Service
@Transactional(readOnly = true)
public class LogService extends BaseService<Log, Integer> {

    @Autowired
    private LogDao logDao;

    @Override
    public HibernateDao<Log, Integer> getEntityDao() {
        return logDao;
    }

    /**
     * 批量删除日志
     */
    @Transactional(readOnly = false)
    public void deleteLog(List<Integer> idList) {
        logDao.deleteBatch(idList);
    }
}
