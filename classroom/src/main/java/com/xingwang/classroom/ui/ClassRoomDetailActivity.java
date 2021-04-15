package com.xingwang.classroom.ui;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;

import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beautydefinelibrary.BeautyDefine;
import com.beautydefinelibrary.ImagePickerCallBack;
import com.beautydefinelibrary.ImagePickerDefine;
import com.beautydefinelibrary.OpenPageDefine;
import com.beautydefinelibrary.ShareResultCallBack;
import com.beautydefinelibrary.UploadResultCallBack;

import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder;
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;
import com.xingwang.classroom.ClassRoomLibUtils;
import com.xingwang.classroom.R;
import com.xingwang.classroom.adapter.DetailBarrageAdapter;
import com.xingwang.classroom.bean.ADBean;
import com.xingwang.classroom.bean.CommentBean;
import com.xingwang.classroom.bean.DetailBean;
import com.xingwang.classroom.bean.FavoritBean;
import com.xingwang.classroom.bean.IsFavoritBean;
import com.xingwang.classroom.bean.SendCommentBean;
import com.xingwang.classroom.dialog.BottomADSheetDialog;


import com.xingwang.classroom.view.CustomBarrageLayout;
import com.xinwang.bgqbaselib.dialog.CenterDefineDialog;
import com.xinwang.bgqbaselib.view.CustomProgressBar;
import com.xingwang.classroom.view.LandLayoutVideo;
import com.xingwang.classroom.ws.ChannelStatusListener;
import com.xingwang.classroom.ws.WsManagerUtil;
import com.xinwang.bgqbaselib.base.BaseNetActivity;
import com.xinwang.bgqbaselib.http.ApiParams;
import com.xinwang.bgqbaselib.http.HttpCallBack;
import com.xinwang.bgqbaselib.http.HttpUrls;
import com.xinwang.bgqbaselib.utils.AndroidBug5497Workaround;
import com.xinwang.bgqbaselib.utils.CommentUtils;
import com.xinwang.bgqbaselib.utils.Constants;
import com.xinwang.bgqbaselib.utils.GsonUtils;
import com.xinwang.bgqbaselib.utils.HttpUtil;
import com.xinwang.bgqbaselib.utils.KeyBoardHelper;
import com.xinwang.bgqbaselib.utils.MyToast;
import com.xinwang.bgqbaselib.utils.NoDoubleClickUtils;
import com.xinwang.bgqbaselib.utils.SharedPreferenceUntils;
import com.xinwang.bgqbaselib.utils.StatusBarUtils;
import com.ycbjie.webviewlib.InterWebListener;
import com.ycbjie.webviewlib.VideoWebListener;
import com.ycbjie.webviewlib.WebProgress;
import com.ycbjie.webviewlib.X5WebChromeClient;
import com.ycbjie.webviewlib.X5WebUtils;
import com.ycbjie.webviewlib.X5WebView;
import com.ycbjie.webviewlib.X5WebViewClient;


import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.util.ArrayList;
import java.util.List;





/**
 * Date:2019/8/22
 * Time;9:09
 * author:baiguiqiang
 */
public class ClassRoomDetailActivity extends BaseNetActivity implements KeyBoardHelper.OnKeyBoardStatusChangeListener {
    private LandLayoutVideo mVideoPlayer;
    public OrientationUtils orientationUtils;
    private TextView btSend;
    private EditText etContent;
    private ImageView ivPic,ivComment,ivProduct;
    private RelativeLayout rlVideoRoot;
    private DetailBarrageAdapter mBarrageAdapter;
    private Fragment[] mFragments = new Fragment[]{new ClassRoomDetailFragment(),new ClassRoomCommentFragment()};
    private int curPos =0;
    private boolean isPlay=false;
    private boolean isPause;
    private CustomBarrageLayout cblBarrage;
    private int pageNum=10;
    private KeyBoardHelper mKeyBoardHelper;
    List<CommentBean.DataBean.CommentsBean> mComments =new ArrayList<>();
    int state =1;
    public int mId;
    private int clickChild=-1;
    private ImagePickerDefine imagePickerDefine;

