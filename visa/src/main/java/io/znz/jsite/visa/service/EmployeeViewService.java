/**
 * EmployeeViewService.java
 * io.znz.jsite.visa.service
 * Copyright (c) 2017, 北京科技有限公司版权所有.
*/

package io.znz.jsite.visa.service;

import io.znz.jsite.base.NutzBaseService;
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

import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.ioc.aop.Aop;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.uxuexi.core.common.util.Util;
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
	 * 添加员工操作
	 * @param addForm
	 */
	public Object addUserData(EmployeeAddForm addForm) {
		//添加用户数据
		addForm.setCreateTime(new Date());
		addForm.setStatus(UserDeleteStatusEnum.NO.intKey());//未删除
		addForm.setUserType(UserTypeEnum.PERSONNEL.intKey());//工作人员身份
		addForm.setDisableUserStatus(UserStatusEnum.VALID.intKey());//激活
		//初始密码
		byte[] salt = Digests.generateSalt(SALT_SIZE);
		addForm.setSalt(Encodes.encodeHex(salt));
		byte[] password = INIT_PASSWORD.getBytes();
		byte[] hashPassword = Digests.sha1(password, salt, HASH_INTERATIONS);
		addForm.setPassword(Encodes.encodeHex(hashPassword));
		EmployeeEntity userdto = FormUtil.add(dbDao, addForm, EmployeeEntity.class);
		Integer userId = userdto.getId();//得到用户id
		//##########################添加用户就职表数据##########################//
		UserJobMapEntity userjob = new UserJobMapEntity();
		userjob.setEmpId(userId);
		userjob.setStatus(UserJobStatusEnum.JOB.intKey());//在职
		userjob.setHireDate(new Date());
		dbDao.insert(userjob);
		return userdto;
	}

	/**
	 * 回显客户信息数据
	 * @param cid 
	 */
	public Object updateDate(long uid) {
		EmployeeEntity one = dbDao.fetch(EmployeeEntity.class, uid);
		return one;
	}

	/**
	 * 编辑保存
	 * @param updateForm
	 * @return 
	 */
	public Object updateDataSave(EmployeeUpdateForm updateForm) {
		//查询出当前数据库中已存在的数据
		List<EmployeeEntity> before = dbDao.query(EmployeeEntity.class, Cnd.where("id", "=", updateForm.getId()), null);
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
		dbDao.updateRelations(before, after);
		return null;
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
