package com.xingwang.classroom.bean;


import com.xinwang.bgqbaselib.http.CommonEntity;

/**
 * Date:2019/9/16
 * Time;9:46
 * author:baiguiqiang
 */
public class SendCommentBean extends CommonEntity {


    /**
     * data : {"comments":[],"total":192}
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
         * comments : []
         * total : 192
         */

        private String comment_id;
        private int state;//1正常

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

        public String getComment_id() {
            return comment_id;
        }

        public void setComment_id(String comment_id) {
            this.comment_id = comment_id;
        }
    }
}
