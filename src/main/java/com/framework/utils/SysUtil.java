package com.framework.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by yuanjinglin on 17/6/15.
 */
public class SysUtil {
    /**
     * 获取工程名
     * @return
     */
    public static String getProjectName(){
        String[] names=System.getProperty("user.dir").split("/");
        return names[names.length-1];
    }

    /**
     * 获取本机IP
     * @return
     */
    public static String getLocalIp(){
        InetAddress localhost = null;
        try {
            localhost = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return localhost.getHostAddress();
    }

    public static void main(String[] args){
        getProjectName();
        getLocalIp();
    }
}
