package com.framework.monitor.eyes.dubbo.filter;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.rpc.*;
import com.framework.monitor.eyes.dubbo.trace.RPCSpan;
import com.framework.monitor.eyes.dubbo.trace.SpanStateEnum;
import com.framework.monitor.eyes.dubbo.trace.TraceContext;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * dubbo 服务提供方 监控
 * Created by yuanjinglin on 17/6/1.
 */
//@Activate(group = "provider", value = "EyesFilter")
public class ProviderEyesFilter implements Filter {
    private static final Logger LOGGER= LoggerFactory.getLogger("eyesFilterLog");
    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        Gson gson=new Gson();
        URL url=invoker.getUrl();
        Map<String,String> paramMap= url.getParameters();
        String applicationName=paramMap.get("application");
        String ip=url.getHost();
        Map<String, String> attaches = invocation.getAttachments();
        RPCSpan clientSpan=gson.fromJson(attaches.get(TraceContext.SPAN_KEY),RPCSpan.class);
        RPCSpan serviceSpan=new RPCSpan();
        serviceSpan.setSr(System.currentTimeMillis());
        serviceSpan.setApplicationName(applicationName);
        serviceSpan.setIp(ip);
        serviceSpan.setSpanId(clientSpan.getSpanId());
        serviceSpan.setTraceId(clientSpan.getTraceId());
        serviceSpan.setParentId(clientSpan.getParentId());
        if(TraceContext.getTraceId()==null){
            TraceContext.start();
            TraceContext.setTraceId(clientSpan.getTraceId());
            TraceContext.setSpanId(serviceSpan.getSpanId());
        }
        serviceSpan.setState(SpanStateEnum.SR.getKey());
        LOGGER.info(serviceSpan.toString());
        Result result= invoker.invoke(invocation);
        serviceSpan.setSs(System.currentTimeMillis());
        serviceSpan.setState(SpanStateEnum.SS.getKey());
        LOGGER.info(serviceSpan.toString());
        return result;
    }
}
