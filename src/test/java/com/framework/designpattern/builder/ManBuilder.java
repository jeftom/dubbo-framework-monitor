package com.framework.designpattern.builder;

/**
 * Created by yuanjinglin on 17/7/5.
 */
public class ManBuilder implements PersonBuilder{
    Person person;
    public ManBuilder() {
        person=new Person();
    }

    @Override
    public void buildBody() {
        person.setBody("男人的身体");
    }

    @Override
    public void buildHead() {
        person.setBody("男人的头");
    }

    @Override
    public void buildFoot() {
        person.setBody("男人的脚");
    }

    @Override
    public Person buildPerson() {
        return person;
    }
}
