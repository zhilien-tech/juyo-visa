/**
 * CustomerDto.java
 * io.znz.jsite.visa.simulator.dto
 * Copyright (c) 2017, 北京科技有限公司版权所有.
*/
package io.znz.jsite.visa.simulator.dto;

import io.znz.jsite.util.StringUtils;
import io.znz.jsite.visa.bean.Army;
import io.znz.jsite.visa.bean.OldName;
import io.znz.jsite.visa.bean.Option;
import io.znz.jsite.visa.bean.Passport;
import io.znz.jsite.visa.bean.Spouse;
import io.znz.jsite.visa.bean.Travel;
import io.znz.jsite.visa.bean.Usa;
import io.znz.jsite.visa.bean.helper.Relation;
import io.znz.jsite.visa.bean.helper.State;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.google.common.collect.Lists;

/**
 * 用于美国官方签证网站的数据传输对象
 * @author   朱晓川
 * @Date	 2017年7月6日 	 
 */
public class CustomerDto {

	private Long id;

	/*-----------基本信息-----------*/
	private String lastName;
	private String firstName;
	private String lastNameEN;
	private String firstNameEN;

	private String idCardNo;//身份证号
	private String gender;//性别

	/*-----------护照信息-----------*/

	private String passport;//护照号
	private String type = "普通";//护照类型  写死
	private String code = "CHN";//国家编码
	//签发时间
	private Date issueDate;
	//过期时间
	private Date expiryDate;
	private String issueProvince;//护照签发省份拼音
	private String issueCity;//护照签发城市拼音

	/*----------出生信息------------*/

	private Date birthday;//生日
	private String birthCountry;//出生国家
	private String birthProvince;//出生省份拼音
	private String birthCity;//出生城市拼音

	private String birthProvinceCN;//出生省份
	private String birthCityCN;//出生城市

	/*-----------现居地信息-----------*/
	private String country;//国籍
	private String province;//省份
	private String city;//城市

	private String mobile;// 手机号
	private String phone;//座机号
	private String email;//邮箱

	private String zipCode;//邮编
	private String address;//住址街道号
	private String room;//小区楼号单元楼层房间号
	private String addressEN;//住址街道号
	private String roomEN;//小区楼号单元楼层房间号

	/*---------------------------------------------------------*/

	private State state = State.DRAFT;

	private List<Option> otherCountry;//其他国家常住居民

	private List<Option> languages;//会说的语言

	private List<Option> visitCountry;//去过的国家

	private List<Option> charitable;//所属公益组织

	private List<Option> finances;//财务证明

	/*---------------------------------------------------------*/

	private List<PhotoDto> photos;// 关联照片

	private List<SchoolDto> schools;//学历

	private List<WorkDto> works;// 工作经历

	private List<FamilyDto> families;//  家人亲戚

	private List<HistoryDto> histories;// 历史出行记录

	private boolean friendInUSA;// 是否有非直系亲属在美国

	/*---------------------------------------------------------*/

	private Spouse spouse;
	private Army army;
	private Usa usa;
	private Passport oldPassport;
	private Travel travel;
	private OldName oldName;

	/*---------------------------------------------------------*/

	private String reason;
	private String files;

	private boolean main;
	private Relation relation;
	private int home;//所属的主申请人的哈市code
	private boolean receipt;
	private String depositSource;//押金来源
	private String depositMethod;//押金方式
	private double depositSum;//押金金额
	private int depositCount = 1;//押金笔数
	private boolean insurance;
	private boolean outDistrict;

	//这些信息都在对应的表中关联着
	private FamilyDto father;
	private FamilyDto mother;
	private WorkDto work;

	public void addWork(WorkDto work) {
		if (work == null) {
			return;
		} else {
			work.setCurrent(true);
		}
		if (works == null) {
			works = Lists.newArrayList(work);
		} else {
			int size = works.size();
			for (int i = 0; i < size; i++) {
				WorkDto w = works.get(i);
				if (w.getId() != null && w.getId().equals(work.getId())) {
					works.set(i, work);
					return;
				}
			}
			works.add(work);
		}
	}

