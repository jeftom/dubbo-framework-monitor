package com.framework.designpattern.abstractfactory;

/**
 * Created by yuanjinglin on 17/7/5.
 */
public class Test {
    public static void main(String[] args){
        AbstractFactory Beazfactory=new BenzFactory();
        AbstractFactory BMWfactory=new BMWFactory();
        Beazfactory.creatDoor();
        BMWfactory.creatDoor();
    }
}
