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
@Table("visa_new_dateplan_jp")
public class NewDateplanJpEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	@Column
	@Comment("主键")
	@Id
	private Integer id;

	@Column
	@Comment("出行表id")
	private Integer trip_jp_id;

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
	@Comment("返回出发城市")
	private String returnstartcity;

	@Column
	@Comment("返回抵达城市")
	private String returnarrivecity;

	@Column
	@Comment("返回航班号")
	private String returnflightnum;

	private Flight flight;

	/**
	 * (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((arrivecity == null) ? 0 : arrivecity.hashCode());
		result = prime * result + ((returndate == null) ? 0 : returndate.hashCode());
		result = prime * result + ((startdate == null) ? 0 : startdate.hashCode());
		return result;
	}

	/**
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NewDateplanJpEntity other = (NewDateplanJpEntity) obj;
		if (arrivecity == null) {
			if (other.arrivecity != null)
				return false;
		} else if (!arrivecity.equals(other.arrivecity))
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