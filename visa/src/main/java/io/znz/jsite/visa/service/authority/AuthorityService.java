/**
 * AuthorityService.java
 * io.znz.jsite.visa.service
 * Copyright (c) 2017, 北京科技有限公司版权所有.
*/

package io.znz.jsite.visa.service.authority;

import io.znz.jsite.base.NutzBaseService;
import io.znz.jsite.core.entity.EmployeeEntity;
import io.znz.jsite.core.util.Const;
import io.znz.jsite.visa.dto.DeptJobForm;
import io.znz.jsite.visa.dto.JobDto;
import io.znz.jsite.visa.entity.comfunjob.CompanyFunctionJobEntity;
import io.znz.jsite.visa.entity.comfunmap.CompanyFunctionEntity;
import io.znz.jsite.visa.entity.company.CompanyEntity;
import io.znz.jsite.visa.entity.companyjob.CompanyJobEntity;
import io.znz.jsite.visa.entity.department.DepartmentEntity;
import io.znz.jsite.visa.entity.function.FunctionEntity;
import io.znz.jsite.visa.entity.job.JobEntity;
import io.znz.jsite.visa.enums.UserDeleteStatusEnum;
import io.znz.jsite.visa.forms.authority.AuthoritySqlForm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Sqls;
import org.nutz.dao.pager.Pager;
import org.nutz.dao.sql.Sql;
import org.nutz.ioc.aop.Aop;
import org.nutz.json.Json;
import org.springframework.stereotype.Service;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.uxuexi.core.common.util.BeanUtil;
import com.uxuexi.core.common.util.Util;
import com.uxuexi.core.db.util.DbSqlUtil;
import com.uxuexi.core.web.chain.support.JsonResult;

/**
 * 权限管理
 * @author   崔建斌
 * @Date	 2017年7月10日 	 
 */
@SuppressWarnings("rawtypes")
@Service
public class AuthorityService extends NutzBaseService {
	/**
	 * 公司列表展示
	 * @param sqlForm
	 */
	public Object authoritylist(AuthoritySqlForm sqlForm, Pager pager) {
		pager = new Pager();
		pager.setPageNumber(sqlForm.getPageNumber());
		pager.setPageSize(sqlForm.getPageSize());
		return this.listPage(sqlForm, pager);
	}

	//保存数据
	@Aop("txDb")
	public Map<String, String> saveDeptJobData(DeptJobForm addForm, final HttpSession session) {
		//通过session获取公司的id
		CompanyEntity company = (CompanyEntity) session.getAttribute(Const.USER_COMPANY_KEY);
		long comId = company.getId();//得到公司的id
		String jobJson = addForm.getJobJson();

		//1,先添加部门，拿到部门id
		Sql sql1 = Sqls.create(sqlManager.get("authority_dept_data"));
		sql1.params().set("deptName", addForm.getDeptName());
		sql1.params().set("comId", comId);
		DepartmentEntity newDept = DbSqlUtil.fetchEntity(dbDao, DepartmentEntity.class, sql1);
		if (Util.isEmpty(newDept)) {
			newDept = new DepartmentEntity();
			newDept.setComId(comId);
			newDept.setDeptName(addForm.getDeptName());
			newDept = dbDao.insert(newDept);
		}
		//获取到部门id
		Long deptId = newDept.getId();
		JobDto[] jobJsonArray = Json.fromJsonAsArray(JobDto.class, jobJson);

		if (!Util.isEmpty(jobJsonArray)) {
			for (JobDto jobDto : jobJsonArray) {
				saveOrUpdateSingleJob(deptId, null, comId, jobDto.getJobName(), jobDto.getFunctionIds());
			}
		}
		return JsonResult.success("添加成功!");
	}

