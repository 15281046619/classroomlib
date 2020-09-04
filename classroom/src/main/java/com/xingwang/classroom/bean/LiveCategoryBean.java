package com.xingwang.classroom.bean;


import com.xingwang.classroom.http.CommonEntity;

import java.util.List;

/**
 Date:2020/8/3
 Time;11:00
 author:baiguiqiang
 */
public class LiveCategoryBean extends CommonEntity {


    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 5
         * pid : 0
         * title : 水产直播
         * state : 1
         * rank : 1
         */

        private int id;
        private int pid;
        private String title;
        private int state;
        private int rank;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getPid() {
            return pid;
        }

        public void setPid(int pid) {
            this.pid = pid;
        }

        public String getTitle() {
            return title;
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
    }
}
