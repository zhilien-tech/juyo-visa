package io.znz.jsite.visa.service;

import io.znz.jsite.base.HibernateDao;
import io.znz.jsite.base.NutzBaseService;
import io.znz.jsite.visa.bean.Order;
import io.znz.jsite.visa.dao.OrderDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Chaly on 2017/3/23.
 */
@Service
@Transactional(readOnly = true)
//public class OrderService extends BaseService<Order, Long> {
public class OrderService extends NutzBaseService {

	@Autowired
	private OrderDao orderDao;
	@Autowired
	private PdfService pdfService;

	public HibernateDao<Order, Long> getEntityDao() {
		return orderDao;
	}

}
