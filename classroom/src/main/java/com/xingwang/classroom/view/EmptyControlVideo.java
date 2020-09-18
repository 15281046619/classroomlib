package com.xingwang.classroom.view;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shuyu.gsyvideoplayer.model.GSYVideoModel;
import com.shuyu.gsyvideoplayer.utils.Debuger;
import com.shuyu.gsyvideoplayer.video.ListGSYVideoPlayer;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;
import com.shuyu.gsyvideoplayer.video.base.GSYBaseVideoPlayer;
import com.shuyu.gsyvideoplayer.video.base.GSYVideoPlayer;
import com.xingwang.classroom.R;
import com.xingwang.classroom.ui.LiveDetailActivity;
import com.xingwang.classroom.ui.danmu.BiliDanmukuParser;
import com.xingwang.classroom.ui.danmu.DanamakuAdapter;
import com.xingwang.classroom.utils.GlideUtils;
import com.xingwang.classroom.utils.LogUtil;
import com.xingwang.classroom.utils.MyToast;
import com.xingwang.swip.utils.Constants;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import master.flame.danmaku.controller.IDanmakuView;
import master.flame.danmaku.danmaku.loader.ILoader;
import master.flame.danmaku.danmaku.loader.IllegalDataException;
import master.flame.danmaku.danmaku.loader.android.DanmakuLoaderFactory;
import master.flame.danmaku.danmaku.model.BaseDanmaku;
import master.flame.danmaku.danmaku.model.DanmakuTimer;
import master.flame.danmaku.danmaku.model.Duration;
import master.flame.danmaku.danmaku.model.IDisplayer;
import master.flame.danmaku.danmaku.model.android.DanmakuContext;
import master.flame.danmaku.danmaku.model.android.Danmakus;
import master.flame.danmaku.danmaku.model.android.SpannedCacheStuffer;
import master.flame.danmaku.danmaku.parser.BaseDanmakuParser;
import master.flame.danmaku.danmaku.parser.IDataSource;
import master.flame.danmaku.ui.widget.DanmakuView;
import moe.codeest.enviews.ENDownloadView;

/**
 * 无任何控制ui的播放
 * Created by guoshuyu on 2017/8/6.
 */

public class EmptyControlVideo extends StandardGSYVideoPlayer {
    private boolean isLive = true;
    private boolean isLinkScroll = false;
    private  TextView tvSum;
    private DanmakuView mDanmakuView;
    private ImageView ivDanMu;
    private boolean mDanmaKuShow = true;
    private File mDumakuFile;
    private BaseDanmakuParser mParser;//解析器对象
    private DanmakuContext mDanmakuContext;
    private long mDanmakuStartSeekPosition = -1;
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

    @Override
    protected void showWifiDialog() {
        super.showWifiDialog();
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
        findByIdAll();

        if (sampleVideo != null) {

            //对弹幕设置偏移记录
            sampleVideo.setDanmakuStartSeekPosition(getCurrentPositionWhenPlaying());
            sampleVideo.setDanmaKuShow(getDanmaKuShow());
            onPrepareDanmaku(sampleVideo);
        }
        return sampleVideo;
        // return super.startWindowFullscreen(context, actionBar, statusBar);
    }
    private void findByIdAll(){

        tvSum= findViewById(R.id.tvSum);
        mDanmakuView =findViewById(R.id.danmaku_view);
        setDanmakuView(mDanmakuView);
        ivDanMu= findViewById(R.id.ivDanMu);

        ivDanMu.setVisibility(VISIBLE);

        ivDanMu.setOnClickListener(this);
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
        findByIdAll();
        if (getContext() instanceof LiveDetailActivity)
            showOnlineCount(((LiveDetailActivity)getContext()).onlineCount);
        if (gsyVideoPlayer != null) {
            EmptyControlVideo gsyDanmaVideoPlayer = (EmptyControlVideo) gsyVideoPlayer;
            setDanmaKuShow(gsyDanmaVideoPlayer.getDanmaKuShow());
            if (gsyDanmaVideoPlayer.getDanmakuView() != null &&
                    gsyDanmaVideoPlayer.getDanmakuView().isPrepared()) {
                resolveDanmakuSeek(this, gsyDanmaVideoPlayer.getCurrentPositionWhenPlaying());
                resolveDanmakuShow();
                releaseDanmaku(gsyDanmaVideoPlayer);
            }
        }
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
        findByIdAll();
        //初始化弹幕显示
        initDanmaku();
        ivDanMu.setSelected(!getDanmaKuShow());
        if (context instanceof LiveDetailActivity)
            showOnlineCount(((LiveDetailActivity)context).onlineCount);
    }


