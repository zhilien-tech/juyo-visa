package io.znz.jsite.visa.entity.customer;

import org.nutz.dao.entity.annotation.*;
import lombok.Data;
import java.util.Date;

import java.io.Serializable;


@Data
@Table("visa_new_usainfo")
public class NewUsainfoEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id(auto = true)
	private Integer id;
	
	@Column
    @Comment("美国驾照号")
	private String usadriveport;
	
	@Column
    @Comment("驾照签发地")
	private Date driveportsendstate;
	
	@Column
    @Comment("说明")
	private String instruction;
	
	@Column
    @Comment("说明英文")
	private String instructionen;
	
	@Column
    @Comment("签发日期")
	private Date visadate;
	
	@Column
    @Comment("签发类型")
	private String visatype;
	
	@Column
    @Comment("签发地")
	private String sendprevince;
	
	@Column
    @Comment("签证号码")
	private String visaport;
	

}