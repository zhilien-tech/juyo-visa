package io.znz.jsite.visa.bean.helper;

import java.lang.reflect.Field;

/**
 * Created by Chaly on 2017/3/31.
 */
public enum VisaType {
    SINGLE("单次"),
    THREE("三年多次"),
    THREE_COUNTY("东三县"),
    SIX_COUNTY("东六县"),
    FIVE("五年多次");
    private VisaType(String value) {
        try {
            Field fieldName = getClass().getSuperclass().getDeclaredField("name");
            fieldName.setAccessible(true);
            fieldName.set(this, value);
            fieldName.setAccessible(false);
        } catch (Exception e) {
        }
    }
}
