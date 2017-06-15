package io.znz.jsite.visa.entity.customer;

import org.nutz.dao.entity.annotation.*;
import lombok.Data;

import java.io.Serializable;


@Data
@Table("visa_new_language")
public class NewLanguageEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id(auto = true)
	private Integer id;
	
	@Column
    @Comment("客户id")
	private Integer customerid;
	
	@Column
    @Comment("语言")
	private Integer language;
	

}