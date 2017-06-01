package com.framework.utils.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by 李溪林 on 17-2-14.
 */
public class Special1 {

    private long id;

    private String name;

    private Date startDate;

    private BigDecimal money;

    private Integer age;

    private int count;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
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

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "Special1{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", startDate=" + startDate +
                ", money=" + money +
                ", age=" + age +
                ", count=" + count +
                '}';
    }
}
