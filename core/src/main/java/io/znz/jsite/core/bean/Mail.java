package io.znz.jsite.core.bean;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SelectBeforeUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * Created by Chaly on 15/11/10.
 */
@Entity
@Table(name = "conf_mail")
@DynamicUpdate
@DynamicInsert
@SelectBeforeUpdate
public class Mail {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "ID", unique = true, nullable = false)
    private Integer id;
    @Column(name = "HOST", nullable = false, length = 50)
    private String host;
    @Column(name = "PORT")
    private int port;
    @Column(name = "USERNAME", nullable = false, length = 50)
    private String username;
    @Column(name = "ADDRESS", nullable = false, length = 50)
    private String address;
    @Column(name = "PASSWORD", length = 50)
    private String password;
    @Column(name = "AVAILABLE")
    private boolean available = true;//1:配置是否可用,默认可用
    @Column(name = "AUTH")
    private boolean auth = false;//1:配置是否验证

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public boolean isAuth() {
        return auth;
    }

    public void setAuth(boolean auth) {
        this.auth = auth;
    }
}
