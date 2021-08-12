package com.xinwang.shoppingcenter.bean;

import com.xinwang.bgqbaselib.http.CommonEntity;

import java.io.Serializable;

/**
 * Date:2021/3/30
 * Time;9:24
 * author:baiguiqiang
 */
public class GoodsDetailBean extends CommonEntity {
    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean implements Serializable {
        private GoodsBean.DataBean goods;

        public GoodsBean.DataBean getGoods() {
            return goods;
        }

        public void setGoods(GoodsBean.DataBean goods) {
            this.goods = goods;
        }
    }

    /**
     * Date:2021/6/11
     * Time;14:29
     * author:baiguiqiang
     */
    public static class MediaBean{
        private String path;
        private int type;// 0图片 1视频
        private String picPath;//缩略图或者视频封面

        public MediaBean(String path, int type, String picPath) {
            this.path = path;
            this.type = type;
            this.picPath = picPath;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getPicPath() {
            return picPath;
        }

        public void setPicPath(String picPath) {
            this.picPath = picPath;
        }
    }
}
