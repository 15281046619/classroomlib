package com.xinwang.shoppingcenter.bean;

import com.xinwang.bgqbaselib.http.CommonEntity;

import java.util.List;

/**
 * Date:2021/4/30
 * Time;14:47
 * author:baiguiqiang
 */
public class OrderGoodBean extends CommonEntity {
    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private String total;
        private List<OrderListBean.DataBean.OrdersBean.ItemsBean> items;

        public String getTotal() {
            return total;
        }

        public void setTotal(String total) {
            this.total = total;
        }

        public List<OrderListBean.DataBean.OrdersBean.ItemsBean> getItems() {
            return items;
        }

        public void setItems(List<OrderListBean.DataBean.OrdersBean.ItemsBean> items) {
            this.items = items;
        }
    }
}
