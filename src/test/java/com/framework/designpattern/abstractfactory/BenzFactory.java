package com.framework.designpattern.abstractfactory;

/**
 * Created by yuanjinglin on 17/7/5.
 */
public class BenzFactory extends AbstractFactory {
    @Override
    public Door creatDoor() {
        return new BenzDoor();
    }

    @Override
    public Wheel creatWheel() {
        return new BenzWheel();
    }
}
