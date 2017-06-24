package io.znz.jsite.visa.entity.japan;

import io.znz.jsite.visa.bean.Flight;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

@Data
@Table("visa_new_trip_jp")
public class NewTripJpEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id(auto = true)
	private Integer id;

	@Column
	@Comment("订单id")
	private Long order_jp_id;

	@Column
	@Comment("出行目的")
	private String trippurpose;
	@Column
	@Comment("出发日期")
	private Date startdate;
	@Column
	@Comment("出发城市")
	private String startcity;
	@Column
	@Comment("抵达城市")
	private String arrivecity;
	@Column
	@Comment("航班号")
	private String flightnum;
	@Column
	@Comment("返回日期")
	private Date returndate;
	@Column
	@Comment("返回出发日期")
	private String returnstartcity;
	@Column
	@Comment("返回抵达城市")
	private String returnarrivecity;
	@Column
	@Comment("返回航班号")
	private String returnflightnum;

	@Column
	@Comment("往返或多程")
	private Integer oneormore;

	private Flight filght;

}