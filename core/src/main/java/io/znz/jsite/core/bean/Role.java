package io.znz.jsite.core.bean;

import com.alibaba.fastjson.annotation.JSONField;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.HashSet;
import java.util.Set;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * 角色entity
 * @author Chaly
 */
@Entity
@Table(name = "sys_role")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@DynamicUpdate
@DynamicInsert
public class Role implements java.io.Serializable {

    // Fields
    @Transient
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "ID", unique = true, nullable = false)
    private Integer id;
    @Column(name = "NAME", nullable = false, length = 20)
    private String name;
    @Column(name = "HOME")
    private String home;
    @Column(name = "ROLE_CODE", nullable = false, length = 20, unique = true)
    private String roleCode;
    @Column(name = "DESCRIPTION", length = 65535)
    private String description;
    @Column(name = "SORT")
    private Short sort;
    @Column(name = "DEL_FLAG", length = 1)
    private String delFlag;
    @JSONField(serialize = false)
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "role")
    private Set<UserRole> userRoles = new HashSet<UserRole>(0);
    @JSONField(serialize = false)
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "role")
    private Set<RolePermission> rolePermsses = new HashSet<RolePermission>(0);

    // Constructors

    /** default constructor */
    public Role() {
    }

    public Role(Integer id) {
        this.id = id;
    }

    /** minimal constructor */
    public Role(String name, String roleCode) {
        this.name = name;
        this.roleCode = roleCode;
    }

    /** full constructor */
    public Role(String name, String roleCode, String description, Short sort,
                String delFlag, Set<UserRole> userRoles,
                Set<RolePermission> rolePermsses) {
        this.name = name;
        this.roleCode = roleCode;
        this.description = description;
        this.sort = sort;
        this.delFlag = delFlag;
        this.userRoles = userRoles;
        this.rolePermsses = rolePermsses;
    }

    // Property accessors


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHome() {
        return home;
    }

    public void setHome(String home) {
        this.home = home;
    }

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Short getSort() {
        return sort;
    }

    public void setSort(Short sort) {
        this.sort = sort;
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

    public Set<RolePermission> getRolePermsses() {
        return rolePermsses;
    }

    public void setRolePermsses(Set<RolePermission> rolePermsses) {
        this.rolePermsses = rolePermsses;
    }
}
