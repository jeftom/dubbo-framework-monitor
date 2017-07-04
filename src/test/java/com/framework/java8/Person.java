package com.framework.java8;

/**
 * Created by yuanjinglin on 17/7/3.
 */
public class Person {
    private String name;

    public Person(String name) {
        this.name = name;
    }
    public Person(User user) {
        this.name = user.getName();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
