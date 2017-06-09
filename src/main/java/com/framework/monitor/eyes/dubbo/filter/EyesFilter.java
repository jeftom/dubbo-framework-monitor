package com.framework.monitor.eyes.dubbo.filter;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.rpc.*;
import com.framework.monitor.eyes.dubbo.trace.TraceContext;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Created by yuanjinglin on 17/6/1.
 */
//@Activate(group = "provider", value = "EyesFilter")
public class EyesFilter implements Filter {
    private static final Logger LOGGER= LoggerFactory.getLogger(EyesFilter.class);
    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        LOGGER.info("eyes fileter working!!!");
        URL url=invoker.getUrl();
        Map<String,String> paramMap= url.getParameters();
        String applicationName=paramMap.get("application");
        String ip=url.getHost();
        Map<String, String> attaches = invocation.getAttachments();
        attaches.put(TraceContext.TRACE_ID_KEY,"test001");
        attaches.put(TraceContext.SPAN_ID_KEY,"1");
        Gson gson=new Gson();
        LOGGER.info("param:::::"+gson.toJson(paramMap));
        LOGGER.info("url:::::"+gson.toJson(url));
        return invoker.invoke(invocation);
    }
}
