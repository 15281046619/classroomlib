package com.xinwang.shoppingcenter.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xinwang.bgqbaselib.dialog.BaseDialog;
import com.xinwang.shoppingcenter.R;
import com.xinwang.shoppingcenter.bean.CategoryBean;

/**
 * Date:2021/3/24
 * Time;14:26
 * author:baiguiqiang
 */
public class CategorySelectDialog extends BaseDialog {
    private Callback1 callback1;
    private CategoryBean.DataBean categoryBean;
    private int lineSum =3;//一行显示3个
    public static CategorySelectDialog getInstance(CategoryBean.DataBean categoryBean){
        CategorySelectDialog instance = new CategorySelectDialog();
        Bundle bundle = new Bundle();
        bundle.putSerializable("data",categoryBean);
        instance.setArguments(bundle);
        return instance;
    }
    @Override
    protected int layoutResId() {
        return R.layout.dialog_category_select_shoppingcneter;
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Dialog dialog = new Dialog(getContext(), R.style.DialogRightCenterStyleClassRoom);
        dialog.setContentView(layoutResId());
        Window window = dialog.getWindow();
        if (window!=null) {
            WindowManager.LayoutParams layoutParams = window.getAttributes();
            layoutParams.height= WindowManager.LayoutParams.MATCH_PARENT;
            layoutParams.gravity = Gravity.RIGHT;
            window.setAttributes(layoutParams);
        }
        return dialog;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        categoryBean = (CategoryBean.DataBean) getArguments().getSerializable("data");
        new Thread(){
            @Override
            public void run() {
                showAttr();
            }
        }.start();

        findViewById(R.id.tvOk).setOnClickListener(v -> dismissDialog());

        findViewById(R.id.tvCancel).setOnClickListener(v -> {
            if (callback1!=null&&categoryBean!=null&&!categoryBean.getSelectAttr().isEmpty()&&categoryBean.getAttr().size()>0){
                categoryBean.getSelectAttr().clear();
                callback1.update(categoryBean);
            }
            dismissDialog();
        });
    }

    /**
     * 显示属性数据
     */
    private void showAttr() {
        LinearLayout llRoot = findViewById(R.id.llRoot);
        llRoot.removeAllViews();
        if (categoryBean!=null) {
            for (int i = 0; i < categoryBean.getAttr().size(); i++) {
                View mRoot = LayoutInflater.from(getContext()).inflate(R.layout.item_dialog_slect_shoppingcenter, llRoot, false);
                TextView tvTitle = mRoot.findViewById(R.id.tvTitle);
                tvTitle.setText(categoryBean.getAttr().get(i).getTitle());
                LinearLayout mLLRoot = mRoot.findViewById(R.id.llRoot);
                showChildView(mLLRoot, i);
                getActivity().runOnUiThread(() -> llRoot.addView(mRoot));

            }
        }
    }
    private void showChildView(LinearLayout llRoot,int pos){
        llRoot.removeAllViews();

        String[] mValues = categoryBean.getAttr().get(pos).getValue().split(" ");
        if (mValues!=null&&mValues.length>0)
            for (int j=0;j<mValues.length/lineSum+(mValues.length%lineSum==0?0:1);j++){
                View mChildRoot = LayoutInflater.from(getContext()).inflate(R.layout.item_dialog_slect_item_shoppingcenter, llRoot, false);
                TextView tvItem1 = mChildRoot.findViewById(R.id.tvItem1);
                TextView tvItem2 = mChildRoot.findViewById(R.id.tvItem2);
                TextView tvItem3 = mChildRoot.findViewById(R.id.tvItem3);


                tvItem1.setText(mValues[lineSum*j]);
                if (mValues[lineSum*j].equals(categoryBean.getSelectAttr().get(categoryBean.getAttr().get(pos).getField()))){
                    tvItem1.setSelected(true);
                }else
                    tvItem1.setSelected(false);
                if (lineSum*j+1<mValues.length) {
                    tvItem2.setText(mValues[lineSum * j + 1]);
                    if (mValues[lineSum*j+1].equals(categoryBean.getSelectAttr().get(categoryBean.getAttr().get(pos).getField()))){
                        tvItem2.setSelected(true);
                    }else
                        tvItem2.setSelected(false);
                } else
                    tvItem2.setVisibility(View.INVISIBLE);
                if (lineSum*j+2<mValues.length) {
                    tvItem3.setText(mValues[lineSum * j + 2]);
                    if (mValues[lineSum*j+2].equals(categoryBean.getSelectAttr().get(categoryBean.getAttr().get(pos).getField()))){
                        tvItem3.setSelected(true);
                    }else
                        tvItem3.setSelected(false);
                } else
                    tvItem3.setVisibility(View.INVISIBLE);

                int finalJ = j;
                tvItem1.setOnClickListener(v -> {
                    if (!tvItem1.isSelected())
                        categoryBean.getSelectAttr().put(categoryBean.getAttr().get(pos).getField(),mValues[lineSum* finalJ]);
                    else
                        categoryBean.getSelectAttr().remove(categoryBean.getAttr().get(pos).getField());
                    showChildView(llRoot,pos);
                    callback1.update(this.categoryBean);
                });
                tvItem2.setOnClickListener(v -> {
                    if (!tvItem2.isSelected())
                        categoryBean.getSelectAttr().put(categoryBean.getAttr().get(pos).getField(),mValues[lineSum* finalJ+1]);
                    else
                        categoryBean.getSelectAttr().remove(categoryBean.getAttr().get(pos).getField());
                    showChildView(llRoot,pos)  ;
                    callback1.update(this.categoryBean);
                });
                tvItem3.setOnClickListener(v -> {
                    if (!tvItem3.isSelected())
                        categoryBean.getSelectAttr().put(categoryBean.getAttr().get(pos).getField(),mValues[lineSum* finalJ+2]);
                    else
                        categoryBean.getSelectAttr().remove(categoryBean.getAttr().get(pos).getField());
                    showChildView(llRoot,pos) ;
                    callback1.update(this.categoryBean);
                });
                getActivity().runOnUiThread(() -> llRoot.addView(mChildRoot));

            }
    }
    public CategorySelectDialog setCallback(Callback1 callback1 ){
        this.callback1 = callback1;
        return this;
    }
    public  interface Callback1 {
        void update(CategoryBean.DataBean categoryBean);
    }
}
