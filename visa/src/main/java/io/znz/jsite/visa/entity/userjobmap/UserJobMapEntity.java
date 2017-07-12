/**
 * UserJobMap.java
 * io.znz.jsite.visa.entity.userjobmap
 * Copyright (c) 2017, 北京科技有限公司版权所有.
*/

package io.znz.jsite.visa.entity.userjobmap;

import java.util.Date;

import lombok.Data;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

/**
 * 用户就职表
 * @author   崔建斌
 * @Date	 2017年7月10日 	 
 */
@Data
@Table("visa_new_emp_job")
public class UserJobMapEntity {
	@Column
	@Id(auto = true)
	@Comment("主键")
	private long id;
	@Column
	@Comment("用户id")
	private long empId;
	@Column
	@Comment("公司职位id")
	private long comJobId;
	@Column
	@Comment("状态")
	private Integer status;
	@Column
	@Comment("入职时间")
	private Date hireDate;
	@Column
	@Comment("离职时间")
	private Date leaveDate;
	@Column
	@Comment("备注")
	private String remark;
}
