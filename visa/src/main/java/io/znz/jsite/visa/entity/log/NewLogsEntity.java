/**
 * OtherNationalityEntity.java
 * io.znz.jsite.visa.entity.othernationality
 * Copyright (c) 2017, 北京科技有限公司版权所有.
*/

package io.znz.jsite.visa.entity.log;

import java.util.Date;

import lombok.Data;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

/**
 * 是否有其他国籍
 * @author   崔建斌
 * @Date	 2017年6月22日 	 
 */
@Data
@Table("visa_new_logs")
public class NewLogsEntity {
	@Id(auto = true)
	@Column
	@Comment("主键")
	private long id;
	@Column
	@Comment("日志内容")
	private String context;
	@Column
	@Comment("创建时间")
	private Date createTime;

}
