package com.framework.designpattern.builder;

/**
 * Created by yuanjinglin on 17/7/5.
 */
public interface PersonBuilder {
    void buildHead();
    void buildBody();
    void buildFoot();
    Person buildPerson();
}