	private void saveOrUpdateSingleJob(Long deptId, Long jobId, Long companyId, String jobName, String functionIds) {
		//1，判断操作类型，执行职位新增或者修改
		if (Util.isEmpty(jobId) || jobId <= 0) {
			//添加操作
			Sql sql = Sqls.create(sqlManager.get("authority_company_job"));
			sql.params().set("comId", companyId);
			sql.params().set("jobName", jobName);
			JobEntity newJob = DbSqlUtil.fetchEntity(dbDao, JobEntity.class, sql);

			if (Util.isEmpty(newJob)) {
				//职位不存在则新增
				newJob = new JobEntity();
				newJob.setJobName(jobName);
				newJob.setDeptId(deptId);
				newJob = dbDao.insert(newJob);
				jobId = newJob.getId();//得到职位id
				//该公司添加新的职位
				CompanyJobEntity newComJob = new CompanyJobEntity();
				newComJob.setComId(companyId);
				newComJob.setJobId(jobId);
				dbDao.insert(newComJob);
			} else {
				//如果职位名称已存在
				throw new IllegalArgumentException("该公司此职位已存在,无法添加,职位名称:" + jobName);
			}

		} else {
			//更新操作
			JobEntity newJob = dbDao.fetch(JobEntity.class, Cnd.where("id", "=", jobId));
			if (Util.isEmpty(newJob)) {
				throw new IllegalArgumentException("欲更新的职位不存在,jobId:" + jobId);
			}

			//判断该公司是否存在同名的其他职位
			Sql sql = Sqls.create(sqlManager.get("authority_company_job_update"));
			sql.params().set("comId", companyId);
			sql.params().set("jobName", jobName);
			sql.params().set("jobId", jobId);
			JobEntity existsJob = DbSqlUtil.fetchEntity(dbDao, JobEntity.class, sql);

			if (!Util.isEmpty(existsJob)) {
				//如果职位名称已存在
				throw new IllegalArgumentException("该公司此职位已存在,无法修改,职位名称:" + jobName);
			}

			dbDao.update(JobEntity.class, Chain.make("jobName", jobName), Cnd.where("id", "=", newJob.getId()));
		}

		//2，截取功能模块id，根据功能id和公司id查询出公司功能id，用公司功能id和职位id往公司功能职位表添加数据
		if (!Util.isEmpty(functionIds)) {
			Iterable<String> funcIdIter = Splitter.on(",").omitEmptyStrings().split(functionIds);
			String funcIds = Joiner.on(",").join(funcIdIter);

			List<CompanyFunctionJobEntity> before = dbDao.query(CompanyFunctionJobEntity.class,
					Cnd.where("jobId", "=", jobId), null);
			List<CompanyFunctionEntity> comFucs = dbDao.query(CompanyFunctionEntity.class,
					Cnd.where("comId", "=", companyId).and("funId", "IN", funcIds), null);
			//欲更新为
			List<CompanyFunctionJobEntity> after = Lists.newArrayList();
			for (CompanyFunctionEntity cf : comFucs) {
				CompanyFunctionJobEntity newComFun = new CompanyFunctionJobEntity();
				newComFun.setJobId(jobId);
				newComFun.setComFunId(Long.valueOf(cf.getId()));
				after.add(newComFun);
			}
			dbDao.updateRelations(before, after);
		}
	}

	public Object queryComAllFunction(long jobId, HttpServletRequest request) {
		Map<String, Object> obj = Maps.newHashMap();
		//从session中取出当前登录公司信息
		CompanyEntity company = (CompanyEntity) request.getSession().getAttribute(Const.USER_COMPANY_KEY);
		long comId = company.getId();//得到当前登录公司id
		List<FunctionEntity> allModule = getCompanyFunctions(request);
		DeptJobForm deptJobForm = new DeptJobForm();
		if (jobId > 0) {
			allModule = getJobFunctionInfos(jobId, allModule);
			Sql sql = Sqls.create(sqlManager.get("authority_dept_job_list"));
			sql.params().set("jobId", jobId);
			deptJobForm = DbSqlUtil.fetchEntity(dbDao, DeptJobForm.class, sql);
		}
		obj.put("list", allModule);
		obj.put("dept", deptJobForm);
		return "redirect:/" + obj;
	}

	private List<FunctionEntity> getJobFunctionInfos(long jobId, final List<FunctionEntity> allModule) {
		Sql comFuncSql = Sqls.create(sqlManager.get("authority_job_function"));
		comFuncSql.params().set("jobId", jobId);
		//每一个职位的功能不一样，每次必须是一个新的功能集合
		List<FunctionEntity> newFunctions = new ArrayList<FunctionEntity>();
		for (FunctionEntity srcFunc : allModule) {
			FunctionEntity dest = new FunctionEntity();
			BeanUtil.copyProperties(srcFunc, dest);
			newFunctions.add(dest);
		}
		//根据职位查询关系
		List<FunctionEntity> jobFuncs = DbSqlUtil.query(dbDao, FunctionEntity.class, comFuncSql);
		//如果职位有功能
		if (!Util.isEmpty(jobFuncs)) {
			for (FunctionEntity f : newFunctions) {
				if (jobFuncs.contains(f)) {
					f.setChecked("true");
				}
			}
		}
		return newFunctions;
	}

	/**
	 * 查询当前登录公司拥有的所有功能
	 * @param request
	 */
	public List<FunctionEntity> getCompanyFunctions(HttpServletRequest request) {
		//从session中获取当前登录公司数据
		CompanyEntity company = (CompanyEntity) request.getSession().getAttribute(Const.USER_COMPANY_KEY);
		long comId = company.getId();//得到公司的id
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
	 * 添加公司操作
	 * @param addForm
	 */
	public Object addcompany(CompanyEntity addForm, EmployeeEntity empForm) {
		return JsonResult.success("添加成功");
	}

	/**
	 * 回显公司信息
	 * @param comId
	 */
	public Object updatecompany(long comId) {
		return dbDao.fetch(CompanyEntity.class, comId);
	}

	/**
	 * 编辑保存公司信息
	 * @param updateForm
	 */
	public Object updateCompanySave(CompanyEntity updateForm) {
		return null;
	}

	/**
	 * 根据用户id删除单条数据
	 * @param userId
	 */
	@Aop("txDb")
	public boolean deleteCompany(Integer comId) {
		if (!Util.isEmpty(comId)) {
			dbDao.update(CompanyEntity.class, Chain.make("deletestatus", UserDeleteStatusEnum.YES.intKey()),
					Cnd.where("id", "=", comId));
			return true;
		}
		return false;
	}
}
