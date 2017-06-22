package com.framework.designpattern.adapter;

/**
 * 适配器
 * Created by yuanjinglin on 17/6/19.
 */
public class SocketAdapter implements DBInterface {
    private GBInterface gbStocket;

    public SocketAdapter(GBInterface gbStocket){
        this.gbStocket=gbStocket;
    }
    @Override
    public void powerWithTwoRound() {
        gbStocket.powerWithThreeRound();
    }
}
