package com.framework.tools;

import java.io.Serializable;

/**
 * 通用的表示数对结构
 * Created by guoyongzheng on 14-12-2.
 */
public class CommonPair<F, S> implements Serializable {

    private static final long serialVersionUID = 3201953615039699253L;

    private F first;
    private S second;

    /**
     * 默认构造函数
     */
    public CommonPair() {
    }

    public CommonPair(F first, S second) {
        this.first = first;
        this.second = second;
    }

    public F getFirst() {
        return first;
    }

    public void setFirst(F first) {
        this.first = first;
    }

    public S getSecond() {
        return second;
    }

    public void setSecond(S second) {
        this.second = second;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CommonPair)) return false;

        CommonPair commonPair = (CommonPair) o;

        if (first != null ? !first.equals(commonPair.first) : commonPair.first != null) return false;
        if (second != null ? !second.equals(commonPair.second) : commonPair.second != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = first != null ? first.hashCode() : 0;
        result = 31 * result + (second != null ? second.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "CommonPair{" +
                "first=" + first +
                ", second=" + second +
                '}';
    }
}
