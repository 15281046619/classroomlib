package com.xingwang.swip.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * 公共实体类，格式一致的话继承此类
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommonEntity<T> implements Serializable {
   private String message;
   private int status;
   private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message==null?"数据出错了～":message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
