package io.znz.jsite.visa.bean;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by Chaly on 2017/4/7.
 */
@Entity
@Table(name = "visa_flight")
@org.nutz.dao.entity.annotation.Table("visa_flight")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@DynamicUpdate
@DynamicInsert
public class Flight {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@org.nutz.dao.entity.annotation.Id
	private Integer id;
	@Column(name = "`from`")
	@org.nutz.dao.entity.annotation.Column("from")
	private String from;//起飞机场
	@Column(name = "`to`")
	@org.nutz.dao.entity.annotation.Column("to")
	private String to;//降落机场
	@org.nutz.dao.entity.annotation.Column("company")
	private String company;//航空公司
	@org.nutz.dao.entity.annotation.Column("line")
	private String line;//航班号
	@Temporal(TemporalType.TIME)
	@JSONField(format = "HH:mm")
	@org.nutz.dao.entity.annotation.Column("departure")
	private Date departure;//起飞时间
	@JSONField(format = "HH:mm")
	@Temporal(TemporalType.TIME)
	@org.nutz.dao.entity.annotation.Column("landing")
	private Date landing;//着陆时间
	@Column(name = "from_terminal")
	@org.nutz.dao.entity.annotation.Column("from_terminal")
	private String fromTerminal;//起飞航站楼
	@Column(name = "to_terminal")
	@org.nutz.dao.entity.annotation.Column("to_terminal")
	private String toTerminal;//降落航站楼
	@Column(name = "from_city")
	@org.nutz.dao.entity.annotation.Column("from_city")
	private String fromCity;//起飞城市
	@Column(name = "to_city")
	@org.nutz.dao.entity.annotation.Column("to_city")
	private String toCity;//降落城市

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getLine() {
		return line;
	}

	public void setLine(String line) {
		this.line = line;
	}

	public Date getDeparture() {
		return departure;
	}

	public void setDeparture(Date departure) {
		this.departure = departure;
	}

	public Date getLanding() {
		return landing;
	}

	public void setLanding(Date landing) {
		this.landing = landing;
	}

	public String getFromTerminal() {
		return fromTerminal;
	}

	public void setFromTerminal(String fromTerminal) {
		this.fromTerminal = fromTerminal;
	}

	public String getToTerminal() {
		return toTerminal;
	}

	public void setToTerminal(String toTerminal) {
		this.toTerminal = toTerminal;
	}

	public String getFromCity() {
		return fromCity;
	}

	public void setFromCity(String fromCity) {
		this.fromCity = fromCity;
	}

	public String getToCity() {
		return toCity;
	}

	public void setToCity(String toCity) {
		this.toCity = toCity;
	}
}
