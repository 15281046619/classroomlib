package com.xinwang.shoppingcenter.view;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ImageView;

import com.shuyu.gsyvideoplayer.utils.NetworkUtils;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

import com.xinwang.shoppingcenter.R;
import com.xinwang.shoppingcenter.ui.SimplePlayerActivity;


/**
 * Created by shuyu on 2016/12/23.
 * CustomGSYVideoPlayer是试验中，建议使用的时�?�使用StandardGSYVideoPlayer
 */
public class EmpLayoutVideo extends StandardGSYVideoPlayer  {




    /**
     * 1.5.0�?始加入，如果�?要不同布�?区分功能，需要重�?
     */
    public EmpLayoutVideo(Context context, Boolean fullFlag) {
        super(context, fullFlag);
    }

    public EmpLayoutVideo(Context context) {
        super(context);
    }

    public EmpLayoutVideo(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    //这个必须配置�?上面的构造才能生�?
    @Override
    public int getLayoutId() {
        if (getContext()==null){
            return R.layout.sample_video_normal_shoppingcenter;
        }
        if (getContext() instanceof SimplePlayerActivity) {
            SimplePlayerActivity classRoomDetailActivity = (SimplePlayerActivity) getContext();

            if (classRoomDetailActivity.orientationUtils == null) {
                return R.layout.sample_video_normal_shoppingcenter;
            }
            if (classRoomDetailActivity.orientationUtils.getScreenType() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                return R.layout.sample_video_land_shoppingcenter;
            }
        }
        return R.layout.sample_video_normal_shoppingcenter;
    }

    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
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
    protected void showWifiDialog() {
        if (!NetworkUtils.isAvailable(mContext)) {
            //Toast.makeText(mContext, getResources().getString(R.string.no_net), Toast.LENGTH_LONG).show();
            startPlayLogic();
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivityContext());
        builder.setMessage(getResources().getString(R.string.tips_not_wifi));
        builder.setPositiveButton(getResources().getString(R.string.tips_not_wifi_confirm), (dialog, which) -> {
            dialog.dismiss();
            startPlayLogic();
        });
        builder.setNegativeButton(getResources().getString(R.string.tips_not_wifi_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
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

