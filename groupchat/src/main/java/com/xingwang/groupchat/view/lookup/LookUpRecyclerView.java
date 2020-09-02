package com.xingwang.groupchat.view.lookup;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.xingwang.groupchat.R;
import com.xingwang.groupchat.utils.CommentUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 使用方法
 * 1.initLoadData(data) data;list<LookUpBean>  实体类继承LookUpBean。
 * 2.initShow(adapter) adapter:LeftAdapter 继承LeftAdapter实现抽象方法。 leftAdapter实现显示首字母显示
 *
 */
public class LookUpRecyclerView extends CoordinatorLayout implements View.OnTouchListener, TextWatcher {
    private EditText etContent;
    private RecyclerView rcvRight;
    private RecyclerView rcvLeft;
    private TextView tvCenter;
    private RightAdapter mRightAdapter;
    private List<LookUpBean> mDatas =new ArrayList<>();
    private List<LookUpBean> mSelectDatas = new ArrayList<>();
    private LeftAdapter mLeftAdapter;
    private WrapContentLinearLayoutManager mLayoutManager;

    public LookUpRecyclerView(Context context) {
        super(context);
        init();
    }

    public LookUpRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LookUpRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    private void init(){
        View rootView = LayoutInflater.from(getContext()).inflate(R.layout.groupchat_layout_lookup_root,this);
        etContent= rootView.findViewById(R.id.et_content);
        rcvRight= rootView.findViewById(R.id.rcv_right);
        rcvLeft= rootView.findViewById(R.id.rcv_left);
        tvCenter= rootView.findViewById(R.id.tv_center);
        initRecyclerView();
        initData();
        initListener();
    }

