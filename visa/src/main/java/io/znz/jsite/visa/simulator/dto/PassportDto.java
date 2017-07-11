/**
 * PassportDto.java
 * io.znz.jsite.visa.simulator.dto
 * Copyright (c) 2017, 北京科技有限公司版权所有.
*/

package io.znz.jsite.visa.simulator.dto;

import io.znz.jsite.visa.bean.Option;

/**
 * 旧护照信息
 * @author   朱晓川
 * @Date	 2017年7月7日 	 
 */
public class PassportDto {

	private Integer id;
	private String passport;//原护照号
	private Option country;//原护照号国家
	private String why;//原护照号丢失原因
	private String whyEN;//原护照号丢失原因英文

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getPassport() {
		return passport;
	}

	public void setPassport(String passport) {
		this.passport = passport;
	}

	public Option getCountry() {
		return country;
	}

	public void setCountry(Option country) {
		this.country = country;
	}

	public String getWhy() {
		return why;
	}

	public void setWhy(String why) {
		this.why = why;
	}

	public String getWhyEN() {
		return whyEN;
	}

	public void setWhyEN(String whyEN) {
		this.whyEN = whyEN;
	}
}
