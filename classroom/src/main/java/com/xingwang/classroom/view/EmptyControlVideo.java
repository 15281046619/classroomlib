package com.xingwang.classroom.view;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.util.AttributeSet;

import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;
import com.xingwang.classroom.R;
import com.xingwang.classroom.ui.LiveDetailActivity;
import com.xingwang.classroom.utils.LogUtil;

/**
 * 无任何控制ui的播放
 * Created by guoshuyu on 2017/8/6.
 */

public class EmptyControlVideo extends StandardGSYVideoPlayer {
    private boolean isLive = true;
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
        LogUtil.i("TAG",isLive+"getNormalLiveOrVideoLayout");
        if (isLive){
            return R.layout.empty_control_live_normal_classroom;
        }else {
            return R.layout.empty_control_video_normal_classroom;
        }
    }
    private int  getLandLiveOrVideoLayout(){
        LogUtil.i("TAG",isLive+"getLandLiveOrVideoLayout");
        if (isLive){
            return R.layout.empty_control_live_land_classroom;
        }else {
            return R.layout.empty_control_video_land_classroom;
        }
    }

    @Override
    protected void init(Context context) {
        if (context instanceof LiveDetailActivity){
            isLive =((LiveDetailActivity)context).isLive;
        }
        super.init(context);
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
        super.touchDoubleUp();
        //不需要双击暂停
    }
}
