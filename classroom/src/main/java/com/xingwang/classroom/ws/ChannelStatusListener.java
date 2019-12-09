package com.xingwang.classroom.ws;



/**
 * Date:2019/10/31
 * Time;10:38
 * author:baiguiqiang
 */
public abstract class ChannelStatusListener {
    /**
     * 订阅成功
     * @param response
     */
    public void subscribeSuccess(String response) {

    }
    /**
     * 创建连接成功
     * @param response
     */
    public void createSuccess(String response) {

    }



    /**
     * 取消订阅成功
     * @param response
     */
    public void unSubscribeSuccess(String response) {

    }

    /**
     *错误移除
     */
    public void onFailure(int code,String message) {

    }

    /**
     * 监听消息
     * @param message
     */
    public void onMessage(String message){

    }

}
