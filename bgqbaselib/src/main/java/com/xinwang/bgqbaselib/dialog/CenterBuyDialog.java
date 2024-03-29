package com.xinwang.bgqbaselib.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.beautydefinelibrary.BeautyDefine;
import com.beautydefinelibrary.LocationCallBack;
import com.beautydefinelibrary.LocationDefine;
import com.xinwang.bgqbaselib.R;
import com.xinwang.bgqbaselib.utils.KeyBoardHelper;
import com.xinwang.bgqbaselib.utils.MyToast;
import com.xinwang.bgqbaselib.utils.SharedPreferenceUntils;


/**
 * 购买药品dialog
 */
public class CenterBuyDialog extends BaseDialog {

    private Button btOk;
    private KeyBoardHelper mKeyBoardHelper;
    public LocationDefine imagePickerDefine;

    @Override
    protected int layoutResId() {
        return R.layout.dialog_center_buy_shoppingcenter;
    }
    private Callback1<String> callback1;


    public static CenterBuyDialog getInstance(String title,String des){
        CenterBuyDialog mDialog = new CenterBuyDialog();
        Bundle bundle =new Bundle();
        bundle.putString("title",title);
        bundle.putString("des",des);
        mDialog.setArguments(bundle);
        return mDialog;
    }
    /**
     * 跳转定位页面
     */
    public void goLocationAddress(){
        if (mKeyBoardHelper!=null)
        mKeyBoardHelper.closeInputMethodManager();
        imagePickerDefine = BeautyDefine.getLocationDefine((AppCompatActivity) getActivity());
        imagePickerDefine.showLocation(new LocationCallBack() {
            @Override
            public void onResult(String s, String s1, String s2, String s3, String s4, String s5, String s6, String s7) {
                etAddress.setText(s4);
                if (!s7.equals(s4)&&!s7.equals(s1))
                etAddress.append(" "+s7);
                etAddress.setSelection(etAddress.getText().toString().length());
            }
        });
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
        if (mKeyBoardHelper!=null) {
            mKeyBoardHelper.closeInputMethodManager();
            mKeyBoardHelper.onDestroy();
        }
    }

    EditText etNumber,etName,etAddress;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        etNumber = findViewById(R.id.etNumber);
        etName = findViewById(R.id.etName);
        assert getArguments() != null;
        ((TextView)findViewById(R.id.tvTitle)).setText(getArguments().getString("title"));
        ((TextView)findViewById(R.id.tvDes)).setText(getArguments().getString("des"));
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
                MyToast.myToast(getContext(),"请填写收货人姓名");
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
        findViewById(R.id.ivLocation).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if (callback1!=null)
                   callback1.clickLocation();
            }
        });
    }



    public CenterBuyDialog setCallback(Callback1<String> callback1 ){
        this.callback1 = callback1;
        return this;
    }
    public  interface Callback1<T> {
        void run(T t);
        void clickLocation();
    }
}
