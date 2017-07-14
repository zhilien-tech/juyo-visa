/**
 * UserStatusEnum.java
 * io.znz.jsite.visa.enums
 * Copyright (c) 2017, 北京科技有限公司版权所有.
*/

package io.znz.jsite.visa.enums;

import com.uxuexi.core.common.enums.IEnum;

/**
 * 在美亲戚和我的关系枚举
 * @author   孙斌
 * @Date	 2017年6月11日 	 
 */
public enum RelationRelativeWithMeEnum implements IEnum {
	SPOUSE(3, "S"), FIANCÉ(0, "F"), CHILD(1, "C"), SIBLING(2, "B");
	private int key;
	private String value;

	private RelationRelativeWithMeEnum(final int key, final String value) {
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
