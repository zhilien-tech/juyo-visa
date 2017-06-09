package io.znz.jsite.core.bean;

import static javax.persistence.GenerationType.IDENTITY;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Formula;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 区域entity
 */
@Entity
@Table(name = "sys_area")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@DynamicUpdate
@DynamicInsert
public class Area implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "ID", unique = true, nullable = false)
    private Integer id;
    //区号
    @Column(name = "CITY_CODE")
    private String cityCode;
    //邮编
    @Column(name = "ZIP_CODE")
    private String zipCode;
    //城市名字
    @Column(name = "NAME", nullable = false, length = 50)
    private String name;
    //城市简称
    @Column(name = "SHORT_NAME", nullable = false, length = 50)
    private String shortName;
    //全称
    @Column(name = "MERGER_NAME", length = 100)
    private String mergerName;
    //精度
    @Column(name = "LNG")
    private Double lng;
    //纬度
    @Column(name = "LAT")
    private Double lat;
    //拼音码
    @Column(name = "PINYIN", length = 100)
    private String pinyin;
    //层级
    @Column(name = "LEVEL")
    private int level;
    //父级
    @JoinColumn(name = "PID")
    @ManyToOne
    private Area parent;

    @Formula("(SELECT if(count(area.id)>0,'1','0') FROM sys_area area WHERE area.pid = id)")
    private boolean state;

    /** default constructor */
    public Area() {}

    /** full constructor */
    public Area(String cityCode, String zipCode, String name, String shortName, String mergerName, Double lng,
        Double lat, String pinyin, int level, Area parent
    ) {
        this.cityCode = cityCode;
        this.zipCode = zipCode;
        this.name = name;
        this.shortName = shortName;
        this.mergerName = mergerName;
        this.lng = lng;
        this.lat = lat;
        this.pinyin = pinyin;
        this.level = level;
        this.parent = parent;
    }

    // Property accessors
    public int getPid() {
        return parent == null ? 0 : parent.id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
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

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getMergerName() {
        return mergerName;
    }

    public void setMergerName(String mergerName) {
        this.mergerName = mergerName;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public Area getParent() {
        return parent;
    }

    public void setParent(Area parent) {
        this.parent = parent;
    }

    public boolean getState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }
}