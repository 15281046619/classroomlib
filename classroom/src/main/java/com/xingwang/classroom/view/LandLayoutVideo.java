package com.xingwang.classroom.view;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.shuyu.gsyvideoplayer.utils.NetworkUtils;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;
import com.xingwang.classroom.R;
import com.xingwang.classroom.ui.ClassRoomDetailActivity;
import com.xingwang.classroom.utils.GlideUtils;
import com.xingwang.classroom.utils.LogUtil;


/**
 * Created by shuyu on 2016/12/23.
 * CustomGSYVideoPlayer是试验中，建议使用的时�?�使用StandardGSYVideoPlayer
 */
public class LandLayoutVideo extends StandardGSYVideoPlayer  {

    private ImageView ivShape,ivCollect;
   // private ImageView ivBgThumb;
    private RelativeLayout rlAd;

    /**
     * 1.5.0�?始加入，如果�?要不同布�?区分功能，需要重�?
     */
    public LandLayoutVideo(Context context, Boolean fullFlag) {
        super(context, fullFlag);
    }

    public LandLayoutVideo(Context context) {
        super(context);
    }

    public LandLayoutVideo(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

  /*  @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (v.getId()==R.id.progress){
            return true;
        }else
            return super.onTouch(v, event);
    }*/

    //这个必须配置�?上面的构造才能生�?
    @Override
    public int getLayoutId() {
        if (getContext()==null){
            return R.layout.sample_video_normal_classroom;
        }
        if (getContext() instanceof ClassRoomDetailActivity) {
            ClassRoomDetailActivity classRoomDetailActivity = (ClassRoomDetailActivity) getContext();

            if (classRoomDetailActivity.orientationUtils == null) {
                return R.layout.sample_video_normal_classroom;
            }
            if (classRoomDetailActivity.orientationUtils.getScreenType() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                return R.layout.sample_video_land_classroom;
            }
        }
        return R.layout.sample_video_normal_classroom;
    }

    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
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
    }

    @Override
    public void onConfigurationChanged(Activity activity, Configuration newConfig, OrientationUtils orientationUtils, boolean hideActionBar, boolean hideStatusBar) {
        super.onConfigurationChanged(activity, newConfig, orientationUtils, hideActionBar, hideStatusBar);
        if (activity!=null&&ivCollect!=null){
            ClassRoomDetailActivity classRoomDetailActivity = (ClassRoomDetailActivity) activity;
            ivCollect.setSelected(classRoomDetailActivity.isCollect);

        }
    }
   /* public ImageView getBgThumb(){
        return ivBgThumb;
    }*/
    @Override
    public boolean isVerticalVideo() {
        return super.isVerticalVideo();
    }
    //获取到视频的宽高
    public boolean isGetVideoSize(){
       return getCurrentVideoHeight()>0&&getCurrentVideoWidth()>0;
    }
    @Override
    protected void initInflate(Context context) {
        super.initInflate(context);
        if (context==null){
            return;
        }
        if (getContext() instanceof  ClassRoomDetailActivity) {
            ClassRoomDetailActivity classRoomDetailActivity = (ClassRoomDetailActivity) getContext();
            ivShape = findViewById(R.id.iv_shape);
            ivCollect = findViewById(R.id.iv_collect);
            rlAd = findViewById(R.id.rl_ad);

            ivCollect.setSelected(classRoomDetailActivity.isCollect);
            ivShape.setOnClickListener(v -> {
                classRoomDetailActivity.goShape(v);
            });
            ivCollect.setOnClickListener(v -> {
                classRoomDetailActivity.goCollect(v);
            });
            if (mProgressBar != null)
                mProgressBar.setEnabled(false);
        }
    }
   /* public void setThumb(ImageView imageView){
        if (imageView!=null&&thumb!=null){
           *//* thumb.removeAllViews();
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            thumb.setVisibility(VISIBLE);
            thumb.addView(imageView,new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));*//*
            thumb.setVisibility(VISIBLE);
            setThumbImageView(imageView);
        }
    }*/
    /**
     * 不使用滑动改变进度
     */
    /*@Override
    protected void showProgressDialog(float deltaX, String seekTime, int seekTimePosition, String totalTime, int totalTimeDuration) {

    }*/

    public View getCollectView(){
        return ivCollect;
    }
    public RelativeLayout getADViewRoot(){
        return rlAd;
    }
    @Override
    protected void showWifiDialog() {
        if (!NetworkUtils.isAvailable(mContext)) {
            Toast.makeText(mContext, getResources().getString(com.shuyu.gsyvideoplayer.R.string.no_net), Toast.LENGTH_LONG).show();
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivityContext());
        builder.setMessage(getResources().getString(com.shuyu.gsyvideoplayer.R.string.tips_not_wifi));
        builder.setPositiveButton(getResources().getString(com.shuyu.gsyvideoplayer.R.string.tips_not_wifi_confirm), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                startPlayLogic();
            }
        });
        builder.setNegativeButton(getResources().getString(com.shuyu.gsyvideoplayer.R.string.tips_not_wifi_cancel), (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }

    @Override
    protected void updateStartImage() {
        if (mIfCurrentIsFullscreen) {
            if(mStartButton instanceof ImageView) {
                ImageView imageView = (ImageView) mStartButton;
                if (mCurrentState == CURRENT_STATE_PLAYING) {
                    imageView.setImageResource(R.drawable.video_click_pause_selector);
                } else if (mCurrentState == CURRENT_STATE_ERROR) {
                    imageView.setImageResource(R.drawable.video_click_play_selector);
                } else {
                    imageView.setImageResource(R.drawable.video_click_play_selector);
                }
            }
        }else {
            super.updateStartImage();
        }
    }


}

