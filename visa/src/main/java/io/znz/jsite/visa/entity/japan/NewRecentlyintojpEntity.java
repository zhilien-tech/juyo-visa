package io.znz.jsite.visa.entity.japan;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

@Data
@Table("visa_new_recentlyintojp")
public class NewRecentlyintojpEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id(auto = true)
	@Column
	@Comment("主键")
	private long id;

	@Column
	@Comment("客户id")
	private long customerJpId;

	@Column
	@Comment("入境时间")
	private Date intoDate;

	@Column
	@Comment("出境时间")
	private Date outofDate;

	@Column
	@Comment("停留时间")
	private String stayDays;

	@Column
	@Comment("创建时间")
	private Date createTime;

}