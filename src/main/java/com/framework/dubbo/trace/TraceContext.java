package com.framework.dubbo.trace;

import java.io.Serializable;
import java.util.List;

/**
 * Created by yuanjinglin on 17/6/2.
 */
public class TraceContext implements Serializable{
    private static ThreadLocal<String> TRACE_ID = new InheritableThreadLocal<>();
    private static ThreadLocal<Long> SPAN_ID = new InheritableThreadLocal<>();
    private static ThreadLocal<List<Long>> SPAN_LIST = new InheritableThreadLocal<>();
    public static final String TRACE_ID_KEY = "traceId";
    public static final String SPAN_ID_KEY = "spanId";
    public static final String CST = "cst";
    public static final String CRT= "crt";
    public static final String SRT = "srt";
    public static final String SST = "sst";

    public static void clear(){
        TRACE_ID.remove();
        SPAN_ID.remove();
        SPAN_LIST.remove();
    }


}
