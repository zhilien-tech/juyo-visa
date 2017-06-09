package io.znz.jsite.base.bean;

import io.znz.jsite.util.ConvertUtil;
import io.znz.jsite.util.StringUtils;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Junction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Chaly on 2017/3/21.
 */
public class PageFilter {
    public int page = 1;
    public int skip = 0;
    public int take = 20;
    public int pageSize = 20;
    public Filter filter;
    public List<Map<String, String>> sort;

    public static class Filter {
        public String field;
        public String operator;
        public String value;

        public String logic;
        public List<Filter> filters;
    }

    public enum Operator {
        EQ, NEQ, LT, GT, LE, GE, LTE, GTE,

        LIKE, STARTSWITH, CONTAINS, DOESNOTCONTAIN, ENDSWITH,

        ISNULL, ISNOTNULL, ISEMPTY, ISNOTEMPTY
    }

    private Criterion buildCriterion(Class clazz, Filter filter) throws NoSuchFieldException {
        Class type = getType(clazz, filter.field);
        Object value = ConvertUtil.convertStringToObject(filter.value, type);
        Operator operator = Operator.valueOf(filter.operator.toUpperCase());

        Criterion criterion = null;
        switch (operator) {
            case EQ:
                criterion = Restrictions.eq(filter.field, value);
                break;
            case NEQ:
                criterion = Restrictions.ne(filter.field, value);
                break;
            case LE:
            case LTE:
                criterion = Restrictions.le(filter.field, value);
                break;
            case LT:
                criterion = Restrictions.lt(filter.field, value);
                break;
            case GE:
            case GTE:
                criterion = Restrictions.ge(filter.field, value);
                break;
            case GT:
                criterion = Restrictions.gt(filter.field, value);
                break;
            case LIKE:
            case CONTAINS:
                criterion = Restrictions.ilike(filter.field, (String) value, MatchMode.ANYWHERE);
                break;
            case STARTSWITH:
                criterion = Restrictions.ilike(filter.field, (String) value, MatchMode.START);
                break;
            case ENDSWITH:
                criterion = Restrictions.ilike(filter.field, (String) value, MatchMode.END);
                break;
            case DOESNOTCONTAIN:
                criterion = Restrictions.sqlRestriction(filter.field + " NOT LIKE '%" + value + "%' ");
                break;
            case ISEMPTY:
                criterion = Restrictions.isEmpty(filter.field);
                break;
            case ISNOTEMPTY:
                criterion = Restrictions.isNotEmpty(filter.field);
                break;
            case ISNULL:
                criterion = Restrictions.isNull(filter.field);
                break;
            case ISNOTNULL:
                criterion = Restrictions.isNotNull(filter.field);
                break;
        }
        return criterion;
    }

    /**
     * 递归获取属性的类型 eg. "user.id"
     */
    private Class getType(Class rootClazz, String field) throws NoSuchFieldException {
        String f = StringUtils.substringBefore(field, ".");
        Class c = rootClazz.getDeclaredField(f).getType();
        String subField = StringUtils.substringAfter(field, ".");
        if (StringUtils.isBlank(subField)) {
            return c;
        } else {
            return getType(c, subField);
        }
    }

    public Criterion[] getFilter(Class clazz) {
        List<Criterion> list = new ArrayList<Criterion>();
        try {
            if (filter != null) {
                Junction junction = "or".equalsIgnoreCase(filter.logic) ? Restrictions.disjunction() : Restrictions.conjunction();
                if (filter.filters != null) {
                    for (Filter f1 : filter.filters) {
                        if (f1.filters != null && f1.filters.size() > 0) {
                            Junction j;
                            if ("or".equalsIgnoreCase(f1.logic)) {
                                j = Restrictions.disjunction();
                            } else {
                                j = Restrictions.conjunction();
                            }
                            for (Filter f2 : f1.filters) {
                                j.add(buildCriterion(clazz, f2));
                            }
                            junction.add(j);
                        } else {
                            junction.add(buildCriterion(clazz, f1));
                        }
                    }
                }
                list.add(junction);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list.toArray(new Criterion[list.size()]);
    }

    public Pageable getPageable() {
        Sort sorts = null;
        if (sort != null) {
            for (Map<String, String> map : sort) {
                Sort s = new Sort(Sort.Direction.fromString(map.get("dir")), map.get("field"));
                if (sorts == null) {
                    sorts = s;
                } else {
                    sorts.and(s);
                }
            }
        } else {
            sorts = new Sort(Sort.Direction.fromString("desc"), "id");
        }
        return new PageRequest(page - 1, pageSize, sorts);
    }

}
