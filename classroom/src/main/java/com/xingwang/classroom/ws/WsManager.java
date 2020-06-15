package com.xingwang.classroom.ws;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;


import com.xingwang.classroom.utils.CommentUtils;
import com.xingwang.classroom.utils.LogUtil;
import com.xingwang.classroom.ws.listener.IWsManager;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class WsManager implements IWsManager {
    private final static int RECONNECT_INTERVAL = 10 * 1000;    //重连自增步长
    private final static long RECONNECT_MAX_TIME = 20 * 1000;   //最大重连间隔
    private Context mContext;
    private String wsUrl;
    private WebSocket mWebSocket;
    private OkHttpClient mOkHttpClient;
    private Request mRequest;
    private int mCurrentStatus = WsStatus.DISCONNECTED;     //websocket连接状态
    private boolean isNeedReconnect;          //是否需要断线自动重连
    private boolean isManualClose = false;         //是否为手动关闭websocket连接
    private WsStatusListener wsStatusListener;
    private Lock mLock;
    private Handler wsMainHandler = new Handler(Looper.getMainLooper());
    private int reconnectCount = 0;   //重连次数
    private int delayedTime = 50000;//发送空消息时间ms
    private Handler mHandlerSendMessage ;
    boolean isSendRequest= true;
    private ExecutorService executor  =Executors.newFixedThreadPool(1);
    private Runnable reconnectRunnable = new Runnable() {

        @Override
        public void run() {
            if (wsStatusListener != null) {
                wsStatusListener.onReconnect();
            }
            buildConnect();
        }
    };
    void execute(Runnable runnable) {
        executor.execute(runnable);
    }

   private Runnable mSendMessageRunnable = new Runnable() {
        @Override
        public void run() {
            sendMessage("");//发送空消息保持连接
           if (mHandlerSendMessage!=null) {
               mHandlerSendMessage.removeCallbacks(mSendMessageRunnable);
               mHandlerSendMessage.postDelayed(mSendMessageRunnable, delayedTime);
           }
            if (!isSendRequest){
                tryReconnect();
            }
        }
    };
    private WebSocketListener mWebSocketListener = new WebSocketListener() {

        @Override
        public void onOpen(WebSocket webSocket, final Response response) {
            LogUtil.i(":onOpen()",WsManager.class);
            mWebSocket = webSocket;
            setCurrentStatus(WsStatus.CONNECTED);
            connected();
            isSendRequest = true;
            if (wsStatusListener != null) {
                if (Looper.myLooper() != Looper.getMainLooper()) {
                    wsMainHandler.post(() -> {
                        if (mHandlerSendMessage==null) {
                            mHandlerSendMessage = new Handler();
                            mHandlerSendMessage.postDelayed(mSendMessageRunnable, delayedTime);
                        }
                        wsStatusListener.onOpen(response);
                    });
                }else {
                    if (mHandlerSendMessage==null) {
                        mHandlerSendMessage = new Handler();
                        mHandlerSendMessage.postDelayed(mSendMessageRunnable, delayedTime);
                    }
                    wsStatusListener.onOpen(response);
                }
            }
        }

        @Override

        public void onMessage(WebSocket webSocket, final ByteString bytes) {
            LogUtil.i(":onMessage()",WsManager.class);
            isSendRequest = true;
            if (wsStatusListener != null) {
                if (Looper.myLooper() != Looper.getMainLooper()) {
                    wsMainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            wsStatusListener.onMessage(bytes);
                        }
                    });
                } else {
                    wsStatusListener.onMessage(bytes);
                }
            }
        }



        @Override

        public void onMessage(WebSocket webSocket, final String text) {
            LogUtil.i(":onMessage():"+text,WsManager.class);
            isSendRequest = true;
            if (wsStatusListener != null) {
                if (Looper.myLooper() != Looper.getMainLooper()) {
                    wsMainHandler.post(() -> backListener(text));
                } else {
                    backListener(text);
                }
            }
        }

        private synchronized void backListener(final String text) {
            execute(() -> wsStatusListener.onMessage(text));
        }


        @Override
        public void onClosing(WebSocket webSocket, final int code, final String reason) {
            isSendRequest =true;
            LogUtil.i(":onClosing()",WsManager.class);
            if (wsStatusListener != null) {
                if (Looper.myLooper() != Looper.getMainLooper()) {
                    wsMainHandler.post(() -> wsStatusListener.onClosing(code, reason));
                } else {
                    wsStatusListener.onClosing(code, reason);
                }
            }
        }



        @Override
        public void onClosed(WebSocket webSocket, final int code, final String reason) {
            LogUtil.i("onClosed()",WsManager.class);
            isSendRequest =true;
            if (wsStatusListener != null) {
                if (Looper.myLooper() != Looper.getMainLooper()) {
                    wsMainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            wsStatusListener.onClosed(code, reason);
                        }
                    });
                } else {
                    wsStatusListener.onClosed(code, reason);
                }
            }
        }


        @Override

        public void onFailure(WebSocket webSocket, final Throwable t, final Response response) {
            isSendRequest =true;
            LogUtil.i("onFailure()"+t.getMessage(),WsManager.class);
            tryReconnect();

            if (wsStatusListener != null) {
                if (Looper.myLooper() != Looper.getMainLooper()) {
                    wsMainHandler.post(() -> wsStatusListener.onFailure(t, response));
                } else {
                    wsStatusListener.onFailure(t, response);
                }
            }
        }
    };



    private WsManager(Builder builder) {
        mContext = builder.mContext;
        wsUrl = builder.wsUrl;
        isNeedReconnect = builder.needReconnect;
        mOkHttpClient = builder.mOkHttpClient;
        this.mLock = new ReentrantLock();
    }

    /**
     * 更加连接数目来判断是否从连
     * 检查从连
     */
    private void checkConnected(){
        if ( mOkHttpClient!=null){
            LogUtil.i("连接数目:"+mOkHttpClient.connectionPool().connectionCount());
            if (mOkHttpClient.connectionPool().connectionCount()==0){//连接数为0从连
                tryReconnect();
            }
        }
    }

    private void initWebSocket() {
        if (mOkHttpClient == null) {
            mOkHttpClient = new OkHttpClient.Builder()
                    .retryOnConnectionFailure(true)
                    .pingInterval(10,TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10,TimeUnit.SECONDS)
                    .connectTimeout(10,TimeUnit.SECONDS)
                    .build();
        }

        if (mRequest == null) {
            mRequest = new Request.Builder()
                    .url(wsUrl)
                    .build();
        }

        mOkHttpClient.dispatcher().cancelAll();
        try {
            mLock.lockInterruptibly();
            try {
                mOkHttpClient.newWebSocket(mRequest, mWebSocketListener);

            } finally {
                mLock.unlock();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


    @Override
    public WebSocket getWebSocket() {
        return mWebSocket;
    }


     void setWsStatusListener(WsStatusListener wsStatusListener) {
        this.wsStatusListener = wsStatusListener;
    }



    @Override
    public synchronized boolean isWsConnected() {
        return mCurrentStatus == WsStatus.CONNECTED;

    }



    @Override

    public synchronized int getCurrentStatus() {
        return mCurrentStatus;
    }



    @Override

    public synchronized void setCurrentStatus(int currentStatus) {
        this.mCurrentStatus = currentStatus;
    }



    @Override

    public void startConnect() {
        isManualClose = false;
        buildConnect();
    }



    @Override
    public void stopConnect() {
        if (mHandlerSendMessage!=null)
            mHandlerSendMessage.removeCallbacks(mSendMessageRunnable);
        if (wsMainHandler!=null){
            wsMainHandler.removeCallbacks(reconnectRunnable);
        }
        isManualClose = true;
        disconnect();

    }



     void tryReconnect() {
        LogUtil.i("tryReconnect");
        if (!isNeedReconnect | isManualClose) {
            return;
        }
        if (!CommentUtils.isNetworkConnected(mContext)) {
            setCurrentStatus(WsStatus.DISCONNECTED);
            if (wsStatusListener != null) {
                if (Looper.myLooper() != Looper.getMainLooper()) {
                    wsMainHandler.post(() -> wsStatusListener.onMessage(""));
                }else{
                    wsStatusListener.onMessage("");
                }
            }
            return;
        }
        setCurrentStatus(WsStatus.RECONNECT);
        long delay = reconnectCount * RECONNECT_INTERVAL;
        wsMainHandler.postDelayed(reconnectRunnable, delay > RECONNECT_MAX_TIME ? RECONNECT_MAX_TIME : delay);
        reconnectCount++;
    }



    private void cancelReconnect() {
        wsMainHandler.removeCallbacks(reconnectRunnable);
        reconnectCount = 0;

    }



    private void connected() {
        cancelReconnect();
    }



    private void disconnect() {
        if (mCurrentStatus == WsStatus.DISCONNECTED) {
            return;
        }
        cancelReconnect();
        if (mOkHttpClient != null) {
            mOkHttpClient.dispatcher().cancelAll();
        }

        if (mWebSocket != null) {
            boolean isClosed = mWebSocket.close(WsStatus.CODE.NORMAL_CLOSE, WsStatus.TIP.NORMAL_CLOSE);
            //非正常关闭连接
            if (!isClosed) {
                if (wsStatusListener != null) {
                    wsStatusListener.onClosed(WsStatus.CODE.ABNORMAL_CLOSE, WsStatus.TIP.ABNORMAL_CLOSE);
                }

            }

        }
        setCurrentStatus(WsStatus.DISCONNECTED);
    }



    private synchronized void buildConnect() {

        if (!CommentUtils.isNetworkConnected(mContext)) {
            setCurrentStatus(WsStatus.DISCONNECTED);
            return;
        }

        switch (getCurrentStatus()) {
            case WsStatus.CONNECTED:
            case WsStatus.CONNECTING:
                break;
            default:
                setCurrentStatus(WsStatus.CONNECTING);
                initWebSocket();
        }

    }



    //发送消息

    @Override

    public boolean sendMessage(String msg) {
        LogUtil.i("sendMessage():"+msg,WsManager.class);
        return send(msg);
    }



    @Override

    public boolean sendMessage(ByteString byteString) {
        return send(byteString);
    }

    private boolean send(Object msg) {
        boolean isSend = false;
        if (mWebSocket != null && mCurrentStatus == WsStatus.CONNECTED) {
            if (msg instanceof String) {
                if (!TextUtils.isEmpty((String)msg)){
                    isSendRequest = false;
                }
                isSend = mWebSocket.send((String) msg);
            } else if (msg instanceof ByteString) {
                isSend = mWebSocket.send((ByteString) msg);
            }
            if (isSend){
                checkConnected();
                 //发送消息后移除定时消息，从新
                if (mHandlerSendMessage!=null) {
                    mHandlerSendMessage.removeCallbacks(mSendMessageRunnable);
                    mHandlerSendMessage.postDelayed(mSendMessageRunnable, delayedTime);
                }
            }else {
                //发送消息失败，尝试重连
                tryReconnect();
            }
        }
        return isSend;
    }
    public static final class Builder {
        private Context mContext;
        private String wsUrl;
        private boolean needReconnect = true;
        private OkHttpClient mOkHttpClient;

        public Builder(Context val) {
            mContext = val;
        }
        public Builder wsUrl(String val) {
            wsUrl = val;
            return this;

        }
        public Builder client(OkHttpClient val) {
            mOkHttpClient = val;
            return this;
        }

         Builder needReconnect(boolean val) {
            needReconnect = val;
            return this;
        }
        public WsManager build() {
            return new WsManager(this);
        }

    }

}
