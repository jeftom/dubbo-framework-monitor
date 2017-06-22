package com.framework.designpattern.adapter;

/**
 * 国标充电器
 * Created by yuanjinglin on 17/6/20.
 */
public class GBSocket implements GBInterface {


    @Override
    public void powerWithThreeRound() {
        System.out.println("使用国标3孔充电电");
    }
}
