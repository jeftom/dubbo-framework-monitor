package com.nfsq.framework.utils;

import com.nfsq.framework.utils.model.TestUserModel;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by 李溪林 on 17-2-16.
 */
public class ListUtilsTest {

    @Test
    public void toSet() {
        List<String> l1 = new ArrayList();
        l1.add("1");
        l1.add("2fd");

        Set<String> s1 = ListUtils.toSet(l1);
        for (String item : s1) {
            System.out.println(item);
        }
    }

    @Test
    public void forEach(){
        List<TestUserModel> data = this.getData();
        ListUtils.forEach(data, f -> {
            System.out.println(f.getName());
        });
    }

    @Test
    public void take() {
        List<Integer> list = new ArrayList<>();
        for (int k = 0; k < 10; k++) {
            list.add(k + 1);
        }
        List<Integer> r1 = ListUtils.take(list, 1);
        List<Integer> r2 = ListUtils.take(list, 2);
        List<Integer> r3 = ListUtils.take(list, 3);
        List<Integer> r4 = ListUtils.take(list, 4);
        List<Integer> r5 = ListUtils.take(list, 5);
        List<Integer> r6 = ListUtils.take(list, 6);
        List<Integer> r7 = ListUtils.take(list, 20);

    }

    @Test
    public void skip() {
        List<Integer> list = new ArrayList<>();
        for (int k = 0; k < 10; k++) {
            list.add(k + 1);
        }
        List<Integer> r1 = ListUtils.skip(list, 1);
        List<Integer> r2 = ListUtils.skip(list, 2);
        List<Integer> r3 = ListUtils.skip(list, 3);
        List<Integer> r4 = ListUtils.skip(list, 4);
        List<Integer> r5 = ListUtils.skip(list, 5);
        List<Integer> r6 = ListUtils.skip(list, 6);
        List<Integer> r7 = ListUtils.skip(list, 20);
    }

    @Test
    public void take_skip() {
        List<Integer> list = new ArrayList<>();
        for (int k = 0; k < 12; k++) {
            list.add(k + 1);
        }
        int index = 1, size = 4;

        for (int k = index; k <= 3; k++) {
            List<Integer> r1 = ListUtils.take(ListUtils.skip(list, (index - 1) * size), size);
            index++;
        }
    }

    @Test
    public void toMap(){
        List<TestUserModel> data = this.getData();
        System.out.println("原始数据:" + data.size());

        Map<String, TestUserModel> ss = ListUtils.toMap(data, (a) -> { return a.getId(); });
    }



    @Test
    public void batch(){

        List<TestUserModel> data = this.getData();
        System.out.println("原始数据:" + data.size());
        List<Integer> total = ListUtils.batchWithReturn(data, 2, a -> a.size(), null, true);

        total.forEach(f -> System.out.println("分批数量:" + f));
    }

    @Test
    public void select(){
        List<TestUserModel> data = this.getData();

        List<Integer> s1 = ListUtils.select(data, a -> a.getAge());

        s1.forEach(f -> System.out.println(f));
    }

    private List<TestUserModel> getData(){
        List<TestUserModel> data = new ArrayList<>();
        TestUserModel u1 = new TestUserModel();
        u1.setId(UUID.randomUUID().toString());
        u1.setName("老王");
        u1.setAge(33);
        u1.setMoney(new BigDecimal(99));
        data.add(u1);

        TestUserModel u2 = new TestUserModel();
        u2.setId(UUID.randomUUID().toString());
        u2.setName("大头儿子");
        u2.setAge(11);
        u2.setId("318");
        u2.setMoney(new BigDecimal(929));
        data.add(u2);

        TestUserModel u3 = new TestUserModel();
        u3.setId(UUID.randomUUID().toString());
        u3.setName("小头爸爸");
        u3.setAge(22);
        u3.setMoney(new BigDecimal(399));
        data.add(u3);

        TestUserModel u4 = new TestUserModel();
        u4.setId(UUID.randomUUID().toString());
        u4.setName("妈妈");
        u4.setAge(22);
        u4.setMoney(new BigDecimal(919));
        data.add(u4);

        List t1 = new ArrayList();

        return data;
    }
}
