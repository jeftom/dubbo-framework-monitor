package com.framework.monitor.eyes.dubbo.trace;

/**
 * Created by yuanjinglin on 17/6/9.
 */

/**
 * 保存监控信息变量
 */
public  class TraceData {
    /**
     * 记录根根节点
     */
    public LocalMethodSpan root;
    /**
     * 当前正在调用方法节点
     */
    public LocalMethodSpan currentEntry;
    /**
     * 堆栈树高度
     */
    public int level;

}