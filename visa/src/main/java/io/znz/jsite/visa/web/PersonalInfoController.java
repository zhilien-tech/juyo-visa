/**
 * PersonalInfoController.java
 * io.znz.jsite.visa.web
 * Copyright (c) 2017, 北京科技有限公司版权所有.
*/

package io.znz.jsite.visa.web;

import io.znz.jsite.base.bean.PageFilter;
import io.znz.jsite.core.bean.User;
import io.znz.jsite.visa.service.UserViewService;

import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 个人信息控制类
 * @author   崔建斌
 * @Date	 2017年6月6日 	 
 */
@Controller
@RequestMapping("visa/personalinfo")
public class PersonalInfoController {

	@Autowired
	private UserViewService userViewService;

	/**
	 * 个人信息列表页展示
	 * @param filter
	 */
	@RequestMapping(value = "personallist")
	@ResponseBody
	private Object personallist(@RequestBody(required = false) PageFilter filter) {
		if (filter == null) {
			filter = new PageFilter();
		}
		Restrictions.conjunction(filter.getFilter(User.class));
		return userViewService.userListData();
	}
}
