package io.znz.jsite.base;

import org.hibernate.criterion.Criterion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Chaly on 15/9/24.
 */
@NoRepositoryBean
public interface HibernateDao<T, PK extends Serializable> extends JpaRepository<T, PK> {

    public List<T> find(List<PropertyFilter> filters);

    public Page<T> findPage(final Pageable pageable, final List<PropertyFilter> filters);

    public Page<T> findPage(final Pageable pageable, final Criterion... criterions);

    public void clear();

    public void evict(Object primaryKey);

}