package com.framework.monitor.eyes.dubbo.Interceptor;

import com.framework.monitor.Interceptor.StackData;

import com.framework.monitor.Interceptor.bean.DistributionTraceBean;
import com.framework.monitor.eyes.dubbo.trace.TraceContext;
import com.framework.monitor.eyes.dubbo.trace.TraceData;
import com.framework.monitor.eyes.dubbo.trace.LocalMethodSpan;
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

    private static final Log LOGGER = LogFactory.getLog("eyesFilterLog");
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
     * @param name 纪录被代理类的方法名（即被拦截的被代理类方法名）
     */
    public void before(String name) {
        //从TraceContext中获取AOP拦截的方法构建的TraceData结构数据
        TraceData traceData = TraceContext.getMethodTrace();
        LocalMethodSpan currentEntry = new LocalMethodSpan(name, System.currentTimeMillis());
        if (traceData == null  || traceData.currentEntry == null) {
            traceData = new TraceData();
            traceData.root = currentEntry;
            traceData.level = 0;
            TraceContext.setMethodTrace(traceData);
            //设置全局调用链唯一ID
            String traceId= TraceContext.getTraceId();
            if(traceId==null){
                traceId= UUID.randomUUID().toString();
                TraceContext.setTraceId(traceId);
            }
        } else {
            LocalMethodSpan parent = traceData.currentEntry;
            currentEntry.parent = parent;
            //被引用的子方法纪录到child中
            parent.childs.add(currentEntry);
        }
        //纪录的结点下移，深度加一。
        traceData.currentEntry = currentEntry;//设置当前节点
        currentEntry.level = traceData.level;//设置点钱节点级别
        traceData.level++;

    }

    /**
     * 统计方法执行时间结束，该方法放置于MethodInvocation的proceed之后
     *
     * @param threshold 是否打印方法执行时间的阈值，统计结束时将执行时间超过该值则写入日志
     */
    public void after(int threshold) {
        TraceData traceData = TraceContext.getMethodTrace();
        if (traceData != null) {
            LocalMethodSpan self = traceData.currentEntry;
            self.endTime = System.currentTimeMillis();
            traceData.currentEntry = self.parent;
            traceData.level--;
            LOGGER.info(self.toString());
        }


    }

    /**
     * 此处还可以进行改进，可以将超时的数据放入一个有界队列
     * 里，在另一个线程进行打印。
     *
     * @param data
     */
    private void printStack(TraceData data) {
        StringBuilder sb = new StringBuilder("\r\n");
        LocalMethodSpan root = data.root;
        //构建树形结构
        appendNode(root, sb);

        DistributionTraceBean distributionTraceBean = DistributionTraceBean.get();
        if (distributionTraceBean != null && distributionTraceBean.getTraceId() != null
                && distributionTraceBean.getRpcId() != null) {
            String rpcId = distributionTraceBean.getRpcId();
            if (!"0".equals(rpcId)) {
                String[] rpcIdArray = rpcId.split("\\.");
                List<String> rpcIdList = new ArrayList<>(Arrays.asList(rpcIdArray));
                //调用接口的时候RpcId最后一位数+1
                Integer lastId = Integer.valueOf(rpcIdArray[rpcIdArray.length - 1]) + 1;
                rpcIdList.set(rpcIdList.size() - 1, String.valueOf(lastId));
                rpcId = Joiner.on(".").skipNulls().join(rpcIdList).toString();
                distributionTraceBean.setRpcId(rpcId);
            } else {
                distributionTraceBean.setRpcId("0.0");
            }
            LOGGER.info(sb.toString() + "traceId:" + distributionTraceBean.getTraceId()
                    + ",rpcId:" + rpcId);
        } else {
            LOGGER.info(sb.toString());
        }

        //LoggerRun.infoLogger.info(sb.toString());

    }

    /**
     * 遍历一次请求所有被拦截的方法（迭代），构建树形结构图。
     *
     * @param entry 起始跟节点数据
     * @param sb    存放树形结构图
     */
    private static void appendNode(LocalMethodSpan entry, StringBuilder sb) {
        long totalTime = entry.endTime - entry.beginTime;
        if (entry.level == 1) {
            sb.append("start count" + "\r\n");
            sb.append("|-");
        }
        sb.append(totalTime);
        sb.append(" ms; [");
        sb.append(entry.name);
        sb.append("]");
        //遍历当前结点所有子结点即被引用函数
        for (LocalMethodSpan cnode : entry.childs) {
            sb.append("\r\n|");
            for (int i = 0, l = entry.level; i < l; i++) {
                sb.append("+---");
            }
            //迭代遍历
            appendNode(cnode, sb);
        }

    }
}
