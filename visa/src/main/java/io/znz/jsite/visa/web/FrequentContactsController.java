/**
 * FrequentContactsController.java
 * io.znz.jsite.visa.web
 * Copyright (c) 2017, 北京科技有限公司版权所有.
*/

package io.znz.jsite.visa.web;

import io.znz.jsite.base.BaseController;

import org.nutz.dao.Dao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.uxuexi.core.db.dao.IDbDao;

/**
 * 常用联系人
 * @author   崔建斌
 * @Date	 2017年6月20日 	 
 */
@Controller
@RequestMapping("visa/frequentcontacts")
public class FrequentContactsController extends BaseController {
	@Autowired
	protected IDbDao dbDao;

	@Autowired
	protected Dao nutDao;
}
