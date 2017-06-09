package io.znz.jsite.visa.bean;

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
@Table(name = "visa_work")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@DynamicUpdate
@DynamicInsert
public class Work {
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
    private String industry;//行业
    @Column(columnDefinition = "double DEFAULT 0")
    private double salary;//月薪
    private String phone;// 公司电话
    @Type(type = "yes_no")
    @Column(columnDefinition = "char(1) DEFAULT 'N' COMMENT '是否当前的工作'")
    private boolean current;//当前的工作

    private String job;//职务
    @Column(name = "job_en", columnDefinition = "varchar(255) COMMENT '职务描述英文'")
    private String jobEN;
    private String bewrite;//职务描述
    @Column(name = "bewrite_en", columnDefinition = "varchar(255) COMMENT '职务描述英文'")
    private String bewriteEN;

    private String country;//国籍
    private String province;//省份
    private String city;//城市

    @Column(name = "zip_code")
    private String zipCode;//邮编

    private String name;// 名称
    private String address;//住址街道号
    private String room;//小区楼号单元楼层房间号

    @Column(name = "name_en", columnDefinition = "varchar(255) COMMENT '公司名称英文'")
    private String nameEN;// 名称
    @Column(name = "address_en", columnDefinition = "varchar(255) COMMENT '公司街道号英文'")
    private String addressEN;//住址街道号
    @Column(name = "room_en", columnDefinition = "varchar(255) COMMENT '公司楼号单元楼层房间号'")
    private String roomEN;//公司楼号单元楼层房间号

    private String duty;//职责
    @Column(name = "duty_en", columnDefinition = "varchar(255) COMMENT '职责英文'")
    private String dutyEN;//职责英文

    @Column(name = "boss_last_name", columnDefinition = "varchar(255) COMMENT '主管中文名'")
    private String bossLastName;
    @Column(name = "boss_first_name", columnDefinition = "varchar(255) COMMENT '主管中文姓'")
    private String bossFirstName;
    @Column(name = "boss_last_name_en", columnDefinition = "varchar(255) COMMENT '主管英文名'")
    private String bossLastNameEN;
    @Column(name = "boss_first_name_en", columnDefinition = "varchar(255) COMMENT '主管英文姓'")
    private String bossFirstNameEN;

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

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isCurrent() {
        return current;
    }

    public void setCurrent(boolean current) {
        this.current = current;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getJobEN() {
        return jobEN;
    }

    public void setJobEN(String jobEN) {
        this.jobEN = jobEN;
    }

    public String getBewrite() {
        return bewrite;
    }

    public void setBewrite(String bewrite) {
        this.bewrite = bewrite;
    }

    public String getBewriteEN() {
        return bewriteEN;
    }

    public void setBewriteEN(String bewriteEN) {
        this.bewriteEN = bewriteEN;
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

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
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

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getNameEN() {
        return nameEN;
    }

    public void setNameEN(String nameEN) {
        this.nameEN = nameEN;
    }

    public String getAddressEN() {
        return addressEN;
    }

    public void setAddressEN(String addressEN) {
        this.addressEN = addressEN;
    }

    public String getRoomEN() {
        return roomEN;
    }

    public void setRoomEN(String roomEN) {
        this.roomEN = roomEN;
    }

    public String getDuty() {
        return duty;
    }

    public void setDuty(String duty) {
        this.duty = duty;
    }

    public String getDutyEN() {
        return dutyEN;
    }

    public void setDutyEN(String dutyEN) {
        this.dutyEN = dutyEN;
    }

    public String getBossLastName() {
        return bossLastName;
    }

    public void setBossLastName(String bossLastName) {
        this.bossLastName = bossLastName;
    }

    public String getBossFirstName() {
        return bossFirstName;
    }

    public void setBossFirstName(String bossFirstName) {
        this.bossFirstName = bossFirstName;
    }

    public String getBossLastNameEN() {
        return bossLastNameEN;
    }

    public void setBossLastNameEN(String bossLastNameEN) {
        this.bossLastNameEN = bossLastNameEN;
    }

    public String getBossFirstNameEN() {
        return bossFirstNameEN;
    }

    public void setBossFirstNameEN(String bossFirstNameEN) {
        this.bossFirstNameEN = bossFirstNameEN;
    }
}
