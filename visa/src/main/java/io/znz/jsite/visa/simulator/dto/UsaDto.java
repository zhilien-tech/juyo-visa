/**
 * UsaDto.java
 * io.znz.jsite.visa.simulator.dto
 * Copyright (c) 2017, 北京科技有限公司版权所有.
*/

package io.znz.jsite.visa.simulator.dto;

import java.util.Date;

/**
 * @author   朱晓川
 * @Date	 2017年7月7日 	 
 */
public class UsaDto {

	private Integer id;

	private String drivingLicense;//美国驾照号
	private String drivingLicenseProvince;//美国驾照签发地

	private String immigrant;//移民申请原因
	private String immigrantEN;//移民申请原因英文

	private String oldVisa;//原签证号码
	private Date oldVisaDate;//原签证时间
	private boolean sameAsThis;//是否和本次相同
	private String oldVisaType;//原签证类型
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
