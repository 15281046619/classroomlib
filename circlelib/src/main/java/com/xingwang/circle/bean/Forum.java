package com.xingwang.circle.bean;

import com.blankj.utilcode.util.EmptyUtils;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.List;
@JsonIgnoreProperties(ignoreUnknown = true)
public class Forum implements Serializable {

    /**
     * id : 1
     * pid : 0
     * title : 牲畜
     * categorys : ["求助","资讯"]
     */

    private String id;
    private String pid;
    private String title;
    private String logo;
    private List<String> categorys;
    private String thread_num;
    private List<Forum> childForums;
    private boolean isExpanded = false;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getTitle() {
        return title==null?"":title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getCategorys() {
        return categorys;
    }

    public void setCategorys(List<String> categorys) {
        this.categorys = categorys;
    }

    public List<Forum> getChildForums() {
        return childForums;
    }

    public void setChildForums(List<Forum> childForums) {
        this.childForums = childForums;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getThread_num() {
        return thread_num;
    }

    public void setThread_num(String thread_num) {
        this.thread_num = thread_num;
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }

    //判断是否有子栏目
    //true-有 false-无
    public boolean hasChild(){
        if (EmptyUtils.isNotEmpty(childForums))
            return true;
        return false;
    }
}
