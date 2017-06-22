package com.framework.designpattern.adapter;

/**
 * 旅馆
 * Created by yuanjinglin on 17/6/20.
 */
public class Hotel {
    /**
     * 插座
     */
    private DBInterface dbsocket;
    public Hotel(){}
    public Hotel(DBInterface dbsocket){
        this.dbsocket=dbsocket;
    }

    /**
     * 充电
     */
    public void charge(){
        dbsocket.powerWithTwoRound();
    }
}
