package io.znz.jsite.visa.bean.helper;

import java.lang.reflect.Field;

/**
 * Created by Chaly on 2017/3/9.
 */
public enum Range {
    USA("美国"),
    JAPAN("日本"),
    GLOBAL("通用");

    private Range(String value) {
        try {
            Field fieldName = getClass().getSuperclass().getDeclaredField("name");
            fieldName.setAccessible(true);
            fieldName.set(this, value);
            fieldName.setAccessible(false);
        } catch (Exception e) {
        }
    }
}