    private List<ADBean> mAdbeans;
    private DetailBean mBean;
    public boolean isCollect = false;
    private boolean isClickPic = false;
    public View viewDot;
    private String channel;
    private X5WebView webView;
    private boolean isLoadVideo =true;//是不是加载视频 ，不是就是网页直播
    private FrameLayout flameLayout;
    private ImageView icCollectTpLink,icShapeTalink,backTpLink;
    private boolean isScreen = false;//竖屏 全屏
    private int mVideoHeight ;//默认720*1280分辨率
    //  private boolean isInitViewHeight = false;//videoView高度通过计算视频源宽高比计算得来
    public static Intent getIntent(Context context, int id) {
        Intent intent = new Intent(context, ClassRoomDetailActivity.class);
        intent.putExtra("id", id);
        return intent;
    }
    public static Intent getIntent(Context context, int id,int resultCode) {
        return getIntent(context,id).putExtra("resultCode",resultCode);
    }
    public static Intent getIntent(Context context, int id,int resultCode, String resultParameter) {
        return getIntent(context,id).putExtra("resultCode",resultCode).putExtra("resultParameter",resultParameter);
    }

    @Override
    protected int layoutResId() {
        return R.layout.activity_class_room_detail_classroom;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

       // if (!isStartLaunch()) {
            AndroidBug5497Workaround.assistActivity(this);

            mKeyBoardHelper = new KeyBoardHelper(this);
            mKeyBoardHelper.onCreate();
            mVideoPlayer = findViewById(R.id.video_track);
            webView = findViewById(R.id.webview);
            flameLayout = findViewById(R.id.flameLayout);
            btSend = findViewById(R.id.bt_send);
            cblBarrage = findViewById(R.id.cbl_barrage);
            etContent = findViewById(R.id.et_content);
            // ivThumb =  findViewById(R.id.ivThumb);
            rlVideoRoot = findViewById(R.id.rlVideoRoot);

            ivPic = findViewById(R.id.iv_pic);
            ivProduct = findViewById(R.id.iv_product);
            ivComment = findViewById(R.id.iv_comment);
            viewDot = findViewById(R.id.view_dot);
            icCollectTpLink = findViewById(R.id.iv_collect_tplink);
            icShapeTalink = findViewById(R.id.iv_shape_tplink);
            backTpLink = findViewById(R.id.back_tplink);

            initId();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ((LinearLayout.LayoutParams) rlVideoRoot.getLayoutParams()).setMargins(0, StatusBarUtils.getStatusHeight(this), 0, 0);
                ((LinearLayout.LayoutParams) flameLayout.getLayoutParams()).setMargins(0, StatusBarUtils.getStatusHeight(this), 0, 0);
            }
            mVideoHeight = CommentUtils.getScreenWidth(this) * 720 / 1280;
            mVideoPlayer.getLayoutParams().height = mVideoHeight;
            rlVideoRoot.setMinimumHeight(mVideoHeight);
            setCurFragment(0, false, true);

