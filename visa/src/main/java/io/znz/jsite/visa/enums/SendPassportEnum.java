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

public enum SendPassportEnum implements IEnum {
	daiqu(1, "代取"), bankqu(2, "中信银行自取"), fastmailqu(3, "使馆快递");
	private int key;
	private String value;

	private SendPassportEnum(final int key, final String value) {
		this.value = value;
		this.key = key;
	}

	public static String get(int a) {
		SendPassportEnum[] values = SendPassportEnum.values();
		for (SendPassportEnum orderJapanVisaType : values) {
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
