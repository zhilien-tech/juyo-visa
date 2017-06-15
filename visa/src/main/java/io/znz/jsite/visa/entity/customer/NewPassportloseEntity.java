package io.znz.jsite.visa.entity.customer;

import org.nutz.dao.entity.annotation.*;
import lombok.Data;

import java.io.Serializable;


@Data
@Table("visa_new_passportlose")
public class NewPassportloseEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id(auto = true)
	private Integer id;
	
	@Column
    @Comment("签发国家")
	private String sendcountry;
	
	@Column
    @Comment("护照号")
	private String passport;
	
	@Column
    @Comment("原因")
	private String reason;
	
	@Column
    @Comment("原因英文")
	private String reasonen;
	
	@Column
    @Comment("客户管理id")
	private Integer customerid;
	

}