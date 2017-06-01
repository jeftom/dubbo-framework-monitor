package com.framework.utils;

import com.framework.enums.SortDirection;
import com.framework.utils.model.Special1;
import com.framework.utils.model.Target1;
import com.framework.utils.model.TestUserModel;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by 李溪林 on 17-2-16.
 */
public class CollectionUtilsTest {

    private void out(Object o){
        System.out.println(o);
    }

    @Test
    public void isNullOrEmpty(){
        Collection<Integer> d0 = new ArrayList<>();
        d0.add(0);

        List<Long> d1 = new ArrayList<>();
        d1.add(2L);

        ArrayList<String> d2 = new ArrayList<>();
        d2.add("333");

        Set<Long> d3 = new HashSet<>();
        d3.add(44L);

        this.out(CollectionUtils.isNullOrEmpty(d0));
        this.out(CollectionUtils.isNullOrEmpty(d1));
        this.out(CollectionUtils.isNullOrEmpty(d2));
        this.out(CollectionUtils.isNullOrEmpty(d3));
    }

    @Test
    public void forEach(){
        Collection<TestUserModel> data = this.getData();
        CollectionUtils.forEach(data, f -> {
            System.out.println(f.getName());
        });
    }

    @Test
    public void all(){
        Collection<TestUserModel> data = this.getData();
        List<TestUserModel> data2 = new ArrayList<>(data);
        ArrayList<TestUserModel> data3 = new ArrayList<>(data);
        Set<TestUserModel> data4 = new HashSet<>(data);

        //判定 集合中的所有项,是否都满足"年龄 > 10"的条件
        boolean e1 = CollectionUtils.all(data4, a -> a.getAge() > 10);
        this.out(e1);
        //判定 集合中的所有项,是否都满足"年龄 > 20"的条件
        boolean e2 = CollectionUtils.all(data4, a -> a.getAge() > 20);
        this.out(e2);
        //判定 集合中的所有项,是否都满足"年龄 > 30"的条件
        boolean e3 = CollectionUtils.all(data4, a -> a.getAge() > 30);
        this.out(e3);
    }

    @Test
    public void any(){
        Collection<TestUserModel> data = this.getData();
        List<TestUserModel> data2 = new ArrayList<>(data);
        ArrayList<TestUserModel> data3 = new ArrayList<>(data);
        Set<TestUserModel> data4 = new HashSet<>(data);

        //判定 集合中是否存在满足"年龄 > 30"条件的项
        boolean e1 = CollectionUtils.any(data3, a -> a.getAge() > 30);
        this.out(e1);
        //判定 集合中是否存在满足"姓名包含王"条件的项
        boolean e2 = CollectionUtils.any(data3, a -> a.getName().contains("王"));
        this.out(e2);
        //判定 集合中是否存在满足"id等于318"条件的项
        boolean e3 = CollectionUtils.any(data3, a -> a.getId() != null && a.getId().equals("318"));
        this.out(e3);
        //判定 集合中是否存在满足"姓名包含隔壁"条件的项
        boolean e4 = CollectionUtils.any(data3, a -> a.getName().contains("隔壁"));
        this.out(e4);
    }

    @Test
    public void where(){
        Collection<TestUserModel> data = this.getData();
        List<TestUserModel> data2 = new ArrayList<>(data);
        ArrayList<TestUserModel> data3 = new ArrayList<>(data);
        Set<TestUserModel> data4 = new HashSet<>(data);

        //从集合中过滤出满足"年龄 > 30"条件的新集合
        Collection<TestUserModel> e1 = CollectionUtils.where(data4, a -> a.getAge() > 30);
        this.out(e1.size());
        //从集合中过滤出满足"姓名包含头"条件的新集合
        Collection<TestUserModel> e2 = CollectionUtils.where(data4, a -> a.getName().contains("头"));
        this.out(e2.size());
        //从集合中过滤出满足"id等于318"条件的新集合
        Collection<TestUserModel> e3 = CollectionUtils.where(data4, a -> a.getId() != null && a.getId().equals("318"));
        this.out(e3.size());
        //从集合中过滤出满足"姓名包含隔壁"条件的新集合
        Collection<TestUserModel> e4 = CollectionUtils.where(data4, a -> a.getName().contains("隔壁"));
        this.out(e4.size());
    }

    @Test
    public void firstOrDefault(){
        Collection<TestUserModel> data = this.getData();

        //获取 集合中满足"年龄 > 20"条件的第一项
        TestUserModel e1 = CollectionUtils.firstOrDefault(data, a -> a.getAge() > 20);
        //获取 集合中满足"姓名包含王"条件的第一项
        TestUserModel e2 = CollectionUtils.firstOrDefault(data, a -> a.getName().contains("王"));
        //获取 集合中满足"id等于318"条件的第一项
        TestUserModel e3 = CollectionUtils.firstOrDefault(data, a -> a.getId() != null && a.getId().equals("318"));
        //获取 集合中满足"姓名包含王"条件的第一项
        TestUserModel e4 = CollectionUtils.firstOrDefault(data, a -> a.getName().contains("隔壁"));
    }

