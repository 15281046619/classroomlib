package com.xingwang.classroom.ui.live;



import android.annotation.TargetApi;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.transition.Transition;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beautydefinelibrary.BeautyDefine;
import com.beautydefinelibrary.ShareResultCallBack;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder;
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack;
import com.shuyu.gsyvideoplayer.utils.Debuger;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;
import com.xingwang.classroom.ClassRoomLibUtils;
import com.xingwang.classroom.R;
import com.xingwang.classroom.adapter.HomeViewpagerAdapter;

import com.xingwang.classroom.bean.LiveDetailBean;
import com.xingwang.classroom.bean.LiveIsSubscribeBean;
import com.xingwang.classroom.bean.OnlineCountBean;
import com.xingwang.classroom.bean.PlayInfoBean;
import com.xingwang.classroom.bean.VodListBean;
import com.xingwang.classroom.dialog.CenterDefineDialog;
import com.xinwang.bgqbaselib.http.ApiParams;
import com.xinwang.bgqbaselib.http.HttpCallBack;
import com.xinwang.bgqbaselib.http.HttpUrls;
import com.xingwang.classroom.listener.OnTransitionListener;
import com.xinwang.bgqbaselib.utils.AndroidBug5497Workaround;
import com.xinwang.bgqbaselib.utils.CommentUtils;
import com.xinwang.bgqbaselib.utils.Constants;
import com.xinwang.bgqbaselib.utils.KeyBoardHelper;
import com.xinwang.bgqbaselib.utils.MyToast;
import com.xinwang.bgqbaselib.utils.StatusBarUtils;
import com.xinwang.bgqbaselib.view.CustomProgressBar;
import com.xingwang.classroom.view.EmptyControlVideo;
import com.xinwang.bgqbaselib.base.BaseNetActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static com.xinwang.bgqbaselib.http.HttpUrls.URL_ZHI_BO;


/**
 * Date:2020/6/2
 * Time;10:54
 * author:baiguiqiang
 */
public class LiveDetailActivity extends BaseNetActivity {
    private EmptyControlVideo mVideoPlayer;
    private TextView tvTime;
    public OrientationUtils orientationUtils;
    private boolean isTransition =true;
    private Transition transition;
    public boolean isLive ;
    private int mVideoHeight ;//默认720*1280分辨率
    private  TabLayout tabLayout;
    private  ViewPager viewPager;
    private String mId;//直播id
    public LiveDetailBean mLiveDetailBean;//直播详情接口获取数据

