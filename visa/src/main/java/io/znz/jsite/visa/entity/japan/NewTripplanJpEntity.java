package io.znz.jsite.visa.entity.japan;

import io.znz.jsite.visa.bean.Scenic;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import lombok.Data;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

import com.uxuexi.core.common.util.DateUtil;
import com.uxuexi.core.common.util.Util;

@Data
@Table("visa_new_tripplan_jp")
public class NewTripplanJpEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id(auto = true)
	private Integer id;

	@Column
	@Comment("订单id")
	private Long order_jp_id;

	@Column
	@Comment("第几天")
	private Integer daynum;

	@Column
	@Comment("日期")
	private Date nowdate;

	@Column
	@Comment("城市")
	private String city;

	@Column
	@Comment("景区id")
	private String viewid;

	@Column
	@Comment("酒店id")
	private Integer hotelid;

	@Column
	@Comment("房间类型")
	private Integer hometype;

	@Column
	@Comment("房间数")
	private Integer homenum;

	@Column
	@Comment("住几晚")
	private Integer homeday;

	@Column
	@Comment("入住时间")
	private Date intime;

	@Column
	@Comment("退房时间")
	private Date outtime;

	@Column
	@Comment("早餐")
	private Integer breakfast;

	@Column
	@Comment("晚餐")
	private Integer dinner;

	private List<Scenic> scenics;//景点

	private String scenicIds;

	private Date endDate;//生成酒店的pdf用到

	public Date getNowdate() {
		if (!Util.isEmpty(nowdate)) {
			int hours = nowdate.getHours();
			if (hours == 23) {
				nowdate = DateUtil.addDay(nowdate, 1);
				nowdate.setHours(0);
			}
		}

		return nowdate;
	}

	public void setScenics(List<Scenic> scenics) {
		this.scenics = scenics;
		if (!Util.isEmpty(scenics)) {
			StringBuffer buff = new StringBuffer();
			for (int i = 0; i < scenics.size(); i++) {
				if (i == scenics.size() - 1) {
					buff.append(scenics.get(i).getId());
				} else {
					buff.append(scenics.get(i).getId() + ",");
				}

			}
			this.scenicIds = buff.toString();
		}
	}

}
