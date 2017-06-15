package io.znz.jsite.visa.entity.customer;

import org.nutz.dao.entity.annotation.*;
import lombok.Data;
import java.util.Date;

import java.io.Serializable;


@Data
@Table("visa_new_oldworks")
public class NewOldworksEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id(auto = true)
	private Integer id;
	
	@Column
    @Comment("客户id")
	private Integer customerid;
	
	@Column
    @Comment("开始日期")
	private Date startdate;
	
	@Column
    @Comment("结束日期")
	private Date enddate;
	
	@Column
    @Comment("单位名称")
	private String unitname;
	
	@Column
    @Comment("电话")
	private String phone;
	
	@Column
    @Comment("单位名称英文")
	private String unitnameen;
	
	@Column
    @Comment("国家")
	private String country;
	
	@Column
    @Comment("省份")
	private String prevince;
	
	@Column
    @Comment("城市")
	private String city;
	
	@Column
    @Comment("单位详细地址")
	private String unitaddresssmall;
	
	@Column
    @Comment("单位大概地址")
	private String unitaddressbig;
	
	@Column
    @Comment("邮编")
	private String zipcode;
	
	@Column
    @Comment("单位详细地址英文")
	private String unitaddresssmallen;
	
	@Column
    @Comment("单位大概地址英文")
	private String unitaddressbigen;
	
	@Column
    @Comment("职位")
	private String job;
	
	@Column
    @Comment("职位英文")
	private String joben;
	
	@Column
    @Comment("主管姓")
	private String chargexing;
	
	@Column
    @Comment("主管姓英文")
	private String chargexingen;
	
	@Column
    @Comment("主管名字")
	private String chargename;
	
	@Column
    @Comment("主管名字英文")
	private String chargenameen;
	
	@Column
    @Comment("工作职责")
	private String workduty;
	
	@Column
    @Comment("工作职责英文")
	private String workdutyen;
	

}