/**
 * HistoryDto.java
 * io.znz.jsite.visa.simulator.dto
 * Copyright (c) 2017, 北京科技有限公司版权所有.
*/

package io.znz.jsite.visa.simulator.dto;

import io.znz.jsite.visa.bean.helper.Period;
import io.znz.jsite.visa.bean.helper.Range;

import java.util.Date;

/**
 * 出行历史
 */
public class HistoryDto {

	private Integer id;
	//入境时间
	private Date arrivalDate;
	private int stay;//停留周期数
	private Period period;//停留周期
	private Range destination;//目的地的国家
	private String remark;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getArrivalDate() {
		return arrivalDate;
	}

	public void setArrivalDate(Date arrivalDate) {
		this.arrivalDate = arrivalDate;
	}

	public int getStay() {
		return stay;
	}

	public void setStay(int stay) {
		this.stay = stay;
	}

	public Period getPeriod() {
		return period;
	}

	public void setPeriod(Period period) {
		this.period = period;
	}

	public Range getDestination() {
		return destination;
	}

	public void setDestination(Range destination) {
		this.destination = destination;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}
