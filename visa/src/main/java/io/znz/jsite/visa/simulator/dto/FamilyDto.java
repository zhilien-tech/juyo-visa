/**
 * FamilyDto.java
 * io.znz.jsite.visa.simulator.dto
 * Copyright (c) 2017, 北京科技有限公司版权所有.
*/

package io.znz.jsite.visa.simulator.dto;

import java.util.Date;

public class FamilyDto {

	private Integer id;
	private String lastName;
	private String firstName;
	private String lastNameEN;
	private String firstNameEN;

	private Date birthday;//生日
	private String relation;

	private boolean inUsa;//是否在美国
	private boolean inJapan;//是否在日本
	private String usaStatus = "O";//亲属在美身份
	private String usaAddress;// 在美地址
	private String usaPhone;// 在美电话

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastNameEN() {
		return lastNameEN;
	}

	public void setLastNameEN(String lastNameEN) {
		this.lastNameEN = lastNameEN;
	}

	public String getFirstNameEN() {
		return firstNameEN;
	}

	public void setFirstNameEN(String firstNameEN) {
		this.firstNameEN = firstNameEN;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getRelation() {
		return relation;
	}

	public void setRelation(String relation) {
		this.relation = relation;
	}

	public boolean isInUsa() {
		return inUsa;
	}

	public void setInUsa(boolean inUsa) {
		this.inUsa = inUsa;
	}

	public boolean isInJapan() {
		return inJapan;
	}

	public void setInJapan(boolean inJapan) {
		this.inJapan = inJapan;
	}

	public String getUsaStatus() {
		return usaStatus;
	}

	public void setUsaStatus(String usaStatus) {
		this.usaStatus = usaStatus;
	}

	public String getUsaAddress() {
		return usaAddress;
	}

	public void setUsaAddress(String usaAddress) {
		this.usaAddress = usaAddress;
	}

	public String getUsaPhone() {
		return usaPhone;
	}

	public void setUsaPhone(String usaPhone) {
		this.usaPhone = usaPhone;
	}

}
