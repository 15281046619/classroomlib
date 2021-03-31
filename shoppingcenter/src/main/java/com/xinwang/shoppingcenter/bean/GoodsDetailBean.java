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
}
