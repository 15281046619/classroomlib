package com.xingwang.classroom.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xingwang.classroom.R;
import com.xingwang.classroom.utils.GlideUtils;
import com.xingwang.classroom.view.gift.GiftBean;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Date:2020/9/17
 * Time;13:32
 * author:baiguiqiang
 */
public class BottomGifDialog extends BaseDialog {
    private Callback1<Integer> callback1;
    private  TextView tvJF;
    private TextView btSend;
    private List<GiftBean>  maps =new ArrayList<>();
    private int curPos =0;
    @Override
    protected int layoutResId() {
        return R.layout.dialog_bottom_gif;
    }
    public static BottomGifDialog getInstance(List<GiftBean> mData,int curJF){
        BottomGifDialog instance = new BottomGifDialog();
        Bundle bundle = new Bundle();
        bundle.putSerializable("data", (Serializable) mData);
        bundle.putString("sum",curJF+"");
        instance.setArguments(bundle);
        return instance;
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Dialog dialog = super.onCreateDialog(savedInstanceState);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.gravity = Gravity.BOTTOM;
        layoutParams.windowAnimations = R.style.BottomSheetStyleClassRoom;
        window.setAttributes(layoutParams);
        return dialog;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        tvJF = findViewById(R.id.tvJF);
        tvJF.setText(getArguments().getString("sum"));
        maps= (List<GiftBean>) getArguments().getSerializable("data");
        GridView gradView = findViewById(R.id.gradView);

        ItemAdapter itemAdapter= new ItemAdapter();
        gradView.setAdapter(itemAdapter);
        gradView.setOnItemClickListener((parent, view, position, id) -> {
            curPos =position;
            itemAdapter.notifyDataSetChanged();
        });
        findViewById(R.id.ivClose).setOnClickListener(v -> dismissDialog());
        btSend =     findViewById(R.id.bt_send);
        btSend.setOnClickListener(v -> {
            if (callback1!=null){
                btSend.setEnabled(false);
                callback1.run(curPos);
            }
        });
    }

    /**
     * 礼物送出成功
     * @param curJFSum 当前选中的积分
     */
    public void sendGiftSuccess(int curJFSum){
        tvJF.setText(curJFSum+"");
        btSend.setEnabled(true);
    }
    public void setGiftFailure(){
        btSend.setEnabled(true);
    }

    public void setCallback(Callback1<Integer> callback1 ){
        this.callback1 = callback1;
    }
    public  interface Callback1<T> {
        void run(T t);
    }

    private class ItemAdapter extends BaseAdapter {



        @Override
        public int getCount() {
            return maps.size();
        }



        @Override
        public Object getItem(int position) {
            return null;
        }


        @Override
        public long getItemId(int position) {
            return position;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder ;
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_bottom_gift_classroom, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.tvName = (TextView) convertView.findViewById(R.id.tvName);
                viewHolder.rlRoot = (LinearLayout) convertView.findViewById(R.id.rlRoot);
                viewHolder.tvSum = (TextView) convertView.findViewById(R.id.tvSum);
                viewHolder.ivIcon = (ImageView) convertView.findViewById(R.id.ivIcon);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.tvName.setText(maps.get(position).getGiftName());
            viewHolder.tvSum.setText(maps.get(position).getGiftPrice()+"积分");
            GlideUtils.loadAvatar(maps.get(position).getGiftImgLoc(),viewHolder.ivIcon);
            if (curPos ==position){
                viewHolder.rlRoot.setBackgroundResource(R.drawable.shape_select_item_gift_classroom);
            }else {
                viewHolder.rlRoot.setBackgroundResource(R.color.GIftItemBgClassRoom);
            }
            //viewHolder.rlRoot.setSelected(curPos ==position);
            return convertView;
        }
        class ViewHolder {
            ImageView ivIcon;
            LinearLayout rlRoot;
            TextView tvName;
            TextView tvSum;

        }

    }
}
