package com.xingwang.classroom.bean;

import com.xingwang.classroom.http.CommonEntity;

import java.util.List;

/**
 * Date:2019/8/26
 * Time;9:48
 * author:baiguiqiang
 */
public class CategoryBean  extends CommonEntity {


    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 1
         * pid : 0
         * title : 疾病防治
         * state : 1
         * rank : 0
         */

        private int id;
        private int pid;
        private String title;
        private int state;
        private int rank;

        public DataBean(int id, String title) {
            this.id = id;
            this.title = title;
        }

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
