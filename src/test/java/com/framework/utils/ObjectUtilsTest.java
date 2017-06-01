package com.framework.utils;

import com.framework.utils.model.Special1;
import com.framework.utils.model.Source1;
import com.framework.utils.model.Target1;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by 李溪林 on 17-2-16.
 */
public class ObjectUtilsTest {
    @Test
    public void copyPropertyTo1() throws Exception {
        Source1 s1 = new Source1();
        s1.setId(318L);
        s1.setName("李白");
        s1.setStartDate(new Date());
        s1.setMoney(new BigDecimal(999));
        s1.setCreaterId("691");
        s1.setCreaterName("落阳");

        Target1 t1 = new Target1();

        ObjectUtils.copyPropertyTo(s1, t1, false);
        System.out.println(t1.toString());

        Source1 s2 = new Source1();
        s2.setName("杜甫");
        Source1 t2 = new Source1();

        ObjectUtils.copyPropertyTo(s2, t2, false);
        System.out.println(t2.toString());
    }

    @Test
    public void copyPropertyTo2() throws Exception {
        Source1 s1 = new Source1();
        s1.setId(318L);
        s1.setName("李白");
        s1.setStartDate(new Date());
        s1.setMoney(new BigDecimal(999));

        Special1 p1 = new Special1();

        ObjectUtils.copyPropertyTo(s1, p1, false);
        System.out.println(p1.toString());
    }

    @Test
    public void copyPropertyTo3() throws Exception {
        Special1 s1 = new Special1();
        s1.setId(318L);
        s1.setStartDate(new Date());
        s1.setMoney(new BigDecimal(999));
        s1.setAge(18);
        s1.setCount(33);

        Target1 p1 = new Target1();

        ObjectUtils.copyPropertyTo(s1, p1, false);
        System.out.println(p1.toString());
    }

    @Test
    public void copyPropertyTo4() throws Exception {
        Special1 s1 = new Special1();
        s1.setId(318L);
        s1.setAge(94);

        Target1 p1 = new Target1();

        ObjectUtils.copyPropertyTo(s1, p1, (a, b) -> {
            b.setName(a.getAge().toString());
            b.setMoney(new BigDecimal(a.getAge()));
        });

        System.out.println(p1.toString());
    }
}
