/**
 * UserStatusEnum.java
 * io.znz.jsite.visa.enums
 * Copyright (c) 2017, 北京科技有限公司版权所有.
*/

package io.znz.jsite.visa.enums;

import com.uxuexi.core.common.enums.IEnum;

/**
 * 同行人和我的关系枚举
 * @author   孙斌
 * @Date	 2017年6月11日 	 
 */
public enum DayTypeEnum implements IEnum {
	DAY(0, "D"), WEEK(1, "W"), MONTH(2, "M"), YEAR(3, "Y"), H(4, "H");
	private int key;
	private String value;

	private DayTypeEnum(final int key, final String value) {
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
