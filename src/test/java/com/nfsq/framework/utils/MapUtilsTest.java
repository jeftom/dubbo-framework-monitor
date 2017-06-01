package com.nfsq.framework.utils;

import com.nfsq.framework.utils.model.TestUserModel;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 李溪林 on 17-2-16.
 */
public class MapUtilsTest {

    @Test
    public void to1() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", "aaaaa");
        map.put("name", "bbbb");
        map.put("sex", true);
        map.put("age", 318);
        map.put("birthDay", "2017-10-12");
        map.put("money", "99.0");

        TestUserModel obj1 = new TestUserModel();
        MapUtils.to(map,obj1,false, true, false);
        System.out.println(obj1);

        TestUserModel obj2 = new TestUserModel();
        MapUtils.to(map,obj2,false, true, true);
        System.out.println(obj2);
    }
}
