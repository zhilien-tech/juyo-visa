package io.znz.jsite.visa.entity.customer;

import org.nutz.dao.entity.annotation.*;
import lombok.Data;

import java.io.Serializable;


@Data
@Table("visa_new_workinfo")
public class NewWorkinfoEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id(auto = true)
	private Integer id;
	
	@Column
    @Comment("客户id")
	private Integer customerid;
	
	@Column
    @Comment("工作状况")
	private Integer jobstatus;
	
	@Column
    @Comment("月薪")
	private Double monthmoney;
	
	@Column
    @Comment("当前职务")
	private String nowjob;
	
	@Column
    @Comment("当前职务英文")
	private String nowjoben;
	
	@Column
    @Comment("职业描述")
	private String jobdescription;
	
	@Column
    @Comment("职业描述英文")
	private String jobdescriptionen;
	
	@Column
    @Comment("当前工作国家")
	private String nowcountry;
	
	@Column
    @Comment("当前省份")
	private String nowprevince;
	
	@Column
    @Comment("当前城市")
	private String nowcity;
	
	@Column
    @Comment("单位名称")
	private String unitname;
	
	@Column
    @Comment("单位电话")
	private String unitphone;
	
	@Column
    @Comment("单位名称英文")
	private String unitnameen;
	
	@Column
    @Comment("公司详细地址")
	private String unitaddresssmall;
	
	@Column
    @Comment("邮编")
	private String zipcode;
	
	@Column
    @Comment("单位详细地址英文")
	private String unitaddresssmallen;
	
	@Column
    @Comment("单位地址大概地址")
	private String unitaddressbig;
	
	@Column
    @Comment("单位大概地址英文")
	private String unitaddressbigen;
	
	@Column
    @Comment("工作职责")
	private String jobduty;
	
	@Column
    @Comment("工作职责英文")
	private String jobdutyen;
	

}