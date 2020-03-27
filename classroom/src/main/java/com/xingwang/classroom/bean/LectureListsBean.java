package com.xingwang.classroom.bean;

import com.xingwang.classroom.http.CommonEntity;

import java.io.Serializable;
import java.util.List;

/**
 * Date:2019/8/26
 * Time;10:23
 * author:baiguiqiang
 */
public class LectureListsBean extends CommonEntity {

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean implements Serializable {
        /**
         * id : 2
         * category : 栏目一
         * title : cesss
         * body : 11111111
         * thumb : http://attach.kf.xw518.com/2019/08-26/09/5d63355767d66_1.jpg
         * ad : 11111111
         * click : 2
         * state : 1
         * video_src : http://attach.kf.xw518.com/2019/08-26/09/5d6335603e829_1.mp4
         * top : 1
         * publish_time : 2019-08-26 09:26:57
         */

        private int id;
        private String category;
        private String title;
        private String body;
        private String thumb;
        private String ad;
        private int click;
        private int state;
        private String type;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        private String video_src;
        private int top;
        private String publish_time;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getBody() {
            return body;
        }

        public void setBody(String body) {
            this.body = body;
        }

        public String getThumb() {
            return thumb;
        }

        public void setThumb(String thumb) {
            this.thumb = thumb;
        }

        public String getAd() {
            return ad;
        }

        public void setAd(String ad) {
            this.ad = ad;
        }

        public int getClick() {
            return click;
        }

        public void setClick(int click) {
            this.click = click;
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

        public String getVideo_src() {
            return video_src;
        }

        public void setVideo_src(String video_src) {
            this.video_src = video_src;
        }

        public int getTop() {
            return top;
        }

        public void setTop(int top) {
            this.top = top;
        }

        public String getPublish_time() {
            return publish_time;
        }

        public void setPublish_time(String publish_time) {
            this.publish_time = publish_time;
        }
    }
}
