package io.znz.jsite.visa.entity.japan;

import org.nutz.dao.entity.annotation.*;
import lombok.Data;

import java.io.Serializable;


@Data
@Table("visa_new_trip_jp")
public class NewTripJpEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id(auto = true)
	private Integer id;
	
	@Column
    @Comment("订单id")
	private Integer orderJpId;
	
	@Column
    @Comment("出行目的")
	private Integer trippurpose;
	
	@Column
    @Comment("往返或多程")
	private Integer oneormore;
	

}