/**
 * SqsJpTotalController.java
 * io.znz.jsite.visa.web
 * Copyright (c) 2017, 北京科技有限公司版权所有.
*/

package io.znz.jsite.visa.web;

import io.znz.jsite.base.BaseController;
import io.znz.jsite.visa.forms.djstotal.DjsJpTotalForm;
import io.znz.jsite.visa.service.djstotal.DjsJpTotleService;

import javax.servlet.http.HttpSession;

import org.nutz.dao.pager.Pager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 日本送签社统计
 * @author   崔建斌
 * @Date	 2017年8月22日 	 
 */
@Controller
@RequestMapping("visa/djsjptotal")
public class DjsJpTotalController extends BaseController {

	@Autowired
	private DjsJpTotleService djsJpTotleService;//日本送签社统计

	/**
	 * 送签社统计列表查询
	 */
	@RequestMapping(value = "djsjptotalList")
	@ResponseBody
	public Object djsjptotalList(@RequestBody DjsJpTotalForm sqlForm, Pager pager, final HttpSession session) {
		return djsJpTotleService.djsjptotalList(sqlForm, pager, session);
	}

	//下拉框初始化
	@RequestMapping(value = "compSelectfind")
	@ResponseBody
	public Object compSelectfind(int compType, final HttpSession session) {
		return djsJpTotleService.compSelectfind(compType, session);
	}
}
