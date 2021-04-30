package com.xinwang.shoppingcenter.bean;

import com.xinwang.bgqbaselib.http.CommonEntity;

/**
 * Date:2021/4/30
 * Time;10:12
 * author:baiguiqiang
 */
public class OrderBean extends CommonEntity {
    private OrderListBean.DataBean.OrdersBean data;

    public OrderListBean.DataBean.OrdersBean getData() {
        return data;
    }

    public void setData(OrderListBean.DataBean.OrdersBean data) {
        this.data = data;
    }
}
