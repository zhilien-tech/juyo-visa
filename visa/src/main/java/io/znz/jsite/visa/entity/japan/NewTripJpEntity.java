package io.znz.jsite.visa.entity.japan;

import io.znz.jsite.visa.bean.Flight;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

import com.uxuexi.core.common.util.DateUtil;
import com.uxuexi.core.common.util.Util;

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

	private Flight gofilght;
	private Flight returnfilght;

	//日本时区 少一小时
	public Date getStartdate() {
		if (!Util.isEmpty(startdate)) {
			int hours = startdate.getHours();
			if (hours == 23) {
				startdate = DateUtil.addDay(startdate, 1);
				startdate.setHours(0);
			}
		}
		return startdate;
	}

	public Date getReturndate() {
		if (!Util.isEmpty(returndate)) {
			int hours = returndate.getHours();
			if (hours == 23) {
				returndate = DateUtil.addDay(returndate, 1);
				returndate.setHours(0);
			}
		}

		return returndate;
	}

	/**
	 * (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((arrivecity == null) ? 0 : arrivecity.hashCode());
		result = prime * result + ((oneormore == null) ? 0 : oneormore.hashCode());
		result = prime * result + ((returndate == null) ? 0 : returndate.hashCode());
		result = prime * result + ((startdate == null) ? 0 : startdate.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NewTripJpEntity other = (NewTripJpEntity) obj;
		if (arrivecity == null) {
			if (other.arrivecity != null)
				return false;
		} else if (!arrivecity.equals(other.arrivecity))
			return false;
		if (oneormore == null) {
			if (other.oneormore != null)
				return false;
		} else if (!oneormore.equals(other.oneormore))
			return false;
		if (returndate == null) {
			if (other.returndate != null)
				return false;
		} else if (!returndate.equals(other.returndate))
			return false;
		if (startdate == null) {
			if (other.startdate != null)
				return false;
		} else if (!startdate.equals(other.startdate))
			return false;
		return true;
	}

}