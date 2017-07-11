/**
 * CompanyFunction.java
 * io.znz.jsite.visa.entity.comfunmap
 * Copyright (c) 2017, 北京科技有限公司版权所有.
*/

package io.znz.jsite.visa.entity.comfunmap;

import lombok.Data;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

/**
 * 公司功能关系表
 * @author   崔建斌
 * @Date	 2017年7月10日 	 
 */
@Data
@Table("visa_new_company_function_map")
public class CompanyFunctionEntity {
	@Column
	@Id(auto = true)
	@Comment("主键")
	private long id;
	@Column
	@Comment("功能id")
	private long funId;
	@Column
	@Comment("公司id")
	private long comId;
}
