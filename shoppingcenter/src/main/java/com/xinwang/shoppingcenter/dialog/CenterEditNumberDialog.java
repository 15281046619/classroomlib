package com.xinwang.shoppingcenter.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.xinwang.bgqbaselib.dialog.BaseDialog;
import com.xinwang.bgqbaselib.utils.KeyBoardHelper;
import com.xinwang.bgqbaselib.utils.MyToast;
import com.xinwang.shoppingcenter.R;


public class CenterEditNumberDialog extends BaseDialog {

    private Button btOk;
    private KeyBoardHelper mKeyBoardHelper;

    @Override
    protected int layoutResId() {
        return R.layout.dialog_edit_number_center_shopping;
    }
    private CenterEditNumberDialog.Callback1<Integer> callback1;


    public static CenterEditNumberDialog getInstance(int maxSum,long  maxStock,String number){
        CenterEditNumberDialog mDialog = new CenterEditNumberDialog();
        Bundle bundle =new Bundle();
        bundle.putInt("maxSum",maxSum);
        bundle.putLong("maxStock",maxStock);
        bundle.putString("number",number);
        mDialog.setArguments(bundle);
        return mDialog;
    }



    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Dialog dialog = new Dialog(getContext(), com.xinwang.bgqbaselib.R.style.DialogCenterClassRoom);
        dialog.setContentView(layoutResId());
        Window window = dialog.getWindow();
        if (window!=null) {
            WindowManager.LayoutParams layoutParams = window.getAttributes();
            layoutParams.gravity = Gravity.CENTER;
            window.setAttributes(layoutParams);
        }
        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        mKeyBoardHelper = new KeyBoardHelper(getActivity());
        mKeyBoardHelper.onCreate();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mKeyBoardHelper!=null) {
            mKeyBoardHelper.closeInputMethodManager();
            mKeyBoardHelper.onDestroy();
        }
    }

    EditText etNumber;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        etNumber = findViewById(R.id.etNumber);

        assert getArguments() != null;

        int maxSum = getArguments().getInt("maxSum");
        long maxStock = getArguments().getLong("maxStock");
        String number = getArguments().getString("number");
        etNumber.setText(number+"");
        etNumber.setSelection(etNumber.getText().toString().length());
        etNumber.requestFocus();
        etNumber.setSelected(true);
        findViewById(R.id.btCancel).setOnClickListener(v -> dismissDialog());
        btOk = findViewById(com.xinwang.bgqbaselib.R.id.btOk);
        btOk.setOnClickListener(v -> {
            try {
                int mNumber = Integer.parseInt(etNumber.getText().toString());
                if (mNumber==0){
                    MyToast.myToast(getActivity(),"输入不合法");
                    return;
                }
                if (mNumber>maxStock){
                    MyToast.myToast(getActivity(),"超出库存数目");
                    return;
                }

                if (mNumber>maxSum){
                    MyToast.myToast(getActivity(),"超出个人限购数目");
                    return;
                }
                if (callback1!=null){
                    callback1.run(mNumber);
                }
                dismissDialog();
            }catch (Exception e){
                MyToast.myToast(getContext(),"输入不合法");
            }


        });

    }



    public CenterEditNumberDialog setCallback(CenterEditNumberDialog.Callback1<Integer> callback1 ){
        this.callback1 = callback1;
        return this;
    }
    public  interface Callback1<T> {
        void run(T t);
    }
}
