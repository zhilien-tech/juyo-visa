package io.znz.jsite.visa.entity.japan;

import org.nutz.dao.entity.annotation.*;
import lombok.Data;

import java.io.Serializable;


@Data
@Table("visa_new_fastmail_jp")
public class NewFastmailJpEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id(auto = true)
	private Integer id;
	
	@Column
    @Comment("资料来源")
	private Integer datasource;
	
	@Column
    @Comment("快递单号")
	private String fastmailnum;
	
	@Column
    @Comment("回邮方式")
	private Integer mailmethod;
	
	@Column
    @Comment("回邮地址")
	private String mailaddress;
	
	@Column
    @Comment("联系人")
	private String linkpeople;
	
	@Column
    @Comment("电话")
	private String phone;
	
	@Id(auto = true)
	private String invoicecontent = "";
	
	@Column
    @Comment("发票头")
	private String invoicehead;
	
	@Column
    @Comment("备注")
	private String remaker;
	
	@Column
    @Comment("订单id")
	private Integer orderJpId;
	

}