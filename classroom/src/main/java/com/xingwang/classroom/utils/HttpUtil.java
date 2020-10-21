package com.xingwang.classroom.utils;

import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.xingwang.classroom.http.CommonEntity;
import com.xingwang.classroom.http.HttpCallBack;
import com.xingwang.classroom.http.HttpCallProgressBack;



import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;
import okio.BufferedSink;
import okio.Okio;
import okio.Source;

public class HttpUtil {
    private static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");//mdiatype 这个需要和服务端保持一致
    private static Handler mainHandler = new Handler(Looper.getMainLooper());
    private static OkHttpClient okHttpClient;

    private static OkHttpClient getInstance() {
        if (okHttpClient == null) {
            synchronized (HttpUtil.class) {
                if (okHttpClient == null) {
                    okHttpClient = new OkHttpClient.Builder()
                            .connectTimeout(10, TimeUnit.SECONDS)//10秒连接超时
                            .writeTimeout(10, TimeUnit.SECONDS)//10m秒写入超时
                            .readTimeout(10, TimeUnit.SECONDS)//10秒读取超时
                            .addInterceptor(new HttpHeaderInterceptor())//头部信息统一处理
                            //.addInterceptor(new CommonParamsInterceptor())//公共参数统一处理
                            .build();
                }
            }
        }
        return okHttpClient;
    }

    /**
     * @param url      url地址
     * @param callBack 请求回调接口
     */
    public static void get(String url, HttpCallBack callBack) {
        commonGet(getRequestForGet(url, null, null), CommonEntity.class, callBack);
    }

    /**
     * @param url      url地址
     * @param cls      泛型返回参数
     * @param callBack 请求回调接口
     */
    public static <T extends Serializable> void get(String url, Class<T> cls, HttpCallBack<T> callBack) {
        commonGet(getRequestForGet(url, null, null), cls, callBack);
    }

    /**
     * @param url      url地址
     * @param params   HashMap<String, String> 参数
     * @param callBack 请求回调接口
     */
    public static void get(String url, HashMap<String, Object> params, HttpCallBack callBack) {
        commonGet(getRequestForGet(url, params, null), CommonEntity.class, callBack);
    }

    /**
     * @param url      url地址
     * @param params   HashMap<String, String> 参数
     * @param cls      泛型返回参数
     * @param callBack 请求回调接口
     */
    public static <T extends Serializable> void get(String url, HashMap<String, Object> params, Class<T> cls, HttpCallBack<T> callBack) {
        commonGet(getRequestForGet(url, params, null), cls, callBack);
    }

    /**
     * @param url      url地址
     * @param params   HashMap<String, String> 参数
     * @param callBack 请求回调接口
     * @param tag      网络请求tag
     */
    public static void get(String url, HashMap<String, Object> params, HttpCallBack callBack, Object tag) {
        commonGet(getRequestForGet(url, params, tag), CommonEntity.class, callBack);
    }

    /**
     * @param url      url地址
     * @param params   HashMap<String, String> 参数
     * @param callBack 请求回调接口
     * @param cls      泛型返回参数
     * @param tag      网络请求tag
     */
    public static <T extends Serializable> void get(String url, HashMap<String, Object> params, Class<T> cls, HttpCallBack<T> callBack, Object tag) {
        commonGet(getRequestForGet(url, params, tag), cls, callBack);
    }

    /**
     * @param url      url地址
     * @param cls      泛型返回参数
     * @param callBack 请求回调接口
     */
    public static <T extends Serializable> void post(String url, Class<T> cls, HttpCallBack<T> callBack) {
        commonPost(getRequestForPost(url, null, null), cls, callBack);
    }




    /**
     * @param url      url地址
     * @param params   HashMap<String, Object> 参数
     * @param callBack 请求回调接口
     */
    public static void post(String url, HashMap<String, Object> params, HttpCallBack callBack) {
        commonPost(getRequestForPost(url, params, null), CommonEntity.class, callBack);
    }

    /**
     * @param url      url地址
     * @param params   HashMap<String, Object> 参数
     * @param callBack 请求回调接口
     * @param tag      网络请求tag
     */
    public static void post(String url, HashMap<String, Object> params, HttpCallBack callBack, Object tag) {
        commonPost(getRequestForPost(url, params, tag), CommonEntity.class, callBack);
    }

