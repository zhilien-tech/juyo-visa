package io.znz.jsite.visa.bean.helper;

import java.lang.reflect.Field;

/**
 * Created by Chaly on 2017/3/6.
 */
public enum Payer {
    SELF("S", "我自己"),
    COMPANY("C", "公司/组织"),
    OTHER("O", "其他人");

    String letter;

    private Payer(String letter, String value) {
        this.letter = letter;
        try {
            Field fieldName = getClass().getSuperclass().getDeclaredField("name");
            fieldName.setAccessible(true);
            fieldName.set(this, value);
            fieldName.setAccessible(false);
        } catch (Exception e) {
        }
    }

    public String getValue() {
        return letter;
    }
}
