/**
 * CompanyFunctionJobEntity.java
 * io.znz.jsite.visa.entity.comfunjob
 * Copyright (c) 2017, 北京科技有限公司版权所有.
*/

package io.znz.jsite.visa.entity.comfunjob;

import lombok.Data;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

/**
 * 公司职位功能关系表
 * @author   崔建斌
 * @Date	 2017年7月10日 	 
 */
@Data
@Table("visa_new_com_fun_job_map")
public class CompanyFunctionJobEntity {
	@Column
	@Id(auto = true)
	@Comment("主键")
	private long id;
	@Column
	@Comment("职位id")
	private long jobId;
	@Column
	@Comment("公司功能id")
	private long comFunId;
}
