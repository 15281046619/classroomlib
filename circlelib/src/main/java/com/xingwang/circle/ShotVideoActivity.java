package com.xingwang.circle;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;

import com.blankj.utilcode.util.ImageUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.cjt2325.cameralibrary.JCameraView;
import com.cjt2325.cameralibrary.listener.ClickListener;
import com.cjt2325.cameralibrary.listener.ErrorListener;
import com.cjt2325.cameralibrary.listener.JCameraListener;
import com.xingwang.circle.base.BaseActivity;
import com.xingwang.circle.view.MyJzvdStd;
import com.xingwang.swip.utils.Constants;
import com.xingwang.swip.utils.ActivityManager;

import java.io.File;

import cn.jzvd.JZUtils;

public class ShotVideoActivity extends BaseActivity {

    private JCameraView jcameraview;
    private String firstFramePath;

    @Override
    public int getLayoutId() {
        return R.layout.circle_activity_shot_video;
    }

    @Override
    protected void initView() {
        jcameraview=findViewById(R.id.jcameraview);
    }

    @Override
    public void initData() {
        //设置视频保存路径

        jcameraview.setSaveVideoPath(Constants.VIDEO_PATH);

        //设置只能录像或只能拍照或两种都可以（默认两种都可以）
        jcameraview.setFeatures(JCameraView.BUTTON_STATE_ONLY_RECORDER);
        jcameraview.setTip("长按摄像");

        //设置视频质量
        jcameraview.setMediaQuality(JCameraView.MEDIA_QUALITY_LOW);

        //JCameraView监听
        jcameraview.setErrorLisenter(new ErrorListener() {
            @Override
            public void onError() {
                //打开Camera失败回调
                runOnUiThread(() -> ToastUtils.showShort("摄像头打开失败，请重试"));

            }

            @Override
            public void AudioPermissionError() {
                //没有录取权限回调
                runOnUiThread(() -> ToastUtils.showShort("未获取设备权限，请设置"));
            }
        });

        jcameraview.setJCameraLisenter(new JCameraListener() {
            @Override
            public void captureSuccess(Bitmap bitmap) {
                //获取图片bitmap
                Log.i("JCameraView", "bitmap = " + bitmap.getWidth());
            }

            @Override
            public void recordSuccess(String url, Bitmap firstFrame) {
                // 返回到播放页面
                firstFramePath=Constants.VIDEO_PATH+File.separator +System.currentTimeMillis()+ ".jpg";
                ImageUtils.save(firstFrame,firstFramePath, Bitmap.CompressFormat.JPEG,true );
                Intent intent = new Intent();
                intent.putExtra(Constants.INTENT_DATA,url);
                intent.putExtra(Constants.INTENT_DATA1,firstFramePath);
                setResult(RESULT_OK, intent);

                ShotVideoActivity.this.finish();
            }
        });
        //左边按钮点击事件
        jcameraview.setLeftClickListener(new ClickListener() {
            @Override
            public void onClick() {
             cancleShotVideo();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (jcameraview!=null)
            jcameraview.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        if (jcameraview!=null)
            jcameraview.onPause();
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
           cancleShotVideo();
            //不执行父类点击事件
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    private void cancleShotVideo(){
        Intent intent = new Intent();
        intent.putExtra(Constants.INTENT_DATA,"");
        setResult(RESULT_OK, intent);
        ActivityManager.getInstance().finishActivity();
    }
}
