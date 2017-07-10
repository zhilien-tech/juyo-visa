package io.znz.jsite.visa.bean.helper;

import java.lang.reflect.Field;

import com.uxuexi.core.common.enums.IEnum;

/**
 * Created by Chaly on 2017/3/6.
 */
public enum Gender implements IEnum {
	MALE("男"), FEMALE("女"), OTHER("其他");

	private int key;
	private String value;

	private Gender(String value) {
		try {
			Field fieldName = getClass().getSuperclass().getDeclaredField("name");
			fieldName.setAccessible(true);
			fieldName.set(this, value);
			fieldName.setAccessible(false);
		} catch (Exception e) {
		}
	}

	@Override
	public String key() {
		return String.valueOf(key);

	}

	@Override
	public String value() {
		return value;
	}
}
