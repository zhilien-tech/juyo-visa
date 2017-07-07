/**
 * CompanyEntity.java
 * io.znz.jsite.visa.entity.company
 * Copyright (c) 2017, 北京科技有限公司版权所有.
*/

package io.znz.jsite.visa.entity.company;

import java.util.Date;

import lombok.Data;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

/**
 * 公司
 * @author   崔建斌
 * @Date	 2017年7月5日 	 
 */
@Data
@Table("visa_new_company")
public class CompanyEntity {
	@Column
	@Id(auto = true)
	@Comment("主键")
	private Integer id;
	@Column
	@Comment("管理员账号id")
	private Integer adminId;
	@Column
	@Comment("管理员姓名")
	private String adminName;
	@Column
	@Comment("公司名称")
	private String comName;
	@Column
	@Comment("公司类型")
	private Integer comType;
	@Column
	@Comment("备注")
	private String remark;
	@Column
	@Comment("联系人")
	private String connect;
	@Column
	@Comment("联系电话")
	private String mobile;
	@Column
	@Comment("邮箱")
	private String email;
	@Column
	@Comment("座机号码")
	private String landLine;
	@Column
	@Comment("地址")
	private String address;
	@Column
	@Comment("营业执照")
	private String license;
	@Column
	@Comment("操作人")
	private Integer empId;
	@Column
	@Comment("创建时间")
	private Date createTime;
	@Column
	@Comment("修改时间")
	private Date updateTime;
	@Column
	@Comment("删除标识")
	private Integer deletestatus;
}
