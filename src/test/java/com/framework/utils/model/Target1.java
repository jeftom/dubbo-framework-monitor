package com.framework.utils.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by 李溪林 on 17-2-14.
 */
public class Target1 extends Basic {

    private Long id;

    private String name;

    private Date startDate;

    private BigDecimal money;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    @Override
    public String toString() {
        return "Target1{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", startDate=" + startDate +
                ", money=" + money +
                '}';
    }
}
