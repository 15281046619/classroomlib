package com.xinwang.shoppingcenter.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xinwang.bgqbaselib.adapter.BaseLoadMoreAdapter;
import com.xinwang.bgqbaselib.utils.CommentUtils;
import com.xinwang.bgqbaselib.utils.Constants;
import com.xinwang.bgqbaselib.utils.CountUtil;
import com.xinwang.bgqbaselib.utils.GlideUtils;
import com.xinwang.bgqbaselib.utils.TimeUtil;
import com.xinwang.shoppingcenter.R;
import com.xinwang.shoppingcenter.ShoppingCenterLibUtils;
import com.xinwang.shoppingcenter.bean.GoodsBean;
import com.xinwang.shoppingcenter.bean.OrderListBean;
import com.xinwang.shoppingcenter.interfaces.AdapterItemClickListener;
import com.xinwang.shoppingcenter.interfaces.OrderButtonListener;

import java.util.List;

/**
 * Date:2021/3/19
 * Time;14:05
 * author:baiguiqiang
 */
public class ShoppingOrderListAdapter extends BaseLoadMoreAdapter<OrderListBean.DataBean.OrdersBean> {
    private OrderButtonListener orderButtonListener;

    public ShoppingOrderListAdapter(List<OrderListBean.DataBean.OrdersBean> mDatas) {
        super(mDatas);
    }

    @Override
    protected void onBaseBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof BaseViewHolder){
            BaseViewHolder baseViewHolder = (BaseViewHolder) viewHolder;
            GlideUtils.loadAvatar(TextUtils.isEmpty(mDatas.get(i).getItems().get(0).getSku().getCover())?mDatas.get(i).getItems().get(0).getGoods().getCover()
                    :mDatas.get(i).getItems().get(0).getSku().getCover(),R.color.BGPressedClassRoom,baseViewHolder.icCove);
            if (mDatas.get(i).getCreate_time()==0){
                baseViewHolder.tvTime.setText("");
            }else
                baseViewHolder.tvTime.setText(TimeUtil.getYMDHMS1(mDatas.get(i).getCreate_time()+""));
            if (mDatas.get(i).getPay_state()== Constants.PAY_STATE_NO&&mDatas.get(i).getCancel_state()==1){//未支付并且未取消
                baseViewHolder.tvState.setText("已下单");
                baseViewHolder.btCancel.setVisibility(View.VISIBLE);
                baseViewHolder.btPay.setVisibility(View.VISIBLE);
                baseViewHolder.btCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (orderButtonListener!=null)
                            orderButtonListener.onCancel(i);
                    }
                });
                baseViewHolder.btPay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (orderButtonListener!=null)
                            orderButtonListener.onPay(baseViewHolder.tvTitle.getText().toString(),i);
                    }
                });
            }else if (!TextUtils.isEmpty(mDatas.get(i).getWaybill_no())){
                baseViewHolder.tvState.setText("已发货");
                baseViewHolder.btCancel.setVisibility(View.VISIBLE);
                baseViewHolder.btPay.setVisibility(View.VISIBLE);
                baseViewHolder.btCancel.setText("查看物流");
                baseViewHolder.btPay.setText("确认收货");
                baseViewHolder.btCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (orderButtonListener!=null)
                            orderButtonListener.onCancel(i);
                    }
                });
                baseViewHolder.btPay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (orderButtonListener!=null)
                            orderButtonListener.onPay(baseViewHolder.tvTitle.getText().toString(),i);
                    }
                });
            }else {
                if (mDatas.get(i).getCancel_state()==2)
                    baseViewHolder.tvState.setText("已取消");
                else if(mDatas.get(i).getCancel_state()==3)
                    baseViewHolder.tvState.setText("取消中");
                baseViewHolder.btCancel.setVisibility(View.GONE);
                baseViewHolder.btPay.setVisibility(View.GONE);
            }



            int sum=0;
            int price=0;
            StringBuffer title=new StringBuffer();
            for(int j=0;j<mDatas.get(i).getItems().size();j++){
                sum+=mDatas.get(i).getItems().get(j).getNum();
                price+=mDatas.get(i).getItems().get(j).getNum()*mDatas.get(i).getItems().get(j).getPrice();
                if (j==0){
                    title.append(mDatas.get(i).getItems().get(j).getGoods().getTitle());
                }else {
                    if (j==1) {
                        if (title.indexOf(mDatas.get(i).getItems().get(j).getGoods().getTitle())==-1) {
                            title.append("、");
                            title.append(mDatas.get(i).getItems().get(j).getGoods().getTitle());
                        }
                    }else if (j==2){
                        title.append("...");
                    }
                }

            }
            baseViewHolder.tvTitle.setText(title+" 共"+sum+"件商品");
            if (mDatas.get(i).getPrice()==0){
                mDatas.get(i).setPrice(price);
            }
            baseViewHolder.tvPrice.setText(ShoppingCenterLibUtils.getPriceSpannable("￥"+ CountUtil.changeF2Y(mDatas.get(i).getPrice())));
        }
    }
    public void setOnClickButtonListener(OrderButtonListener orderButtonListener){
        this.orderButtonListener = orderButtonListener;
    }

    @Override
    public RecyclerView.ViewHolder onBaseCreateViewHolder(View view, int viewType) {
        return new BaseViewHolder(view);
    }
    class BaseViewHolder extends RecyclerView.ViewHolder {
        ImageView icCove;

        TextView tvTitle,tvPrice,tvState,tvTime,tvPriceName;
        TextView btPay,btCancel;
        BaseViewHolder(@NonNull View itemView) {
            super(itemView);
            icCove = itemView.findViewById(R.id.icCove);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvState = itemView.findViewById(R.id.tvState);
            btPay = itemView.findViewById(R.id.btPay);
            btCancel = itemView.findViewById(R.id.btCancel);
            tvPriceName = itemView.findViewById(R.id.tvPriceName);
        }
    }
    @Override
    public int getViewLayout(int viewType) {
        return R.layout.item_shopping_order_list_shoppingcenter;
    }


}
