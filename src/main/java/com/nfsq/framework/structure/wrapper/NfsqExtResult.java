package com.nfsq.framework.structure.wrapper;

import java.util.List;

/**
 * 带错误信息的dubbo返回类型
 *
 * Created by guoyongzheng on 15/11/11.
 */
public class NfsqExtResult<T> extends NfsqResult<T> {

    private static final long serialVersionUID = -145793336550499172L;
    /**
     * 错误信息
     */
    private List<String> errorList;

    /**
     * 无参构造器
     */
    public NfsqExtResult() {
        super();
    }

    /**
     * 含参构造器
     *
     * @param success
     * @param data
     * @param code
     * @param message
     */
    public NfsqExtResult(boolean success, T data, String code, String message, List<String> errorList) {
        super(success, data, code, message);
        this.errorList = errorList;
    }

    /**
     * 判断是否有错误发生
     * @return
     */
    public boolean hasErrors() {
        return errorList != null && !errorList.isEmpty();
    }

    public List<String> getErrorList() {
        return errorList;
    }

    public void setErrorList(List<String> errorList) {
        this.errorList = errorList;
    }
}
