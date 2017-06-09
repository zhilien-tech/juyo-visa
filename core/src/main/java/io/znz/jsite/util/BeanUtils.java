package io.znz.jsite.util;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Chaly on 16/10/12.
 */
public class BeanUtils extends org.springframework.beans.BeanUtils {

    private static String[] getNullPropertyNames(Object source, String[] include, String[] exclude) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<String>();
        for (java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if(srcValue == null){
                emptyNames.add(pd.getName());
                continue;
            }
            if (exclude != null && ArrayUtils.contains(exclude, pd.getName())) {
                emptyNames.add(pd.getName());
                continue;
            }
            if(include != null && !ArrayUtils.contains(include, pd.getName())){
                emptyNames.add(pd.getName());
                continue;
            }
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

    public static void copyPropertiesIgnoreNull(Object src, Object target) {
        BeanUtils.copyProperties(src, target, ArrayUtils.addAll(getNullPropertyNames(src, null, null)));
    }

    public static void copyPropertiesInclude(Object src, Object target, String... properties) {
        BeanUtils.copyProperties(src, target, getNullPropertyNames(src, properties, null));
    }

    public static void copyPropertiesExclude(Object src, Object target, String... properties) {
        BeanUtils.copyProperties(src, target, getNullPropertyNames(src, null, properties));
    }

}
