/**
 * NewPeerPersionEntity.java
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
 * @Date	 2017年6月12日 	
 * 
 *  
 *  id	int	11	0	0	-1	0	0	0		0	主键				-1	0
tripid	int	11	0	-1	0	0	0	0		0	出行表的id				0	0
peerxing	varchar	255	0	-1	0	0	0	0		0	同行人姓	utf8	utf8_general_ci		0	0
peerxingen	varchar	255	0	-1	0	0	0	0		0	同行人姓英文	utf8	utf8_general_ci		0	0
peername	varchar	255	0	-1	0	0	0	0		0	同行人名字	utf8	utf8_general_ci		0	0
peernameen	varchar	255	0	-1	0	0	0	0		0	同行人名字拼音	utf8	utf8_general_ci		0	0
relationme	int	11	0	-1	0	0	0	0		0	和我的关系				0	0

 */
@Data
@Table("visa_new_peerpersion")
public class NewPeerPersionEntity {
	@Column
	@Comment("主键")
	@Id(auto = true)
	private int id;
	@Column
	private String peerxing;
	@Column
	private String peerxingen;
	@Column
	private String peernameen;
	@Column
	private String peername;
	@Column
	private int tripid;
	@Column
	private int relationme;
}
