package com.framework.monitor.eyes.dubbo.Interceptor;

import com.framework.monitor.Interceptor.StackData;

import com.framework.monitor.Interceptor.bean.DistributionTraceBean;
import com.framework.monitor.eyes.dubbo.trace.TraceContext;
import com.framework.monitor.eyes.dubbo.trace.LocalTraceData;
import com.framework.monitor.eyes.dubbo.trace.MethodSpan;
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
                after(threshold);
            }
            return result;

        } else {
            return invocation.proceed();
        }

    }

    /**
     * 统计方法执行时间开始，该方法放置于MethodInvocation的proceed之前。
     *
     * @param methodName 纪录被代理类的方法名（即被拦截的被代理类方法名）
     */
    public void before(String methodName) {
        //设置全局调用链唯一ID
        String traceId= TraceContext.getTraceId();
        //从TraceContext中获取AOP拦截的方法构建的TraceData结构数据
        LocalTraceData traceData = TraceContext.getMethodTrace();
        MethodSpan currentEntry = new MethodSpan(methodName, System.currentTimeMillis());
        if (traceData == null  || traceData.currentEntry == null) {
            traceData = new LocalTraceData();
            traceData.root = currentEntry;
            traceData.currentEntry=currentEntry;
            traceData.level = 0;
            TraceContext.start();
            if(traceId==null){
                traceId= UUID.randomUUID().toString();
                TraceContext.setTraceId(traceId);
            }
            TraceContext.setMethodTrace(traceData);
        } else {
            MethodSpan parent = traceData.currentEntry;
            currentEntry.parent = parent;
            //被引用的子方法纪录到child中
            //parent.childs.add(currentEntry);
            currentEntry.setTraceId(traceId);
        }
        currentEntry.setTraceId(traceId);
        LOGGER.info(currentEntry.toString());
        //纪录的结点下移，深度加一。
        traceData.currentEntry = currentEntry;//设置当前节点
        currentEntry.level = traceData.level;//设置点钱节点级别
        TraceContext.setSpanId(traceData.currentEntry.level+"");
        traceData.level++;

    }

    /**
     * 统计方法执行时间结束，该方法放置于MethodInvocation的proceed之后
     *
     * @param threshold 是否打印方法执行时间的阈值，统计结束时将执行时间超过该值则写入日志
     */
    public void after(int threshold) {
        LocalTraceData traceData = TraceContext.getMethodTrace();
        if (traceData != null) {
            MethodSpan self = traceData.currentEntry;
            if(self!=null){
                self.endTime = System.currentTimeMillis();
                traceData.currentEntry = self.parent;
                traceData.level--;
                LOGGER.info(self.toString());
                TraceContext.setSpanId(traceData.level+"");
            }
        }


    }
}
