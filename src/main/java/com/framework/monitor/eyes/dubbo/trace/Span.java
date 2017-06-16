package com.framework.monitor.eyes.dubbo.trace;

/**
 * Created by yuanjinglin on 17/6/9.
 */
public class Span {
    /**
     * 调用全局id
     */
    private String traceId;
    private  String name;
    private String applicationName;

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }
}
