package com.framework.monitor.eyes.dubbo.filter;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.remoting.TimeoutException;
import com.alibaba.dubbo.rpc.*;
import com.framework.monitor.eyes.dubbo.trace.*;
import com.framework.monitor.eyes.dubbo.trace.enums.RPCResultEnum;
import com.framework.monitor.eyes.dubbo.trace.enums.SpanStateEnum;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.UUID;

/**
 * dubbo 消费方 监控
 * Created by yuanjinglin on 17/6/1.
 */
//@Activate(group = "provider", value = "ConsumeEyesFilter")
public class ConsumeEyesFilter implements Filter {
    private static final Logger LOGGER= LoggerFactory.getLogger("eyesFilterLog");
    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        URL url=invoker.getUrl();
        Map<String,String> paramMap= url.getParameters();
        String applicationName=paramMap.get("application");
        String ip=url.getHost();
        String methodName=url.getPath()+"."+paramMap.get("methods");
        //拿traceId,如果为空则是第一次调用,生成一个
        String traceId= TraceContext.getTraceId();
        if(traceId==null){
            traceId= UUID.randomUUID().toString();
            TraceContext.setTraceId(traceId);
        }
        //如果没有spanid则生成一个
        String spanId= TraceContext.getSpanId();
        if(spanId==null){
            spanId="0";
            TraceContext.start();
            TraceContext.setSpanId(spanId);
        }
        //生成本地调用span
        String nextChildSpanId= SpanIDUtil.proRpcNextChildSpanId();
        MethodSpan consumeSpan=new MethodSpan();
        consumeSpan.setParentId(spanId);
        consumeSpan.setSpanId(nextChildSpanId);
        consumeSpan.setTraceId(traceId);
        consumeSpan.setCs(System.currentTimeMillis());
        consumeSpan.setApplicationName(applicationName);
        consumeSpan.setName(methodName);
        consumeSpan.setIp(ip);
        TraceContext.addChildSpan(consumeSpan);
        Gson gson=new Gson();
        Map<String, String> attaches = invocation.getAttachments();
        attaches.put(TraceContext.TRACE_ID_KEY,traceId);
        attaches.put(TraceContext.SPAN_ID_KEY,nextChildSpanId);
        attaches.put(TraceContext.SPAN_KEY,gson.toJson(consumeSpan));
        consumeSpan.setState(SpanStateEnum.CS.getKey());
        LOGGER.info(consumeSpan.toString());
        Result result=null;
        try {
            result=invoker.invoke(invocation);
        }catch (Exception e){
            consumeSpan.setResult(RPCResultEnum.ERROR.getKey());
            throw e;
        }finally {
            consumeSpan.setCr(System.currentTimeMillis());
            consumeSpan.setState(SpanStateEnum.CR.getKey());
            LOGGER.info(consumeSpan.toString());
        }
        return result;
    }
}
