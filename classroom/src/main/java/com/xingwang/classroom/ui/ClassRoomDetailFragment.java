package com.xingwang.classroom.ui;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.beautydefinelibrary.BeautyDefine;
import com.xingwang.classroom.R;
import com.xingwang.classroom.bean.DetailBean;
import com.xinwang.bgqbaselib.base.BaseLazyLoadFragment;
import com.xinwang.bgqbaselib.utils.CommentUtils;
import com.xinwang.bgqbaselib.utils.LogUtil;
import com.zzhoujay.richtext.RichText;
import com.zzhoujay.richtext.callback.OnUrlClickListener;


/**
 * Date:2019/8/22
 * Time;9:09
 * author:baiguiqiang
 */
public class ClassRoomDetailFragment extends BaseLazyLoadFragment {
    private TextView tvTitle,tvContent,tvSum;
    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_detail_classroom,container,false);
        tvTitle= view.findViewById(R.id.tv_title);
        tvContent= view.findViewById(R.id.tv_content);
        tvSum= view.findViewById(R.id.tv_sum);

        if (getContext()!=null)
            RichText.initCacheDir(getContext());
        return view;
    }

    @Override
    public void initData() {

    }
    public void upDateShow(DetailBean mBean){
        if (mBean!=null&&mBean.getData().getLecture()!=null) {
            if (tvTitle!=null)
                tvTitle.setText(mBean.getData().getLecture().getTitle());
            if (tvSum!=null)
                tvSum.setText(mBean.getData().getLecture().getClick() + "次学习");
            if (tvContent!=null)
                RichText.from(mBean.getData().getLecture().getBody()).imageClick((imageUrls, position) ->
                        BeautyDefine.getImagePreviewDefine(getActivity()).showImagePreview(imageUrls, position))
                        .urlClick(url -> {
                            CommentUtils.jumpWebBrowser(getActivity(),url);
                            return true;
                        }).into(tvContent);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
