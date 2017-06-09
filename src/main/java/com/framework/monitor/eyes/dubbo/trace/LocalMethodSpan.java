package com.framework.monitor.eyes.dubbo.trace;

import com.framework.utils.GsonUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuanjinglin on 17/6/9.
 */
public class LocalMethodSpan extends Span {
    public String name;
    public Long beginTime;
    public Long endTime;
    public int  level;
    /**
     * 父级节点
     */
    public LocalMethodSpan parent;
    /**
     * 调用子方法节点
     */
    public List<LocalMethodSpan> childs;
    public LocalMethodSpan(String name, long currentTimeMillis) {
        this.name = name;
        this.beginTime = currentTimeMillis;
        this.childs = new ArrayList<LocalMethodSpan>(10);
    }

    @Override
    public String toString() {
        return GsonUtil.toJson(this);
    }
}
