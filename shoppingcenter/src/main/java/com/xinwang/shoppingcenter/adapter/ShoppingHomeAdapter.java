package com.xinwang.shoppingcenter.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beautydefinelibrary.BeautyDefine;
import com.xinwang.bgqbaselib.adapter.BaseLoadMoreAdapter;
import com.xinwang.bgqbaselib.utils.CountUtil;
import com.xinwang.bgqbaselib.utils.GlideUtils;
import com.xinwang.bgqbaselib.utils.LogUtil;
import com.xinwang.shoppingcenter.R;
import com.xinwang.shoppingcenter.ShoppingCenterLibUtils;
import com.xinwang.shoppingcenter.bean.GoodsBean;
import com.xinwang.shoppingcenter.interfaces.AdapterItemClickListener;

import java.util.List;

/**
 * Date:2021/3/19
 * Time;14:05
 * author:baiguiqiang
 */
public class ShoppingHomeAdapter extends BaseLoadMoreAdapter<GoodsBean.DataBean> {
    private AdapterItemClickListener adapterItemClickListener;
    private int itemWidth;
    public ShoppingHomeAdapter(List<GoodsBean.DataBean> mDatas, int  itemWidth) {
        super(mDatas);
        this.itemWidth = itemWidth;
    }

    @Override
    protected void onBaseBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof BaseViewHolder){
            BaseViewHolder baseViewHolder = (BaseViewHolder) viewHolder;

            ViewGroup.LayoutParams layoutParams = baseViewHolder.ivContent.getLayoutParams();
            layoutParams.height = (int) (Float.parseFloat(mDatas.get(i).getCoverRate())*itemWidth);
            //LogUtil.i(layoutParams.height+" "+itemWidth);
            baseViewHolder.tvContent.setText(mDatas.get(i).getTitle());
            baseViewHolder.tvClick.setText(mDatas.get(i).getClick()+"人点击");
            baseViewHolder.rlAdd.setOnClickListener(v -> {
                if (adapterItemClickListener!=null){
                    adapterItemClickListener.add(i);
                }
            });
            if (layoutParams.height>0)
                GlideUtils.loadAvatar(BeautyDefine.getThumbUrlDefine().createThumbUrl(itemWidth,layoutParams.height,mDatas.get(i).getCover()),R.color.BGPressedClassRoom,baseViewHolder.ivContent,itemWidth,layoutParams.height);//必须设置宽高防止反复滑动图片显示问题
            else
                GlideUtils.loadAvatar(mDatas.get(i).getCover(),R.color.BGPressedClassRoom,baseViewHolder.ivContent);
            if (mDatas.get(i).getMin_price()==0){
                baseViewHolder.tvClick.setVisibility(View.VISIBLE);
                baseViewHolder.tvPrice.setVisibility(View.GONE);
            }else {
                baseViewHolder.tvPrice.setVisibility(View.VISIBLE);
                baseViewHolder.tvClick.setVisibility(View.GONE);
                baseViewHolder.tvPrice.setText(ShoppingCenterLibUtils.getPriceSpannable("￥"+ CountUtil.changeF2Y(mDatas.get(i).getMin_price())));
            }
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
        ImageView ivContent;
        TextView tvClick;
        TextView tvContent,tvPrice;
        RelativeLayout rlAdd;

        BaseViewHolder(@NonNull View itemView) {
            super(itemView);
            ivContent = itemView.findViewById(R.id.ivContent);
            tvClick = itemView.findViewById(R.id.tvClick);
            tvContent = itemView.findViewById(R.id.tvContent);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            rlAdd = itemView.findViewById(R.id.rlAdd);
        }
    }
    @Override
    public int getViewLayout(int viewType) {
        return R.layout.item_shopping_home_shoppingcenter;
    }


}
