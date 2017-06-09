package io.znz.jsite.visa.bean.helper;

import java.lang.reflect.Field;

/**
 * 状态
 * Created by Chaly on 2016/12/3.
 */
public enum State {
    DRAFT("草稿"),
    PENDING("待审核"),
    CONFIRMED("已确认"),
    BUSY("提交中"),
    SUCCESS("成功"),
    FINISH("完成"),
    REFUSE("拒绝");

    private State(String value) {
        try {
            Field fieldName = getClass().getSuperclass().getDeclaredField("name");
            fieldName.setAccessible(true);
            fieldName.set(this, value);
            fieldName.setAccessible(false);
        } catch (Exception e) {
        }
    }
}
