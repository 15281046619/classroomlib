package com.xingwang.classroom.ui;


import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
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
import android.text.TextUtils;
import android.transition.Transition;

import android.widget.RelativeLayout;


import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder;
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack;
import com.shuyu.gsyvideoplayer.player.PlayerFactory;
import com.shuyu.gsyvideoplayer.utils.GSYVideoType;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;
import com.xingwang.classroom.ClassRoomLibUtils;
import com.xingwang.classroom.R;
import com.xingwang.classroom.adapter.HomeViewpagerAdapter;

import com.xingwang.classroom.listener.OnTransitionListener;
import com.xingwang.classroom.utils.CommentUtils;
import com.xingwang.classroom.utils.Constants;
import com.xingwang.classroom.utils.StatusBarUtils;
import com.xingwang.classroom.view.EmptyControlVideo;

import java.util.Arrays;

import tv.danmaku.ijk.media.exo2.Exo2PlayerManager;

/**
 * Date:2020/6/2
 * Time;10:54
 * author:baiguiqiang
 */
public class LiveDetailActivity extends BaseNetActivity {
    private EmptyControlVideo mVideoPlayer;
    public OrientationUtils orientationUtils;
    private boolean isTransition =true;
    private Transition transition;
    public boolean isLive ;
    private int mVideoHeight ;//默认720*1280分辨率
    private  TabLayout tabLayout;
    private  ViewPager viewPager;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        isLive =getIntent().getBooleanExtra(Constants.EXTRA_IS_LIVE,true);
        super.onCreate(savedInstanceState);
        setNavigationBarColor(android.R.color.black);

        initView();
        initData();
    }

    private void initData() {
        initVideoPlay("");
    }

    private void  initVideoPlay(String url){
        if (isLive)
            url ="http://ivi.bupt.edu.cn/hls/cctv1hd.m3u8";
        else
            url ="https://xw518app.oss-cn-beijing.aliyuncs.com/2020/04/27/090431_App6.mp4";
        // PlayerFactory.setPlayManager(Exo2PlayerManager.class);//使用系统解码器全屏切换会卡顿黑屏
        //mVideoPlayer.setUp(url, true, "");
        //过渡动画
        // initTransition();
        if (orientationUtils==null) {
            //外部辅助的旋转，帮助全屏
            orientationUtils = new OrientationUtils(this, mVideoPlayer);
            //初始化不打开外部的旋转
            orientationUtils.setEnable(false);


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
                    .setLooping(true)
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

            mVideoPlayer.startPlayLogic();
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
        mVideoPlayer.release();
        if (orientationUtils != null)
            orientationUtils.releaseListener();
    }

    @Override
    public void onBackPressed() {
        //释放所有
        mVideoPlayer.setVideoAllCallBack(null);
        GSYVideoManager.releaseAllVideos();
        if (isTransition && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            super.onBackPressed();
        } else {
            new Handler().postDelayed(() -> {
                finish();
                overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
            }, 500);
        }
    }

    private void initView() {

        tabLayout =findViewById(R.id.tabLayout);
        viewPager =findViewById(R.id.viewPager);
        mVideoPlayer = findViewById(R.id.video_track);
        //  RelativeLayout rlTime = findViewById(R.id.rlTime);
        mVideoHeight = CommentUtils.getScreenWidth(this)*720/1280;
        mVideoPlayer.getLayoutParams().height =mVideoHeight;
        RelativeLayout rlVideoRoot =findViewById(R.id.rlVideoRoot);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            rlVideoRoot.setPadding(0, StatusBarUtils.getStatusHeight(this), 0, 0);
        }
        initTabLayout();
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
    private Fragment[] mArrayLists ={LiveChatFragment.getInstance("1"),LiveDesFragment.getInstance("tt"),LiveGoodListFragment.getInstance("ee")};
    private String[] mTitle ={"现场互动","直播介绍","产品列表"};
    private void initTabLayout() {

        tabLayout.removeAllTabs();
        HomeViewpagerAdapter mViewPagerAdapter = new HomeViewpagerAdapter(getSupportFragmentManager(), Arrays.asList(mArrayLists), Arrays.asList(mTitle));
        viewPager.setAdapter(mViewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
      //  viewpager.setCurrentItem(initPos);
    }

    @Override
    protected int layoutResId() {
        return R.layout.activity_live_detail_classromm;
    }
}
