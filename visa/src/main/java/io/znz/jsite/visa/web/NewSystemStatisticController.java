/**
 * SystemStatisticController.java
 * io.znz.jsite.visa.web
 * Copyright (c) 2017, 北京科技有限公司版权所有.
*/

package io.znz.jsite.visa.web;

import io.znz.jsite.visa.form.NewSysStatisticSqlForm;
import io.znz.jsite.visa.service.NewSystemStatisticService;

import org.nutz.dao.pager.Pager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.uxuexi.core.db.dao.IDbDao;

/**
 * 系统平台 统计列表
 * <p>
 *
 * @author   彭辉
 * @Date	 2017年8月22日 	 
 */
@Controller
@RequestMapping("visa/systemstatistic")
public class NewSystemStatisticController {

	@Autowired
	protected IDbDao dbDao;

	@Autowired
	private NewSystemStatisticService newSystemStatisticService;

	//统计列表
	@RequestMapping(value = "list")
	@ResponseBody
	public Object list(@RequestBody NewSysStatisticSqlForm form) {
		Pager pager = new Pager();
		pager.setPageNumber(form.getPageNumber());
		pager.setPageSize(form.getPageSize());
		return newSystemStatisticService.listPage(form, pager);
	}

	//下拉框初始化
	@RequestMapping(value = "compSelectfind")
	@ResponseBody
	public Object compSelectfind(int compType) {
		return newSystemStatisticService.compSelectfind(compType);
	}
}
