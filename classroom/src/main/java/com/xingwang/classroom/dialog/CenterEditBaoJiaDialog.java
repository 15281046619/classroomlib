package com.xingwang.classroom.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.xingwang.classroom.R;
import com.xinwang.bgqbaselib.dialog.BaseDialog;
import com.xinwang.bgqbaselib.utils.KeyBoardHelper;
import com.xinwang.bgqbaselib.utils.MyToast;



public class CenterEditBaoJiaDialog extends BaseDialog {

    private Button btOk;
    private KeyBoardHelper mKeyBoardHelper;

    @Override
    protected int layoutResId() {
        return R.layout.dialog_edit_baojia_center_shopping;
    }
    private CenterEditBaoJiaDialog.Callback1 callback1;


    public static CenterEditBaoJiaDialog getInstance(String provice,String type,String unit){
        CenterEditBaoJiaDialog mDialog = new CenterEditBaoJiaDialog();
        Bundle bundle =new Bundle();
        bundle.putString("provice",provice);
        bundle.putString("type",type);
        bundle.putString("unit",unit);
        mDialog.setArguments(bundle);
        return mDialog;
    }



    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Dialog dialog = new Dialog(getContext(), com.xinwang.bgqbaselib.R.style.DialogCenterClassRoom);
        dialog.setContentView(layoutResId());
        Window window = dialog.getWindow();
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);//总是显示输入框
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

    EditText etPrice,etDes;
    private int digits =2;
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        etPrice = findViewById(R.id.etPrice);
        etDes = findViewById(R.id.etDes);
        TextView tvType =  findViewById(R.id.tvType);

        TextView tvProvice =  findViewById(R.id.tvProvice);
        tvType.setText(getArguments().getString("type"));
        tvProvice.setText(getArguments().getString("provice"));

        etPrice.setHint("请填写价格("+getArguments().getString("unit")+")");
        etPrice.requestFocus();
        etPrice.setSelected(true);
        etPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().contains(".")){
                    if (s.length()-1-s.toString().indexOf(".")>2){
                        s = s.toString().substring(0,s.toString().indexOf(".")+digits+1);
                        etPrice.setText(s);
                        etPrice.setSelection(s.length());
                        return;
                    }
                }
                if (s.toString().trim().equals(".")){
                    s="0"+s;
                    etPrice.setText(s);
                    etPrice.setSelection(2);
                    return;
                }
                if (s.toString().startsWith("0")&&s.toString().trim().length()>1){
                    if (!s.toString().substring(1,2).equals(".")){
                        etPrice.setText(s.subSequence(0,1));
                        etPrice.setSelection(1);
                    }

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        findViewById(R.id.btCancel).setOnClickListener(v -> dismissDialog());
        btOk = findViewById(com.xinwang.bgqbaselib.R.id.btOk);
        btOk.setOnClickListener(v -> {
            try {
                if (TextUtils.isEmpty(etPrice.getText().toString().trim())){
                    MyToast.myToast(getContext(),"请输入价格");

                }else if (Float.parseFloat(etPrice.getText().toString().trim())<=0){
                    MyToast.myToast(getContext(),"输入不合法");
                }else {
                    if (callback1!=null){
                        callback1.run(tvProvice.getText().toString(),tvType.getText().toString(),etPrice.getText().toString().trim(),etDes.getText().toString().trim());
                    }
                    dismissDialog();
                }

            }catch (Exception e){
                MyToast.myToast(getContext(),"输入不合法");
            }
        });

    }



    public CenterEditBaoJiaDialog setCallback(CenterEditBaoJiaDialog.Callback1 callback1 ){
        this.callback1 = callback1;
        return this;
    }
    public  interface Callback1 {
        void run(String provice,String type,String price,String content);
    }
}
