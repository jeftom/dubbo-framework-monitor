package com.nfsq.framework.tools;

/**
 * 把受检异常包装成runtime异常的包装器
 * Created by guoyongzheng on 15/3/30.
 */
public class WrappedRTException extends RuntimeException {

    private static final long serialVersionUID = -4132268714358372293L;

    /**
     * 默认构造函数
     * @param e
     */
    public WrappedRTException(Throwable e) {
        super(e.getMessage(), e);
    }
}
