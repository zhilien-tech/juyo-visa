package io.znz.jsite.visa.entity.japan;

import org.nutz.dao.entity.annotation.*;
import lombok.Data;

import java.io.Serializable;


@Data
@Table("visa_new_oldname_jp")
public class NewOldnameJpEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id(auto = true)
	private Integer id;
	
	@Column
    @Comment("")
	private Integer customerJpId;
	
	@Column
    @Comment("曾用姓")
	private String oldxing;
	
	@Column
    @Comment("曾用姓拼音")
	private String oldxingen;
	
	@Column
    @Comment("曾用名")
	private String oldname;
	
	@Column
    @Comment("曾用名拼音")
	private String oldnameen;
	

}