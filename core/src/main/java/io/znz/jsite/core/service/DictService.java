package io.znz.jsite.core.service;

import io.znz.jsite.base.BaseService;
import io.znz.jsite.base.HibernateDao;
import io.znz.jsite.core.bean.Dict;
import io.znz.jsite.core.dao.DictDao;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 字典service
 * @author Chaly
 */
@Service
@Transactional(readOnly = true)
public class DictService extends BaseService<Dict, Integer> {

    @Autowired
    private DictDao dictDao;

    @Override
    public HibernateDao<Dict, Integer> getEntityDao() {
        return dictDao;
    }

    public List<Dict> findByKey(String Key) {
        return dictDao.findByKeyOrderBySort(Key);
    }

    public List<Dict> findByIdIn(Integer[] ids) {
        return dictDao.findByIdIn(ids);
    }

    public List<Dict> findByKeyAndCategory(String key, String category) {
        return dictDao.findByKeyAndCategoryOrderBySort(key, category);
    }

    public List<Dict> findByKeyAndCategoryNot(String key, String category) {
        return dictDao.findByKeyAndCategoryNotOrderBySort(key, category);
    }

    public Dict findByKeyAndValue(String key, String value) {
        return dictDao.findByKeyAndValueOrderBySort(key, value);
    }

    public Dict findByValue(String value) {
        return dictDao.findByValue(value);
    }

    /**
     * * 按照KEY和VALUE查找字典数据,如果不存在则尝试使用key和value创建一个新字典
     * @param key 字典的key
     * @param value value
     * @param label 字典的label
     * @param args args[0]:备注(字符串);args[1]:排序(正整数);args[0]:分类(字符串);
     * @return 如果数据库存在则返回数据库中的值, 否则返回新建的值
     */
    @Transactional(readOnly = false)
    public Dict findOrCreateByKeyAndValue(String key, String value, String label, String category, String... args) {
        Dict dict = dictDao.findByKeyAndValueOrderBySort(key, value);
        if (dict != null) {
            return dict;
        }
        dict = new Dict();
        dict.setKey(key);
        dict.setValue(value);
        dict.setLabel(label);
        dict.setDelFlag(false);
        if (StringUtils.isNotBlank(category)) {
            dict.setCategory(category);
        }
        if (args != null) {
            if (args.length > 0) {
                dict.setRemark(args[0].toString());
            }
            if (args.length > 1) {
                dict.setSort(Integer.parseInt(args[1].toString()));
            }
            if (args.length > 2) {
                dict.setCategory(args[2].toString());
            }
        }
        dict = dictDao.save(dict);
        return dict;
    }

    public List<String> findAllKey() {
        return dictDao.findAllKey();
    }

    public List<Dict> findByKeys(List<String> keys) {
        return dictDao.findByKeyInOrderBySortDesc(keys);
    }
}