    private Handler mHandler = new Handler();
    private long mTime = 0;
    public boolean isPlay =false;
    private boolean isPause;
    public long onlineCount=-1;//在线人数
    public boolean liveEnd = false;//收到直播结束标准
    private Runnable mDownRunnable=new Runnable() {
        @Override
        public void run() {
            mTime -= 1;
            if (mTime>0){
                tvTime.setText(sToHMS(mTime));
                mHandler.postDelayed(this,1000);
            }else{
                setResult(100);//直播正在开始回调给列表
                mHandler.removeCallbacks(this);
                tvTime.setText("直播即将开始");
                mVideoPlayer.startPlayLogic();
              /*  if (isLive) {
                    mVideoPlayer.goneThumbBg();
                    tvTime.setVisibility(View.GONE);
                }*/
            }
        }
    };
    //每隔10s刷新一次在线人数查询
    private Runnable mOnlineCountRunnable= () -> getOnlineCount();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        isLive =getIntent().getBooleanExtra(Constants.EXTRA_IS_LIVE,true);
        Uri uri = getIntent().getData();
        if (uri != null) {
            String is_end = uri.getQueryParameter("is_end");
            if (!TextUtils.isEmpty(is_end)){
                isLive ="1".equals(is_end);
            }
        }
        super.onCreate(savedInstanceState);
       // if (!isStartLaunch()) {
            setNavigationBarColor(android.R.color.black);
            //mId = getIntent().getStringExtra("id");
            initId();
            initView();
            initData();
        //}
    }
    /**
     * 获取上个页面传过来的id
     */
    private void initId() {
        Intent intent =getIntent();
        mId =intent.getStringExtra("id");
        Uri uri = intent.getData();
        if (uri == null) {
            return;
        }
        mId = uri.getQueryParameter("id");
    }

    private void initData() {
        mVideoPlayer.getBackButton().setOnClickListener(v -> onBackPressed());
        getLiveDetail();
    }
    private void getLiveDetail(){
        requestGet(HttpUrls.URL_LIVE_DETAIL(),new ApiParams().with("id",mId),
                LiveDetailBean.class, new HttpCallBack<LiveDetailBean>() {

                    @Override
                    public void onFailure(String message) {
                        requestFailureShow(message);
                    }

                    @Override
                    public void onSuccess(LiveDetailBean liveDetailBean) {
                        mLiveDetailBean =liveDetailBean;

                        if (liveDetailBean.getData().getLive().getIs_end()==2&&isLive){//参数是直播但是获取到数据直播已经结束了 只能重新
                            liveEnd();
                           // return;
                        }
                        getIsSubscribe();

                        onlineCount =liveDetailBean.getData().getLive().getClick();
                        ((EmptyControlVideo)mVideoPlayer.getCurrentPlayer()).showOnlineCount(onlineCount);
                    }
                });
    }
    public void liveEnd(){
        setResult(101);
        if (mLiveDetailBean.getData().getLive().getCover()!=null)
            mVideoPlayer.showThumbBg(mLiveDetailBean.getData().getLive().getCover());
        tvTime.setVisibility(View.VISIBLE);
        tvTime.setText("直播已经结束");
        CenterDefineDialog.getInstance("直播已经结束,是否查看回放视频").setCallback(integer -> {
            ClassRoomLibUtils.startLiveDetailActivity(LiveDetailActivity.this,mId,false);
            finish();
        }).showDialog(getSupportFragmentManager());
    }
    /**
     * 获取是否关注
     */
    private void getIsSubscribe(){
        requestGet(HttpUrls.URL_LIVE_IS_SUBSCRIBE(),new ApiParams().with("id",mId),
                LiveIsSubscribeBean.class, new HttpCallBack<LiveIsSubscribeBean>() {

                    @Override
                    public void onFailure(String message) {
                        requestFailureShow(message);
                    }

                    @Override
                    public void onSuccess(LiveIsSubscribeBean mLiveIsSubScribeBean) {
                        if (isLive){
                            findViewById(R.id.rl_empty).setVisibility(View.GONE);
                            showUi(mLiveIsSubScribeBean,URL_ZHI_BO+mLiveDetailBean.getData().getLive().getAlias()+".m3u8");
                        }else {
                            getVodList(mLiveIsSubScribeBean);
                        }
                    }
                });
    }
    private boolean isOne =false;//是不是只有一个录播视频
    /**
     * 获取录播地址
     */
    private void getVodList(LiveIsSubscribeBean mLiveIsSubScribeBean){
        requestGet(HttpUrls.URL_LIVE_VOD_LIST(),new ApiParams().with("live_id",mId),
                VodListBean.class, new HttpCallBack<VodListBean>() {

                    @Override
                    public void onFailure(String message) {
                     //   requestFailureShow(message);
                        showUi(mLiveIsSubScribeBean,"");
                    }

                    @Override
                    public void onSuccess(VodListBean mVodListBean) {

                        if (mVodListBean.getData().getLiveRecordVideoList().getLiveRecordVideo().size()>=1) {
                            Collections.reverse(mVodListBean.getData().getLiveRecordVideoList().getLiveRecordVideo());
                            int curPos = getIntent().getIntExtra("position",0);
                            if (mVodListBean.getData().getLiveRecordVideoList().getLiveRecordVideo().size()==1)
                                isOne =true;
                            if (curPos>=mVodListBean.getData().getLiveRecordVideoList().getLiveRecordVideo().size()){
                                curPos=0;
                            }
                            getPlayInfo(mLiveIsSubScribeBean, mVodListBean.getData().getLiveRecordVideoList().getLiveRecordVideo().get(curPos).getVideo().getVideoId());
                        }else {
                            MyToast.myLongToast(getApplicationContext(),"录播视频暂时还未生成，请稍后重新进入");
                        }
                    }
                });

    }

    /**
     * 系统弹幕 重要弹幕
     * @param content
     */
    public void addSySDanMu(String content){
        ((EmptyControlVideo)(mVideoPlayer.getCurrentPlayer())).addDanmaku(isLive,content, Color.parseColor("#ff4081"), (byte) 9);
    }
    public void addDanMu(String content){
        ((EmptyControlVideo)(mVideoPlayer.getCurrentPlayer())).addDanmaku(isLive,content, Color.WHITE, (byte) 7);
    }
    /**
     * 获取录播地址详情
     */
    private void getPlayInfo(LiveIsSubscribeBean mLiveIsSubScribeBean,String mVideoId){
        requestGet(HttpUrls.URL_LIVE_PLAY_INFO(),new ApiParams().with("video_id",mVideoId),
                PlayInfoBean.class, new HttpCallBack<PlayInfoBean>() {

                    @Override
                    public void onFailure(String message) {
                        //requestFailureShow(message);
                        showUi(mLiveIsSubScribeBean,"");
                    }

                    @Override
                    public void onSuccess(PlayInfoBean mVodListBean) {
                        findViewById(R.id.rl_empty).setVisibility(View.GONE);
                        showUi(mLiveIsSubScribeBean,  mVodListBean.getData().getPlayInfoList().getPlayInfo().get(0).getPlayURL());
                    }
                });

    }

    /**
     * 获得在线人数
     */
    private void getOnlineCount() {
        if (mLiveDetailBean!=null)
            requestGet(HttpUrls.URL_LIVE_ONLINE_COUNT(),new ApiParams().with("live_id",mLiveDetailBean.getData().getLive().getId()),
                    OnlineCountBean.class, new HttpCallBack<OnlineCountBean>() {

                        @Override
                        public void onFailure(String message) {
                            mHandler.removeCallbacks(mOnlineCountRunnable);
                            mHandler.postDelayed(mOnlineCountRunnable,10000);
                        }

                        @Override
                        public void onSuccess(OnlineCountBean mVodListBean) {
                            onlineCount =mVodListBean.getData();
                            ((EmptyControlVideo)mVideoPlayer.getCurrentPlayer()).showOnlineCount(mVodListBean.getData());
                            mHandler.removeCallbacks(mOnlineCountRunnable);
                            mHandler.postDelayed(mOnlineCountRunnable,10000);
                        }
                    });
    }

    /**
     * 初始化显示界面
     * @param lectureListsBean
     * @param url
     */
    private void showUi(LiveIsSubscribeBean lectureListsBean,String url) {
    /*    if (lectureListsBean.getData().isSubscribe()){
            tvVote.setText("已关注");
        }else {
            tvVote.setText("+");
            tvVote.setOnClickListener(v -> getVote());
        }*/
        initVideoPlay(url);
        //目前调用浏览数，去掉在线数
        if (Constants.APP_DBG) {
            mHandler.removeCallbacks(mOnlineCountRunnable);
            mHandler.post(mOnlineCountRunnable);
        }
        initTabLayout();
    }
   /* private void getVote() {
        if (mLiveDetailBean!=null) {
            BeautyDefine.getOpenPageDefine(this).progressControl(new OpenPageDefine.ProgressController.Showder("关注中",false));
            requestPost(HttpUrls.URL_LIVE_SUBSCRIBE(), new ApiParams().with("live_id", String.valueOf(mLiveDetailBean.getData().getLive().getId())),
                    CommonEntity.class, new HttpCallBack<CommonEntity>() {

                        @Override
                        public void onFailure(String message) {
                            MyToast.myToast(getApplicationContext(),message);
                            BeautyDefine.getOpenPageDefine(LiveDetailActivity.this).progressControl(new OpenPageDefine.ProgressController.Hider());
                        }

                        @Override
                        public void onSuccess(CommonEntity mVodListBean) {
                            BeautyDefine.getOpenPageDefine(LiveDetailActivity.this).progressControl(new OpenPageDefine.ProgressController.Hider());
                            tvVote.setText("已关注");
                            tvVote.setEnabled(false);
                        }
                    });
        }
    }*/


    private void  initVideoPlay(String url){


        if (TextUtils.isEmpty(url)){//视频地址获取失败
            findViewById(R.id.rl_empty).setVisibility(View.GONE);
            MyToast.myLongToast(getApplicationContext(),"视频地址获取失败，请重新进入直播间");
            return;
        }
        if (orientationUtils==null) {
            //外部辅助的旋转，帮助全屏
            orientationUtils = new OrientationUtils(this, mVideoPlayer);
            //初始化不打开外部的旋转
            orientationUtils.setEnable(false);

            GSYVideoOptionBuilder  gsyVideoOption = new GSYVideoOptionBuilder();
            gsyVideoOption.setIsTouchWiget(true)
                    .setIsTouchWigetFull(true)
                    .setRotateViewAuto(false)
                    .setLockLand(false)
                    .setAutoFullWithSize(false)
                    .setShowFullAnimation(false)
                    .setNeedLockFull(true)
                    .setUrl(url)
                    .setNeedShowWifiTip(false)
                    .setDismissControlTime(5000)
                    .setCacheWithPlay(true)
                    .setCachePath(ClassRoomLibUtils.getVideoCachePathFile(this))
                    .setLooping(isOne)
                    .setGSYVideoProgressListener((progress, secProgress, currentPosition, duration) -> Debuger.printfLog(" progress " + progress + " secProgress " + secProgress + " currentPosition " + currentPosition + " duration " + duration))
                    .setVideoAllCallBack(new GSYSampleCallBack() {

                        @Override
                        public void onPrepared(String url, Object... objects) {
                            super.onPrepared(url, objects);
                            //开始播放了才能旋转和全屏
                            orientationUtils.setEnable(true);
                            isPlay = true;
                            //隐藏图片动作写到这可能出现倒计时结束，直播还没开启 播放错误得样子
                            if (isLive) {
                                mVideoPlayer.goneThumbBg();
                                tvTime.setVisibility(View.GONE);
                            }

                        }

                        @Override
                        public void onAutoComplete(String url, Object... objects) {
                            super.onAutoComplete(url, objects);
                            if (isLive){// 正在直播状态 结束后
                                liveEnd();
                            }else {//播放下一个视频
                                if (!isOne) {
                                    ClassRoomLibUtils.startLiveDetailActivity(LiveDetailActivity.this, mId, false,
                                            getIntent().getIntExtra("position",0)+1);
                                    finish();
                                    overridePendingTransition(0,0);
                                }
                            }

                        }

                        @Override
                        public void onClickResume(String url, Object... objects) {
                            super.onClickResume(url, objects);
                        }

                        @Override
                        public void onQuitFullscreen(String url, Object... objects) {
                            super.onQuitFullscreen(url, objects);
                            if (orientationUtils != null) {
                                orientationUtils.backToProtVideo();
                            }
                            if (mArrayLists.length>0)
                            ((LiveChatFragment) mArrayLists[0]).refreshAdapter();
                        }

                        @Override
                        public void onPlayError(String url, Object... objects) {
                            super.onPlayError(url, objects);

                            if (isLive){
                                if (liveEnd){//直播结束标识
                                    liveEnd();
                                    return;
                                }
                              /*  if (!isPlay){//直播前没有准备到资源
                                    mVideoPlayer.clickStartIcon();
                                    return;
                                }*/
                                mVideoPlayer.clickStartIcon();
                                return;
                            }

                            MyToast.myLongToast(getApplicationContext(),"播放错误，点击播放重试");
                        }
                    }).setLockClickListener((view, lock) -> {
                if (orientationUtils != null) {
                    //配合下方的onConfigurationChanged
                    orientationUtils.setEnable(!lock);
                }
            }).build(mVideoPlayer);

            mVideoPlayer.getFullscreenButton().setOnClickListener(v -> {

                try {// 可能出现java.lang.IllegalStateException at android.media.MediaPlayer.getVideoHeight(Native Method)
                    KeyBoardHelper.hideSoftInput(this);//隐藏软键盘
                    //直接横屏
                    orientationUtils.resolveByClick();
                    //第一个true是否需要隐藏actionbar，第二个true是否需要隐藏statusbar
                    mVideoPlayer.startWindowFullscreen(LiveDetailActivity.this, true, true);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            if (isLive&&(System.currentTimeMillis()/1000<mLiveDetailBean.getData().getLive().getStart_time())) {//开始播放前
                if (mLiveDetailBean.getData().getLive().getCover()!=null)
                    mVideoPlayer.showThumbBg(mLiveDetailBean.getData().getLive().getCover());
                tvTime.setVisibility(View.VISIBLE);
                mTime = mLiveDetailBean.getData().getLive().getStart_time()-System.currentTimeMillis()/1000;
                mHandler.post(mDownRunnable);
            }else {
                mVideoPlayer.startPlayLogic();
            }
        }
    }

    private void initTransition() {
        if (isTransition && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            postponeEnterTransition();
            ViewCompat.setTransitionName(mVideoPlayer, "IMG_TRANSITION");
            addTransitionListener();
            startPostponedEnterTransition();
        }else {
            mVideoPlayer.startPlayLogic();
        }
    }
    private String sToHMS(long m){
        return "距离直播还有"+m/86400+"天"+(m % 86400 / 3600) + "时" + m % 86400 % 3600 / 60 + "分" + m % 60+"秒";
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private boolean addTransitionListener() {
        transition = getWindow().getSharedElementEnterTransition();
        if (transition != null) {
            transition.addListener(new OnTransitionListener(){
                @Override
                public void onTransitionEnd(Transition transition) {
                    super.onTransitionEnd(transition);
                    mVideoPlayer.startPlayLogic();
                    transition.removeListener(this);
                }
            });
            return true;
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mHandler!=null&&mDownRunnable!=null)
            mHandler.removeCallbacks(mDownRunnable);
        if (mHandler!=null&&mOnlineCountRunnable!=null)
            mHandler.removeCallbacks(mOnlineCountRunnable);
        if (mVideoPlayer!=null) {//isplay 1.12去掉isplay判断
            mVideoPlayer.getCurrentPlayer().release();
        }
        if (orientationUtils != null)
            orientationUtils.releaseListener();

    }

    /**
     * 分享直播
     */
    public void goShape(View view){
        if (mLiveDetailBean!=null) {
            ArrayList<String> mPics = new ArrayList<>();
            mPics.add(mLiveDetailBean.getData().getLive().getCover());
            String regMatchTag = "<[^>]*>";
            //暂时不用uri跳转 ，classroom://"+getPackageName()+".zbdetail?id="+mBean.getData().getLecture().getId()+"&type='video'
            BeautyDefine.getShareDefine(this).share("live/detail","id="+ mLiveDetailBean.getData().getLive().getId()+"&is_end="+(isLive?1:0),"classroom://"+getPackageName()+".zbdetail?id="+ mLiveDetailBean.getData().getLive().getId()+"&is_end="+(isLive?1:0),HttpUrls.URL_DOWNLOAD(),mPics,mLiveDetailBean.getData().getLive().getTitle(),
                    mLiveDetailBean.getData().getLive().getBody().replaceAll(regMatchTag,""),new ShareResultCallBack(){

                        @Override
                        public void onSucceed()
                        {

                        }
                        @Override
                        public void onFailure(String s) {
                            MyToast.myToast(getApplicationContext(),s);
                        }
                    });
        }
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        //如果旋转了就全屏
        if (isPlay && !isPause) {
            mVideoPlayer.onConfigurationChanged(this, newConfig, orientationUtils, true, true);
        }
    }
    @Override
    public void onBackPressed() {
        if (orientationUtils != null) {
            orientationUtils.backToProtVideo();
        }

        if (GSYVideoManager.backFromWindowFull(this)) {
            return;
        }
        super.onBackPressed();
    }
    private boolean isPlaying = false;
    @Override
    protected void onPause() {
        if (mVideoPlayer!=null) {
            isPlaying = mVideoPlayer.getGSYVideoManager().isPlaying();
            mVideoPlayer.getCurrentPlayer().onVideoPause();
        }
       /* if (mVideoPlayer != null) {
            SharedPreferenceUntils.putString(this, "playposition" + mId, mVideoPlayer.getCurrentPositionWhenPlaying() + "");
        }*/
        super.onPause();
        isPause = true;
    }
    @Override
    protected void onResume() {
        if (isPlaying)
            mVideoPlayer.getCurrentPlayer().onVideoResume(false);
        super.onResume();
        isPause = false;
    }




    private void initView() {
        AndroidBug5497Workaround.assistActivity(this);
        tabLayout =findViewById(R.id.tabLayout);
        tvTime =findViewById(R.id.tvTime);
        //tvVote =findViewById(R.id.tvVote);
        viewPager =findViewById(R.id.viewPager);
        viewPager.setOffscreenPageLimit(4);
        mVideoPlayer = findViewById(R.id.video_track);
        //  RelativeLayout rlTime = findViewById(R.id.rlTime);
        mVideoHeight = CommentUtils.getScreenWidth(this)*720/1280;
        mVideoPlayer.getLayoutParams().height =mVideoHeight;
        RelativeLayout rlVideoRoot =findViewById(R.id.rlVideoRoot);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            rlVideoRoot.setPadding(0, StatusBarUtils.getStatusHeight(this), 0, 0);
        }
    }
    private Fragment[] mArrayLists ;
    private String[] mTitle ={"现场互动","直播介绍","产品列表","排行榜"};
    private void initTabLayout() {
        mArrayLists =new  Fragment[]{
                LiveChatFragment.getInstance(String.valueOf(mLiveDetailBean.getData().getLive().getId()),
                        mLiveDetailBean.getData().getLive().getFixed_state()==1?mLiveDetailBean.getData().getLive().getFixed_str():""
                        ,mLiveDetailBean.getData().getLive().getSpeaker()),
                LiveDesFragment.getInstance(mLiveDetailBean.getData().getLive().getBody()),LiveGoodListFragment.getInstance(mId)
            ,LiveGiftTopsFragment.getInstance(mId)};
        tabLayout.removeAllTabs();
        HomeViewpagerAdapter mViewPagerAdapter = new HomeViewpagerAdapter(getSupportFragmentManager(), Arrays.asList(mArrayLists), Arrays.asList(mTitle));
        viewPager.setAdapter(mViewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }
    private void requestFailureShow(String error){
        TextView tvMsg = findViewById(R.id.tv_msg);
        tvMsg.setText(error);
        CustomProgressBar progressbar = findViewById(R.id.progressbar);
        progressbar .setVisibility(View.GONE);
        findViewById(R.id.rl_empty).setOnClickListener(v -> {
            progressbar.setVisibility(View.VISIBLE);
            tvMsg.setText("加载中...");
            initData();
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        mArrayLists[ viewPager.getCurrentItem()].onActivityResult(requestCode,resultCode,data);
    }
    @Override
    protected int layoutResId() {
        return R.layout.activity_live_detail_classromm;
    }
}
