package com.xinwang.shoppingcenter.bean;

/**
 * Date:2021/4/28
 * Time;8:53
 * author:baiguiqiang
 */
public class OrderStateUpdateBean {
    private int state;//1未付款 2已付款

    public OrderStateUpdateBean(int state) {
        this.state = state;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
