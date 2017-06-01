package io.znz.jsite.visa.bean;

import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

/**
 * Created by Chaly on 2017/3/13.
 */
@Entity
@Table(name = "visa_passport")
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@DynamicUpdate
@DynamicInsert
public class Passport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "passport", columnDefinition = "varchar(100) COMMENT ' 原护照号,为了方便查找'")
    private String passport;//原护照号
    @Embedded
    private Option country;//原护照号国家
    @Column(name = "why")
    private String why;//原护照号丢失原因
    @Column(name = "why_en")
    private String whyEN;//原护照号丢失原因英文

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPassport() {
        return passport;
    }

    public void setPassport(String passport) {
        this.passport = passport;
    }

    public Option getCountry() {
        return country;
    }

    public void setCountry(Option country) {
        this.country = country;
    }

    public String getWhy() {
        return why;
    }

    public void setWhy(String why) {
        this.why = why;
    }

    public String getWhyEN() {
        return whyEN;
    }

    public void setWhyEN(String whyEN) {
        this.whyEN = whyEN;
    }
}
