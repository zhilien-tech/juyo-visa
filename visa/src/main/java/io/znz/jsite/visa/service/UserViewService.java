/**
 * UserViewService.java
 * io.znz.jsite.visa.service
 * Copyright (c) 2017, 北京科技有限公司版权所有.
*/

package io.znz.jsite.visa.service;

import io.znz.jsite.base.NutzBaseService;
import io.znz.jsite.core.bean.User;
import io.znz.jsite.core.util.UserUtil;
import io.znz.jsite.exception.JSiteException;
import io.znz.jsite.visa.bean.entity.SysUserEntity;

import java.util.List;
import java.util.Map;

import org.apache.poi.ss.formula.functions.T;
import org.nutz.dao.Cnd;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;
import com.uxuexi.core.common.util.Util;

/**
 * TODO(这里用一句话描述这个类的作用)
 * @author   崔建斌
 * @Date	 2017年6月6日 	 
 */
@Service
@Transactional(readOnly = true)
public class UserViewService extends NutzBaseService<T> {

	/**
	 * 根据用户id查出个人信息
	 * @param userlist
	 */
	public Object userListData() {
		User user = UserUtil.getUser();
		String userId = null;
		if (!Util.isEmpty(user)) {
			userId = user.getId();//得到当前登录用户的id
		}
		if (user == null) {
			throw new JSiteException("请登录后再试!");
		}
		Map<String, Object> obj = Maps.newHashMap();
		List<SysUserEntity> userlist = dbDao.query(SysUserEntity.class, Cnd.where("id", "=", userId), null);
		obj.put("userlist", userlist);
		return obj;
	}
}
