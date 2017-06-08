package com.framework.dubbo.trace;

/**
 * Created by yuanjinglin on 17/6/8.
 */
public class SpanIDUtil {
    public static String proNextChildSpanId(){
        String currentSpanId=TraceContext.getSpanId();
        int nextSpanNum=TraceContext.getNextSpanId();
        return currentSpanId+"."+nextSpanNum;
    }
}
