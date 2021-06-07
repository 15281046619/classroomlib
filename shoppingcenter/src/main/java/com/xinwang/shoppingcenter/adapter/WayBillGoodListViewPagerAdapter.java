package com.xinwang.shoppingcenter.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xinwang.bgqbaselib.utils.CountUtil;
import com.xinwang.bgqbaselib.utils.GlideUtils;
import com.xinwang.shoppingcenter.R;
import com.xinwang.shoppingcenter.ShoppingCenterLibUtils;
import com.xinwang.shoppingcenter.bean.GoodsBean;
import com.xinwang.shoppingcenter.bean.OrderListBean;
import com.xinwang.shoppingcenter.bean.SkuBean;
import java.util.List;

/**
 * Date:2021/6/2
 * Time;11:31
 * author:baiguiqiang
 */
public class WayBillGoodListViewPagerAdapter extends PagerAdapter {
    private List<OrderListBean.DataBean.OrdersBean.ItemsBean> itemsBeans;
    private Context context;
    public WayBillGoodListViewPagerAdapter(Context context,List<OrderListBean.DataBean.OrdersBean.ItemsBean> mData){
       this.context =context;
        itemsBeans =mData;
    }
    @Override
    public int getCount() {
        return itemsBeans.size();
    }
    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view==o;
    }



    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
       View view = LayoutInflater.from(context).inflate(R.layout.item_waybill_good_list_shoppingcenter,container,false);
       ImageView icCove = view.findViewById(R.id.icCove);
       TextView tvTitle = view.findViewById(R.id.tvTitle);
        TextView tvPrice = view.findViewById(R.id.tvPrice);
       TextView tvSku =view. findViewById(R.id.tvSku);
        TextView number =view. findViewById(R.id.number);
        GlideUtils.loadAvatar(TextUtils.isEmpty(itemsBeans.get(position).getSku().getCover())?itemsBeans.get(position).getGoods().getCover()
                :itemsBeans.get(position).getSku().getCover(),R.color.BGPressedClassRoom,icCove);
        tvSku.setText(getSkus(itemsBeans.get(position).getGoods(),itemsBeans.get(position).getSku()));
        number.setText("×"+itemsBeans.get(position).getNum());
        tvTitle.setText(itemsBeans.get(position).getGoods().getTitle());
        tvPrice.setText(ShoppingCenterLibUtils.getPriceSpannable("￥"+ CountUtil.changeF2Y(itemsBeans.get(position).getSku().getPrice())));
        container.addView(view);
        return view;
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
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
