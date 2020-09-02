package com.xingwang.groupchat.view.lookup;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xingwang.groupchat.R;

import java.lang.ref.WeakReference;


public class RightAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private String[] mDatas;

    private int curItemPos=-1;
    private int mSelectColor;
    private int mNoSelectColor;
    private int mItemHeight;

    private myHandler mHandler =new myHandler(this);
    RightAdapter(Context context){
        mDatas= context.getResources().getStringArray(R.array.groupchat_looup_right_array);
        mNoSelectColor=  ContextCompat.getColor(context,R.color.groupchat_text_black);
        mSelectColor= ContextCompat.getColor(context,R.color.groupchat_colorPrimaryDark);
        mItemHeight = context.getResources().getDimensionPixelOffset(R.dimen.groupchat_dp_16);
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View mView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.groupchat_item_lookup_right,viewGroup,false);
        ViewGroup.LayoutParams mLayoutParams = mView.getLayoutParams();
        mLayoutParams.height =mItemHeight;
        mView.setLayoutParams(mLayoutParams);
        return new RightViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        RightViewHolder mViewHolder = (RightViewHolder) viewHolder;
        mViewHolder.tvContent.setText(mDatas[i]);

        if (curItemPos==i){
            mViewHolder.tvContent.setTextColor(mSelectColor);
        }else {
            mViewHolder.tvContent.setTextColor(mNoSelectColor);
        }

    }

    @Override
    public int getItemCount() {
        return mDatas.length;
    }


     void  notifyItem(int position){
        int temp = curItemPos;
        curItemPos =-1;
        notifyItemChanged(temp);
        curItemPos = position;
        notifyItemChanged(curItemPos);
    }
    class RightViewHolder extends RecyclerView.ViewHolder{
        TextView tvContent;
         RightViewHolder(@NonNull View itemView) {
            super(itemView);
             tvContent = itemView.findViewById(R.id.tv_content);
        }
    }
     int getItemHeight(){
        return mItemHeight;
    }
     String getItemContent(int pos){
        if (pos<mDatas.length)
            return mDatas[pos];
        else
            return mDatas[mDatas.length-1];
    }
     void textToPos(String text){
        new Thread(){
            @Override
            public void run() {
                for (int i=0;i<mDatas.length;i++){
                    if (mDatas[i].equals(text)){
                        mHandler.sendEmptyMessage(i);
                        return;
                    }
                }
            }
        }.start();
    }
    private static class  myHandler extends Handler {
        private WeakReference<RightAdapter> mRightAdapters;

        myHandler(RightAdapter mRightAdapter) {
            mRightAdapters = new WeakReference<>(mRightAdapter);

        }
        @Override
        public void handleMessage(Message msg) {
            if (mRightAdapters.get()!=null)
                mRightAdapters.get().notifyItem(msg.what);
        }
    }
}
