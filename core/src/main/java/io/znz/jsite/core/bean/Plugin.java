package io.znz.jsite.core.bean;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
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
import java.util.Date;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * Created by Chaly on 15/10/30.
 */

@Entity
@Table(name = "sys_plugin")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@DynamicUpdate
@DynamicInsert
public class Plugin implements java.io.Serializable {

    // Fields
    @Transient
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "ID", unique = true, nullable = false)
    private Integer id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "PATH")
    private String path;

    @Column(name = "AUTHOR")
    private String author;

    @Column(name = "USED")
    private boolean used;

    @Column(name = "ROOT")
    private String root;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "UPLOAD_TIME", length = 19)
    private Date uploadTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "INSTALL_TIME", length = 19)
    private Date installTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "UNINSTALL_TIME", length = 19)
    private Date uninstallTime;

    @Column(name = "FILE_CONFLICT")
    private boolean fileConflict;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "PLUG_PERMS")
    private String plugPerms;

    public boolean getCanInstall() {
        if (!isUsed() && !isFileConflict()) {
            return true;
        } else {
            return false;
        }
    }

    public boolean getCanUnInstall() {
        if (isUsed() && !isFileConflict()) {
            return true;
        } else {
            return false;
        }
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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    public String getRoot() {
        return root;
    }

    public void setRoot(String root) {
        this.root = root;
    }

    public Date getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(Date uploadTime) {
        this.uploadTime = uploadTime;
    }

    public Date getInstallTime() {
        return installTime;
    }

    public void setInstallTime(Date installTime) {
        this.installTime = installTime;
    }

    public Date getUninstallTime() {
        return uninstallTime;
    }

    public void setUninstallTime(Date uninstallTime) {
        this.uninstallTime = uninstallTime;
    }

    public boolean isFileConflict() {
        return fileConflict;
    }

    public void setFileConflict(boolean fileConflict) {
        this.fileConflict = fileConflict;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPlugPerms() {
        return plugPerms;
    }

    public void setPlugPerms(String plugPerms) {
        this.plugPerms = plugPerms;
    }
}