    private void initData() {
        mRightAdapter = new RightAdapter(getContext());
        rcvRight.setAdapter(mRightAdapter);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {  //把操作放在用户点击的时候
            View v = ((Activity)getContext()).getCurrentFocus();      //得到当前页面的焦点,ps:有输入框的页面焦点一般会被输入框占据

            if (!isShouldHideKeyboard(v, ev)) { //判断用户点击的是否是输入框以外的区域
                CommentUtils.closeSoftKeyboard((Activity) getContext());
            }
        }
        return super.dispatchTouchEvent(ev);
    }
    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时则不能隐藏
     *
     * @param v 该view
     * @param event MotionEvent
     * @return 是否隐藏
     */
    private Boolean isShouldHideKeyboard(View v, MotionEvent event){
        if (v!=null)
            if ( v instanceof EditText) {  //判断得到的焦点控件是否包含EditText
                int[] l = new int[]{0, 0};
                v.getLocationInWindow(l);
                int left = l[0];
                //得到输入框在屏幕中上下左右的位置
                int top = l[1];
                int bottom = top + v.getHeight();
                int right = left + v.getWidth();
                return !(event.getX() > left && event.getX() < right
                        && event.getY() > top && event.getY() < bottom);
            }
        // 如果焦点不是EditText则忽略
        return false;
    }
    @SuppressLint("ClickableViewAccessibility")
    private void initListener(){

        rcvRight.setOnTouchListener(this);
        rcvLeft.setOnTouchListener(this);
        etContent.addTextChangedListener(this);
        rcvLeft.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (mLayoutManager!=null){
                    int position =mLayoutManager.findFirstVisibleItemPosition();
                    if (position>=0&&mLeftAdapter.getData().get(position).isPeak()) {
                        String mHeadStr = mLeftAdapter.getData().get(position).getHeadStr();
                        mRightAdapter.textToPos(mHeadStr);
                    }
                }
            }
        });
    }
    private void initRecyclerView() {
        rcvRight.setLayoutManager( new WrapContentLinearLayoutManager(getContext()));
        mLayoutManager =new WrapContentLinearLayoutManager(getContext());
        rcvLeft.setLayoutManager( mLayoutManager);

    }


    /**
     * 初始化显示的数据集合
     * 最好异步处理
     * @param mDatas 数据集
     */
    public void initLoadData(List<LookUpBean> mDatas){
        this.mDatas=mDatas;
        dataSort(mDatas);
    }

    /**
     * 初始化显示列表
     * @param mLeftAdapter adapter
     */
    public void initShow(LeftAdapter mLeftAdapter){
        if (this.mLeftAdapter==null){
            this.mLeftAdapter = mLeftAdapter;
            rcvLeft.setAdapter(mLeftAdapter);
            etContent.setHint("搜索"+mDatas.size()+"位成员");
        }
    }

    private void dataSort(List<LookUpBean> mDatas){
        Collections.sort(mDatas, (o1, o2) -> {
            if (o1.getHeadStr().equals(o2.getHeadStr())) {
                return 0;
            } else {
                return o1.getHeadStr().compareTo(o2.getHeadStr());
            }
        });
        String cutStr="";
        for (LookUpBean mData:mDatas){
            if (!mData.getHeadStr().equals(cutStr)){
                cutStr =mData.getHeadStr();
                mData.setPeak(true);
            }else {
                mData.setPeak(false);
            }
        }
    }
    private Handler mHandler =new Handler(Looper.getMainLooper());
    private Runnable mRunnable = () ->{
        if (tvCenter!=null)
            tvCenter.setVisibility(GONE);};

    private void handlerLeave(){
        mHandler.removeCallbacks(mRunnable);
        mHandler.postDelayed(mRunnable,2000);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        rcvLeft.requestFocus();
        if (v.getId()== R.id.rcv_right) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_UP:
                    handlerLeave();
                    rcvRight.setBackground(null);
                    break;
                case MotionEvent.ACTION_DOWN:

                    notifyRightAdapter(event.getY());
                    mHandler.removeCallbacks(mRunnable);
                    tvCenter.setVisibility(VISIBLE);
                    rcvRight.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.groupchat_select_lookup_right));
                    break;
                case MotionEvent.ACTION_MOVE:
                    notifyRightAdapter(event.getY());
                    break;
                case MotionEvent.ACTION_CANCEL:
                    handlerLeave();
                    rcvRight.setBackground(null);
                    break;
            }
            return true;
        }
        return false;
    }
    private void notifyRightAdapter(float y){
        int rightPos = (int) (y/mRightAdapter.getItemHeight());
        if (rightPos>=0&&rightPos<mRightAdapter.getItemCount()) {
            tvCenter.setText(mRightAdapter.getItemContent(rightPos));
            notifyLeftAdapter(rightPos);
        }
    }
    private void notifyLeftAdapter(int rightPos){
        for (int i=0; i<mDatas.size();i++){
            if (mDatas.get(i).getHeadStr().equals(mRightAdapter.getItemContent(rightPos))){
                //rcvLeft.scrollToPosition(i);
                mLayoutManager.scrollToPositionWithOffset(i,0);
                mRightAdapter.notifyItem(rightPos);
                break;
            }
        }

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (mDatas==null||mLeftAdapter==null){
            return;
        }
        if (TextUtils.isEmpty(etContent.getText().toString().trim())){
            dataSort(mDatas);
            mLeftAdapter.setData(mDatas);
            mLeftAdapter.notifyDataSetChanged();
        }else {
            lookUpText(etContent.getText().toString());
        }
    }

    /**
     * 查找输入文字
     * @param toString 查找文字
     */
    private void lookUpText(String toString) {
        mSelectDatas.clear();
        for (int i=0;i<mDatas.size();i++){
            if (mDatas.get(i).getLookUpKey().contains(toString)){
                mSelectDatas.add(mDatas.get(i));
            }
        }
        dataSort(mSelectDatas);
        mLeftAdapter.setData(mSelectDatas);
        mLeftAdapter.notifyDataSetChanged();
    }
}
