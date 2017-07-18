/**
 * Job.java
 * io.znz.jsite.visa.entity.job
 * Copyright (c) 2017, 北京科技有限公司版权所有.
*/

package io.znz.jsite.core.entity.job;

import java.util.Date;

import lombok.Data;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

/**
 * 职位表
 * @author   崔建斌
 * @Date	 2017年7月10日 	 
 */
@Data
@Table("visa_new_job")
public class JobEntity {
	@Column
	@Id(auto = true)
	@Comment("主键")
	private long id;
	@Column
	@Comment("部门id")
	private long deptId;
	@Column
	@Comment("职位名称")
	private String jobName;
	@Column
	@Comment("创建时间")
	private Date createTime;
	@Column
	@Comment("更新时间")
	private Date updateTime;
	@Column
	@Comment("备注")
	private String remark;
}
