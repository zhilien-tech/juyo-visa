/**
 * FunctionEntity.java
 * io.znz.jsite.visa.entity.function
 * Copyright (c) 2017, 北京科技有限公司版权所有.
*/

package io.znz.jsite.visa.entity.function;

import java.util.Date;

import lombok.Data;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

/**
 * 功能管理
 * @author   崔建斌
 * @Date	 2017年7月6日 	 
 */
@Data
@Table("visa_new_function")
public class FunctionEntity {
	@Column
	@Id(auto = true)
	@Comment("主键")
	private Integer id;
	@Column
	@Comment("上级功能id")
	private Integer parentId;
	@Column
	@Comment("功能名称")
	private String funName;
	@Column
	@Comment("访问地止")
	private String url;
	@Column
	@Comment("功能等级")
	private Integer level;
	@Column
	@Comment("创建时间")
	private Date createTime;
	@Column
	@Comment("更新时间")
	private Date updateTime;
	@Column
	@Comment("备注")
	private String remark;
	@Column
	@Comment("序号")
	private Integer sort;
	@Column
	@Comment("菜单栏图标样式")
	private String portrait;

}