	public void addMother(FamilyDto mother) {
		if (mother == null) {
			return;
		} else {
			mother.setRelation(Relation.MOTHER);
		}
		if (families == null) {
			families = Lists.newArrayList(mother);
		} else {
			int size = families.size();
			for (int i = 0; i < size; i++) {
				FamilyDto f = families.get(i);
				if (f.getId() != null && f.getId().equals(mother.getId())) {
					families.set(i, mother);
					return;
				}
			}
			families.add(mother);
		}
	}

	public void addFather(FamilyDto father) {
		if (father == null) {
			return;
		} else {
			father.setRelation(Relation.FATHER);
		}
		if (families == null) {
			families = Lists.newArrayList(father);
		} else {
			int size = families.size();
			for (int i = 0; i < size; i++) {
				FamilyDto f = families.get(i);
				if (f.getId() != null && f.getId().equals(father.getId())) {
					families.set(i, father);
					return;
				}
			}
			families.add(father);
		}
	}

	public WorkDto getWork() {
		if (work != null)
			return work;
		if (works != null) {
			for (WorkDto w : works) {
				if (w.isCurrent()) {
					work = w;
					return w;
				}
			}
		}
		return null;
	}

	public FamilyDto getFather() {
		if (father != null)
			return father;
		if (families != null) {
			for (FamilyDto family : families) {
				if (family.getRelation() == Relation.FATHER) {
					family.setRelation(Relation.FATHER);
					father = family;
					return family;
				}
			}
		}
		return null;
	}

	public FamilyDto getMother() {
		if (mother != null)
			return mother;
		if (families != null) {
			for (FamilyDto family : families) {
				if (family.getRelation() == Relation.MOTHER) {
					family.setRelation(Relation.MOTHER);
					mother = family;
					return family;
				}
			}
		}
		return null;
	}

	public List<FamilyDto> getFamilies() {
		List<FamilyDto> list = new ArrayList<FamilyDto>();
		if (families != null) {
			Iterator<FamilyDto> it = families.iterator();
			while (it.hasNext()) {
				FamilyDto f = it.next();
				if (f.getRelation() != Relation.FATHER && f.getRelation() != Relation.MOTHER) {
					list.add(f);
				}
			}
		}
		return list;
	}

	public List<WorkDto> getWorks() {
		List<WorkDto> list = new ArrayList<WorkDto>();
		if (works != null) {
			Iterator<WorkDto> it = works.iterator();
			while (it.hasNext()) {
				WorkDto w = it.next();
				if (!w.isCurrent()) {
					list.add(w);
				}
			}
		}
		return list;
	}

	public List<Option> getOtherCountry() {
		if (otherCountry == null)
			otherCountry = new ArrayList<Option>();
		return otherCountry;
	}

	public List<HistoryDto> getHistories() {
		if (histories == null)
			histories = new ArrayList<HistoryDto>();
		return histories;
	}

	public int getHome() {
		if (home == 0) {
			home = (lastName + firstName).hashCode();
		}
		return home;
	}

	public String getBirthCityCN() {
		String citys[] = { "北京", "上海", "重庆", "天津" };
		if (birthCityCN == null || StringUtils.containsAny(birthCityCN, citys)) {
			birthCityCN = "";
		}
		return birthCityCN;
	}

