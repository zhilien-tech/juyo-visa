/**
 * NewVisaProgressController.java
 * io.znz.jsite.visa.web
 * Copyright (c) 2017, 北京科技有限公司版权所有.
*/

package io.znz.jsite.visa.web;

import io.znz.jsite.core.util.Const;
import io.znz.jsite.visa.entity.japan.NewCustomerJpEntity;
import io.znz.jsite.visa.entity.usa.NewCustomerEntity;
import io.znz.jsite.visa.entity.user.EmployeeEntity;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.SqlManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.beust.jcommander.internal.Maps;
import com.uxuexi.core.common.util.Util;
import com.uxuexi.core.db.dao.IDbDao;

/**
 * TODO(这里用一句话描述这个类的作用)
 * <p>
 * TODO(这里描述这个类补充说明 – 可选)
 *
 * @author   孙斌
 * @Date	 2017年6月27日 	 
 */
@Controller
@RequestMapping("visa/progress")
public class NewVisaProgressController {
	@Autowired
	protected IDbDao dbDao;
	/**nutz dao*/
	@Autowired
	protected Dao nutDao;
	/**
	 * 注入容器中的sqlManager对象，用于获取sql
	 */
	@Autowired
	protected SqlManager sqlManager;

	/**
	 * 列表查看订单信息
	 */
	@RequestMapping(value = "country")
	@ResponseBody
	public Object list(HttpServletRequest request) {
		EmployeeEntity user = (EmployeeEntity) request.getSession().getAttribute(Const.SESSION_NAME);
		//美国
		List<NewCustomerEntity> usalist = dbDao.query(NewCustomerEntity.class, Cnd.where("empid", "=", user.getId())
				.orderBy("createtime", "desc"), null);

		//日本
		List<NewCustomerJpEntity> japanlist = dbDao.query(NewCustomerJpEntity.class,
				Cnd.where("empid", "=", user.getId()).orderBy("createtime", "desc"), null);
		Map<String, Integer> map = Maps.newHashMap();
		if (!Util.isEmpty(japanlist) && !Util.isEmpty(usalist)) {
			map.put("usa", usalist.get(0).getId());
			map.put("japan", Integer.valueOf(japanlist.get(0).getId() + ""));

		} else if (Util.isEmpty(japanlist) && !Util.isEmpty(usalist)) {
			map.put("usa", usalist.get(0).getId());

		} else if (!Util.isEmpty(japanlist) && Util.isEmpty(usalist)) {
			map.put("japan", Integer.valueOf(japanlist.get(0).getId() + ""));

		}

		return map;
	}

}
