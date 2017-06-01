package com.nfsq.framework.tools;

import org.junit.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by guoyongzheng on 14-12-16.
 */
public class CommonStopWatchTest {

    @Test
    public void testWatch() throws Exception{
        CommonStopWatch watch = new CommonStopWatch();
        Thread.sleep(1000L);
        watch.addWatch(null);
        Thread.sleep(1000L);
        watch.addWatch(null);
        watch.pause();
        Thread.sleep(1000L);
        watch.restart();
        Thread.sleep(1000L);
        long time = watch.print();

        assertTrue(time > 3L*1000*1000*1000);
        assertTrue(time < 4L * 1000 * 1000 * 1000);
    }

    //测试bigDecimal —— 跟本类无关
    @Test
    public void testBigDecimal() {
        BigDecimal d = new BigDecimal("10.23585871125");

        assertEquals("10.24", d.setScale(2, RoundingMode.HALF_UP).toString());

        d = BigDecimal.ZERO.subtract(new BigDecimal("10.23585871125"));
        assertEquals("-10.24", d.setScale(2, RoundingMode.HALF_UP).toString());

        d = new BigDecimal("0.495");
        assertEquals("0.50", d.setScale(2, RoundingMode.HALF_UP).toString());

        d = BigDecimal.ZERO.subtract(new BigDecimal("0.495"));
        assertEquals("-0.50", d.setScale(2, RoundingMode.HALF_UP).toString());

    }
}
