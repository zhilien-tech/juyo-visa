package io.znz.jsite.core.util;

import io.znz.jsite.core.bean.Site;

/**
 * CMS线程变量
 */
public class ThreadLocalUtil {
    /**
     * 当前站点线程变量
     */
    private static ThreadLocal<Site> siteVariable = new ThreadLocal<Site>();

    /**
     * 获得当前站点
     */
    public static Site getSite() {
        return siteVariable.get();
    }

    /**
     * 设置当前站点
     */
    public static void setSite(Site site) {
        siteVariable.set(site);
    }

    /**
     * 移除当前站点
     */
    public static void removeSite() {
        siteVariable.remove();
    }
}
