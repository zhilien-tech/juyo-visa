/**
 * NewOrderEntity.java
 * io.znz.jsite.visa.entity.usa
 * Copyright (c) 2017, 北京科技有限公司版权所有.
*/

package io.znz.jsite.visa.entity.usa;

import io.znz.jsite.visa.entity.customer.CustomerManageEntity;

import java.util.Date;
import java.util.List;

import lombok.Data;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

/**
 * TODO(这里用一句话描述这个类的作用)
 * <p>
 * TODO(这里描述这个类补充说明 – 可选)
 *
 * @author   孙斌
 * @Date	 2017年6月12日 	
 * 
 *  
 *  id	int	11	0	0	-1	0	0	0		0	主键				-1	0
ordernumber	varchar	255	0	-1	0	0	0	0		0	订单号	utf8	utf8_general_ci		0	0
cus_management_id	int	11	0	-1	0	0	0	0		0	客户管理id				0	0
headcount	int	11	0	-1	0	0	0	0		0	人头数				0	0
visatype	int	11	0	-1	0	0	0	0		0	签证类型				0	0
area	int	11	0	-1	0	0	0	0		0	领区				0	0
ishurry	int	11	0	-1	0	0	0	0		0	是否加急				0	0
paytype	int	11	0	-1	0	0	0	0		0	付款方式				0	0
money	double	0	0	-1	0	0	0	0		0					0	0
sendtime	date	0	0	-1	0	0	0	0		0	送签时间				0	0
outtime	date	0	0	-1	0	0	0	0		0	出签时间				0	0
remaker	varchar	255	0	-1	0	0	0	0		0	备注	utf8	utf8_general_ci		0	0
sharecountmany	int	11	0	-1	0	0	0	0		0	批量分享次数				0	0
noticecountmany	int	11	0	-1	0	0	0	0		0	批量通知次数				0	0
countrytype	int	11	0	-1	0	0	0	0		0	国家类型				0	0

 */
@Data
@Table("visa_new_order")
public class NewOrderEntity {

	@Column
	@Comment("主键")
	@Id(auto = true)
	private int id;
	@Column
	private String ordernumber;
	@Column
	private String remaker;
	@Column
	private long cus_management_id;
	@Column
	private int headcount;
	private int visatype;
	@Column
	private int area;
	@Column
	private int ishurry;
	@Column
	private int paytype;
	@Column
	private double money;
	@Column
	private int sharecountmany;
	@Column
	private int noticecountmany;
	@Column
	private int countrytype;
	@Column
	private Date sendtime;
	@Column
	private Date outtime;
	@Column
	private int status;
	@Column
	private int customerSource;//客户来源
	@Column
	private Date createtime;
	@Column
	private Date updatetime;
	/**
	 * 保存订单涉及到的类
	 */
	private CustomerManageEntity customermanage;

	private NewTrip trip;

	private List<NewPeerPersionEntity> peerList;

	private NewPayPersionEntity payPersion;

	private NewPayCompanyEntity payCompany;

	private NewFastMailEntity fastMail;

	private NewCustomerresourceEntity customerresource;

	//资料是否填写完成
	private int writebasicinfo;
}
