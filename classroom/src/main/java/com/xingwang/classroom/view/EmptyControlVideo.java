package com.xingwang.classroom.view;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;
import com.shuyu.gsyvideoplayer.video.base.GSYBaseVideoPlayer;
import com.shuyu.gsyvideoplayer.video.base.GSYVideoPlayer;
import com.xingwang.classroom.R;
import com.xingwang.classroom.ui.LiveDetailActivity;
import com.xingwang.classroom.utils.GlideUtils;
import com.xingwang.classroom.utils.LogUtil;

/**
 * 无任何控制ui的播放
 * Created by guoshuyu on 2017/8/6.
 */

public class EmptyControlVideo extends StandardGSYVideoPlayer {
    private boolean isLive = true;
    private boolean isLinkScroll = false;
    private  TextView tvSum;
    public EmptyControlVideo(Context context, Boolean fullFlag) {
        super(context, fullFlag);
    }

    public EmptyControlVideo(Context context) {
        super(context);
    }

    public EmptyControlVideo(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public void setIsLive(boolean isLive){
        this.isLive =isLive;
    }
    @Override
    public int getLayoutId() {

        if (getContext()==null){
            return getNormalLiveOrVideoLayout();
        }
        if (getContext() instanceof LiveDetailActivity) {
            LiveDetailActivity mLiveDetailActivity = (LiveDetailActivity) getContext();

            if (mLiveDetailActivity.orientationUtils == null) {
                return getNormalLiveOrVideoLayout();
            }
            if (mLiveDetailActivity.orientationUtils.getScreenType() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                return getLandLiveOrVideoLayout();
            }
        }
        return getNormalLiveOrVideoLayout();
    }
    private int  getNormalLiveOrVideoLayout(){
        if (isLive){
            return R.layout.empty_control_live_normal_classroom;
        }else {
            return R.layout.empty_control_video_normal_classroom;
        }
    }
    private int  getLandLiveOrVideoLayout(){

        if (isLive){
            return R.layout.empty_control_live_land_classroom;
        }else {
            return R.layout.empty_control_video_land_classroom;
        }
    }

    /**
     * 全屏得时候 findViewById
     * @param context
     * @param actionBar
     * @param statusBar
     * @return
     */
    @Override
    public GSYBaseVideoPlayer startWindowFullscreen(Context context, boolean actionBar, boolean statusBar) {
        EmptyControlVideo sampleVideo = (EmptyControlVideo) super.startWindowFullscreen(context, actionBar, statusBar);
        //sampleVideo.tvSum = findViewById(R.id.tvSum);
        tvSum= findViewById(R.id.tvSum);

        return sampleVideo;
        // return super.startWindowFullscreen(context, actionBar, statusBar);
    }

    /**
     * 退出全屏得时候 findViewById 才能够同步
     * @param oldF
     * @param vp
     * @param gsyVideoPlayer
     */
    @Override
    protected void resolveNormalVideoShow(View oldF, ViewGroup vp, GSYVideoPlayer gsyVideoPlayer) {
        super.resolveNormalVideoShow(oldF, vp, gsyVideoPlayer);
        tvSum= findViewById(R.id.tvSum);
        if (getContext() instanceof LiveDetailActivity)
            showOnlineCount(((LiveDetailActivity)getContext()).onlineCount);
    }

    @Override
    protected void changeUiToNormal() {
        super.changeUiToNormal();
    }

    public void showThumbBg(String url){
        findViewById(R.id.ivThumbBg).setVisibility(VISIBLE);
        GlideUtils.loadAvatar(url,findViewById(R.id.ivThumbBg));
    }
    public void goneThumbBg(){
        findViewById(R.id.ivThumbBg).setVisibility(GONE);
    }
    @Override
    protected void init(Context context) {

        if (context instanceof LiveDetailActivity){
            isLive =((LiveDetailActivity)context).isLive;
        }
        super.init(context);
        post(new Runnable() {
            @Override
            public void run() {
                gestureDetector = new GestureDetector(getContext().getApplicationContext(), new GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public boolean onDoubleTap(MotionEvent e) {
                        touchDoubleUp();
                        return super.onDoubleTap(e);
                    }

                    @Override
                    public boolean onSingleTapConfirmed(MotionEvent e) {
                        if (!mChangePosition && !mChangeVolume && !mBrightness) {
                            onClickUiToggle();
                        }
                        return super.onSingleTapConfirmed(e);
                    }

                    @Override
                    public void onLongPress(MotionEvent e) {
                        super.onLongPress(e);
                    }
                });
            }
        });
        tvSum =findViewById(R.id.tvSum);
        if (context instanceof LiveDetailActivity)
            showOnlineCount(((LiveDetailActivity)context).onlineCount);
    }


    @Override
    protected void initInflate(Context context) {
        super.initInflate(context);

    }

    //显示在线人数
    public void showOnlineCount(long count){
        if (count!=-1){
            tvSum.setVisibility(VISIBLE);
            tvSum.setText(String.valueOf(count * 10 + System.currentTimeMillis() % 10));
        }
    }
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (isLinkScroll && !isIfCurrentIsFullscreen()) {
            getParent().requestDisallowInterceptTouchEvent(true);
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    protected void touchSurfaceMoveFullLogic(float absDeltaX, float absDeltaY) {
        super.touchSurfaceMoveFullLogic(absDeltaX, absDeltaY);
        //不给触摸快进，如果需要，屏蔽下方代码即可
        mChangePosition = false;

        //不给触摸音量，如果需要，屏蔽下方代码即可
        mChangeVolume = false;

        //不给触摸亮度，如果需要，屏蔽下方代码即可
        mBrightness = false;
    }
    @Override
    public boolean isVerticalVideo() {
        return super.isVerticalVideo();
    }
    //获取到视频的宽高
    public boolean isGetVideoSize(){
        return getCurrentVideoHeight()>0&&getCurrentVideoWidth()>0;
    }
    @Override
    protected void touchDoubleUp() {
        if (!isLive)
            super.touchDoubleUp();
        //不需要双击暂停
    }
}
