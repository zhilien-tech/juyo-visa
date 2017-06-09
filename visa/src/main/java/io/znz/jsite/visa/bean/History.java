package io.znz.jsite.visa.bean;

import io.znz.jsite.visa.bean.helper.Period;
import io.znz.jsite.visa.bean.helper.Range;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Chaly on 2017/3/6.
 */
@Entity
@Table(name = "visa_history")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@DynamicUpdate
@DynamicInsert
public class History {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    //入境时间
    @Temporal(TemporalType.DATE)
    @Column(name = "arrival_date")
    private Date arrivalDate;
    @Column(columnDefinition = "int(11) DEFAULT 1")
    private int stay;//停留周期数
    @Enumerated(value = EnumType.STRING)
    private Period period;//停留周期
    @Enumerated(value = EnumType.STRING)
    private Range destination;//目的地的国家
    @Column(columnDefinition = "varchar(100) COMMENT '备注'")
    private String remark;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Range getDestination() {
        return destination;
    }

    public void setDestination(Range destination) {
        this.destination = destination;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
