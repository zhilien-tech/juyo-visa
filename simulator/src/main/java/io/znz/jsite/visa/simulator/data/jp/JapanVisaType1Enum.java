/**
 * JapanVisaTypeEnum.java
 * io.znz.jsite.visa.simulator.data.jp
 * Copyright (c) 2017, 北京科技有限公司版权所有.
*/

package io.znz.jsite.visa.simulator.data.jp;

import com.uxuexi.core.common.enums.IEnum;

/**
 * 日本签证类型
 * <p>
 * 单次签证，大使馆官方网站填写1，数次填写N(VISA_TYPE_1  radio)
 * 
 * @author   朱晓川
 * @Date	 2017年8月7日 	 
 */
public enum JapanVisaType1Enum implements IEnum {

	SINGLE(1, "2"), MULTIPLE(2, "N");

	private int key;
	private String value;

	private JapanVisaType1Enum(final int key, final String value) {
		this.key = key;
		this.value = value;
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
