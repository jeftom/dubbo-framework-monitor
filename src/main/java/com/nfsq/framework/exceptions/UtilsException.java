package com.nfsq.framework.exceptions;

/**
 * 工具类异常
 * Created by 李溪林 on 17-2-15.
 */
public class UtilsException extends RuntimeException {

    /**
     * 构造方法
     *
     * @param message 异常信息
     */
    public UtilsException(String message) {
        super(message);
    }

    /**
     * 构造方法
     *
     * @param cause 异常对象
     */
    public UtilsException(Throwable cause) {
        super(cause);
    }

    /**
     * 构造方法
     *
     * @param message 异常信息
     * @param cause   异常对象
     */
    public UtilsException(String message, Throwable cause) {
        super(message, cause);
    }
}
