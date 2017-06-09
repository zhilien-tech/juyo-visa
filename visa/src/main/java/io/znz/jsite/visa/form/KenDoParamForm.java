/**
 * KenDoParamForm.java
 * io.znz.jsite.visa.form
 * Copyright (c) 2017, 北京科技有限公司版权所有.
*/

package io.znz.jsite.visa.form;

import lombok.Data;

import com.uxuexi.core.web.form.SQLParamForm;

/**
 * TODO(这里用一句话描述这个类的作用)
 * <p>
 * TODO(这里描述这个类补充说明 – 可选)
 *
 * @author   孙斌
 * @Date	 2017年6月8日 	 
 */
@Data
public abstract class KenDoParamForm implements SQLParamForm {
	private int take;
	private int skip;
	//当前页数
	private int pageNumber;

	//每页记录数
	private int pageSize;

}
