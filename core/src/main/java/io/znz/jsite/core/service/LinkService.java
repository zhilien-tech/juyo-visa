package io.znz.jsite.core.service;

import io.znz.jsite.base.BaseService;
import io.znz.jsite.base.HibernateDao;
import io.znz.jsite.core.bean.Link;
import io.znz.jsite.core.dao.LinkDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 友情链接service
 */
@Service
@Transactional(readOnly = true)
public class LinkService extends BaseService<Link, Integer> {

    @Autowired
    private LinkDao linkDao;

    @Override
    public HibernateDao<Link, Integer> getEntityDao() {
        return linkDao;
    }

    /**
     * 查找是否可用找友情链接
     */
    public List<Link> findByEnabled(boolean enabled) {
        return linkDao.findByEnabledOrderBySort(enabled);
    }

    /**
     * 按照类型找友情链接
     */
    public List<Link> findByType(String type) {
        return linkDao.findByTypeOrderBySort(type);
    }

    /**
     * 按照位置找友情链接
     */
    public List<Link> findByPosition(String position) {
        return linkDao.findByPositionOrderBySort(position);
    }

    /**
     * 按照类型和位置找友情链接
     */
    public List<Link> findByTypeAndPosition(String type, String position) {
        return linkDao.findByTypeAndPositionOrderBySort(type, position);
    }

    /**
     * 按照类型和可用状态查找友情链接
     */
    public List<Link> findByTypeAndEnabled(String type, boolean enabled) {
        return linkDao.findByTypeAndEnabledOrderBySort(type, enabled);
    }

    /**
     * 按照位置和可用状态查找友情链接
     */
    public List<Link> findByPositionAndEnabled(String position, int limit) {
        return linkDao.findByPositionAndEnabledTrueOrderBySort(position, limit);
    }

    /**
     * 按照位置,类型和可用状态查找友情链接
     */
    public List<Link> findByTypeAndPositionAndEnabled(String type, String position, boolean enabled) {
        return linkDao.findByTypeAndPositionAndEnabledOrderBySort(type, position, enabled);
    }

    /**
     * 查看友情链接并增加点击量
     */
    public Link updateViews(int id) {
        return linkDao.updateViews(id);
    }

}
