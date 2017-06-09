package io.znz.jsite.core.bean;

import java.io.Serializable;

/**
 * Created by Chaly on 15/11/18.
 */
public class UrlChain implements Serializable {
    private int id;
    private String name; //url名称/描述
    private String url; //地址

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPermissions() {
        return permissions;
    }

    public void setPermissions(String permissions) {
        this.permissions = permissions;
    }

    private String roles; //所需要的角色，可省略
    private String permissions; //所需要的权限，可省略

}
