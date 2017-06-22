package com.framework.designpattern.adapter;

/**
 * 适配器模式测试
 * 德国旅馆正常只有国标的插座
 * 德表的充电器游客可以正常充电
 * Created by yuanjinglin on 17/6/20.
 */
public class Test {
    public static void main(String[] args){
        //正常德标的入住酒店直接可以充电
        DBInterface dbsocket=new DBSocket();
        Hotel hotel=new Hotel(dbsocket);
        hotel.charge();
        //国标的需要转换成德国标准,不改hotel实现就适配下,变成德标,现在用国标的也可以充电了
        GBInterface gbsocket=new GBSocket();
        SocketAdapter adapter=new SocketAdapter(gbsocket);
        Hotel hotel2=new Hotel(adapter);
        hotel2.charge();
    }
}
