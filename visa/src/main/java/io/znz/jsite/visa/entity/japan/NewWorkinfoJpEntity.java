package io.znz.jsite.visa.entity.japan;

import org.nutz.dao.entity.annotation.*;
import lombok.Data;

import java.io.Serializable;


@Data
@Table("visa_new_workinfo_jp")
public class NewWorkinfoJpEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id(auto = true)
	private Integer id;
	
	@Column
    @Comment("客户id")
	private Integer customerJpId;
	
	@Column
    @Comment("我的职业")
	private String myjob;
	
	@Column
    @Comment("单位名称")
	private String unitname;
	
	@Column
    @Comment("单位电话")
	private String unitphone;
	
	@Column
    @Comment("单位地址")
	private String unitaddress;
	

}