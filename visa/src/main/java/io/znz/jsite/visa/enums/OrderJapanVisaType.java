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

public enum OrderJapanVisaType implements IEnum {
	SINGLE(0, "单次"), one(1, "一年单次"), THREE_COUNTY(2, "东三县"), SIX_COUNTY(3, "新三县"), run(4, "冲绳"), three(5, "普通三年多次"), five(
			6, "普通五年");
	private int key;
	private String value;

	private OrderJapanVisaType(final int key, final String value) {
		this.value = value;
		this.key = key;
	}

	public static String get(int a) {
		OrderJapanVisaType[] values = OrderJapanVisaType.values();
		for (OrderJapanVisaType orderJapanVisaType : values) {
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
