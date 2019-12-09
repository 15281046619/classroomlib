package com.xingwang.classroom.ws;

import java.io.Serializable;

/**
 * 公共实体类，格式一致的话继承此类
 */
public class CommentEntity implements Serializable {
   private String event;
   private String scene;
   private String errno;
   private String errmsg;
   private String status;
   private Object data;

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getEvent() {
        return event==null?"":event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getScene() {
        return scene==null?"":scene;
    }

    public void setScene(String scene) {
        this.scene = scene;
    }

    public String getErrno() {
        return errno;
    }

    public void setErrno(String errno) {
        this.errno = errno;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
