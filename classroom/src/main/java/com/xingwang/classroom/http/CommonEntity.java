package com.xingwang.classroom.http;

import java.io.Serializable;

/**
 * 公共实体类，格式一致的话继承此类
 */
public class CommonEntity implements Serializable {

   private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private int status;





    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
