package io.znz.jsite.core.dao;

import io.znz.jsite.base.HibernateDao;
import io.znz.jsite.core.bean.Dict;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 字典DAO
 * @author Chaly
 */
@Repository
public interface DictDao extends HibernateDao<Dict, Integer> {

    List<Dict> findByKeyOrderBySort(String key);

    List<Dict> findByKeyAndCategoryOrderBySort(String key, String category);

    List<Dict> findByKeyAndCategoryNotOrderBySort(String key, String category);

    Dict findByKeyAndValueOrderBySort(String key, String value);

    List<Dict> findByIdIn(Integer ids[]);

    Dict findByValue(String value);

    @Query("SELECT dict.key FROM Dict dict GROUP BY dict.key")
    List<String> findAllKey();

    List<Dict> findByKeyInOrderBySortDesc(List<String> keys);
}
