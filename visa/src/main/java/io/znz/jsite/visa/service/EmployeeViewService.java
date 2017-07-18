/**
 * EmployeeViewService.java
 * io.znz.jsite.visa.service
 * Copyright (c) 2017, 北京科技有限公司版权所有.
*/

package io.znz.jsite.visa.service;

import io.znz.jsite.base.NutzBaseService;
import io.znz.jsite.core.entity.companyjob.CompanyJobEntity;
import io.znz.jsite.core.util.Const;
import io.znz.jsite.util.security.Digests;
import io.znz.jsite.util.security.Encodes;
import io.znz.jsite.visa.entity.user.EmployeeEntity;
import io.znz.jsite.visa.entity.user.SysUserEntity;
import io.znz.jsite.visa.entity.userjobmap.UserJobMapEntity;
import io.znz.jsite.visa.enums.UserDeleteStatusEnum;
import io.znz.jsite.visa.enums.UserJobStatusEnum;
import io.znz.jsite.visa.enums.UserStatusEnum;
import io.znz.jsite.visa.enums.UserTypeEnum;
import io.znz.jsite.visa.forms.employeeform.EmployeeAddForm;
import io.znz.jsite.visa.forms.employeeform.EmployeeUpdateForm;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Sqls;
import org.nutz.dao.entity.Record;
import org.nutz.dao.sql.Sql;
import org.nutz.ioc.aop.Aop;
import org.springframework.stereotype.Service;

import com.google.common.collect.Maps;
import com.uxuexi.core.common.util.BeanUtil;
import com.uxuexi.core.common.util.Util;
import com.uxuexi.core.db.util.DbSqlUtil;
import com.uxuexi.core.web.chain.support.JsonResult;
import com.uxuexi.core.web.util.FormUtil;

/**
 * 员工管理业务层
 * @author   崔建斌
 * @Date	 2017年6月11日 	 
 */
@Service
public class EmployeeViewService extends NutzBaseService<SysUserEntity> {

	public static final int HASH_INTERATIONS = 1024;
	private static final int SALT_SIZE = 8; //盐长度
	private static final String INIT_PASSWORD = "000000"; //初始密码

	/**
	 * 页面加载时根据当前登录公司id查询出该公司下面的所有部门
	 * @param session
	 */
	public Object queryDept(final HttpSession session) {
		Map<String, Object> obj = Maps.newHashMap();
		//通过session获取公司的id
		CompanyJobEntity company = (CompanyJobEntity) session.getAttribute(Const.USER_COMPANY_KEY);
		long comId = company.getComId();//得到公司的id
		Sql sql = Sqls.create(sqlManager.get("employee_query_deptname_list"));
		Cnd cnd = Cnd.NEW();
		cnd.and("d.comId", "=", comId);
		cnd.and("d.deptName", "!=", "公司管理部");
		cnd.and("d.deptName", "!=", "");
		List<Record> queryList = dbDao.query(sql, cnd, null);
		obj.put("queryList", queryList);
		return obj;
	}

	/**
	 * 添加员工操作
	 * @param addForm
	 */
	@Aop("txDb")
	public Object addUserData(EmployeeAddForm addForm, long jobId, final HttpSession session) {
		//通过session获取公司的id
		CompanyJobEntity company = (CompanyJobEntity) session.getAttribute(Const.USER_COMPANY_KEY);
		long comId = company.getComId();//得到公司的id
		//查询出如果本公司下的某个员工离职了，再此入职让他的状态改为入职状态即可
		Sql sqlUser = Sqls.create(sqlManager.get("employee_update_old_user"));
		Cnd cnd = Cnd.NEW();
		cnd.and("cj.comId", "=", comId);
		cnd.and("e.status", "=", UserJobStatusEnum.QUIT.intKey());//离职
		cnd.and("ej.status", "=", UserJobStatusEnum.QUIT.intKey());//离职
		cnd.and("e.telephone", "=", addForm.getTelephone());//用户名
		List<Record> before = dbDao.query(sqlUser, cnd, null);
		Long userId = null;
		for (Record record : before) {
			if (!Util.isEmpty(before)) {
				userId = (long) record.getInt("id");
			}
		}
		if (Util.isEmpty(before)) {
			//添加用户数据
			addForm.setCreateTime(new Date());
			addForm.setStatus(UserJobStatusEnum.JOB.intKey());//在职
			addForm.setUserType(UserTypeEnum.PERSONNEL.intKey());//工作人员身份
			addForm.setDisableUserStatus(UserStatusEnum.VALID.intKey());//激活
			addForm.setComId(comId);
			//初始密码
			byte[] salt = Digests.generateSalt(SALT_SIZE);
			addForm.setSalt(Encodes.encodeHex(salt));
			byte[] password = INIT_PASSWORD.getBytes();
			byte[] hashPassword = Digests.sha1(password, salt, HASH_INTERATIONS);
			addForm.setPassword(Encodes.encodeHex(hashPassword));
			EmployeeEntity userdto = FormUtil.add(dbDao, addForm, EmployeeEntity.class);
			Integer userIds = userdto.getId();//得到用户id
			//根据公司id和职位id查询出公司职位表的id
			CompanyJobEntity comJob = dbDao.fetch(CompanyJobEntity.class,
					Cnd.where("comId", "=", comId).and("jobId", "=", jobId));
			Long comJobId = comJob.getId();//得到公司职位id
			//往用户就职表中填入数据
			Sql sql = Sqls.create(sqlManager.get("employee_add_emp_job_data"));
			sql.params().set("userId", userId);
			sql.params().set("statusId", UserJobStatusEnum.JOB.intKey());//在职
			sql.params().set("comJobId", comJobId);
			UserJobMapEntity newUser = DbSqlUtil.fetchEntity(dbDao, UserJobMapEntity.class, sql);
			if (Util.isEmpty(newUser)) {
				newUser = new UserJobMapEntity();
				newUser.setEmpId(userIds);
				newUser.setComJobId(comJobId);
				newUser.setStatus(UserJobStatusEnum.JOB.intKey());//在职
				newUser.setHireDate(new Date());
				newUser = dbDao.insert(newUser);
			}
		}
		//若此员工在本公司离职，但是又想入职，查询出他的信息之后更新此员工的状态即可
		if (!Util.isEmpty(before)) {
			dbDao.update(UserJobMapEntity.class, Chain.make("status", UserJobStatusEnum.JOB.intKey()),
					Cnd.where("empId", "=", userId));
			dbDao.update(EmployeeEntity.class, Chain.make("status", UserJobStatusEnum.JOB.intKey()),
					Cnd.where("id", "=", userId));
		}
		return JsonResult.success("添加成功!");
	}

