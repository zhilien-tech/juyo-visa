/**
 * UserStatusEnum.java
 * io.znz.jsite.visa.enums
 * Copyright (c) 2017, 北京科技有限公司版权所有.
*/

package io.znz.jsite.visa.enums;

import com.uxuexi.core.common.enums.IEnum;

/**
 * 同行人和我的关系枚举
 * @author   孙斌
 * @Date	 2017年6月11日 	 
 */
public enum PeerPersionRelationWithMeEnum implements IEnum {
	SPOUSE(2, "S"), FRIEND(3, "C"), RELATIVE(1, "R"), BUSINESS_ASSOCIATE(4, "B"), EMPLOYER(8, "P"), SCHOOL_OFFICIAL(0,
			"H"), OTHER(7, "O");
	private int key;
	private String value;

	private PeerPersionRelationWithMeEnum(final int key, final String value) {
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
