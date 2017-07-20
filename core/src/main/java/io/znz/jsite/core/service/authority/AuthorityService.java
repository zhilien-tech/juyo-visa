/**
 * AuthorityService.java
 * io.znz.jsite.visa.service
 * Copyright (c) 2017, 北京科技有限公司版权所有.
*/

package io.znz.jsite.core.service.authority;

import io.znz.jsite.base.NutzBaseService;
import io.znz.jsite.core.entity.function.FunctionEntity;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.nutz.dao.Cnd;
import org.nutz.dao.Sqls;
import org.nutz.dao.sql.Sql;
import org.springframework.stereotype.Service;

import com.uxuexi.core.common.util.Util;
import com.uxuexi.core.db.util.DbSqlUtil;

/**
 * 权限管理
 * @author   崔建斌
 * @Date	 2017年7月10日 	 
 */
@Service("authorityViewService")
public class AuthorityService extends NutzBaseService {
	/**
	 * 查询当前登录公司拥有的所有功能
	 * @param request
	 */
	public List<FunctionEntity> getCompanyFunctions(Long comId) {
		Sql comFunSql = Sqls.fetchEntity(sqlManager.get("authority_company_function"));
		comFunSql.params().set("comId", comId);
		List<FunctionEntity> allModule = DbSqlUtil.query(dbDao, FunctionEntity.class, comFunSql);
		//排序functionMap
		Collections.sort(allModule, new Comparator<FunctionEntity>() {
			@Override
			public int compare(FunctionEntity tf1, FunctionEntity tf2) {
				if (!Util.isEmpty(tf1.getSort()) && !Util.isEmpty(tf2.getSort())) {
					if (tf1.getSort() > tf2.getSort()) {
						return 1;
					} else if (tf1.getSort() == tf2.getSort()) {
						return 0;
					} else if (tf1.getSort() < tf2.getSort()) {
						return -1;
					}
				}
				return 0;
			}
		});
		return allModule;
	}

	/**
	 * 根据请求路径查询功能
	 * @param requestPath
	 * @return
	 */
	public FunctionEntity findFuctionByRequestPath(String requestPath) {
		return dbDao.fetch(FunctionEntity.class, Cnd.where("url", "LIKE", "%" + requestPath + "%"));
	}
}