	/**
	 * 回显用户信息数据
	 * @param cid 
	 */
	public Object updateDate(long uid, final HttpSession session) {
		Map<String, Object> obj = Maps.newHashMap();
		Sql sqluser = Sqls.create(sqlManager.get("employee_update_data"));
		sqluser.params().set("userId", uid);
		Record one = dbDao.fetch(sqluser);
		//通过session获取公司的id
		CompanyJobEntity company = (CompanyJobEntity) session.getAttribute(Const.USER_COMPANY_KEY);
		long comId = company.getComId();//得到公司的id
		Sql sql = Sqls.create(sqlManager.get("employee_select_dept_list"));
		Cnd cnd = Cnd.NEW();
		cnd.and("d.comId", "=", comId);
		cnd.and("d.deptName", "!=", "公司管理部");
		List<Record> userDeptList = dbDao.query(sql, cnd, null);
		obj.put("one", one);//用户信息
		obj.put("userDeptList", userDeptList);//部门职位信息
		return obj;
	}

	/**
	 * 编辑保存
	 * @param updateForm
	 * @return 
	 */
	public Object updateDataSave(EmployeeUpdateForm updateForm, final HttpSession session) {
		//通过session获取公司的id
		CompanyJobEntity company = (CompanyJobEntity) session.getAttribute(Const.USER_COMPANY_KEY);
		long comId = company.getComId();//得到公司的id
		Map<String, Object> obj = Maps.newHashMap();
		if (!Util.isEmpty(updateForm)) {
			updateForm.setStatus(UserJobStatusEnum.JOB.intKey());
			updateForm.setUpdateTime(new Date());
		}
		EmployeeEntity user = new EmployeeEntity();
		BeanUtil.copyProperties(updateForm, user);
		this.updateIgnoreNull(user);//更新用户表中的数据
		//根据公司id和职位id查询出公司职位表的id
		CompanyJobEntity comJob = dbDao.fetch(CompanyJobEntity.class,
				Cnd.where("comId", "=", comId).and("jobId", "=", updateForm.getJobId()));
		long comJobId = comJob.getId();//得到公司职位id
		//更新部门职位
		dbDao.update(CompanyJobEntity.class, Chain.make("jobId", updateForm.getJobId()), Cnd.where("id", "=", comJobId));
		dbDao.update(UserJobMapEntity.class, Chain.make("comJobId", comJobId),
				Cnd.where("empId", "=", updateForm.getId()));

		//查询出当前数据库中已存在的数据
		/*List<EmployeeEntity> before = dbDao.query(EmployeeEntity.class, Cnd.where("id", "=", updateForm.getId()), null);
		//欲更新为
		List<EmployeeEntity> after = Lists.newArrayList();
		if (!Util.isEmpty(before)) {
			for (EmployeeEntity t : before) {
				EmployeeEntity m = new EmployeeEntity();
				m.setFullName(updateForm.getFullName());
				m.setUserType(t.getUserType());
				m.setTelephone(updateForm.getTelephone());
				m.setQq(updateForm.getQq());
				m.setLandline(updateForm.getLandline());
				m.setEmail(updateForm.getEmail());
				m.setDepartment(updateForm.getDepartment());
				m.setJob(updateForm.getJob());
				m.setDisableUserStatus(updateForm.getDisableUserStatus());
				m.setCreateTime(t.getCreateTime());
				m.setUpdateTime(new Date());
				m.setStatus(t.getStatus());
				after.add(m);
			}
		}
		dbDao.updateRelations(before, after);*/
		return obj;
	}

	/**
	 * 根据用户id删除单条数据
	 * @param userId
	 */
	@Aop("txDb")
	public boolean deleteUserData(Integer userId) {
		if (!Util.isEmpty(userId)) {
			dbDao.update(EmployeeEntity.class, Chain.make("status", UserDeleteStatusEnum.YES.intKey()),
					Cnd.where("id", "=", userId));
		}
		return true;
	}

	/**
	 * 初始化密码操作
	 * @param userId
	 */
	public boolean initpassword(long userId) {
		if (!Util.isEmpty(userId)) {
			//EmployeeEntity fetch = dbDao.fetch(EmployeeEntity.class, userId);
			byte[] slt = Digests.generateSalt(SALT_SIZE);
			String salt = Encodes.encodeHex(slt);
			byte[] password = INIT_PASSWORD.getBytes();
			byte[] hashPassword = Digests.sha1(password, slt, HASH_INTERATIONS);
			String encodeHex = Encodes.encodeHex(hashPassword);
			dbDao.update(EmployeeEntity.class, Chain.make("password", encodeHex).add("salt", salt),
					Cnd.where("id", "=", userId));
		}
		return true;
	}
}
