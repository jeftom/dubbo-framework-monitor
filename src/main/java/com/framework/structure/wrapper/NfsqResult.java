package com.framework.structure.wrapper;

import java.io.Serializable;

/**
 * 公用的dubbo接口 result 对象结构
 *
 * Created by guoyongzheng on 15/11/11.
 */
public class NfsqResult<T> implements Serializable {

    private static final long serialVersionUID = -9130472993784728577L;

    /**
     * 成功标识
     */
    private boolean success = false;

    /**
     * 返回数据
     */
    private T data;

    /**
     * 返回结果code
     */
    private String code;

    /**
     * 返回信息
     */
    private String message;

    /**
     * 无参构造器
     */
    public NfsqResult(){}

    /**
     * 含参构造器
     * @param success
     * @param data
     * @param code
     * @param message
     */
    public NfsqResult(boolean success, T data, String code, String message) {
        this.success = success;
        this.data = data;
        this.code = code;
        this.message = message;
    }


    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
