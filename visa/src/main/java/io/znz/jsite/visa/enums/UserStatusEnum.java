/**
 * UserStatusEnum.java
 * io.znz.jsite.visa.enums
 * Copyright (c) 2017, 北京科技有限公司版权所有.
*/

package io.znz.jsite.visa.enums;

import com.uxuexi.core.common.enums.IEnum;

/**
 * 用户状态枚举
 * @author   崔建斌
 * @Date	 2017年6月11日 	 
 */
public enum UserStatusEnum implements IEnum {
	INVALID(0, "未激活"), VALID(1, "激活"), FROZEN(2, "冻结");
	private int key;
	private String value;

	private UserStatusEnum(final int key, final String value) {
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
