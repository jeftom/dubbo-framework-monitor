package com.nfsq.framework.annotation;

import java.lang.annotation.*;

/**
 * 描述注解,用于表示描述信息
 * 可通过本注解结合TypeUtils中的方法来获取
 * Created by 李溪林 on 17-2-16.
 */
@Documented
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Description {

    /**
     * 描述信息
     *
     * @return 描述信息
     */
    String value();
}
