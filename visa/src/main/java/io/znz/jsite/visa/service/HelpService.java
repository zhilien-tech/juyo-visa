package io.znz.jsite.visa.service;

import io.znz.jsite.base.BaseService;
import io.znz.jsite.base.HibernateDao;
import io.znz.jsite.visa.bean.Help;
import io.znz.jsite.visa.bean.HelpPK;
import io.znz.jsite.visa.dao.HelpDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Chaly on 2017/3/29.
 */
@Service
@Transactional(readOnly = true)
public class HelpService extends BaseService<Help, Integer> {

    @Autowired
    private HelpDao helpDao;

    @Override
    public HibernateDao<Help, Integer> getEntityDao() {
        return helpDao;
    }

    public Help findByKeyAndCid(String key, long cid) {
        return helpDao.findById_KeyAndId_Cid(key, cid);
    }

    @Transactional
    public void deleteByCustomer(long cid) {
        helpDao.deleteByCustomer(cid);
    }


    public Map<String, String> findByCid(long cid) {
        List<Help> helps = helpDao.findById_Cid(cid);
        Map<String, String> map = new HashMap<String, String>();
        if (helps != null && helps.size() > 0) {
            for (Help help : helps) {
                HelpPK pk = help.getId();
                map.put(pk.getKey(), help.getMsg());
            }
        }
        return map;
    }

}
