package com.nfsq.framework.structure.tree;

import junit.framework.TestCase;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Trees 测试类
 * </br>
 * 树形结构参考：
 * </br>
 *
 * 1
 * |--2
 * |  |--4
 * |  |  |-6
 * |  |
 * |  |--5
 * |
 * |--3
 * |  |--7
 * |
 * Created by wangguibin on 14/12/9.
 */
public class TreesTest extends TestCase {

    /**
     * 测试用数据
     */
    private Map<Integer,TestTreeElement<Integer>> map =new HashMap<Integer,TestTreeElement<Integer>>();

    TestTreeElement element1=new TestTreeElement(1,0);
    TestTreeElement element2=new TestTreeElement(2,1);
    TestTreeElement element3=new TestTreeElement(3,1);
    TestTreeElement element4=new TestTreeElement(4,2);
    TestTreeElement element5=new TestTreeElement(5,2);
    TestTreeElement element6=new TestTreeElement(6,4);
    TestTreeElement element7=new TestTreeElement(7,3);

    Trees<Integer,TestTreeElement<Integer>>  trees;


    public TreesTest(){
        //构建树的依赖关系
        map.put(new Integer(1),element1);
        map.put(new Integer(2),element2);
        map.put(new Integer(3),element3);
        map.put(new Integer(4),element4);
        map.put(new Integer(5),element5);
        map.put(new Integer(6),element6);
        map.put(new Integer(7),element7);
        trees=new Trees<Integer,TestTreeElement<Integer>>(map);

    }


