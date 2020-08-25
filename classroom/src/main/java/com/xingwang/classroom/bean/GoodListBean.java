package com.xingwang.classroom.bean;

import com.xingwang.classroom.http.CommonEntity;

import java.io.Serializable;
import java.util.List;

public class GoodListBean extends CommonEntity {

    private List<GoodBean> data;

    public List<GoodBean> getData() {
        return data;
    }

    public void setData(List<GoodBean> data) {
        this.data = data;
    }

    public static class GoodBean implements Serializable {
        /**
         * id : 10
         * live_id : 33
         * title : de
         * cover : 3
         * pics : 43
         * body : 434
         * state : 1
         * price : 33.00
         */

        private long id;
        private long live_id;
        private String title;
        private String cover;
        private String pics;
        private String body;
        private int state;
        private String price;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public long getLive_id() {
            return live_id;
        }

        public void setLive_id(long live_id) {
            this.live_id = live_id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getCover() {
            return cover;
        }

        public void setCover(String cover) {
            this.cover = cover;
        }

        public String getPics() {
            return pics;
        }

        public void setPics(String pics) {
            this.pics = pics;
        }

        public String getBody() {
            return body;
        }

        public void setBody(String body) {
            this.body = body;
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }
    }
}
