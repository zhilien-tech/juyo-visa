/**
 * JapanVisaTypeEnum.java
 * io.znz.jsite.visa.simulator.data.jp
 * Copyright (c) 2017, 北京科技有限公司版权所有.
*/

package io.znz.jsite.visa.simulator.data.jp;

import com.uxuexi.core.common.enums.IEnum;

/**
 * 日本东北六县签证类型(数次签证的时候才需要选择)
 * <p>
 * 东北六县选择3，否则选4(VISA_TYPE_2  radio)
 * 
 * @author   朱晓川
 * @Date	 2017年8月7日 	 
 */
public enum JapanVisaType2Enum implements IEnum {

	SINGLE(3, "3"), MULTIPLE(4, "4");

	private int key;
	private String value;

	private JapanVisaType2Enum(final int key, final String value) {
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
