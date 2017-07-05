package com.framework.designpattern.builder;

/**
 * Created by yuanjinglin on 17/7/5.
 */
public class Test {
    public static void main(String[] args){
        //造人的builder,定义了创建人体部分的各个方法
        PersonBuilder personBuilder=new ManBuilder();
        //构建类,定义了怎么创造人
        PersonDirector personDirector=new PersonDirector();
        //创建并返回返回人
        Person person=personDirector.constructPerson(personBuilder);
    }

}
