package io.znz.jsite.visa.bean;

import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Chaly on 2017/3/6.
 */
@Entity
@Table(name = "visa_army")
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@DynamicUpdate
@DynamicInsert
public class Army {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    //开始建间
    @Temporal(TemporalType.DATE)
    @Column(name = "start_date")
    private Date startDate;
    //结束建间
    @Temporal(TemporalType.DATE)
    @Column(name = "end_date")
    private Date endDate;
    private String country;//国家
    @Column(columnDefinition = "varchar(50) COMMENT '军种'")
    private String type;//军种
    @Column(columnDefinition = "varchar(100) COMMENT '军衔'")
    private String rank;//军衔
    @Column(columnDefinition = "varchar(100) COMMENT '军事特长'")
    private String specialty;//特长

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

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }
}
