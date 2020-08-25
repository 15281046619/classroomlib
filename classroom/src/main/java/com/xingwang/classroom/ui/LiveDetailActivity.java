package com.xingwang.classroom.ui;


import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.transition.Transition;

import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.google.android.exoplayer2.SeekParameters;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder;
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack;
import com.shuyu.gsyvideoplayer.model.VideoOptionModel;
import com.shuyu.gsyvideoplayer.player.PlayerFactory;
import com.shuyu.gsyvideoplayer.utils.GSYVideoType;
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
import com.xingwang.classroom.http.ApiParams;
import com.xingwang.classroom.http.HttpCallBack;
import com.xingwang.classroom.http.HttpUrls;
import com.xingwang.classroom.listener.OnTransitionListener;
import com.xingwang.classroom.utils.AndroidBug5497Workaround;
import com.xingwang.classroom.utils.CommentUtils;
import com.xingwang.classroom.utils.Constants;
import com.xingwang.classroom.utils.KeyBoardHelper;
import com.xingwang.classroom.utils.MyToast;
import com.xingwang.classroom.utils.StatusBarUtils;
import com.xingwang.classroom.view.CustomProgressBar;
import com.xingwang.classroom.view.EmptyControlVideo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import tv.danmaku.ijk.media.exo2.Exo2PlayerManager;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

import static com.xingwang.classroom.http.HttpUrls.URL_ZHI_BO;

/**
 * Date:2020/6/2
 * Time;10:54
 * author:baiguiqiang
 */
