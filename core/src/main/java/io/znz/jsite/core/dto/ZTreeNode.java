/**
 * ZTreeNode.java
 * com.linyun.airline.admin.authority.authoritymanage.form
 * Copyright (c) 2016, 北京科技有限公司版权所有.
*/

package io.znz.jsite.core.dto;

import lombok.Data;

/**
 * @author   崔建斌
 * @Date	 2016年12月14日 	 
 */
@Data
public class ZTreeNode {

	private long id;
	private long pId;
	private String name;
	private boolean open;
	private String checked;

}
