/**
 * VisaJapanApproStatusEnum.java
 * io.znz.jsite.visa.enums
 * Copyright (c) 2017, 北京科技有限公司版权所有.
*/

package io.znz.jsite.visa.enums;

import com.uxuexi.core.common.enums.IEnum;

/**
 * TODO(这里用一句话描述这个类的作用)
 * <p>
 * TODO(这里描述这个类补充说明 – 可选)
 *
 * @author   孙斌
 * @Date	 2017年8月16日 	 
 */
public enum VisaJapanApproStatusEnum implements IEnum {
	placeOrder(15, "下单"), shared(1, "已分享"), writeInfo(2, "资料填写"), firstReview(3, "初审"), agree(4, "通过"), refuse(5, "拒绝"), readySubmit(
			8, "准备提交使馆"), fail(23, "提交失败"), japansend(17, "递送"), japancoming(18, "发招宝中"), japanAlreadySend(19, "已发招宝"), japanSendFail(
			20, "发招宝失败"), japanReport(21, "归国报告"), japanReportFail(22, "归国报告失败");
	private int key;
	private String value;

	private VisaJapanApproStatusEnum(final int key, final String value) {
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
