/**
 * NewCustomerOrderEntity.java
 * io.znz.jsite.visa.entity.usa
 * Copyright (c) 2017, 北京科技有限公司版权所有.
*/

package io.znz.jsite.visa.entity.usa;

import lombok.Data;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

/**
 * TODO(这里用一句话描述这个类的作用)
 * <p>
 * TODO(这里描述这个类补充说明 – 可选)
 *
 * @author   孙斌
 * @Date	 2017年6月14日 	 
 */
@Data
@Table("visa_new_customer_order")
public class NewCustomerOrderEntity {

	@Column
	@Comment("主键")
	@Id(auto = true)
	private int id;
	@Column
	private int orderid;
	@Column
	private int customerid;
}
