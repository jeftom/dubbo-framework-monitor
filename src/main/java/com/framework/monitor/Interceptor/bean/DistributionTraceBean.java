package com.framework.monitor.Interceptor.bean;

/**
 * Created by jm on 2016/8/18.
 * 分布式追踪 ThreadLocal
 */
public class DistributionTraceBean {
    /**
     * 调用链ID
     */
    private String rpcId;
    /**
     * 本次发起的请求全局统一ID
     */
    private String traceId;
    /**
     * 计算rpc的序列号
     */
    private String serialNumber;

    private final static ThreadLocal<DistributionTraceBean> threadLocal = new ThreadLocal<>();

    public static DistributionTraceBean init(String uuid, String rpcId, String serialNumber ) {
        DistributionTraceBean requestContextBean = new DistributionTraceBean();
        requestContextBean.traceId = uuid;
        requestContextBean.rpcId = rpcId;
        requestContextBean.serialNumber = serialNumber;
        threadLocal.set(requestContextBean);
        return requestContextBean;
    }

    /**
     * 获取当前线程
     * @return
     */
    public static DistributionTraceBean get(){
        return threadLocal.get();
    }

    public static void remove(){
         threadLocal.remove();
    }

    public String getRpcId() {
        return rpcId;
    }

    public void setRpcId(String rpcId) {
        this.rpcId = rpcId;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    @Override
    public String toString() {
        return "DistributionTraceBean{" +
                "rpcId='" + rpcId + '\'' +
                ", traceId='" + traceId + '\'' +
                ", serialNumber='" + serialNumber + '\'' +
                '}';
    }
}
