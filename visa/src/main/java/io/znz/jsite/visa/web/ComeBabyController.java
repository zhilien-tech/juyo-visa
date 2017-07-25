/**
 * ComeBabyController.java
 * io.znz.jsite.visa.web
 * Copyright (c) 2017, 北京科技有限公司版权所有.
*/

package io.znz.jsite.visa.web;

import io.znz.jsite.core.entity.companyjob.CompanyJobEntity;
import io.znz.jsite.core.util.Const;
import io.znz.jsite.visa.forms.comebaby.ComeBabySqlForm;
import io.znz.jsite.visa.service.comebaby.ComeBabyService;

import javax.servlet.http.HttpSession;

import org.nutz.dao.Dao;
import org.nutz.dao.pager.Pager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.uxuexi.core.common.util.Util;
import com.uxuexi.core.db.dao.IDbDao;
import com.uxuexi.core.web.base.page.Pagination;

/**
 * TODO(这里用一句话描述这个类的作用)
 * <p>
 * TODO(这里描述这个类补充说明 – 可选)
 *
 * @author   孙斌
 * @Date	 2017年7月25日 	 
 */
@Controller
@RequestMapping("visa/comebaby")
public class ComeBabyController {
	@Autowired
	protected IDbDao dbDao;

	@Autowired
	protected Dao nutDao;

	@Autowired
	private ComeBabyService comeBabyService;

	/**
	 * 回显美国基本信息数据
	 * @param request
	 */
	@RequestMapping(value = "comeList")
	@ResponseBody
	public Object comeList(@RequestBody ComeBabySqlForm form, final HttpSession session) {
		CompanyJobEntity company = (CompanyJobEntity) session.getAttribute(Const.USER_COMPANY_KEY);
		if (!Util.isEmpty(company)) {
			long comId = company.getComId();
			form.setComId(comId);
		}
		Pager pager = new Pager();
		pager.setPageNumber(form.getPageNumber());
		pager.setPageSize(form.getPageSize());
		Pagination listPage = comeBabyService.listPage(form, pager);
		return comeBabyService.listPage(form, pager);
	}

}
