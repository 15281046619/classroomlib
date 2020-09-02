package com.xingwang.swip.utils;

import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.util.Log;

import com.blankj.utilcode.util.EmptyUtils;
import com.xingwang.swip.bean.CommonEntity;

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
        commonGet(getRequestForGet(url, null, null), callBack);
    }

    /**
     * @param url      url地址
     * @param params   HashMap<String, String> 参数
     * @param callBack 请求回调接口
     */
    public static void get(String url, HashMap<String, String> params, HttpCallBack callBack) {
        commonGet(getRequestForGet(url, params, null),callBack);
    }
    /**
     * @param url      url地址
     * @param params   HashMap<String, String> 参数
     * @param callBack 请求回调接口
     * @param tag      网络请求tag
     */
    public static void get(String url, HashMap<String, String> params, HttpCallBack callBack, Object tag) {
        commonGet(getRequestForGet(url, params, tag), callBack);
    }

    /**
     * @param url      url地址
     * @param callBack 请求回调接口
     */
    public static void post(String url, HttpCallBack callBack) {
        commonPost(getRequestForPost(url, null, null),callBack);
    }




    /**
     * @param url      url地址
     * @param params   HashMap<String, Object> 参数
     * @param callBack 请求回调接口
     */
    public static void post(String url, HashMap<String, String> params, HttpCallBack callBack) {
        commonPost(getRequestForPost(url, params, null), callBack);
    }

    /**
     *
     * @param actionUrl 地址
     * @param tag  标识
     * @param paramsMap 参数
     * @param callBack 返回
     */
  /*  public static void  upLoadFile(String actionUrl,Object tag, HashMap<String, Object> paramsMap,final HttpCallBack callBack){
        commonPost(getRequestForPostFile(actionUrl,paramsMap,tag),callBack);
    }
    public static void  upLoadFile(String actionUrl, HashMap<String, Object> paramsMap,final HttpCallBack callBack){
        commonPost(getRequestForPostFile(actionUrl,paramsMap,null),callBack);
    }*/

    /**
     * GET请求 公共请求部分
     */
    private static void commonGet(Request request,final HttpCallBack callBack) {
        if (request == null) return;
        Call call = getInstance().newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull final IOException e) {
                Log.i("commonGet",e.getMessage());
                mainHandler.post(() -> callBack.onFailure("请求失败"));
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                try {
                    String mDatas = response.body().string();
                    Log.i("commonGet",mDatas);
                    final CommonEntity commonEntity = JsonUtils.jsonToPojo(mDatas,CommonEntity.class);

                    handleResult(commonEntity,callBack);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * POST请求 公共请求部分
     */
    private static <T extends Serializable> void commonPost(Request request,final HttpCallBack callBack) {
        if (request == null) return;
        Call call = getInstance().newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull final IOException e) {
                mainHandler.post(() -> {
                   // Log.i("commonPost","onFailure:"+e.getMessage());
                    callBack.onFailure("请求失败!");
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                try {
                    String mdata = response.body().string();//该流只能调用一次 不然closed异常
                    Log.i("commonPost","onSuccess:"+mdata);
                    final CommonEntity<T> commonEntity = JsonUtils.jsonToPojo(mdata,CommonEntity.class);
                    handleResult(commonEntity,callBack);

                } catch (Exception e) {
                    mainHandler.post(() ->  callBack.onFailure(e.getMessage()));
                }
            }
        });

    }

    private static void handleResult(CommonEntity commonEntity,HttpCallBack callBack){
        if (EmptyUtils.isEmpty(commonEntity)){
            mainHandler.post(() ->  callBack.onFailure("数据解析错误!"));
            return;
        }
        if (commonEntity.getStatus()==1){
            mainHandler.post(() -> callBack.onSuccess(JsonUtils.objectToJson(commonEntity.getData())));
        }else {
            //Log.i("handle","失败"+commonEntity.getStatus()+commonEntity.getMessage());
            mainHandler.post(() -> callBack.onFailure(commonEntity.getMessage()));
        }
    }

    private static Request getRequestForPost(String url, Map<String, String> params, Object tag) {
        if (url == null || "".equals(url)) {
            return null;
        }
        if (params == null) {
            params = new HashMap<>();
        }

        //Log.i("url",url);

        Uri.Builder builder = new Uri.Builder();
        for (String key : params.keySet()) {
            builder.appendQueryParameter(key,params.get(key));
        }

        String query = builder.build().toString().replace("?","");

        //Log.i("param",query);

        RequestBody body = RequestBody.create(MEDIA_TYPE_JSON,query);

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

    private static Request getRequestForGet(String url, HashMap<String, String> params, Object tag) {
        if (url == null || "".equals(url)) {

            return null;
        }

       // Log.i("url",url);

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

        //Log.i("param",request.url().toString());
        return request;
    }


    private static String paramsToString(String url, HashMap<String, String> params) {
        StringBuilder url_builder = new StringBuilder();
        url_builder.append(url);
        url_builder.append("?");
        if (params != null && params.size() > 0) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                try {
                    url_builder.append("&").append(entry.getKey()).append("=").append(URLEncoder.encode(entry.getValue(), "UTF-8"));
                } catch (Exception e) {
                    e.printStackTrace();
                    url_builder.append("&").append(entry.getKey()).append("=").append(entry.getValue());
                }
            }
        }
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

    public interface HttpCallBack {
        void onFailure(String message);
        void onSuccess(String json);
    }
}
