package com.framework.tools;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

/**
 * java8 特性的工具类
 *
 * Created by guoyongzheng on 15/12/2.
 */
public class Java8Support {

    /**
     * 私有化构造函数
     */
    private Java8Support(){}

    /**
     * 从legacy的Date对象转化为ZonedDateTime对象
     * @param date
     * @return
     */
    public static ZonedDateTime transferZonedDateTime(Date date) {
        if (date == null) {
            return null;
        }

        return ZonedDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    /**
     * 从legacy的Date对象转为LocalDateTime
     * @param date
     * @return
     */
    public static LocalDateTime transferDateTime(Date date) {

        if (date == null) {
            return null;
        }

        return transferZonedDateTime(date).toLocalDateTime();
    }
}
