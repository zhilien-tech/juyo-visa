package io.znz.jsite.visa.bean.helper;

import java.lang.reflect.Field;

/**
 * Created by Chaly on 2017/3/6.
 */
public enum UsaStatus {
    CITIZEN("S", "美国公民"),
    GREEN_CARD("C", "绿卡"),
    NONIMMIGRANT("P", "非移民"),
    OTHER("O", "其他");

    private String letter;

    public String getValue() {
        return letter;
    }

    private UsaStatus(String letter, String value) {
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
