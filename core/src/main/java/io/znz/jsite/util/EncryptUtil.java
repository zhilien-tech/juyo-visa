package io.znz.jsite.util;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by Chaly on 15/12/3.
 */
public class EncryptUtil {

    /**
     * 字符串加密工具方法
     * @param target 需要加密的字符串
     * @return 加密后的字符串
     */
    public static String encryptString(String target) {
        if (StringUtils.isEmpty(target)) {
            return "??????";
        }
        target = target.trim();
        if (target.length() == 1) {
            return target;
        } else if (target.length() <= 3) {
            return "*" + target.substring(0);
        } else if (target.length() <= 8) {
            return "***" + target.substring(2);
        } else if (target.length() <= 11) {
            return encryptStringLengthMoreThanEight(target, 3, 4);
        } else {
            return encryptStringLengthMoreThanEight(target, 4, 3);
        }
    }

    private static String encryptStringLengthMoreThanEight(String target, int start, int end) {
        String hideNo = target.substring(start, target.length() - end);
        StringBuilder stars = new StringBuilder();
        for (int i = 0; i < hideNo.length(); i++) {
            stars.append("*");
        }
        //插入前缀
        stars.insert(0, target.substring(0, start));
        //追加后缀
        stars.append(target.substring(target.length() - end));
        return stars.toString();
    }
}
