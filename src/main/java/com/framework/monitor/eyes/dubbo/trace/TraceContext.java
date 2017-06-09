package com.framework.monitor.eyes.dubbo.trace;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuanjinglin on 17/6/2.
 */
public class TraceContext implements Serializable{
    private static ThreadLocal<String> TRACE_ID = new InheritableThreadLocal<>();
    private static ThreadLocal<String> LOCAL_SPAN_ID = new InheritableThreadLocal<>();
    private static ThreadLocal<List<Span>> CHILD_SPAN_LIST = new InheritableThreadLocal<>();
    public static final String TRACE_ID_KEY = "traceId";
    public static final String SPAN_ID_KEY = "spanId";
    public static final String SPAN_KEY="span";
    public static final String CS = "cs";
    public static final String CR= "cr";
    public static final String SR = "sr";
    public static final String SS = "ss";

    public static void clear(){
        TRACE_ID.remove();
        LOCAL_SPAN_ID.remove();
        CHILD_SPAN_LIST.remove();
    }
    public static void start(){
        clear();
        CHILD_SPAN_LIST.set(new ArrayList<>());
    }
    public static String getTraceId(){
        return TRACE_ID.get();
    }
    public static void setTraceId(String traceId){
        TRACE_ID.set(traceId);
    }
    public static String getSpanId(){
        return LOCAL_SPAN_ID.get();
    }
    public static void setSpanId(String spanId){
        LOCAL_SPAN_ID.set(spanId);
    }
    public static int getNextSpanId(){
       return CHILD_SPAN_LIST.get().size();
    }
    public static void addChildSpan(Span span){
        CHILD_SPAN_LIST.get().add(span);
    }
}
