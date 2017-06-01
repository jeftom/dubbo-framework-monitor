package com.framework.structure.tree;

/**
 * 测试用的树元素类
 * Created by wangguibin on 14/12/9.
 */
public class TestTreeElement<Integer> implements TreeElement<Integer> {
   private  Integer pos;
    private  Integer parent;

    public TestTreeElement(Integer pos,Integer parent){
        this.pos=pos;
        this.parent=parent;
    }

    @Override
    public Integer getParent() {
        return parent;
    }

    public Integer getPos(){
        return pos;
    }

    @Override
    public String toString() {
        return getPos().toString();
    }
}
