/**
 * FunctionService.java
 * io.znz.jsite.visa.service.function
 * Copyright (c) 2017, 北京科技有限公司版权所有.
*/

package io.znz.jsite.visa.service.function;

import io.znz.jsite.base.NutzBaseService;
import io.znz.jsite.visa.entity.comfunjob.CompanyFunctionJobEntity;
import io.znz.jsite.visa.entity.comfunmap.CompanyFunctionEntity;
import io.znz.jsite.visa.entity.function.FunctionEntity;
import io.znz.jsite.visa.forms.function.FunctionSqlForm;

import java.util.Date;
import java.util.List;

import org.nutz.dao.Cnd;
import org.nutz.dao.pager.Pager;
import org.springframework.stereotype.Service;

import com.uxuexi.core.common.util.Util;
import com.uxuexi.core.web.chain.support.JsonResult;

/**
 * 功能管理
 * @author   崔建斌
 * @Date	 2017年7月6日 	 
 */
@Service
public class FunctionService extends NutzBaseService<FunctionEntity> {
	/**
	 * 功能列表展示
	 * @param sqlForm
	 */
	public Object functionlist(FunctionSqlForm sqlForm, Pager pager) {
		pager = new Pager();
		pager.setPageNumber(sqlForm.getPageNumber());
		pager.setPageSize(sqlForm.getPageSize());
		return this.listPage(sqlForm, pager);
	}

	/**
	 * 功能管理添加操作
	 * @param addForm
	 */
	public Object addfunction(FunctionEntity addForm) {
		//添加功能表数据
		addForm.setCreateTime(new Date());
		Integer parentId = addForm.getParentId();
		if (Util.isEmpty(parentId)) {
			addForm.setParentId(0);
		}
		FunctionEntity addFunction = dbDao.insert(addForm);
		Long funId = null;
		if (!Util.isEmpty(addFunction)) {
			funId = addFunction.getId();//得到功能id
		}
		//填写公司功能表数据
		CompanyFunctionEntity comFun = new CompanyFunctionEntity();
		comFun.setFunId(funId);
		CompanyFunctionEntity addComFun = dbDao.insert(comFun);
		Long comFunId = null;
		if (!Util.isEmpty(addComFun)) {
			comFunId = addComFun.getId();//得到公司功能id
		}
		//填写公司职位功能表数据
		CompanyFunctionJobEntity comFunJob = new CompanyFunctionJobEntity();
		comFunJob.setComFunId(comFunId);
		dbDao.insert(comFunJob);
		return JsonResult.success("添加成功");
	}

	/**
	 * 回显功能管理数据
	 * @param funId
	 */
	public Object updatefunction(long funId) {
		return dbDao.fetch(FunctionEntity.class, funId);
	}

	/**
	 * 编辑保存功能数据
	 * @param updateForm
	 */
	public Object updateFunctionSave(FunctionEntity updateForm) {
		//修改功能信息
		FunctionEntity function = this.fetch(updateForm.getId());
		updateForm.setUpdateTime(new Date());
		return nutDao.updateIgnoreNull(updateForm);
	}

	/**
	 * 查询上级功能
	 */
	public Object selectparentname() {
		List<FunctionEntity> query = dbDao.query(FunctionEntity.class, Cnd.where("parentId", "=", 0), null);
		return query;

	}
}
