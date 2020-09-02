package com.xingwang.swip.title;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.StringUtils;
import com.xingwang.swip.R;


/**
 */

public class TopTitleView extends RelativeLayout implements View.OnClickListener {
    private View mMainLayout;
    private Activity hostActivity;
    private RelativeLayout mTitle_container;
    private RelativeLayout mBack_layout;
    private ImageView mLeft_icon;
    private TextView mTitle_tv;
    private LinearLayout mRight_first;
    private ImageView mFirst_img;
    private TextView mFirst_tv;
    private LinearLayout mRight_second;
    private ImageView mSecond_img;
    /**
     * 定义标题栏弹窗按钮
     */
    public TitlePopup titlePopup;

    private OnBackClickListener onBackClickListener;
    private OnRightFirstClickListener onRightFirstClickListener;
    private TitlePopup.OnItemOnClickListener onItemOnClickListener;

    public interface OnBackClickListener {

        void onBackClickListener();
    }

    public interface OnRightFirstClickListener {
        void onRightFirstClickListener(View v);
    }


    public TopTitleView(@NonNull Context context) {
        this(context, null);
    }

    public TopTitleView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }


    private void init(AttributeSet attrs) {
        mMainLayout = inflate(getContext(), R.layout.swip_view_top_title, this);

        mTitle_container = (RelativeLayout) findViewById(R.id.title_container);
        mBack_layout = (RelativeLayout) findViewById(R.id.back_layout);
        mLeft_icon = (ImageView) findViewById(R.id.left_icon);
        mTitle_tv = (TextView) findViewById(R.id.title_tv);
        mRight_first = (LinearLayout) findViewById(R.id.right_first);
        mFirst_img = (ImageView) findViewById(R.id.first_img);
        mFirst_tv = (TextView) findViewById(R.id.first_tv);
        mRight_second = (LinearLayout) findViewById(R.id.right_second);
        mSecond_img = (ImageView) findViewById(R.id.second_img);
        mBack_layout.setOnClickListener(this);
        mRight_first.setOnClickListener(this);
        mRight_second.setOnClickListener(this);


        setupViews(attrs);
        hostActivity = getHostActivity(getContext());
        initPopData();
    }

    private void setupViews(AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.swip_topTitle);
        try {
            setTitleBackground(a.getColor(
                    R.styleable.swip_topTitle_title_background_color,
                    ContextCompat.getColor(getContext(), R.color.reslib_colorWindowContentBalck)));
            setTitleText(a.getString(R.styleable.swip_topTitle_title_text));
            setTitleSize(a.getDimension(R.styleable.swip_topTitle_title_size, 20));
            setTitleColor(a.getColor(R.styleable.swip_topTitle_title_color, ContextCompat.getColor(getContext(), R.color.swip_white)));
            setLeftButtonBackground(a.getResourceId(R.styleable.swip_topTitle_left_button_background, R.drawable.swip_bg_title_img));
            int right_first_img = a.getResourceId(R.styleable.swip_topTitle_right_first_img, 0);
            String right_first_tv = a.getString(R.styleable.swip_topTitle_right_first_tv);
            setRightFirst(right_first_img, right_first_tv);
            setRightSecondImg(a.getResourceId(R.styleable.swip_topTitle_right_second_img, 0));
            isShowSeondImg(a.getBoolean(R.styleable.swip_topTitle_is_show_right_second, true));
            isShowBack(a.getBoolean(R.styleable.swip_topTitle_title_is_show_back, true));
        } finally {
            a.recycle();
        }
    }

    private void isShowBack(boolean aBoolean) {
        if (aBoolean) {
            mBack_layout.setVisibility(VISIBLE);
        } else {
            mBack_layout.setVisibility(GONE);
        }
    }

    /**
     * 右侧第二个按钮是否显示
     *
     * @param aBoolean
     */
    public void isShowSeondImg(boolean aBoolean) {
        if (aBoolean) {
            mRight_second.setVisibility(VISIBLE);
        } else {
            mRight_second.setVisibility(GONE);
        }
    }

    private void setRightSecondImg(int resourceId) {
        if (resourceId != 0) {
            mSecond_img.setImageResource(resourceId);
        }
    }

    private void setRightFirst(int rightFirstImg, String rightFirstTv) {
        if (rightFirstImg != 0) {
            mRight_first.setVisibility(VISIBLE);
            mFirst_tv.setVisibility(GONE);
            mFirst_img.setVisibility(VISIBLE);
            mFirst_img.setImageResource(rightFirstImg);
        } else if (!TextUtils.isEmpty(rightFirstTv)) {
            mRight_first.setVisibility(VISIBLE);
            mFirst_tv.setVisibility(VISIBLE);
            mFirst_img.setVisibility(GONE);
            mFirst_tv.setText(rightFirstTv);
        } else {
            mRight_first.setVisibility(GONE);
        }
    }

    /**
     * 设置右侧第一个按钮为文字
     *
     * @param right_first_tv
     * @param onRightFirstClickListener
     */
    public void setRightFristText(String right_first_tv, OnRightFirstClickListener onRightFirstClickListener) {
        this.onRightFirstClickListener = onRightFirstClickListener;
        if (!TextUtils.isEmpty(right_first_tv)) {
            mRight_first.setVisibility(VISIBLE);
            mFirst_tv.setVisibility(VISIBLE);
            mFirst_img.setVisibility(GONE);
            mFirst_tv.setText(right_first_tv);
        } else {
            mRight_first.setVisibility(GONE);
        }
    }

    /**
     * 设置右侧第一个按钮为文字
     *
     * @param right_first_tv
     */
    public void setRightFristText(String right_first_tv) {
        if (!StringUtils.isEmpty(right_first_tv)) {
            mRight_first.setVisibility(VISIBLE);
            mFirst_tv.setVisibility(VISIBLE);
            mFirst_img.setVisibility(GONE);
            mFirst_tv.setText(right_first_tv);
        } else {
            mRight_first.setVisibility(GONE);
        }
    }

    /**
     * 设置右侧第一个按钮为文字大小，颜色
     *
     */
    public void setRightFristTextColor(int color) {
        mRight_first.setVisibility(VISIBLE);
        mFirst_tv.setVisibility(VISIBLE);
        mFirst_img.setVisibility(GONE);
        mFirst_tv.setTextColor(color);
    }

    /**
     * 设置右侧是否可点击
     *
     * @param isClick
     */
    public void setRightFristClick(boolean isClick) {
        mRight_first.setClickable(isClick);
    }
    /**
     * 右侧是否可点击
     *
     */
    public boolean isRightFristClickable( ) {
        return mRight_first.isClickable();
    }

    /**
     * 设置右侧是否可见
     *
     * @param visible
     */
    public void setRightFristVisible(int visible) {
        mRight_first.setVisibility(visible);
    }

    /**
     * 设置右侧是否显示的状态
     *
     * @param status
     */
    public void setRightFristStatus(int status) {
        mRight_first.setVisibility(status);
    }
    public void setRightFristEnable(boolean isEnable){
        mRight_first.setEnabled(isEnable);
        mFirst_tv.setEnabled(isEnable);
    }
    public void setRightFristImg(int right_first_img, OnRightFirstClickListener onRightFirstClickListener) {
        this.onRightFirstClickListener = onRightFirstClickListener;
        if (right_first_img != 0) {
            mRight_first.setVisibility(VISIBLE);
            mFirst_tv.setVisibility(GONE);
            mFirst_img.setVisibility(VISIBLE);
            mFirst_img.setImageResource(right_first_img);
        } else {
            mRight_first.setVisibility(GONE);
        }
    }

    /**
     * 设置右侧第一个按钮的点击事件
     *
     * @param onRightFirstClickListener
     */
    public void setOnRightFirstClickListener(OnRightFirstClickListener onRightFirstClickListener) {
        this.onRightFirstClickListener = onRightFirstClickListener;
    }

    /**
     * 设置title颜色
     *
     * @param color
     */
    public void setTitleColor(int color) {
        mTitle_tv.setTextColor(color);
    }

    /**
     * 设置左边按钮背景
     *
     * @param resourceId
     */
    public void setLeftButtonBackground(int resourceId) {
        mLeft_icon.setBackgroundResource(resourceId);
    }

    /**
     * 设置title内容
     *
     * @param titleSize
     */
    public void setTitleSize(float titleSize) {
        mTitle_tv.setTextSize(titleSize);
    }

    /**
     * 设置title内容
     *
     * @param titleText
     */
    public void setTitleText(String titleText) {
        String title = "";
        if (!TextUtils.isEmpty(titleText)) {
            if (titleText.length() > 12) {
                title = titleText.substring(0, 12) + "...";
            } else {
                title = titleText;
            }
        }
        mTitle_tv.setText(title);
    }


    /**
     * 设置背景颜色
     *
     * @param color
     */
    public void setTitleBackground(int color) {
        mTitle_container.setBackgroundColor(color);
    }

    /**
     * 设置点击返回按钮事件
     *
     * @param onBackClickListener
     */
    public void setOnBackClickListener(OnBackClickListener onBackClickListener) {
        this.onBackClickListener = onBackClickListener;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.back_layout) {
            if (onBackClickListener != null) {
                onBackClickListener.onBackClickListener();
            } else {
                if (hostActivity != null) {
                    hostActivity.finish();
                }
            }

        } else if (id == R.id.right_first) {
            if (onRightFirstClickListener != null) {
                onRightFirstClickListener.onRightFirstClickListener(v);
            }
        } else if (id == R.id.right_second) {
            if (hostActivity != null) {
                setBackgroundAlpha(hostActivity, 0.5f);
            }
            titlePopup.show(v);
        }
    }
    public OnRightFirstClickListener getListener(){
        return onRightFirstClickListener;
    }
    private Activity getHostActivity(Context context) {
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (Activity) context;
            }
            context = ((ContextWrapper) context).getBaseContext();
        }
        return null;
    }

    /**
     * 初始化数据
     */
    private void initPopData() {
        // 实例化标题栏弹窗
        titlePopup = new TitlePopup(getContext(), LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        // 给标题栏弹窗添加子类
     /*   titlePopup.addAction(new ActionItem(getContext(), "首页",
                R.drawable.title_home_page));*/

        titlePopup.setItemOnClickListener(new TitlePopup.OnItemOnClickListener() {
            @Override
            public void onItemClick(ActionItem item, int position) {
                if (onItemOnClickListener != null) {
                    onItemOnClickListener.onItemClick(item, position);
                }
            }
        });

        titlePopup.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                if (hostActivity != null) {
                    setBackgroundAlpha(hostActivity, 1f);
                }
            }
        });
    }

    /**
     * 添加点击菜单项
     *
     * @param actionItem
     */
    public void addPopData(ActionItem actionItem) {
        if (titlePopup != null) {
            titlePopup.addAction(actionItem);
        }
    }

    /**
     * 设置更多弹窗的点击回调
     *
     * @param onItemOnClickListener
     */
    public void setOnItemOnClickListener(TitlePopup.OnItemOnClickListener onItemOnClickListener) {
        this.onItemOnClickListener = onItemOnClickListener;
    }


    /**
     * 设置页面的透明度
     *
     * @param bgAlpha 1表示不透明
     */
    private void setBackgroundAlpha(Activity activity, float bgAlpha) {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = bgAlpha;
        if (bgAlpha == 1) {
            //不移除该Flag的话,在有视频的页面上的视频会出现黑屏的bug
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        } else {
            //此行代码主要是解决在华为手机上半透明效果无效的bug
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        }
        activity.getWindow().setAttributes(lp);
    }
}
