package com.xingwang.swip;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.SoundEffectConstants;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;


/**
 * 状态帧布局
 *
 * @author Alex
 */
@SuppressWarnings("all")
public class StateFrameLayout extends FrameLayout {

    public static final int STATE_NORMAL = 0;// 普通
    public static final int STATE_LOADING = 1;// 载入
    public static final int STATE_ERROR = 2;// 错误
    public static final int STATE_EMPTY = 3;// 空白
    private int mState;
    private boolean mAlwaysDrawChild;
    private OnStateClickListener mClickListener;
    private int mLoadingLayoutId;
    private int mErrorLayoutId;
    private int mEmptyLayoutId;
    private View mLoadingView;
    private View mErrorView;
    private View mEmptyView;

    public StateFrameLayout(Context context) {
        super(context);
        initView(context, null);
    }

    public StateFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public StateFrameLayout(Context context, AttributeSet attrs,
                            int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    @TargetApi(21)
    public StateFrameLayout(Context context, AttributeSet attrs, int defStyleAttr,
                            int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        setWillNotDraw(false);
        setClickable(true);
        TypedArray custom = context.obtainStyledAttributes(attrs, R.styleable.swip_stateFrameLayout);
        boolean alwaysDrawChild = custom.getBoolean(
                R.styleable.swip_stateFrameLayout_sflAlwaysDrawChild, false);
        int state = custom.getInt(R.styleable.swip_stateFrameLayout_sflState, STATE_NORMAL);
        mLoadingLayoutId = custom.getResourceId(R.styleable.swip_stateFrameLayout_sflLoadingLayout,
                NO_ID);
        mErrorLayoutId = custom.getResourceId(R.styleable.swip_stateFrameLayout_sflErrorLayout, NO_ID);
        mEmptyLayoutId = custom.getResourceId(R.styleable.swip_stateFrameLayout_sflEmptyLayout, NO_ID);
        custom.recycle();
        setAlwaysDrawChild(alwaysDrawChild);
        setState(state);
    }

    /**
     * Returns a set of layout parameters with a width of
     * {@link ViewGroup.LayoutParams#MATCH_PARENT},
     * and a height of {@link ViewGroup.LayoutParams#MATCH_PARENT}.
     */
    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    // Override to allow type-checking of LayoutParams.
    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams;
    }

    @Override
    protected LayoutParams generateLayoutParams(ViewGroup.LayoutParams lp) {
        if (Build.VERSION.SDK_INT >= 19) {
            if (lp instanceof LayoutParams) {
                return new LayoutParams((LayoutParams) lp);
            } else if (lp instanceof FrameLayout.LayoutParams) {
                return new LayoutParams((FrameLayout.LayoutParams) lp);
            }
        }
        if (lp instanceof MarginLayoutParams) {
            return new LayoutParams((MarginLayoutParams) lp);
        } else {
            return new LayoutParams(lp);
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (mLoadingLayoutId != NO_ID) {
            View vLoading = LayoutInflater.from(getContext()).inflate(
                    mLoadingLayoutId, this, false);
            setLoadingView(vLoading);
        }
        if (mErrorLayoutId != NO_ID) {
            View vError = LayoutInflater.from(getContext()).inflate(
                    mErrorLayoutId, this, false);
            setErrorView(vError);
        }
        if (mEmptyLayoutId != NO_ID) {
            View vEmpty = LayoutInflater.from(getContext()).inflate(
                    mEmptyLayoutId, this, false);
            setEmptyView(vEmpty);
        }
        final int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            LayoutParams lp = (LayoutParams) child.getLayoutParams();
            switch (lp.getState()) {
                default:
                case STATE_NORMAL:
                    break;
                case STATE_LOADING:
                    mLoadingView = child;
                    break;
                case STATE_EMPTY:
                    mEmptyView = child;
                    break;
                case STATE_ERROR:
                    mErrorView = child;
                    break;
            }
        }
        checkViewVisibility();
    }


