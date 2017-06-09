package io.znz.jsite.core.dao;

import io.znz.jsite.base.HibernateDao;
import io.znz.jsite.core.bean.Org;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * 机构DAO
 */
@Repository
public interface OrgDao extends HibernateDao<Org, Integer> {

    /**
     * 健在父级为NULL的组织
     */
    List<Org> findByParent_IdIsNull();

    /**
     * 按照父级id获取组织列表
     * @param pid 父级
     * @return 区域里列表
     */
    List<Org> findByParent_Id(Integer pid);

    List<Org> findByIdNot(Integer id);
}
