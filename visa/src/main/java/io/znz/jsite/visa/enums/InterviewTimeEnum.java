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

public enum InterviewTimeEnum implements IEnum {
	all(1, "全天"), am(2, "上午"), pm(3, "下午");
	private int key;
	private String value;

	private InterviewTimeEnum(final int key, final String value) {
		this.value = value;
		this.key = key;
	}

	public static String get(int a) {
		InterviewTimeEnum[] values = InterviewTimeEnum.values();
		for (InterviewTimeEnum orderJapanVisaType : values) {
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