    @Override
    protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
        if (mAlwaysDrawChild) {
            return super.drawChild(canvas, child, drawingTime);
        }
        switch (mState) {
            default:
            case STATE_NORMAL:
                return child == mLoadingView || child == mErrorView || super.drawChild(canvas, child, drawingTime);
            case STATE_LOADING:
                return child != mLoadingView || super.drawChild(canvas, child, drawingTime);
            case STATE_ERROR:
                return child != mErrorView || super.drawChild(canvas, child, drawingTime);
            case STATE_EMPTY:
                return child != mEmptyView || super.drawChild(canvas, child, drawingTime);
        }
    }


    @Override
    public boolean performClick() {
        final boolean superResult = super.performClick();
        boolean result = false;
        if (mClickListener != null) {
            if (mClickListener instanceof OnAllStateClickListener) {
                OnAllStateClickListener listener = (OnAllStateClickListener) mClickListener;
                switch (mState) {
                    case STATE_NORMAL:
                        if (!superResult)
                            playSoundEffect(SoundEffectConstants.CLICK);
                        listener.onNormalClick(this);
                        result = true;
                        break;
                    case STATE_LOADING:
                        if (!superResult)
                            playSoundEffect(SoundEffectConstants.CLICK);
                        listener.onLoadingClick(this);
                        result = true;
                        break;
                    case STATE_ERROR:
                        if (!superResult)
                            playSoundEffect(SoundEffectConstants.CLICK);
                        listener.onErrorClick(this);
                        result = true;
                        break;
                    case STATE_EMPTY:
                        if (!superResult)
                            playSoundEffect(SoundEffectConstants.CLICK);
                        listener.onEmptyClick(this);
                        result = true;
                        break;
                }
            } else {
                switch (mState) {
                    case STATE_ERROR:
                        if (!superResult)
                            playSoundEffect(SoundEffectConstants.CLICK);
                        mClickListener.onErrorClick(this);
                        result = true;
                        break;
                }
            }
        }
        return superResult || result;
    }


    /**
     * 设置自定义载入View
     *
     * @param loadingView  载入View
     * @param layoutParams 布局方式
     */
    public void setLoadingView(View loadingView, LayoutParams layoutParams) {
        if (mLoadingView == loadingView) {
            return;
        }
        if (mLoadingView != null) {
            removeView(mLoadingView);
            mLoadingView = null;
        }
        if (loadingView != null) {
            mLoadingView = loadingView;
            addView(mLoadingView, layoutParams);
            checkViewVisibility();
        }
    }

    /**
     * 设置自定义载入View
     *
     * @param loadingView 载入View
     */
    public void setLoadingView(View loadingView) {
        if (loadingView == null) {
            setLoadingView(null, null);
            return;
        }
        ViewGroup.LayoutParams lpHas = loadingView.getLayoutParams();
        LayoutParams lp = lpHas == null ? new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT, Gravity.CENTER) :
                generateLayoutParams(lpHas);
        lp.setState(STATE_LOADING);
        setLoadingView(loadingView, lp);
    }

    /**
     * 设置自定义错误View
     *
     * @param errorView    错误View
     * @param layoutParams 布局方式
     */
    public void setErrorView(View errorView, LayoutParams layoutParams) {
        if (mErrorView == errorView) {
            return;
        }
        if (mErrorView != null) {
            removeView(mErrorView);
            mErrorView = null;
        }
        if (errorView != null) {
            mErrorView = errorView;
            addView(mErrorView, layoutParams);
            checkViewVisibility();
        }
    }

    /**
     * 设置自定义错误View
     *
     * @param errorView 错误View
     */
    public void setErrorView(View errorView) {
        if (errorView == null) {
            setErrorView(null, null);
            return;
        }
        ViewGroup.LayoutParams lpHas = errorView.getLayoutParams();
        LayoutParams lp = lpHas == null ? new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT, Gravity.CENTER) :
                generateLayoutParams(lpHas);
        lp.setState(STATE_ERROR);
        setErrorView(errorView, lp);
    }

    /**
     * 设置自定义空白View
     *
     * @param emptyView    空白View
     * @param layoutParams 布局方式
     */
    public void setEmptyView(View emptyView, LayoutParams layoutParams) {
        if (mEmptyView == emptyView) {
            return;
        }
        if (mEmptyView != null) {
            removeView(mEmptyView);
            mEmptyView = null;
        }
        if (emptyView != null) {
            mEmptyView = emptyView;
            addView(mEmptyView, layoutParams);
            checkViewVisibility();
        }
    }

    /**
     * 设置自定义空白View
     *
     * @param emptyView 空白View
     */
    public void setEmptyView(View emptyView) {
        if (emptyView == null) {
            setEmptyView(null, null);
            return;
        }
        ViewGroup.LayoutParams lpHas = emptyView.getLayoutParams();
        LayoutParams lp = lpHas == null ? new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT, Gravity.CENTER) :
                generateLayoutParams(lpHas);
        lp.setState(STATE_EMPTY);
        setEmptyView(emptyView, lp);
    }

    /**
     * 设置状态View
     *
     * @param loading 载入
     * @param error   错误
     * @param empty   空白
     */
    @SuppressWarnings("unused")
    public void setStateViews(View loading, View error, View empty) {
        setLoadingView(loading);
        setErrorView(error);
        setEmptyView(empty);
    }

    private void checkViewVisibility() {
        switch (mState) {
            default:
            case STATE_NORMAL:
                if (mLoadingView != null)
                    mLoadingView.setVisibility(View.GONE);
                if (mErrorView != null)
                    mErrorView.setVisibility(View.GONE);
                if (mEmptyView != null)
                    mEmptyView.setVisibility(View.GONE);
                showAllItems(true);
                break;
            case STATE_LOADING:
                if (mLoadingView != null)
                    mLoadingView.setVisibility(View.VISIBLE);
                if (mErrorView != null)
                    mErrorView.setVisibility(View.GONE);
                if (mEmptyView != null)
                    mEmptyView.setVisibility(View.GONE);
                showAllItems(mAlwaysDrawChild);
                break;
            case STATE_ERROR:
                if (mLoadingView != null)
                    mLoadingView.setVisibility(View.GONE);
                if (mErrorView != null)
                    mErrorView.setVisibility(View.VISIBLE);
                if (mEmptyView != null)
                    mEmptyView.setVisibility(View.GONE);
                showAllItems(mAlwaysDrawChild);
                break;
            case STATE_EMPTY:
                if (mLoadingView != null)
                    mLoadingView.setVisibility(View.GONE);
                if (mErrorView != null)
                    mErrorView.setVisibility(View.GONE);
                if (mEmptyView != null)
                    mEmptyView.setVisibility(View.VISIBLE);
                showAllItems(mAlwaysDrawChild);
                break;
        }
    }

    private void showAllItems(boolean show) {
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (child != mLoadingView && child != mEmptyView && child != mErrorView) {
                child.setVisibility(show ? VISIBLE : INVISIBLE);
            }
        }
    }


    /**
     * 获取状态
     *
     * @return 状态
     */
    public int getState() {
        return mState;
    }

    /**
     * 设置状态
     *
     * @param state 状态
     */
    public void setState(int state) {
        if (mState != state) {
            mState = state;
            invalidateState();
        }
    }

    /**
     * 刷新状态
     */
    public void invalidateState() {
        checkViewVisibility();
        invalidate();
    }

    /**
     * 修改状态为普通
     */
    @SuppressWarnings("unused")
    public void normal() {
        setState(STATE_NORMAL);
    }

    /**
     * 修改状态为载入
     */
    @SuppressWarnings("unused")
    public void loading() {
        setState(STATE_LOADING);
    }

    /**
     * 修改状态为错误
     */
    @SuppressWarnings("unused")
    public void error() {
        setState(STATE_ERROR);
    }

    /**
     * 修改状态为空白
     */
    @SuppressWarnings("unused")
    public void empty() {
        setState(STATE_EMPTY);
    }

    /**
     * 是否始终绘制子项
     *
     * @return 是否始终绘制子项
     */
    public boolean isAlwaysDrawChild() {
        return mAlwaysDrawChild;
    }

    /**
     * 设置是否始终绘制子项
     *
     * @param draw 是否始终绘制子项
     */
    public void setAlwaysDrawChild(boolean draw) {
        mAlwaysDrawChild = draw;
        invalidate();
    }

    /**
     * 状态点击监听
     *
     * @param listener 状态点击监听
     */
    @SuppressWarnings("unused")
    public void setOnStateClickListener(OnStateClickListener listener) {
        mClickListener = listener;
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState ss = new SavedState(superState);
        ss.mState = getState();
        ss.mAlwaysDrawChild = isAlwaysDrawChild();
        return ss;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;
        setAlwaysDrawChild(ss.mAlwaysDrawChild);
        mState = ss.mState;
        invalidateState();
        super.onRestoreInstanceState(ss.getSuperState());
    }

    private static class SavedState extends BaseSavedState {
        private int mState = STATE_NORMAL;
        private boolean mAlwaysDrawChild = false;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            mState = in.readInt();
            mAlwaysDrawChild = in.readInt() == 1;
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(mState);
            out.writeInt(mAlwaysDrawChild ? 1 : 0);
        }

        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }

    /**
     * 状态点击监听
     *
     * @author Alex
     */
    public interface OnAllStateClickListener extends OnStateClickListener {
        void onNormalClick(StateFrameLayout layout);

        void onLoadingClick(StateFrameLayout layout);

        void onEmptyClick(StateFrameLayout layout);
    }

    /**
     * 状态点击监听
     *
     * @author Alex
     */
    public interface OnStateClickListener {
        void onErrorClick(StateFrameLayout layout);
    }

    /**
     * Per-child layout information associated with WrapLayout.
     */
    public static class LayoutParams extends FrameLayout.LayoutParams {

        private int mState = STATE_NORMAL;

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
            int state = STATE_NORMAL;
            TypedArray custom = c.obtainStyledAttributes(attrs, R.styleable.swip_stateFrameLayout_Layout);
            state = custom.getInt(R.styleable.swip_stateFrameLayout_Layout_sflLayout_state, state);
            custom.recycle();
            mState = state;
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(int width, int height, int gravity) {
            super(width, height, gravity);
        }

        public LayoutParams(int width, int height, int gravity, int state) {
            super(width, height, gravity);
            mState = state;
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }

        public LayoutParams(MarginLayoutParams source) {
            super(source);
        }

        @TargetApi(19)
        public LayoutParams(FrameLayout.LayoutParams source) {
            super(source);
        }

        @TargetApi(19)
        public LayoutParams(LayoutParams source) {
            super(source);
            mState = source.mState;
        }

        /**
         * 设置状态
         *
         * @param state 状态
         */
        public void setState(int state) {
            if (mState != state) {
                mState = state;
            }
        }

        /**
         * 获取状态
         *
         * @return 状态
         */
        public int getState() {
            return mState;
        }
    }
}
