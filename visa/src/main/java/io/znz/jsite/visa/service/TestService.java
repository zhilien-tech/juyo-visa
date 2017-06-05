package io.znz.jsite.visa.service;

import io.znz.jsite.base.NutzBaseService;
import io.znz.jsite.visa.entity.TestEntity;

import org.nutz.dao.Sqls;
import org.nutz.dao.sql.Sql;
import org.springframework.stereotype.Service;

import com.uxuexi.core.db.util.DbSqlUtil;

/**
 * Created by Chaly on 2017/3/7.
 */
@Service
public class TestService extends NutzBaseService<TestEntity> {

	public Object test() {
		Sql sql = Sqls.create(sqlManager.get("template_select"));
		return DbSqlUtil.query(dbDao, TestEntity.class, sql);
	}

}
