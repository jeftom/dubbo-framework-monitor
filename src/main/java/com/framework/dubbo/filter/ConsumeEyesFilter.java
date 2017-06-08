package com.framework.dubbo.filter;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.rpc.*;
import com.framework.dubbo.trace.Span;
import com.framework.dubbo.trace.SpanIDUtil;
import com.framework.dubbo.trace.SpanStateEnum;
import com.framework.dubbo.trace.TraceContext;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.UUID;

/**
 * dubbo 消费方 监控
 * Created by yuanjinglin on 17/6/1.
 */
//@Activate(group = "provider", value = "EyesFilter")
public class ConsumeEyesFilter implements Filter {
    private static final Logger LOGGER= LoggerFactory.getLogger("eyesFilterLog");
    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        URL url=invoker.getUrl();
        Map<String,String> paramMap= url.getParameters();
        String applicationName=paramMap.get("application");
        String ip=url.getHost();

        //拿traceId,如果为空则是第一次调用,生成一个
        String traceId=TraceContext.getTraceId();
        if(traceId==null){
            traceId= UUID.randomUUID().toString();
            TraceContext.setTraceId(traceId);
        }
        //如果没有spanid则生成一个
        String spanId=TraceContext.getSpanId();
        if(spanId==null){
            spanId="0";
            TraceContext.start();
            TraceContext.setSpanId(spanId);
        }
        //生成本地调用span
        String nextChildSpanId= SpanIDUtil.proNextChildSpanId();
        Span consumeSpan=new Span();
        consumeSpan.setParentId(spanId);
        consumeSpan.setSpanId(nextChildSpanId);
        consumeSpan.setTraceId(traceId);
        consumeSpan.setCs(System.currentTimeMillis());
        consumeSpan.setApplicationName(applicationName);
        consumeSpan.setIp(ip);
        TraceContext.addChildSpan(consumeSpan);
        Gson gson=new Gson();
        Map<String, String> attaches = invocation.getAttachments();
        attaches.put(TraceContext.TRACE_ID_KEY,traceId);
        attaches.put(TraceContext.SPAN_ID_KEY,nextChildSpanId);
        attaches.put(TraceContext.SPAN_KEY,gson.toJson(consumeSpan));
        consumeSpan.setState(SpanStateEnum.CS.getKey());
        LOGGER.info(consumeSpan.toString());
        Result result =invoker.invoke(invocation);
        consumeSpan.setCr(System.currentTimeMillis());
        consumeSpan.setState(SpanStateEnum.CR.getKey());
        LOGGER.info(consumeSpan.toString());
        return result;
    }
}
