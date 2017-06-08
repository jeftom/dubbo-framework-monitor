package com.framework.dubbo.filter;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.rpc.*;
import com.framework.dubbo.trace.Span;
import com.framework.dubbo.trace.SpanStateEnum;
import com.framework.dubbo.trace.TraceContext;
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
    private static final Logger LOGGER= LoggerFactory.getLogger(ProviderEyesFilter.class);
    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        LOGGER.info("provider eyes fileter working!!!");
        Gson gson=new Gson();
        URL url=invoker.getUrl();
        Map<String,String> paramMap= url.getParameters();
        String applicationName=paramMap.get("application");
        String ip=url.getHost();
        Map<String, String> attaches = invocation.getAttachments();
        Span clientSpan=gson.fromJson(attaches.get(TraceContext.SPAN_KEY),Span.class);
        Span serviceSpan=new Span();
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
        LOGGER.info("service::: sr"+serviceSpan);
        Result result= invoker.invoke(invocation);
        serviceSpan.setSs(System.currentTimeMillis());
        serviceSpan.setState(SpanStateEnum.SS.getKey());
        LOGGER.info("service::: ss"+serviceSpan);
        return result;
    }
}
