package com.xingwang.classroom.ui;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beautydefinelibrary.BeautyDefine;
import com.xingwang.classroom.R;
import com.xingwang.classroom.adapter.DetailAdapter;
import com.xingwang.classroom.adapter.LiveChatAdapter;
import com.xingwang.classroom.bean.LiveChatListBean;
import com.xingwang.classroom.bean.OnlineCountBean;
import com.xingwang.classroom.http.ApiParams;
import com.xingwang.classroom.http.CommonEntity;
import com.xingwang.classroom.http.HttpCallBack;
import com.xingwang.classroom.http.HttpUrls;
import com.xingwang.classroom.utils.Constants;
import com.xingwang.classroom.utils.GsonUtils;
import com.xingwang.classroom.utils.KeyBoardHelper;
import com.xingwang.classroom.utils.LogUtil;
import com.xingwang.classroom.utils.MyToast;
import com.xingwang.classroom.view.VpSwipeRefreshLayout;
import com.xingwang.classroom.ws.ChannelStatusListener;
import com.xingwang.classroom.ws.WsManagerUtil;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Date:2020/8/13
 * Time;15:24
 * author:baiguiqiang
 */
public class LiveChatFragment extends BaseLazyLoadFragment {
    private VpSwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private LiveChatAdapter mAdapter;
    private int maxSum =10;
    private String channel;
    private TextView btSend,tvFixed;
    private ImageView ivPic;
    private EditText etContent;