    @Test
    public void lastOrDefault(){
        Collection<TestUserModel> data = this.getData();

        //获取 集合中满足"年龄 > 20"条件的最后一项
        TestUserModel e1 = CollectionUtils.lastOrDefault(data, a -> a.getAge() > 10);
        //获取 集合中满足"姓名包含王"条件的最后一项
        TestUserModel e2 = CollectionUtils.lastOrDefault(data, a -> a.getName().contains("头"));
        //获取 集合中满足"id等于318"条件的最后一项
        TestUserModel e3 = CollectionUtils.lastOrDefault(data, a -> a.getId() != null && a.getId().equals("318"));
        //获取 集合中满足"姓名包含王"条件的最后一项
        TestUserModel e4 = CollectionUtils.lastOrDefault(data, a -> a.getName().contains("隔壁"));
    }

    @Test
    public void size(){
        Collection<TestUserModel> data = this.getData();

        //获取 集合中满足"年龄 > 20"条件的项的数量
        int e1 = CollectionUtils.size(data, a -> a.getAge() > 20);
        //获取 集合中满足"姓名包含王"条件的项的数量
        int e2 = CollectionUtils.size(data, a -> a.getName().contains("王"));
        //获取 集合中满足"id等于318"条件的项的数量
        int e3 = CollectionUtils.size(data, a -> a.getId() != null && a.getId().equals("318"));
        //获取 集合中满足"姓名包含王"条件的项的数量
        int e4 = CollectionUtils.size(data, a -> a.getName().contains("隔壁"));
    }

    @Test
    public void select(){
        Collection<TestUserModel> data = this.getData();
        List<TestUserModel> data2 = new ArrayList<>(data);
        ArrayList<TestUserModel> data3 = new ArrayList<>(data);
        Set<TestUserModel> data4 = new HashSet<>(data);

        Collection<Integer> s1 = CollectionUtils.select(data4, a -> a.getAge());

        s1.forEach(f -> System.out.println(f));
    }

    @Test
    public void toMap(){
        Collection<TestUserModel> data = this.getData();

        //以年龄为key,user为value,转换成map
        Map<Integer, TestUserModel> m1 = CollectionUtils.toMap(data, a -> a.getAge());

        //以姓名为key,user为value,转换成map
        Map<String, TestUserModel> m2 = CollectionUtils.toMap(data, a -> a.getName());
    }

    @Test
    public void groupBy(){
        Collection<TestUserModel> data = this.getData();

        Map<Integer, Collection<TestUserModel>> r1 = CollectionUtils.groupBy(data, a -> a.getAge());
        int a = 1;
    }

    @Test
    public void take() {
        Collection<Integer> list = new ArrayList<>();
        for (int k = 0; k < 10; k++) {
            list.add(k + 1);
        }
        Collection<Integer> r1 = CollectionUtils.take(list, 1);
        Collection<Integer> r2 = CollectionUtils.take(list, 2);
        Collection<Integer> r3 = CollectionUtils.take(list, 3);
        Collection<Integer> r4 = CollectionUtils.take(list, 4);
        Collection<Integer> r5 = CollectionUtils.take(list, 5);
        Collection<Integer> r6 = CollectionUtils.take(list, 6);
        Collection<Integer> r7 = CollectionUtils.take(list, 20);
    }

    @Test
    public void skip() {
        Collection<Integer> list = new ArrayList<>();
        for (int k = 0; k < 10; k++) {
            list.add(k + 1);
        }
        Collection<Integer> r1 = CollectionUtils.skip(list, 1);
        Collection<Integer> r2 = CollectionUtils.skip(list, 2);
        Collection<Integer> r3 = CollectionUtils.skip(list, 3);
        Collection<Integer> r4 = CollectionUtils.skip(list, 4);
        Collection<Integer> r5 = CollectionUtils.skip(list, 5);
        Collection<Integer> r6 = CollectionUtils.skip(list, 6);
        Collection<Integer> r7 = CollectionUtils.skip(list, 20);

    }

    @Test
    public void sort(){
        Collection<Integer> list = new ArrayList<>();
        for (int k = 0; k < 10; k++) {
            list.add(k + 1);
        }
        Collection<Integer> d1 = CollectionUtils.sort(list);
        d1.forEach(f -> {
            System.out.println(f);
        });

        Collection<Integer> d2 = CollectionUtils.sortDescending(list);
        d2.forEach(f -> {
            System.out.println(f);
        });
    }

