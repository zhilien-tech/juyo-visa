package io.znz.jsite.visa.bean.helper;

import com.uxuexi.core.common.enums.IEnum;

/**
 * Created by Chaly on 2017/3/12.
 */
public enum Period implements IEnum {
	DAY("D", "天"), WEEK("W", "周"), MONTH("M", "月"), YEAR("Y", "年"), H("H", "24小时内");
	private String key;
	private String value;

	public String getValue() {
		return key;
	}

	private Period(String key, String value) {
		this.key = key;
		this.value = value;
	}

	@Override
	public String key() {
		return key;

	}

	@Override
	public String value() {
		return value;
	}
}
