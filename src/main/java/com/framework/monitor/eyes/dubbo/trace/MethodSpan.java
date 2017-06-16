package com.framework.monitor.eyes.dubbo.trace;

import com.framework.utils.GsonUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuanjinglin on 17/6/9.
 */
public class MethodSpan extends Span implements Serializable {
    private static final long serialVersionUID = 6185949997237664073L;

    public Long cs;
    public Long cr;
    public String spanId;
    public String parentId;
    public String ip;
    public String result=RPCResultEnum.OK.getKey();
    /**
     * 父级节点
     */
    transient public MethodSpan parent;
    /**
     * 调用子方法节点
     */
    transient public List<MethodSpan> childs;
    public MethodSpan(String methodName, long currentTimeMillis) {
        this.setName(methodName);
        this.cs = currentTimeMillis;
        this.childs = new ArrayList<MethodSpan>(3);
    }

    @Override
    public String toString() {
        return GsonUtil.toJson(this);
    }
}
