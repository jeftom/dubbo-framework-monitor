package com.framework.structure.tree;

import com.framework.pattern.specification.Specification;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 树形结构
 * Created by wangguibin on 14/12/9.
 */
public class Trees<K, V extends TreeElement<K>> {

    /**
     * 防止数据错误造成空循环而设置的阈值
     */
    private static final int MAX_LOOP = 200;

    /**
     * 键值对
     */
    private Map<K, V> valueMap;

    /**
     * 树结构
     */
    private Map<K, TreeNode<K>> nodes = new HashMap<>();

    /**
     * 构造函数，传入一个map构造成树结构
     *
     * @param valueMap
     */
    public Trees(Map<K, V> valueMap) {
        this.valueMap = valueMap;

        if (valueMap != null && !valueMap.isEmpty()) {
            //
            for (Map.Entry<K, V> entry : valueMap.entrySet()) {
                K k = entry.getKey();
                V v = entry.getValue();
                K p = v.getParent();

                //检索和创建本身对应的treenode
                TreeNode<K> thisNode = nodes.get(k);
                if (thisNode == null) {
                    thisNode = new TreeNode<>();
                    nodes.put(k, thisNode);
                }
                thisNode.parent = p;

                //检索和创建父节点对应的treenode
                if (p != null) {
                    TreeNode<K> parentNode = nodes.get(p);
                    if (parentNode == null) {
                        parentNode = new TreeNode<>();
                        nodes.put(p, parentNode);
                    }
                    parentNode.getChildren().add(k);
                }
            }
        }
    }

    /**
     * 获取id对应的值
     *
     * @param id
     * @return
     */
    public V getValue(K id) {
        if (valueMap == null) {
            return null;
        }
        return valueMap.get(id);
    }

    /**
     * 查询祖辈点集合
     * </br>
     * 查询结果中包含当前节点
     *
     * @param k 当前节点
     * @return 祖辈点集合, 如果没有查到值, 返回空的list
     */
    public List<V> findAncestors(K k) {
        return this.findAncestors(k, null);
    }

    /**
     * 查询子节点集合
     * </br>
     * 查询结果中包含当前节点
     *
     * @param k 当前节点
     * @return 子节点集合, 如果没有查到值, 返回空的list
     */
    public List<V> findChildren(K k) {
        return this.findChildren(k, null);
    }

    /**
     * 查询祖辈点集合
     * </br>
     * 查询结果中包含当前节点
     *
     * @param start 起始节点
     * @param spec  查询规范
     * @return 祖辈点集合, 如果没有查到值, 返回空的list
     */
    public List<V> findAncestors(K start, Specification<K> spec) {
        List<V> result = new ArrayList<>();
        //空防御
        if (null == start || nodes == null || valueMap == null) {
            return result;
        }
        //添加原始节点
        V startValue = valueMap.get(start);
        if (startValue != null) {
            result.add(startValue);
        }

        K current = start;
        int times = 0;//循环次数计数
        while (true) {
            if (spec != null && !spec.isSatisfiedBy(current)) {
                break;
            }

            //防止数据错误造成的死循环。
            if (times > MAX_LOOP) {
                throw new IllegalStateException("在树查询中出现了疑似死循环，强制跳出");
            }

            //查询当前节点值
            TreeNode<K> currentNode = nodes.get(current);
            if (currentNode == null) {
                break;
            }

            //查询父节点
            current = currentNode.getParent();
            //判空
            if (current == null) {
                break;
            }

            V parentValue = valueMap.get(current);
            //将结果加到结果集中
            if (parentValue != null) {
                result.add(parentValue);
            }

            times++;
        }
        return result;
    }

    /**
     * 查询子节点集合
     * </br>
     * 查询结果中包含当前节点
     *
     * @param root 当前节点
     * @param spec 查询规范
     * @return 子节点集合, 如果没有查到值, 返回空的list
     */
    public List<V> findChildren(K root, Specification<K> spec) {
        List<V> result = new ArrayList<>();
        //空防御
        if (null == root || nodes == null || valueMap == null) {
            return result;
        }
        //添加原始节点
        V startValue = valueMap.get(root);
        if (startValue != null) {
            result.add(startValue);
        }

        int times = 0;//循环次数计数
        //查询当前节点值
        TreeNode<K> rootNode = nodes.get(root);
        if (rootNode == null) {
            return result;
        }
        //用迭代代替递归：遍历当前层级节点的下一级所有子结点集合
        List<K> nextGen = new ArrayList<>(rootNode.children);
        while (nextGen != null && !nextGen.isEmpty()) {
            //防止数据错误造成的死循环。
            if (times > MAX_LOOP) {
                throw new IllegalStateException("在树查询中出现了疑似死循环，强制跳出");
            }
            //孙代节点列表
            List<K> grandChildren = new ArrayList<>();
            //遍历所有子代节点
            for (K child : nextGen) {
                //判空
                if (child == null) {
                    continue;
                }
                //将子代节点对应的值加入返回值列表
                V childValue = valueMap.get(child);
                if (childValue != null) {
                    result.add(childValue);
                }

                //spec用来阻止继续向下寻找子节点，但对本身节点并不起效，所以放在填充result之后
                if (spec != null && !spec.isSatisfiedBy(child)) {
                    continue;
                }

                //查询child对应节点
                TreeNode<K> childNode = nodes.get(child);
                if (childNode == null) {
                    continue;
                }
                //将孙代节点加入下迭代列表
                grandChildren.addAll(childNode.getChildren());
            }
            //重置下一代列表为孙代节点列表
            nextGen = grandChildren;
            times++;
        }
        return result;
    }

    /**
     * 树结构节点
     *
     * @param <K>
     */
    private static class TreeNode<K> implements TreeElement<K> {

        K parent;

        private List<K> children = new ArrayList<>();

        List<K> getChildren() {
            return children;
        }

        /**
         * 查询父节点
         *
         * @return
         */
        @Override
        public K getParent() {
            return parent;
        }
    }

}
