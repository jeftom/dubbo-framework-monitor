package com.nfsq.framework.pattern.specification;

/**
 * Created by guoyongzheng on 14-12-15.
 */
class NotSpecification<T> extends AbstractSpecification<T> {

    private final Specification<T> spec;

    /**
     * 构造函数
     * @param spec
     */
    NotSpecification(Specification<T> spec) {
        this.spec = spec;
    }

    /**
     * 是否满足
     *
     * @param t
     * @return boolean
     */
    @Override
    public boolean isSatisfiedBy(T t) {
        return !spec.isSatisfiedBy(t);
    }
}
