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
public enum OrderVisaUsaRelationEnum implements IEnum {
	parent(0, "父母"), relation(1, "亲戚"), spouse(2, "配偶"), friend(3, "朋友"), busness(4, "商业关系"), school(5, "学校"), employer(
			6, "雇主"), others(7, "其它"), son(8, "子女"), ;
	private int key;
	private String value;

	private OrderVisaUsaRelationEnum(final int key, final String value) {
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
