package io.znz.jsite.visa.bean;

import com.alibaba.fastjson.annotation.JSONField;
import io.znz.jsite.visa.bean.helper.Payer;
import io.znz.jsite.visa.bean.helper.Period;
import io.znz.jsite.visa.bean.helper.Relation;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by Chaly on 2017/3/6.
 */
@Entity
@Table(name = "visa_travel")
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@DynamicUpdate
@DynamicInsert
public class Travel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    @JSONField(serialize = false)
    @JoinColumn(name = "customer_id")
    private Customer customer;
    private String country;//所去国家
    //到达时间
    @Temporal(TemporalType.DATE)
    @Column(name = "arrival_date")
    private Date arrivalDate;
    @Column(columnDefinition = "int(11) DEFAULT 1")
    private int stay;//停留周期数
    @Enumerated(EnumType.STRING)
    private Period period;//停留周期
    private String team;//团队出行
    @Column(name = "entry_province", columnDefinition = "varchar(255) COMMENT '入境省份/州/城市上级'")
    private String entryProvince;//入境省份/州/城市上级
    @Column(name = "entry_city", columnDefinition = "varchar(255) COMMENT '入境省份/州/城市上级'")
    private String entryCity;//入境城市

    private String hotel;//酒店
    @Column(name = "zip_code")
    private String zipCode;//邮编
    private String address;

    @Column(name = "contacts_last_name", columnDefinition = "varchar(255) COMMENT '联系人中文名'")
    private String contactsLastName;
    @Column(name = "contacts_first_name", columnDefinition = "varchar(255) COMMENT '联系人中文姓'")
    private String contactsFirstName;
    @Column(name = "contacts_last_name_en", columnDefinition = "varchar(255) COMMENT '联系人英文名'")
    private String contactsLastNameEN;
    @Column(name = "contacts_first_name_en", columnDefinition = "varchar(255) COMMENT '联系人英文姓'")
    private String contactsFirstNameEN;
    @Column(name = "contacts_province")
    private String contactsProvince;
    @Column(name = "contacts_city")
    private String contactsCity;
    @Column(name = "contacts_address")
    private String contactsAddress;
    @Column(name = "contacts_zip_code")
    private String contactsZipCode;
    @Column(name = "contacts_phone")
    private String contactsPhone;
    @Column(name = "contacts_email")
    private String contactsEmail;
    @Enumerated(EnumType.STRING)
    private Relation contactsRelation;

    @Enumerated(EnumType.STRING)
    private Payer payer;

    //支付人信息
    @Column(name = "payer_last_name", columnDefinition = "varchar(255) COMMENT '支付人中文名'")
    private String payerLastName;
    @Column(name = "payer_first_name", columnDefinition = "varchar(255) COMMENT '支付人中文名'")
    private String payerFirstName;
    @Column(name = "payer_last_name_en", columnDefinition = "varchar(255) COMMENT '支付人英文名'")
    private String payerLastNameEN;
    @Column(name = "payer_first_name_en", columnDefinition = "varchar(255) COMMENT '支付人英文名'")
    private String payerFirstNameEN;
    @Column(name = "payer_phone")
    private String payerPhone;
    @Column(name = "payer_email")
    private String payerEmail;
    @Enumerated(EnumType.STRING)
    @Column(name = "payer_relation")
    private Relation payerRelation;

    //支付公司信息
    @Column(name = "company_name")
    private String companyName;
    @Column(name = "company_name_en")
    private String companyNameEN;
    @Column(name = "company_relation")
    private String companyRelation;
    @Column(name = "company_relation_en")
    private String companyRelationEN;
    @Column(name = "company_country")
    private String companyCountry;
    @Column(name = "company_province")
    private String companyProvince;
    @Column(name = "company_city")
    private String companyCity;
    @Column(name = "company_address")
    private String companyAddress;
    @Column(name = "company_room")
    private String companyRoom;
    @Column(name = "company_address_en")
    private String companyAddressEN;
    @Column(name = "company_room_en")
    private String companyRoomEN;
    @Column(name = "company_phone")
    private String companyPhone;
    @Column(name = "company_zip_code")
    private String companyZipCode;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "visa_travel_together", joinColumns = @JoinColumn(name = "travel_id"))
    private List<Together> togethers;// 同行人

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
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

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public String getEntryProvince() {
        return entryProvince;
    }

    public void setEntryProvince(String entryProvince) {
        this.entryProvince = entryProvince;
    }

    public String getEntryCity() {
        return entryCity;
    }

    public void setEntryCity(String entryCity) {
        this.entryCity = entryCity;
    }

    public String getHotel() {
        return hotel;
    }

    public void setHotel(String hotel) {
        this.hotel = hotel;
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

    public String getContactsLastName() {
        return contactsLastName;
    }

    public void setContactsLastName(String contactsLastName) {
        this.contactsLastName = contactsLastName;
    }

    public String getContactsFirstName() {
        return contactsFirstName;
    }

    public void setContactsFirstName(String contactsFirstName) {
        this.contactsFirstName = contactsFirstName;
    }

    public String getContactsLastNameEN() {
        return contactsLastNameEN;
    }

    public void setContactsLastNameEN(String contactsLastNameEN) {
        this.contactsLastNameEN = contactsLastNameEN;
    }

    public String getContactsFirstNameEN() {
        return contactsFirstNameEN;
    }

    public void setContactsFirstNameEN(String contactsFirstNameEN) {
        this.contactsFirstNameEN = contactsFirstNameEN;
    }

    public String getContactsProvince() {
        return contactsProvince;
    }

    public void setContactsProvince(String contactsProvince) {
        this.contactsProvince = contactsProvince;
    }

    public String getContactsCity() {
        return contactsCity;
    }

    public void setContactsCity(String contactsCity) {
        this.contactsCity = contactsCity;
    }

    public String getContactsAddress() {
        return contactsAddress;
    }

    public void setContactsAddress(String contactsAddress) {
        this.contactsAddress = contactsAddress;
    }

    public String getContactsZipCode() {
        return contactsZipCode;
    }

    public void setContactsZipCode(String contactsZipCode) {
        this.contactsZipCode = contactsZipCode;
    }

    public String getContactsPhone() {
        return contactsPhone;
    }

    public void setContactsPhone(String contactsPhone) {
        this.contactsPhone = contactsPhone;
    }

    public String getContactsEmail() {
        return contactsEmail;
    }

    public void setContactsEmail(String contactsEmail) {
        this.contactsEmail = contactsEmail;
    }

    public Relation getContactsRelation() {
        return contactsRelation;
    }

    public void setContactsRelation(Relation contactsRelation) {
        this.contactsRelation = contactsRelation;
    }

    public Payer getPayer() {
        return payer;
    }

    public void setPayer(Payer payer) {
        this.payer = payer;
    }

    public String getPayerLastName() {
        return payerLastName;
    }

    public void setPayerLastName(String payerLastName) {
        this.payerLastName = payerLastName;
    }

    public String getPayerFirstName() {
        return payerFirstName;
    }

    public void setPayerFirstName(String payerFirstName) {
        this.payerFirstName = payerFirstName;
    }

    public String getPayerLastNameEN() {
        return payerLastNameEN;
    }

    public void setPayerLastNameEN(String payerLastNameEN) {
        this.payerLastNameEN = payerLastNameEN;
    }

    public String getPayerFirstNameEN() {
        return payerFirstNameEN;
    }

    public void setPayerFirstNameEN(String payerFirstNameEN) {
        this.payerFirstNameEN = payerFirstNameEN;
    }

    public String getPayerPhone() {
        return payerPhone;
    }

    public void setPayerPhone(String payerPhone) {
        this.payerPhone = payerPhone;
    }

    public String getPayerEmail() {
        return payerEmail;
    }

    public void setPayerEmail(String payerEmail) {
        this.payerEmail = payerEmail;
    }

    public Relation getPayerRelation() {
        return payerRelation;
    }

    public void setPayerRelation(Relation payerRelation) {
        this.payerRelation = payerRelation;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyNameEN() {
        return companyNameEN;
    }

    public void setCompanyNameEN(String companyNameEN) {
        this.companyNameEN = companyNameEN;
    }

    public String getCompanyRelation() {
        return companyRelation;
    }

    public void setCompanyRelation(String companyRelation) {
        this.companyRelation = companyRelation;
    }

    public String getCompanyRelationEN() {
        return companyRelationEN;
    }

    public void setCompanyRelationEN(String companyRelationEN) {
        this.companyRelationEN = companyRelationEN;
    }

    public String getCompanyCountry() {
        return companyCountry;
    }

    public void setCompanyCountry(String companyCountry) {
        this.companyCountry = companyCountry;
    }

    public String getCompanyProvince() {
        return companyProvince;
    }

    public void setCompanyProvince(String companyProvince) {
        this.companyProvince = companyProvince;
    }

    public String getCompanyCity() {
        return companyCity;
    }

    public void setCompanyCity(String companyCity) {
        this.companyCity = companyCity;
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }

    public String getCompanyRoom() {
        return companyRoom;
    }

    public void setCompanyRoom(String companyRoom) {
        this.companyRoom = companyRoom;
    }

    public String getCompanyAddressEN() {
        return companyAddressEN;
    }

    public void setCompanyAddressEN(String companyAddressEN) {
        this.companyAddressEN = companyAddressEN;
    }

    public String getCompanyRoomEN() {
        return companyRoomEN;
    }

    public void setCompanyRoomEN(String companyRoomEN) {
        this.companyRoomEN = companyRoomEN;
    }

    public String getCompanyPhone() {
        return companyPhone;
    }

    public void setCompanyPhone(String companyPhone) {
        this.companyPhone = companyPhone;
    }

    public String getCompanyZipCode() {
        return companyZipCode;
    }

    public void setCompanyZipCode(String companyZipCode) {
        this.companyZipCode = companyZipCode;
    }

    public List<Together> getTogethers() {
        return togethers;
    }

    public void setTogethers(List<Together> togethers) {
        this.togethers = togethers;
    }

}
