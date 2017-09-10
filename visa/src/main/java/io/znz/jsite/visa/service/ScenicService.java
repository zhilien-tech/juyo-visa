package io.znz.jsite.visa.service;

import io.znz.jsite.base.BaseService;
import io.znz.jsite.base.HibernateDao;
import io.znz.jsite.util.StringUtils;
import io.znz.jsite.visa.bean.Scenic;
import io.znz.jsite.visa.dao.ScenicDao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Chaly on 2017/4/11.
 */
@Service
@Transactional(readOnly = true)
public class ScenicService extends BaseService<Scenic, Integer> {

	@Autowired
	private ScenicDao scenicDao;

	public HibernateDao<Scenic, Integer> getEntityDao() {
		return scenicDao;
	}

	public List<Scenic> findByFilter(String filter) {
		List<Scenic> result;
		if (StringUtils.isNotBlank(filter))
			result = scenicDao.findByFilter(filter);
		else
			result = scenicDao.findAll();
		return result;
	}

	public List<Scenic> findByCity(String filter) {
		List<Scenic> result;
		if (StringUtils.isNotBlank(filter))
			result = scenicDao.findByCity(filter);
		else
			result = scenicDao.findAll();
		return result;
	}
}
