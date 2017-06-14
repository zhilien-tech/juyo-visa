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
public enum OrderVisaApproStatusEnum implements IEnum {
	draft(0, "草稿"), submiting(1, "提交中"), agree(2, "同意"), refuse(3, "拒绝");
	private int key;
	private String value;

	private OrderVisaApproStatusEnum(final int key, final String value) {
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