    @Test
    public void sum(){
        Collection<Float> list = new ArrayList<>();
        short a = 1, b = 3;
        list.add(1F);
        list.add(3F);
        list.add(null);

        Float r1 = CollectionUtils.sum(list);
        System.out.println(r1);

        Collection<TestUserModel> users = new ArrayList<>();
        TestUserModel u1 = new TestUserModel();
        u1.setAge(20);
        u1.setMoney(new BigDecimal(101));
        u1.setHeight(99);
        users.add(u1);

        TestUserModel u2 = new TestUserModel();
        u2.setAge(18);
        u2.setMoney(new BigDecimal("20.01"));
        users.add(u2);

        TestUserModel u3 = new TestUserModel();
        u3.setAge(null);
        u3.setMoney(null);
        users.add(u3);

        Integer r11 = CollectionUtils.sum(users, u -> u.getAge());
        System.out.println(r11);

        BigDecimal r22 = CollectionUtils.sum(users, u -> u.getMoney());
        System.out.println(r22);

        int r33 = CollectionUtils.sum(users, u -> u.getHeight());
        System.out.println(r33);
    }

    @Test
    public void max(){
        Collection<TestUserModel> data = this.getData();

        //获取 集合中"年龄"属性的最大值
        Integer e1 = CollectionUtils.max(data, a -> a.getAge());
        System.out.println(e1);
    }

    @Test
    public void min(){
        Collection<TestUserModel> data = this.getData();

        //获取 集合中"年龄"属性的最小值
        Integer e1 = CollectionUtils.min(data, a -> a.getAge());
        System.out.println(e1);
    }

    @Test
    public void average(){
        Collection<Integer> list = new ArrayList<>();
        list.add(2);
        list.add(4);
        list.add(null);

        Integer r1 = CollectionUtils.average(list);
        System.out.println(r1);

        Collection<TestUserModel> users = new ArrayList<>();
        TestUserModel u1 = new TestUserModel();
        u1.setAge(20);
        u1.setMoney(new BigDecimal(101));
        u1.setHeight(99);
        users.add(u1);

        TestUserModel u2 = new TestUserModel();
        u2.setAge(19);
        u2.setMoney(new BigDecimal("20.01"));
        users.add(u2);

        TestUserModel u3 = new TestUserModel();
        u3.setAge(null);
        u3.setMoney(null);
        users.add(u3);

        Integer r11 = CollectionUtils.average(users, u -> u.getAge());
        System.out.println(r11);
    }

    @Test
    public void splitPage(){
        Collection<TestUserModel> data = this.getData();
        System.out.println("原始数据:");
        data.forEach(f -> this.out(f.getName()));
        System.out.println("***************");
        Collection<TestUserModel> r1 = CollectionUtils.splitPager(data,1,1);
        r1.forEach(f -> this.out(f.getName()));
        System.out.println("***************");
        Collection<TestUserModel> r2 = CollectionUtils.splitPager(data,1,2);
        r2.forEach(f -> this.out(f.getName()));
        System.out.println("***************");
        Collection<TestUserModel> r3 = CollectionUtils.splitPager(data,1,2, o -> o.getAge());
        r3.forEach(f -> this.out(f.getName()));
        System.out.println("***************");
        Collection<TestUserModel> r4 = CollectionUtils.splitPager(data,1,2, o -> o.getAge(), SortDirection.DESCENDING);
        r4.forEach(f -> this.out(f.getName()));
    }

    @Test
    public void castTo() throws Exception {
        List<Special1> d1 = new ArrayList<>();

        Special1 s1 = new Special1();
        s1.setId(318L);
        s1.setAge(94);
        d1.add(s1);

        Special1 s2 = new Special1();
        s2.setId(447L);
        s2.setAge(23);
        d1.add(s2);


        Collection<Target1> r1 = CollectionUtils.castTo(d1, Target1.class, (a, b) -> {
            b.setName(a.getAge().toString());
            b.setMoney(new BigDecimal(a.getAge()));
        });
        r1.forEach(f -> {
            System.out.println(f.toString());
        });
    }



    private Collection<TestUserModel> getData(){
        Collection<TestUserModel> data = new ArrayList<>();
        TestUserModel u1 = new TestUserModel();
        u1.setName("老王");
        u1.setAge(33);
        u1.setMoney(new BigDecimal(99));
        data.add(u1);

        TestUserModel u2 = new TestUserModel();
        u2.setName("大头儿子");
        u2.setAge(11);
        u2.setId("318");
        u2.setMoney(new BigDecimal(929));
        data.add(u2);

        TestUserModel u3 = new TestUserModel();
        u3.setName("小头爸爸");
        u3.setAge(22);
        u3.setMoney(new BigDecimal(399));
        data.add(u3);

        TestUserModel u4 = new TestUserModel();
        u4.setName("妈妈");
        u4.setAge(22);
        u4.setMoney(new BigDecimal(919));
        data.add(u4);

        List t1 = new ArrayList();

        return data;
    }
}
