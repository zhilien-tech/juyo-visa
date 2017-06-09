package io.znz.jsite.core.dao;

import io.znz.jsite.base.HibernateDao;
import io.znz.jsite.core.bean.Link;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * 友情链接DAO
 */
@Repository
public interface LinkDao extends HibernateDao<Link, Integer>, LinkDaoExt {

    /**
     * 查找是否可用找友情链接
     */
    public List<Link> findByEnabledOrderBySort(boolean enabled);

    /**
     * 按照类型找友情链接
     */
    public List<Link> findByTypeOrderBySort(String type);

    /**
     * 按照位置找友情链接
     */
    public List<Link> findByPositionOrderBySort(String position);

    /**
     * 按照类型和可用状态查找友情链接
     */
    public List<Link> findByTypeAndEnabledOrderBySort(String type, boolean enabled);

    /**
     * 按照类型和位置找友情链接
     */
    public List<Link> findByTypeAndPositionOrderBySort(String type, String position);

    /**
     * 按照位置,类型和可用状态查找友情链接
     */
    public List<Link> findByTypeAndPositionAndEnabledOrderBySort(String type, String position, boolean enabled);

    @Query("UPDATE Link link SET link.views = link.views + 1 WHERE link.id = ?1")
    @Modifying
    public Link updateViews(int id);

}
