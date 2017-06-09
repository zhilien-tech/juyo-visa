package io.znz.jsite.core.bean;

import com.alibaba.fastjson.annotation.JSONField;
import io.znz.jsite.util.EncryptUtil;
import io.znz.jsite.util.StringUtils;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * 用户entity
 *
 * @author Chaly
 */
@Entity
@Table(name = "sys_user")
@DynamicUpdate
@DynamicInsert
@Embeddable
public class User implements java.io.Serializable {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "ID", unique = true, nullable = false)
    private String id;
    @Column(name = "LOGIN_NAME", nullable = false, length = 200, unique = true)
    private String loginName;
    @Column(name = "NAME", nullable = false, length = 20)
    private String name;
    @JSONField(serialize = false)
    @Column(name = "PASSWORD", nullable = false)
    private String password;
    @JSONField(serialize = false)
    @Column(name = "SALT")
    private String salt;
    @Column(name = "BIRTHDAY", length = 19)
    private Timestamp birthday;
    @Column(name = "GENDER", columnDefinition = "smallint(6) DEFAULT 0")
    private Short gender;
    @Column(name = "EMAIL", length = 50)
    private String email;
    @Column(name = "PHONE", length = 20)
    private String phone;
    @Column(name = "AVATAR", length = 500)
    private String avatar;
    @Column(name = "CITY", length = 255, columnDefinition = "varchar(255) DEFAULT '北京'")
    private String city;
    @Column(name = "CREATE_DATE", length = 19)
    private Timestamp createDate;
    @Column(name = "STATE", length = 1)
    private String state;
    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "LOGIN_COUNT")
    private Integer loginCount;
    @Column(name = "PREVIOUS_VISIT", length = 19)
    private Timestamp previousVisit;
    @Column(name = "LAST_VISIT", length = 19)
    private Timestamp lastVisit;
    @Column(name = "DEL_FLAG", length = 1)
    private String delFlag;
    @JSONField(serialize = false)
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user")
    private Set<UserRole> userRoles = new HashSet<UserRole>(0);

    /**
     * default constructor
     */
    public User() {
    }

    public User(String id) {
        this.id = id;
    }

    /**
     * minimal constructor
     */
    public User(String loginName, String name, String password) {
        this.loginName = loginName;
        this.name = name;
        this.password = password;
    }

    //获取加密的手机号
    public String getEncryptPhone() {
        return EncryptUtil.encryptString(phone);
    }

    public String getRoleName() {
        Iterator<UserRole> iterator = getUserRoles().iterator();
        StringBuilder sb = new StringBuilder();
        while (iterator.hasNext()) {
            sb.append(iterator.next().getRole().getName()).append("/");
        }
        return StringUtils.removeEnd(sb.toString(), "/");
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public Timestamp getBirthday() {
        return birthday;
    }

    public void setBirthday(Timestamp birthday) {
        this.birthday = birthday;
    }

    public Short getGender() {
        return gender;
    }

    public void setGender(Short gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Timestamp getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getLoginCount() {
        return loginCount;
    }

    public void setLoginCount(Integer loginCount) {
        this.loginCount = loginCount;
    }

    public Timestamp getPreviousVisit() {
        return previousVisit;
    }

    public void setPreviousVisit(Timestamp previousVisit) {
        this.previousVisit = previousVisit;
    }

    public Timestamp getLastVisit() {
        return lastVisit;
    }

    public void setLastVisit(Timestamp lastVisit) {
        this.lastVisit = lastVisit;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public Set<UserRole> getUserRoles() {
        return userRoles;
    }

    public void setUserRoles(Set<UserRole> userRoles) {
        this.userRoles = userRoles;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
