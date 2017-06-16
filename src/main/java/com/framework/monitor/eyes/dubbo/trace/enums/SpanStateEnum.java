package com.framework.monitor.eyes.dubbo.trace.enums;

/**
 * Created by yuanjinglin on 17/6/8.
 */
public enum SpanStateEnum {
    CS("CS", "CS"),
    SR("SR", "SR"),
    SS("SS", "SS"),
    CR("CR", "CR");

    private String key;
    private String des;

    SpanStateEnum(String key, String des) {
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
