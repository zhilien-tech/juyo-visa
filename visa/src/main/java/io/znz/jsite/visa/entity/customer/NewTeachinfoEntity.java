package io.znz.jsite.visa.entity.customer;

import org.nutz.dao.entity.annotation.*;
import lombok.Data;
import java.util.Date;

import java.io.Serializable;


@Data
@Table("visa_new_teachinfo")
public class NewTeachinfoEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id(auto = true)
	private Integer id;
	
	@Column
    @Comment("客户id")
	private Integer customerid;
	
	@Column
    @Comment("开始时间")
	private Date startdate;
	
	@Column
    @Comment("结束时间")
	private Date enddate;
	
	@Column
    @Comment("学校名字")
	private String schoolname;
	
	@Column
    @Comment("学校电话")
	private String schoolphone;
	
	@Column
    @Comment("学校名字英文")
	private String schoolnameen;
	
	@Column
    @Comment("国家")
	private String country;
	
	@Column
    @Comment("省")
	private String prevince;
	
	@Column
    @Comment("城市")
	private String city;
	
	@Column
    @Comment("学校地址详细")
	private String schooladdresssmall;
	
	@Column
    @Comment("邮编")
	private String zipcode;
	
	@Column
    @Comment("学校详细地址英文")
	private String schooladdresssmallen;
	
	@Column
    @Comment("学校大概地址")
	private String schooladdressbig;
	
	@Column
    @Comment("学校大概地址英文")
	private String schooladdressbigen;
	
	@Column
    @Comment("专业")
	private String major;
	
	@Column
    @Comment("专业英文")
	private String majoren;
	

}