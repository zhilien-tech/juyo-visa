/**
 * PlaceInformationEntity.java
 * io.znz.jsite.visa.entity.placeinformation
 * Copyright (c) 2017, 北京科技有限公司版权所有.
*/

package io.znz.jsite.visa.entity.placeinformation;

import java.util.Date;

import lombok.Data;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

/**
 * 签证信息-地点信息
 * @author   崔建斌
 * @Date	 2017年6月25日 	 
 */
@Data
@Table("vias_new_place_information")
public class PlaceInformationEntity {
	@Id(auto = true)
	@Column
	@Comment("主键")
	private long id;
	@Column
	@Comment("客户id")
	private long customerId;
	@Column
	@Comment("提交申请的地点")
	private String applyPlace;
	@Column
	@Comment("目前的地点")
	private String currentLocation;
	@Column
	@Comment("创建时间")
	private Date createTime;
	@Column
	@Comment("更新时间")
	private Date updateTime;
	@Column
	@Comment("状态")
	private Integer status;
	@Column
	@Comment("备注")
	private String remark;
}
