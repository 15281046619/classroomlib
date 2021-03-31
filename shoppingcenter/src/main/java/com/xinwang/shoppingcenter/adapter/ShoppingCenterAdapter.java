package com.xinwang.shoppingcenter.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.xinwang.bgqbaselib.adapter.BaseLoadMoreAdapter;
import com.xinwang.bgqbaselib.utils.GlideUtils;
import com.xinwang.bgqbaselib.utils.GsonUtils;
import com.xinwang.bgqbaselib.utils.SharedPreferenceUntils;
import com.xinwang.shoppingcenter.R;
import com.xinwang.shoppingcenter.bean.GoodsBean;
import com.xinwang.shoppingcenter.interfaces.AdapterItemClickListener;


import java.util.List;

/**
 * Date:2021/3/19
 * Time;14:05
 * author:baiguiqiang
 */
public class ShoppingCenterAdapter extends BaseLoadMoreAdapter<GoodsBean.DataBean> {
    private AdapterItemClickListener adapterItemClickListener;

    public ShoppingCenterAdapter(List<GoodsBean.DataBean> mDatas) {
        super(mDatas);
        setLoadState(2);//加载完成 不显示footer
    }

    /**
     * 全选获取取消
     */
    public void allCheck(Context context,boolean isCheck){
        for (int i=0;i<mDatas.size();i++){
            mDatas.get(i).setCheck(isCheck);
        }
        saveUpdate(context);
    }
    /**
     * 是不是全选
     * @return
     */
    public boolean isAllCheck(){
        for (int i=0;i<mDatas.size();i++){
            if (!mDatas.get(i).isCheck()){
                return false;
            }
        }
        return true;
    }

    /**
     * 保存更新adaptr
     * @param context
     * @return
     */
    public void saveUpdate(Context context){
        SharedPreferenceUntils.saveGoods(context, GsonUtils.createGsonString(mDatas));
        notifyDataSetChanged();
    }

    @Override
    protected void onBaseBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof BaseViewHolder){
            BaseViewHolder baseViewHolder = (BaseViewHolder) viewHolder;
            baseViewHolder.rbCheck .setChecked(mDatas.get(i).isCheck());
            baseViewHolder.tvTitle.setText(mDatas.get(i).getTitle());
            baseViewHolder.tvAdd.setOnClickListener(v -> {
                mDatas.get(i).setAddSum(mDatas.get(i).getAddSum()+1);
                saveUpdate(baseViewHolder.tvSub.getContext());
            });
            baseViewHolder.tvSum.setText(String.valueOf(mDatas.get(i).getAddSum()));
            baseViewHolder.tvSub.setOnClickListener(v -> {
                if (mDatas.get(i).getAddSum()>1){
                    mDatas.get(i).setAddSum(mDatas.get(i).getAddSum()-1);
                    saveUpdate(baseViewHolder.tvSub.getContext());
                }

            });
            GlideUtils.loadAvatar(mDatas.get(i).getCover(),R.color.BGPressedClassRoom,baseViewHolder.icCove);
            baseViewHolder.rbCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    mDatas.get(i).setCheck(isChecked);
                    SharedPreferenceUntils.saveGoods(baseViewHolder.tvSub.getContext(),GsonUtils.createGsonString(mDatas));
                    if (adapterItemClickListener!=null)
                        adapterItemClickListener.onClick(i,buttonView);

                }
            });
        }
    }
    public void setOnClickListener(AdapterItemClickListener adapterItemClickListener){
        this.adapterItemClickListener = adapterItemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onBaseCreateViewHolder(View view, int viewType) {
        return new BaseViewHolder(view);
    }
    class BaseViewHolder extends RecyclerView.ViewHolder {
        CheckBox rbCheck;
        ImageView icCove;
        TextView tvTitle,tvSub,tvSum,tvAdd;
        BaseViewHolder(@NonNull View itemView) {
            super(itemView);
            rbCheck =itemView.findViewById(R.id.rbCheck);
            icCove =itemView.findViewById(R.id.icCove);
            tvTitle =itemView.findViewById(R.id.tvTitle);
            tvSub =itemView.findViewById(R.id.tvSub);
            tvSum =itemView.findViewById(R.id.tvSum);
            tvAdd =itemView.findViewById(R.id.tvAdd);
        }
    }
    @Override
    public int getViewLayout(int viewType) {
        return R.layout.item_shopping_center_shoppingcenter;
    }


}
