package com.nfsq.framework.utils;

import org.junit.Test;

import java.util.List;
import java.util.Set;

/**
 * Created by 李溪林 on 17-2-15.
 */
public class StringUtilsTest {

    @Test
    public void isNullOrEmpty(){
        String[] strs = new String[]{null, "", "  ", "12"};
        for(String item : strs){
            Boolean r = StringUtils.isNullOrEmpty(item);
            System.out.println("判定对象:" + item + ",判定结果:" + r);
        }
    }

    @Test
    public void isNullOrWhiteSpace(){
        String[] strs = new String[]{null, "", "  ", "12"};
        for(String item : strs){
            Boolean r = StringUtils.isNullOrWhiteSpace(item);
            System.out.println("判定对象:" + item + ",判定结果:" + r);
        }
    }

    @Test
    public void split(){
        String str = "1,2,3,4,5,8,,,9,";

        String[] a1 = StringUtils.split(str, ",", false);
        String[] a2 = StringUtils.split(str, ",", true);

        System.out.println("分割并保留空元素,长度:" + a1.length);
        for(String item : a1){
            System.out.println("每项:" + item);
        }
        System.out.println("分割并移除空元素,长度:" + a2.length);
        for(String item : a2){
            System.out.println("每项:" + item);
        }
    }

    @Test
    public void toIntList() {
        String str = "1,2,3,4,5,8,9,1o";

        List<Integer> i1 = StringUtils.toIntList(str,",", true);
        for (Integer item : i1){ System.out.println(item); }
        try
        {
            List<Integer> i2 = StringUtils.toIntList(str, ",", false);
            for (Integer item : i2){ System.out.println(item); }
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void toIntSet() {
        String str = "1,2,3,4,5,8,19,1o";

        Set<Integer> i1 = StringUtils.toIntSet(str,",", true);
        for (Integer item : i1){ System.out.println(item); }
        try
        {
            Set<Integer> i2 = StringUtils.toIntSet(str, ",", false);
            for (Integer item : i2){ System.out.println(item); }
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void toLongList() {
        String str = "1,2,3,4,5,8,19,1o";

        List<Long> i1 = StringUtils.toLongList(str,",", true);
        for (Long item : i1){ System.out.println(item); }
        try
        {
            List<Long> i2 = StringUtils.toLongList(str, ",", false);
            for (Long item : i2){ System.out.println(item); }
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void toLongSet() {
        String str = "1,2,3,4,5,8,9,1o";

        Set<Long> i1 = StringUtils.toLongSet(str,",", true);
        for (Long item : i1){ System.out.println(item); }
        try
        {
            Set<Long> i2 = StringUtils.toLongSet(str, ",", false);
            for (Long item : i2){ System.out.println(item); }
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void getByteLength(){
        String str1 = "123de z", str2 = "abc d", str3 = "我擦了-qwe";
        long l1 = StringUtils.getByteLength(str1);
        System.out.println(l1);
        long l2 = StringUtils.getByteLength(str2);
        System.out.println(l2);
        long l3 = StringUtils.getByteLength(str3);
        System.out.println(l3);
    }
}
