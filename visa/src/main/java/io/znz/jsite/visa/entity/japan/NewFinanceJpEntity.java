package io.znz.jsite.visa.entity.japan;

import org.nutz.dao.entity.annotation.*;
import lombok.Data;

import java.io.Serializable;


@Data
@Table("visa_new_finance_jp")
public class NewFinanceJpEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id(auto = true)
	private Integer id;
	
	@Column
    @Comment("客户id")
	private Integer customerJpId;
	
	@Column
    @Comment("业务")
	private Integer business;
	
	@Column
    @Comment("业务具体信息")
	private String detail;
	

}