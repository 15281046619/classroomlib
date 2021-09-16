package com.xingwang.classroom.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.beautydefinelibrary.BeautyDefine;
import com.xingwang.classroom.R;
import com.xingwang.classroom.bean.BaoJiaBean;
import com.xingwang.classroom.view.CircularImage;
import com.xinwang.bgqbaselib.adapter.BaseLoadMoreAdapter;
import com.xinwang.bgqbaselib.utils.GlideUtils;

import java.util.List;

/**
 * Date:2021/9/15
 * Time;16:18
 * author:baiguiqiang
 */
public class BaoJiaListAdapter extends BaseLoadMoreAdapter<BaoJiaBean.DataBean> {
    private String unit;
    public BaoJiaListAdapter(List<BaoJiaBean.DataBean> mDatas,String unit) {
        super(mDatas);
        this.unit = unit;
    }

    @Override
    protected void onBaseBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof BaoJiaListHolder){
            BaoJiaListHolder baoJiaListHolder = (BaoJiaListHolder) viewHolder;
            BaoJiaBean.DataBean itemsBean = mDatas.get(i);
            GlideUtils.loadAvatar(BeautyDefine.getThumbUrlDefine().createThumbUrl(40, 40, itemsBean.getUser().getAvatar())
                    , R.mipmap.default_teammate_avatar_classroom, baoJiaListHolder.ivAvatar);
            baoJiaListHolder.tvName.setText(itemsBean.getUser().getNickname());
            baoJiaListHolder.tvTime.setText(itemsBean.getDate());
            baoJiaListHolder.tvPrice.setText(itemsBean.getPrice());
            baoJiaListHolder.tvUnit.setText(unit);
        }
    }

    @Override
    public RecyclerView.ViewHolder onBaseCreateViewHolder(View view, int viewType) {
        return new BaoJiaListHolder(view);
    }

    @Override
    public int getViewLayout(int viewType) {
        return R.layout.item_bao_jia_list_classroom;
    }
    class BaoJiaListHolder extends RecyclerView.ViewHolder{
        CircularImage ivAvatar;
        TextView tvName;
        TextView tvTime;
        TextView tvPrice;
        TextView tvUnit;
        public BaoJiaListHolder(@NonNull View itemView) {
            super(itemView);
            ivAvatar =itemView.findViewById(R.id.ivAvatar);
            tvName =itemView.findViewById(R.id.tvName);
            tvTime =itemView.findViewById(R.id.tvTime);
            tvPrice =itemView.findViewById(R.id.tvPrice);
            tvUnit =itemView.findViewById(R.id.tvUnit);
        }
    }
}
