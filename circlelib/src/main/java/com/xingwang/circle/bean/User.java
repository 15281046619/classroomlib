package com.xingwang.circle.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class User implements Serializable {

    /**
     * id : 1
     * phone : 184****1033
     * avatar : http://xw518app.oss-cn-beijing.aliyuncs.com/00444f9e6cb147059442ddf8446e0f221.png
     * nickname : 知乎
     * jifen : 99394
     * profile : 将坠的被噬而斑斓的颜色
     * badge : gov
     * create_time : 2019-05-17 15:49:10
     */

    private long id;
    private String phone;
    private String avatar;
    private String nickname;
    private int jifen;
    private String profile;
    private String badge;
    private String create_time;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAvatar() {
        return avatar==null?"":avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getJifen() {
        return jifen;
    }

    public void setJifen(int jifen) {
        this.jifen = jifen;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getBadge() {
        return badge;
    }

    public void setBadge(String badge) {
        this.badge = badge;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }
}
