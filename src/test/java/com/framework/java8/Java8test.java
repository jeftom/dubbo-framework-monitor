package com.framework.java8;


import com.google.common.collect.Lists;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by yuanjinglin on 17/7/3.
 */
public class Java8test {
    public static void main(String[] args){
        List<String> names = Arrays.asList("peter", "anna", "mike", "xenia","pppp");
        List<String> upcaseNames =names.stream().map(a -> a.toUpperCase()).collect(Collectors.toList());
        List<String> sortNames=names.stream().sorted((a,b)->a.compareTo(b)).collect(Collectors.toList());
        List<String> filterNames=names.stream().filter(a-> a.startsWith("p")).collect(Collectors.toList());
        String name=names.stream().filter(a-> a.startsWith("p")).findFirst().get();
        String name1=names.stream().filter(a-> a.startsWith("p")).findAny().get();
        long count=names.stream().filter(a-> a.startsWith("p")).count();
        List<String> filterNames1=names.stream()
                .filter(a->{
                            String b=a.toUpperCase();
                            return b.startsWith("X");
                            }).collect(Collectors.toList());

        List<Integer> nums = Lists.newArrayList(1,1,null,2,3,4,null,5,6,7,8,9,10);
        System.out.println("sum is:"+nums.stream().filter(num -> num != null).
        distinct().mapToInt(num -> num * 2).
        peek(System.out::println).skip(2).limit(4).sum());

        Set<String> namesSet=names.stream().sorted((a, b)->a.compareTo(b)).collect(Collectors.toSet());
        //reduce
        List<Integer> ints = Lists.newArrayList(1,2,3,4,5,6,7,8,9,10);
        Integer sumTotal=ints.stream().reduce(0, (sum, item) -> sum + item);
        Optional<Integer> sumopt=  ints.stream().reduce((sum, item) -> sum + item);



        //转换map
        User[] users={new User(0L, "张三", 18,1)
                ,new User(1L, "lwhy", 19,1)
                ,new User(2L, "op", 20,0)
                ,new User(3L, "zp", 18,0)
                ,new User(4L, "li", 19,1)};

        //reducing
        int count1=Stream.of(users).collect(Collectors.reducing(0,User::getAge,Integer::sum));

        Optional<User> orderUsers = Stream.of(users).collect(Collectors.maxBy(Comparator.comparing(User::getAge)));
        //直接返回对象
        User user = Stream.of(users).collect(Collectors.collectingAndThen(
                Collectors.maxBy(Comparator.comparing(User::getAge)),Optional::get)
        );
        //直接返回其他类型对象
        Person person=Stream.of(users).collect(Collectors.collectingAndThen(
                Collectors.maxBy(Comparator.comparing(User::getAge)),a->{return new Person(a.get());}));
        Map<Long,User> map=Stream.of(users).collect(Collectors.toMap(User::getId, a->a));
        //换种写法,自定义key(注意stream只能消费一次)
        Map<String,User> map1=Stream.of(users).collect(Collectors.toMap(a->{return a.getId()+a.getName();},a->a));
        //按年龄分组
        Map<Integer,List<User>> groupMap=Stream.of(users).collect(Collectors.groupingBy(User::getAge));
        //分组count
        Map<Integer,Long> groupCountMap=Stream.of(users).collect(Collectors.groupingBy(User::getAge,Collectors.counting()));
        //分组sum
        Map<Integer,Integer> groupSumMap=Stream.of(users).collect(Collectors.groupingBy(User::getAge,Collectors.summingInt(User::getAge)));
        Map<Integer,Integer> groupSumMap2=Stream.of(users).collect(Collectors.groupingBy(User::getAge,Collectors.reducing(0,User::getAge,Integer::sum)));
        //分组取最大
        Map<Integer,Optional<User>> groupMaxMap=Stream.of(users).collect(Collectors.groupingBy(User::getSex,Collectors.maxBy(Comparator.comparing(User::getAge))));
        //collectingAndThen转换
        Map<Integer, User> res = Stream.of(users).collect(
                Collectors.groupingBy(User::getSex, Collectors.collectingAndThen(
                        Collectors.maxBy(Comparator.comparingInt(User::getAge)),
                        Optional::get  // 为转换函数，转换最终的数据
                ))
        );

        //分区
        Map<Boolean,List<User>> parMap=Stream.of(users).collect(Collectors.partitioningBy(a-> a.getName().startsWith("l")));
        //分区再分组
        Map<Boolean,Map<Integer,List<User>>> parGruMap=Stream.of(users).collect(Collectors.partitioningBy(a-> a.getName().startsWith("l"),Collectors.groupingBy(User::getSex)));

        //mapping
        Set<String> set=Stream.of(users).collect(Collectors.mapping(User::getName,Collectors.toSet()));
        List<String> listMapping=Stream.of(users).collect(Collectors.mapping(User::getName,Collectors.toList()));



    }

}
