package com.framework.monitor.eyes.dubbo.trace;

/**
 * Created by yuanjinglin on 17/6/8.
 */
public class SpanIDUtil {
    public static String proRpcNextChildSpanId(){
        String currentSpanId= TraceContext.getSpanId();
        int nextSpanNum= TraceContext.getNextSpanId();
        return currentSpanId+"."+nextSpanNum;
    }
}
