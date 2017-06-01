package io.znz.jsite.core.dao;

import io.znz.jsite.base.HibernateDao;
import io.znz.jsite.core.bean.Plugin;
import org.springframework.stereotype.Repository;


/**
 * 插件DAO
 * @author Chaly
 */
@Repository
public interface PluginDao extends HibernateDao<Plugin, Integer> {

}
