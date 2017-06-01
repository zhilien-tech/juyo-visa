package io.znz.jsite.visa.service;

import io.znz.jsite.base.BaseService;
import io.znz.jsite.base.HibernateDao;
import io.znz.jsite.visa.bean.History;
import io.znz.jsite.visa.dao.HistoryDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Chaly on 2017/3/10.
 */
@Service
@Transactional(readOnly = true)
public class HistoryService extends BaseService<History, Integer> {

    @Autowired
    private HistoryDao historyDao;

    @Override
    public HibernateDao<History, Integer> getEntityDao() {
        return historyDao;
    }

}
