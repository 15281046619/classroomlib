package com.xingwang.circle.bean;

import com.blankj.utilcode.util.EmptyUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.xingwang.swip.utils.JsonUtils;

import java.io.Serializable;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Card implements Serializable {

    /**
     * id : 84
     * forum_id : 5
     * category : 求助
     * customer_id : 1
     * type : text
     * body :
     * publish_time : 2019-10-22 15:21:43
     * state : 1
     * seal :
     * comment_num : 0
     * favorite_num : 0
     * digg_num : 0
     * customer :
     * is_digg : 0
     * is_favorite : 0
     */

    private String id;
    private String forum_id;
    private String category;
    private String type;
    //帖子主体内容
    private String body;
    private String publish_time;
    private int state;
    private String badge;
    private int comment_num;
    private String favorite_num;
    private int digg_num;
    private User user;
    private int is_digg;
    private int is_favorite;

    private List<CommentBean> usefulComments;//精选评论
    //是否有精选评论 true-有 false-无
    private boolean isUefulComment;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getForum_id() {
        return forum_id;
    }

    public void setForum_id(String forum_id) {
        this.forum_id = forum_id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public long getCustomer_id() {
        return getUser().getId();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getPublish_time() {
        return publish_time;
    }

    public void setPublish_time(String publish_time) {
        this.publish_time = publish_time;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getBadge() {
        return badge;
    }

    public void setBadge(String badge) {
        this.badge = badge;
    }

    public int getComment_num() {
        return comment_num;
    }

    public void setComment_num(int comment_num) {
        this.comment_num = comment_num;
    }

    public String getFavorite_num() {
        return favorite_num;
    }

    public void setFavorite_num(String favorite_num) {
        this.favorite_num = favorite_num;
    }

    public int getDigg_num() {
        return digg_num;
    }

    public void setDigg_num(int digg_num) {
        this.digg_num = digg_num;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getIs_digg() {
        return is_digg;
    }

    public void setIs_digg(int is_digg) {
        this.is_digg = is_digg;
    }

    public int getIs_favorite() {
        return is_favorite;
    }

    public void setIs_favorite(int is_favorite) {
        this.is_favorite = is_favorite;
    }

    public CardBody getCardBody() {
        return JsonUtils.jsonToPojo(body,CardBody.class);
    }

    public List<CommentBean> getUsefulComments() {
        return usefulComments;
    }

    public void setUsefulComments(List<CommentBean> usefulComments) {
        this.usefulComments = usefulComments;
    }

    public boolean isUsefulComment(){
        if (EmptyUtils.isEmpty(usefulComments))
            return false;
        return true;
    }
}
