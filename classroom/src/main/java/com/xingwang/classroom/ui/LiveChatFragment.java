package com.xingwang.classroom.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.beautydefinelibrary.BeautyDefine;
import com.beautydefinelibrary.ImagePickerCallBack;
import com.beautydefinelibrary.ImagePickerDefine;
import com.beautydefinelibrary.OpenPageDefine;
import com.beautydefinelibrary.UploadResultCallBack;
import com.xingwang.classroom.R;
import com.xingwang.classroom.adapter.LiveChatAdapter;
import com.xingwang.classroom.bean.LiveChatListBean;
import com.xingwang.classroom.bean.UserInfoBean;
import com.xingwang.classroom.dialog.BottomGifDialog;
import com.xingwang.classroom.dialog.BottomGifSubmitDialog;
import com.xingwang.classroom.dialog.CenterBuyDialog;
import com.xingwang.classroom.dialog.CenterRedPackDialog;
import com.xingwang.classroom.http.ApiParams;
import com.xingwang.classroom.http.CommonEntity;
import com.xingwang.classroom.http.HttpCallBack;
import com.xingwang.classroom.http.HttpUrls;
import com.xingwang.classroom.utils.Constants;
import com.xingwang.classroom.utils.GlideUtils;
import com.xingwang.classroom.utils.GsonUtils;
import com.xingwang.classroom.utils.KeyBoardHelper;
import com.xingwang.classroom.utils.MyToast;
import com.xingwang.classroom.view.VpSwipeRefreshLayout;
import com.xingwang.classroom.view.WrapContentLinearLayoutManager;
import com.xingwang.classroom.view.gift.GiftBean;
import com.xingwang.classroom.view.gift.GiftView;
import com.xingwang.classroom.ws.ChannelStatusListener;
import com.xingwang.classroom.ws.WsManagerUtil;
import org.json.JSONObject;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Date:2020/8/13
 * Time;15:24
 * author:baiguiqiang
 */
public class LiveChatFragment extends BaseLazyLoadFragment implements KeyBoardHelper.OnKeyBoardStatusChangeListener {
    private VpSwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private LiveChatAdapter mAdapter;
    private ImageView ivGift;
    private int maxSum =15;
    private String channel;
    private TextView btSend,tvFixed,tvNewMessage;
    private ImageView ivPic,ivBug;
    private EditText etContent;
    private int newMessageSum =0;
    private KeyBoardHelper mKeyBoardHelper;
    private GiftView giftView;
    private String quote_id=null;//@某条id
    private boolean isClickPic=false;
    private BottomGifDialog mChooseGiftDialog;
    private int curJF =-1;
    private String speaker;
    private  BottomGifSubmitDialog mGitSubmit;
    private Handler handler =new Handler();
    private Runnable showGiftSubmitRunnable = () -> showGifSubmitDialog();
    private Runnable closeGiftSubmitRunnable =new Runnable() {
        @Override
        public void run() {
            if (mGitSubmit!=null){
                mGitSubmit.dismissDialog();
                mGitSubmit=null;
            }
        }
    };
    public static LiveChatFragment getInstance(String id,String fixedStr,String speaker){
        LiveChatFragment mFragment = new LiveChatFragment();
        Bundle bundle = new Bundle();
        bundle.putString("id",id);
        bundle.putString("fixedStr",fixedStr);//置顶内容 ''为不置顶
        bundle.putString("speaker",speaker);//置顶内容 ''为不置顶
        mFragment.setArguments(bundle);
        return mFragment;
    }



