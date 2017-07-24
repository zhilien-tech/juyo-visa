package io.znz.jsite.visa.entity.japan;

import io.znz.jsite.visa.entity.customer.CustomerManageEntity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import lombok.Data;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

@Data
@Table("visa_new_order_jp")
public class NewOrderJpEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id(auto = true)
	private long id;

	@Column
	@Comment("用户id")
	private Integer userid;

	@Column
	@Comment("客户管理id")
	private Long customer_manager_id;

	@Column
	@Comment("订单类型")
	private String ordernumber;

	@Column
	@Comment("人数")
	private Integer headnum;

	@Column
	@Comment("签证类型")
	private Integer visatype;

	@Column
	@Comment("领区")
	private Integer area;

	@Column
	@Comment("是否加急")
	private Integer ishurry;

	@Column
	@Comment("行程")
	private Integer triptype;

	@Column
	@Comment("收款方式")
	private Integer paytype;

	@Column
	@Comment("金额")
	private Double money;

	@Column
	@Comment("送签时间")
	private Date senddate;

	@Column
	@Comment("出签时间")
	private Date outdate;

	@Column
	@Comment("备注")
	private String remark;

	@Column
	@Comment("国家类型")
	private Integer countrytype;

	@Column
	@Comment("状态")
	private Integer status;

	@Column
	@Comment("更新时间")
	private Date updatetime;

	@Column
	@Comment("创建时间")
	private Date createtime;

	@Column
	@Comment("分享次数")
	private Integer sharenum;

	@Column
	@Comment("递送次数")
	private Integer sendnum;
	@Column
	@Comment("客户来源")
	private Integer customerSource;
	@Column
	@Comment("客户来源")
	private long comId;

	private CustomerManageEntity customermanage;

	private NewTripJpEntity tripJp;

	private List<NewDateplanJpEntity> dateplanJpList;

	private List<NewTripplanJpEntity> tripplanJpList;

	private NewFastmailJpEntity fastMail;

	//日本下载需要的东西
	private String template;

	public String getTemplate() {
		if (template == null)
			template = "newHasee";
		return template;
	}

	//每个订单对应多个客户
	private List<NewCustomerJpEntity> customerJpList;

	private NewCustomerresourceJpEntity customerresourceJp;

	private List<NewProposerInfoJpEntity> proposerInfoJpList;

	//资料是否填写完成
	private int writebasicinfo;
}