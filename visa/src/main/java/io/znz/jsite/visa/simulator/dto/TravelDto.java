/**
 * TravelDto.java
 * io.znz.jsite.visa.simulator.dto
 * Copyright (c) 2017, 北京科技有限公司版权所有.
*/

package io.znz.jsite.visa.simulator.dto;

import io.znz.jsite.visa.bean.helper.Period;

import java.util.Date;
import java.util.List;

/**
 * @author   朱晓川
 * @Date	 2017年7月7日 	 
 */
public class TravelDto {

	private Integer id;
	private String country;//所去国家
	//到达时间
	private Date arrivalDate;
	private int stay;//停留周期数
	private Period period;//停留周期
	private String team;//团队出行
	private String entryProvince;//入境省份/州/城市上级
	private String entryCity;//入境城市

	private String hotel;//酒店
	private String zipCode;//邮编
	private String address;

	private String contactsLastName;
	private String contactsFirstName;
	private String contactsLastNameEN;
	private String contactsFirstNameEN;
	private String contactsProvince;
	private String contactsCity;
	private String contactsAddress;
	private String contactsZipCode;
	private String contactsPhone;
	private String contactsEmail;
	private String contactsRelation;

	private String payer;

	//支付人信息
	private String payerLastName;
	private String payerFirstName;
	private String payerLastNameEN;
	private String payerFirstNameEN;
	private String payerPhone;
	private String payerEmail;
	private String payerRelation;

	//支付公司信息
	private String companyName;
	private String companyNameEN;
	private String companyRelation;
	private String companyRelationEN;
	private String companyCountry;
	private String companyProvince;
	private String companyCity;
	private String companyAddress;
	private String companyRoom;
	private String companyAddressEN;
	private String companyRoomEN;
	private String companyPhone;
	private String companyZipCode;

	private List<TogetherDto> togethers;// 同行人

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
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

	public String getTeam() {
		return team;
	}

	public void setTeam(String team) {
		this.team = team;
	}

	public String getEntryProvince() {
		return entryProvince;
	}

	public void setEntryProvince(String entryProvince) {
		this.entryProvince = entryProvince;
	}

	public String getEntryCity() {
		return entryCity;
	}

	public void setEntryCity(String entryCity) {
		this.entryCity = entryCity;
	}

	public String getHotel() {
		return hotel;
	}

