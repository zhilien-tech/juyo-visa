package io.znz.jsite.core.bean;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.Entity;
import javax.persistence.EntityResult;
import javax.persistence.FieldResult;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;
import javax.persistence.Transient;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * 权限 Permissione
 *
 * @author Chaly
 */
@Entity
@Table(name = "sys_permission")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@DynamicUpdate
@DynamicInsert
@SqlResultSetMapping(name = "PermissionResult",
    entities = {
        @EntityResult(entityClass = Permission.class, fields = {
            @FieldResult(name = "id", column = "ID"),
            @FieldResult(name = "parent", column = "PID"),
            @FieldResult(name = "name", column = "NAME"),
            @FieldResult(name = "type", column = "TYPE"),
            @FieldResult(name = "path", column = "PATH"),
            @FieldResult(name = "sort", column = "SORT"),
            @FieldResult(name = "url", column = "URL"),
            @FieldResult(name = "icon", column = "ICON"),
            @FieldResult(name = "code", column = "CODE"),
            @FieldResult(name = "color", column = "COLOR"),
            @FieldResult(name = "description", column = "DESCRIPTION"),
        })
    },
    columns = {
        @ColumnResult(name = "state"),
    }
)
public class Permission implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "ID", unique = true, nullable = false)
    private Integer id;
    //父级
    @JoinColumn(name = "PID")
    @ManyToOne
    private Permission parent;
    @Column(name = "NAME", nullable = false, length = 50)
    private String name;
    //菜单权限标识名
    @Column(name = "PATH", length = 50)
    private String path;
    @Column(name = "TYPE", length = 20)
    private String type;
    @Column(name = "SORT")
    private Integer sort;
    @Column(name = "URL")
    private String url;
    @Column(name = "ICON")
    private String icon;
    @Column(name = "COLOR")
    private String color;
    @Column(name = "CODE", length = 50)
    private String code;
    @Column(name = "DESCRIPTION", length = 65535)
    private String description;
    @Transient
    private String state;

    public Permission() {
    }

    public Permission(Integer id) {
        this.id = id;
    }

    public Permission(Permission parent, String name, String type, String url, String code) {
        this.parent = parent;
        this.name = name;
        this.type = type;
        this.url = url;
        this.code = code;
    }

    public int getPid() {
        return parent == null ? 0 : parent.id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Permission getParent() {
        return parent;
    }

    public void setParent(Permission parent) {
        this.parent = parent;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

}
