/**
 * PayPersionRelationEnum.java
 * io.znz.jsite.visa.enums
 * Copyright (c) 2017, 北京科技有限公司版权所有.
*/

package io.znz.jsite.visa.enums;

import com.uxuexi.core.common.enums.IEnum;

/**
 * 支付人关系
 * @author   孙斌
 * @Date	 2017年7月14日 	 
 */
public enum PayPersionRelationEnum implements IEnum {
	CHILD(8, "C"), PARENT(0, "P"), SPOUSE(2, "S"), OTHER_RELATIVE(1, "R"), FRIEND(3, "F"), OTHER(7, "O");
	private int key;
	private String value;

	private PayPersionRelationEnum(final int key, final String value) {
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
