package com.xingwang.swip.title;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.blankj.utilcode.util.ScreenUtils;
import com.xingwang.swip.R;

import java.util.ArrayList;

/**
 * @author yangyu 功能描述：标题按钮上的弹窗（继承自PopupWindow）
 */
public class TitlePopup extends PopupWindow {
    private Context mContext;

    /**
     * 列表弹窗的间隔
     */
    private final int LIST_PADDING = 10;

    /**
     * 实例化一个矩形
     */
    private Rect mRect = new Rect();

    /**
     * 坐标的位置（x、y）
     */
    private final int[] mLocation = new int[2];

    /**
     * 屏幕的宽度和高度
     */
    private int mScreenWidth, mScreenHeight;

    /**
     * 判断是否需要添加或更新列表子类项
     */
    private boolean mIsDirty;

    /**
     * 位置不在中心
     */
    private int popupGravity = Gravity.NO_GRAVITY;

    /**
     * 弹窗子类项选中时的监听
     */
    private OnItemOnClickListener mItemOnClickListener;

    /**
     * 定义列表对象
     */
    private ListView mListView;

    /**
     * 定义弹窗子类项列表
     */
    private ArrayList<ActionItem> mActionItems = new ArrayList<ActionItem>();

    public TitlePopup(Context context) {
        // 设置布局的参数
        this(context, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }

    @SuppressWarnings("deprecation")
    public TitlePopup(Context context, int width, int height) {
        this.mContext = context;

        // 设置可以获得焦点
        setFocusable(true);
        // 设置弹窗内可点击
        setTouchable(true);
        // 设置弹窗外可点击
        setOutsideTouchable(true);

        // 获得屏幕的宽度和高度
//        try {
            mScreenWidth = ScreenUtils.getScreenWidth();
            mScreenHeight = ScreenUtils.getScreenHeight();
//        }catch (Exception  e){
//            mScreenWidth = 1024;
//            mScreenHeight = 768;
//        }
        // 设置弹窗的宽度和高度
        setWidth(width);
        setHeight(height);

        setBackgroundDrawable(new BitmapDrawable());

        // 设置弹窗的布局界面
        setContentView(LayoutInflater.from(mContext).inflate(
                R.layout.swip_view_title_popup, null));

        initUI();
    }

    /**
     * 初始化弹窗列表
     */
    private void initUI() {
        mListView = (ListView) getContentView().findViewById(R.id.title_list);

        mListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int index,
                                    long arg3) {
                // 点击子类项后，弹窗消失
                dismiss();
                if (mItemOnClickListener != null) {
                    mItemOnClickListener.onItemClick(mActionItems.get(index),
                            index);
                }
            }
        });
    }

    /**
     * 显示弹窗列表界面
     *
     * @param view
     */
    public void show(View view) {
        // 获得点击屏幕的位置坐标
        view.getLocationOnScreen(mLocation);

        // 设置矩形的大小
        mRect.set(mLocation[0], mLocation[1], mLocation[0] + view.getWidth(),
                mLocation[1] + view.getHeight());

        // 判断是否需要添加或更新列表子类项
        if (mIsDirty) {
            populateActions();
        }

        // 显示弹窗的位置
        showAtLocation(view, popupGravity, mScreenWidth - LIST_PADDING
                - (getWidth() / 2), mRect.bottom);
    }

    /**
     * 设置弹窗列表子项
     */
    private void populateActions() {
        mIsDirty = false;

        // 设置列表的适配器
        mListView.setAdapter(new MyAdapter());
    }

    /**
     * 添加子类项
     *
     * @param action
     */
    public void addAction(ActionItem action) {
        if (action != null) {
            mActionItems.add(0, action);
           // mIsDirty = true;
            populateActions();
        }
    }

    /**
     * 清除子类项
     */
    public void cleanAction() {
        if (mActionItems.isEmpty()) {
            mActionItems.clear();
            mIsDirty = true;
        }
    }

    public void cleanAction(String s) {
        if (!mActionItems.isEmpty()) {
            for (int i = 0; i < mActionItems.size(); i++) {
                if (mActionItems.get(i).mTitle.equals(s)) {
                    mActionItems.remove(mActionItems.get(i));
                    break;
                }
            }
           // mIsDirty = true;
            populateActions();
        }
    }

    /**
     * 根据位置得到子类项
     */
    public ActionItem getAction(int position) {
        if (position < 0 || position > mActionItems.size())
            return null;
        return mActionItems.get(position);
    }

    /**
     * 设置监听事件
     */
    public void setItemOnClickListener(
            OnItemOnClickListener onItemOnClickListener) {
        this.mItemOnClickListener = onItemOnClickListener;
    }

    /**
     * @author yangyu 功能描述：弹窗子类项按钮监听事件
     */
    public static interface OnItemOnClickListener {
        public void onItemClick(ActionItem item, int position);

    }


    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mActionItems == null ? 0 : mActionItems.size();
        }


        @Override
        public Object getItem(int position) {
            return mActionItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Holder holder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.swip_view_title_popuo_item, null);
                holder = new Holder();
                holder.ivItem = (ImageView) convertView.findViewById(R.id.iv_menu_item);
                holder.tvItem = (TextView) convertView.findViewById(R.id.tv_menu_item);
                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }
            holder.ivItem.setImageDrawable(mActionItems.get(position).mDrawable);
            holder.tvItem.setText(mActionItems.get(position).mTitle);
            return convertView;
        }

        class Holder {
            ImageView ivItem;
            TextView tvItem;
        }
    }
}
