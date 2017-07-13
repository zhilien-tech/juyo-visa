package io.znz.jsite.visa.bean.helper;

import java.lang.reflect.Field;

/**
 * Created by Chaly on 2017/3/12.
 */
public enum Period {
    DAY("D", "天"),
    WEEK("W", "周"),
    MONTH("M", "月"),
    YEAR("Y", "年"),
    H("H", "24小时内");
    private String letter;

    public String getValue() {
        return letter;
    }

    private Period(String letter, String value) {
        this.letter = letter;
        try {
            Field fieldName = getClass().getSuperclass().getDeclaredField("name");
            fieldName.setAccessible(true);
            fieldName.set(this, value);
            fieldName.setAccessible(false);
        } catch (Exception e) {
        }
    }
}
