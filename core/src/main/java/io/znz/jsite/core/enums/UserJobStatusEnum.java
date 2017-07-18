/**
 * UserJobStatusEnum.java
 * io.znz.jsite.visa.enums
 * Copyright (c) 2017, 北京科技有限公司版权所有.
*/

package io.znz.jsite.core.enums;

import com.uxuexi.core.common.enums.IEnum;

/**
 * 用户就职状态
 * @author   崔建斌
 * @Date	 2017年7月10日 	 
 */
public enum UserJobStatusEnum implements IEnum {
	JOB(1, "在职"), QUIT(2, "离职");
	private int key;
	private String value;

	private UserJobStatusEnum(final int key, final String value) {
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