    /**
     *
     * @param actionUrl 地址
     * @param tag  标识
     * @param paramsMap 参数
     * @param cls 类型
     * @param callBack 返回
     * @param <T>
     */
    public static<T extends Serializable> void  upLoadFile(String actionUrl,Object tag, HashMap<String, Object> paramsMap,Class<T> cls ,final HttpCallBack<T> callBack){
        commonPost(getRequestForPostFile(actionUrl,paramsMap,tag),cls,callBack);
    }
    public static<T extends Serializable> void  upLoadFile(String actionUrl, HashMap<String, Object> paramsMap,Class<T> cls ,final HttpCallBack<T> callBack){
        commonPost(getRequestForPostFile(actionUrl,paramsMap,null),cls,callBack);
    }
    /**
     * @param url      url地址
     * @param params   HashMap<String, Object> 参数
     * @param cls      泛型返回参数
     * @param callBack 请求回调接口
     */
    public static <T extends Serializable> void post(String url, HashMap<String, Object> params, Class<T> cls, final HttpCallBack<T> callBack) {
        commonPost(getRequestForPost(url, params, null), cls, callBack);
    }
    public static <T extends Serializable> void post(String url, HashMap<String, Object> params, Class<T> cls, final HttpCallBack<T> callBack,Object object) {
        commonPost(getRequestForPost(url, params, object), cls, callBack);
    }