            ivPic.setOnClickListener(v -> {
                isClickPic = true;
                requestPermission();
            });
            ivProduct.setOnClickListener(v -> {
                if (mAdbeans != null && mAdbeans.size() > 0) {
                    ClassRoomLibUtils.startWebActivity(ClassRoomDetailActivity.this,
                            mAdbeans.get(0).getBody().getLink()
                            , true, "产品详情");
                }
            });
            cblBarrage.setOnClickListener(v -> {
                if (!NoDoubleClickUtils.isDoubleClick()) {//不能连续点击后
                    setCurFragment(1, false, false);
                }
            });
            btSend.setOnClickListener(v -> goSendComment());
            etContent.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (TextUtils.isEmpty(etContent.getText().toString().trim())) {
                        btSend.setVisibility(View.GONE);
                        ivComment.setVisibility(View.VISIBLE);
                        ivPic.setVisibility(View.VISIBLE);
                        if (mAdbeans != null && mAdbeans.size() > 0) {
                            ivProduct.setVisibility(View.VISIBLE);
                        }

                    } else {
                        btSend.setVisibility(View.VISIBLE);
                        ivComment.setVisibility(View.GONE);
                        ivPic.setVisibility(View.GONE);
                        ivProduct.setVisibility(View.GONE);
                    }
                }
            });

            initRequestData();
            mKeyBoardHelper.setOnKeyBoardStatusChangeListener(this);
   //     }
    }

    /**
     * 获取上个页面传过来的id
     */
    private void initId() {
        Intent intent =getIntent();
        mId =intent.getIntExtra("id",0);
        Uri uri = intent.getData();
        if (uri == null) {
            return;
        }
        String actionData = uri.getQueryParameter("id");
        if (actionData!=null)
            mId =Integer.parseInt(actionData);

    }


    private void  initVideoPlay(String url,String thumb){
        // url ="rtmp://live.xw518.com/test/1";
        flameLayout.setVisibility(View.GONE);

        if (orientationUtils==null) {
            //外部辅助的旋转，帮助全屏
            orientationUtils = new OrientationUtils(this, mVideoPlayer);
            //初始化不打开外部的旋转
            orientationUtils.setEnable(false);
            mVideoPlayer.getBackButton().setOnClickListener(v -> onBackPressed());
            GSYVideoOptionBuilder gsyVideoOption = new GSYVideoOptionBuilder();
            String mPlayPosition = SharedPreferenceUntils.getString(this, "playposition" + mId, "0");
            if (TextUtils.isEmpty(url)){
                mVideoPlayer.showThumbBg(thumb);
            }
            mVideoPlayer.setVisibility(View.VISIBLE);
            gsyVideoOption.setIsTouchWiget(true)
                    .setIsTouchWigetFull(true)
                    .setRotateViewAuto(false)
                    .setLockLand(false)
                    .setAutoFullWithSize(false)
                    .setShowFullAnimation(false)
                    .setNeedLockFull(true)
                    .setUrl(url)
                    .setNeedShowWifiTip(true)
                    .setDismissControlTime(5000)
                    .setSeekOnStart(Long.parseLong(mPlayPosition))
                    .setCacheWithPlay(true)
                    .setCachePath(ClassRoomLibUtils.getVideoCachePathFile(this))
                    .setLooping(true)
                    .setGSYVideoProgressListener((progress, secProgress, currentPosition, duration) -> {
                       /* if (mVideoPlayer.isInPlayingState()&&(mVideoPlayer.getBgThumb().getVisibility()==View.VISIBLE)){

                        }*/
                       /* if (mVideoPlayer.isGetVideoSize()&&!isInitViewHeight&&!mVideoPlayer.isVerticalVideo()){
                            isInitViewHeight = true;
                           mVideoPlayer.getLayoutParams().height = CommentUtils.getScreenWidth(this)*mVideoPlayer.getCurrentVideoHeight()/mVideoPlayer.getCurrentVideoWidth();
                        }*/
                    })
                    .setVideoAllCallBack(new GSYSampleCallBack() {
                        @Override
                        public void onPrepared(String url, Object... objects) {
                            super.onPrepared(url, objects);
                            //开始播放了才能旋转和全屏
                            orientationUtils.setEnable(true);
                            isPlay = true;
                            // mVideoPlayer.getBgThumb().setVisibility(View.GONE);
                        }

                        @Override
                        public void onQuitFullscreen(String url, Object... objects) {
                            super.onQuitFullscreen(url, objects);
                            if (orientationUtils != null) {
                                orientationUtils.backToProtVideo();
                            }
                        }
                    }).setLockClickListener((view, lock) -> {
                if (orientationUtils != null) {
                    //配合下方的onConfigurationChanged
                    orientationUtils.setEnable(!lock);
                }
            }).build(mVideoPlayer);

            mVideoPlayer.getFullscreenButton().setOnClickListener(v -> {
                if (!mVideoPlayer.isGetVideoSize())//获取到 视频宽高才能全屏
                    return;
                if (mVideoPlayer.isVerticalVideo()) {
                    if (isScreen) {
                        mVideoPlayer.getLayoutParams().height = mVideoHeight;
                    }else {
                        mVideoPlayer.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
                    }
                    isScreen =!isScreen;
                }
                else {
                    try {

                        // 可能出现java.lang.IllegalStateException at android.media.MediaPlayer.getVideoHeight(Native Method)
                        //直接横屏
                        orientationUtils.resolveByClick();
                        //第一个true是否需要隐藏actionbar，第二个true是否需要隐藏statusbar
                        mVideoPlayer.startWindowFullscreen(ClassRoomDetailActivity.this, true, true);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            mVideoPlayer.startPlayLogic();

        }
    }




    private void showBottomDialog(ADBean adBean) {
        int mWidth = CommentUtils.getScreenWidth(this);
        int mHeight = CommentUtils.getScrrenHeight(this);
        int mDialogHeight = mWidth/4;
        int mDialogWidth = mWidth/3;
        float verticalMargin;
        if (mHeight>mWidth){//横屏
            verticalMargin =0.5f;
        }else {
            verticalMargin =0;
        }

        BottomADSheetDialog mDialog = BottomADSheetDialog.getInstance(adBean.getBody().getImg_src(),mDialogHeight,mDialogWidth,verticalMargin);
        mDialog.setCallback(integer -> BeautyDefine.getBrowserDefine(ClassRoomDetailActivity.this).openBrowser(adBean.getBody().getLink()));
        if (getSupportFragmentManager()!=null)
            mDialog.showDialog(getSupportFragmentManager());
    }

    public void goCollect(View view){

        requestPost(isCollect? HttpUrls.URL_UNFAVORITE():HttpUrls.URL_FAVORITE(),new ApiParams().with("relation_id",mId+"").with("type","lecture"),
                FavoritBean.class,new HttpCallBack<FavoritBean>() {
                    @Override
                    public void onFailure(String message) {
                        MyToast.myToast(getApplicationContext(),message);
                    }
                    @Override
                    public void onSuccess(FavoritBean mBean) {
                        MyToast.myToast(getApplicationContext(),mBean.getMessage());
                        isCollect = !isCollect;
                       // CourseFavoriteLiveData.getInstance().notifyInfoChanged(new CourseFavoriteInfo(mId,isCollect,-1));
                        view.setSelected(isCollect);
                    }
                });

    }
    public void goShape(View view){

        if (mBean!=null) {
            ArrayList<String> mPics = new ArrayList<>();
            mPics.add(mBean.getData().getLecture().getThumb());
            String regMatchTag = "<[^>]*>";

            BeautyDefine.getShareDefine(this).share("lecture/detail",CommentUtils.urlDecode(new String[]{"id"},new String[]{mBean.getData().getLecture().getId()+""}),
                    "",HttpUrls.URL_SHARE()+"?"+CommentUtils.urlDecode(new String[]{"id"},new String[]{mBean.getData().getLecture().getId()+""}),mPics,mBean.getData().getLecture().getTitle(),
                    mBean.getData().getLecture().getBody().replaceAll(regMatchTag,""),new ShareResultCallBack(){

                        @Override
                        public void onSucceed(){

                        }
                        @Override
                        public void onFailure(String s) {
                            MyToast.myToast(getApplicationContext(),s);
                        }
                    });
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
        ActivityCompat.requestPermissions(this, permissions, requestCode);
    }

    /**
     * 检查是否已被授权危险权限
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
    private void jumpPic(){
        imagePickerDefine = BeautyDefine.getImagePickerDefine(ClassRoomDetailActivity.this);

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
        BeautyDefine.getOpenPageDefine(this).progressControl(new OpenPageDefine.ProgressController.Showder("上传中",false));
        BeautyDefine.getUploadDefine().upload(new File[]{new File(s)},new UploadResultCallBack(){

            @Override
            public void onSucceed(String[] strings) {
                ivPic.setTag(strings[0]);
                goSendComment();
            }

            @Override
            public void onFailure() {
                choosePicCommentError();
                MyToast.myToast(getApplicationContext(),"上传失败");
                BeautyDefine.getOpenPageDefine(ClassRoomDetailActivity.this).progressControl(new OpenPageDefine.ProgressController.Hider());
            }
        });
    }

    /**
     * 发送评论
     */
    private void goSendComment() {
        btSend.setEnabled(false);
        String mContent = etContent.getText().toString();
        ApiParams mApiParams = new ApiParams();
        Object mPos = etContent.getTag();
        if (TextUtils.isEmpty(mContent)) {//发送图片
            mApiParams.with("pics",ivPic.getTag());
            if (mPos!=null){//对父级评论
                if (clickChild==-1)
                    mApiParams.with("pid",mComments.get((Integer) mPos).getId()+"");
                else
                    mApiParams.with("pid",mComments.get((Integer) mPos).getSub_comments().get(clickChild).getId()+"");
            }else {
                mApiParams.with("lecture_id", mId + "");
            }
        }else {
            BeautyDefine.getOpenPageDefine(this).progressControl(new OpenPageDefine.ProgressController.Showder("发送中",false));
            mApiParams.with("body",mContent);
            if (mPos!=null){//对父级评论
                if (clickChild==-1)
                    mApiParams.with("pid",mComments.get((Integer) mPos).getId()+"");
                else
                    mApiParams.with("pid",mComments.get((Integer) mPos).getSub_comments().get(clickChild).getId()+"");
            }else {
                mApiParams.with("lecture_id", mId + "");
            }
        }
        HttpUtil.cancelTag(this);
        requestPost(HttpUrls.URL_PUBLISH(),mApiParams, SendCommentBean.class,new HttpCallBack<SendCommentBean>() {

            @Override
            public void onFailure(String message) {
                btSend.setEnabled(true);
                MyToast.myToast(getApplicationContext(),message);
                BeautyDefine.getOpenPageDefine(ClassRoomDetailActivity.this).progressControl(new OpenPageDefine.ProgressController.Hider());
                choosePicCommentError();
            }

            @Override
            public void onSuccess(SendCommentBean commentBean) {
                if (commentBean.getData().getState()==1) {
                   // MyToast.myToast(getApplicationContext(), " 发送成功");//直接长连接里面返回数据
                }else {
                    MyToast.myToast(getApplicationContext(), "正在审核");
                }
                isClickPic = false;
                etContent.setTag(null);
                BeautyDefine.getOpenPageDefine(ClassRoomDetailActivity.this).progressControl(new OpenPageDefine.ProgressController.Hider());
                btSend.setEnabled(true);
                etContent.setHint("请输入你想说的话");
                etContent.setText("");
                clickChild = -1;
                mKeyBoardHelper.closeInputMethodManager();
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
            clickChild = -1;
            etContent.setHint("请输入你想说的话");
        }
    }
    private void initRequestData() {
        requestIsCollect();
    }

    private void requestIsCollect() {
        requestGet(HttpUrls.URL_ISFAVORITE(),new ApiParams().with("relation_id",mId).with("type","lecture"),
                IsFavoritBean.class,new HttpCallBack<IsFavoritBean>() {
                    @Override
                    public void onFailure(String message) {
                        requestFailureShow(message);
                    }
                    @Override
                    public void onSuccess(IsFavoritBean mBean) {
                        isCollect = mBean.getData()==1;
                        if (mVideoPlayer!=null&&mVideoPlayer.getCollectView()!=null) {
                            mVideoPlayer.getCollectView().setVisibility(View.VISIBLE);
                            mVideoPlayer.getCollectView().setSelected(isCollect);
                        }
                        icCollectTpLink.setVisibility(View.VISIBLE);
                        icCollectTpLink.setSelected(isCollect);
                        requestDetailData(Constants.LOAD_DATA_TYPE_INIT);
                    }
                });

    }

    private String mTitle;
    private void requestDetailData(int requestType) {
        requestGet(HttpUrls.URL_DETAIL(),new ApiParams().with("id",mId),
                DetailBean.class,new HttpCallBack<DetailBean>() {
                    @Override
                    public void onFailure(String message) {
                        requestFailureShow(message);
                    }
                    @Override
                    public void onSuccess(DetailBean mBean) {
                        mTitle=  mBean.getData().getLecture().getTitle();
                        if (mBean.getData().getLecture().getType().equals("video")) {//视频
                            initVideoPlay(mBean.getData().getLecture().getContent(),mBean.getData().getLecture().getThumb());
                        }else {//加载网页
                            isLoadVideo= false;
                            initWebView(mBean.getData().getLecture().getContent());
                        }
                        ivComment.setOnClickListener(v -> {
                            if (!NoDoubleClickUtils.isDoubleClick()) {//不能连续点击后
                                if (curPos == 0) {
                                    setCurFragment(1, false, false);
                                } else{
                                    setCurFragment(0, true, false);
                                }
                            }
                        });

                        etContent.setFocusable(true);
                        etContent.setFocusableInTouchMode(true);
                        ((ClassRoomDetailFragment)mFragments[0]).upDateShow(mBean);
                        mAdbeans = GsonUtils.changeGsonToSafeList(mBean.getData().getLecture().getAd(),ADBean.class);
                        if (mAdbeans.size() > 0){
                            ivProduct.setVisibility(View.VISIBLE);
                        }
                        ClassRoomDetailActivity.this.mBean = mBean;
                        requestSubscribe();//长连接监听评论
                        requestCommentListData(requestType,Integer.MAX_VALUE);//请求评论信息
                    }
                });
    }
    private X5WebChromeClient x5WebChromeClient;
    private X5WebViewClient x5WebViewClient;
    private WebProgress progress;
    private void initWebView(String content) {
        // QbSdk.setDownloadWithoutWifi(true);
        showWebView(content);
    }
    int mScreenWidth = 1280;
    private void showWebView(String content){
        mScreenWidth = CommentUtils.getScreenWidth(this);
        icCollectTpLink.setOnClickListener(this::goCollect);
        icShapeTalink.setOnClickListener(this::goShape);
        backTpLink.setOnClickListener(v -> finish());
        flameLayout.setVisibility(View.VISIBLE);
        //mVideoPlayer.setVisibility(View.GONE);
        rlVideoRoot.setVisibility(View.GONE);
        setWebViewHeight(false);
        progress = findViewById(R.id.progress1);
        progress.show();
        progress.setColor(this.getResources().getColor(R.color.colorAccentClassRoom));

        webView.loadUrl(content);
        if ( webView.getX5WebViewExtension()!=null){
            Bundle data = new Bundle();
            data.putBoolean("standardFullScreen", true);// true表示标准全屏，false表示X5全屏；不设置默认false，
            data.putBoolean("supportLiteWnd", false);// false：关闭小窗；true：开启小窗；不设置默认true，
            data.putInt("DefaultVideoScreen", 1);// 1：以页面内开始播放，2：以全屏开始播放；不设置默认：1
            webView.getX5WebViewExtension().invokeMiscMethod("setVideoParams", data);
            SharedPreferenceUntils.saveX5State(this,true);
        }else {
            //  SharedPreferenceUntils.saveX5State(this,false);
            CenterDefineDialog mDialog = CenterDefineDialog.getInstance("x5内核安装失败，播放异常,关闭应用重试");
            mDialog .setCallback(integer -> {
                try {
                    android.os.Process.killProcess(android.os.Process.myPid());
                }catch (Exception e){
                    try {
                        System.exit(0);
                    }catch (Exception e1){
                        e1.printStackTrace();
                    }
                }
            });
            mDialog.showDialog(getSupportFragmentManager());
        }

        x5WebChromeClient = webView.getX5WebChromeClient();
        x5WebViewClient = webView.getX5WebViewClient();


        x5WebChromeClient.setWebListener(interWebListener);
        x5WebViewClient.setWebListener(interWebListener);
        //设置是否自定义视频视图
        webView.setShowCustomVideo(false);
        x5WebChromeClient.setVideoWebListener(new VideoWebListener() {

            @Override
            public void showVideoFullView() {
                //视频全频播放时监听

            }

            @Override
            public void hindVideoFullView() {
                //隐藏全频播放，也就是正常播放视频
                setWebViewHeight(true);
            }

            @Override
            public void showWebView() {
                //显示webView
            }

            @Override
            public void hindWebView() {
                //隐藏webView
            }
        });
    }
    private void setWebViewHeight(boolean isFullViewToNormal){

        flameLayout.getLayoutParams().height = mScreenWidth;
        webView.getLayoutParams().height =flameLayout.getLayoutParams().height;
        //视频与下部分有黑边
        int topHeight;
        if (isFullViewToNormal){
            topHeight =210;
        }else {
            topHeight =200;
        }
        ((RelativeLayout.LayoutParams) findViewById(R.id.rl_head).getLayoutParams()).topMargin =-topHeight*mScreenWidth/1080;
    }
    private InterWebListener interWebListener = new InterWebListener() {
        @Override
        public void hindProgressBar() {
            progress.hide();
        }

        @Override
        public void showErrorView(@X5WebUtils.ErrorType int type) {
            switch (type){
                //没有网络
                case X5WebUtils.ErrorMode.NO_NET:
                    break;
                //404，网页无法打开
                case X5WebUtils.ErrorMode.STATE_404:

                    break;
                //onReceivedError，请求网络出现error
                case X5WebUtils.ErrorMode.RECEIVED_ERROR:

                    break;
                //在加载资源时通知主机应用程序发生SSL错误
                case X5WebUtils.ErrorMode.SSL_ERROR:

                    break;
                default:
                    break;
            }
        }

        @Override
        public void startProgress(int newProgress) {
            progress.setWebProgress(newProgress);
            if (newProgress>=70){
                webView.loadUrl("javascript:$('#header').remove();$('#videoSign').remove();$('#footer').remove();$('title').text('"+mTitle+"');" );
            }
        }

        @Override
        public void showTitle(String title) {
        }
    };

    public void handleFinish() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAfterTransition();
        } else {
            finish();
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (!isLoadVideo) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                //全屏播放退出全屏
                if (x5WebChromeClient != null && x5WebChromeClient.inCustomView()) {
                    x5WebChromeClient.hideCustomView();
                    return true;
                    //返回网页上一页
                } else if (webView.canGoBack()) {
                    webView.goBack();
                    return true;
                    //退出网页
                } else {
                    handleFinish();
                }
            }
        }
        return super.onKeyDown(keyCode, event);
    }



    void requestSubscribe(){
        WsManagerUtil.getInstance().onCreate(this, new ChannelStatusListener() {
            @Override
            public void createSuccess(String response) {
                super.createSuccess(response);
                channel= HttpUrls.CHANNEL+mBean.getData().getLecture().getId()+".new_comment";
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
                            MyToast.myToast(getApplicationContext(),message);
                        }
                    }
                });
            }
        });
    }

    private void handlerMessage(String message) {

        try {
            JSONObject jsonObject =new JSONObject(message);
            int curPosition = -1;
            if (channel!=null&&channel.equals(jsonObject.getString("channel"))){
                CommentBean.DataBean.CommentsBean mCommentBean = GsonUtils.changeGsonToBean(jsonObject.getString("content"), CommentBean.DataBean.CommentsBean.class);
                if (mCommentBean.getPid()==0){//评论父级 这里之前是bid==0 修改为pid 2020.8.5
                    mComments.add(0,mCommentBean);
                    Object tag = etContent.getTag();//当点击某个item，发  送消息时候，突然来消息
                    if (tag!=null){
                        etContent.setTag((int)tag+1);
                    }
                    curPosition =0;
                }else{
                    for (int i=0;i<mComments.size();i++){
                        if (mComments.get(i).getId()==mCommentBean.getBid()){
                            mComments.get(i).getSub_comments().add(mCommentBean);
                            if (clickChild!=-1)//选择子评论
                                clickChild =clickChild+1;
                            curPosition =i;
                            break;
                        }
                    }
                    if (curPosition==-1){
                        mComments.add(0,mCommentBean);
                    }
                }

                int finalCurPosition = curPosition;
                runOnUiThread(() -> {
                    if (mBarrageAdapter!=null)
                        mBarrageAdapter.notifyItemInserted(cblBarrage, mCommentBean);
                    if (curPos==0){
                        fragmentNotifyDataSetChanged();
                    }else{
                        if (mFragments!=null&&mFragments[1].isAdded()){
                            ClassRoomCommentFragment mFragment = (ClassRoomCommentFragment) mFragments[1];
                            mFragment.swipeRefreshUi(finalCurPosition);
                        }
                    }
                });

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    boolean isRequesting =false;
    void requestCommentListData(int requestType,int lastId) {
        isRequesting = true;
        requestGet(HttpUrls.URL_COMMENTLIST(),new ApiParams().with("lecture_id",mId)
                .with("num", pageNum).with("backward_div_id",lastId), CommentBean.class,new HttpCallBack<CommentBean>() {

            @Override
            public void onFailure(String message) {
                if (requestType==Constants.LOAD_DATA_TYPE_INIT){
                    requestFailureShow(message);
                }
                isRequesting = false;
            }

            @Override
            public void onSuccess(CommentBean commentBean) {

                if(commentBean.getData().getComments().size()<pageNum){
                    state=3;
                }else
                    state=1;
                if (requestType==Constants.LOAD_DATA_TYPE_INIT){
                    showBarrage(commentBean);
                    findViewById(R.id.rl_empty).setVisibility(View.GONE);
                    mComments = commentBean.getData().getComments();
                }else {
                    if (requestType==Constants.LOAD_DATA_TYPE_REFRESH)
                        mComments.clear();
                    mComments.addAll(commentBean.getData().getComments());
                }
                fragmentNotifyDataSetChanged();
                isRequesting = false;
            }
        });
    }
    private void fragmentNotifyDataSetChanged(){
        if (mFragments[1].isAdded()){
            ClassRoomCommentFragment mFragment = (ClassRoomCommentFragment) mFragments[1];
            mFragment.initAdapter();
        }
    }

    private void showBarrage(CommentBean commentBean) {
        if (mBarrageAdapter==null) {
            mBarrageAdapter = new DetailBarrageAdapter(commentBean.getData().getComments());
            cblBarrage.setAdapter(mBarrageAdapter);
        }
    }

    @Override
    public void onBackPressed() {
        if (curPos==1){
            setCurFragment(0, false, false);
            return;
        }
        if (isLoadVideo&&mVideoPlayer!=null){
            if ( mVideoPlayer.isVerticalVideo()&&isScreen) {
                mVideoPlayer.getLayoutParams().height = mVideoHeight;
                // setNavigationBarColor(android.R.color.white);
                isScreen =!isScreen;
                return;
            }
            if (orientationUtils != null) {
                orientationUtils.backToProtVideo();
            }

            if (GSYVideoManager.backFromWindowFull(this)) {
                return;
            }


        }
        super.onBackPressed();
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
    private boolean isPlaying = false;
    @Override
    protected void onPause() {
        if (isLoadVideo&&mVideoPlayer!=null) {
            isPlaying = mVideoPlayer.getGSYVideoManager().isPlaying();
            mVideoPlayer.getCurrentPlayer().onVideoPause();
            if (mVideoPlayer != null) {
                SharedPreferenceUntils.putString(this, "playposition" + mId, mVideoPlayer.getCurrentPositionWhenPlaying() + "");
            }
        }
        super.onPause();

        isPause = true;
    }

    @Override
    protected void onResume() {
        if (isLoadVideo&&isPlaying&&mVideoPlayer!=null)
            mVideoPlayer.getCurrentPlayer().onVideoResume(false);
        super.onResume();
        isPause = false;
    }

    @Override
    protected void onDestroy() {
        WsManagerUtil.getInstance().onDestroy(null);
        super.onDestroy();
        if (mVideoPlayer!=null) {
            mVideoPlayer.getCurrentPlayer().release();
        }
        if (orientationUtils != null)
            orientationUtils.releaseListener();
        if (mKeyBoardHelper != null) {
            mKeyBoardHelper.onDestroy();
        }
        webViewDestroy();
        if (cblBarrage!=null)
        cblBarrage.onDestroy();
    }
    private  void  webViewDestroy(){
        if (x5WebChromeClient!=null)
            //有音频播放的web页面的销毁逻辑
            //在关闭了Activity时，如果Webview的音乐或视频，还在播放。就必须销毁Webview
            //但是注意：webview调用destory时,webview仍绑定在Activity上
            //这是由于自定义webview构建时传入了该Activity的context对象
            //因此需要先从父容器中移除webview,然后再销毁webview:
            if (webView != null) {
                ViewGroup parent = (ViewGroup) webView.getParent();
                if (parent != null) {
                    parent.removeView(webView);
                }
                webView.removeAllViews();
                webView.destroy();
                webView = null;
            }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (imagePickerDefine != null) {
            imagePickerDefine.onActivityResultHanlder(requestCode, resultCode, data);
        }

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (isLoadVideo) {
            //如果旋转了就全屏
            if (isPlay && !isPause) {
                mVideoPlayer.onConfigurationChanged(this, newConfig, orientationUtils, true, true);
            }
        }
    }

    public void setCurFragment(int position,boolean isBack,boolean isFirst){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (!isFirst) {
            if (position==0){
                viewDot.setVisibility(View.GONE);
                cblBarrage.setVisibility(View.VISIBLE);
            }else {
                cblBarrage.setVisibility(View.GONE);
            }
            if (isBack)
                transaction.setCustomAnimations( R.anim.slide_left_in_classroom,R.anim.slide_right_out_classroom);
            else
                transaction.setCustomAnimations(R.anim.slide_right_in_classroom, R.anim.slide_left_out_classroom);
        }else {
            transaction.setCustomAnimations( R.anim.slide_left_in_classroom,R.anim.slide_left_in_classroom);//第一次不要动画
        }
        transaction.replace(R.id.frame,mFragments[position]);
        transaction.commitAllowingStateLoss();
        curPos = position;

    }

    public void showCommentPid(int position,int childPos) {
        etContent.setText("");
        if (childPos!=-1)
            etContent.setHint("回复:"+mComments.get(position).getSub_comments().get(childPos).getUser().getshowName());
        else {
            etContent.setHint("回复:"+mComments.get(position).getUser().getshowName());
        }
        etContent.setTag(position);
        clickChild = childPos;
        etContent.requestFocus();
        mKeyBoardHelper.openInputMethodManager(etContent);
    }

    @Override
    public void OnKeyBoardChanged(boolean visible, int keyBoardHeight) {
        if (!visible){
            if (etContent.getTag()!=null&&!isClickPic){//点击对某人评论在隐藏软键盘
                etContent.setTag(null);
                if (btSend.isEnabled())
                    etContent.setText("");
                etContent.setHint("请输入你想说的话");
            }
        }
    }
}
