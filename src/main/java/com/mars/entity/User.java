package com.mars.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="mars_user")
public class User extends BaseEntity{

    //账户
    @Column(name="name", nullable = false, unique = true)
    private String name;

    //邮箱
    @Column(name="email", nullable = false, unique = true)
    private String email;

    //加密密码
    @Column(name="password", nullable = false)
    private String password;

    //logo
    @Column(name="logo")
    private String logo;

    //描述
    @Column(name="desc")
    private String desc;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
