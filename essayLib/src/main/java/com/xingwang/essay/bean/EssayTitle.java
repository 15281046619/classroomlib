package com.xingwang.essay.bean;

import com.blankj.utilcode.util.EmptyUtils;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EssayTitle implements Serializable {

    /**
     * id : 12
     * pid : 0
     * title : 技术
     * state : 1
     * rank : 0
     */

    private long id;
    private long pid;
    private String title;
    private int state;
    private int rank;

    private List<EssayTitle> childTitles;

    public long getId() {
        return id;
    }

    public String getStrId() {
        return String.valueOf(id);
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getPid() {
        return pid;
    }

    public void setPid(long pid) {
        this.pid = pid;
    }

    public String getTitle() {
        return title==null?"":title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public List<EssayTitle> getChildTitles() {
        return childTitles;
    }

    public void setChildTitles(List<EssayTitle> childTitles) {
        this.childTitles = childTitles;
        if (hasChild()){//此时有子层级
            EssayTitle essayTitle =new EssayTitle();
            essayTitle.setId(getId());
            essayTitle.setPid(getPid());
            essayTitle.setTitle("全部");
            this.childTitles.add(0,essayTitle);
        }
    }

    //是否为顶级标题
    public boolean isTop(){
        if (getPid()==0)
            return true;
        return false;
    }

    //是否有子选项
    public boolean hasChild(){
        if(EmptyUtils.isNotEmpty(getChildTitles()))
            return true;
        return false;
    }
}
