package io.znz.jsite.ext.freemarker.util;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.DefaultObjectWrapperBuilder;

/**
 * Created by Chaly on 2017/4/27.
 */
public class BeanWrapperFactory {

    private BeanWrapperFactory() {
    }

    private static final DefaultObjectWrapperBuilder defaultObjectWrapperBuilder = new DefaultObjectWrapperBuilder(Configuration.VERSION_2_3_26);

    public static DefaultObjectWrapper get() {
        return defaultObjectWrapperBuilder.build();
    }
}
