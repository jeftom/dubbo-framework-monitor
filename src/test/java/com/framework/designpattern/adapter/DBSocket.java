package com.framework.designpattern.adapter;

/**
 * 德标充电器
 * Created by yuanjinglin on 17/6/20.
 */
public class DBSocket implements DBInterface {
    @Override
    public void powerWithTwoRound() {
        System.out.println("使用德标2孔充电电");
    }
}
