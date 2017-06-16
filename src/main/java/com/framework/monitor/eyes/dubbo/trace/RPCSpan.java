package com.framework.monitor.eyes.dubbo.trace;

import com.framework.utils.GsonUtil;

import java.io.Serializable;

/**
 * Created by yuanjinglin on 17/6/8.
 */
public class RPCSpan extends  Span implements Serializable {

    private static final long serialVersionUID = -5351285011174602214L;

    /**
     * 每一次调用都认为是一个span
     */
    private String spanId;
    /**
     * 父级id,用于构建调用链树
     */
    private String parentId;
    private String ip;
    private String state;
    private String result=RPCResultEnum.OK.getKey();

    private Long cs;

    private Long sr;

    private Long cr;

    private Long ss;

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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return GsonUtil.toJson(this);
    }
}
