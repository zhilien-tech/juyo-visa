/**
 * SpouseDto.java
 * io.znz.jsite.visa.simulator.dto
 * Copyright (c) 2017, 北京科技有限公司版权所有.
*/

package io.znz.jsite.visa.simulator.dto;

import io.znz.jsite.visa.bean.helper.Marital;

import java.util.Date;

/**
 * TODO(这里用一句话描述这个类的作用)
 * <p>
 * TODO(这里描述这个类补充说明 – 可选)
 *
 * @author   朱晓川
 * @Date	 2017年7月7日 	 
 */
public class SpouseDto {

	private Integer id;
	private Marital state = Marital.SINGLE;
	private Date wedDate;
	private String lastName;
	private String firstName;
	private String lastNameEN;
	private String firstNameEN;

	private Date birthday;//生日
	private String birthCountry;//出生国家
	private String birthProvince;//出生省份拼音
	private String birthCity;//出生城市拼音

	private String country;//现居国家
	private String province;//现居省份
	private String city;//现居城市
	private String job;//配偶职位或者职位

	private String nationality;//国籍

	//如果和申请人没住在一起
	private String zipCode;//邮编
	private String address;//住址街道号
	private String addressEN;//地址英文名

	private String company;//单位中文名
	private String phone;//单位电话
	private String companyEN;//单位英文名

	private String divorceCountry;//离婚国家
	private Date divorceDate;//离婚日期
	private String divorceReason;//离婚原因
	private String divorceReasonEN;//离婚原因英文

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Marital getState() {
		return state;
	}

	public void setState(Marital state) {
		this.state = state;
	}

	public Date getWedDate() {
		return wedDate;
	}

	public void setWedDate(Date wedDate) {
		this.wedDate = wedDate;
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

	public String getBirthCountry() {
		return birthCountry;
	}

	public void setBirthCountry(String birthCountry) {
		this.birthCountry = birthCountry;
	}

	public String getBirthProvince() {
		return birthProvince;
	}

	public void setBirthProvince(String birthProvince) {
		this.birthProvince = birthProvince;
	}

	public String getBirthCity() {
		return birthCity;
	}

	public void setBirthCity(String birthCity) {
		this.birthCity = birthCity;
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

	public String getJob() {
		return job;
	}

	public void setJob(String job) {
		this.job = job;
	}

	public String getNationality() {
		return nationality;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getAddressEN() {
		return addressEN;
	}

	public void setAddressEN(String addressEN) {
		this.addressEN = addressEN;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getCompanyEN() {
		return companyEN;
	}

	public void setCompanyEN(String companyEN) {
		this.companyEN = companyEN;
	}

	public String getDivorceCountry() {
		return divorceCountry;
	}

	public void setDivorceCountry(String divorceCountry) {
		this.divorceCountry = divorceCountry;
	}

	public Date getDivorceDate() {
		return divorceDate;
	}

	public void setDivorceDate(Date divorceDate) {
		this.divorceDate = divorceDate;
	}

	public String getDivorceReason() {
		return divorceReason;
	}

	public void setDivorceReason(String divorceReason) {
		this.divorceReason = divorceReason;
	}

	public String getDivorceReasonEN() {
		return divorceReasonEN;
	}

	public void setDivorceReasonEN(String divorceReasonEN) {
		this.divorceReasonEN = divorceReasonEN;
	}

}
