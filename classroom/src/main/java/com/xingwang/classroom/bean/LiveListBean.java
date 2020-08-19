package com.xingwang.classroom.bean;


import com.xingwang.classroom.http.CommonEntity;

import java.util.List;

/**
 Date:2020/8/3
 Time;11:00
 author:baiguiqiang
 */
public class LiveListBean extends CommonEntity {


    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 221
         * category_id : 5
         * title : 夏季鱼类保健之肠道保健
         * body : <p>随着夏季气温升高，饵料系数也随之增加，而鱼类肠炎问题也更为常见，肛门发红、白便、生长缓慢......如何做好夏季鱼类肠道保健，让鱼儿健康生长？8月6日19：30 兴旺水产养殖直播间给你答案！</p>
         * cover : http://xw518app.oss-cn-beijing.aliyuncs.com//default/2020/08/03/104344_直播806.jpg
         * start_time : 1596713400
         * is_end : 1
         * speaker :
         * state : 1
         * click : 12
         * guest_count : 5
         * fixed_state : 1
         * fixed_str :
         * chat_count : 0
         */

        private int id;
        private int category_id;
        private String title;
        private String body;
        private String cover;
        private int start_time;
        private int is_end;
        private String speaker;
        private int state;
        private int click;
        private int guest_count;
        private int fixed_state;
        private String fixed_str;
        private int chat_count;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getCategory_id() {
            return category_id;
        }

        public void setCategory_id(int category_id) {
            this.category_id = category_id;
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

        public String getCover() {
            return cover;
        }

        public void setCover(String cover) {
            this.cover = cover;
        }

        public int getStart_time() {
            return start_time;
        }

        public void setStart_time(int start_time) {
            this.start_time = start_time;
        }

        public int getIs_end() {
            return is_end;
        }

        public void setIs_end(int is_end) {
            this.is_end = is_end;
        }

        public String getSpeaker() {
            return speaker;
        }

        public void setSpeaker(String speaker) {
            this.speaker = speaker;
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

        public int getClick() {
            return click;
        }

        public void setClick(int click) {
            this.click = click;
        }

        public int getGuest_count() {
            return guest_count;
        }

        public void setGuest_count(int guest_count) {
            this.guest_count = guest_count;
        }

        public int getFixed_state() {
            return fixed_state;
        }

        public void setFixed_state(int fixed_state) {
            this.fixed_state = fixed_state;
        }

        public String getFixed_str() {
            return fixed_str;
        }

        public void setFixed_str(String fixed_str) {
            this.fixed_str = fixed_str;
        }

        public int getChat_count() {
            return chat_count;
        }

        public void setChat_count(int chat_count) {
            this.chat_count = chat_count;
        }
    }
}
