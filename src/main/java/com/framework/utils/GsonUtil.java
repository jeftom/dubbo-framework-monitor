package com.framework.utils;

import com.google.gson.Gson;

/**
 * Gson工具类
 * Created by yuanjinglin on 17/3/15.
 */
public class GsonUtil {

    public static String toJson(Object o){
        Gson gson=new Gson();
        return gson.toJson(o);
    }
}
