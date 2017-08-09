/**
 * LogController.java
 * io.znz.jsite.visa.web
 * Copyright (c) 2017, 北京科技有限公司版权所有.
*/

package io.znz.jsite.visa.web;

import io.znz.jsite.visa.entity.log.NewLogsEntity;

import java.util.List;

import org.nutz.dao.Cnd;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.uxuexi.core.db.dao.IDbDao;

/**
 * TODO(这里用一句话描述这个类的作用)
 * <p>
 * TODO(这里描述这个类补充说明 – 可选)
 *
 * @author   孙斌
 * @Date	 2017年8月9日 	 
 */
@Controller
@RequestMapping("visa/newlog")
public class NewLogController {
	@Autowired
	protected IDbDao dbDao;

	@RequestMapping(value = "list")
	@ResponseBody
	public Object list() {
		List<NewLogsEntity> query = dbDao.query(NewLogsEntity.class,
				Cnd.where("id", "!=", 0).orderBy("createTime", "desc"), null);
		return query;
	}
}