    private WrapContentLinearLayoutManager mLinearLayout;
    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_live_chat_classroom,container,false);
        mKeyBoardHelper =new KeyBoardHelper(getActivity());
        mKeyBoardHelper.onCreate();
        speaker =getArguments().getString("speaker");
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        btSend = view.findViewById(R.id.bt_send);
        ivPic = view.findViewById(R.id.iv_pic);
        ivGift = view.findViewById(R.id.ivGift);
        tvNewMessage = view.findViewById(R.id.tvNewMessage);
        giftView = view.findViewById(R.id.giftView);

        tvFixed = view.findViewById(R.id.tvFixed);
        ivBug = view.findViewById(R.id.ivBug);
        ivBug.setAlpha(0.7f);
        GlideUtils.loadGif(R.mipmap.ic_qg_classroom,ivBug);
        setFixed(getArguments().getString("fixedStr"));
        etContent = view.findViewById(R.id.et_content);
        recyclerView = view.findViewById(R.id.recyclerView);
        mLinearLayout = new WrapContentLinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLinearLayout);

        mAdapter = new LiveChatAdapter(getActivity(),speaker);
        tvNewMessage.setOnClickListener(v -> {
            if (mAdapter!=null)
                recyclerView.scrollToPosition(mAdapter.getItemCount() - 1);
        });
        mAdapter.setOnItemClick((position, child, type) -> {
            etContent.setText("");
            etContent.setTag(position);
            etContent.setHint("回复:"+mAdapter.getData().get(position).getUser().getNickname());
            quote_id = String.valueOf(mAdapter.getData().get(position).getId());
            etContent.requestFocus();
            mKeyBoardHelper.openInputMethodManager(etContent);
        });
        recyclerView.setAdapter(mAdapter);
        swipeRefreshLayout.setColorSchemeResources(R.color.SwipeRefreshLayoutClassRoom);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            if (mAdapter!=null&&mAdapter.getData().size()>0) {
                requestData(Constants.LOAD_DATA_TYPE_MORE, mAdapter.getData().get(0).getId());
            }else {
                requestData(Constants.LOAD_DATA_TYPE_MORE,Integer.MAX_VALUE);
                // swipeRefreshLayout.setRefreshing(false);
            }
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
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(newMessageSum!=0&&mAdapter.getItemCount()-1-newMessageSum < mLinearLayout.findLastVisibleItemPosition()){
                    newMessageSum =0;
                    updateTvNewMessage();
                }
            }
        });
        if (BeautyDefine.getUserInfoDefine(getActivity()).isOfficial()) {
            ivPic.setOnClickListener(v -> {
                isClickPic = true;
                requestPermission();
            });
            ivPic.setVisibility(View.VISIBLE);
        }else {
            ivPic.setVisibility(View.GONE);
            // ivPic.setColorFilter(ContextCompat.getColor(getActivity(),R.color.GrayClassRoom));
        }
        ivGift.setOnClickListener(v -> showChooseGiftDialog());
        mKeyBoardHelper.setOnKeyBoardStatusChangeListener(this);
        LiveDetailActivity mActivity = (LiveDetailActivity) getActivity();
        if (Constants.isShowGiftSubmitDialog&&mActivity.isLive){
            handler.postDelayed(showGiftSubmitRunnable,10000);
        }

        return view;
    }


    private int curChooseJf=0;
    private void showChooseGiftDialog() {
        if (curJF!=-1) {
            if (mChooseGiftDialog==null||!mChooseGiftDialog.isVisible()) {
                mChooseGiftDialog = BottomGifDialog.getInstance(giftView.getAllGift(), curJF);
                mChooseGiftDialog.setCallback(integer -> {
                    curChooseJf = Integer.parseInt(giftView.getAllGift().get(integer).getGiftPrice());
                    goSendComment(giftView.getAllGift().get(integer).getGiftName(), "4");
                });
                mChooseGiftDialog.showDialog(Objects.requireNonNull(getActivity()).getSupportFragmentManager());
            }
        }else {
            getJF();
        }
    }

    private void setFixed(String content){

        if (!TextUtils.isEmpty(content)) {
            tvFixed.setText(Html.fromHtml(content));
            tvFixed.setVisibility(View.VISIBLE);
            tvFixed.requestFocus();
            tvFixed.setSelected(true);
        }else {
            tvFixed.setSelected(false);
            tvFixed.clearFocus();
            tvFixed.setVisibility(View.GONE);
        }
    }
    private void requestPermission(){
        requestDangerousPermissions(new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA},100);
    }
    public void requestDangerousPermissions(String[] permissions, int requestCode) {
        if (checkDangerousPermissions(permissions)){
            jumpPic();

            return;
        }
        requestPermissions( permissions, requestCode);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 100) {
            for (int grantResult : grantResults) {
                if (grantResult != PackageManager.PERMISSION_GRANTED) {
                    MyToast.myToast(getActivity().getApplicationContext(), "你拒绝了该权限");
                    return;
                }
            }
            jumpPic();
        }
    }
    private ImagePickerDefine imagePickerDefine;
    private void jumpPic(){
        imagePickerDefine = BeautyDefine.getImagePickerDefine((AppCompatActivity) getActivity());
        imagePickerDefine.showSinglePicker(false, new ImagePickerCallBack() {
            @Override
            public void onResult(List<String> list, ImagePickerDefine.MediaType mediaType, List<String> list1) {
                if (list!=null&&list.size()>0)
                    goUploadPic(list.get(0));
            }

            @Override
            public void onCancel() {
                choosePicCommentError();
            }
        });
    }
    /**
     * 上传图片
     * @param s 路径
     */
    private void goUploadPic(String s) {
        BeautyDefine.getOpenPageDefine(getActivity()).progressControl(new OpenPageDefine.ProgressController.Showder("上传中",false));
        BeautyDefine.getUploadDefine().upload(new File[]{new File(s)},new UploadResultCallBack(){

            @Override
            public void onSucceed(String[] strings) {
                goSendComment(strings[0],"2");
            }

            @Override
            public void onFailure() {
                choosePicCommentError();
                MyToast.myToast(getActivity().getApplicationContext(),"上传失败");
                BeautyDefine.getOpenPageDefine(getActivity()).progressControl(new OpenPageDefine.ProgressController.Hider());
            }
        });
    }
    private void choosePicCommentError() {
        if (isClickPic) {
            isClickPic = false;
            quote_id=null;
            etContent.setHint("请输入你想说的话");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (imagePickerDefine != null) {
            imagePickerDefine.onActivityResultHanlder(requestCode, resultCode, data);
        }
    }

    /**
     * 检查是否已被授权危险权限
     */
    public boolean checkDangerousPermissions(String[] permissions) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }

        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(getActivity(), permission) != PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), permission)) {
                return false;
            }
        }
        return true;
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

    /**
     * 处理系统推送的消息
     * @param message
     */
    private void handlerMessage(String message) {
        try{
            JSONObject object = new JSONObject(message);
            switch (object.getJSONObject("content").getString("event")){
                case Constants.CHANNEL_TYPE_NEW_ORDER:
                case Constants.CHANNEL_TYPE_NEW_MESSAGE:
                    LiveChatListBean.DataBean.ItemsBean mBean = GsonUtils.changeGsonToBean(object.getJSONObject("content").getString("data"), LiveChatListBean.DataBean.ItemsBean.class);
                    showChannelMessage(mBean);
                    break;
                case  Constants.CHANNEL_TYPE_REMOVE_ITEM:
                    int mId =object.getJSONObject("content").getJSONObject("data").getInt("id");
                    for (int i=0;i<mAdapter.getData().size();i++){
                        if(mAdapter.getData().get(i).getId()==mId){
                            int finalI = i;
                            getActivity().runOnUiThread(() -> {
                                mAdapter.getData().remove(finalI);
                                mAdapter.notifyDataSetChanged();
                            });
                            break;
                        }
                    }
                    break;
                case  Constants.CHANNEL_TYPE_LIVE_STOP:
                    getActivity().setResult(101);//移除聊天室
                    if (getActivity() instanceof LiveDetailActivity){
                        ((LiveDetailActivity) getActivity()).liveEnd =true;
                    }
                    break;
                case  Constants.CHANNEL_TYPE_LIVE_FIXED1:
                    String showContent= object.getJSONObject("content").getString("data");
                    getActivity().runOnUiThread(() -> setFixed(showContent));

                    break;
                case  Constants.CHANNEL_TYPE_LIVE_FIXED2:
                    getActivity().runOnUiThread(() -> setFixed(""));
                    break;
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
        GiftBean mGiftBean = getGiftBean(mBean);
        if (mAdapter!=null){//还没有初始化
            getActivity().runOnUiThread(() -> {
                if (((LiveDetailActivity)getActivity()).orientationUtils!=null&&((LiveDetailActivity)getActivity()).orientationUtils.getIsLand()!=0 ){//横屏幕
                    if (!TextUtils.isEmpty(mBean.getGame_tips())){//答题获奖 横批不显示红包dialog
                        ((LiveDetailActivity)getActivity()).addSySDanMu("恭喜“"+mBean.getUser().getNickname()+"”抢答中获得积分");
                        return;
                    }
                    if (mBean.getType()==1) {
                        ((LiveDetailActivity) getActivity()).addDanMu(mBean.getUser().getNickname() + "：" + mBean.getBody());
                    } else if (mBean.getType()==2){
                        ((LiveDetailActivity)getActivity()).addDanMu(mBean.getUser().getNickname()+":[图片]");
                    }else if (mBean.getType()==3){
                        ((LiveDetailActivity)getActivity()).addSySDanMu("恭喜“"+mBean.getUser().getNickname()+"”下单成功");
                    }else if (mBean.getType()==4){
                        ((LiveDetailActivity)getActivity()).addSySDanMu("“"+mBean.getUser().getNickname()+"”打赏了【"+mBean.getBody()+"】给"+speaker);
                    }
                }else {
                    if (!TextUtils.isEmpty(mBean.getGame_tips())){//答题获奖
                        if (String.valueOf(mBean.getUser().getId()).equals(BeautyDefine.getUserInfoDefine(getContext()).getUserId())){//获奖者是自己显示红包dialog
                            showRedPackDialog(mBean.getGame_tips());
                        }else {//
                            ((LiveDetailActivity)getActivity()).addSySDanMu("恭喜“"+mBean.getUser().getNickname()+"”抢答中获得积分");
                        }
                    }else if (mBean.getType()==3){
                        ((LiveDetailActivity)getActivity()).addSySDanMu("恭喜“"+mBean.getUser().getNickname()+"”下单成功");
                    }else if (mBean.getType()==4){
                        ((LiveDetailActivity)getActivity()).addSySDanMu("“"+mBean.getUser().getNickname()+"”打赏了【"+mBean.getBody()+"】给"+speaker);
                        if (mGiftBean!=null)
                            giftView.addData(mGiftBean);
                    }


                    boolean isScrollViewEnd = mLinearLayout.findLastVisibleItemPosition() == (mAdapter.getItemCount() - 2) || mAdapter.getItemCount() == 0;//是否滚动到底部
                    mAdapter.notifyItemInserted(mAdapter.getItemCount() - 1);
                    if (isScrollViewEnd) {//是否到底底部，没有不用移动
                        recyclerView.scrollToPosition(mAdapter.getItemCount() - 1);
                    }else {
                        newMessageSum++;
                        updateTvNewMessage();
                    }
                }
            });
        }
    }

    private GiftBean getGiftBean(LiveChatListBean.DataBean.ItemsBean mBean) {
        if (mBean.getType()==4){
            GiftBean mCurGift = getGiftBean(mBean.getBody());
            if (mCurGift!=null)
                return new GiftBean(mBean.getUser().getAvatar(),mBean.getUser().getNickname(),mBean.getUser().getId()+""
                        ,mCurGift.getGiftName(),mCurGift.getGiftId(),"",mCurGift.getGiftImgLoc(),mCurGift.getGiftPrice(),1);
        }
        return null;

    }
    private GiftBean getGiftBean(String body){
        for (int i=0;i<giftView.getAllGift().size();i++){
            if (giftView.getAllGift().get(i).getGiftName().equals(body)){
                return giftView.getAllGift().get(i);
            }
        }
        return  null;
    }
    /**
     * 刷新adater
     */
    public void refreshAdapter(){
        if (mAdapter!=null&&mAdapter.getData().size()!=mAdapter.getItemCount()){
            mAdapter.notifyDataSetChanged();
            recyclerView.scrollToPosition(mAdapter.getItemCount()-1);
        }
    }

    private void showRedPackDialog(String tip) {
        if (getActivity() instanceof LiveDetailActivity)
            CenterRedPackDialog.getInstance(((LiveDetailActivity)getActivity()).mLiveDetailBean.getData().getLive().getCover(),"直播间",tip).showDialog(getActivity().getSupportFragmentManager());
    }


    private void updateTvNewMessage(){
        if (newMessageSum>0){
            tvNewMessage.setText(newMessageSum+"条新消息");
            tvNewMessage.setVisibility(View.VISIBLE);
        }else {
            tvNewMessage.setVisibility(View.GONE);
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

    private void showGifSubmitDialog(){
        if (mGitSubmit==null) {
            mGitSubmit = BottomGifSubmitDialog.getInstance(
                    ((LiveDetailActivity) getActivity()).mLiveDetailBean.getData().getLive().getCover(),
                    BeautyDefine.getUserInfoDefine(getContext()).getNickName());
            mGitSubmit.setCallback(integer -> {
                showChooseGiftDialog();
            });
            mGitSubmit.showDialog(Objects.requireNonNull(getActivity()).getSupportFragmentManager());
            Constants.isShowGiftSubmitDialog =false;
            handler.postDelayed(closeGiftSubmitRunnable,8000);
        }
    }

    public void initAdapter(boolean isInit,int sum){
        if (isInit) {
            mAdapter.notifyDataSetChanged();
            recyclerView.scrollToPosition(mAdapter.getItemCount()-1);
            initSend();
        }else {
            mAdapter.notifyDataSetChanged();
            if (sum>0)
                recyclerView.scrollToPosition(sum+1);
        }
        swipeRefreshLayout.setRefreshing(false);

    }

    private void initSend() {
        ivBug.setOnClickListener(v -> CenterBuyDialog.getInstance().setCallback(s -> goSendComment(s,"3")).showDialog(getActivity().getSupportFragmentManager()));
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
                if (TextUtils.isEmpty(etContent.getText().toString().trim())){
                    btSend.setVisibility(View.GONE);
                    if (BeautyDefine.getUserInfoDefine(getActivity()).isOfficial())
                        ivPic.setVisibility(View.VISIBLE);
                    ivGift.setVisibility(View.VISIBLE);
                }else {
                    btSend.setVisibility(View.VISIBLE);
                    ivPic.setVisibility(View.GONE);
                    ivGift.setVisibility(View.GONE);
                }
            }
        });
    }

    /**
     * 发生评论  1普通文本，2图片 3,下单，4打赏
     */
    private void goSendComment(String body,String type) {
        if (type.equals("1"))
            btSend.setEnabled(false);
        ApiParams mParams = new ApiParams().with("live_id", getArguments().getString("id"))
                .with("type", type).with("body", body);
        if (quote_id!=null&&type!="3"&&type!="4")
            mParams.with("quote_id",quote_id);
        requestPost(HttpUrls.URL_LIVE_CHAT_SEND(),mParams,
                CommonEntity.class, new HttpCallBack<CommonEntity>() {

                    @Override
                    public void onFailure(String message) {
                        if (type.equals("1")) {
                            btSend.setEnabled(true);
                        }else if (type.equals("2")){
                            BeautyDefine.getOpenPageDefine(getActivity()).progressControl(new OpenPageDefine.ProgressController.Hider());
                            choosePicCommentError();
                        }else if (type.equals("4")){
                            if (mChooseGiftDialog!=null)
                                mChooseGiftDialog.setGiftFailure();
                        }
                        MyToast.myToast(getActivity(),message);

                    }

                    @Override
                    public void onSuccess(CommonEntity commonEntity) {
                        if (type.equals("1")) {
                            btSend.setEnabled(true);
                            etContent.setText("");
                            KeyBoardHelper.hideSoftInput(getActivity());
                            // MyToast.myToast(getActivity(),commonEntity.getMessage());
                        }else if (type.equals("2")){
                            BeautyDefine.getOpenPageDefine(getActivity()).progressControl(new OpenPageDefine.ProgressController.Hider());
                            choosePicCommentError();
                            // MyToast.myToast(getActivity(),commonEntity.getMessage());
                        }else if (type.equals("3")){
                            MyToast.myToast(getActivity(),"下单成功");
                        }else if (type.equals("4")){
                            if (mChooseGiftDialog!=null) {
                                curJF =curJF -curChooseJf;
                                mChooseGiftDialog.sendGiftSuccess(curJF);
                            }
                        }

                    }
                });
    }

    private void getJF(){
        ivGift.setEnabled(false);
        requestGet(HttpUrls.URL_USER_INFO(),new ApiParams(),
                UserInfoBean.class, new HttpCallBack<UserInfoBean>() {

                    @Override
                    public void onFailure(String message) {
                        ivGift.setEnabled(true);
                        MyToast.myLongToast(getActivity(),"获取积分数目失败，请重新点击");

                    }

                    @Override
                    public void onSuccess(UserInfoBean mUserBean) {
                        ivGift.setEnabled(true);
                        curJF =mUserBean.getData().getJifen();
                        showChooseGiftDialog();
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
    public void onDestroyView() {
        WsManagerUtil.getInstance().onDestroy(null);
        super.onDestroyView();
        if (mKeyBoardHelper != null) {
            mKeyBoardHelper.onDestroy();
        }
        if(giftView!=null){
            giftView.onDestroy();
        }

        if (handler!=null&&showGiftSubmitRunnable!=null){
            handler.removeCallbacks(showGiftSubmitRunnable);
        }

        if (handler!=null&&closeGiftSubmitRunnable!=null){
            handler.removeCallbacks(closeGiftSubmitRunnable);
        }
    }

    @Override
    public void OnKeyBoardChanged(boolean visible, int keyBoardHeight) {
        if (!visible){
            if (quote_id!=null&&!isClickPic){//点击对某人评论在隐藏软键盘
                quote_id=null;
                if (btSend.isEnabled())
                    etContent.setText("");
                etContent.setHint("请输入你想说的话");
            }
        }
    }

}
