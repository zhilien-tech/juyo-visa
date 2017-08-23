/**
 * SqsJpTotleService.java
 * io.znz.jsite.visa.service.sqsjptotal
 * Copyright (c) 2017, 北京科技有限公司版权所有.
*/

package io.znz.jsite.visa.service.sqsjptotal;

import io.znz.jsite.base.NutzBaseService;
import io.znz.jsite.core.entity.companyjob.CompanyJobEntity;
import io.znz.jsite.core.util.Const;
import io.znz.jsite.visa.forms.SqlJpTotalForm;
import io.znz.jsite.visa.totalbean.TotalJpBean;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.nutz.dao.pager.Pager;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

/**
 * 日本送签社统计
 * @author   崔建斌
 * @Date	 2017年8月22日 	 
 */
@Service
public class SqsJpTotleService extends NutzBaseService {
	public Object sqsjptotalList(SqlJpTotalForm sqlForm, Pager pager, final HttpSession session) {
		//通过session获取公司的id
		CompanyJobEntity company = (CompanyJobEntity) session.getAttribute(Const.USER_COMPANY_KEY);
		long comId = company.getComId();//得到公司的id
		sqlForm.setComId(comId);
		pager = new Pager();
		pager.setPageNumber(sqlForm.getPageNumber());
		pager.setPageSize(sqlForm.getPageSize());
		List<TotalJpBean> list = Lists.newArrayList();

		return this.listPage(sqlForm, pager);
	}
}
