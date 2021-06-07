package com.xinwang.shoppingcenter.bean;

import com.xinwang.bgqbaselib.http.CommonEntity;

import java.util.List;

/**
 * Date:2021/6/2
 * Time;9:54
 * author:baiguiqiang
 */
public class WaybillListBean extends CommonEntity {
    private WaybillListData data;

    public WaybillListData getData() {
        return data;
    }

    public void setData(WaybillListData data) {
        this.data = data;
    }

    public static class WaybillListData{
         private List<OrderListBean.DataBean.OrdersBean> waybills;

         public List<OrderListBean.DataBean.OrdersBean> getWaybills() {
             return waybills;
         }

         public void setWaybills(List<OrderListBean.DataBean.OrdersBean> waybills) {
             this.waybills = waybills;
         }
     }
}
