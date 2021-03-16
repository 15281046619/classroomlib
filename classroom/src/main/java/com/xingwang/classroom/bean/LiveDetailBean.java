package com.xingwang.classroom.bean;


import com.xinwang.bgqbaselib.http.CommonEntity;

/**
 * Date:2020/8/19
 * Time;16:44
 * author:baiguiqiang
 */
public class LiveDetailBean  extends CommonEntity {
    private  DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private LiveListBean.DataBean live;

        public LiveListBean.DataBean getLive() {
            return live;
        }

        public void setLive(LiveListBean.DataBean live) {
            this.live = live;
        }
    }

}
