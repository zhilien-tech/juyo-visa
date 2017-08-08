/**
 * Test.java
 * io.znz.jsite.visa.web
 * Copyright (c) 2017, 北京科技有限公司版权所有.
*/

package io.znz.jsite.visa.web;

import io.znz.jsite.visa.entity.japan.NewComeBabyJpEntity;
import io.znz.jsite.visa.zidingyienum.GetCommentUtil;

/**
 * TODO(这里用一句话描述这个类的作用)
 * <p>
 * TODO(这里描述这个类补充说明 – 可选)
 *
 * @author   孙斌
 * @Date	 2017年6月14日 	 
 */
public class Test {
	public static final String HASH_ALGORITHM = "SHA-1";
	public static final int HASH_INTERATIONS = 1024;
	private static final int SALT_SIZE = 8; //盐长度

	public static void main(String[] args) {
		/*//		List<Field> list = Arrays.asList(NewComeBabyJpEntity.getClass().getDeclaredFields());
		Field[] fields = NewComeBabyJpEntity.class.getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			Field field = fields[i];
			if (field.isAnnotationPresent(GetComment.class)) {//是否使用GetComment注解
				for (Annotation anno : field.getDeclaredAnnotations()) {//获得所有的注解
					if (anno.annotationType().equals(GetComment.class)) {//找到自己的注解
						System.out.println((GetComment) anno);
						System.out.println(field.getDeclaredAnnotations());
						System.out.println(field.getName());
						System.out.println(field.getAnnotation(GetComment.class).name());
						if(!((GetComment)anno).isCanNull()){//注解的值
						    if(TestUtil.getFieldValueByName(field.getName(),obj)==null){
						        throw new RuntimeException("类："+NewComeBabyJpEntity.class+"的属性："+field.getName()+"不能为空，实际的值:"+TestUtil.getFieldValueByName(field.getName(),obj)+",注解：field.getDeclaredAnnotations()");
						    }
						}
						if(!((GetComment)anno).isCanEmpty()){
						    if(TestUtil.getFieldValueByName(field.getName(),obj).equals("")){
						        throw new RuntimeException("类："+NewComeBabyJpEntity.class+"的属性："+field.getName()+"不能为空字符串，实际的值:"+TestUtil.getFieldValueByName(field.getName(),obj)+",注解：field.getDeclaredAnnotations()");
						    }
						}
						if(!((GetComment)anno).isCanZero()){
						    if(TestUtil.getFieldValueByName(field.getName(),obj).toString().equals("0") || TestUtil.getFieldValueByName(field.getName(),obj).toString().equals("0.0")){
						        throw new RuntimeException("类："+NewComeBabyJpEntity.class+"的属性："+field.getName()+"不能为空字符0，实际的值:"+TestUtil.getFieldValueByName(field.getName(),obj)+",注解：field.getDeclaredAnnotations()");
						    }
						}
					}
				}
			}

		}*/

		String name = GetCommentUtil.getComment(new NewComeBabyJpEntity(), "comFullName");
		System.out.println(name);
	}
}
