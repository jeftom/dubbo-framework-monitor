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
    public String methodName;
    public Long beginTime;
    public Long endTime;
    public int  level;
    /**
     * 父级节点
     */
    public MethodSpan parent;
    /**
     * 调用子方法节点
     */
   // public List<MethodSpan> childs;
    public MethodSpan(String methodName, long currentTimeMillis) {
        this.methodName = methodName;
        this.beginTime = currentTimeMillis;
       // this.childs = new ArrayList<MethodSpan>(3);
    }

    @Override
    public String toString() {
        return GsonUtil.toJson(this);
    }
}
