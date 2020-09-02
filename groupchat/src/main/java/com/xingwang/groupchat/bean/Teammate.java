package com.xingwang.groupchat.bean;


import com.xingwang.groupchat.view.lookup.LookUpBean;

import java.io.Serializable;

/**
 * 队员
 *         "id":"2",
 *         "team_id":"28",
 *         "name":"陈广",
 *         "password":"123456",
 *         "avatar":null,
 *         "phone":null,
 *         "state":"1"
 * */

public class Teammate extends LookUpBean implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    //员工id
    private String team_id;
    private String phone;
    private String name;
    private String avatar;
    private String password;
    private String state;
    //团队id
    private String organization_id;
 /*   //职位
    @Transient
    private Position position;
    private String pos;
*/
    private int type;
    private boolean isClick=true;//是不是能够点击默认可以点击 ，如果邀请过不能点击
    private boolean isOnline = false;

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }

    public boolean getClick() {
        return isClick;
    }

    public void setClick(boolean click) {
        isClick = click;
    }

    public int getType() {
        return type;
    }
    public void setType(int type) {
        this.type = type;
    }

    //建群时是否被选中 true选中
    private boolean isCheck=false;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTeam_id() {
        return team_id==null?"":team_id;
    }

    public void setTeam_id(String team_id) {
        this.team_id = team_id;
    }

    public String getPhone() {
        return phone==null?"":phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name==null?"":name.replace("(","").replace("[","").replace(")","").replace("]","");
    }
    public String getShowName() {
        return name==null?"用户"+team_id:name.replace("(","").replace("[","").replace(")","").replace("]","");

    }

    public void setName(String name) {
        this.name =name;
    }

    public String getAvatar() {
        return avatar==null?"":avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }


    public String getTarget() {
        return name.replace("(","").replace("[","").replace(")","").replace("]","");

    }


    public boolean getIsCheck() {
        return this.isCheck;
    }

    public void setIsCheck(boolean isCheck) {
        this.isCheck = isCheck;
    }

    public String getOrganization_id() {
        return organization_id;
    }

    public void setOrganization_id(String organization_id) {
        this.organization_id = organization_id;
    }

    @Override
    public String getLookUpKey() {
        return getShowName()+(isOnline()?"在线":"离线")+getPhone();
    }

}
