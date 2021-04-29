package com.xinwang.shoppingcenter.interfaces;

import com.xinwang.bgqbaselib.sku.bean.Sku;

/**
 * Date:2021/4/12
 * Time;10:51
 * author:baiguiqiang
 */
public interface OrderButtonListener {
    void onCancel(int pos);//取消订单
    void onPay(String title,int pos);//支付订单
}
