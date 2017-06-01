package com.framework.repository;

/**
 * 实体对象的repository
 * Created by guoyongzheng on 14-11-28.
 */
public interface EntityRepository<K, V> {

    /**
     * 根据id查询/获取数据对象
     * @param id
     * @return
     */
    V getValue(K id);

    /**
     * 清空id所代表的数据对象
     * @param id
     */
    boolean removeValue(K id);
}
