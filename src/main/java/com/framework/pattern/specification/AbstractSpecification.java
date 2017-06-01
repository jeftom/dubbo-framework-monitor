package com.framework.pattern.specification;

/**
 * 定义了与、或、非运算的specification。
 * Created by guoyongzheng on 14-12-15.
 */
public abstract class AbstractSpecification<T> implements Specification<T> {

    /**
     * 与运算
     * @param another
     * @return
     */
    public AbstractSpecification<T> and(Specification<T> another) {
        return new AndSpecification<>(this, another);
    }

    /**
     * 或运算
     * @param another
     * @return
     */
    public AbstractSpecification<T> or(Specification<T> another) {
        return new OrSpecification<>(this, another);
    }

    /**
     * 非运算
     * @return
     */
    public AbstractSpecification<T> not() {
        return new NotSpecification<>(this);
    }
}
