package com.framework.designpattern.abstractfactory;

/**
 * Created by yuanjinglin on 17/7/5.
 */
public class BMWFactory extends AbstractFactory{
    @Override
    public Door creatDoor() {
        return new BMWDoor();
    }

    @Override
    public Wheel creatWheel() {
        return new BenzWheel();
    }
}
