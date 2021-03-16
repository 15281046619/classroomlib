package com.xingwang.classroom.view.gift;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xingwang.classroom.R;
import com.xinwang.bgqbaselib.utils.GlideUtils;
import com.xingwang.classroom.view.CircularImage;

import java.util.ArrayList;
import java.util.List;


/**
 * Date:2020/9/12
 * Time;9:51
 * author:baiguiqiang
 */
public class GiftView extends LinearLayout{
    private RelativeLayout rlRoot1,rlRoot2;
    private CircularImage ivHead1,ivHead2;
    private TextView tvName1,tvName2,tvGiftName1,tvGiftName2,tvSum1,tvSum2;
    private ImageView ivGiftImg1,ivGiftImg2;
    private Handler wsMainHandler = new Handler(Looper.getMainLooper());

    private List<GiftBean> mData =new ArrayList<>();//所有要显示的数据
    private GiftBean mBean1 ;//正在显示1
    private GiftBean mBean2 ;//正在显示2
    public  List<GiftBean> mAllGift =new ArrayList<>();
    public GiftView(Context context) {
        this(context,null);
    }

    public GiftView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public GiftView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(LinearLayout.VERTICAL);
        inflate(context,R.layout.item_gift_view, this);
        rlRoot2 =findViewById(R.id.rlRoot2);
        rlRoot1 =findViewById(R.id.rlRoot1);
        ivHead1 =findViewById(R.id.ivHead1);
        ivHead2 =findViewById(R.id.ivHead2);
        tvName1 =findViewById(R.id.tvName1);
        tvName2 =findViewById(R.id.tvName2);
        tvGiftName1 =findViewById(R.id.tvGiftName1);
        tvGiftName2 =findViewById(R.id.tvGiftName2);
        ivGiftImg1 =findViewById(R.id.ivGiftImg1);
        ivGiftImg2 =findViewById(R.id.ivGiftImg2);
        tvSum1 =findViewById(R.id.tvSum1);
        tvSum2 =findViewById(R.id.tvSum2);

    }
    public void addData(GiftBean mBean){

        if (!indexBeanEqual(mBean)) {
            synchronized (this) {
                mData.add(mBean);
            }
            if (mBean1 == null) {
                wsMainHandler.post(() -> showContent1());
            } else if (mBean2 == null) {
                wsMainHandler.post(() ->showContent2());
            }
        }
    }

    private void getGiftLists(){
        mAllGift.add(new GiftBean("","","","爱心","0","",R.mipmap.ic_ax_classroom,"1",0));
        mAllGift.add(new GiftBean("","","","自行车","1","",R.mipmap.ic_zxc_classroom,"5",0));
        mAllGift.add(new GiftBean("","","","小轿车","2","",R.mipmap.ic_xjc_classroom,"10",0));
        mAllGift.add(new GiftBean("","","","跑车","3","",R.mipmap.ic_pc_classroom,"20",0));
        mAllGift.add(new GiftBean("","","","飞机","4","",R.mipmap.ic_fj_classroom,"50",0));
        mAllGift.add(new GiftBean("","","","火箭","5","",R.mipmap.ic_hj_classroom,"100",0));
        mAllGift.add(new GiftBean("","","","油轮","6","",R.mipmap.ic_ylun_classroom,"200",0));
        mAllGift.add(new GiftBean("","","","航母","7","",R.mipmap.ic_hm_classroom,"500",0));
    }
    public List<GiftBean> getAllGift(){
        if (mAllGift.size()==0)
            getGiftLists();
        return mAllGift;
    }
    private void showContent1() {
        if (mData.get(0)==mBean2){
            if (mData.size()>=2) {
                mBean1 = mData.get(1);
            }else {
                rlRoot1.setVisibility(View.INVISIBLE);
                return;
            }
        }else {
            mBean1 = mData.get(0);
        }
        rlRoot1.setVisibility(View.VISIBLE);
        GlideUtils.loadAvatar(mBean1.getAvatar(), ivHead1);
        // GlideUtils.loadAvatar(mBean1.getGiftImg(), ivGiftImg1);
        // GlideUtils.loadAvatar(mBean1.getGiftImgLoc(), ivGiftImg1);
        ivGiftImg1.setImageResource(mBean1.getGiftImgLoc());
        tvName1.setText(mBean1.getName());
        tvGiftName1.setText("送出 "+mBean1.getGiftName());
        tvSum1.setText("1");
        showAnimator(rlRoot1);
    }

    private void showContent2(){
        if (mData.get(0)==mBean1){
            if (mData.size()>=2) {
                mBean2 = mData.get(1);
            }else {
                rlRoot2.setVisibility(View.INVISIBLE);
                return;
            }
        }else {
            mBean2 =mData.get(0);
        }

        rlRoot2.setVisibility(View.VISIBLE);
        GlideUtils.loadAvatar(mBean2.getAvatar(),ivHead2);
        //  GlideUtils.loadAvatar(mBean2.getGiftImg(), ivGiftImg2);

        ivGiftImg2.setImageResource(mBean2.getGiftImgLoc());
        tvName2.setText(mBean2.getName());
        tvGiftName2.setText("送出 "+mBean2.getGiftName());
        tvSum2.setText("1");
        showAnimator(rlRoot2);
    }
    private AnimatorSet animatorSetAlpha;
    private   ValueAnimator aAnimatorAlpha;
    private void showAnimator(View view){
        animatorSetAlpha = new AnimatorSet();//组合动画
        aAnimatorAlpha = ObjectAnimator.ofFloat(view,"alpha",0f,1f);
        ValueAnimator bAnimator = ObjectAnimator.ofFloat(view,"translationX",-getMeasuredWidth(),0);
        animatorSetAlpha.setDuration(400);
        animatorSetAlpha.setInterpolator(new DecelerateInterpolator());
        animatorSetAlpha.play(bAnimator).with(aAnimatorAlpha);//两个动画同时开始
        animatorSetAlpha.start();
        aAnimatorAlpha.addListener(new Animator.AnimatorListener(){

            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (view.getId()==R.id.rlRoot1) {
                    translationAnimatorEnd(tvSum1);
                }else {
                    translationAnimatorEnd(tvSum2);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }
    private void translationAnimatorEnd(View view){
        if (view.getId()==R.id.tvSum1){
            if (mBean1.getSum()<=Integer.parseInt(tvSum1.getText().toString())){//显示完这条数据
                synchronized (this) {
                    mData.remove(mBean1);
                    HideAlphaAnimator(rlRoot1);
                }
            }else {
                tvSum1.setText(String.valueOf(Integer.parseInt(tvSum1.getText().toString())+1));
                showScaleSumAnimator(tvSum1);
            }
        }else {
            if (mBean2.getSum()==Integer.parseInt(tvSum2.getText().toString())){//显示完这条数据
                synchronized (this) {
                    mData.remove(mBean2);
                    HideAlphaAnimator(rlRoot2);
                }
            }else {
                tvSum2.setText(String.valueOf(Integer.parseInt(tvSum2.getText().toString())+1));
                showScaleSumAnimator(tvSum2);
            }
        }
    }
    private ValueAnimator aAnimatorClose;
    /**
     * 隐藏动画
     */
    private void HideAlphaAnimator(View view){
        aAnimatorClose = ObjectAnimator.ofFloat(view,"alpha",1f,0f);
        aAnimatorClose.setDuration(1200);
        aAnimatorClose.setInterpolator(new AccelerateInterpolator());
        aAnimatorClose.start();
        aAnimatorClose.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (view.getId()==R.id.rlRoot1) {
                    synchronized (this) {
                        mBean1 = null;
                        if (mData.size() == 0 || (mBean2 != null && mData.size() == 1)) {
                            rlRoot1.setVisibility(INVISIBLE);
                        } else {
                            showContent1();
                        }
                    }

                }else {
                    synchronized (this) {
                        mBean2 = null;
                        if (mData.size() == 0||(mBean1!=null&&mData.size()==1)) {
                            rlRoot2.setVisibility(INVISIBLE);
                        } else {
                            showContent2();
                        }
                    }
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }
    private  AnimatorSet animatorSetScale;
    private  ValueAnimator animatorScale;
    private void showScaleSumAnimator(TextView tvSum1) {
        animatorSetScale = new AnimatorSet();//组合动画
        tvSum1.setPivotX(0);
        tvSum1.setPivotY(tvSum1.getMeasuredHeight());// 变换中心点
        animatorScale = ObjectAnimator.ofFloat(tvSum1,"scaleX",1f,1.8f,1f);
        ValueAnimator aAnimatorb= ObjectAnimator.ofFloat(tvSum1,"scaleY",1f,1.8f,1f);

        animatorSetScale.setDuration(300);
        animatorSetScale.play(animatorScale).with(aAnimatorb);
        animatorSetScale.start();
        animatorScale.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                translationAnimatorEnd(tvSum1);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

    }


    /**
     * 同一个人同一个礼物合并数据
     * @param mBean
     */
    private boolean indexBeanEqual(GiftBean mBean){
        synchronized (this) {
            for (int i = 0; i < mData.size(); i++) {
                if (mData.get(i).getGiftId().equals(mBean.getGiftId()) && mData.get(i).getUserId().equals(mBean.getUserId())) {
                    if (mData.get(i).getSum() + mBean.getSum()>99){
                        return false;
                    }else {
                        mData.get(i).setSum(mData.get(i).getSum() + mBean.getSum());
                        return true;
                    }
                }
            }
            return false;
        }
    }
    public void onDestroy(){
        if (animatorScale!=null) {
            animatorScale.removeAllListeners();
            animatorScale.cancel();

        }
        if (aAnimatorClose!=null){
            aAnimatorClose.removeAllListeners();
            aAnimatorClose.cancel();
        }
        if (aAnimatorAlpha!=null){
            aAnimatorAlpha.removeAllListeners();
            aAnimatorAlpha.cancel();
        }
        if (animatorSetScale!=null){
            animatorSetScale.cancel();
            animatorSetScale=null;
        }
        if (animatorSetAlpha!=null){
            animatorSetAlpha.cancel();
            animatorSetAlpha=null;
        }
    }
}
