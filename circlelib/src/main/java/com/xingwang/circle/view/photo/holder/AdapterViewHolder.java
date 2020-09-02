package com.xingwang.circle.view.photo.holder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xingwang.circle.view.photo.adapter.ViewHolderHelper;

public class AdapterViewHolder {
    protected View mConvertView;
    protected ViewHolderHelper mViewHolderHelper;

    private AdapterViewHolder(ViewGroup parent, int layoutId) {
        mConvertView = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
        mConvertView.setTag(this);
        mViewHolderHelper = new ViewHolderHelper(parent, mConvertView);
    }

    /**
     * 拿到一个可重用的ViewHolder对象
     *
     * @param convertView
     * @param parent
     * @param layoutId
     * @return
     */
    public static AdapterViewHolder dequeueReusableAdapterViewHolder(View convertView, ViewGroup parent, int layoutId) {
        if (convertView == null) {
            return new AdapterViewHolder(parent, layoutId);
        }
        return (AdapterViewHolder) convertView.getTag();
    }

    public ViewHolderHelper getViewHolderHelper() {
        return mViewHolderHelper;
    }

    public View getConvertView() {
        return mConvertView;
    }
}
