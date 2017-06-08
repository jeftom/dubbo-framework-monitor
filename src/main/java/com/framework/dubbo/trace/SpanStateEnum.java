package com.framework.dubbo.trace;

/**
 * Created by yuanjinglin on 17/6/8.
 */
public enum SpanStateEnum {
    CS(1, "CS"),
    SR(2, "SR"),
    SS(3, "SS"),
    CR(4, "CR");

    private int key;
    private String des;

    SpanStateEnum(int key, String des) {
        this.key = key;
        this.des = des;
    }

    public int getKey() {
        return key;
    }

    public String getDes() {
        return des;
    }
}
