/**
 * NewTrip.java
 * io.znz.jsite.visa.entity.usa
 * Copyright (c) 2017, 北京科技有限公司版权所有.
*/

package io.znz.jsite.visa.entity.usa;

import java.util.Date;

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
 * 
 * 
 *id	int	11	0	0	-1	0	0	0		0	主键				-1	0
orderid	int	11	0	-1	0	0	0	0		0	订单id				0	0
arrivedate	date	0	0	-1	0	0	0	0		0	到美时间				0	0
staytime	int	11	0	-1	0	0	0	0		0	停留时间				0	0
staytype	int	11	0	-1	0	0	0	0		0	停留单位				0	0
teamname	varchar	255	0	-1	0	0	0	0		0	团队名称	utf8	utf8_general_ci		0	0
intostate	varchar	255	0	-1	0	0	0	0		0	进入的州	utf8	utf8_general_ci		0	0
intocity	varchar	255	0	-1	0	0	0	0		0	入境城市	utf8	utf8_general_ci		0	0
usahotel	varchar	255	0	-1	0	0	0	0		0	美国酒店	utf8	utf8_general_ci		0	0
linkaddress	varchar	255	0	-1	0	0	0	0		0	联系地址	utf8	utf8_general_ci		0	0
zipcode	varchar	255	0	-1	0	0	0	0		0	邮编	utf8	utf8_general_ci		0	0
linkxing	varchar	255	0	-1	0	0	0	0		0	美国联系人姓	utf8	utf8_general_ci		0	0
linkname	varchar	255	0	-1	0	0	0	0		0	美国联系人名字	utf8	utf8_general_ci		0	0
linkxingen	varchar	255	0	-1	0	0	0	0		0	联系人姓拼音	utf8	utf8_general_ci		0	0
linknameen	varchar	255	0	-1	0	0	0	0		0	联系人名字拼音	utf8	utf8_general_ci		0	0
linkrelation	int	11	0	-1	0	0	0	0		0	联系人和我的关系				0	0
linkstate	varchar	255	0	-1	0	0	0	0		0	联系人所在的州	utf8	utf8_general_ci		0	0
linkcity	varchar	255	0	-1	0	0	0	0		0	联系人城市	utf8	utf8_general_ci		0	0
detailaddress	varchar	255	0	-1	0	0	0	0		0	联系人详细地址	utf8	utf8_general_ci		0	0
phone	varchar	255	0	-1	0	0	0	0		0	电话	utf8	utf8_general_ci		0	0
email	varchar	255	0	-1	0	0	0	0		0	邮箱	utf8	utf8_general_ci		0	0
paypersion	varchar	255	0	-1	0	0	0	0		0	支付人	utf8	utf8_general_ci		0	0
linkzipcode	varchar	255	0	-1	0	0	0	0		0	联系人邮编	utf8	utf8_general_ci		0	0

 */
@Data
@Table("visa_new_trip")
public class NewTrip {
	@Column
	@Comment("主键")
	@Id(auto = true)
	private int id;
	@Column
	private String teamname;
	@Column
	private String intostate;
	@Column
	private String intocity;
	@Column
	private String usahotel;
	@Column
	private String linkaddress;
	@Column
	private String zipcode;
	@Column
	private String linkxing;
	@Column
	private String linkname;
	@Column
	private String linkxingen;
	@Column
	private String linknameen;
	@Column
	private String linkstate;
	@Column
	private String linkcity;
	@Column
	private String detailaddress;
	@Column
	private String phone;
	@Column
	private String email;
	@Column
	private String paypersion;
	@Column
	private String linkzipcode;
	@Column
	private int orderid;
	@Column
	private int staytime;
	@Column
	private int staytype;
	@Column
	private int linkrelation;
	@Column
	private Date arrivedate;

}
