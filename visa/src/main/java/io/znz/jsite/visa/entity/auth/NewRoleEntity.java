package io.znz.jsite.visa.entity.auth;

import org.nutz.dao.entity.annotation.*;
import lombok.Data;
import java.util.Date;

import java.io.Serializable;


@Data
@Table("visa_new_role")
public class NewRoleEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id(auto = true)
	private Integer id;
	
	@Column
    @Comment("角色名称")
	private String roleName;
	
	@Column
    @Comment("创建时间")
	private Date createTime;
	
	@Column
    @Comment("更新时间")
	private Date updateTime;
	
	@Column
    @Comment("状态")
	private Integer status;
	
	@Column
    @Comment("备注")
	private String remark;
	

}