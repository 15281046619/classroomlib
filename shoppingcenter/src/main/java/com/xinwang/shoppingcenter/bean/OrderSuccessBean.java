package com.xinwang.shoppingcenter.bean;

import com.xinwang.bgqbaselib.http.CommonEntity;

/**
 * Date:2021/4/28
 * Time;9:00
 * author:baiguiqiang
 */
public class OrderSuccessBean extends CommonEntity {

    /**
     * data : {"order_id":23}
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
         * order_id : 23
         */

        private int order_id;

        public int getOrder_id() {
            return order_id;
        }

        public void setOrder_id(int order_id) {
            this.order_id = order_id;
        }
    }
}
