package com.framework.designpattern.builder;

/**
 * Created by yuanjinglin on 17/7/5.
 */
public class PersonDirector {
    public Person constructPerson(PersonBuilder builder){
        builder.buildHead();
        builder.buildBody();
        builder.buildFoot();
        return builder.buildPerson();
    }
}
