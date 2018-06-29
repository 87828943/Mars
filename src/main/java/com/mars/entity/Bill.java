package com.mars.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name="mars_bill")
public class Bill extends BaseEntity{

    //账单类型
    @Column(name="bill_type")
    private Integer billType;

    //备注
    @Column(name="remark")
    private String remark;

    //userId
    @Column(name="user_id")
    private Long userId;

    //金额
    @Column(name="money",precision=12, scale=2)
    private BigDecimal money;

    public Integer getBillType() {
        return billType;
    }

    public void setBillType(Integer billType) {
        this.billType = billType;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }
}
