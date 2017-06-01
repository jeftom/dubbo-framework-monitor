package com.framework.utils;

import com.framework.exceptions.UtilsException;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

/**
 * Map工具类
 * Created by 李溪林 on 17-2-16.
 */
public class MapUtils {

    private static final String currentClassName = "MapUtils";

    /**
     * 将Map(格式为String,Object)转换成指定类型实例
     * @param map 要转换的map
     * @param target 指定类型实例
     * @param matchWithDescription 是否用属性的 Description 注解进行匹配
     * @param ignoreConvertException 是否忽略类型转换异常
     * @param <T> 指定类型实例的类型
     * @return 指定类型实例
     */
    public static <T> T to(Map<String, Object> map, T target, boolean matchWithDescription, boolean ignoreConvertException){
        return to(map, target, matchWithDescription, ignoreConvertException, false);
    }

    /**
     * 将Map(格式为String,Object)转换成指定类型实例
     * @param map 要转换的map
     * @param target 指定类型实例
     * @param matchWithDescription 是否用属性的 Description 注解进行匹配
     * @param ignoreConvertException 是否忽略类型转换异常
     * @param isExcel 是否是excel的数据转换
     * @param <T> 指定类型实例的类型
     * @return 指定类型实例
     */
    public static <T> T to(Map<String, Object> map, T target, boolean matchWithDescription, boolean ignoreConvertException, boolean isExcel) {
        if (map == null) {
            return null;
        }
        if(target == null){
            throw new UtilsException(currentClassName + "中的方法[to]发生异常:目标对象不能为null.");
        }

        Class cls = target.getClass();
        PropertyDescriptor[] props = TypeUtils.getPropertyDescriptors(cls);
        for (PropertyDescriptor prop : props) {
            Method setter = prop.getWriteMethod();
            if (setter == null) {
                continue;
            }

            String name = matchWithDescription ? TypeUtils.getDescription(cls, prop) : prop.getName();
            if (!map.containsKey(name)) {
                continue;
            }

            Class targetPropType = setter.getParameterTypes()[0];
            Object tempValue = map.get(name);
            if (tempValue == null) {
                continue;
            }
            Class sourcePropType = tempValue.getClass();
            if (!TypeUtils.isAssignable(targetPropType, sourcePropType)) {
                if(isExcel){
                    //由于excel中的数字都会被识别成double,导致无法转换至model中的number属性
                    //由于excel中复制过去的数字会被识别成文本,导致无法转换成数字 或 时间
                    //因此为excel的map数据考虑,允许以下情况的转换:
                    //date to string / double to long / double to int / double to bigdecimal
                    //string to long / string to int /string to bigdecimal / string to date
                    if(targetPropType == String.class){
                        String the;
                        //date to string
                        if(sourcePropType == Date.class){
                            //自动按照年月日时分秒格式转换
                            the = DateUtils.format((Date)tempValue, "yyyy-MM-dd HH:mm:ss");
                        } else {
                            the = tempValue.toString();
                        }
                        try {
                            setter.invoke(target, the);
                        }catch (Exception e){
                            if(!ignoreConvertException){
                                throw new UtilsException(currentClassName + "中的方法[to]发生数据写入异常:["+ prop.getName() +"]属性写入失败.", e);
                            }
                        }

                        continue;
                    }

                    if(sourcePropType == Double.class){
                        if (targetPropType != Long.class && targetPropType != Integer.class && targetPropType != BigDecimal.class) {
                            continue;
                        }
                        if (!TypeUtils.isPublic(setter)) {
                            setter.setAccessible(true);
                        }
                        try {
                            if (targetPropType == Long.class) {
                                //double to long
                                Long spe = BigDecimal.valueOf((Double) tempValue).longValue();
                                setter.invoke(target, spe);
                            } else if (targetPropType == Integer.class) {
                                //double to integer
                                Integer spe = BigDecimal.valueOf((Double) tempValue).intValue();
                                setter.invoke(target, spe);
                            } else if (targetPropType == BigDecimal.class){
                                //double to bigdecimal
                                BigDecimal spe = BigDecimal.valueOf((Double) tempValue);
                                setter.invoke(target, spe);
                            }
                        } catch (Exception e) {
                            if (!ignoreConvertException) {
                                throw new UtilsException(currentClassName + "中的方法[to]发生数据写入异常:["+ prop.getName() +"]属性写入失败.", e);
                            }
                        }
                    }else if(sourcePropType == String.class){
                        if (targetPropType != Long.class && targetPropType != Integer.class && targetPropType != BigDecimal.class && targetPropType != Date.class) {
                            continue;
                        }
                        if (!TypeUtils.isPublic(setter)) {
                            setter.setAccessible(true);
                        }
                        try {
                            if (targetPropType == Long.class) {
                                //string to long
                                Long spe = new BigDecimal(tempValue.toString()).longValue();
                                setter.invoke(target, spe);
                            } else if (targetPropType == Integer.class) {
                                //string to integer
                                Integer spe = new BigDecimal(tempValue.toString()).intValue();
                                setter.invoke(target, spe);
                            } else if (targetPropType == BigDecimal.class){
                                //string to bigdecimal
                                BigDecimal spe = new BigDecimal(tempValue.toString());
                                setter.invoke(target, spe);
                            } else if(targetPropType == Date.class){
                                //string to date
                                Date spe = DateUtils.parse(tempValue.toString(), true);
                                setter.invoke(target, spe);
                            }
                        } catch (Exception e) {
                            if (!ignoreConvertException) {
                                throw new UtilsException(currentClassName + "中的方法[to]发生数据写入异常:["+ prop.getName() +"]属性写入失败.", e);
                            }
                        }
                    } else {
                        //其他情况
                    }
                }
            } else {
                if (!TypeUtils.isPublic(setter)) {
                    setter.setAccessible(true);
                }
                try {
                    setter.invoke(target, map.get(name));
                } catch (Exception e) {
                    if (!ignoreConvertException) {
                        throw new UtilsException(currentClassName + "中的方法[to]发生数据写入异常:["+ prop.getName() +"]属性写入失败.", e);
                    }
                }
            }
        }
        return target;
    }
}
