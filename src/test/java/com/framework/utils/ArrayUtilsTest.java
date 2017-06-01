package com.framework.utils;

import com.framework.exceptions.UtilsException;
import com.framework.utils.model.TestUserModel;
import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by 李溪林 on 17-2-15.
 */
public class ArrayUtilsTest {

    @Test
    public void toList(){

        String[] a1 = new String[]{"1","2"," 3 "};
        List<String> l1 = ArrayUtils.toList(a1);
        l1.add("7");
        for(String item : l1){
            System.out.println(item);
        }

        Integer[] a2 = new Integer[]{1,2,5,7,8};
        List<Integer> l2 = ArrayUtils.toList(a2);
        for(Integer item : l2){
            System.out.println(item);
        }

        Double[] a3 = new Double[]{1.31D,2.56D,5.67D,7.01D,8.98D};
        List<Double> l3 = ArrayUtils.toList(a3);
        for(Double item : l3){
            System.out.println(item);
        }
    }

    @Test
    public void toSet(){

        String[] a1 = new String[]{"1","2"," 3 "};
        Set<String> l1 = ArrayUtils.toSet(a1);
        for(String item : l1){
            System.out.println(item);
        }

        Integer[] a2 = new Integer[]{1,2,5,7,8};
        Set<Integer> l2 = ArrayUtils.toSet(a2);
        for(Integer item : l2){
            System.out.println(item);
        }

        Double[] a3 = new Double[]{1.31D,2.56D,5.67D,7.01D,8.98D};
        Set<Double> l3 = ArrayUtils.toSet(a3);
        for(Double item : l3){
            System.out.println(item);
        }
    }

    @Test
    public void toIntArray() {
        String[] arr1 = new String[]{"1","2","3","4","5"};

        try{
            Integer[] r1 = ArrayUtils.toIntArray(arr1, false);
            for(Integer item : r1){ System.out.println(item.toString()); }
        }
        catch (UtilsException e1){
            //
        }

        String[] arr2 = new String[]{"1","2","3","4","5", "1o", "abc"};

        try{
            //保留异常
            Integer[] r2 = ArrayUtils.toIntArray(arr2, false);
            for(Integer item : r2){ System.out.println(item.toString()); }
        }
        catch (UtilsException e1){
            String message = e1.getMessage();
            System.out.println(message);
        }

        try{
            //忽略异常
            Integer[] r3 = ArrayUtils.toIntArray(arr2);
            for(Integer item : r3){ System.out.println(item.toString()); }
        }
        catch (UtilsException e1){
            //
        }
    }

    @Test
    public void all(){
        Integer[] a1 = new Integer[]{1,2,5,7,8};
        boolean r1 = ArrayUtils.all(a1, a -> a > 0);
        System.out.println(r1);
        boolean r2 = ArrayUtils.all(a1, a -> a > 1);
        System.out.println(r2);
    }

    @Test
    public void any(){
        Integer[] a1 = new Integer[]{1,2,5,7,8};
        boolean r1 = ArrayUtils.any(a1, a -> a > 7);
        System.out.println(r1);
        boolean r2 = ArrayUtils.any(a1, a -> a > 10);
        System.out.println(r2);
    }

    @Test
    public void firstOrDefault(){
        String[] a1 = new String[]{"1","2"," 3 "};

        String r1 = ArrayUtils.firstOrDefault(a1, f -> f.equals("2"));
        System.out.println(r1);

        Integer[] a2 = new Integer[]{1,2,5,7,8};

        Integer r2 = ArrayUtils.firstOrDefault(a2, f -> f > 6);
        System.out.println(r2);

        Integer r3 = ArrayUtils.firstOrDefault(a2, f -> f > 9);
        System.out.println(r3);
    }

    @Test
    public void lastOrDefault(){
        Integer[] a1 = new Integer[]{1,2,5,7,8};
        Integer r1 = ArrayUtils.lastOrDefault(a1, a -> a > 5);
        System.out.println(r1);
        Integer r2 = ArrayUtils.lastOrDefault(a1, a -> a > 10);
        System.out.println(r2);
    }

    @Test
    public void size(){
        Integer[] a1 = new Integer[]{1,2,5,7,8};
        int r1 = ArrayUtils.size(a1, a -> a > 2);
        System.out.println(r1);
        int r2 = ArrayUtils.size(a1, a -> a > 10);
        System.out.println(r2);
    }

    @Test
    public void toMap(){
        Integer[] a1 = new Integer[]{1,2,5,7,8};
        Map<Integer, Integer> r1 = ArrayUtils.toMap(a1, a -> { return a + 1; });

        for(Map.Entry<Integer, Integer> item : r1.entrySet()){
            System.out.println(item.getKey());
            System.out.println(item.getValue());
        }
    }

    @Test
    public void toMap1(){
        TestUserModel[] d1 = new TestUserModel[3];

        TestUserModel t1 = new TestUserModel();
        t1.setName("测试1");
        t1.setAge(22);
        d1[0] = t1;

        TestUserModel t2 = new TestUserModel();
        t2.setName("测试2");
        t2.setAge(33);
        d1[1] = t2;

        TestUserModel t3 = new TestUserModel();
        t3.setName("测试3");
        t3.setAge(44);
        d1[2] = t3;

        Map<String, Integer> r1 = ArrayUtils.toMap(d1, a -> { return a.getName(); }, b -> { return b.getAge(); });
        r1.forEach((key, value) -> {
            System.out.println(key);
            System.out.println(value);
            System.out.println("***");
        });
    }
}
