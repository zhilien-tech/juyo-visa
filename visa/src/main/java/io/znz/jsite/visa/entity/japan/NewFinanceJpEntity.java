package io.znz.jsite.visa.entity.japan;

import java.io.Serializable;

import lombok.Data;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

@Data
@Table("visa_new_finance_jp")
public class NewFinanceJpEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id(auto = true)
	private Integer id;

	@Column
	@Comment("客户id")
	private Long customer_jp_id;

	@Column
	@Comment("业务")
	private String business;

	@Column
	@Comment("业务具体信息")
	private String detail;

}