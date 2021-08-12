package com.xinwang.shoppingcenter.view;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gongwen.marqueen.MarqueeFactory;
import com.xinwang.bgqbaselib.utils.CommentUtils;
import com.xinwang.bgqbaselib.utils.GlideUtils;
import com.xinwang.bgqbaselib.utils.TimeUtil;
import com.xinwang.shoppingcenter.R;
import com.xinwang.shoppingcenter.bean.LateLyOrderBean;
import com.xinwang.shoppingcenter.ui.OrderDetailActivity;
import com.xinwang.shoppingcenter.ui.ShoppingDetailActivity;

/**
 * Date:2021/8/6
 * Time;13:54
 * author:baiguiqiang
 */
public class ComplexViewMF extends MarqueeFactory<RelativeLayout, LateLyOrderBean.DataBean> {
    private LayoutInflater inflater;
    private Context mContext;
    public ComplexViewMF(Context mContext) {
        super(mContext);
        this.mContext = mContext;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    protected RelativeLayout generateMarqueeItemView(LateLyOrderBean.DataBean data) {
        RelativeLayout mView = (RelativeLayout) inflater.inflate(R.layout.item_marquee_shoppingcenter, null);
        ((TextView) mView.findViewById(R.id.tvName)).setText(data.getUser().getNickname());
        ((TextView) mView.findViewById(R.id.tvTime)).setText(TimeUtil.getTimeFormatText(TimeUtil.getYMDHMS2(data.getOrder().getCreate_time()+"")));
        ((TextView) mView.findViewById(R.id.tvGood)).setText(data.getGoods().getTitle());
        mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ShoppingDetailActivity.class);
                intent.putExtra("id",data.getGoods().getId());
                mContext.startActivity(intent);
            }
        });

        return mView;
    }
}
