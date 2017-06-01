package io.znz.jsite.core.service;

import io.znz.jsite.base.BaseService;
import io.znz.jsite.base.HibernateDao;
import io.znz.jsite.core.bean.Area;
import io.znz.jsite.core.dao.AreaDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 区域service
 */
@Service
@Transactional(readOnly = true)
public class AreaService extends BaseService<Area, Integer> {

    @Autowired
    private AreaDao areaDao;

    @Override
    public HibernateDao<Area, Integer> getEntityDao() {
        return areaDao;
    }

    /**
     * 按照父级id获取区域列表,如果pid为空则加载顶级区域
     * @param pid 父级
     * @return 区域里列表
     */
    public List<Area> findByParent(Integer pid) {
        if (pid == null) {
            return areaDao.findByParent_IdIsNull();
        } else {
            return areaDao.findByParent_Id(pid);
        }
    }

    public List<Area> findByLevel(int level) {
        return areaDao.findByLevel(level);
    }

}
