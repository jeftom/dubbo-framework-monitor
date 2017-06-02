package com.framework.dubbo.trace;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by yuanjinglin on 17/6/2.
 */
public class RpcTraceInfo implements Serializable{
    private static final long serialVersionUID = -5452491506958823224L;
    private String ip;
    private String applicationName;
    /**
     * 调用全局id
     */
    private String traceId;
    /**
     * 每一次调用都认为是一个span
     */
    private Long spanId;
    /**
     * 父级id,用于构建调用链树
     */
    private Long parentId;
    /**
     * 客户端发送请求时间
     * 客户端调用时间=cr-cs
     * 服务端处理时间=sr-ss
     */
    private Date cst;
    /**
     * 服务端收到请求时间
     */
    private Date srt;
    /**
     * 服务端处理完逻辑时间
     */
    private Date sst;
    /**
     * 客户端收到处理完成请求时间
     */
    private Date crt;

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public Date getCrt() {
        return crt;
    }

    public void setCrt(Date crt) {
        this.crt = crt;
    }

    public Date getCst() {
        return cst;
    }

    public void setCst(Date cst) {
        this.cst = cst;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getSpanId() {
        return spanId;
    }

    public void setSpanId(Long spanId) {
        this.spanId = spanId;
    }

    public Date getSrt() {
        return srt;
    }

    public void setSrt(Date srt) {
        this.srt = srt;
    }

    public Date getSst() {
        return sst;
    }

    public void setSst(Date sst) {
        this.sst = sst;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }
}
