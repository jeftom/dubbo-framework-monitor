package com.framework.monitor.eyes.dubbo.trace;

import com.framework.monitor.eyes.dubbo.trace.enums.RPCResultEnum;
import com.framework.utils.GsonUtil;
import com.sun.javafx.binding.MapExpressionHelper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuanjinglin on 17/6/9.
 */
public class MethodSpan implements Serializable {
    private static final long serialVersionUID = 6185949997237664073L;

    public String traceId;
    public String name;
    public String applicationName;
    public String spanId;
    public String parentId;
    public String ip;
    public String result= RPCResultEnum.OK.getKey();
    public String state;
    public Long cs;
    public Long cr;
    public Long ss;
    public Long sr;
    /**
     * 父级节点
     */
    transient public MethodSpan parent;
    /**
     * 调用子方法节点
     */
    transient public List<MethodSpan> childs;
    public MethodSpan(){}
    public MethodSpan(String methodName, long currentTimeMillis) {
        this.name=methodName;
        this.cs = currentTimeMillis;
        this.childs = new ArrayList<MethodSpan>(3);
    }

    public Long getCr() {
        return cr;
    }

    public void setCr(Long cr) {
        this.cr = cr;
    }

    public Long getCs() {
        return cs;
    }

    public void setCs(Long cs) {
        this.cs = cs;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public MethodSpan getParent() {
        return parent;
    }

    public void setParent(MethodSpan parent) {
        this.parent = parent;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getSpanId() {
        return spanId;
    }

    public void setSpanId(String spanId) {
        this.spanId = spanId;
    }

    public Long getSr() {
        return sr;
    }

    public void setSr(Long sr) {
        this.sr = sr;
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

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    @Override
    public String toString() {
        return GsonUtil.toJson(this);
    }
}
