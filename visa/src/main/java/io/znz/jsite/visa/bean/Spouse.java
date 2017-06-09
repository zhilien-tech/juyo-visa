package io.znz.jsite.visa.bean;

import io.znz.jsite.visa.bean.helper.Marital;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by Chaly on 2017/3/6.
 */
@Entity
@Table(name = "visa_spouse")
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@DynamicUpdate
@DynamicInsert
public class Spouse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Enumerated(EnumType.STRING)
    private Marital state = Marital.SINGLE;
    @Temporal(TemporalType.DATE)
    @Column(name = "wed_date")
    private Date wedDate;
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
    @Column(name = "birth_country")
    private String birthCountry;//出生国家
    @Column(name = "birth_province")
    private String birthProvince;//出生省份拼音
    @Column(name = "birth_city")
    private String birthCity;//出生城市拼音

    private String country;//现居国家
    private String province;//现居省份
    private String city;//现居城市
    private String job;//配偶职位或者职位

    private String nationality;//国籍

    //如果和申请人没住在一起
    @Column(name = "zip_code")
    private String zipCode;//邮编
    private String address;//住址街道号
    @Column(name = "address_en")
    private String addressEN;//地址英文名

    private String company;//单位中文名
    private String phone;//单位电话
    @Column(name = "company_en")
    private String companyEN;//单位英文名

    @Column(name = "divorce_country")
    private String divorceCountry;//离婚国家
    @Temporal(TemporalType.DATE)
    @Column(name = "divorce_date")
    private Date divorceDate;//离婚日期
    @Column(name = "divorce_reason")
    private String divorceReason;//离婚原因
    @Column(name = "divorce_reason_en")
    private String divorceReasonEN;//离婚原因英文

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Marital getState() {
        return state;
    }

    public void setState(Marital state) {
        this.state = state;
    }

    public Date getWedDate() {
        return wedDate;
    }

    public void setWedDate(Date wedDate) {
        this.wedDate = wedDate;
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

    public String getBirthCountry() {
        return birthCountry;
    }

    public void setBirthCountry(String birthCountry) {
        this.birthCountry = birthCountry;
    }

    public String getBirthProvince() {
        return birthProvince;
    }

    public void setBirthProvince(String birthProvince) {
        this.birthProvince = birthProvince;
    }

    public String getBirthCity() {
        return birthCity;
    }

    public void setBirthCity(String birthCity) {
        this.birthCity = birthCity;
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

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
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

    public String getAddressEN() {
        return addressEN;
    }

    public void setAddressEN(String addressEN) {
        this.addressEN = addressEN;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCompanyEN() {
        return companyEN;
    }

    public void setCompanyEN(String companyEN) {
        this.companyEN = companyEN;
    }

    public String getDivorceCountry() {
        return divorceCountry;
    }

    public void setDivorceCountry(String divorceCountry) {
        this.divorceCountry = divorceCountry;
    }

    public Date getDivorceDate() {
        return divorceDate;
    }

    public void setDivorceDate(Date divorceDate) {
        this.divorceDate = divorceDate;
    }

    public String getDivorceReason() {
        return divorceReason;
    }

    public void setDivorceReason(String divorceReason) {
        this.divorceReason = divorceReason;
    }

    public String getDivorceReasonEN() {
        return divorceReasonEN;
    }

    public void setDivorceReasonEN(String divorceReasonEN) {
        this.divorceReasonEN = divorceReasonEN;
    }
}
