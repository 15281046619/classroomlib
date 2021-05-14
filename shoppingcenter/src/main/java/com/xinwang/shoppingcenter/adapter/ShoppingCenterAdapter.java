package com.xinwang.shoppingcenter.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.xinwang.bgqbaselib.adapter.BaseLoadMoreAdapter;
import com.xinwang.bgqbaselib.sku.bean.Sku;
import com.xinwang.bgqbaselib.sku.bean.SkuAttribute;
import com.xinwang.bgqbaselib.utils.CountUtil;
import com.xinwang.bgqbaselib.utils.GlideUtils;
import com.xinwang.bgqbaselib.utils.GsonUtils;
import com.xinwang.bgqbaselib.utils.MyToast;
import com.xinwang.bgqbaselib.utils.SharedPreferenceUntils;
import com.xinwang.shoppingcenter.R;
import com.xinwang.shoppingcenter.ShoppingCenterLibUtils;
import com.xinwang.shoppingcenter.bean.GoodsBean;
import com.xinwang.shoppingcenter.dialog.CenterEditNumberDialog;
import com.xinwang.shoppingcenter.interfaces.AdapterItemClickListener;


import java.util.List;

/**
 * Date:2021/3/19
 * Time;14:05
 * author:baiguiqiang
 */
public class ShoppingCenterAdapter extends BaseLoadMoreAdapter<Sku> {
    private AdapterItemClickListener adapterItemClickListener;

    public ShoppingCenterAdapter(List<Sku> mDatas) {
        super(mDatas);
        setLoadState(2);//加载完成 不显示footer
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
            baseViewHolder.tvTitle.setText(mDatas.get(i).getGoodTitle());
            baseViewHolder.tvAdd.setOnClickListener(v -> {
                if (mDatas.get(i).getAddSum()<mDatas.get(i).getStockQuantity()) {
                    if (mDatas.get(i).getAddSum()>=mDatas.get(0).getMaxBugSum()){
                        MyToast.myToast(baseViewHolder.tvSub.getContext(),"超出个人限购数目");
                        return;
                    }
                    mDatas.get(i).setAddSum(mDatas.get(i).getAddSum() + 1);
                    saveUpdate(baseViewHolder.tvSub.getContext());
                    if (adapterItemClickListener != null) {
                        adapterItemClickListener.add(i);
                    }
                }else {
                    MyToast.myToast(  baseViewHolder.tvSub.getContext(),"超出库存数目");
                }
            });
            baseViewHolder.tvSum.setText(String.valueOf(mDatas.get(i).getAddSum()));
            baseViewHolder.tvSku.setText("");
            if (mDatas.get(i).getAttributes()!=null&&mDatas.get(i).getAttributes().size()>0) {
                for (SkuAttribute skuAttribute : mDatas.get(i).getAttributes()) {
                    baseViewHolder.tvSku.append(skuAttribute.getValue() + " ");
                }
                baseViewHolder.tvSku.setVisibility(View.VISIBLE);
            }else {
                baseViewHolder.tvSku.setVisibility(View.INVISIBLE);
            }

            if (mDatas.get(i).getSellingPrice()==0)
                baseViewHolder.tvPrice.setText("");
            else
                baseViewHolder.tvPrice.setText(ShoppingCenterLibUtils.getPriceSpannable("￥"+ CountUtil.changeF2Y(mDatas.get(i).getSellingPrice())));
            baseViewHolder.tvSub.setOnClickListener(v -> {
                if (mDatas.get(i).getAddSum()>1){
                    mDatas.get(i).setAddSum(mDatas.get(i).getAddSum()-1);
                    saveUpdate(baseViewHolder.tvSub.getContext());
                    if (adapterItemClickListener!=null){
                        adapterItemClickListener.sub(i);
                    }
                }else {

                    MyToast.myToast(  baseViewHolder.tvSub.getContext(),"数值低于范围");
                }

            });
            baseViewHolder.tvSum.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CenterEditNumberDialog.getInstance(mDatas.get(0).getMaxBugSum(),mDatas.get(i).getStockQuantity(),baseViewHolder.tvSum.getText().toString())
                            .setCallback(new CenterEditNumberDialog.Callback1<Integer>() {
                                @Override
                                public void run(Integer integer) {
                                    mDatas.get(i).setAddSum(integer);
                                    int curNumber =Integer.parseInt(baseViewHolder.tvSum.getText().toString());
                                    if (curNumber!=integer){
                                        int countNumber =integer-curNumber;
                                        int countPrice = integer * mDatas.get(i).getSellingPrice()-curNumber * mDatas.get(i).getSellingPrice() ;
                                        saveUpdate(baseViewHolder.tvSub.getContext());
                                        if (adapterItemClickListener != null) {
                                            adapterItemClickListener.setNumber(i,countNumber,countPrice);
                                        }
                                    }
                                }
                            }).showDialog(((FragmentActivity)baseViewHolder.tvSub.getContext()).getSupportFragmentManager());
                }
            });
            GlideUtils.loadAvatar(mDatas.get(i).getMainImage(),R.color.BGPressedClassRoom,baseViewHolder.icCove);
            baseViewHolder.rbCheck.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDatas.get(i).setCheck(  baseViewHolder.rbCheck.isChecked());
                    SharedPreferenceUntils.saveGoods(baseViewHolder.tvSub.getContext(),GsonUtils.createGsonString(mDatas));
                    if (adapterItemClickListener!=null)
                        adapterItemClickListener.onClick(i,v);
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
        TextView tvTitle,tvSub,tvSum,tvAdd,tvSku,tvPrice;
        BaseViewHolder(@NonNull View itemView) {
            super(itemView);
            rbCheck =itemView.findViewById(R.id.rbCheck);
            icCove =itemView.findViewById(R.id.icCove);
            tvTitle =itemView.findViewById(R.id.tvTitle);
            tvSub =itemView.findViewById(R.id.tvSub);
            tvSum =itemView.findViewById(R.id.tvSum);
            tvAdd =itemView.findViewById(R.id.tvAdd);
            tvPrice =itemView.findViewById(R.id.tvPrice);
            tvSku =itemView.findViewById(R.id.tvSku);
        }
    }
    @Override
    public int getViewLayout(int viewType) {
        return R.layout.item_shopping_center_shoppingcenter;
    }


}
