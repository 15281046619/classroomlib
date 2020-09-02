package com.xingwang.groupchat.view.lookup;


import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xingwang.groupchat.R;

import java.util.List;

public abstract class LeftAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<LookUpBean> mDatas;
    public LeftAdapter(List<LookUpBean> mDatas){
        this.mDatas = mDatas;
    }
    public void setData(List<LookUpBean> mDatas){
        this.mDatas =mDatas;
    }
    public List<LookUpBean> getData(){
        return mDatas;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.groupchat_item_lookup_left,viewGroup,false);
        return new LeftViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        LookUpBean mData = mDatas.get(i);
        LeftViewHolder mViewHolder = (LeftViewHolder) viewHolder;
        if (mData.isPeak()){
            mViewHolder.tvTitle.setText(mData.getHeadStr());
            mViewHolder.tvTitle.setVisibility(View.VISIBLE);
            mViewHolder.viewTitleLine.setVisibility(View.VISIBLE);
        }else{
            mViewHolder.tvTitle.setVisibility(View.GONE);
            mViewHolder.viewTitleLine.setVisibility(View.GONE);
        }
        onBindViewHolder(mViewHolder,mData,i);
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }
   public class LeftViewHolder extends RecyclerView.ViewHolder{
       private final SparseArray<View> views = new SparseArray();
        private TextView tvTitle;
        private View viewTitleLine;
        public RelativeLayout rlItem;
        public LeftViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            viewTitleLine = itemView.findViewById(R.id.view_title_line);
            rlItem = itemView.findViewById(R.id.rl_item);
            rlItem.addView(LayoutInflater.from(itemView.getContext()).inflate(getLayoutResource(),null));
        }
        public LeftViewHolder setText(String str, int viewId){
            ((TextView) itemView.findViewById(viewId)).setText(str);
            return this;
        }

       public <T extends View> T getView(@IdRes int viewId) {
           View view = this.views.get(viewId);
           if (view == null) {
               view = this.itemView.findViewById(viewId);
               this.views.put(viewId, view);
           }

           return (T) view;
       }
    }

    protected abstract int getLayoutResource() ;
    protected abstract void onBindViewHolder(LeftViewHolder mViewHolder,LookUpBean mData,int position);
}
