package com.nfsq.framework.structure.tree;

import com.nfsq.framework.pattern.specification.Specification;

/**
 * Created by wangguibin on 14/12/9.
 */
public class TestSpecifaction<T> implements Specification<T> {

    private T pos;

   public TestSpecifaction(T pos){
       this.pos=pos;
   }

    @Override
    public boolean isSatisfiedBy(T o) {
        T treeElement= o;
        if (null!=treeElement && treeElement.equals(pos)){
            return false;
        }

        return true;
    }
}
