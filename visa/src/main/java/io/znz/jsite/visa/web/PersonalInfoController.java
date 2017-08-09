/**
 * PersonalInfoController.java
 * io.znz.jsite.visa.web
 * Copyright (c) 2017, 北京科技有限公司版权所有.
*/

package io.znz.jsite.visa.web;

import io.znz.jsite.base.BaseController;
import io.znz.jsite.visa.forms.personalInfo.PersonalInfoSqlForm;
import io.znz.jsite.visa.forms.personalInfo.PersonalInfoUpdateForm;
import io.znz.jsite.visa.service.UserViewService;
import io.znz.jsite.visa.service.personalInfo.PersonalInfoService;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

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
	@Autowired
	private PersonalInfoService personalInfoService;

	/**
	 * 个人信息列表页展示
	 * @param filter
	 */
	@RequestMapping(value = "personallist")
	@ResponseBody
	private Object personallist(PersonalInfoSqlForm form, final HttpSession session) {
		return personalInfoService.personallist(form, session);
	}

	/**
	 * 执行'修改操作'
	 */
	@RequestMapping(value = "personalUpdate", method = RequestMethod.POST)
	@ResponseBody
	public Object updatePersonal(PersonalInfoUpdateForm updateForm) {
		return personalInfoService.updatePersonal(updateForm);
	}

}
