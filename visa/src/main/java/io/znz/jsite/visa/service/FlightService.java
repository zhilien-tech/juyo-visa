package io.znz.jsite.visa.service;

import io.znz.jsite.base.BaseService;
import io.znz.jsite.base.HibernateDao;
import io.znz.jsite.util.StringUtils;
import io.znz.jsite.visa.bean.Flight;
import io.znz.jsite.visa.dao.FlightDao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Chaly on 2017/4/11.
 */
@Service
@Transactional(readOnly = true)
public class FlightService extends BaseService<Flight, Integer> {

	@Autowired
	private FlightDao flightDao;

	@Override
	public HibernateDao<Flight, Integer> getEntityDao() {
		return flightDao;
	}

	public List<Flight> findByFilter(String filter) {
		List<Flight> result;
		if (StringUtils.isNotBlank(filter))
			result = flightDao.findByFilter(filter);
		else
			result = flightDao.findAll();
		return result;
	}

	public List<Flight> filghtByCity(String fromCity, String toCity) {
		List<Flight> result = null;
		if (StringUtils.isNotBlank(fromCity) && StringUtils.isNotBlank(toCity)) {
			result = flightDao.filghtByCity(fromCity, toCity);
		} else {
			//result = flightDao.findAll();
		}
		return result;
	}
}
