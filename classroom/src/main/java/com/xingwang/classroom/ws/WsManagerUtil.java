package com.xingwang.classroom.ws;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.LocalBroadcastManager;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.xingwang.classroom.http.HttpUrls;
import com.xingwang.classroom.utils.LogUtil;

import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Response;
import okio.ByteString;

import static android.content.Context.CONNECTIVITY_SERVICE;

/**
 * 基本使用方法 activity 中oncrate成功后subscribe成功  onDestroy调用 onDestroy
 * Date:2019/10/31
 * Time;10:03
 * author:baiguiqiang
 */
public class WsManagerUtil {
    public static int NETERROR=-1;
    private  WsManager wsManager;
    private ChannelStatusListener channelStatusListener;
    private String  channel=null;//订阅频道 null 就没有订阅
    private static WsManagerUtil mInstance;
    private BroadcastReceiver networkChangeReceiver;
    private Context context;
    public static WsManagerUtil getInstance() {

        if (mInstance == null) {
            synchronized (WsManagerUtil.class) {
                if (mInstance == null) {
                    mInstance = new WsManagerUtil();
                }
            }
        }
        return mInstance;
    }

    /**
     * 初始化连接 连接成功默认会订阅
     * @param context
     */
    public void onCreate(Context context,ChannelStatusListener channelStatusListener){
        this.channelStatusListener = channelStatusListener;
        if (wsManager==null) {
            wsManager = new WsManager.Builder(context)
                    .client(new OkHttpClient().newBuilder()
                            .pingInterval(10, TimeUnit.SECONDS)
                            .retryOnConnectionFailure(true)
                            .readTimeout(10, TimeUnit.SECONDS)
                            .writeTimeout(10, TimeUnit.SECONDS)
                            .connectTimeout(10, TimeUnit.SECONDS)
                            .build())
                    .needReconnect(true)
                    .wsUrl(HttpUrls.CHANNEL_WS_URL)
                    .build();
            wsManager.setWsStatusListener(mParentStatusListener);
            wsManager.startConnect();
        }else {
            wsManager.tryReconnect();
        }
        initNetworkChangeListener(context);

    }

    private void initNetworkChangeListener(Context context){
        this.context = context;
        //注册网络改变监听
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        if (networkChangeReceiver==null) {
            networkChangeReceiver = new  BroadcastReceiver(){

                @Override
                public void onReceive(Context context, Intent intent) {
                    ConnectivityManager connectionManager = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
                    NetworkInfo networkInfo = connectionManager.getActiveNetworkInfo();
                    if (networkInfo != null && networkInfo.isAvailable()) {
                        if (wsManager!=null){
                            wsManager.tryReconnect();
                        }
                    } else {
                        if (channelStatusListener!=null){
                            if (channelStatusListener!=null){
                                channelStatusListener.onFailure(NETERROR,"网络连接失败");
                            }
                        }
                    }
                }
            };
            context.registerReceiver(networkChangeReceiver, intentFilter);
        }
    }

    /**
     * 订阅
     */
    public void subscribe(String channel,ChannelStatusListener channelStatusListener){
        sendMessageSubscribe(channel);
       this.channel =channel;
       this.channelStatusListener =channelStatusListener;
    }

    /**
     * 取消订阅
     */
    public void unSubscribe(ChannelStatusListener channelStatusListener){
        sendMessageUnSubscribe(channel);
        this.channel =null;
        this.channelStatusListener =channelStatusListener;
    }
    /**
     * 断开连接
     */
    public void onDestroy(ChannelStatusListener channelStatusListener){
        this.channelStatusListener =channelStatusListener;
        if (context!=null){
            try {//可能出现异常 Receiver not registered
                context.unregisterReceiver(networkChangeReceiver);
            }catch (Exception e){

            }

        }
        if (wsManager!=null) {
            wsManager.stopConnect();
            wsManager = null;
            mInstance= null;
        }

    }
    private WsStatusListener mParentStatusListener=new WsStatusListener() {
        @Override
        synchronized public void onOpen(Response response) {
            if (channel!=null){
                sendMessageSubscribe(channel);
            }
            if (channelStatusListener!=null){
                channelStatusListener.createSuccess(response.message());
            }
        }

        @Override
        synchronized public void onMessage(String text) {
            if (channelStatusListener!=null){
                try{
                    JSONObject jsonObject=new JSONObject(text);
                    if (jsonObject.getString("event").equals("subscribe")){
                        channelStatusListener.subscribeSuccess(jsonObject.getString("data"));
                    }else if (jsonObject.getString("event").equals("unSubscribe")){
                        channelStatusListener.unSubscribeSuccess(jsonObject.getString("data"));
                    }else {
                        channelStatusListener.onMessage(text);
                    }
                }catch (Exception e){
                    try {
                        channelStatusListener.onMessage(text);
                    }catch (Exception e1){

                    }

                }
            }
        }

        @Override
        public void onMessage(ByteString bytes) {

        }

        @Override
        public void onReconnect() {


        }

        @Override
        public void onClosing(int code, String reason) {

        }

        @Override
        public void onClosed(int code, String reason) {

        }

        @Override
        public void onFailure(Throwable t, Response response) {
            if (channelStatusListener!=null&&response!=null){
                channelStatusListener.onFailure(response.code(),response.message());
            }
        }
    };
    private void sendMessageSubscribe(String channel){
        if (wsManager!=null){
            wsManager.sendMessage("{\"event\":\"subscribe\",\"data\":\""+channel+"\"}");
        }
    }
    private void sendMessageUnSubscribe(String channel){
        if (wsManager!=null){
            wsManager.sendMessage("{\"event\":\"unSubscribe\",\"data\":\""+channel+"\"}");
        }
    }
}
