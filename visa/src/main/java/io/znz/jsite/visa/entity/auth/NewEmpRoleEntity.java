package io.znz.jsite.visa.entity.auth;

import org.nutz.dao.entity.annotation.*;
import lombok.Data;

import java.io.Serializable;


@Data
@Table("visa_new_emp_role")
public class NewEmpRoleEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id(auto = true)
	private Integer id;
	
	@Column
    @Comment("员工表")
	private Integer empid;
	
	@Column
    @Comment("角色表id")
	private Integer roleid;
	

}