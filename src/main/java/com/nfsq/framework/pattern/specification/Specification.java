package com.nfsq.framework.pattern.specification;

/**
 * Specification接口
 */
public interface Specification<T> {
    /**
     * 是否满足
     * @return boolean
     */
   boolean isSatisfiedBy(T t);
}
