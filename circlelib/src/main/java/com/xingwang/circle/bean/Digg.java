package com.xingwang.circle.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Digg implements Serializable {

    /**
     * id : 323
     * type : thread
     * relation_id : 91
     * customer_id : 42
     * digg_time : 2019-10-28 09:01:51
     */

    private String id;
    private String type;
    private int relation_id;
    private long user_id;
    private String digg_time;
    private User user;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getRelation_id() {
        return relation_id;
    }

    public void setRelation_id(int relation_id) {
        this.relation_id = relation_id;
    }

    public String getDigg_time() {
        return digg_time==null?"":digg_time;
    }

    public void setDigg_time(String digg_time) {
        this.digg_time = digg_time;
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
