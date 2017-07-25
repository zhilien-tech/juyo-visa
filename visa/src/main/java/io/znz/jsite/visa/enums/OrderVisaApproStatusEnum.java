/**
 * UserStatusEnum.java
 * io.znz.jsite.visa.enums
 * Copyright (c) 2017, 北京科技有限公司版权所有.
*/

package io.znz.jsite.visa.enums;

import com.uxuexi.core.common.enums.IEnum;

/**
 * 订单领区枚举
 * @author   孙斌
 * @Date	 2017年6月11日 	 
 */
public enum OrderVisaApproStatusEnum implements IEnum {
	placeOrder(15, "下单"), shared(1, "已分享"), writeInfo(2, "资料填写"), firstReview(3, "初审"), agree(4, "通过"), refuse(5, "拒绝"), waitingSend(
			6, "代送"), DS(7, "DS-160"), readySubmit(8, "准备提交使馆"), submiting(16, "提交中"), submited(9, "已提交使馆"), yueVisa(
			10, "约签"), Return(11, "返回"), refuseVisa(12, "拒签"), complete(13, "完成"), EVUS(14, "EVUS");
	private int key;
	private String value;

	private OrderVisaApproStatusEnum(final int key, final String value) {
		this.value = value;
		this.key = key;
	}

	@Override
	public String key() {
		return String.valueOf(key);
	}

	@Override
	public String value() {
		return value;
	}

	public int intKey() {
		return key;
	}
}
