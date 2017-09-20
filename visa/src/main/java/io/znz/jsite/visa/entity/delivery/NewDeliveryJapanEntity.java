/**
 * NewDeliveryJapanEntity.java
 * io.znz.jsite.visa.entity.delivery
 * Copyright (c) 2017, 北京科技有限公司版权所有.
*/

package io.znz.jsite.visa.entity.delivery;

import java.util.Date;

import lombok.Data;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

import com.uxuexi.core.common.util.DateUtil;
import com.uxuexi.core.common.util.Util;

/**
 * TODO(这里用一句话描述这个类的作用)
 * <p>
 * TODO(这里描述这个类补充说明 – 可选)
 *
 * @author   孙斌
 * @Date	 2017年6月23日 	 
 */
@Data
@Table("visa_new_delivery_japan")
public class NewDeliveryJapanEntity {
	@Id(auto = true)
	private Integer id;

	@Column
	@Comment("訂單id")
	private Integer order_jp_id;

	@Column
	@Comment("最早时间")
	private Date earlydate;
	@Column
	@Comment("最晚时间")
	private Date latterdate;
	@Column
	@Comment("面签时段")
	private Integer interviewtime;
	@Column
	@Comment("面签时间要求")
	private String interviewtimerequest;
	@Column
	@Comment("护照递送方式")
	private Integer visasendtype;
	@Column
	@Comment("银行自取省份")
	private String prevince;
	@Column
	@Comment("银行自取详细地址")
	private String detailplace;
	@Column
	@Comment("护照快递地址")
	private String fastmailaddress;

	//日本时区 少一小时
	public Date getEarlydate() {
		if (!Util.isEmpty(earlydate)) {
			int hours = earlydate.getHours();
			if (hours == 23) {
				earlydate = DateUtil.addDay(earlydate, 1);
				earlydate.setHours(0);
			}
		}
		return earlydate;
	}

	public Date getLatterdate() {
		if (!Util.isEmpty(latterdate)) {
			int hours = latterdate.getHours();
			if (hours == 23) {
				latterdate = DateUtil.addDay(latterdate, 1);
				latterdate.setHours(0);
			}
		}
		return latterdate;
	}

}
