package io.znz.jsite.visa.bean;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by Chaly on 2017/3/6.
 */
@Entity
@Table(name = "visa_usa_info")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@DynamicUpdate
@DynamicInsert
public class Usa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "driving_license")
    private String drivingLicense;//美国驾照号
    @Column(name = "driving_license_province")
    private String drivingLicenseProvince;//美国驾照号

    private String immigrant;//移民申请原因
    @Column(name = "immigrant_en")
    private String immigrantEN;//移民申请原因英文

    @Column(name = "old_visa")
    private String oldVisa;//原签证号码
    @Temporal(TemporalType.DATE)
    @Column(name = "old_visa_date")
    private Date oldVisaDate;//原签证时间
    @Type(type = "yes_no")
    @Column(name = "same_as_this", columnDefinition = "char(1) DEFAULT 'Y' COMMENT '是否和本次相同'")
    private boolean sameAsThis;//是否和本次相同
    @Column(name = "old_visa_type")
    private String oldVisaType;//原签证类型
    @Column(name = "old_visa_city")
    //美国:北京、上海、广州，成都、沈阳
    //日本:北京、上海、广州，成都、沈阳、青岛
    private String oldVisaCity;//原签证签发地

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDrivingLicense() {
        return drivingLicense;
    }

    public void setDrivingLicense(String drivingLicense) {
        this.drivingLicense = drivingLicense;
    }

    public String getDrivingLicenseProvince() {
        return drivingLicenseProvince;
    }

    public void setDrivingLicenseProvince(String drivingLicenseProvince) {
        this.drivingLicenseProvince = drivingLicenseProvince;
    }

    public String getImmigrant() {
        return immigrant;
    }

    public void setImmigrant(String immigrant) {
        this.immigrant = immigrant;
    }

    public String getImmigrantEN() {
        return immigrantEN;
    }

    public void setImmigrantEN(String immigrantEN) {
        this.immigrantEN = immigrantEN;
    }

    public String getOldVisa() {
        return oldVisa;
    }

    public void setOldVisa(String oldVisa) {
        this.oldVisa = oldVisa;
    }

    public Date getOldVisaDate() {
        return oldVisaDate;
    }

    public void setOldVisaDate(Date oldVisaDate) {
        this.oldVisaDate = oldVisaDate;
    }

    public boolean isSameAsThis() {
        return sameAsThis;
    }

    public void setSameAsThis(boolean sameAsThis) {
        this.sameAsThis = sameAsThis;
    }

    public String getOldVisaType() {
        return oldVisaType;
    }

    public void setOldVisaType(String oldVisaType) {
        this.oldVisaType = oldVisaType;
    }

    public String getOldVisaCity() {
        return oldVisaCity;
    }

    public void setOldVisaCity(String oldVisaCity) {
        this.oldVisaCity = oldVisaCity;
    }
}
