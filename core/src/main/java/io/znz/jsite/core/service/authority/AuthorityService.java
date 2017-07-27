/**
 * AuthorityService.java
 * io.znz.jsite.visa.service
 * Copyright (c) 2017, 北京科技有限公司版权所有.
*/

package io.znz.jsite.core.service.authority;

import io.znz.jsite.base.NutzBaseService;
import io.znz.jsite.core.entity.function.FunctionEntity;
import io.znz.jsite.core.util.Const;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.nutz.dao.Cnd;
import org.nutz.dao.Sqls;
import org.nutz.dao.sql.Sql;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
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

	/**
	 * 登录时查询出用户的功能
	 * @param request
	 * @param functions
	 */
	public Object loginfunctions(HttpServletRequest request, String logintype, String orderId) {
		List<FunctionEntity> functions = Lists.newArrayList();
		if ("5".equals(logintype)) {
			//我的签证
			FunctionEntity usaf1 = new FunctionEntity();
			usaf1.setId(7);
			usaf1.setParentId(0);
			usaf1.setFunName("我的签证");
			usaf1.setLevel(1);
			usaf1.setCreateTime(new Date());
			usaf1.setRemark("我的签证");
			usaf1.setSort(7);
			usaf1.setPortrait("fa fa-arrows-h");
			functions.add(usaf1);
			//办理中签证
			FunctionEntity usaf2 = new FunctionEntity();
			usaf2.setId(8);
			usaf2.setParentId(7);
			usaf2.setFunName("办理中签证");
			usaf2.setUrl("myvisa/transactVisa/visaNationList.html?logintype=5&orderId=" + orderId);
			usaf2.setLevel(2);
			usaf2.setCreateTime(new Date());
			usaf2.setRemark("办理中签证");
			usaf2.setSort(8);
			functions.add(usaf2);
			//			//我的资料
			//			FunctionEntity usaf3 = new FunctionEntity();
			//			usaf3.setId(9);
			//			usaf3.setParentId(0);
			//			usaf3.setFunName("我的资料");
			//			usaf3.setLevel(1);
			//			usaf3.setCreateTime(new Date());
			//			usaf3.setRemark("我的资料");
			//			usaf3.setPortrait("fa fa-list-alt");
			//			usaf3.setSort(9);
			//			functions.add(usaf3);
		} else {

			HttpSession session = request.getSession();
			//功能session
			functions = (List<FunctionEntity>) session.getAttribute(Const.AUTHS_KEY);
		}
		return functions;
	}
}
