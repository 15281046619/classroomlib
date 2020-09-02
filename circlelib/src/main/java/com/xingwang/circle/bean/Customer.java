package com.xingwang.circle.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
@JsonIgnoreProperties(ignoreUnknown = true)
public class Customer implements Serializable {

    /**
     * id : 1
     * nickname : 微信大佬
     * avatar : http://xw518app.oss-cn-beijing.aliyuncs.com/6aefe21f837647d899aaadfea1dbc14c1.png
     * team_id : 0
     * fullname :
     * remark :
     * create_time : 2019-05-17 15:49:10
     * product_line :
     * phone : 184****1033
     * area :
     * jifen : 111
     * profile : 果皇果后好
     */

    private String id;
    private String nickname;
    private String avatar;
    private String team_id;
    private String fullname;
    private String remark;
    private String create_time;
    private String product_line;
    private String phone;
    private String area;
    private String jifen;
    private String profile;
    private String badge;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname==null?"无昵称":nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatar() {
        return avatar==null?"":avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getTeam_id() {
        return team_id;
    }

    public void setTeam_id(String team_id) {
        this.team_id = team_id;
    }

    public String getFullname() {
        return fullname==null?"":fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCreate_time() {
        return create_time==null?"":create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getProduct_line() {
        return product_line;
    }

    public void setProduct_line(String product_line) {
        this.product_line = product_line;
    }

    public String getPhone() {
        return phone==null?"":phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getJifen() {
        return jifen;
    }

    public void setJifen(String jifen) {
        this.jifen = jifen;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getBadge() {
        return badge==null?"":badge;
    }

    public void setBadge(String badge) {
        this.badge = badge;
    }
}
