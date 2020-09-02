package com.xingwang.circle.bean;

import com.blankj.utilcode.util.EmptyUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
@JsonIgnoreProperties(ignoreUnknown = true)
public class CardBody {
    private String text;
    //视频
    private String video;
    private String cover;
    //图片
    private List<String> imgs;

    public String getText() {
        return text==null?"":text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getVideo() {
        return video==null?"":video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getCover() {
        return cover==null?"":cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public List<String> getImgs() {
        return imgs;
    }

    public void setImgs(List<String> imgs) {
        this.imgs = imgs;
    }
}
