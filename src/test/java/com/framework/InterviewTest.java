package com.framework;

import junit.framework.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

/**
 * 面试题目
 *
 * Created by guoyongzheng on 15/5/12.
 */
public class InterviewTest {

    @Test
    public void testIter() {
        //遍历一个map
        Map<String, String> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", "value2");
        map.put("key3", "value3");
        map.put("key4", "value4");

        //遍历map
        //todo ...

        //遍历一个list
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);

        //遍历list
        //todo ...


        //从list中删掉所有的偶数
        //todo ...

    }

    //有一个数列，第一项是1，第二项是1，第三项是2 ＝1+1 ，第四项是3 ＝1+2，第五项是5 ＝2+3 ...
    // 即：每一项是前两项之和。
    //指定数字n，求数列的第n项。
    @Test
    public void testFib () {
        int n = 10;

        //打印出数列的第10项
        //todo

    }

    @Test
    public void testStream() {
        List<Integer> list = new ArrayList<>();

        list.add(1);
        list.add(2);

        int index = list.indexOf(list.stream().filter(i -> i == 2).findFirst().get());

        assertEquals(index, 1);


    }

    @Test
    public void testTest() {
        short s1 = 1;
        s1 += 1;

        //wrong: incompatible types
        //s1 = s1 + 1;

        String str[] = new String[] {"aa", "bb"};

        int l = str.length;

        // array doesn't have length() method.
        //l = str.length();

        int a = 10;
        String s = "10";

        Integer b = Integer.valueOf(a);
        int x = Integer.parseInt(s);
        String y = String.valueOf(a);

        String test = "<<楼主说的非常对！\\\\和谐社会，科学上网>=。";

        test = test.replaceAll("[<>\\\\=]", "");

        assertEquals("楼主说的非常对！和谐社会，科学上网。", test);

        int[]  array = new int[] {7, 6, 4, 2, 1};
        array = bubbleSort(array);
        Integer[] arrayI = new Integer[array.length];
        for (int i = 0; i < array.length; i++) {
            arrayI[i] = Integer.valueOf(array[i]);
        }
        assertEquals("[1, 2, 4, 6, 7]", Arrays.asList(arrayI).toString());

        array = new int[] {2, 1};
        array = bubbleSort(array);
        arrayI = new Integer[array.length];
        for (int i = 0; i < array.length; i++) {
            arrayI[i] = Integer.valueOf(array[i]);
        }
        assertEquals("[1, 2]", Arrays.asList(arrayI).toString());

        array = new int[] {2};
        array = bubbleSort(array);
        arrayI = new Integer[array.length];
        for (int i = 0; i < array.length; i++) {
            arrayI[i] = Integer.valueOf(array[i]);
        }
        assertEquals("[2]", Arrays.asList(arrayI).toString());
    }

    public static class Singleton{
        // 私有化构造函数避免显式初始化
        private Singleton(){}

        private static Singleton instance = new Singleton();

        public static Singleton getInstance() {
            return instance;
        }
    }

    public int[] bubbleSort(int[] array) {

        if (array.length == 0) {
            return array;
        }

        for (int i = array.length; i > 0; i--) {
            for (int j = 0; j + 1 < i; j++) {
                if (array[j] > array[j + 1]) {
                    // 交换array[j]和array[j+1]的顺序
                    int temp = array[j];
                    array[j] = array[j + 1];
                    array[j + 1] = temp;
                }
            }
        }

        return array;
    }

    @Test
    public void testFloat() {
        float f = 3.9f;
        float g = 1.3f * 3;
        assertFalse(f == g);
    }

    @Test
    public void testReflect() {
        List<String> ls = new ArrayList<>();
        Assert.assertEquals(ls.getClass(), ArrayList.class);
    }

}