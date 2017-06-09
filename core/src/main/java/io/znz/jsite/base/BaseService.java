package io.znz.jsite.base;

import org.hibernate.criterion.Criterion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;


/**
 * 基础service 所有service继承该类
 * 提供基础的实体操作方法
 * 使用HibernateDao<T,PK>进行业务对象的CRUD操作,子类需重载getEntityDao()函数提供该DAO.
 *
 * @author Chaly
 */
public abstract class BaseService<T, PK extends Serializable> {

    /**
     * 子类需要实现该方法，提供注入的dao
     */
    public abstract HibernateDao<T, PK> getEntityDao();

    @Transactional(readOnly = true)
    public T get(final PK id) {
        return getEntityDao().findOne(id);
    }

    @Transactional(readOnly = false)
    public T save(final T entity) {
        if (entity == null) return null;
        return getEntityDao().save(entity);
    }
    @Transactional(readOnly = false)
    public List<T> save(final List<T> entities) {
        if (entities == null) return null;
        return getEntityDao().save(entities);
    }

    @Transactional(readOnly = false)
    public T update(final T entity) {
        return getEntityDao().save(entity);
    }

    @Transactional(readOnly = false)
    public void delete(final T entity) {
        getEntityDao().delete(entity);
    }

    @Transactional(readOnly = false)
    public void delete(final PK id) {
        getEntityDao().delete(id);
    }

    @Transactional(readOnly = false)
    public void deleteAll(Iterable<? extends T> entities) {
        getEntityDao().delete(entities);
    }

    @Transactional(readOnly = true)
    public List<T> getAll() {
        return getEntityDao().findAll();
    }

    @Transactional(readOnly = true)
    public List<T> getAll(Sort sort) {
        return getEntityDao().findAll(sort);
    }

    @Transactional(readOnly = true)
    public Page<T> getAll(Pageable pageable) {
        return getEntityDao().findAll(pageable);
    }

    public List<T> search(final List<PropertyFilter> filters) {
        return getEntityDao().find(filters);
    }

    @Transactional(readOnly = true)
    public Page<T> search(final Pageable pageable, final List<PropertyFilter> filters) {
        return getEntityDao().findPage(pageable, filters);
    }

    @Transactional(readOnly = true)
    public Page<T> search(final Pageable pageable, final Criterion ... filters) {
        return getEntityDao().findPage(pageable, filters);
    }

    public void clear() {
        getEntityDao().clear();
    }

    /**
     * 按照主键清除缓存
     */
    public void evict(Object primaryKey) {
        getEntityDao().evict(primaryKey);
    }

}
