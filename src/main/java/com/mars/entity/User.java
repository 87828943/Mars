package com.mars.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name="mars_user")
public class User extends BaseEntity{

    /**
	 * 
	 */
	private static final long serialVersionUID = -5566658246030079901L;

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
    @Column(name="description")
    private String description;
    
    //性别
    @Column(name="sex")
    private Integer sex = 0;
    //收入
    @Column(name="income",precision=12, scale=2)
    private BigDecimal income;
    
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public BigDecimal getIncome() {
		return income;
	}

	public void setIncome(BigDecimal income) {
		this.income = income;
	}

	

	
	/*@Override
	public String toString() {
		return "name=" + name + "#email=" + email + "#password=" + password + "#logo=" + logo + "#description=" + description;
	}*/
    
}
