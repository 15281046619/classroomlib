package com.xinwang.shoppingcenter.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import com.beautydefinelibrary.BeautyDefine;

import com.xinwang.bgqbaselib.dialog.BaseDialog;
import com.xinwang.bgqbaselib.utils.KeyBoardHelper;
import com.xinwang.bgqbaselib.utils.MyToast;
import com.xinwang.bgqbaselib.utils.SharedPreferenceUntils;
import com.xinwang.shoppingcenter.R;

/**
 * 购买药品dialog
 */
public class CenterBuyDialog extends BaseDialog {

    private Button btOk;
    private View mCurView;
    private KeyBoardHelper mKeyBoardHelper;
    @Override
    protected int layoutResId() {
        return R.layout.dialog_center_buy_shoppingcenter;
    }
    private Callback1<String> callback1;


    public static CenterBuyDialog getInstance(){
        return new CenterBuyDialog();
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Dialog dialog = new Dialog(getContext(),R.style.DialogCenterClassRoom);
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
        mKeyBoardHelper.closeInputMethodManager();
        mKeyBoardHelper.onDestroy();
    }

    EditText etNumber,etName,etAddress;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        etNumber = findViewById(R.id.etNumber);
        etName = findViewById(R.id.etName);
        etAddress = findViewById(R.id.etAddress);
        etAddress.setText(SharedPreferenceUntils.getSaveAddress(getContext()));
        etName.setText(SharedPreferenceUntils.getSaveName(getContext()));
        String savePhone = SharedPreferenceUntils.getSavePhone(getContext());
        etNumber.setText(TextUtils.isEmpty(savePhone)? BeautyDefine.getUserInfoDefine(getContext()).getPhone():savePhone);

        findViewById(R.id.btCancel).setOnClickListener(v -> dismissDialog());
        btOk = findViewById(R.id.btOk);
        btOk.setOnClickListener(v -> {
            String address=etAddress.getText().toString().trim();
            String name= etName.getText().toString().trim();
            String phone =etNumber.getText().toString().trim();
            if (TextUtils.isEmpty(phone)){
                MyToast.myToast(getContext(),"请填写手机号码");
                return;
            }else if (TextUtils.isEmpty(address)){
                MyToast.myToast(getContext(),"请填写详细地址");
                return;
            }else  if (TextUtils.isEmpty(name)){
                MyToast.myToast(getContext(),"请填写买家姓名");
                return;
            }

            SharedPreferenceUntils.saveAddress(getContext(),address);
            SharedPreferenceUntils.saveName(getContext(),name);
            SharedPreferenceUntils.savePhone(getContext(),phone);

            if (callback1!=null){
                callback1.run("{\"phone\":\""+phone+"\",\"name\":\""+name+"\",\"address\":\""+address+"\"}");
            }
            dismissDialog();
        });

    }



    public CenterBuyDialog setCallback(Callback1<String> callback1 ){
        this.callback1 = callback1;
        return this;
    }
    public  interface Callback1<T> {
        void run(T t);
    }
}
