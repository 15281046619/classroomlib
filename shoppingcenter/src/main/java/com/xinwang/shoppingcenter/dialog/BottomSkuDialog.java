package com.xinwang.shoppingcenter.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.RelativeSizeSpan;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;



import com.xinwang.bgqbaselib.dialog.BaseDialog;
import com.xinwang.bgqbaselib.sku.bean.Sku;
import com.xinwang.bgqbaselib.sku.bean.SkuAttribute;
import com.xinwang.bgqbaselib.sku.view.OnSkuListener;
import com.xinwang.bgqbaselib.sku.view.SkuSelectScrollView;
import com.xinwang.bgqbaselib.utils.CommentUtils;
import com.xinwang.bgqbaselib.utils.GlideUtils;
import com.xinwang.bgqbaselib.utils.MyToast;
import com.xinwang.shoppingcenter.R;
import com.xinwang.shoppingcenter.ShoppingCenterLibUtils;
import com.xinwang.shoppingcenter.interfaces.OnClickOkListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Date:2020/9/17
 * Time;13:32
 * author:baiguiqiang
 */
public class BottomSkuDialog extends BaseDialog {
    private List<Sku> skuList =new ArrayList<>();
    private  TextView tvSelect,tvSum,tvOk,tvStore;
    private  SkuSelectScrollView skuSelectScrollView;
    private OnClickOkListener onClickOkListener;
    @Override
    protected int layoutResId() {
        return R.layout.dialog_bottom_sku_shoppingcenter;
    }

    /**
     *
     * @param mData
     * @param clickType 0 加入购物车 1 直接购买
     * @return
     */
    public static BottomSkuDialog getInstance(List<Sku> mData, int clickType){
        BottomSkuDialog instance = new BottomSkuDialog();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("data", (ArrayList<? extends Parcelable>) mData);
        bundle.putInt("type", clickType);
        instance.setArguments(bundle);
        return instance;
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getContext(), R.style.DialogGifSubmitStyleClassRoom);
        Window window = dialog.getWindow();
        dialog.setContentView(layoutResId());
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, CommentUtils.getScrrenHeight(Objects.requireNonNull(getActivity()))*4/5);
        layoutParams.gravity = Gravity.BOTTOM;

        layoutParams.windowAnimations = R.style.BottomSheetStyleClassRoom;
        window.setAttributes(layoutParams);
        return dialog;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        skuList =  getArguments().getParcelableArrayList("data");
        int type =getArguments().getInt("type");
        skuSelectScrollView = findViewById(R.id.skuView);
        TextView textView= findViewById(R.id.tvPrice);
        tvSelect= findViewById(R.id.tvSelect);

        tvSum =findViewById(R.id.tvSum);
        tvStore =findViewById(R.id.tvStore);

        tvOk =findViewById(R.id.tvOk);

        skuSelectScrollView.setSkuList(skuList);

        skuSelectScrollView.setListener(new OnSkuListener() {
            @Override
            public void onUnselected(SkuAttribute unselectedAttribute) {
                if (!TextUtils.isEmpty(skuList.get(0).getShowPrice())) {
                    textView.setText(ShoppingCenterLibUtils.getPriceSpannable("￥" + skuList.get(0).getShowPrice()));
                }
                tvStore.setText("库存"+skuList.get(0).getTotalStock()+"件");
                tvSelect.setText("");
            }

            @Override
            public void onSelect(SkuAttribute selectAttribute) {

            }

            @Override
            public void onSkuSelected(Sku sku) {
                textView.setText(ShoppingCenterLibUtils.getPriceSpannable("￥" + sku.getSellingPrice()));
                tvStore.setText("库存"+sku.getStockQuantity()+"件");

                showSelectTitle();

            }
        });


        if (skuList.size()>0&&!TextUtils.isEmpty(skuList.get(0).getShowPrice())) {
            GlideUtils.loadAvatar(skuList.get(0).getMainImage(),R.color.BGPressedClassRoom,findViewById(R.id.ivImg));
            textView.setText(ShoppingCenterLibUtils.getPriceSpannable("￥" + skuList.get(0).getShowPrice()));
        }
        if (skuList.size()>0){
            tvStore.setText("库存"+skuList.get(0).getTotalStock()+"件");
        }
        findViewById(R.id.viewLine).getLayoutParams().height=1;//设置线高度1像素

        if (type==0){
            tvOk.setText("加入购物车");
        }else if (type==1){
            tvOk.setText("立即购买");
        }
        findViewById(R.id.ivClose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissDialog();
            }
        });
        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (skuSelectScrollView.getSelectedSku()!=null) {
                    if (skuSelectScrollView.getSelectedSku().getStockQuantity()<=0){
                        MyToast.myToast(getActivity(),"该规格没有库存");
                        return;
                    }

                    skuSelectScrollView.getSelectedSku().setAddSum(Integer.parseInt(tvSum.getText().toString()));
                    if (type==0){
                        ShoppingCenterLibUtils.addShoppingCenter(getActivity(),skuSelectScrollView.getSelectedSku());
                    }else if (type==1){//进入确认页面
                        if (onClickOkListener!=null){
                            onClickOkListener.onClickOk(skuSelectScrollView.getSelectedSku());
                        }
                    }
                    dismissDialog();
                }else
                    MyToast.myToast(getActivity(),"请选择后提交");
            }
        });


        findViewById(R.id.tvAdd).setOnClickListener(v -> {
            if ((skuSelectScrollView.getSelectedSku()!=null&&Integer.parseInt(tvSum.getText().toString())>=skuSelectScrollView.getSelectedSku().getStockQuantity())||
                    (skuSelectScrollView.getSelectedSku()==null&&Integer.parseInt(tvSum.getText().toString())>=skuList.get(0).getTotalStock())){
                MyToast.myToast(getActivity(),"超出库存数目");
                return;
            }
            tvSum.setText(Integer.parseInt(tvSum.getText().toString())+1+"");
            showSelectTitle();
        });
        tvSum.setText("1");
        findViewById(R.id.tvSub).setOnClickListener(v -> {
            if (Integer.parseInt(tvSum.getText().toString())>1){
                tvSum.setText(Integer.parseInt(tvSum.getText().toString())-1+"");
                showSelectTitle();
            }else {
                MyToast.myToast(getActivity(),"数值低于范围");
            }

        });
    }
    public BottomSkuDialog setOnClickOkListener(OnClickOkListener onClickOkListener){
        this.onClickOkListener =onClickOkListener;
        return this;

    }
    private void showSelectTitle() {
        if (skuSelectScrollView.getSelectedSku() != null){
            tvSelect.setText("已选择: ");
            for (SkuAttribute attribute : skuSelectScrollView.getSelectedSku().getAttributes()) {
                tvSelect.append(attribute.getValue() + " ");
            }
            tvSelect.append("×" + tvSum.getText().toString());
        }
    }






}