    @Test
    public void testFindAncestors() throws Exception {
        //查询7的祖辈节点(1、3),树形结构参考类注释
        List<Integer> list7=new ArrayList<Integer>();
        list7.add(7);
        list7.add(1);
        list7.add(3);
        List<TestTreeElement<Integer>> tree7=trees.findAncestors(7);
        assertTrue(list7.size() == tree7.size());
        for(TestTreeElement element:tree7){
            assertTrue(list7.contains(element.getPos()) );
        }

        //查询6的祖辈节点(1、2、4),树形结构参考类注释
        List<Integer> list6=new ArrayList<Integer>();
        list6.add(6);
        list6.add(1);
        list6.add(2);
        list6.add(4);
        List<TestTreeElement<Integer>> tree6=trees.findAncestors(6);
        assertTrue(list6.size() == tree6.size());
        for(TestTreeElement element:tree6){
            assertTrue(list6.contains(element.getPos()) );
        }

        //查询5的祖辈节点(1、2),树形结构参考类注释
        List<Integer> list5=new ArrayList<Integer>();
        list5.add(5);
        list5.add(1);
        list5.add(2);
        List<TestTreeElement<Integer>> tree5=trees.findAncestors(5);
        assertTrue(list5.size() == tree5.size());
        for(TestTreeElement element:tree5){
            assertTrue(list5.contains(element.getPos()) );
        }

        //查询1的祖辈节点(空的list),树形结构参考类注释
        List<Integer> list1=new ArrayList<Integer>();
        List<TestTreeElement<Integer>> tree1=trees.findAncestors(1);
        assertTrue(list5.size() == tree5.size());

    }
    @Test
    public void testFindChildren() throws Exception {
        //查询1的子节点(2、3、4、5、6、7),树形结构参考类注释
        List<Integer> list1=new ArrayList<Integer>();
        list1.add(1);
        list1.add(2);
        list1.add(3);
        list1.add(4);
        list1.add(5);
        list1.add(6);
        list1.add(7);
        List<TestTreeElement<Integer>> trees1=trees.findChildren(1);
        System.out.println(trees1);
        assertTrue(list1.size() ==trees1.size());
        for(TestTreeElement element:trees1){
            assertTrue(list1.contains(element.getPos()) );
        }

        //查询2的子节点(4、5、6),树形结构参考类注释
        List<Integer> list2=new ArrayList<Integer>();
        list2.add(2);
        list2.add(4);
        list2.add(5);
        list2.add(6);
        List<TestTreeElement<Integer>> trees2=trees.findChildren(2);
        assertTrue(list2.size()==trees2.size());
        for(TestTreeElement element:trees2){
            assertTrue(list2.contains(element.getPos()) );
        }

        //查询4的子节点(6),树形结构参考类注释
        List<Integer> list4=new ArrayList<Integer>();
        list4.add(4);
        list4.add(6);
        List<TestTreeElement<Integer>> trees4= trees.findChildren(4);
        assertTrue(list4.size()==trees4.size());
        for(TestTreeElement element:trees4){
            assertTrue(list4.contains(element.getPos()) );
        }

        //查询7的子节点(空list),树形结构参考类注释
        List<TestTreeElement<Integer>> trees7= trees.findChildren(7);
        assertTrue(1==trees7.size());
    }
    @Test
    public void testFindAncestors1() throws Exception {
        //创建查询规范 满足isSatisfiedBy()返回true、查询到1 就停止,但是会把1查询出来
        TestSpecifaction<Integer> specifaction=new TestSpecifaction(1);
        //specifaction.isSatisfiedBy(element1);
        //查询7的祖辈节点(1、3),树形结构参考类注释
        List<Integer> list7=new ArrayList<Integer>();
        list7.add(7);
        list7.add(1);
        list7.add(3);
        List<TestTreeElement<Integer>> tree7=  trees.findAncestors(7,specifaction);
        assertTrue(list7.size() == tree7.size());
        for(TestTreeElement element:tree7){
            assertTrue(list7.contains(element.getPos()) );
        }

        //创建查询规范 满足isSatisfiedBy()返回true、查询到2 就停止,但是会把2查询出来
        TestSpecifaction<Integer> specifaction2=new TestSpecifaction(2);
        //specifaction.isSatisfiedBy(element2);
        //查询6的祖辈节点(2、4),树形结构参考类注释
        List<Integer> list6=new ArrayList<Integer>();
        list6.add(6);
        list6.add(2);
        list6.add(4);
        List<TestTreeElement<Integer>> tree6=  trees.findAncestors(6,specifaction2);
        assertTrue(list6.size()==tree6.size());
        for(TestTreeElement element:tree6){
            assertTrue(list6.contains(element.getPos()) );
        }

        //创建查询规范 满足isSatisfiedBy()返回true、查询到1 就停止,但是会把1查询出来
        //specifaction.isSatisfiedBy(element1);
        List<Integer> list1=new ArrayList<Integer>();
        list1.add(1);
        //查询1的祖辈节点(空 list),树形结构参考类注释
        List<TestTreeElement<Integer>> tree1=trees.findAncestors(1,specifaction);
        assertTrue(list1.size() == tree1.size());
    }
    @Test
    public void testFindChildren1() throws Exception {
        //创建查询规范 满足isSatisfiedBy()返回true、查询到4 就停止,但是会把4查询出来
        TestSpecifaction<Integer> specifaction=new TestSpecifaction(4);
        //specifaction.isSatisfiedBy(element4);
        //查询1的子节点(2、3、4、5、7),树形结构参考类注释
        List<Integer> list1=new ArrayList<Integer>();
        list1.add(1);
        list1.add(2);
        list1.add(3);
        list1.add(4);
        list1.add(5);
        list1.add(7);
        List<TestTreeElement<Integer>> trees1=trees.findChildren(1,specifaction);
        assertEquals(list1.size(), trees1.size());
        for(TestTreeElement element:trees1){
            assertTrue(list1.contains(element.getPos()) );
        }

        //创建查询规范 满足isSatisfiedBy()返回true、查询到6 就停止,但是会把6查询出来
        TestSpecifaction<Integer> specifaction6=new TestSpecifaction(6);
        //specifaction6.isSatisfiedBy(element6);
        //查询2的子节点(4、5、6),树形结构参考类注释
        List<Integer> list2=new ArrayList<Integer>();
        list2.add(2);
        list2.add(4);
        list2.add(5);
        list2.add(6);
        List<TestTreeElement<Integer>> trees2=trees.findChildren(2,specifaction6);
        assertTrue(list2.size()==trees2.size());
        for(TestTreeElement element:trees2){
            assertTrue(list2.contains(element.getPos()) );
        }

        //创建查询规范 满足isSatisfiedBy()返回true、查询到7 就停止,但是会把7查询出来
        TestSpecifaction<Integer> specifaction7=new TestSpecifaction(7);
        //specifaction7.isSatisfiedBy(element7);
        //查询3的子节点(7),树形结构参考类注释
        List<TestTreeElement<Integer>> trees3= trees.findChildren(3,specifaction7);
        assertTrue(2==trees3.size());

        //查询7的子节点(空 list),树形结构参考类注释
        List<TestTreeElement<Integer>> trees7= trees.findChildren(7,specifaction7);
        assertTrue(1==trees7.size());
    }
}
