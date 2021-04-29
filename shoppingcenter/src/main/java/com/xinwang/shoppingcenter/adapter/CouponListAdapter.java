package com.xinwang.shoppingcenter.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
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
    private Double aDouble;
    public CouponListAdapter(List<CouponBean.DataBean.CouponsBean> mDatas,Double mCurPrice) {
        super(mDatas);
        aDouble =mCurPrice;
    }
    public Double getCurPrice(){
        return aDouble;
    }
    @Override
    protected void onBaseBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof BaseViewHolder ){
            BaseViewHolder mViewHolder = (BaseViewHolder) viewHolder;
            mViewHolder.ivSelect.setSelected(i==curPos);
            mViewHolder.tvDes.setText(mDatas.get(i).getInstructions());
            mViewHolder.tvTitle.setText(mDatas.get(i).getName());
            mViewHolder.tvPrice.setText(ShoppingCenterLibUtils.getPriceSpannable("￥"+ CountUtil.doubleToString(CountUtil.divide(mDatas.get(i).getFee(),100D))));
            mViewHolder.tvTime.setText("有效期至"+ TimeUtil.getYMD(mDatas.get(i).getExpire_at()+""));
            if (mDatas.get(i).getMin_money()>0)
                mViewHolder.tvMinPrice.setText("满"+CountUtil.doubleToString(mDatas.get(i).getMin_money())+"可用");
            else
                mViewHolder.tvMinPrice.setText("");
            if (aDouble>=mDatas.get(i).getMin_money()){
                mViewHolder.viewIsUser.setVisibility(View.VISIBLE);
            }else {
                mViewHolder.viewIsUser.setVisibility(View.GONE);
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
        TextView tvDes,tvPrice,tvTitle,tvTime,tvMinPrice;
        View viewIsUser;


        BaseViewHolder(@NonNull View itemView) {
            super(itemView);
            ivSelect = itemView.findViewById(R.id.ivSelect);
            tvDes= itemView.findViewById(R.id.tvDes);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvMinPrice = itemView.findViewById(R.id.tvMinPrice);
            tvTime = itemView.findViewById(R.id.tvTime);
            viewIsUser = itemView.findViewById(R.id.viewIsUser);
        }
    }
}
