package com.framework.java8;

/**
 * Created by yuanjinglin on 17/7/3.
 */
public class User {
    User(){

    }
    public User(Long id,String name,Integer age,Integer sex){
        this.id=id;
        this.name=name;
        this.age=age;
        this.sex=sex;
    }
    private Long id;
    private String name;
    private Integer age;
    private Integer sex;

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

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

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }


}
