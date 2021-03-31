package com.xinwang.shoppingcenter.adapter;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xinwang.bgqbaselib.utils.GsonUtils;
import com.xinwang.bgqbaselib.utils.SharedPreferenceUntils;
import com.xinwang.shoppingcenter.R;

import java.util.List;

/**
 * Date:2021/3/23
 * Time;14:59
 * author:baiguiqiang
 */
public class SearchHistoryAdapter extends RecyclerView.Adapter<SearchHistoryAdapter.SearchHistoryViewHolder> {
    private List<String> mList;
    private OnItemClickListener onItemClickListener;
    public SearchHistoryAdapter(List<String> mList,OnItemClickListener onItemClickListener){
        this.mList =mList;
        this.onItemClickListener =onItemClickListener;
    }

    @NonNull
    @Override
    public SearchHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new SearchHistoryViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_search_history_shoppingcenter,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull SearchHistoryViewHolder searchHistoryViewHolder, @SuppressLint("RecyclerView") int i) {
        searchHistoryViewHolder.tvContent.setText(mList.get(i));
        searchHistoryViewHolder.ivDelete.setOnClickListener(v -> {
            mList.remove(i);
            SharedPreferenceUntils.saveSearchHistory(searchHistoryViewHolder.ivDelete.getContext(), GsonUtils.createGsonString(mList));
            notifyDataSetChanged();
        });
        searchHistoryViewHolder.rlRoot.setOnClickListener(v -> {
            if (onItemClickListener!=null){
                onItemClickListener.onClick(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class SearchHistoryViewHolder extends RecyclerView.ViewHolder{
        private TextView tvContent;
        private ImageView ivDelete;
        private RelativeLayout rlRoot;
        public SearchHistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvContent =itemView.findViewById(R.id.tvContent);
            ivDelete =itemView.findViewById(R.id.ivDelete);
            rlRoot =itemView.findViewById(R.id.rlRoot);
        }
    }
   public interface OnItemClickListener{
        void onClick(int position);
    }
}
