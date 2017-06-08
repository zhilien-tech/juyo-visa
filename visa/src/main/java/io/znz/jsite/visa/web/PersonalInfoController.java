/**
 * PersonalInfoController.java
 * io.znz.jsite.visa.web
 * Copyright (c) 2017, 北京科技有限公司版权所有.
*/

package io.znz.jsite.visa.web;

import io.znz.jsite.base.BaseController;
import io.znz.jsite.visa.service.UserViewService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;

/**
 * 个人信息控制类
 * @author   崔建斌
 * @Date	 2017年6月6日 	 
 */
@Controller
@RequestMapping("visa/personalinfo")
public class PersonalInfoController extends BaseController {

	@Autowired
	private UserViewService userViewService;

	/**
	 * 个人信息列表页展示
	 * @param filter
	 */
	@RequestMapping(value = "personallist")
	@ResponseBody
	private Object personallist() {
		return userViewService.userListData();
	}

	@RequestMapping(value = "show", method = RequestMethod.GET)
	@ResponseBody
	public Object show(long cid) {
		//Customer customer = userViewService
		return JSON.parseObject("");
	}
}
