/**
 * LoginAuthorityService.java
 * io.znz.jsite.core.service.authority
 * Copyright (c) 2017, 北京科技有限公司版权所有.
*/

package io.znz.jsite.core.service.authority;

import io.znz.jsite.base.NutzBaseService;
import io.znz.jsite.core.entity.function.FunctionEntity;
import io.znz.jsite.core.enums.UserJobStatusEnum;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.nutz.dao.Sqls;
import org.nutz.dao.sql.Sql;
import org.springframework.stereotype.Service;

import com.uxuexi.core.db.util.DbSqlUtil;

/**
 * 用户登录系统配置权限
 * @author   崔建斌
 * @Date	 2017年7月17日 	 
 */
@Service
public class LoginAuthorityService extends NutzBaseService {

	/**
	 * 普通工作人员登录时
	 * @param userId
	 */
	public List<FunctionEntity> employeeLoginFunction(long userId, HttpServletRequest request) {
		Sql sql = Sqls.create(sqlManager.get("loginauthority_user_function_all"));
		sql.params().set("userId", userId);
		sql.params().set("status", UserJobStatusEnum.JOB.intKey());//在职
		List<FunctionEntity> empfunlist = DbSqlUtil.query(dbDao, FunctionEntity.class, sql, null);
		//request.getSession().setAttribute(Const.USER_JOB_FUNCTION, empfunlist);
		return empfunlist;
	}

	/**
	 * 超级管理员登录时
	 */
	public List<FunctionEntity> superAdministratorFunction() {
		List<FunctionEntity> query = dbDao.query(FunctionEntity.class, null, null);
		return query;
	}
}
