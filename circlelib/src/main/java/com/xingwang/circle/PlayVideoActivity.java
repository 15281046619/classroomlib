package com.xingwang.circle;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.view.View;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.xingwang.circle.base.BaseActivity;
import com.xingwang.circle.view.MyJzvd;
import com.xingwang.circle.view.MyJzvdStd;
import com.xingwang.swip.utils.Constants;
import com.xingwang.swip.utils.ActivityManager;

import cn.jzvd.JZMediaInterface;
import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;

public class PlayVideoActivity extends BaseActivity {

    protected JzvdStd jzt_video;
    private String videoUrl;

    public static Intent getIntent(Context context, String videoPath) {
        Intent intent = new Intent(context, PlayVideoActivity.class);
        intent.putExtra(Constants.INTENT_DATA, videoPath);
        context.startActivity(intent);
        return intent;
    }

    @Override
    public int getLayoutId() {
        return R.layout.circle_activity_play_video;
    }

    @Override
    protected void initView() {
        jzt_video=findViewById(R.id.jzt_video);
    }

    @Override
    public void initData() {
        videoUrl =  getIntent().getStringExtra(Constants.INTENT_DATA);

        jzt_video.backButton.setOnClickListener(v -> ActivityManager.getInstance().finishActivity());
        jzt_video.batteryLevel.setVisibility(View.GONE);
        jzt_video.fullscreenButton.setVisibility(View.GONE);

       // GlideUtils.loadPic(cardBody.getCover(),jzt_video.thumbImageView,PlayVideoActivity.this);

        if (EmptyUtils.isNotEmpty(videoUrl)){
            jzt_video.setUp(videoUrl,"", Jzvd.SCREEN_NORMAL);
            jzt_video.setScreenFullscreen();
        }else {
            ToastUtils.showShort("视频地址获取失败！");
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Jzvd.FULLSCREEN_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
        Jzvd.NORMAL_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
    }

    @Override
    protected void onPause() {
        super.onPause();
        Jzvd.resetAllVideos();
        //Change these two variables back
        Jzvd.FULLSCREEN_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE;
        Jzvd.NORMAL_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
