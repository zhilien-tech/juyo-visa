package io.znz.jsite.visa.bean;

import io.znz.jsite.visa.bean.helper.Relation;
import io.znz.jsite.visa.bean.helper.UsaStatus;
import org.hibernate.annotations.*;
import org.hibernate.annotations.Cache;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by Chaly on 2017/3/6.
 */
@Entity
@Table(name = "visa_family")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@DynamicUpdate
@DynamicInsert
public class Family {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "last_name", columnDefinition = "varchar(255) COMMENT '中文名'")
    private String lastName;
    @Column(name = "first_name", columnDefinition = "varchar(255) COMMENT '中文姓'")
    private String firstName;
    @Column(name = "last_name_en", columnDefinition = "varchar(255) COMMENT '英文名'")
    private String lastNameEN;
    @Column(name = "first_name_en", columnDefinition = "varchar(255) COMMENT '英文姓'")
    private String firstNameEN;

    @Temporal(TemporalType.DATE)
    private Date birthday;//生日
    @Enumerated(EnumType.STRING)
    private Relation relation;

    @Type(type = "yes_no")
    @Column(name = "in_usa",columnDefinition = "char(1) DEFAULT 'N' COMMENT '是否在美国'")
    private boolean inUsa;//是否在美国
    @Type(type = "yes_no")
    @Column(name = "in_japan",columnDefinition = "char(1) DEFAULT 'N' COMMENT '是否在日本'")
    private boolean inJapan;//是否在日本
    @Enumerated(EnumType.STRING)
    @Column(name = "usa_status")
    private UsaStatus usaStatus = UsaStatus.OTHER;//亲属在美身份
    @Column(name = "usa_address")
    private String usaAddress;// 在美地址
    @Column(name = "usa_phone")
    private String usaPhone;// 在美电话

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Relation getRelation() {
        return relation;
    }

    public void setRelation(Relation relation) {
        this.relation = relation;
    }

    public boolean isInUsa() {
        return inUsa;
    }

    public void setInUsa(boolean inUsa) {
        this.inUsa = inUsa;
    }

    public boolean isInJapan() {
        return inJapan;
    }

    public void setInJapan(boolean inJapan) {
        this.inJapan = inJapan;
    }

    public UsaStatus getUsaStatus() {
        return usaStatus;
    }

    public void setUsaStatus(UsaStatus usaStatus) {
        this.usaStatus = usaStatus;
    }

    public String getUsaAddress() {
        return usaAddress;
    }

    public void setUsaAddress(String usaAddress) {
        this.usaAddress = usaAddress;
    }

    public String getUsaPhone() {
        return usaPhone;
    }

    public void setUsaPhone(String usaPhone) {
        this.usaPhone = usaPhone;
    }
}
