package com.nfsq.framework.tools;

import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import static org.junit.Assert.assertEquals;

/**
 * Created by guoyongzheng on 15/12/2.
 */
public class Java8SupportTest {

    @Test
    public void testTransferDateTime() {
        Date date = new Date();

        LocalDateTime rs = Java8Support.transferDateTime(date);

        System.out.println(rs);

        LocalDate ld = rs.toLocalDate();

        assertEquals(ld, LocalDateTime.now().toLocalDate());
    }
}
