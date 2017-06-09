package com.framework.monitor.Interceptor;

/**
 * Created by handepei on 15/3/12.
 */

/**
 * 保存监控信息变量
 */
public  class StackData {
    /**
     * 记录根根节点
     */
    public StackEntry root;
    /**
     * 当前正在调用方法节点
     */
    public StackEntry currentEntry;
    /**
     * 堆栈树高度
     */
    public int level;

}