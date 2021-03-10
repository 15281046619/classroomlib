package com.xingwang.classroom.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.beautydefinelibrary.BeautyDefine;
import com.beautydefinelibrary.ImagePickerCallBack;
import com.beautydefinelibrary.ImagePickerDefine;
import com.beautydefinelibrary.OpenPageDefine;
import com.beautydefinelibrary.UploadResultCallBack;
import com.beautydefinelibrary.UserInfoDefine;
import com.xingwang.classroom.R;
import com.xingwang.classroom.adapter.CommentDetailAdapter;
import com.xingwang.classroom.bean.CommentBean;
import com.xingwang.classroom.bean.CommentDetailBean;
import com.xingwang.classroom.bean.SendCommentBean;
import com.xingwang.classroom.http.ApiParams;
import com.xingwang.classroom.http.HttpCallBack;
import com.xingwang.classroom.http.HttpUrls;
import com.xingwang.classroom.utils.AndroidBug5497Workaround;
import com.xingwang.classroom.utils.Constants;
import com.xingwang.classroom.utils.HttpUtil;
import com.xingwang.classroom.utils.KeyBoardHelper;
import com.xingwang.classroom.utils.MyToast;
import com.xingwang.classroom.utils.TimeUtil;
import com.xingwang.classroom.view.CustomProgressBar;
import com.xingwang.classroom.view.CustomToolbar;
import com.xingwang.classroom.view.VpSwipeRefreshLayout;
import com.xingwang.classroom.view.WrapContentLinearLayoutManager;
import com.xingwang.classroom.view.loadmore.EndlessRecyclerOnScrollListener;


import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * Date:2019/10/25
 * Time;13:55
 * author:baiguiqiang
 */