    public static LiveChatFragment getInstance(String id,String fixedStr){
        LiveChatFragment mFragment = new LiveChatFragment();
        Bundle bundle = new Bundle();
        bundle.putString("id",id);
        bundle.putString("fixedStr",fixedStr);//置顶内容 ''为不置顶
        mFragment.setArguments(bundle);
        return mFragment;
    }
    private  LinearLayoutManager mLinearLayout;
    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_live_chat,container,false);

        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        btSend = view.findViewById(R.id.bt_send);
        ivPic = view.findViewById(R.id.iv_pic);

        tvFixed = view.findViewById(R.id.tvFixed);

        setFixed(getArguments().getString("fixedStr"));
        etContent = view.findViewById(R.id.et_content);
        recyclerView = view.findViewById(R.id.recyclerView);
        mLinearLayout = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLinearLayout);
        mAdapter = new LiveChatAdapter(getActivity());
        recyclerView.setAdapter(mAdapter);
        swipeRefreshLayout.setColorSchemeResources(R.color.SwipeRefreshLayoutClassRoom);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            if (mAdapter!=null&&mAdapter.getData().size()>0)
                requestData(Constants.LOAD_DATA_TYPE_MORE,mAdapter.getData().get(0).getId());
        });

        requestSubscribe();
        //底部布局弹出,聊天列表上滑
        recyclerView.addOnLayoutChangeListener((v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) -> {
            if (bottom < oldBottom) {
                recyclerView.post(() -> {
                    if (mAdapter.getItemCount() > 0) {
                        recyclerView.scrollToPosition(mAdapter.getItemCount() - 1);
                    }
                });

            }
        });

        ivPic.setOnTouchListener((v, event) -> {//警告 会onclick冲突
            KeyBoardHelper.hideSoftInput(getActivity());
            return false;
        });

        return view;
    }
    private void setFixed(String content){
        if (!TextUtils.isEmpty(content)) {
            tvFixed.setText(Html.fromHtml(content));
            tvFixed.setVisibility(View.VISIBLE);
            tvFixed.setSelected(true);
        }else {
            tvFixed.setVisibility(View.GONE);
        }
    }
    /**
     * 长连接 评论
     */
    void requestSubscribe(){
        WsManagerUtil.getInstance().onCreate(getActivity(), new ChannelStatusListener() {
            @Override
            public void createSuccess(String response) {
                super.createSuccess(response);
                channel= HttpUrls.LIVE_CHANNEL+getArguments().getString("id");
                WsManagerUtil.getInstance().subscribe(channel, new ChannelStatusListener() {
                    @Override
                    public void subscribeSuccess(String response) {
                        super.subscribeSuccess(response);

                    }

                    @Override
                    public void onMessage(String message) {
                        super.onMessage(message);
                        handlerMessage(message);
                    }

                    @Override
                    public void onFailure(int code, String message) {
                        super.onFailure(code, message);
                        if (code==WsManagerUtil.NETERROR){
                            MyToast.myToast(getActivity().getApplicationContext(),message);
                        }
                    }
                });
            }
        });
    }

    private void handlerMessage(String message) {
        try{
            JSONObject object = new JSONObject(message);
            if(Constants.CHANNEL_TYPE_NEW_MESSAGE.equals(object.getJSONObject("content").getString("event"))){//新消息
                LiveChatListBean.DataBean.ItemsBean mBean = GsonUtils.changeGsonToBean(object.getJSONObject("content").getString("data"), LiveChatListBean.DataBean.ItemsBean.class);
                showChannelMessage(mBean);
            }
        }catch (Exception e){

        }
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getActivity()!=null)
            KeyBoardHelper.hideSoftInput(getActivity());
    }

    /**
     *  显示推送消息
     * @param mBean
     */
    public void showChannelMessage(LiveChatListBean.DataBean.ItemsBean mBean){

        mAdapter.getData().add(mBean);
        if (mAdapter!=null){//还没有初始化

            getActivity().runOnUiThread(() -> {
                mAdapter.notifyItemInserted(mAdapter.getItemCount()-1);
                recyclerView.scrollToPosition(mAdapter.getItemCount()-1);
            });
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (getActivity()!=null)
            KeyBoardHelper.hideSoftInput(getActivity());
    }

    @Override
    public void initData() {
        requestData(Constants.LOAD_DATA_TYPE_INIT,Integer.MAX_VALUE);
    }
    public void initAdapter(boolean isInit,int sum){
        if (isInit) {

            recyclerView.scrollToPosition(mAdapter.getItemCount()-1);
         /*   recyclerView.post(() -> {
                //设置第一条可见的消息
                // 如果ListView的刷新还没有完成，直接就调用setSelection，就会导致无效。
                recyclerView.scrollToPosition(mAdapter.getItemCount()-1);
            });*/
            initSend();
        }else {
            mAdapter.notifyDataSetChanged();
            if (sum>0)
                recyclerView.scrollToPosition(sum+1);

        /*    if (sum>0)
                recyclerView.post(() -> {
                    //设置第一条可见的消息
                    // 如果ListView的刷新还没有完成，直接就调用setSelection，就会导致无效。
                    recyclerView.scrollToPosition(sum+1);
                });*/
        }
        swipeRefreshLayout.setRefreshing(false);

    }

    private void initSend() {

        btSend.setOnClickListener(v -> goSendComment(etContent.getText().toString(),"1"));
        etContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(etContent.getText().toString())){
                    btSend.setVisibility(View.GONE);
                    ivPic.setVisibility(View.VISIBLE);
                }else {
                    btSend.setVisibility(View.VISIBLE);
                    ivPic.setVisibility(View.GONE);

                }
            }
        });
    }

    /**
     * 发生评论  1普通文本，2图片
     */
    private void goSendComment(String body,String type) {
        btSend.setEnabled(false);
        etContent.setText("");
        requestPost(HttpUrls.URL_LIVE_CHAT_SEND(),new ApiParams().with("live_id",getArguments().getString("id")).with("type",type).with("body",body),
                CommonEntity.class, new HttpCallBack<CommonEntity>() {

                    @Override
                    public void onFailure(String message) {
                        btSend.setEnabled(true);
                        MyToast.myToast(getActivity(),message);
                    }

                    @Override
                    public void onSuccess(CommonEntity commonEntity) {
                        btSend.setEnabled(true);
                        MyToast.myToast(getActivity(),commonEntity.getMessage());
                    }
                });
    }

    private void requestData(int loadDataTypeInit,int preId) {
        requestGet(HttpUrls.URL_LIVE_CHAT_LISTS(),new ApiParams().with("live_id",getArguments().getString("id")).with("num",maxSum).with("pre_id",preId),
                LiveChatListBean.class, new HttpCallBack<LiveChatListBean>() {

                    @Override
                    public void onFailure(String message) {
                        if (loadDataTypeInit!=Constants.LOAD_DATA_TYPE_INIT){
                            MyToast.myToast(getActivity(),message);
                        }
                        swipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onSuccess(LiveChatListBean mVodListBean) {
                        Collections.reverse(mVodListBean.getData().getItems());//排序倒叙
                        if (loadDataTypeInit != Constants.LOAD_DATA_TYPE_INIT) {
                            if (loadDataTypeInit == Constants.LOAD_DATA_TYPE_REFRESH)
                                mAdapter.getData().clear();
                        }
                        mAdapter.getData().addAll(0,mVodListBean.getData().getItems());// 这里可能出现推送消息先加入列表 ，请求接口在获取数据 ，有重复数据
                        initAdapter(loadDataTypeInit == Constants.LOAD_DATA_TYPE_INIT,mVodListBean.getData().getItems().size());
                    }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        WsManagerUtil.getInstance().onDestroy(null);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }
}