    /**
     * 点击开始按钮
     */
    public void clickStartIcon(){
        super.clickStartIcon();
        if (mCurrentState == CURRENT_STATE_PLAYING) {
            danmakuOnResume();
        } else if (mCurrentState == CURRENT_STATE_PAUSE) {
            danmakuOnPause();
        }
    }
    //显示在线人数
    public void showOnlineCount(long count){
        if (count>0){
            tvSum.setVisibility(VISIBLE);
            if (Constants.APP_DBG)
            tvSum.setText(String.valueOf(count * 10 + System.currentTimeMillis() % 10));
            else
                tvSum.setText(String.valueOf(count));
        }else {
            tvSum.setVisibility(GONE);
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

    @Override
    public void onPrepared() {
        super.onPrepared();
        onPrepareDanmaku(this);
    }

    @Override
    public void onVideoPause() {
        super.onVideoPause();
        danmakuOnPause();
    }
    @Override
    public void onVideoResume(boolean isResume) {
        super.onVideoResume(isResume);
        danmakuOnResume();
    }



    @Override
    public void onCompletion() {
        super.onCompletion();
        releaseDanmaku(this);
    }


    @Override
    public void onSeekComplete() {
        super.onSeekComplete();

        if (mProgressBar!=null) {
            int   time = mProgressBar.getProgress() * getDuration() / 100;
            //如果已经初始化过的，直接seek到对于位置
            if (mHadPlay && getDanmakuView() != null && getDanmakuView().isPrepared()) {
                resolveDanmakuSeek(this, time);
            } else if (mHadPlay && getDanmakuView() != null && !getDanmakuView().isPrepared()) {
                //如果没有初始化过的，记录位置等待
                setDanmakuStartSeekPosition(time);
            }
        }
    }
    private void initDanmaku() {
        // 设置最大显示行数
        HashMap<Integer, Integer> maxLinesPair = new HashMap();
        maxLinesPair.put(BaseDanmaku.TYPE_SCROLL_RL, 5); // 滚动弹幕最大显示5行
        // 设置是否禁止重叠
        HashMap<Integer, Boolean> overlappingEnablePair = new HashMap<Integer, Boolean>();
        overlappingEnablePair.put(BaseDanmaku.TYPE_SCROLL_RL, true);
        overlappingEnablePair.put(BaseDanmaku.TYPE_FIX_TOP, true);

        DanamakuAdapter danamakuAdapter = new DanamakuAdapter(mDanmakuView);

        mDanmakuContext = DanmakuContext.create();
        mDanmakuContext.setDanmakuStyle(IDisplayer.DANMAKU_STYLE_STROKEN, 3).setDuplicateMergingEnabled(false).setScrollSpeedFactor(1.2f).setScaleTextSize(1.2f)
                .setCacheStuffer(new SpannedCacheStuffer(), danamakuAdapter) // 图文混排使用SpannedCacheStuffer
                .setMaximumLines(maxLinesPair)
                .preventOverlapping(overlappingEnablePair);

        if (mDanmakuView != null) {
            if (mDumakuFile != null) {
                mParser = createParser(getIsStream(mDumakuFile));
            }

            //todo 这是为了demo效果，实际上需要去掉这个，外部传输文件进来
            mParser = createParser(null);

            mDanmakuView.setCallback(new master.flame.danmaku.controller.DrawHandler.Callback() {
                @Override
                public void updateTimer(DanmakuTimer timer) {
                }

                @Override
                public void drawingFinished() {

                }

                @Override
                public void danmakuShown(BaseDanmaku danmaku) {
                }

                @Override
                public void prepared() {
                    if (getDanmakuView() != null) {
                        getDanmakuView().start();
                        if (getDanmakuStartSeekPosition() != -1) {
                            resolveDanmakuSeek(EmptyControlVideo.this, getDanmakuStartSeekPosition());
                            setDanmakuStartSeekPosition(-1);
                        }
                        resolveDanmakuShow();
                    }
                }
            });
            mDanmakuView.enableDanmakuDrawingCache(true);
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.ivDanMu) {
            mDanmaKuShow = !mDanmaKuShow;
            if (mDanmaKuShow){
                MyToast.myToast(getContext(),"弹幕已打开",0);
            }else {
                MyToast.myToast(getContext(),"弹幕已关闭",0);
            }
            resolveDanmakuShow();
        }
    }

    @Override
    protected void cloneParams(GSYBaseVideoPlayer from, GSYBaseVideoPlayer to) {
        ((EmptyControlVideo) to).mDumakuFile = ((EmptyControlVideo) from).mDumakuFile;
        super.cloneParams(from, to);
    }
    protected void danmakuOnPause() {
        if (mDanmakuView != null && mDanmakuView.isPrepared()) {
            mDanmakuView.pause();
        }
    }

    protected void danmakuOnResume() {
        if (mDanmakuView != null && mDanmakuView.isPrepared() && mDanmakuView.isPaused()) {
            mDanmakuView.resume();
        }
    }

    public void setDanmaKuStream(File is) {
        mDumakuFile = is;
        if (!getDanmakuView().isPrepared()) {
            onPrepareDanmaku((EmptyControlVideo) getCurrentPlayer());
        }
    }
    private InputStream getIsStream(File file) {
        try {
            InputStream instream = new FileInputStream(file);
            InputStreamReader inputreader = new InputStreamReader(instream);
            BufferedReader buffreader = new BufferedReader(inputreader);
            String line;
            StringBuilder sb1 = new StringBuilder();
            sb1.append("<i>");
            //分行读取
            while ((line = buffreader.readLine()) != null) {
                sb1.append(line);
            }
            sb1.append("</i>");
            Log.e("3333333", sb1.toString());
            instream.close();
            return new ByteArrayInputStream(sb1.toString().getBytes());
        } catch (java.io.FileNotFoundException e) {
            Log.d("TestFile", "The File doesn't not exist.");
        } catch (IOException e) {
            Log.d("TestFile", e.getMessage());
        }
        return null;
    }
    /**
     弹幕的显示与关闭
     */
    private void resolveDanmakuShow() {
        post(() -> {
            if (mDanmaKuShow) {
                if (!getDanmakuView().isShown())
                    getDanmakuView().show();
                ivDanMu.setSelected(false);
            } else {
                if (getDanmakuView().isShown()) {
                    getDanmakuView().hide();
                }
                ivDanMu.setSelected(true);
            }
        });
    }
    /**
     开始播放弹幕
     */
    private void onPrepareDanmaku(EmptyControlVideo gsyVideoPlayer) {
        if (gsyVideoPlayer.getDanmakuView() != null && !gsyVideoPlayer.getDanmakuView().isPrepared() && gsyVideoPlayer.getParser() != null) {
            gsyVideoPlayer.getDanmakuView().prepare(gsyVideoPlayer.getParser(),
                    gsyVideoPlayer.getDanmakuContext());
        }
    }
    /**
     弹幕偏移
     */
    private void resolveDanmakuSeek(EmptyControlVideo gsyVideoPlayer, long time) {
        if (mHadPlay && gsyVideoPlayer.getDanmakuView() != null && gsyVideoPlayer.getDanmakuView().isPrepared()) {
            gsyVideoPlayer.getDanmakuView().seekTo(time);
        }
    }

    /**
     创建解析器对象，解析输入流

     @param stream
     @return
     */
    private BaseDanmakuParser createParser(InputStream stream) {

        if (stream == null) {
            return new BaseDanmakuParser() {

                @Override
                protected Danmakus parse() {
                    return new Danmakus();
                }
            };
        }

        ILoader loader = DanmakuLoaderFactory.create(DanmakuLoaderFactory.TAG_BILI);

        try {
            loader.load(stream);
        } catch (IllegalDataException e) {
            e.printStackTrace();
        }
        BaseDanmakuParser parser = new BiliDanmukuParser();
        IDataSource<?> dataSource = loader.getDataSource();
        parser.load(dataSource);
        return parser;

    }
    /**
     释放弹幕控件
     */
    private void releaseDanmaku(EmptyControlVideo danmakuVideoPlayer) {
        if (danmakuVideoPlayer != null && danmakuVideoPlayer.getDanmakuView() != null) {
            Debuger.printfError("release Danmaku!");
            danmakuVideoPlayer.getDanmakuView().release();
        }
    }
    public BaseDanmakuParser getParser() {
        if (mParser == null) {
            if (mDumakuFile != null) {
                mParser = createParser(getIsStream(mDumakuFile));
            }else {
                mParser = createParser(null);
            }
        }
        return mParser;
    }
    public DanmakuContext getDanmakuContext() {
        return mDanmakuContext;
    }

    public IDanmakuView getDanmakuView() {
        return mDanmakuView;
    }
    public void setDanmakuView(DanmakuView danmakuView) {
        mDanmakuView = danmakuView;

    }
    public long getDanmakuStartSeekPosition() {
        return mDanmakuStartSeekPosition;
    }

    public void setDanmakuStartSeekPosition(long danmakuStartSeekPosition) {
        this.mDanmakuStartSeekPosition = danmakuStartSeekPosition;
    }
    public void setDanmaKuShow(boolean danmaKuShow) {
        mDanmaKuShow = danmaKuShow;
    }

    public boolean getDanmaKuShow() {
        return mDanmaKuShow;
    }



    /**
     模拟添加弹幕数据
     */
    public void addDanmaku(boolean isLive,String content,int textColor,byte priority) {
        BaseDanmaku danmaku = mDanmakuContext.mDanmakuFactory.createDanmaku(BaseDanmaku.TYPE_SCROLL_RL);
        if (danmaku == null || mDanmakuView == null) {
            return;
        }
     /*   try {
            LiveDetailActivity mLiveDetailActivity = (LiveDetailActivity) getContext();
            if (mLiveDetailActivity.orientationUtils != null
                    &&mLiveDetailActivity.orientationUtils.getScreenType() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                danmaku.duration=new Duration(5000);
            }else
                danmaku.duration=new Duration(6000);
        }catch (Exception e){
            danmaku.duration=new Duration(6000);
        }*/
        danmaku.duration=new Duration(6000);
        danmaku.text = content;
        danmaku.padding = 10;
        danmaku.priority = priority;  // 可能会被各种过滤器过滤并隐藏显示，所以提高等级
        danmaku.isLive = isLive;

        danmaku.setTime(mDanmakuView.getCurrentTime() + 500);
        danmaku.textSize = 20f * (mParser.getDisplayer().getDensity() - 0.6f);
        danmaku.textColor = textColor;

      /*  danmaku.textShadowColor = Color.WHITE;
        danmaku.borderColor = Color.GREEN;*/
        mDanmakuView.addDanmaku(danmaku);

    }
}
