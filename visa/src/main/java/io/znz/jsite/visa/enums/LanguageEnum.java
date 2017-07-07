/**
 * LanguageEnum.java
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
 * @Date	 2017年7月7日 	 
 */
public enum LanguageEnum implements IEnum {
	CHINESE(0, "CHINESE"), ENGLISH(1, "ENGLISH"), FRENCH(2, "FRENCH"), SPANISH(3, "SPANISH"), ITALIAN(4, "ITALIAN");
	private int key;
	private String value;

	private LanguageEnum(final int key, final String value) {
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
