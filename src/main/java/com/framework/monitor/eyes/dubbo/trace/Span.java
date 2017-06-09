package com.framework.monitor.eyes.dubbo.trace;

/**
 * Created by yuanjinglin on 17/6/9.
 */
public class Span {
    /**
     * 调用全局id
     */
    private String traceId;

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }
}
