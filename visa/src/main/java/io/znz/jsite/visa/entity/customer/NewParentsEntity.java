package io.znz.jsite.visa.entity.customer;

import org.nutz.dao.entity.annotation.*;
import lombok.Data;
import java.util.Date;

import java.io.Serializable;


@Data
@Table("visa_new_parents")
public class NewParentsEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id(auto = true)
	private Integer id;
	
	@Column
    @Comment("")
	private Integer customerid;
	
	@Column
    @Comment("")
	private String xing;
	
	@Column
    @Comment("")
	private String xingen;
	
	@Column
    @Comment("")
	private String name;
	
	@Column
    @Comment("")
	private String nameen;
	
	@Column
    @Comment("")
	private Date birthday;
	
	@Column
    @Comment("")
	private Integer stayusa;
	
	@Column
    @Comment("")
	private Integer dadormum;
	

}