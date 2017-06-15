package io.znz.jsite.visa.entity.customer;

import org.nutz.dao.entity.annotation.*;
import lombok.Data;
import java.util.Date;

import java.io.Serializable;


@Data
@Table("visa_new_recentlyintousa")
public class NewRecentlyintousaEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id(auto = true)
	private Integer id;
	
	@Column
    @Comment("客户管理id")
	private Integer customerid;
	
	@Column
    @Comment("进美时间")
	private Date intousadate;
	
	@Column
    @Comment("停留时间")
	private Integer stayday;
	
	@Column
    @Comment("停留单位")
	private String stayunit;
	

}