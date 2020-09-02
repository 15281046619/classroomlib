package com.xingwang.circle.view;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class KeyBordHelper {
    private Activity activity;
    private OnKeyBoardStatusChangeListener onKeyBoardStatusChangeListener;
    private int screenHeight; // 空白高度 = 屏幕高度 - 当前 Activity 的可见区域的高度
    // 当 blankHeight 不为 0 即为软键盘高度。
    private int blankHeight = 0;
    private int statusBarHeight = 0;
    public KeyBordHelper(Activity activity) {
        this.activity = activity;
        screenHeight = activity.getResources().getDisplayMetrics().heightPixels;
        //activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        if (activity.getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        //获取status_bar_height资源的ID
        int resourceId =activity.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            //根据资源ID获取响应的尺寸值
            statusBarHeight =activity.getResources().getDimensionPixelSize(resourceId);
        }

    }
    /**
     * 创建的时候添加布局监听
     */
    public void onCreate() {
        View content = activity.findViewById(android.R.id.content);
        // content.addOnLayoutChangeListener(listener); 这个方法有时会出现一些问题
        content.getViewTreeObserver().addOnGlobalLayoutListener(onGlobalLayoutListener);
    }

    /**
     * 销毁的时候移除布局监听
     */
    public void onDestroy() {
        View content = activity.findViewById(android.R.id.content);
        content.getViewTreeObserver().removeOnGlobalLayoutListener(onGlobalLayoutListener);
    }

    /**
     * 实现OnGlobalLayoutListener
     */
    private ViewTreeObserver.OnGlobalLayoutListener onGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override public void onGlobalLayout() {
            Rect rect = new Rect();
            activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
            int newBlankHeight = screenHeight - rect.bottom;
            if (newBlankHeight != blankHeight) {
                if (newBlankHeight > blankHeight) {
                    // keyboard pop
                    if (onKeyBoardStatusChangeListener != null) {
                        onKeyBoardStatusChangeListener.OnKeyBoardChanged(true,newBlankHeight);
                    }
                } else {
                    // newBlankHeight < blankHeight // keyboard close
                    if (onKeyBoardStatusChangeListener != null) {
                        onKeyBoardStatusChangeListener.OnKeyBoardChanged(false,blankHeight);
                    }
                }
            }
            blankHeight = newBlankHeight;
        }
    };

    /**
     * 设置监听器
     * @param onKeyBoardStatusChangeListener onKeyBoardStatusChangeListener
     */
    public void setOnKeyBoardStatusChangeListener( OnKeyBoardStatusChangeListener onKeyBoardStatusChangeListener) {
        this.onKeyBoardStatusChangeListener = onKeyBoardStatusChangeListener;
    }

    /**
     * 键盘变化的监听
     */
    public interface OnKeyBoardStatusChangeListener {
        void OnKeyBoardChanged(boolean visible,int keyBoardHeight);
    }

    public void openInputMethodManager(EditText etContent){
        if (activity!=null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(INPUT_METHOD_SERVICE);
            imm.showSoftInput(etContent, InputMethodManager.SHOW_IMPLICIT);
        }
    }
    public void closeInputMethodManager() {
        if (activity != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(INPUT_METHOD_SERVICE);
            View v = activity.getWindow().peekDecorView();
            if (null != v) {
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        }
    }
    /**
     * 滚动view
     * @param parent  parent
     * @param child child
     * @param isVisible isVisible
     */
    public  void scrollView(ViewGroup parent, View child, boolean isVisible){
        Rect rect = new  Rect();
        parent.getLocalVisibleRect(rect);
        if (isVisible) {
            int[] location =  new int[2];
            child.getLocationInWindow(location);
            if (location[1]-child.getHeight() -rect.bottom>0){
                int scrollHeight = location[1]-child.getHeight() -rect.bottom+statusBarHeight;
                parent.scrollTo(0, scrollHeight);
            }

        } else {
            parent.scrollTo(0, 0);
        }

    }

}
