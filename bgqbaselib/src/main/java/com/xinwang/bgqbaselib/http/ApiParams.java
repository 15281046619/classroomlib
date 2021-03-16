package com.xinwang.bgqbaselib.http;

import java.util.HashMap;


public class ApiParams extends HashMap<String,Object> {
    public ApiParams with(String key, Object value) {
        put(key,value);
        return this;
    }
    @Override
    public String toString() {
        return super.toString();
    }

}
