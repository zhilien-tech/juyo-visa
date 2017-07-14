/**
 * UserStatusEnum.java
 * io.znz.jsite.visa.enums
 * Copyright (c) 2017, 北京科技有限公司版权所有.
*/

package io.znz.jsite.visa.enums;

import com.uxuexi.core.common.enums.IEnum;

/**
 * 在美亲戚身份枚举
 * @author   孙斌
 * @Date	 2017年6月11日 	 
 */
public enum RelationInUSAIdentityEnum implements IEnum {
	CITIZEN(0, "S"), GREEN_CARD(1, "C"), NONIMMIGRANT(2, "P"), OTHER(3, "O");
	private int key;
	private String value;

	private RelationInUSAIdentityEnum(final int key, final String value) {
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
