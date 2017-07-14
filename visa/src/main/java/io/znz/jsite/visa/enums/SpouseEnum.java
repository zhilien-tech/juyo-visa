/**
 * UserStatusEnum.java
 * io.znz.jsite.visa.enums
 * Copyright (c) 2017, 北京科技有限公司版权所有.
*/

package io.znz.jsite.visa.enums;

import com.uxuexi.core.common.enums.IEnum;

/**
 * 配偶状态
 * @author   孙斌
 * @Date	 2017年6月11日 	 
 */
public enum SpouseEnum implements IEnum {
	sing(0, "S"), marry(1, "M"), liyi(2, "D"), sangou(3, "M");
	private int key;
	private String value;

	private SpouseEnum(final int key, final String value) {
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
