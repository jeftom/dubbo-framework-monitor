package com.framework.dubbo.trace;

import com.framework.utils.GsonUtil;
import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by yuanjinglin on 17/6/8.
 */
public class Span implements Serializable {
    private static final long serialVersionUID = -1914718972378147321L;
    private String applicationName;
    /**
     * 调用全局id
     */
    private String traceId;
    /**
     * 每一次调用都认为是一个span
     */
    private String spanId;
    /**
     * 父级id,用于构建调用链树
     */
    private String parentId;

    private Long cs;

    private Long sr;

    private Long cr;

    private Long ss;

    private String ip;

    private Integer state;

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getSpanId() {
        return spanId;
    }

    public void setSpanId(String spanId) {
        this.spanId = spanId;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public Long getCs() {
        return cs;
    }

    public void setCs(Long cs) {
        this.cs = cs;
    }

    public Long getSr() {
        return sr;
    }

    public void setSr(Long sr) {
        this.sr = sr;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Long getCr() {
        return cr;
    }

    public void setCr(Long cr) {
        this.cr = cr;
    }

    public Long getSs() {
        return ss;
    }

    public void setSs(Long ss) {
        this.ss = ss;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return GsonUtil.toJson(this);
    }
}
