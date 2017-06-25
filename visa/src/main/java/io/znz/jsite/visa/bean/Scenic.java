package io.znz.jsite.visa.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
 * Created by Chaly on 2017/4/12.
 */
@Entity
@Table(name = "visa_scenic")
@org.nutz.dao.entity.annotation.Table("visa_scenic")
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@DynamicUpdate
@DynamicInsert
public class Scenic {
	@Id
	@org.nutz.dao.entity.annotation.Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String name;
	@Column(name = "name_jp")
	private String nameJP;
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

	public String getNameJP() {
		return nameJP;
	}

	public void setNameJP(String nameJP) {
		this.nameJP = nameJP;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Scenic [id=" + id + ", name=" + name + ", nameJP=" + nameJP + ", city=" + city + "]";
	}

}
