/**
 * GetCommentUtil.java
 * io.znz.jsite.visa.zidingyienum
 * Copyright (c) 2017, 北京科技有限公司版权所有.
*/

package io.znz.jsite.visa.zidingyienum;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import com.uxuexi.core.common.util.Util;

/**
 * TODO(这里用一句话描述这个类的作用)
 * <p>
 * TODO(这里描述这个类补充说明 – 可选)
 *
 * @author   孙斌
 * @Date	 2017年8月3日 	 
 */
public class GetCommentUtil {

	public static String getComment(Object obj, String fieldName) {
		List<Field> list = Arrays.asList(obj.getClass().getDeclaredFields());
		//		Field[] fields = NewComeBabyJpEntity.class.getDeclaredFields();
		for (int i = 0; i < list.size(); i++) {
			Field field = list.get(i);
			if (field.isAnnotationPresent(GetComment.class)) {//是否使用GetComment注解
				for (Annotation anno : field.getDeclaredAnnotations()) {//获得所有的注解
					if (anno.annotationType().equals(GetComment.class)) {//找到自己的注解

						//获取使用自定义注解的每个字段的名称
						String fieldNamenew = field.getName();
						//获取字段所对应的注解的内容
						String name = field.getAnnotation(GetComment.class).name();
						//通过判断字段是否一致从而取得传入的字段相对应的注解中的内容
						if (!Util.isEmpty(fieldName) && !Util.isEmpty(fieldNamenew)) {
							if (fieldName.equalsIgnoreCase(fieldNamenew)) {

								return name;
							}
						}
					}
				}
			}

		}
		return "";
	}
}
