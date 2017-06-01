package io.znz.jsite.base;

import io.znz.jsite.util.ReflectionUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.internal.CriteriaImpl;
import org.hibernate.transform.ResultTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaEntityInformationSupport;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.util.Assert;

import javax.persistence.EntityManager;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * {@link HibernateDao}接口实现类，并在{@link SimpleJpaRepository}基础上扩展。
 * @param <T> ORM对象
 * @param <PK> 主键ID
 * @author Chaly on 15/9/24.
 */
@NoRepositoryBean   // 必须的
public class HibernateImpl<T, PK extends Serializable> extends SimpleJpaRepository<T, PK> implements HibernateDao<T, PK> {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    protected Class<T> entityClass;

    private final EntityManager em;

    public HibernateImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        em = entityManager;
        entityClass = entityInformation.getJavaType();
    }

    public HibernateImpl(Class<T> domainClass, EntityManager em) {
        this(JpaEntityInformationSupport.getEntityInformation(domainClass, em), em);
    }

    /**
     * 取得当前Session.
     * @return Session
     */
    public Session getSession() {
        return em.unwrap(Session.class);
    }

    /**
     * 按属性条件参数创建Criterion,辅助函数.
     */
    protected Criterion buildCriterion(final String propertyName, final Object propertyValue,
        final PropertyFilter.MatchType matchType
    ) {
        Assert.hasText(propertyName, "propertyName不能为空");
        Criterion criterion = null;
        //根据MatchType构造criterion
        switch (matchType) {
            case EQ:
                criterion = Restrictions.eq(propertyName, propertyValue);
                break;
            case LIKE:
                criterion = Restrictions.like(propertyName, (String) propertyValue, MatchMode.ANYWHERE);
                break;
            case LE:
                criterion = Restrictions.le(propertyName, propertyValue);
                break;
            case LT:
                criterion = Restrictions.lt(propertyName, propertyValue);
                break;
            case GE:
                criterion = Restrictions.ge(propertyName, propertyValue);
                break;
            case GT:
                criterion = Restrictions.gt(propertyName, propertyValue);
        }
        return criterion;
    }

    /**
     * 按属性条件列表创建Criterion数组,辅助函数.
     */
    protected Criterion[] buildCriterionByPropertyFilter(final List<PropertyFilter> filters) {
        List<Criterion> criterionList = new ArrayList<Criterion>();
        for (PropertyFilter filter : filters) {
            if (!filter.hasMultiProperties()) { //只有一个属性需要比较的情况.
                Criterion criterion = buildCriterion(filter.getPropertyName(), filter.getMatchValue(), filter
                    .getMatchType());
                criterionList.add(criterion);
            } else {//包含多个属性需要比较的情况,进行or处理.
                Disjunction disjunction = Restrictions.disjunction();
                for (String param : filter.getPropertyNames()) {
                    Criterion criterion = buildCriterion(param, filter.getMatchValue(), filter.getMatchType());
                    disjunction.add(criterion);
                }
                criterionList.add(disjunction);
            }
        }
        return criterionList.toArray(new Criterion[criterionList.size()]);
    }

    /**
     * 根据Criterion条件创建Criteria.
     * 与find()函数可进行更加灵活的操作.
     * @param criterions 数量可变的Criterion.
     * @return Criteria
     */
    public Criteria createCriteria(final Criterion... criterions) {
        em.getCriteriaBuilder().createQuery();
        Criteria criteria = getSession().createCriteria(entityClass);
        for (Criterion c : criterions) {
            criteria.add(c);
        }
        return criteria;
    }

    public Criteria createCriteria(Boolean isCache, final Criterion... criterions) {
        Criteria criteria = getSession().createCriteria(entityClass);
        for (Criterion c : criterions) {
            criteria.add(c);
        }
        criteria.setCacheable(isCache);
        return criteria;
    }

    /**
     * 按属性过滤条件列表查找对象列表.
     */
    public List<T> find(List<PropertyFilter> filters) {
        Criterion[] criterions = buildCriterionByPropertyFilter(filters);
        return find(criterions);
    }

    /**
     * 按Criteria查询对象列表.
     * @param criterions 数量可变的Criterion.
     * @return 结果集合
     */
    public List<T> find(final Criterion... criterions) {
        return createCriteria(criterions).list();
    }

    /**
     * 执行count查询获得本次Criteria查询所能获得的对象总数.
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    protected long countCriteriaResult(final Criteria c) {
        CriteriaImpl impl = (CriteriaImpl) c;

        // 先把Projection、ResultTransformer、OrderBy取出来,清空三者后再执行Count操作
        Projection projection = impl.getProjection();
        ResultTransformer transformer = impl.getResultTransformer();

        List<CriteriaImpl.OrderEntry> orderEntries = null;
        try {
            orderEntries = (List) ReflectionUtil.getFieldValue(impl, "orderEntries");
            ReflectionUtil.setFieldValue(impl, "orderEntries", new ArrayList());
        } catch (Exception e) {
            logger.error("不可能抛出的异常:{}", e.getMessage());
        }

        // 执行Count查询
        Long totalCountObject = (Long) c.setProjection(Projections.rowCount()).uniqueResult();
        long totalCount = (totalCountObject != null) ? totalCountObject : 0;

        // 将之前的Projection,ResultTransformer和OrderBy条件重新设回去
        c.setProjection(projection);

        if (projection == null) {
            c.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
        }
        if (transformer != null) {
            c.setResultTransformer(transformer);
        }
        try {
            ReflectionUtil.setFieldValue(impl, "orderEntries", orderEntries);
        } catch (Exception e) {
            logger.error("不可能抛出的异常:{}", e.getMessage());
        }

        return totalCount;
    }

    /**
     * 设置分页参数到Criteria对象,辅助函数.
     */
    protected Criteria setPageParameterToCriteria(final Criteria c, final Pageable pageable) {
        Assert.isTrue(pageable.getPageSize() > 0, "Page Size must larger than zero");
        //hibernate的firstResult的序号从0开始
        c.setFirstResult((pageable.getPageNumber()) * pageable.getPageSize());
        c.setMaxResults(pageable.getPageSize());
        if (pageable.getSort() != null) {
            Iterator<Sort.Order> iterator = pageable.getSort().iterator();
            while (iterator.hasNext()) {
                Sort.Order order = iterator.next();
                if (order.isAscending()) {
                    c.addOrder(Order.asc(order.getProperty()));
                } else {
                    c.addOrder(Order.desc(order.getProperty()));
                }
            }
        }
        return c;
    }

    /**
     * 按Criteria分页查询.
     * @param pageable 分页参数.
     * @param criterions 数量可变的Criterion.
     * @return 分页查询结果.附带结果列表及所有查询输入参数.
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public Page<T> findPage(final Pageable pageable, final Criterion... criterions) {
        Assert.notNull(pageable, "page不能为空");
        Criteria c = createCriteria(criterions);
        long totalCount = countCriteriaResult(c);
        setPageParameterToCriteria(c, pageable);
        Page<T> page = new PageImpl(c.list(), pageable, totalCount);
        return page;
    }

    /**
     * 按属性过滤条件列表分页查找对象.
     */
    public Page<T> findPage(final Pageable pageable, final List<PropertyFilter> filters) {
        Criterion[] criterions = buildCriterionByPropertyFilter(filters);
        return findPage(pageable, criterions);
    }

    public void clear() {
        em.clear();
    }

    public void evict(Object primaryKey) {
        em.getEntityManagerFactory().getCache().evict(entityClass, primaryKey);
    }
}
