package com.xingwang.classroom.ui;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.beautydefinelibrary.BeautyDefine;
import com.xingwang.classroom.R;
import com.xingwang.classroom.bean.DetailBean;
import com.zzhoujay.richtext.RichText;


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
            tvTitle.setText(mBean.getData().getLecture().getTitle());
            tvSum.setText(mBean.getData().getLecture().getClick() + "次学习");
            RichText.from(mBean.getData().getLecture().getBody()).imageClick((imageUrls, position) ->
                    BeautyDefine.getImagePreviewDefine(getActivity()).showImagePreview(imageUrls, position)).into(tvContent);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
