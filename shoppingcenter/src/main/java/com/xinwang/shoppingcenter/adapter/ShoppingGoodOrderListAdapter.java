package com.xinwang.shoppingcenter.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xinwang.bgqbaselib.adapter.BaseLoadMoreAdapter;
import com.xinwang.bgqbaselib.utils.Constants;
import com.xinwang.bgqbaselib.utils.CountUtil;
import com.xinwang.bgqbaselib.utils.GlideUtils;
import com.xinwang.bgqbaselib.utils.LogUtil;
import com.xinwang.bgqbaselib.utils.TimeUtil;
import com.xinwang.shoppingcenter.R;
import com.xinwang.shoppingcenter.ShoppingCenterLibUtils;
import com.xinwang.shoppingcenter.bean.GoodsBean;
import com.xinwang.shoppingcenter.bean.OrderListBean;
import com.xinwang.shoppingcenter.bean.SkuBean;
import com.xinwang.shoppingcenter.interfaces.OrderButtonListener;

import java.util.List;

/**
 * Date:2021/3/19
 * Time;14:05
 * author:baiguiqiang
 */
public class ShoppingGoodOrderListAdapter extends BaseLoadMoreAdapter<OrderListBean.DataBean.OrdersBean.ItemsBean> {
    private OrderButtonListener orderButtonListener;

    public ShoppingGoodOrderListAdapter(List<OrderListBean.DataBean.OrdersBean.ItemsBean> mDatas) {
        super(mDatas);
    }

    @Override
    protected void onBaseBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof BaseViewHolder){
            BaseViewHolder baseViewHolder = (BaseViewHolder) viewHolder;
            GlideUtils.loadAvatar(TextUtils.isEmpty(mDatas.get(i).getSku().getCover())?mDatas.get(i).getGoods().getCover()
                    :mDatas.get(i).getSku().getCover(),R.color.BGPressedClassRoom,baseViewHolder.icCove);
            if (mDatas.get(i).getPay_state()== Constants.PAY_STATE_YES&&mDatas.get(i).getWaybill_id()==0
                    &&mDatas.get(i).getRefund_state()==1&&mDatas.get(i).getReview_state()==1){//已付款未发货
                baseViewHolder.tvState.setText("已付款");
                baseViewHolder.btCancel.setVisibility(View.GONE);
                baseViewHolder.btPay.setVisibility(View.GONE);

            }else if (mDatas.get(i).getPay_state()== Constants.PAY_STATE_YES&&mDatas.get(i).getWaybill_id()!=0
                    &&mDatas.get(i).getRefund_state()==1&&mDatas.get(i).getReview_state()==1){
                baseViewHolder.tvState.setText("已发货");
                baseViewHolder.btCancel.setVisibility(View.GONE);
                baseViewHolder.btPay.setVisibility(View.VISIBLE);
                baseViewHolder.btPay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (orderButtonListener!=null){
                            orderButtonListener.onPay(baseViewHolder.tvState.getText().toString(),i);
                        }
                    }
                });

            }else{
                if (mDatas.get(i).getPay_state()== Constants.PAY_STATE_NO){
                    baseViewHolder.tvState.setText("已下单");
                }else {
                    baseViewHolder.tvState.setText("已收货");

                }
                baseViewHolder.btPay.setVisibility(View.GONE);
                baseViewHolder.btCancel.setVisibility(View.GONE);

            }
            if (mDatas.get(i).getCancel_state()!=1){
                baseViewHolder.tvState.setText("已取消");
                baseViewHolder.btCancel.setVisibility(View.GONE);
                baseViewHolder.btPay.setVisibility(View.GONE);
            }

            baseViewHolder.tvSku.setText(getSkus(mDatas.get(i).getGoods(),mDatas.get(i).getSku()));
            baseViewHolder.number.setText("×"+mDatas.get(i).getNum());
            baseViewHolder.tvTitle.setText(mDatas.get(i).getGoods().getTitle());
            baseViewHolder.tvPrice.setText(ShoppingCenterLibUtils.getPriceSpannable("￥"+ CountUtil.changeF2Y(mDatas.get(i).getSku().getPrice())));
        }
    }
    public void setOnClickButtonListener(OrderButtonListener orderButtonListener){
        this.orderButtonListener = orderButtonListener;
    }
    private String getSkus(GoodsBean.DataBean dataBeans, SkuBean.DataBean skuBean){
        StringBuffer stringBuffer =new StringBuffer();
                for (int j = 0; j < dataBeans.getSkus().length; j++) {
                    switch (j) {
                case 0:
                    stringBuffer.append(skuBean.getSku0()).append(" ");
                    break;
                case 1:
                    stringBuffer.append(skuBean.getSku1()).append(" ");
                    break;
                case 2:
                    stringBuffer.append(skuBean.getSku2()).append(" ");
                    break;
                case 3:
                    stringBuffer.append(skuBean.getSku3()).append(" ");
                    break;
                case 4:
                    stringBuffer.append(skuBean.getSku4()).append(" ");
                    break;
                case 5:
                    stringBuffer.append(skuBean.getSku5()).append(" ");
                    break;
                case 6:
                    stringBuffer.append(skuBean.getSku6()).append(" ");
                    break;
                case 7:
                    stringBuffer.append(skuBean.getSku7()).append(" ");
                    break;
                case 8:
                    stringBuffer.append(skuBean.getSku8()).append(" ");
                    break;
                case 9:
                    stringBuffer.append(skuBean.getSku9()).append(" ");
                    break;
            }

        }
        return stringBuffer.toString();
    }
    @Override
    public RecyclerView.ViewHolder onBaseCreateViewHolder(View view, int viewType) {
        return new BaseViewHolder(view);
    }
    class BaseViewHolder extends RecyclerView.ViewHolder {
        ImageView icCove;

        TextView tvTitle,tvPrice,tvState,tvTime,number,tvSku;
        TextView btPay,btCancel;
        BaseViewHolder(@NonNull View itemView) {
            super(itemView);
            icCove = itemView.findViewById(R.id.icCove);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvState = itemView.findViewById(R.id.tvState);
            btPay = itemView.findViewById(R.id.btPay);
            tvSku = itemView.findViewById(R.id.tvSku);
            number = itemView.findViewById(R.id.number);
            btCancel = itemView.findViewById(R.id.btCancel);
        }
    }
    @Override
    public int getViewLayout(int viewType) {
        return R.layout.item_shopping_order_good_shoppingcenter;
    }


}
