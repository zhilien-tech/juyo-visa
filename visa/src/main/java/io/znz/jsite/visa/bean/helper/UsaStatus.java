package io.znz.jsite.visa.bean.helper;

import com.uxuexi.core.common.enums.IEnum;

/**
 * Created by Chaly on 2017/3/6.
 */
public enum UsaStatus implements IEnum {
	CITIZEN("S", "美国公民"), GREEN_CARD("C", "绿卡"), NONIMMIGRANT("P", "非移民"), OTHER("O", "其他");

	private String key;
	private String value;

	private UsaStatus(String key, String value) {
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