public class ClassRoomCommentDetailActivity extends BaseNetActivity implements KeyBoardHelper.OnKeyBoardStatusChangeListener, CommentDetailAdapter.OnClickDetailItemListener {
    private KeyBoardHelper mKeyBoardHelper;
    private RecyclerView mRecyclerView;
    private VpSwipeRefreshLayout swipeRefreshLayout;
    private ImageView ivPic;
    private boolean isClickPic = false;
    private int pageNum=10;
    private String mBid,mDivId,mLectureId;
    private CustomToolbar toolBar;
    private List<CommentBean.DataBean.CommentsBean> mComments=new ArrayList<>();
    private int state=1;
    private CommentDetailAdapter mAdapter;
    private EditText etContent;
    private TextView btSend;
    private ImagePickerDefine imagePickerDefine;
    private  final String[] PERMISSIONS = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA};
    @Override
    protected int layoutResId() {
        return R.layout.activity_comment_detail_classrooom;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (imagePickerDefine != null) {
            imagePickerDefine.onActivityResultHanlder(requestCode, resultCode, data);
        }
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(!isStartLaunch()) {
            AndroidBug5497Workaround.assistActivity(this);
            mKeyBoardHelper = new KeyBoardHelper(this);
            mKeyBoardHelper.onCreate();
            initIntentData();
            initView();
            initSwipeRefresh();
            initListener();
            initRequestData();
        }

    }

    private void initListener() {
        mRecyclerView.setLayoutManager(new WrapContentLinearLayoutManager(this));
        toolBar.setNavigationOnClickListener(v -> finish());
        mKeyBoardHelper.setOnKeyBoardStatusChangeListener(this);

        btSend.setOnClickListener(v -> goSendComment());
        ivPic.setOnClickListener(v ->{
            isClickPic =true;
            requestPermission();
        });
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
                    ivPic.setVisibility(View.VISIBLE);
                }else {
                    btSend.setVisibility(View.VISIBLE);
                    ivPic.setVisibility(View.GONE);
                }
            }
        });
    }

    private void requestPermission() {
        requestDangerousPermissions(PERMISSIONS,100);
    }
    public void requestDangerousPermissions(String[] permissions, int requestCode) {
        if (checkDangerousPermissions(permissions)){
            jumpPic();
            return;
        }
        ActivityCompat.requestPermissions(this, permissions, requestCode);
    }
    private void jumpPic(){
        imagePickerDefine = BeautyDefine.getImagePickerDefine(ClassRoomCommentDetailActivity.this);
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
     * 回复某人图片 取消或者发送失败图片后恢复到最开始状态
     */
    public void choosePicCommentError(){
        if (isClickPic) {
            isClickPic = false;
            etContent.setTag(null);
            etContent.setHint("回复:" + mComments.get(0).getUser().getshowName());
        }
    }


    private void goUploadPic(String s) {
        BeautyDefine.getOpenPageDefine(this).progressControl(new OpenPageDefine.ProgressController.Showder("上传中",false));
        BeautyDefine.getUploadDefine().upload(new File[]{new File(s)},new UploadResultCallBack(){

            @Override
            public void onSucceed(String[] strings) {
                ivPic.setTag(strings[0]);
                goSendComment();
            }

            @Override
            public void onFailure() {
                MyToast.myToast(getApplicationContext(),"上传失败");
                BeautyDefine.getOpenPageDefine(ClassRoomCommentDetailActivity.this).progressControl(new OpenPageDefine.ProgressController.Hider());
            }
        });
    }

    /**
     * 检查是否已被授权危险权限
     * @param permissions w
     * @return w
     */
    public boolean checkDangerousPermissions(String[] permissions) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                return false;
            }
        }
        return true;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 100) {
            for (int grantResult : grantResults) {
                if (grantResult != PackageManager.PERMISSION_GRANTED) {
                    MyToast.myToast(getApplicationContext(), "你拒绝了该权限");
                    return;
                }
            }
            jumpPic();
        }
    }

    private void goSendComment() {
        btSend.setEnabled(false);
        String mContent = etContent.getText().toString();
        ApiParams mApiParams = new ApiParams();
        Object mPos = etContent.getTag();
        if (TextUtils.isEmpty(mContent)) {//发送图片
            mApiParams.with("pics",ivPic.getTag());
        }else{
            BeautyDefine.getOpenPageDefine(this).progressControl(new OpenPageDefine.ProgressController.Showder("发送中",false));
            mApiParams.with("body",mContent);
        }
        mApiParams.with("pid", mComments.get((Integer) mPos).getId()+"");
        HttpUtil.cancelTag(this);
        requestPost(HttpUrls.URL_PUBLISH(),mApiParams, SendCommentBean.class,new HttpCallBack<SendCommentBean>() {

            @Override
            public void onFailure(String message) {
                btSend.setEnabled(true);
                choosePicCommentError();
                MyToast.myToast(getApplicationContext(),message);
                BeautyDefine.getOpenPageDefine(ClassRoomCommentDetailActivity.this).progressControl(new OpenPageDefine.ProgressController.Hider());
            }

            @Override
            public void onSuccess(SendCommentBean commentBean) {
                if (commentBean.getData().getState()==1) {

                    //    MyToast.myToast(getApplicationContext(), " 发送成功");
                    UserInfoDefine mCurUserInfo = BeautyDefine.getUserInfoDefine(getApplicationContext());
                    if(state==3) {//加载完了才能添加到最后
                        String body = "";
                        String pic = "";
                        if (TextUtils.isEmpty(mContent)) {//发送图片
                            pic = ivPic.getTag().toString();
                        } else {
                            body = mContent;
                        }
                        CommentBean.DataBean.CommentsBean.UserBean mToCustomer = mComments.get((Integer) mPos).getUser();
                        CommentBean.DataBean.CommentsBean mCommentsBean = new CommentBean.DataBean.CommentsBean(Integer.parseInt(commentBean.getData().getComment_id()), body, pic,
                                Integer.parseInt(mCurUserInfo.getUserId()),
                                TimeUtil.getCurTime(),
                                new CommentBean.DataBean.CommentsBean.UserBean(Integer.parseInt(mCurUserInfo.getUserId()), mCurUserInfo.getNickName(), mCurUserInfo.getPhone(),
                                        mCurUserInfo.getUserHeadUrl(), mCurUserInfo.getBadge()), mToCustomer);
                        mCommentsBean.setPid(mComments.get((Integer) mPos).getId());
                        mComments.add(mCommentsBean);
                        initAdapter();
                    }
                }else {
                    MyToast.myToast(getApplicationContext(), "正在审核");
                }
                btSend.setEnabled(true);
                isClickPic = false;
                etContent.setHint("回复:" + mComments.get(0).getUser().getshowName());
                etContent.setText("");
                BeautyDefine.getOpenPageDefine(ClassRoomCommentDetailActivity.this).progressControl(new OpenPageDefine.ProgressController.Hider());
                mKeyBoardHelper.closeInputMethodManager();
            }
        });
    }

    private void initIntentData() {
        Uri uri = getIntent().getData();
        if (uri == null) {
            return;
        }
        mBid = uri.getQueryParameter("bid");
        mDivId = uri.getQueryParameter("div_id");
        if (TextUtils.isEmpty(mDivId)){
            mDivId="0";
        }
        mLectureId = uri.getQueryParameter("lecture_id");
    }

    private void initSwipeRefresh() {
        swipeRefreshLayout.setColorSchemeResources(R.color.SwipeRefreshLayoutClassRoom);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            if (mComments.size()>1)
                getData(Constants.LOAD_DATA_TYPE_REFRESH,mComments.get(1).getId()+"");
            else
                swipeRefreshLayout.setRefreshing(false);
        });
    }

    /**
     * mdivid 当前评论id  为0的话是从课程详情跳转过来 没有当前id
     */
    private void initRequestData() {
        requestGet(HttpUrls.URL_COMMENTDETAIL(),new ApiParams().with("id",mDivId.equals("0")?mBid:mDivId), CommentDetailBean.class,new HttpCallBack<CommentDetailBean>() {

            @Override
            public void onFailure(String message) {
                requestFailureShow(message);
            }

            @Override
            public void onSuccess(CommentDetailBean commentBean) {
                mBid =commentBean.getData().getBid()+"";
                if (mDivId.equals("0")){//从课程详情点击过来
                    mComments.add(commentBean.getData());
                    etContent.setHint("回复:"+mComments.get(0).getUser().getshowName());
                    etContent.setTag(0);
                    getData(Constants.LOAD_DATA_TYPE_INIT, "0");
                }else {
                    mComments.add(commentBean.getData());
                    mComments.get(0).setCurPosition(true);
                    if (mDivId.equals(commentBean.getData().getBid()+"")){//顶层评论
                        etContent.setHint("回复:"+mComments.get(0).getUser().getshowName());
                        etContent.setTag(0);
                        getData(Constants.LOAD_DATA_TYPE_INIT, "0");
                    }else {
                        initTopIdData();
                    }
                }
            }
        });
    }

    private void initTopIdData() {
        requestGet(HttpUrls.URL_COMMENTDETAIL(),new ApiParams().with("id",mBid), CommentDetailBean.class,new HttpCallBack<CommentDetailBean>() {

            @Override
            public void onFailure(String message) {
                requestFailureShow(message);
            }

            @Override
            public void onSuccess(CommentDetailBean commentBean) {
                // commentBean.getData().setCurPosition(true);
                mComments.add(0,commentBean.getData());
                etContent.setHint("回复:"+mComments.get(0).getUser().getshowName());
                etContent.setTag(0);
                getData(Constants.LOAD_DATA_TYPE_INIT,mDivId);
            }
        });
    }

    private void initView() {
        mRecyclerView =findViewById(R.id.recycler_view);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        etContent = findViewById(R.id.et_content);
        btSend = findViewById(R.id.bt_send);
        ivPic = findViewById(R.id.iv_pic);
        toolBar = findViewById(R.id.toolbar);
    }

    @Override
    public void OnKeyBoardChanged(boolean visible, int keyBoardHeight) {
        if (!visible){
            if (etContent.getTag()!=null&&!isClickPic){//点击对某人评论在隐藏软键盘
                etContent.setTag(0);
                if (btSend.isEnabled())
                    etContent.setText("");
                etContent.setHint("回复:"+mComments.get(0).getUser().getshowName());
            }
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mKeyBoardHelper!=null){
            mKeyBoardHelper.onDestroy();
        }

    }

    boolean isRequesting =false;
    private void getData(int requestType,String id){
        isRequesting = true;
        if (mBid.equals(mDivId)){
            state = 3;
            findViewById(R.id.rl_empty).setVisibility(View.GONE);
            initAdapter();
        }else {
            ApiParams mApiParams = new ApiParams().with("bid", mBid)
                    .with("num", pageNum).with("lecture_id", mLectureId);
            if (requestType == Constants.LOAD_DATA_TYPE_REFRESH) {
                mApiParams.with("backward_div_id", id);
            }else {
                mApiParams.with("forward_div_id", id);
            }
            requestGet(HttpUrls.URL_COMMENTLIST(), mApiParams, CommentBean.class, new HttpCallBack<CommentBean>() {

                @Override
                public void onFailure(String message) {
                    if (requestType == Constants.LOAD_DATA_TYPE_INIT) {
                        requestFailureShow(message);
                    }
                    isRequesting =false;
                }

                @Override
                public void onSuccess(CommentBean commentBean) {
                    if (requestType != Constants.LOAD_DATA_TYPE_REFRESH) {
                        if (commentBean.getData().getComments().size() < pageNum) {
                            state = 3;
                        } else {
                            state = 1;
                        }
                    }
                    if (requestType == Constants.LOAD_DATA_TYPE_INIT) {
                        findViewById(R.id.rl_empty).setVisibility(View.GONE);
                        mComments.addAll(commentBean.getData().getComments());
                    } else {
                        if (requestType == Constants.LOAD_DATA_TYPE_REFRESH) {
                            for (int i = 0; i <= commentBean.getData().getComments().size() - 1; i++) {
                                mComments.add(1, commentBean.getData().getComments().get(i));
                            }
                        } else
                            mComments.addAll(commentBean.getData().getComments());
                    }
                    initAdapter();
                    isRequesting =false;
                }
            });
        }
    }
    private void requestFailureShow(String error){
        TextView tvMsg = findViewById(R.id.tv_msg);
        tvMsg.setText(error);
        CustomProgressBar progressbar = findViewById(R.id.progressbar);
        progressbar .setVisibility(View.GONE);
        findViewById(R.id.rl_empty).setOnClickListener(v -> {
            progressbar.setVisibility(View.VISIBLE);
            tvMsg.setText("加载中...");
            initRequestData();
        });
    }

    private EndlessRecyclerOnScrollListener mScrollListener = new EndlessRecyclerOnScrollListener() {
        @Override
        public void onLoadMore() {
            if (mComments.size()>0&&!isRequesting)
                getData(Constants.LOAD_DATA_TYPE_MORE,mComments.get(mComments.size()-1).getId()+"");
        }

    };

    public void initAdapter(){
        if (mAdapter==null) {
            mAdapter = new CommentDetailAdapter(mComments, BeautyDefine.getUserInfoDefine(this).getUserId(),this);
            mAdapter.setLoadStateNoNotify(state);
            mAdapter.setOnDetailItemListener(this);
            mRecyclerView.setAdapter(mAdapter);
        }else {
            mAdapter.setLoadStateNoNotify(state);
            mAdapter.notifyDataSetChanged();
        }
        swipeRefreshLayout.setRefreshing(false);
        if (state==3){
            mRecyclerView.removeOnScrollListener(mScrollListener);
        }else {
            mRecyclerView.removeOnScrollListener(mScrollListener);
            mRecyclerView.addOnScrollListener(mScrollListener);
        }
    }

    @Override
    public void onClickItem(int position, int child, int type) {
        etContent.setText("");
        etContent.setHint("回复:"+mComments.get(position).getUser().getshowName());
        etContent.setTag(position);
        etContent.requestFocus();
        mKeyBoardHelper.openInputMethodManager(etContent);
    }
}
