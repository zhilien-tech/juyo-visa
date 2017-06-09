package io.znz.jsite.visa.bean.helper;

import java.lang.reflect.Field;

/**
 * Created by Chaly on 2017/3/6.
 */
public enum Gender {
    MALE("男"),
    FEMALE("女"),
    OTHER("其他");

    private Gender(String value) {
        try {
            Field fieldName = getClass().getSuperclass().getDeclaredField("name");
            fieldName.setAccessible(true);
            fieldName.set(this, value);
            fieldName.setAccessible(false);
        } catch (Exception e) {
        }
    }
}
