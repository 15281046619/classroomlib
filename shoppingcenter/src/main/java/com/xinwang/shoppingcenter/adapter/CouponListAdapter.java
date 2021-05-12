package com.xinwang.shoppingcenter.adapter;

import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xinwang.bgqbaselib.adapter.BaseLoadMoreAdapter;
import com.xinwang.bgqbaselib.utils.CommentUtils;
import com.xinwang.bgqbaselib.utils.CountUtil;
import com.xinwang.bgqbaselib.utils.TimeUtil;
import com.xinwang.shoppingcenter.R;
import com.xinwang.shoppingcenter.ShoppingCenterLibUtils;
import com.xinwang.shoppingcenter.bean.CouponBean;

import java.util.List;

/**
 * Date:2021/4/15
 * Time;14:06
 * author:baiguiqiang
 */
public class CouponListAdapter extends BaseLoadMoreAdapter<CouponBean.DataBean.CouponsBean> {
    public int curPos =-1;//选中pos

    public CouponListAdapter(List<CouponBean.DataBean.CouponsBean> mDatas) {
        super(mDatas);

    }

    @Override
    protected void onBaseBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof BaseViewHolder ){
            BaseViewHolder mViewHolder = (BaseViewHolder) viewHolder;

            mViewHolder.ivSelect.setSelected(i==curPos);

            mViewHolder.tvTitle.setText(mDatas.get(i).getName());
            mViewHolder.tvPrice.setText(ShoppingCenterLibUtils.getPriceSpannable("￥"+ CountUtil.doubleToString(CountUtil.changeF2Y(mDatas.get(i).getFee()))));
            mViewHolder.tvTime.setText("有效期至"+ TimeUtil.getYMD(mDatas.get(i).getExpire_at()+""));
            if (mDatas.get(i).getMin_money()>0)
                mViewHolder.tvMinPrice.setText("满"+CountUtil.changeF2Y(mDatas.get(i).getMin_money())+"可用");
            else
                mViewHolder.tvMinPrice.setText("");
            mViewHolder.tvDes.setText(mDatas.get(i).getInstructions());
            if (TextUtils.isEmpty(mDatas.get(i).getNoUseCause())){
                mViewHolder.llNoUse.setVisibility(View.GONE);
                mViewHolder.tvTitle.setTextColor(ContextCompat.getColor(mViewHolder.tvTitle.getContext(),android.R.color.black));
                mViewHolder.tvTime.setTextColor(ContextCompat.getColor(mViewHolder.tvTitle.getContext(),R.color.textColorClassRoom));
                mViewHolder.tvDes.setTextColor(ContextCompat.getColor(mViewHolder.tvTitle.getContext(),R.color.textColorClassRoom));
                mViewHolder.tvPrice.setTextColor(ContextCompat.getColor(mViewHolder.tvTitle.getContext(),R.color.themeClassRoom));
                mViewHolder.tvMinPrice.setTextColor(ContextCompat.getColor(mViewHolder.tvTitle.getContext(),R.color.themeClassRoom));
                mViewHolder.ivSelect.setImageResource(R.drawable.select_order_checkbutton_shoppingcenter);
            }else {
                mViewHolder.tvTitle.setTextColor(ContextCompat.getColor(mViewHolder.tvTitle.getContext(),R.color.LineClass1Room));
                mViewHolder.tvTime.setTextColor(ContextCompat.getColor(mViewHolder.tvTitle.getContext(),R.color.LineClass1Room));
                mViewHolder.tvDes.setTextColor(ContextCompat.getColor(mViewHolder.tvTitle.getContext(),R.color.LineClass1Room));
                mViewHolder.tvPrice.setTextColor(ContextCompat.getColor(mViewHolder.tvTitle.getContext(),R.color.theme2ClassRoom));
                mViewHolder.tvMinPrice.setTextColor(ContextCompat.getColor(mViewHolder.tvTitle.getContext(),R.color.theme2ClassRoom));
                mViewHolder.ivSelect.setImageResource(R.drawable.ic_baseline_radio_button_unchecked_24_nouse_shoppingcenter);
                mViewHolder.llNoUse.setVisibility(View.VISIBLE);
                mViewHolder.tvNoUse.setText(mDatas.get(i).getNoUseCause());
            }
        }
    }

    @Override
    public RecyclerView.ViewHolder onBaseCreateViewHolder(View view, int viewType) {
        return new BaseViewHolder(view);
    }

    @Override
    public int getViewLayout(int viewType) {
        return R.layout.item_coupon_shoppingcenter;
    }
    class BaseViewHolder extends RecyclerView.ViewHolder {
        ImageView ivSelect;
        TextView tvDes,tvPrice,tvTitle,tvTime,tvMinPrice,tvNoUse;
        LinearLayout llNoUse;


        BaseViewHolder(@NonNull View itemView) {
            super(itemView);
            ivSelect = itemView.findViewById(R.id.ivSelect);
            tvDes= itemView.findViewById(R.id.tvDes);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvMinPrice = itemView.findViewById(R.id.tvMinPrice);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvNoUse = itemView.findViewById(R.id.tvNoUse);
            llNoUse = itemView.findViewById(R.id.llNoUse);

        }
    }
}
