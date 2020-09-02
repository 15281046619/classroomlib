package com.xingwang.circle.bean;

import com.blankj.utilcode.util.EmptyUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.List;
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommentBean implements Serializable {

    /**
     * id : 42
     * thread_id : 91
     * bid : 0
     * body : 嗯嗯
     * pics :
     * state : 1
     * customer_id : 42
     * to_customer_id : 1
     * publish_time : 2019-10-28 09:00:11
     */

    private String id;
    private String comment_id;//发送评论成功后返回的id
    private String thread_id;
    private String bid;//基础评论id
    private String pid;//父评论id
    private String body;
    private String pics;
    private int state;
    private String user_id;//评论人id
    private String to_user_id;//被评论人id
    private String publish_time;
    private User user;//评论用户
    private User to_user;//被评论用户
    private List<CommentBean> sub_comments;//子评论
    //是否有子评论 true-有 false-无
    private boolean isChildComment;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getThread_id() {
        return thread_id;
    }

    public void setThread_id(String thread_id) {
        this.thread_id = thread_id;
    }

    public String getBid() {
        return bid==null?"":bid;
    }

    public void setBid(String bid) {
        this.bid = bid;
    }

    public String getBody() {
        return body==null?"":body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getPics() {
        return pics;
    }

    public void setPics(String pics) {
        this.pics = pics;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getPublish_time() {
        return publish_time==null?"":publish_time;
    }

    public void setPublish_time(String publish_time) {
        this.publish_time = publish_time;
    }

    public List<CommentBean> getSub_comments() {
        return sub_comments;
    }

    public void setSub_comments(List<CommentBean> sub_comments) {
        this.sub_comments = sub_comments;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getComment_id() {
        return comment_id;
    }

    public void setComment_id(String comment_id) {
        this.comment_id = comment_id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getTo_user() {
        return to_user;
    }

    public void setTo_user(User to_user) {
        this.to_user = to_user;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getTo_user_id() {
        return to_user_id;
    }

    public void setTo_user_id(String to_user_id) {
        this.to_user_id = to_user_id;
    }

    public boolean isChildComment(){
        if (EmptyUtils.isEmpty(sub_comments))
            return false;
        return true;
    }
}
