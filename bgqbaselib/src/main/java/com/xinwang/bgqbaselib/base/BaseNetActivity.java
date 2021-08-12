package com.xinwang.bgqbaselib.base;



import com.xinwang.bgqbaselib.http.HttpCallBack;
import com.xinwang.bgqbaselib.utils.HttpUtil;
import com.xinwang.bgqbaselib.utils.LogUtil;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Date:2019/8/19
 * Time;14:28
 * author:baiguiqiang
 */
public abstract class BaseNetActivity extends BaseActivity{
    public <T extends Serializable> void  requestGet(String url, HashMap<String, Object> params,Class<T> clas,  HttpCallBack<T> mHttpCallBack){
        LogUtil.i(url);
        HttpUtil.get(getApplicationContext(),url,params,clas,mHttpCallBack,this);
    }
    public <T extends Serializable> void  requestPost(String url, HashMap<String, Object> params,Class<T> clas, HttpCallBack<T> mHttpCallBack){
        HttpUtil.post(getApplicationContext(),url,params,clas,mHttpCallBack,this);
    }

    @Override
    protected void onDestroy() {
        HttpUtil.cancelTag(getApplicationContext(),this);
        super.onDestroy();
    }
}
