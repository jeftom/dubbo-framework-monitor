package com.nfsq.framework.pattern.specification;

/**
 * Created by guoyongzheng on 14-12-15.
 */
class OrSpecification<T> extends AbstractSpecification<T> {

    private final Specification<T> one;

    private final Specification<T> theother;

    /**
     * 构造函数
     * @param one
     * @param theother
     */
    OrSpecification(Specification<T> one, Specification<T> theother) {
        this.one = one;
        this.theother = theother;
    }

    /**
     * 是否满足
     *
     * @param t
     * @return boolean
     */
    @Override
    public boolean isSatisfiedBy(T t) {
        return one.isSatisfiedBy(t) || theother.isSatisfiedBy(t);
    }
}