	public void setHome(int home) {
		this.home = home;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
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

	public String getIdCardNo() {
		return idCardNo;
	}

	public void setIdCardNo(String idCardNo) {
		this.idCardNo = idCardNo;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getPassport() {
		return passport;
	}

	public void setPassport(String passport) {
		this.passport = passport;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Date getIssueDate() {
		return issueDate;
	}

	public void setIssueDate(Date issueDate) {
		this.issueDate = issueDate;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	public String getIssueProvince() {
		return issueProvince;
	}

	public void setIssueProvince(String issueProvince) {
		this.issueProvince = issueProvince;
	}

	public String getIssueCity() {
		return issueCity;
	}

	public void setIssueCity(String issueCity) {
		this.issueCity = issueCity;
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

	public String getBirthProvinceCN() {
		return birthProvinceCN;
	}

	public void setBirthProvinceCN(String birthProvinceCN) {
		this.birthProvinceCN = birthProvinceCN;
	}

	public void setBirthCityCN(String birthCityCN) {
		this.birthCityCN = birthCityCN;
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

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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

	public String getRoom() {
		return room;
	}

	public void setRoom(String room) {
		this.room = room;
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

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public void setOtherCountry(List<Option> otherCountry) {
		this.otherCountry = otherCountry;
	}

	public List<Option> getLanguages() {
		return languages;
	}

	public void setLanguages(List<Option> languages) {
		this.languages = languages;
	}

	public List<Option> getVisitCountry() {
		return visitCountry;
	}

	public void setVisitCountry(List<Option> visitCountry) {
		this.visitCountry = visitCountry;
	}

	public List<Option> getCharitable() {
		return charitable;
	}

	public void setCharitable(List<Option> charitable) {
		this.charitable = charitable;
	}

	public List<Option> getFinances() {
		return finances;
	}

	public void setFinances(List<Option> finances) {
		this.finances = finances;
	}

	public List<PhotoDto> getPhotos() {
		return photos;
	}

	public void setPhotos(List<PhotoDto> photos) {
		this.photos = photos;
	}

	public List<SchoolDto> getSchools() {
		return schools;
	}

	public void setSchools(List<SchoolDto> schools) {
		this.schools = schools;
	}

	public void setWorks(List<WorkDto> works) {
		this.works = works;
	}

	public void setFamilies(List<FamilyDto> families) {
		this.families = families;
	}

	public void setHistories(List<HistoryDto> histories) {
		this.histories = histories;
	}

	public boolean isFriendInUSA() {
		return friendInUSA;
	}

	public void setFriendInUSA(boolean friendInUSA) {
		this.friendInUSA = friendInUSA;
	}

	public Spouse getSpouse() {
		return spouse;
	}

	public void setSpouse(Spouse spouse) {
		this.spouse = spouse;
	}

	public Army getArmy() {
		return army;
	}

	public void setArmy(Army army) {
		this.army = army;
	}

	public Usa getUsa() {
		return usa;
	}

	public void setUsa(Usa usa) {
		this.usa = usa;
	}

	public Passport getOldPassport() {
		return oldPassport;
	}

	public void setOldPassport(Passport oldPassport) {
		this.oldPassport = oldPassport;
	}

	public OldName getOldName() {
		return oldName;
	}

	public void setOldName(OldName oldName) {
		this.oldName = oldName;
	}

	public Travel getTravel() {
		return travel;
	}

	public void setTravel(Travel travel) {
		this.travel = travel;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getFiles() {
		return files;
	}

	public void setFiles(String files) {
		this.files = files;
	}

	public boolean isMain() {
		return main;
	}

	public void setMain(boolean main) {
		this.main = main;
	}

	public Relation getRelation() {
		return relation;
	}

	public void setRelation(Relation relation) {
		this.relation = relation;
	}

	public boolean isReceipt() {
		return receipt;
	}

	public void setReceipt(boolean receipt) {
		this.receipt = receipt;
	}

	public String getDepositSource() {
		return depositSource;
	}

	public void setDepositSource(String depositSource) {
		this.depositSource = depositSource;
	}

	public String getDepositMethod() {
		return depositMethod;
	}

	public void setDepositMethod(String depositMethod) {
		this.depositMethod = depositMethod;
	}

	public double getDepositSum() {
		return depositSum;
	}

	public void setDepositSum(double depositSum) {
		this.depositSum = depositSum;
	}

	public int getDepositCount() {
		return depositCount;
	}

	public void setDepositCount(int depositCount) {
		this.depositCount = depositCount;
	}

	public boolean isInsurance() {
		return insurance;
	}

	public void setInsurance(boolean insurance) {
		this.insurance = insurance;
	}

	public boolean isOutDistrict() {
		return outDistrict;
	}

	public void setOutDistrict(boolean outDistrict) {
		this.outDistrict = outDistrict;
	}

	public void setFather(FamilyDto father) {
		this.father = father;
	}

	public void setMother(FamilyDto mother) {
		this.mother = mother;
	}

	public void setWork(WorkDto work) {
		this.work = work;
	}
}