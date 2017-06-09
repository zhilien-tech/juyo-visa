package io.znz.jsite.core.service;

import io.znz.jsite.base.BaseService;
import io.znz.jsite.base.HibernateDao;
import io.znz.jsite.core.bean.Plugin;
import io.znz.jsite.core.dao.PluginDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 插件service
 * @author Chaly
 */
@Service
@Transactional(readOnly = true)
public class PluginService extends BaseService<Plugin, Integer> {

    @Autowired
    private PluginDao pluginDao;

    @Override
    public HibernateDao<Plugin, Integer> getEntityDao() {
        return pluginDao;
    }
}
