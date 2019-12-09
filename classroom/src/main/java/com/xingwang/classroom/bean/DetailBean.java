package com.xingwang.classroom.bean;

import com.google.gson.annotations.SerializedName;
import com.xingwang.classroom.http.CommonEntity;



/**
 * Date:2019/9/11
 * Time;15:09
 * author:baiguiqiang
 */
public class DetailBean extends CommonEntity {
    /**
     * data : {"lecture":{"id":14,"category":"鏍忕洰浜�","title":"46366","body":"<p>2423444444444444444<\/p>","thumb":"http://attach.kf.xw518.com/2019/08-30/13/5d68b592c31e2__20190724111903jpg","ad":"<p>2423444444444444444<\/p>","click":3,"state":1,"video_src":"http://attach.kf.xw518.com/2019/08-28/10/5d65ea4350c90_cb3bbf9f1f40626a178efdce0cd1e197.mp4","top":0,"publish_time":"2019-08-28 10:43:19"}}
     */


    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * lecture : {"id":14,"category":"鏍忕洰浜�","title":"46366","body":"<p>2423444444444444444<\/p>","thumb":"http://attach.kf.xw518.com/2019/08-30/13/5d68b592c31e2__20190724111903jpg","ad":"<p>2423444444444444444<\/p>","click":3,"state":1,"video_src":"http://attach.kf.xw518.com/2019/08-28/10/5d65ea4350c90_cb3bbf9f1f40626a178efdce0cd1e197.mp4","top":0,"publish_time":"2019-08-28 10:43:19"}
         */

        private LectureBean lecture;

        public LectureBean getLecture() {
            return lecture;
        }

        public void setLecture(LectureBean lecture) {
            this.lecture = lecture;
        }

        public static class LectureBean {
            /**
             * id : 14
             * category : 鏍忕洰浜�
             * title : 46366
             * body : <p>2423444444444444444</p>
             * thumb : http://attach.kf.xw518.com/2019/08-30/13/5d68b592c31e2__20190724111903jpg
             * ad : <p>2423444444444444444</p>
             * click : 3
             * state : 1
             * video_src : http://attach.kf.xw518.com/2019/08-28/10/5d65ea4350c90_cb3bbf9f1f40626a178efdce0cd1e197.mp4
             * top : 0
             * publish_time : 2019-08-28 10:43:19
             */

            private int id;
            private String category;
            private String title;
            private String body;
            private String thumb;
            private String ad;
            private int click;
            private int state;
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
}
