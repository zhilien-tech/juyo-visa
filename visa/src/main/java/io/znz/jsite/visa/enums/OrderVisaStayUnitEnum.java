/**
 * UserStatusEnum.java
 * io.znz.jsite.visa.enums
 * Copyright (c) 2017, 北京科技有限公司版权所有.
*/

package io.znz.jsite.visa.enums;

import com.uxuexi.core.common.enums.IEnum;

/**
 * 订单领区枚举
 * @author   孙斌
 * @Date	 2017年6月11日 	 
 */
public enum OrderVisaStayUnitEnum implements IEnum {

	day(0, "天"), week(1, "周"), month(2, "月"), year(3, "年");
	private int key;
	private String value;

	private OrderVisaStayUnitEnum(final int key, final String value) {
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
