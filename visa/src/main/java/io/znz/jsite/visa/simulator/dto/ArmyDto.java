/**
 * ArmyDto.java
 * io.znz.jsite.visa.simulator.dto
 * Copyright (c) 2017, 北京科技有限公司版权所有.
*/

package io.znz.jsite.visa.simulator.dto;

import java.util.Date;

/**
 * @author   朱晓川
 * @Date	 2017年7月7日 	 
 */
public class ArmyDto {

	private Integer id;
	//开始建间
	private Date startDate;
	//结束建间
	private Date endDate;
	private String country;//国家
	private String type;//军种
	private String rank;//军衔
	private String specialty;//特长

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getRank() {
		return rank;
	}

	public void setRank(String rank) {
		this.rank = rank;
	}

	public String getSpecialty() {
		return specialty;
	}

	public void setSpecialty(String specialty) {
		this.specialty = specialty;
	}

}
