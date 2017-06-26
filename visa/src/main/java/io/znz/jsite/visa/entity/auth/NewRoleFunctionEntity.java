package io.znz.jsite.visa.entity.auth;

import org.nutz.dao.entity.annotation.*;
import lombok.Data;

import java.io.Serializable;


@Data
@Table("visa_new_role_function")
public class NewRoleFunctionEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id(auto = true)
	private Integer id;
	
	@Column
    @Comment("角色id")
	private Integer roleid;
	
	@Column
    @Comment("功能表id")
	private Integer functionid;
	

}