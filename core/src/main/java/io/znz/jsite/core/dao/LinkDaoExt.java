package io.znz.jsite.core.dao;

import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * 友情链接DAO
 */
@Repository
public interface LinkDaoExt<T> {

    /**
     * 按照位置和可用状态查找友情链接
     */
    public List<T> findByPositionAndEnabledTrueOrderBySort(String position, int limit);

}
