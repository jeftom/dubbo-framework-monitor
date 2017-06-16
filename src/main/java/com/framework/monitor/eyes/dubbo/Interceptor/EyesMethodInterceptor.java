package com.framework.monitor.eyes.dubbo.Interceptor;

import com.framework.monitor.Interceptor.StackData;

import com.framework.monitor.Interceptor.bean.DistributionTraceBean;
import com.framework.monitor.eyes.dubbo.trace.*;
import com.framework.utils.SysUtil;
import com.google.api.client.repackaged.com.google.common.base.Joiner;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;


/**
 * Created by yuanjinglin on 17/6/9.
 */
public class EyesMethodInterceptor implements MethodInterceptor {
    private static final Log LOGGER = LogFactory.getLog(MethodInterceptor.class);
    private static final Log EYESLOGGER = LogFactory.getLog("eyesFilterLog");
    /**
     * 性能监控开关可以在运行时动态设置开关
     */
    private volatile boolean switchOn = true;
    /**
     * 是否打印方法执行时间的阈值，执行时间超过该值则写入日志
     */
    private volatile int threshold = -1;
    /**
     * 线程变量，存储AOP拦截的每个方法的开始时间及结束时间
     */
    private ThreadLocal<StackData> dataHolder = new ThreadLocal<StackData>();


    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        if (switchOn) {
            Object result = null;
            String name = invocation.getThis().getClass().getName() + "." + invocation.getMethod().getName();
            try {
                before(name);
                result = invocation.proceed();
            } finally {
                after();
            }
            return result;

        } else {
            return invocation.proceed();
        }

    }
    public void before(String name) {
        //设置全局调用链唯一ID
        String traceId= TraceContext.getTraceId();
        if (traceId == null) {
            traceId= UUID.randomUUID().toString();
            TraceContext.setTraceId(traceId);
        }

        MethodSpan currentSpan=TraceContext.getSpan();
        MethodSpan span =new MethodSpan(name,System.currentTimeMillis());
        if(currentSpan==null){//如果为空说明是第一次进来
            TraceContext.start();
            span.spanId="0";
            span.parentId="-1";
        }else{//不是第一次
            //设置上下文的span,新建一个当前的span,id新成成,parent为上下文拿到的,设置parent为上下文的span,然后替换掉上下文
            span.spanId=currentSpan.spanId+"."+currentSpan.childs.size();//生成下一级spanid 号
            span.parentId=currentSpan.spanId;
            span.parent=currentSpan;
        }
        span.setApplicationName(getProjectName());
        span.ip=SysUtil.getLocalIp();
        span.setTraceId(traceId);
        TraceContext.setSpan(span);
        TraceContext.setSpanId(span.spanId);
        EYESLOGGER.info(span.toString());
    }
    public void after() {
        MethodSpan currentSpan=TraceContext.getSpan();
        if (currentSpan != null) {
            currentSpan.cr = System.currentTimeMillis();
            EYESLOGGER.info(currentSpan.toString());
            MethodSpan parent = currentSpan.parent;
            if(parent!=null){//父级为空说明是顶级
                TraceContext.setSpan(parent);
                TraceContext.setSpanId(parent.spanId);
            }
        }
    }
    private String getProjectName(){
        String path=this.getClass().getClassLoader().getResource("/").getPath();
        String[] names=path.split("/");
        return names[names.length-3];
    }

}
