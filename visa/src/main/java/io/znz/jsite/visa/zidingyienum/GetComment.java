/**
 * GetComment.java
 * io.znz.jsite.visa.zidingyienum
 * Copyright (c) 2017, 北京科技有限公司版权所有.
*/

package io.znz.jsite.visa.zidingyienum;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * TODO(这里用一句话描述这个类的作用)
 * <p>
 * TODO(这里描述这个类补充说明 – 可选)
 *
 * @author   孙斌
 * @Date	 2017年8月3日 	 
 */
@Documented
@Target(ElementType.FIELD)
@Inherited
@Retention(RetentionPolicy.RUNTIME)
//项目启动时就加载
public @interface GetComment {
	/**
	 * 字段名称
	 *
	 */
	String name() default "";

	/**
	 * 错误原因
	 *
	 */

	String text() default "";
}
