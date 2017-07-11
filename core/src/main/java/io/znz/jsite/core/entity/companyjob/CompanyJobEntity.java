/**
 * CompanyJob.java
 * io.znz.jsite.visa.entity.companyjob
 * Copyright (c) 2017, 北京科技有限公司版权所有.
*/

package io.znz.jsite.core.entity.companyjob;

import lombok.Data;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

/**
 * 公司职位表
 * @author   崔建斌
 * @Date	 2017年7月10日 	 
 */
@Data
@Table("visa_new_company_job")
public class CompanyJobEntity {
	@Column
	@Id(auto = true)
	@Comment("主键")
	private long id;
	@Column
	@Comment("职位id")
	private long jobId;
	@Column
	@Comment("公司id")
	private long comId;
}
