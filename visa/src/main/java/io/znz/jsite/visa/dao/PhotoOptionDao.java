package io.znz.jsite.visa.dao;

import io.znz.jsite.base.HibernateDao;
import io.znz.jsite.visa.bean.PhotoOption;
import io.znz.jsite.visa.bean.helper.Range;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Chaly on 2017/3/13.
 */
@Repository
public interface PhotoOptionDao extends HibernateDao<PhotoOption, Integer> {

    @Query("SELECT po FROM PhotoOption po WHERE po.useFor = '通用' OR po.useFor = ?1")
    List<PhotoOption> listOptionsByRange(Range range);

}
