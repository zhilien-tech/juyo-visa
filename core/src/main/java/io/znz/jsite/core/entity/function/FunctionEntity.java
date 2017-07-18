/**
 * FunctionEntity.java
 * io.znz.jsite.visa.entity.function
 * Copyright (c) 2017, 北京科技有限公司版权所有.
*/

package io.znz.jsite.core.entity.function;

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
	private long id;
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

	/**在树形节点中是否选中*/
	private String checked = "false";

	/**
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FunctionEntity other = (FunctionEntity) obj;
		if (id != other.id)
			return false;
		return true;
	}

	/**
	 * (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	/**
	 * (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	@Override
	public FunctionEntity clone() throws CloneNotSupportedException {
		FunctionEntity clone = null;
		try {
			clone = (FunctionEntity) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException(e); // won't happen 
		}
		return clone;
	}

	public int compareTo(FunctionEntity o) {
		if (null == o || null == o.getSort()) {
			return -1;
		}
		return this.getSort().compareTo(o.getSort());
	}

}
