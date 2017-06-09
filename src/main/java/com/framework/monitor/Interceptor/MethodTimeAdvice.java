package com.framework.monitor.Interceptor;

import com.framework.monitor.bean.DistributionTraceBean;
import com.google.api.client.repackaged.com.google.common.base.Joiner;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Created by handepei on 15/3/10.
 */
public class MethodTimeAdvice implements MethodInterceptor {
    private Logger logger = Logger.getLogger("runTimeCount");
    /**
     *   性能监控开关可以在运行时动态设置开关
     */
    private volatile boolean switchOn = true;
    /**
     * 是否打印方法执行时间的阈值，执行时间超过该值则写入日志
     */
    private volatile int threshold = -1;
    /**
     * 线程变量，存储AOP拦截的每个方法的开始时间及结束时间
     */
    private  ThreadLocal<StackData> dataHolder = new ThreadLocal<StackData>();


    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        if(switchOn){
            String name = null;
            Object result = null;
            try {
                name = invocation.getThis().getClass().getName() + "." + invocation.getMethod().getName();
            } catch (Exception e) {
                logger.error("MethodTimeAdive.ivoke() error",e);
            }
            try {
                startCount(name);
                result = invocation.proceed();
            } finally {
                stopCount(threshold);

            }
            return result;

        }
        else {
            return invocation.proceed();
        }

    }

    /**
     * 统计方法执行时间开始，该方法放置于MethodInvocation的proceed之前。
     * @param logName  纪录被代理累的方法名（即被拦截的被代理类方法名）
     */
    public  void startCount(String logName) {
        //从dataHolder中获取AOP拦截的方法构建的StackData结构数据
        StackData data = dataHolder.get();
        //获取当前AOP拦截的方法构建的StackData结构数据
        StackEntry currentEntry = new StackEntry(logName, System.currentTimeMillis());
        if (data == null || data.level ==0 || data.currentEntry ==null) {
            data = new StackData();
            data.root = currentEntry;
            data.level = 1;
            dataHolder.set(data);
        } else {
            StackEntry parent = data.currentEntry;
            currentEntry.parent=parent;
            try {
                //被引用的子方法纪录到child中
                parent.child.add(currentEntry);
            } catch (Exception e) {
                logger.error("MethodTimeAdvice Error ---",e);
            }

        }
        //纪录的结点下移，深度加一。
        data.currentEntry = currentEntry;
        currentEntry.level=data.level;
        data.level++;

    }

    /**
     * 统计方法执行时间结束，该方法放置于MethodInvocation的proceed之后
     * @param threshold  是否打印方法执行时间的阈值，统计结束时将执行时间超过该值则写入日志
     */
    public  void stopCount(int threshold) {
        StackData data = dataHolder.get();
        if(data!=null) {
            StackEntry self = data.currentEntry;
            self.endTime = System.currentTimeMillis();
            data.currentEntry = self.parent;
            data.level--;
            if(data.root == self && (self.endTime -self.beginTime) > threshold){
                //调用堆栈退出前，打印日志。
                printStack(data);
            }
        }


    }

    /**
     * 此处还可以进行改进，可以将超时的数据放入一个有界队列
     * 里，在另一个线程进行打印。
     * @param data
     */
    private  void printStack(StackData data) {
        StringBuilder sb = new StringBuilder("\r\n");
        StackEntry root = data.root;
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
            logger.info(sb.toString() + "traceId:" +distributionTraceBean.getTraceId()
                    + ",rpcId:" + rpcId);
        } else {
            logger.info(sb.toString());
        }

        //LoggerRun.infoLogger.info(sb.toString());

    }

    /**
     * 遍历一次请求所有被拦截的方法（迭代），构建树形结构图。
     * @param entry  起始跟节点数据
     * @param sb  存放树形结构图
     */
    private static void appendNode(StackEntry entry, StringBuilder sb) {
        long totalTime = entry.endTime-entry.beginTime ;
        if(entry.level ==1){
            sb.append("start count"+"\r\n");
            sb.append("|-");
        }
        sb.append(totalTime);
        sb.append(" ms; [");
        sb.append(entry.logName);
        sb.append("]");
        //遍历当前结点所有子结点即被引用函数
        for(StackEntry cnode : entry.child){
            sb.append("\r\n|");
            for(int i=0,l=entry.level;i<l;i++){
                sb.append("+---");
            }
            //迭代遍历
            appendNode(cnode,sb);
        }

    }
}
