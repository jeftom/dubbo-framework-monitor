package com.framework.structure.tree;

/**
 * 树形结构元素满足接口
 * Created by wangguibin on 14/12/9.
 */
public interface TreeElement<K> {
    /**
     * 查询父节点
     * @return
     */
    K getParent();
}
