/**
 * UserStatusEnum.java
 * io.znz.jsite.visa.enums
 * Copyright (c) 2017, 北京科技有限公司版权所有.
*/

package io.znz.jsite.visa.enums;

import com.uxuexi.core.common.enums.IEnum;

/**
 * 军队枚举
 * @author   孙斌
 * @Date	 2017年6月11日 	 
 */
public enum ArmyEnum implements IEnum {
	LUJUN(0, "LUJUN"), HAIJUN(1, "HAIJUN"), KONFJUN(2, "KONFJUN");
	private int key;
	private String value;

	private ArmyEnum(final int key, final String value) {
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
