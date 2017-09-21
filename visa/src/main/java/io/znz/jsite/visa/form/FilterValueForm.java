/**
 * filterValueForm.java
 * io.znz.jsite.visa.form
 * Copyright (c) 2017, 北京科技有限公司版权所有.
*/

package io.znz.jsite.visa.form;

import lombok.Data;

/**
 * TODO(kendoui  下拉filter检索)
 * @author  
 * @Date	 2017年9月19日 	 
 */
@Data
public class FilterValueForm {
	private String fromCity;
	private String toCity;
	private String filterValue;
}
