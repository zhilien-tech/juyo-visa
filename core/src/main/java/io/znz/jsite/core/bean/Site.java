package io.znz.jsite.core.bean;


import io.znz.jsite.core.util.Const;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "conf_site")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@DynamicUpdate
@DynamicInsert
public class Site {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "ID", unique = true, nullable = false)
    private Integer id;
    @Column(name = "NAME")
    private String name;
    @Column(name = "LOGO")
    private String logo;
    @Column(name = "VERSION")
    private String version;
    @Column(name = "PATH")
    private String path;
    @Column(name = "DOMAIN")
    private String domain;
    @Column(name = "PROTOCOL")
    private String protocol;
    @Column(name = "REDIRECT")
    private String redirect;
    @Column(name = "ALIAS")
    private String alias;
    @Column(name = "KEYWORDS")
    private String keywords;
    @Column(name = "SHORT_NAME")
    private String shortName;
    /** 是否显示技术支持 */
    @Column(name = "SHOW_POWERED")
    private boolean showPowered = true;
    /** 联系地址 */
    @Column(name = "ADDRESS")
    private String address;
    /** 联系电话 */
    @Column(name = "PHONE")
    private String phone;
    /** 邮政编码 */
    @Column(name = "ZIP_CODE")
    private String zipCode;
    /** E-mail */
    @Column(name = "EMAIL")
    private String email;
    /** 备案编号 */
    @Column(name = "CERTTEXT")
    private String certtext;
    /** 是否网站开启 */
    @Column(name = "IS_CLOSE")
    private boolean isClose = false;
    /** 网站关闭消息 */
    @Column(name = "CLOSE_MESSAGE")
    private String closeMessage;

    //验证方式,默认不验证
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "VERIFY_TYPE")
    private Dict verify;

    /**
     * 获得模板路径。如：/WEB-INF/t/cms/www
     */
    public String getTplPath() {
        return Const.TPL_PATH + "/" + getPath();
    }

    /**
     * 获得模板资源路径。如：/r/cms/www
     */
    public String getResPath() {
        return Const.RES_PATH + "/" + getPath();
    }

    /**
     * 获得上传路径。如：/u/jeecms/path
     */
    public String getUploadPath() {
        return Const.UPLOAD_PATH + getPath();
    }

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

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getRedirect() {
        return redirect;
    }

    public void setRedirect(String redirect) {
        this.redirect = redirect;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public boolean isShowPowered() {
        return showPowered;
    }

    public void setShowPowered(boolean showPowered) {
        this.showPowered = showPowered;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCerttext() {
        return certtext;
    }

    public void setCerttext(String certtext) {
        this.certtext = certtext;
    }

    public boolean isClose() {
        return isClose;
    }

    public void setClose(boolean close) {
        isClose = close;
    }

    public String getCloseMessage() {
        return closeMessage;
    }

    public void setCloseMessage(String closeMessage) {
        this.closeMessage = closeMessage;
    }

    public Dict getVerify() {
        return verify;
    }

    public void setVerify(Dict verify) {
        this.verify = verify;
    }
}
