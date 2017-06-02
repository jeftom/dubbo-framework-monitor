package com.framework.dubbo.trance;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by yuanjinglin on 17/6/2.
 */
public class TraceContext implements Serializable{
    private static ThreadLocal<Long> TRACE_ID = new InheritableThreadLocal<>();
    private static ThreadLocal<Long> SPAN_ID = new InheritableThreadLocal<>();
    private static ThreadLocal<List<Long>> SPAN_LIST = new InheritableThreadLocal<>();
    public static final String TRACE_ID_KEY = "traceId";
    public static final String SPAN_ID_KEY = "spanId";

}
