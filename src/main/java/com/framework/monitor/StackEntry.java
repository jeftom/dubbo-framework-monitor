package com.framework.monitor;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by handepei on 15/3/12.
 */
public class StackEntry {
    public String logName ;
    public long beginTime;
    public long endTime;
    /**
     * 节点所处高度
     */
    public int level;
    /**
     * 调用的子方法
     */
    public List<StackEntry> child;
    /**
     * 上级节点
     */
    public StackEntry parent ;
    public StackEntry(String logName, long currentTimeMillis) {
        this.logName = logName;
        this.beginTime = currentTimeMillis;
        this.child = new ArrayList<StackEntry>(3);
    }
}
