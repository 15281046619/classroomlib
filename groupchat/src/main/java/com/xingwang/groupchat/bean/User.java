package com.xingwang.groupchat.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class User {

    /**
     * id : 42
     * phone : 182****9759
     * avatar : http://xw518app.oss-cn-beijing.aliyuncs.com/dc2d09c458664b17b08d0e974429308542.pngzy
     * nickname : 像个猪儿虫
     * jifen : 438
     * profile : 你好
     * badge : 官方人员
     * create_time : 2019-06-19 11:44:20
     * state : 1
     */

    private long id;
    private String phone;
    private String avatar;
    private String nickname;
    private int jifen;
    private String profile;
    private String badge;
    private String create_time;
    private int is_official;
    private int is_manager;
    private int is_hidden;
    private int state;

    //是否被选中
    private boolean isSelect=false;
    //是否为群成员
    private boolean isGroup=false;

    public long getId() {
        return id;
    }

    public String getStrId() {
        return String.valueOf(id);
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
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getNickname() {
        return nickname==null?"":nickname;
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

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public boolean isGroup() {
        return isGroup;
    }

    public void setGroup(boolean group) {
        isGroup = group;
    }

    public int getIs_official() {
        return is_official;
    }

    public void setIs_official(int is_official) {
        this.is_official = is_official;
    }

    public int getIs_manager() {
        return is_manager;
    }

    public void setIs_manager(int is_manager) {
        this.is_manager = is_manager;
    }

    public int getIs_hidden() {
        return is_hidden;
    }

    public int getHiddenFlag(){
        if (is_hidden==0)
            return 1;
        return 0;
    }

    public int getManagerFlag(){
        if (is_manager==0)
            return 1;
        return 0;
    }

    public void setIs_hidden(int is_hidden) {
        this.is_hidden = is_hidden;
    }
}
