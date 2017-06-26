/**
 * ModifyPasswordController.java
 * io.znz.jsite.visa.web
 * Copyright (c) 2017, 北京科技有限公司版权所有.
*/

package io.znz.jsite.visa.web;

import io.znz.jsite.base.bean.ResultObject;
import io.znz.jsite.core.entity.EmployeeEntity;
import io.znz.jsite.exception.JSiteException;
import io.znz.jsite.util.security.Digests;
import io.znz.jsite.util.security.Encodes;
import io.znz.jsite.visa.forms.employeeform.EmployeeUpdateForm;

import javax.servlet.http.HttpServletRequest;

import org.nutz.dao.Dao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.uxuexi.core.common.util.Util;
import com.uxuexi.core.db.dao.IDbDao;

/**
 * 修改密码
 * @author   崔建斌
 * @Date	 2017年6月26日 	 
 */
@Controller
@RequestMapping("visa/modifypassword")
public class ModifyPasswordController {

	public static final int HASH_INTERATIONS = 1024;
	private static final int SALT_SIZE = 8; //盐长度

	@Autowired
	protected IDbDao dbDao;

	@Autowired
	protected Dao nutDao;

	/**
	 * 修改密码
	 * @param updateForm
	 */
	@RequestMapping(value = "updatepassSave", method = RequestMethod.POST)
	@ResponseBody
	public Object updatepassSave(EmployeeUpdateForm updateForm, HttpServletRequest request) {
		//从session中取出当前登录用户信息
		EmployeeEntity user = (EmployeeEntity) request.getSession().getAttribute("fetch");
		long userId = 0;
		if (user == null) {
			throw new JSiteException("请登录后再试!");
		}
		if (!Util.isEmpty(user)) {
			userId = user.getId();
		}
		EmployeeEntity one = dbDao.fetch(EmployeeEntity.class, userId);
		String password = null;
		if (!Util.isEmpty(one)) {
			password = one.getPassword();//得到数据库中的原密码 d01e28e0af7983d3123e18898f66294f9cf22da5
		}
		String oldpass = updateForm.getPassword();//得到页面传过来的原密码
		String newpass1 = updateForm.getNewpass();//得到页面传来的新密码
		String newpass2 = updateForm.getNewpass2();//得到页面再次传来的新密码

		byte[] salt = Encodes.decodeHex(one.getSalt());
		byte[] pwd1 = oldpass.getBytes();
		byte[] hashPassword = Digests.sha1(pwd1, salt, HASH_INTERATIONS);
		String pass1 = Encodes.encodeHex(hashPassword);//解析页面传来的原密码 d01e28e0af7983d3123e18898f66294f9cf22da5
		if (!Util.isEmpty(password)) {
			if (password.equals(pass1)) {//判断数据库中的原密码和页面上传过来的原密码是否相等
				if (!Util.isEmpty(newpass1) && !Util.isEmpty(newpass2)) {
					if (newpass1.equals(newpass2)) {
						byte[] newsalt = Digests.generateSalt(SALT_SIZE);
						one.setSalt(Encodes.encodeHex(newsalt));
						byte[] newpass = newpass1.getBytes();
						byte[] hashPassword2 = Digests.sha1(newpass, newsalt, HASH_INTERATIONS);
						one.setPassword(Encodes.encodeHex(hashPassword2));//9b4b54aa67b6f495b0060b97078f64321f120959
						dbDao.update(one, null);
					}
				}
			}
		}
		return ResultObject.success("修改成功");
	}
}
