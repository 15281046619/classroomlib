package com.xingwang.classroom.ui;


import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.transition.Transition;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder;
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack;
import com.shuyu.gsyvideoplayer.player.PlayerFactory;
import com.shuyu.gsyvideoplayer.utils.GSYVideoType;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;
import com.xingwang.classroom.ClassRoomLibUtils;
import com.xingwang.classroom.R;
import com.xingwang.classroom.bean.ADBean;
import com.xingwang.classroom.listener.OnTransitionListener;
import com.xingwang.classroom.utils.CommentUtils;
import com.xingwang.classroom.utils.SharedPreferenceUntils;
import com.xingwang.classroom.view.EmptyControlVideo;
import com.xingwang.classroom.view.LandLayoutVideo;

import java.util.Iterator;

import jp.wasabeef.glide.transformations.BlurTransformation;
import tv.danmaku.ijk.media.exo2.Exo2PlayerManager;

/**
 * Date:2020/6/2
 * Time;10:54
 * author:baiguiqiang
 */
public class LiveDetailActivity extends BaseNetActivity {
    private ImageView ivBG;
    private EmptyControlVideo mVideoPlayer;
    public OrientationUtils orientationUtils;
    private boolean isTransition =true;


    private Transition transition;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setNavigationBarColor(android.R.color.black);

        initView();
        initData();
    }

    private void initData() {
        initVideoPlay("");
    }

    private void  initVideoPlay(String url){
        url ="http://devimages.apple.com.edgekey.net/streaming/examples/bipbop_4x3/gear2/prog_index.m3u8";
        //url ="https://xw518app.oss-cn-beijing.aliyuncs.com/2020/04/27/090431_App6.mp4";
        PlayerFactory.setPlayManager(Exo2PlayerManager.class);//使用系统解码器全屏切换会卡顿黑屏
        mVideoPlayer.setUp(url, true, "");
        ivBG.setVisibility(View.GONE);
        //过渡动画
        initTransition();
    /*    if (orientationUtils==null) {
            //外部辅助的旋转，帮助全屏
            orientationUtils = new OrientationUtils(this, mVideoPlayer);
            //初始化不打开外部的旋转
            orientationUtils.setEnable(false);


            mVideoPlayer.getBackButton().setOnClickListener(v -> onBackPressed());
            GSYVideoOptionBuilder gsyVideoOption = new GSYVideoOptionBuilder();
            PlayerFactory.setPlayManager(Exo2PlayerManager.class);//使用系统解码器全屏切换会卡顿黑屏
            //exo缓存模式，支持m3u8，只支持exo
            // CacheFactory.setCacheManager(ExoPlayerCacheManager.class);
            GSYVideoType.setRenderType(GSYVideoType.GLSURFACE);
            GSYVideoType.setShowType(GSYVideoType.SCREEN_TYPE_FULL);


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

                    })
                    .setVideoAllCallBack(new GSYSampleCallBack() {
                        @Override
                        public void onPrepared(String url, Object... objects) {
                            super.onPrepared(url, objects);
                    *//*        //开始播放了才能旋转和全屏
                            orientationUtils.setEnable(true);

                            mVideoPlayer.postDelayed(() -> {
                                if (mVideoPlayer.isVerticalVideo()){
                                    mVideoPlayer.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
                                }else {
                                    mVideoPlayer.getLayoutParams().height = CommentUtils.getScreenWidth(LiveDetailActivity.this)*mVideoPlayer.getCurrentVideoHeight()/mVideoPlayer.getCurrentVideoWidth();
                                }
                                ivBG.setVisibility(View.GONE);

                            },500);*//*


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

            });
            mVideoPlayer.startPlayLogic();
        }*/
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
        ivBG = findViewById(R.id.ivBG);
        mVideoPlayer = findViewById(R.id.video_track);
    }

    @Override
    protected int layoutResId() {
        return R.layout.activity_live_detail;
    }
}
