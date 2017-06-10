package com.framework.monitor.eyes.dubbo.trace;

/**
 * Created by yuanjinglin on 17/6/8.
 */
public enum RPCResultEnum {
    TIMEOUT("TIMEOUT", "TIMEOUT"),
    ERROR("ERROR", "ERROR"),
    OK("OK", "OK"),
    FAILD("FAILD", "FAILD");

    private String key;
    private String des;

    RPCResultEnum(String key, String des) {
        this.key = key;
        this.des = des;
    }

    public String getKey() {
        return key;
    }

    public String getDes() {
        return des;
    }
}
