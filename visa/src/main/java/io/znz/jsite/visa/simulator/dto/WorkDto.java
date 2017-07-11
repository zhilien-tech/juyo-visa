/**
 * WorkDto.java
 * io.znz.jsite.visa.simulator.dto
 * Copyright (c) 2017, 北京科技有限公司版权所有.
*/

package io.znz.jsite.visa.simulator.dto;

import java.util.Date;

public class WorkDto {

	private Integer id;
	//开始建间
	private Date startDate;
	//结束建间
	private Date endDate;
	private String industry;//行业
	private double salary;//月薪
	private String phone;// 公司电话
	private boolean current;//当前的工作

	private String job;//职务
	private String jobEN;
	private String bewrite;//职务描述
	private String bewriteEN;

	private String country;//国籍
	private String province;//省份
	private String city;//城市

	private String zipCode;//邮编

	private String name;// 名称
	private String address;//住址街道号
	private String room;//小区楼号单元楼层房间号

	private String nameEN;// 名称
	private String addressEN;//住址街道号
	private String roomEN;//公司楼号单元楼层房间号

	private String duty;//职责
	private String dutyEN;//职责英文

	private String bossLastName;
	private String bossFirstName;
	private String bossLastNameEN;
	private String bossFirstNameEN;

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

	public String getIndustry() {
		return industry;
	}

	public void setIndustry(String industry) {
		this.industry = industry;
	}

	public double getSalary() {
		return salary;
	}

	public void setSalary(double salary) {
		this.salary = salary;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public boolean isCurrent() {
		return current;
	}

	public void setCurrent(boolean current) {
		this.current = current;
	}

	public String getJob() {
		return job;
	}

	public void setJob(String job) {
		this.job = job;
	}

	public String getJobEN() {
		return jobEN;
	}

	public void setJobEN(String jobEN) {
		this.jobEN = jobEN;
	}

	public String getBewrite() {
		return bewrite;
	}

	public void setBewrite(String bewrite) {
		this.bewrite = bewrite;
	}

	public String getBewriteEN() {
		return bewriteEN;
	}

	public void setBewriteEN(String bewriteEN) {
		this.bewriteEN = bewriteEN;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getRoom() {
		return room;
	}

	public void setRoom(String room) {
		this.room = room;
	}

	public String getNameEN() {
		return nameEN;
	}

	public void setNameEN(String nameEN) {
		this.nameEN = nameEN;
	}

	public String getAddressEN() {
		return addressEN;
	}

	public void setAddressEN(String addressEN) {
		this.addressEN = addressEN;
	}

	public String getRoomEN() {
		return roomEN;
	}

	public void setRoomEN(String roomEN) {
		this.roomEN = roomEN;
	}

	public String getDuty() {
		return duty;
	}

	public void setDuty(String duty) {
		this.duty = duty;
	}

	public String getDutyEN() {
		return dutyEN;
	}

	public void setDutyEN(String dutyEN) {
		this.dutyEN = dutyEN;
	}

	public String getBossLastName() {
		return bossLastName;
	}

	public void setBossLastName(String bossLastName) {
		this.bossLastName = bossLastName;
	}

	public String getBossFirstName() {
		return bossFirstName;
	}

	public void setBossFirstName(String bossFirstName) {
		this.bossFirstName = bossFirstName;
	}

	public String getBossLastNameEN() {
		return bossLastNameEN;
	}

	public void setBossLastNameEN(String bossLastNameEN) {
		this.bossLastNameEN = bossLastNameEN;
	}

	public String getBossFirstNameEN() {
		return bossFirstNameEN;
	}

	public void setBossFirstNameEN(String bossFirstNameEN) {
		this.bossFirstNameEN = bossFirstNameEN;
	}

}
