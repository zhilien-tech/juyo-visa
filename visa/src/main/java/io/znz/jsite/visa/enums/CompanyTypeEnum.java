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
public enum CompanyTypeEnum implements IEnum {
	send(1, "送签社"), land(2, "地接社");
	private int key;
	private String value;

	private CompanyTypeEnum(final int key, final String value) {
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
