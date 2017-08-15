/**
 * JapanSimulatorForm.java
 * io.znz.jsite.visa.simulator.form
 * Copyright (c) 2017, 北京科技有限公司版权所有.
*/

package io.znz.jsite.visa.simulator.form;

import lombok.Data;

/**
 * 用于接收日本自动填表回传的参数
 * @author   朱晓川
 * @Date	 2017年8月15日 	 
 */
@Data
public class JapanSimulatorForm {

	/**受付番号*/
	private String acceptanceNumber;

}
