/**
 * UserLoginEnum.java
 * io.znz.jsite.core.enums
 * Copyright (c) 2017, 北京科技有限公司版权所有.
*/

package io.znz.jsite.core.enums;

import com.uxuexi.core.common.enums.IEnum;

/**
 * 用户登录枚举
 * @author   崔建斌
 * @Date	 2017年6月16日 	 
 */
public enum UserLoginEnum implements IEnum {
	PERSONNEL(1, "工作人员"), TOURIST_IDENTITY(2, "游客身份");
	private int key;
	private String value;

	private UserLoginEnum(final int key, final String value) {
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
