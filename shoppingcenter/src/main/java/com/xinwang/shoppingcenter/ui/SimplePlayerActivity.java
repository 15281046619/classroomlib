package com.xinwang.shoppingcenter.ui;

import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.widget.RelativeLayout;
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder;
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;
import com.xinwang.bgqbaselib.base.BaseNetActivity;
import com.xinwang.bgqbaselib.utils.StatusBarUtils;
import com.xinwang.shoppingcenter.R;
import com.xinwang.shoppingcenter.view.EmpLayoutVideo;


public class SimplePlayerActivity extends BaseNetActivity {

    EmpLayoutVideo videoPlayer;
    private boolean isPause;
    public OrientationUtils orientationUtils;
    private boolean isPlay;

    @Override
    protected int layoutResId() {
        return R.layout.activity_simple_play_shoppingcenter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        videoPlayer =  findViewById(R.id.video_player);
      //  initSettingToolBarHeight();

        String source1 = getIntent().getStringExtra("url");


        if (orientationUtils == null) {
            //外部辅助的旋转，帮助全屏
            orientationUtils = new OrientationUtils(this, videoPlayer);
            //初始化不打开外部的旋转
            orientationUtils.setEnable(false);
            videoPlayer.getBackButton().setOnClickListener(v -> onBackPressed());
            GSYVideoOptionBuilder gsyVideoOption = new GSYVideoOptionBuilder();

            gsyVideoOption.setIsTouchWiget(true)
                    .setIsTouchWigetFull(true)
                    .setRotateViewAuto(false)
                    .setLockLand(false)
                    .setAutoFullWithSize(false)
                    .setShowFullAnimation(false)
                    .setNeedLockFull(true)
                    .setUrl(source1)
                    .setNeedShowWifiTip(true)
                    .setDismissControlTime(5000)
                    .setCacheWithPlay(false)
                    .setLooping(true)
                    .setGSYVideoProgressListener((progress, secProgress, currentPosition, duration) -> {

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
            }).build(videoPlayer);
            videoPlayer.getFullscreenButton().setOnClickListener(v -> {
                try {

                    // 可能出现java.lang.IllegalStateException at android.media.MediaPlayer.getVideoHeight(Native Method)
                    //直接横屏
                    orientationUtils.resolveByClick();
                    //第一个true是否需要隐藏actionbar，第二个true是否需要隐藏statusbar
                    videoPlayer.startWindowFullscreen(SimplePlayerActivity.this, true, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            });
            videoPlayer.startPlayLogic();

        }
    }

    private void initSettingToolBarHeight() {
        RelativeLayout activity_play =  findViewById(R.id.activity_play);
        int mStatusHeight = StatusBarUtils.getStatusHeight(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity_play.setPadding(0, mStatusHeight, 0, 0);
        }

    }
    private boolean isPlaying = false;
    @Override
    protected void onPause() {
        if (videoPlayer!=null) {
            isPlaying = videoPlayer.getGSYVideoManager().isPlaying();
            videoPlayer.getCurrentPlayer().onVideoPause();

        }
        super.onPause();

        isPause = true;
    }

    @Override
    protected void onResume() {
        if (isPlaying&&videoPlayer!=null)
            videoPlayer.getCurrentPlayer().onVideoResume(false);
        super.onResume();
        isPause = false;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
   
            //如果旋转了就全屏
            if (isPlay && !isPause) {
                videoPlayer.onConfigurationChanged(this, newConfig, orientationUtils, true, true);
            }
        
    }
    @Override
    protected void onDestroy() {

        super.onDestroy();
        if (videoPlayer!=null) {
            videoPlayer.getCurrentPlayer().release();
        }
        if (orientationUtils != null)
            orientationUtils.releaseListener();

    }
}
