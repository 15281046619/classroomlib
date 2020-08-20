package com.xingwang.classroom.bean;

import com.xingwang.classroom.http.CommonEntity;

/**
 * Date:2020/8/19
 * Time;16:44
 * author:baiguiqiang
 */
public class LiveIsSubscribeBean extends CommonEntity {
    private  DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private boolean subscribe;

        public boolean isSubscribe() {
            return subscribe;
        }

        public void setSubscribe(boolean subscribe) {
            this.subscribe = subscribe;
        }
    }

}
