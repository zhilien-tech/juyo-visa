package io.znz.jsite.core.bean;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import java.sql.Timestamp;
import java.util.Date;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * 日志entity
 * @author Chaly
 */
@Entity
@Table(name = "sys_log")
@DynamicUpdate
@DynamicInsert
public class Log implements java.io.Serializable {

    @Transient
    private static final long serialVersionUID = 1L;
    // Fields
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "ID", unique = true, nullable = false)
    private Integer id;
    @Column(name = "OPERATION_CODE", nullable = false, length = 50)
    private String operationCode;
    @Column(name = "CREATER", length = 20)
    private String creater;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "CREATE_DATE", nullable = false, length = 19)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Column(name = "TYPE")
    private Integer type;
    @Column(name = "OS", length = 20)
    private String os;
    @Column(name = "BROWSER", length = 20)
    private String browser;
    @Column(name = "IP", length = 20)
    private String ip;
    @Column(name = "MAC", length = 20)
    private String mac;
    @Column(name = "EXECUTE_TIME")
    private Integer executeTime;
    @Column(name = "DESCRIPTION", length = 500)
    private String description;
    @Column(name = "REQUEST_PARAM", length = 500)
    private String requestParam;

    // Constructors

    /** default constructor */
    public Log() {
    }

    /** minimal constructor */
    public Log(String operationCode, Timestamp createDate) {
        this.operationCode = operationCode;
        this.createDate = createDate;
    }

    /** full constructor */
    public Log(String operationCode, String creater, Timestamp createDate,
               Integer type, String os, String browser, String ip, String mac,
               Integer executeTime, String description, String requestParam) {
        this.operationCode = operationCode;
        this.creater = creater;
        this.createDate = createDate;
        this.type = type;
        this.os = os;
        this.browser = browser;
        this.ip = ip;
        this.mac = mac;
        this.executeTime = executeTime;
        this.description = description;
        this.requestParam = requestParam;
    }

    // Property accessors

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOperationCode() {
        return operationCode;
    }

    public void setOperationCode(String operationCode) {
        this.operationCode = operationCode;
    }

    public String getCreater() {
        return creater;
    }

    public void setCreater(String creater) {
        this.creater = creater;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getBrowser() {
        return browser;
    }

    public void setBrowser(String browser) {
        this.browser = browser;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public Integer getExecuteTime() {
        return executeTime;
    }

    public void setExecuteTime(Integer executeTime) {
        this.executeTime = executeTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRequestParam() {
        return requestParam;
    }

    public void setRequestParam(String requestParam) {
        this.requestParam = requestParam;
    }
}