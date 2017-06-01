package io.znz.jsite.visa.bean.helper;

import java.lang.reflect.Field;

/**
 * Created by Chaly on 2017/3/6.
 */
public enum Marital {
    SINGLE("S", "单身"),
    MARRIED("M", "已婚"),
    DIVORCE("D", "离异"),
    WIDOWED("M", "丧偶");

    private String letter;

    public String getValue() {
        return letter;
    }

    private Marital(String letter, String value) {
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
