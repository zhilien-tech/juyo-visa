/**
 * TotalJpBean.java
 * io.znz.jsite.visa.totalbean
 * Copyright (c) 2017, 北京科技有限公司版权所有.
*/

package io.znz.jsite.visa.totalbean;

import lombok.Data;

/**
 * 日本统计javaBean
 * @author   崔建斌
 * @Date	 2017年8月22日 	 
 */
@Data
public class TotalJpBean {
	//主键
	private long id;
	//用户id/
	private Integer userid;
	//公司id
	private long comId;
	//订单号
	private String ordernumber;
	//送签社名称
	private String comFullName;
	//操作人
	private String username;
	//地接社公司名称
	private String landcomFullName;
	//受付番号
	private String completedNumber;
	//主申请人
	private String fullname;
	//人数
	private Integer headnum;
	//签证类型
	private Integer visatype;
	//公司类型(1-送签社,2-地接社)
	private Integer comtype;
}
