package io.znz.jsite.visa.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
 * Created by Chaly on 2017/4/7.
 */
@Entity
@Table(name = "visa_hotel")
@org.nutz.dao.entity.annotation.Table("visa_hotel")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@DynamicUpdate
@DynamicInsert
public class Hotel {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@org.nutz.dao.entity.annotation.Id
	private Integer id;
	private String name;
	private String address;
	@Column(name = "name_jp")
	private String nameJP;
	@Column(name = "address_jp")
	private String addressJP;
	private String phone;
	private String city;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public String getNameJP() {
		return nameJP;
	}

	public void setNameJP(String nameJP) {
		this.nameJP = nameJP;
	}

	public String getAddressJP() {
		return addressJP;
	}

	public void setAddressJP(String addressJP) {
		this.addressJP = addressJP;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}
}
