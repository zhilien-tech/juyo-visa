package io.znz.jsite.core.dao;

import io.znz.jsite.base.HibernateDao;
import io.znz.jsite.core.bean.Area;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * 区域DAO
 */
@Repository
public interface AreaDao extends HibernateDao<Area, Integer> {

    /**
     * 健在父级为NULL的区域
     */
    public List<Area> findByParent_IdIsNull();

    /**
     * 按照父级id获取区域列表
     * @param pid 父级
     * @return 区域里列表
     */
    public List<Area> findByParent_Id(Integer pid);

    /**
     * 按照级别查找区域列表
     * @param level 区域在数据库中对应的级别编码
     */
    public List<Area> findByLevel(int level);

}