	public void setHotel(String hotel) {
		this.hotel = hotel;
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

	public String getContactsLastName() {
		return contactsLastName;
	}

	public void setContactsLastName(String contactsLastName) {
		this.contactsLastName = contactsLastName;
	}

	public String getContactsFirstName() {
		return contactsFirstName;
	}

	public void setContactsFirstName(String contactsFirstName) {
		this.contactsFirstName = contactsFirstName;
	}

	public String getContactsLastNameEN() {
		return contactsLastNameEN;
	}

	public void setContactsLastNameEN(String contactsLastNameEN) {
		this.contactsLastNameEN = contactsLastNameEN;
	}

	public String getContactsFirstNameEN() {
		return contactsFirstNameEN;
	}

	public void setContactsFirstNameEN(String contactsFirstNameEN) {
		this.contactsFirstNameEN = contactsFirstNameEN;
	}

	public String getContactsProvince() {
		return contactsProvince;
	}

	public void setContactsProvince(String contactsProvince) {
		this.contactsProvince = contactsProvince;
	}

	public String getContactsCity() {
		return contactsCity;
	}

	public void setContactsCity(String contactsCity) {
		this.contactsCity = contactsCity;
	}

	public String getContactsAddress() {
		return contactsAddress;
	}

	public void setContactsAddress(String contactsAddress) {
		this.contactsAddress = contactsAddress;
	}

	public String getContactsZipCode() {
		return contactsZipCode;
	}

	public void setContactsZipCode(String contactsZipCode) {
		this.contactsZipCode = contactsZipCode;
	}

	public String getContactsPhone() {
		return contactsPhone;
	}

	public void setContactsPhone(String contactsPhone) {
		this.contactsPhone = contactsPhone;
	}

	public String getContactsEmail() {
		return contactsEmail;
	}

	public void setContactsEmail(String contactsEmail) {
		this.contactsEmail = contactsEmail;
	}

	public String getContactsRelation() {
		return contactsRelation;
	}

	public void setContactsRelation(String contactsRelation) {
		this.contactsRelation = contactsRelation;
	}

	public String getPayer() {
		return payer;
	}

	public void setPayer(String payer) {
		this.payer = payer;
	}

	public String getPayerLastName() {
		return payerLastName;
	}

	public void setPayerLastName(String payerLastName) {
		this.payerLastName = payerLastName;
	}

	public String getPayerFirstName() {
		return payerFirstName;
	}

	public void setPayerFirstName(String payerFirstName) {
		this.payerFirstName = payerFirstName;
	}

	public String getPayerLastNameEN() {
		return payerLastNameEN;
	}

	public void setPayerLastNameEN(String payerLastNameEN) {
		this.payerLastNameEN = payerLastNameEN;
	}

	public String getPayerFirstNameEN() {
		return payerFirstNameEN;
	}

	public void setPayerFirstNameEN(String payerFirstNameEN) {
		this.payerFirstNameEN = payerFirstNameEN;
	}

	public String getPayerPhone() {
		return payerPhone;
	}

	public void setPayerPhone(String payerPhone) {
		this.payerPhone = payerPhone;
	}

	public String getPayerEmail() {
		return payerEmail;
	}

	public void setPayerEmail(String payerEmail) {
		this.payerEmail = payerEmail;
	}

	public String getPayerRelation() {
		return payerRelation;
	}

	public void setPayerRelation(String payerRelation) {
		this.payerRelation = payerRelation;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getCompanyNameEN() {
		return companyNameEN;
	}

	public void setCompanyNameEN(String companyNameEN) {
		this.companyNameEN = companyNameEN;
	}

	public String getCompanyRelation() {
		return companyRelation;
	}

	public void setCompanyRelation(String companyRelation) {
		this.companyRelation = companyRelation;
	}

	public String getCompanyRelationEN() {
		return companyRelationEN;
	}

	public void setCompanyRelationEN(String companyRelationEN) {
		this.companyRelationEN = companyRelationEN;
	}

	public String getCompanyCountry() {
		return companyCountry;
	}

	public void setCompanyCountry(String companyCountry) {
		this.companyCountry = companyCountry;
	}

	public String getCompanyProvince() {
		return companyProvince;
	}

	public void setCompanyProvince(String companyProvince) {
		this.companyProvince = companyProvince;
	}

	public String getCompanyCity() {
		return companyCity;
	}

	public void setCompanyCity(String companyCity) {
		this.companyCity = companyCity;
	}

	public String getCompanyAddress() {
		return companyAddress;
	}

	public void setCompanyAddress(String companyAddress) {
		this.companyAddress = companyAddress;
	}

	public String getCompanyRoom() {
		return companyRoom;
	}

	public void setCompanyRoom(String companyRoom) {
		this.companyRoom = companyRoom;
	}

	public String getCompanyAddressEN() {
		return companyAddressEN;
	}

	public void setCompanyAddressEN(String companyAddressEN) {
		this.companyAddressEN = companyAddressEN;
	}

	public String getCompanyRoomEN() {
		return companyRoomEN;
	}

	public void setCompanyRoomEN(String companyRoomEN) {
		this.companyRoomEN = companyRoomEN;
	}

	public String getCompanyPhone() {
		return companyPhone;
	}

	public void setCompanyPhone(String companyPhone) {
		this.companyPhone = companyPhone;
	}

	public String getCompanyZipCode() {
		return companyZipCode;
	}

	public void setCompanyZipCode(String companyZipCode) {
		this.companyZipCode = companyZipCode;
	}

	public List<TogetherDto> getTogethers() {
		return togethers;
	}

	public void setTogethers(List<TogetherDto> togethers) {
		this.togethers = togethers;
	}

}