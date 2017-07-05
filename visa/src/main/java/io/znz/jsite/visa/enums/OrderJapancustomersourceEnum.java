/**
 * OrderJapanVisaType.java
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
 * @Date	 2017年6月24日 	 
 */

public enum OrderJapancustomersourceEnum implements IEnum {
	online(1, "线上"), OTS(2, "OTS"), zhike(3, "直客"), linedown(3, "线下");
	private int key;
	private String value;

	private OrderJapancustomersourceEnum(final int key, final String value) {
		this.value = value;
		this.key = key;
	}

	public static String get(int a) {
		OrderJapancustomersourceEnum[] values = OrderJapancustomersourceEnum.values();
		for (OrderJapancustomersourceEnum orderJapanVisaType : values) {
			if (a == orderJapanVisaType.key) {
				return orderJapanVisaType.value;
			}
		}
		return "";
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
