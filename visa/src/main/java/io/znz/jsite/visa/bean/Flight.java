package io.znz.jsite.visa.bean;

import com.alibaba.fastjson.annotation.JSONField;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Chaly on 2017/4/7.
 */
@Entity
@Table(name = "visa_flight")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@DynamicUpdate
@DynamicInsert
public class Flight {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "`from`")
    private String from;//起飞机场
    @Column(name = "`to`")
    private String to;//降落机场
    private String company;//航空公司
    private String line;//航班号
    @Temporal(TemporalType.TIME)
    @JSONField(format = "HH:mm")
    private Date departure;//起飞时间
    @JSONField(format = "HH:mm")
    @Temporal(TemporalType.TIME)
    private Date landing;//着陆时间
    @Column(name = "from_terminal")
    private String fromTerminal;//起飞航站楼
    @Column(name = "to_terminal")
    private String toTerminal;//降落航站楼
    @Column(name = "from_city")
    private String fromCity;//起飞城市
    @Column(name = "to_city")
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
