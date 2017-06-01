package com.framework.utils;

import com.framework.exceptions.UtilsException;
import org.junit.Test;

/**
 * @Author: 李溪林
 * @Descriptions:
 * @CreateDate: 17/3/2
 */
public class TryingTest {

    @Test
    public void Try() {

        //以"忽略异常"的方式执行一段代码
        Trying.Try(() -> {
            System.out.println("123");
        });

        //以"忽略try异常,但不忽略cath异常"的方式执行一段代码,
        Trying.Try(() -> {
            //这是要执行的代码,这里的异常会被忽略
            System.out.println("456");
            //故意抛出异常
            Long b = null;
            String c = b.toString();
        }, a -> {
            //这是万一 try 里异常了,才要执行的代码,这里的异常是否忽略取决于 最后参数
            //并且 匿名方法 中的变量 a ,表示 try 中的 Exception 对象
            System.out.println("789");
            System.out.println("***");
            System.out.println("try中发生了异常:" + a.getMessage());
        },false);
    }

    @Test
    public void try2(){
        Integer a1 = Trying.Try(() -> { return this.getInteger(); }, null);
        System.out.println(a1);

        int a2 = Trying.Try(() -> { return this.getInt(); }, 0);
        System.out.println(a2);

        int a3 = Trying.Try(() -> { return this.getIntWithException(); }, 0);
        System.out.println(a3);
    }

    private Integer getInteger(){
        return 318;
    }

    private int getInt(){
        return 1;
    }

    private int getIntWithException(){
        throw new UtilsException("强制抛出异常.");
    }

}
