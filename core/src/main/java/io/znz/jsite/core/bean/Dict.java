package io.znz.jsite.core.bean;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * 字典 entity
 * @author Chaly
 */
@Entity
@Table(name = "sys_dict")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@DynamicUpdate
@DynamicInsert
public class Dict implements java.io.Serializable {

    // Fields
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "ID", unique = true, nullable = false)
    private Integer id;
    @Column(name = "LABEL")
    private String label;
    @Column(name = "VALUE")
    private String value;
    @Column(name = "`KEY`")
    private String key;
    @Column(name = "CATEGORY")
    private String category;
    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "SORT")
    private Integer sort;
    @Column(name = "REMARK")
    private String remark;
    @Column(name = "DEL_FLAG")
    private boolean delFlag;

    // Constructors

    /** default constructor */
    public Dict() {
    }

    public Dict(Integer id) {
        this.id = id;
    }

    /** full constructor */
    public Dict(String label, String value, String key, String description,
                Integer sort, String remark, boolean delFlag) {
        this.label = label;
        this.value = value;
        this.key = key;
        this.description = description;
        this.sort = sort;
        this.remark = remark;
        this.delFlag = delFlag;
    }

    // Property accessors


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public boolean isDelFlag() {
        return delFlag;
    }

    public void setDelFlag(boolean delFlag) {
        this.delFlag = delFlag;
    }
}