public class LiveDetailActivity extends BaseNetActivity {
    private EmptyControlVideo mVideoPlayer;
    private TextView tvVote,tvTime;
    public OrientationUtils orientationUtils;
    private boolean isTransition =true;
    private Transition transition;
    public boolean isLive ;
    private int mVideoHeight ;//默认720*1280分辨率
    private  TabLayout tabLayout;
    private  ViewPager viewPager;
    private String mId;//直播id
    private LiveDetailBean mLiveDetailBean;//直播详情接口获取数据
    private VodListBean mVodListBean;
    private int curPlayVodPos =0;//当前播放录播视频得第几个
    private Handler mHandler = new Handler();
    private long mTime = 0;
    private boolean isPlay =false;
    private boolean isPause;
    public long onlineCount=-1;//在线人数

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
                if (isLive) {
                    mVideoPlayer.goneThumbBg();
                    tvTime.setVisibility(View.GONE);
                }
            }
        }
    };
    private Runnable mOnlineCountRunnable=new Runnable() {//每隔10s刷新一次在线人数查询
        @Override
        public void run() {
            getOnlineCount();
            mHandler.postDelayed(this,10000);
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        isLive =getIntent().getBooleanExtra(Constants.EXTRA_IS_LIVE,true);
        super.onCreate(savedInstanceState);
        setNavigationBarColor(android.R.color.black);
        //mId = getIntent().getStringExtra("id");
        initId();
        initView();
        initData();
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
                            return;
                        }
                        getIsSubscribe();
                        mHandler.post(mOnlineCountRunnable);

                    }
                });
    }
    public void liveEnd(){
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
    /**
     * 获取录播地址
     */
    private void getVodList(LiveIsSubscribeBean mLiveIsSubScribeBean){
        requestGet(HttpUrls.URL_LIVE_VOD_LIST(),new ApiParams().with("live_id",mId),
                VodListBean.class, new HttpCallBack<VodListBean>() {

                    @Override
                    public void onFailure(String message) {
                        requestFailureShow(message);
                    }

                    @Override
                    public void onSuccess(VodListBean mVodListBean) {
                        LiveDetailActivity.this.mVodListBean = mVodListBean;
                        // findViewById(R.id.rl_empty).setVisibility(View.GONE);
                        getPlayInfo(mLiveIsSubScribeBean,mVodListBean.getData().getLiveRecordVideoList().getLiveRecordVideo().get(curPlayVodPos).getVideo().getVideoId());
                    }
                });

    }
    /**
     * 获取录播地址详情
     */
    private void getPlayInfo(LiveIsSubscribeBean mLiveIsSubScribeBean,String mVideoId){
        requestGet(HttpUrls.URL_LIVE_PLAY_INFO(),new ApiParams().with("video_id",mVideoId),
                PlayInfoBean.class, new HttpCallBack<PlayInfoBean>() {

                    @Override
                    public void onFailure(String message) {
                        requestFailureShow(message);
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

                        }

                        @Override
                        public void onSuccess(OnlineCountBean mVodListBean) {
                            onlineCount =mVodListBean.getData();
                            ((EmptyControlVideo)mVideoPlayer.getCurrentPlayer()).showOnlineCount(mVodListBean.getData());
                        }
                    });
    }

    /**
     * 初始化显示界面
     * @param lectureListsBean
     * @param url
     */
    private void showUi(LiveIsSubscribeBean lectureListsBean,String url) {
        if (lectureListsBean.getData().isSubscribe()){
            tvVote.setText("已关注");
        }else {
            tvVote.setText("+");
            tvVote.setOnClickListener(v -> {

            });
        }
        initVideoPlay(url);
        initTabLayout();
    }
  /*  private void getCommentList(int loadDataTypeInit) {
        requestGet(HttpUrls.URL_LIVE_DETAIL(),new ApiParams().with("id",getIntent().getStringExtra("id")),
                LiveDetailBean.class, new HttpCallBack<LiveDetailBean>() {

                    @Override
                    public void onFailure(String message) {
                        requestFailureShow(message);
                    }

                    @Override
                    public void onSuccess(LiveDetailBean lectureListsBean) {
                        findViewById(R.id.rl_empty).setVisibility(View.GONE);
                        getCommentList(Constants.LOAD_DATA_TYPE_INIT);
                    }
                });
    }*/

    private void  initVideoPlay(String url){

        // PlayerFactory.setPlayManager(Exo2PlayerManager.class);//使用系统解码器全屏切换会卡顿黑屏
        //mVideoPlayer.setUp(url, true, "");
        //过渡动画
        // initTransition();
        if (orientationUtils==null) {
            //外部辅助的旋转，帮助全屏
            orientationUtils = new OrientationUtils(this, mVideoPlayer);
            //初始化不打开外部的旋转
            orientationUtils.setEnable(false);
            //尝试降低倍数 视频声音画面不同步
            VideoOptionModel videoOptionModel = new VideoOptionModel(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "framedrop", 30);
            List<VideoOptionModel> list = new ArrayList<>();
            list.add(videoOptionModel);
            GSYVideoManager.instance().setOptionModelList(list);

            mVideoPlayer.getBackButton().setOnClickListener(v -> onBackPressed());
            GSYVideoOptionBuilder gsyVideoOption = new GSYVideoOptionBuilder();
            PlayerFactory.setPlayManager(Exo2PlayerManager.class);//使用系统解码器全屏切换会卡顿黑屏
            //exo缓存模式，支持m3u8，只支持exo
            GSYVideoType.setRenderType(GSYVideoType.GLSURFACE);
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
                    .setCacheWithPlay(true)
                    .setCachePath(ClassRoomLibUtils.getVideoCachePathFile(this))
                    .setLooping(false)
                    .setGSYVideoProgressListener((progress, secProgress, currentPosition, duration) -> {

                  /*      if (mVideoPlayer.isGetVideoSize()&&!isInitViewHeight&&!mVideoPlayer.isVerticalVideo()){
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
                            //隐藏图片动作写到这可能出现倒计时结束，直播还没开启 播放错误得样子
                         /*   if (isLive) {
                                mVideoPlayer.goneThumbBg();
                                tvTime.setVisibility(View.GONE);
                            }*/

                        }

                        @Override
                        public void onAutoComplete(String url, Object... objects) {
                            super.onAutoComplete(url, objects);
                            MyToast.myLongToast(LiveDetailActivity.this,"onAutoComplete");
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
                        }
                    }).setLockClickListener((view, lock) -> {
                if (orientationUtils != null) {
                    //配合下方的onConfigurationChanged
                    orientationUtils.setEnable(!lock);
                }
            }).build(mVideoPlayer);

            mVideoPlayer.getFullscreenButton().setOnClickListener(v -> {
                try {// 可能出现java.lang.IllegalStateException at android.media.MediaPlayer.getVideoHeight(Native Method)
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
        } else {
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
    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mHandler!=null&&mDownRunnable!=null)
            mHandler.removeCallbacks(mDownRunnable);
        if (mHandler!=null&&mOnlineCountRunnable!=null)
            mHandler.removeCallbacks(mOnlineCountRunnable);
        if (isPlay) {
            mVideoPlayer.getCurrentPlayer().release();
        }
        if (orientationUtils != null)
            orientationUtils.releaseListener();

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

        isPlaying = mVideoPlayer.getGSYVideoManager().isPlaying();
        mVideoPlayer.getCurrentPlayer().onVideoPause();
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



    @SuppressLint("ClickableViewAccessibility")
    private void initView() {
        AndroidBug5497Workaround.assistActivity(this);
        tabLayout =findViewById(R.id.tabLayout);
        tvTime =findViewById(R.id.tvTime);
        tvVote =findViewById(R.id.tvVote);
        viewPager =findViewById(R.id.viewPager);
        viewPager.setOffscreenPageLimit(3);
        mVideoPlayer = findViewById(R.id.video_track);
        //  RelativeLayout rlTime = findViewById(R.id.rlTime);
        mVideoHeight = CommentUtils.getScreenWidth(this)*720/1280;
        mVideoPlayer.getLayoutParams().height =mVideoHeight;
        RelativeLayout rlVideoRoot =findViewById(R.id.rlVideoRoot);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            rlVideoRoot.setPadding(0, StatusBarUtils.getStatusHeight(this), 0, 0);
        }

    /*
        TextView tvTime = findViewById(R.id.tvTime);
        AnimatorSet animatorSetsuofang = new AnimatorSet();//组合动画
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(tvTime, "scaleX", 1f, 0f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(tvTime, "scaleY", 1f, 0f);
        scaleX.setRepeatCount(10);
        scaleY.setRepeatCount(10);
        animatorSetsuofang.setDuration(1000);
        animatorSetsuofang.setInterpolator(new DecelerateInterpolator());
        animatorSetsuofang.play(scaleX).with(scaleY);//两个动画同时开始
        animatorSetsuofang.start();
        scaleX.addListener(new Animator.AnimatorListener(){

            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
               // rlTime.setVisibility(View.GONE);
                ivThumb.setVisibility(View.GONE);
                mVideoPlayer.startPlayLogic();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                String mPos =    tvTime.getText().toString();
                int curTime  = Integer.parseInt(mPos)-1;
                tvTime.setText(""+curTime);
            }
        });*/
    }
    private Fragment[] mArrayLists ;
    private String[] mTitle ={"现场互动","直播介绍","产品列表"};
    private void initTabLayout() {
        mArrayLists =new  Fragment[]{
                LiveChatFragment.getInstance(String.valueOf(mLiveDetailBean.getData().getLive().getId()),
                        mLiveDetailBean.getData().getLive().getFixed_state()==1?mLiveDetailBean.getData().getLive().getFixed_str():""),
                LiveDesFragment.getInstance("tt"),LiveGoodListFragment.getInstance("ee")};
        tabLayout.removeAllTabs();
        HomeViewpagerAdapter mViewPagerAdapter = new HomeViewpagerAdapter(getSupportFragmentManager(), Arrays.asList(mArrayLists), Arrays.asList(mTitle));
        viewPager.setAdapter(mViewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        //  viewpager.setCurrentItem(initPos);
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
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {  //把操作放在用户点击的时候
            View v = getCurrentFocus();    //得到当前页面的焦点,ps:有输入框的页面焦点一般会被输入框占据
            if (isShouldHideKeyboard(v, event)) { //判断用户点击的是否是输入框以外的区域
                hideKeyboard(v.getWindowToken()) ;  //收起键盘
            }
        }
        return super.dispatchTouchEvent(event);
    }


    @Override
    protected int layoutResId() {
        return R.layout.activity_live_detail_classromm;
    }
}
