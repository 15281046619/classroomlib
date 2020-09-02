package com.xingwang.essay.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.xingwang.swip.utils.JsonUtils;

import java.io.Serializable;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Essay implements Serializable {

    /**
     * id : 28
     * title : 置顶栏目一
     * imgs : []
     * body : <p>阿斯顿发送到</p>
     * click : 0
     * category : 栏目1
     * state : 1
     * publish_time : 2019-08-26 15:02:08
     * top : 1
     * url : http://192.168.65.74/xwapp/public/article/general/article/detail?id=28
     */

    private String id;
    private String title;
    private String imgs;
    private int click;
    private String publish_time;
    private String url;
    private int top;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title==null?"":title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImgs() {
        return imgs;
    }

    public void setImgs(String imgs) {
        this.imgs = imgs;
    }

    public String getClick() {
        return String.valueOf(click);
    }

    public void setClick(int click) {
        this.click = click;
    }

    public String getPublish_time() {
        return publish_time==null?"":publish_time;
    }

    public void setPublish_time(String publish_time) {
        this.publish_time = publish_time;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<String> getImgList() {
        return JsonUtils.jsonToList(imgs,String.class);
    }

    public int getTop() {
        return top;
    }

    public void setTop(int top) {
        this.top = top;
    }
}