    /**
     * GET请求 公共请求部分
     */
    private static <T extends Serializable> void commonGet(Request request, final Class<T> cls, final HttpCallBack<T> callBack) {
        if (request == null) return;
        Call call = getInstance().newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull final IOException e) {
                try {
                    if (callBack != null && mainHandler != null) {
                        LogUtil.i("onFailure:"+e.getMessage(),HttpUtil.class);

                        mainHandler.post(() -> callBack.onFailure("服务异常,请检查你的网络！"));
                    }
                } catch (Exception e1) {
                    LogUtil.e("Exception:"+e1.getMessage(),HttpUtil.class);
                    if (callBack != null && mainHandler != null) {
                        mainHandler.post(() ->  callBack.onFailure("数据解析异常"));
                    }
                }
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                try {
                    String mDatas = response.body().string();
                    JSONObject jsonObject =new JSONObject(mDatas);
                    if (jsonObject.getInt("status")!=1){
                        String message =jsonObject.getString("message");
                        if ("invalid token".equals(jsonObject.getString("code"))) {
                            mainHandler.post(() -> callBack.onFailure("权限认证失败，请重新登录账号"));
                            ActivityManager.getInstance().finishAllActivity();
                            //BeautyDefine.getAccountDefine().controlReLogin();
                        }
                        else
                            mainHandler.post(() ->  callBack.onFailure(message));
                    }else {
                        final T json = new Gson().fromJson(mDatas, cls);
                        if (callBack != null && mainHandler != null && json != null) {
                            LogUtil.i("onSuccess" + mDatas, HttpUtil.class);
                            mainHandler.post(() -> callBack.onSuccess(json));
                        } else {
                            LogUtil.i("json为null", HttpUtil.class);
                            callBack.onFailure("json为null");
                        }
                    }
                } catch (Exception e) {
                    LogUtil.e("Exception:"+e.getMessage(),HttpUtil.class);

                    if (callBack != null && mainHandler != null) {
                        mainHandler.post(() ->  callBack.onFailure(e.getMessage()));
                    }
                }
            }
        });
    }

    /**
     * POST请求 公共请求部分
     */
    private static <T extends Serializable> void commonPost(Request request, final Class<T> cls, final HttpCallBack<T> callBack) {
        if (request == null) return;
        Call call = getInstance().newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull final IOException e) {
                try {
                    if (callBack != null && mainHandler != null) {
                        mainHandler.post(() -> {
                            LogUtil.i("onFailure:"+e.getMessage(),HttpUtil.class);
                            callBack.onFailure("服务异常,请检查你的网络！");
                        });
                    }
                } catch (Exception e1) {
                    if (callBack != null && mainHandler != null) {
                        mainHandler.post(() ->  callBack.onFailure("数据解析异常"));
                    }
                    LogUtil.e("Exception:"+e1.getMessage(),HttpUtil.class);
                }
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                try {
                    if (callBack != null && mainHandler != null) {
                        String mData = response.body().string();//该流只能调用一次 不然closed异常
                        JSONObject jsonObject =new JSONObject(mData);
                        if (jsonObject.getInt("status")!=1){
                            String message =jsonObject.getString("message");
                            if ("invalid token".equals(jsonObject.getString("code"))) {
                                mainHandler.post(() -> callBack.onFailure("权限认证失败，请重新登录账号"));
                                ActivityManager.getInstance().finishAllActivity();
                               // BeautyDefine.getAccountDefine().controlReLogin();
                            }
                            else
                            mainHandler.post(() ->  callBack.onFailure(message));
                        }else {
                            LogUtil.i("onSuccess:" + mData, HttpUtil.class);
                            final T json = new Gson().fromJson(mData, cls);
                            mainHandler.post(() -> callBack.onSuccess(json));
                        }
                    }
                } catch (Exception e) {
                    LogUtil.e("Exception:"+e.getMessage(),HttpUtil.class);
                    if (callBack != null && mainHandler != null) {
                        mainHandler.post(() ->  callBack.onFailure("数据解析异常"));
                    }

                }
            }
        });

    }

    private static Request getRequestForPost(String url, Map<String, Object> params, Object tag) {
        if (url == null || "".equals(url)) {
            return null;
        }
        if (params == null) {
            params = new HashMap<>();
        }

        Uri.Builder builder = new Uri.Builder();
        for (String key : params.keySet()) {
            builder.appendQueryParameter(key,(String) params.get(key));
        }

        String query = builder.build().toString().replace("?","");

        RequestBody body = RequestBody.create(MEDIA_TYPE_JSON,query);

        LogUtil.i("Send:"+query,HttpUtil.class);

        Request request;
        if (tag != null) {
            request = new Request.Builder().url(url).post(body).tag(tag).build();
        } else {
            request = new Request.Builder().url(url).post(body).build();
        }

        return request;
    }
    private static Request getRequestForPostFile(String url, Map<String, Object> params, Object tag) {
        if (url == null || "".equals(url)) {
            return null;
        }
        Request request ;
        FormBody.Builder builder = new FormBody.Builder();
        for (String key : params.keySet()) {
            builder.add(key, (String) params.get(key));
        }


        //创建RequestBody
        RequestBody body = builder.build();
        //创建RequestBody
        if (tag!=null)
            request =new Request.Builder().url(url).post(body).tag(tag).build();
        else
            request =new Request.Builder().url(url).post(body).build();
        return request;
    }
    private static  <T extends Serializable> RequestBody createProgressRequestBody(final MediaType contentType, final File file, final HttpCallProgressBack<T> callBack) {
        return new RequestBody() {
            @Override
            public MediaType contentType() {
                return contentType;
            }

            @Override
            public long contentLength() {
                return file.length();
            }

            @Override
            public void writeTo(BufferedSink sink) throws IOException {
                Source source;
                try {
                    source = Okio.source(file);
                    Buffer buf = new Buffer();
                    long remaining = contentLength();
                    long current = 0;
                    for (long readCount; (readCount = source.read(buf, 2048)) != -1; ) {
                        sink.write(buf, readCount);
                        current += readCount;
                        LogUtil.i( "current------>" + current,HttpUtil.class);
                        callBack.onProgressCallBack(remaining, current);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }
    private static Request getRequestForGet(String url, HashMap<String, Object> params, Object tag) {
        if (url == null || "".equals(url)) {

            return null;
        }
        Request request;
        if (tag != null) {
            request = new Request.Builder()
                    .url(paramsToString(url, params))
                    .tag(tag)
                    .build();
        } else {
            request = new Request.Builder()
                    .url(paramsToString(url, params))
                    .build();
        }
        return request;
    }


    private static String paramsToString(String url, HashMap<String, Object> params) {
        StringBuilder url_builder = new StringBuilder();
        url_builder.append(url);
        url_builder.append("?");

        if (params != null && params.size() > 0) {
            int i =0;
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                try {
                    if (i==0) {
                        url_builder.append(entry.getKey()).append("=").append(URLEncoder.encode(entry.getValue().toString(), "UTF-8"));
                    }else {
                        url_builder.append("&").append(entry.getKey()).append("=").append(URLEncoder.encode(entry.getValue().toString(), "UTF-8"));
                    }
                    ++i;
                } catch (Exception e) {
                    e.printStackTrace();
                    url_builder.append("&").append(entry.getKey()).append("=").append(entry.getValue());
                }
            }
        }
        LogUtil.i(url_builder.toString());
        return url_builder.toString();
    }

    /**
     * 根据tag标签取消网络请求
     */
    public static void cancelTag(Object tag) {
        if (tag == null) return;
        for (Call call : getInstance().dispatcher().queuedCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
        for (Call call : getInstance().dispatcher().runningCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
    }


    /**
     * 取消所有请求请求
     */
    public static void cancelAll() {
        for (Call call : getInstance().dispatcher().queuedCalls()) {
            call.cancel();
        }
        for (Call call : getInstance().dispatcher().runningCalls()) {
            call.cancel();
        }
    }
}
