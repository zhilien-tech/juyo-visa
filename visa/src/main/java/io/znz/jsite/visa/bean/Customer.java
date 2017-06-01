package io.znz.jsite.visa.bean;

import com.google.common.collect.Lists;
import io.znz.jsite.util.StringUtils;
import io.znz.jsite.visa.bean.helper.Gender;
import io.znz.jsite.visa.bean.helper.Relation;
import io.znz.jsite.visa.bean.helper.State;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.*;

import javax.persistence.CascadeType;
import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Chaly on 2017/3/6.
 */
@Entity
@Table(name = "visa_customer")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@DynamicUpdate
@DynamicInsert
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "idGenerator")
    @GenericGenerator(name = "idGenerator", strategy = "io.znz.jsite.base.bean.TableIdGenerator",
        parameters = {
            @org.hibernate.annotations.Parameter(name = "table_name", value = "sys_id_generator"),
            @org.hibernate.annotations.Parameter(name = "value_column_name", value = "ID_VALUE"),
            @org.hibernate.annotations.Parameter(name = "segment_column_name", value = "ID_NAME"),
            @org.hibernate.annotations.Parameter(name = "segment_value", value = "KEY_VISA_CUSTOMER")
        }
    )
    private Long id;
    //创建时间
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_date")
    private Date createDate = new Date();
    @Column(name = "last_name", columnDefinition = "varchar(255) COMMENT '中文名'")
    private String lastName;
    @Column(name = "first_name", columnDefinition = "varchar(255) COMMENT '中文姓'")
    private String firstName;
    @Column(name = "last_name_en", columnDefinition = "varchar(255) COMMENT '英文名'")
    private String lastNameEN;
    @Column(name = "first_name_en", columnDefinition = "varchar(255) COMMENT '英文姓'")
    private String firstNameEN;

    @Column(name = "id_card_no")
    private String idCardNo;//身份证号
    @Temporal(TemporalType.DATE)
    private Date birthday;//生日
    @Enumerated(value = EnumType.STRING)
    private Gender gender;//性别

    @Column(columnDefinition = "varchar(50) COMMENT '护照号'")
    private String passport;//护照号
    @Column(columnDefinition = "varchar(50) COMMENT '护照类型'")
    private String type = "普通";
    @Column(columnDefinition = "varchar(50) COMMENT '护照国家码'")
    private String code = "CHN";
    //签发时间
    @Temporal(TemporalType.DATE)
    @Column(name = "issue_date")
    private Date issueDate;
    //过期时间
    @Temporal(TemporalType.DATE)
    @Column(name = "expiry_date")
    private Date expiryDate;
    @Column(name = "issue_province")
    private String issueProvince;//护照签发省份拼音
    @Column(name = "issue_city")
    private String issueCity;//护照签发城市拼音

    @Column(name = "birth_country")
    private String birthCountry;//出生国家
    @Column(name = "birth_province")
    private String birthProvince;//出生省份拼音
    @Column(name = "birth_city")
    private String birthCity;//出生城市拼音

    @Column(name = "birth_province_cn")
    private String birthProvinceCN;//出生省份
    @Column(name = "birth_city_cn")
    private String birthCityCN;//出生城市

    private String country;//国籍
    private String province;//省份
    private String city;//城市

    private String mobile;// 手机号
    private String phone;//座机号
    private String email;//邮箱

    @Column(name = "zip_code")
    private String zipCode;//邮编
    private String address;//住址街道号
    private String room;//小区楼号单元楼层房间号
    @Column(name = "address_en")
    private String addressEN;//住址街道号
    @Column(name = "room_en")
    private String roomEN;//小区楼号单元楼层房间号

    @Enumerated(value = EnumType.STRING)
    @Column(name = "state", columnDefinition = "varchar(255) COMMENT '订单状态'")
    private State state = State.DRAFT;

    @ElementCollection
    @CollectionTable(name = "visa_customer_other_country", joinColumns = @JoinColumn(name = "customer_id"))
    private List<Option> otherCountry;//其他国家常住居民

    @ElementCollection
    @CollectionTable(name = "visa_customer_language", joinColumns = @JoinColumn(name = "customer_id"))
    private List<Option> languages;//会说的语言

    @ElementCollection
    @CollectionTable(name = "visa_customer_visit_country", joinColumns = @JoinColumn(name = "customer_id"))
    private List<Option> visitCountry;//去过的国家

    @ElementCollection
    @CollectionTable(name = "visa_customer_charitable", joinColumns = @JoinColumn(name = "customer_id"))
    private List<Option> charitable;//所属公益组织

    @ElementCollection
    @CollectionTable(name = "visa_customer_finance", joinColumns = @JoinColumn(name = "customer_id"))
    private List<Option> finances;//财务证明

    @OneToMany(mappedBy = "id.customer")
    private List<Photo> photos;// 关联照片

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "customer_id")
    private List<School> schools;//学历

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "customer_id")
    private List<Work> works;// 工作经历

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "customer_id")
    private List<Family> families;//  家人亲戚

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "customer_id")
    @OrderBy("arrivalDate DESC")
    private List<History> histories;// 历史出行记录

    @Type(type = "yes_no")
    @Column(name = "friend_in_usa", columnDefinition = "char(1) DEFAULT 'N' COMMENT '有非直系亲属在美国'")
    private boolean friendInUSA;// 有非直系亲属在美国

    @OneToOne(cascade = CascadeType.ALL)
    private Spouse spouse;
    @OneToOne(cascade = CascadeType.ALL)
    private Army army;
    @OneToOne(cascade = CascadeType.ALL)
    private Usa usa;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "old_passport_id", referencedColumnName = "id")
    private Passport oldPassport;
    @Embedded
    private OldName oldName;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "customer")
    private Travel travel;
    @Lob
    private String reason;
    @Lob
    private String files;

    @Type(type = "yes_no")
    @Column(columnDefinition = "char(1) DEFAULT 'N' COMMENT '是否是主申请人'")
    private boolean main;
    @Enumerated(EnumType.STRING)
    @Column(name = "main_relation", columnDefinition = "varchar(255) COMMENT '如果是非主申请人，则显示和主申请人的关系'")
    private Relation relation;
    private int home;//所属的主申请人的哈市code
    @Type(type = "yes_no")
    @Column(columnDefinition = "char(1) DEFAULT 'N' COMMENT '是否有收据'")
    private boolean receipt;
    @Column(name = "deposit_source")
    private String depositSource;//押金来源
    @Column(name = "deposit_method")
    private String depositMethod;//押金方式
    @Min(0)
    @Column(name = "deposit_sum", columnDefinition = "double DEFAULT 0 COMMENT '押金金额'")
    private double depositSum;//押金金额
    @Min(1)
    @Column(name = "deposit_count", columnDefinition = "int(11) DEFAULT 1 COMMENT '押金笔数'")
    private int depositCount = 1;//押金笔数
    @Type(type = "yes_no")
    @Column(columnDefinition = "char(1) DEFAULT 'N' COMMENT '是否有保险'")
    private boolean insurance;
    @Type(type = "yes_no")
    @Column(name = "out_district", columnDefinition = "char(1) DEFAULT 'N' COMMENT '是否是外领区'")
    private boolean outDistrict;

    //这些信息都在对应的表中关联着
    @Transient
    private Family father;
    @Transient
    private Family mother;
    @Transient
    private Work work;

    public void addWork(Work work) {
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
                Work w = works.get(i);
                if (w.getId() != null && w.getId().equals(work.getId())) {
                    works.set(i, work);
                    return;
                }
            }
            works.add(work);
        }
    }

    public void addMother(Family mother) {
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
                Family f = families.get(i);
                if (f.getId() != null && f.getId().equals(mother.getId())) {
                    families.set(i, mother);
                    return;
                }
            }
            families.add(mother);
        }
    }

    public void addFather(Family father) {
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
                Family f = families.get(i);
                if (f.getId() != null && f.getId().equals(father.getId())) {
                    families.set(i, father);
                    return;
                }
            }
            families.add(father);
        }
    }

    public Work getWork() {
        if (work != null)
            return work;
        if (works != null) {
            for (Work w : works) {
                if (w.isCurrent()) {
                    work = w;
                    return w;
                }
            }
        }
        return null;
    }

    public Family getFather() {
        if (father != null)
            return father;
        if (families != null) {
            for (Family family : families) {
                if (family.getRelation() == Relation.FATHER) {
                    family.setRelation(Relation.FATHER);
                    father = family;
                    return family;
                }
            }
        }
        return null;
    }

    public Family getMother() {
        if (mother != null)
            return mother;
        if (families != null) {
            for (Family family : families) {
                if (family.getRelation() == Relation.MOTHER) {
                    family.setRelation(Relation.MOTHER);
                    mother = family;
                    return family;
                }
            }
        }
        return null;
    }

    public List<Family> getFamilies() {
        List<Family> list = new ArrayList<Family>();
        if (families != null) {
            Iterator<Family> it = families.iterator();
            while (it.hasNext()) {
                Family f = it.next();
                if (f.getRelation() != Relation.FATHER && f.getRelation() != Relation.MOTHER) {
                    list.add(f);
                }
            }
        }
        return list;
    }

    public List<Work> getWorks() {
        List<Work> list = new ArrayList<Work>();
        if (works != null) {
            Iterator<Work> it = works.iterator();
            while (it.hasNext()) {
                Work w = it.next();
                if (!w.isCurrent()) {
                    list.add(w);
                }
            }
        }
        return list;
    }

    public List<Option> getOtherCountry() {
        if (otherCountry == null) otherCountry = new ArrayList<Option>();
        return otherCountry;
    }

    public List<History> getHistories() {
        if (histories == null) histories = new ArrayList<History>();
        return histories;
    }

    public int getHome() {
        if (home == 0) {
            home = (lastName + firstName).hashCode();
        }
        return home;
    }

    public String getBirthCityCN() {
        String citys[] = {"北京", "上海", "重庆", "天津"};
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

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
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

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
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

    public List<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }

    public List<School> getSchools() {
        return schools;
    }

    public void setSchools(List<School> schools) {
        this.schools = schools;
    }

    public void setWorks(List<Work> works) {
        this.works = works;
    }

    public void setFamilies(List<Family> families) {
        this.families = families;
    }

    public void setHistories(List<History> histories) {
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

    public void setFather(Family father) {
        this.father = father;
    }

    public void setMother(Family mother) {
        this.mother = mother;
    }

    public void setWork(Work work) {
        this.work = work;
    }
}
