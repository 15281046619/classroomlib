package com.xingwang.groupchat.bean;

import android.content.Context;

import com.beautydefinelibrary.BeautyDefine;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.xingwang.groupchat.view.lookup.LookUpBean;


import java.io.Serializable;
import java.util.List;

/**
* 群
* */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Group extends LookUpBean implements Serializable {

    /**
     * id : 24
     * team_id : 78 //群主的id
     * title : 吃土群
     * introduction : 你财富大家2
     * avatar : http://attach.kf.xw518.com/2019/03-07/17/5c80e1e3a7c98.jpg
     */
    private long id;
    //群主id
    private long user_id;
    private String title;
    private String introduction;
    private String avatar;
    private int state;
    private List<String> team_ids;
    private String teamIdjson;
    //是否是群主,默认 不是
    private boolean leader=false;

    private boolean isCheck=false;

    public long getId() {
        return id;
    }

    public String getStrId() {
        return String.valueOf(id);
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title==null?"":title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIntroduction() {
        return introduction==null?"":introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public long getUser_id() {
        return user_id;
    }

    public String getStrUser_id() {
        return String.valueOf(user_id);
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public List<String> getTeam_ids() {
        return team_ids;
    }

    public void setTeam_ids(List<String> team_ids) {
        this.team_ids = team_ids;
    }


    public void setTeamIdjson(String teamIdjson) {
        this.teamIdjson = teamIdjson;
    }

    public String getTeamIdjson() {
        return teamIdjson;
    }

    //群主&管理员
    public boolean getLeader(Context context) {
        //if (getStrUser_id().equals(BeautyDefine.getUserInfoDefine(context).getUserId()))
        //if (getUser_id()==42)
            return true;
        //return false;
    }


    @Override
    public String getLookUpKey() {
        return getTitle();
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public boolean getIsCheck() {
        return this.isCheck;
    }

    public void setIsCheck(boolean isCheck) {
        this.isCheck = isCheck;
    }
}
