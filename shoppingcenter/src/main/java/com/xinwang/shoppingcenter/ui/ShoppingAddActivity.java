package com.xinwang.shoppingcenter.ui;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.xinwang.bgqbaselib.base.BaseActivity;
import com.xinwang.bgqbaselib.sku.bean.Sku;
import com.xinwang.bgqbaselib.utils.FragmentUtils;
import com.xinwang.bgqbaselib.utils.KeyBoardHelper;
import com.xinwang.bgqbaselib.utils.MyToast;
import com.xinwang.bgqbaselib.view.CustomToolbar;
import com.xinwang.shoppingcenter.R;

import java.util.ArrayList;



/**
 * Date:2021/3/22
 * Time;11:21
 * author:baiguiqiang
 */
public class ShoppingAddActivity extends BaseActivity {
    private CustomToolbar toolbar;
    private EditText etContent;
    private TextView tvOk;
    private ImageView ivDelete;
    private KeyBoardHelper mKeyBoardHelper;
    private Boolean borHelperVisible =false;
    private ArrayList<Sku> mSelectLists =new ArrayList<>();
    @Override
    protected int layoutResId() {
        return R.layout.activity_shopping_add_shoppingcenter;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mKeyBoardHelper = new KeyBoardHelper(this);
        mKeyBoardHelper.onCreate();
        initView();
        initListener();
        initData();
    }

    private void initData() {

        toolbar.setSelected(true);
        String search = getIntent().getStringExtra("search");
       if (search!=null) {
            showFragment(1,search);
        }else {
            showFragment(0, null);
        }

    }

    private void initListener() {
        toolbar.setNavigationOnClickListener(v -> onBack());
        etContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s!=null&&s.length()>0){
                    ivDelete.setVisibility(View.VISIBLE);
                }else {
                    ivDelete.setVisibility(View.INVISIBLE);
                    showFragment(0,null);
                }
            }
        });
        etContent.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId== EditorInfo.IME_ACTION_SEARCH){
                String content =etContent.getText().toString().trim();
                if (!TextUtils.isEmpty(content)){
                    etContent.clearFocus();
                    showFragment(1,content);
                }
                return true;
            }
            return false;
        });
        mKeyBoardHelper.setOnKeyBoardStatusChangeListener((visible, keyBoardHeight) -> borHelperVisible =visible);
        ivDelete.setOnClickListener(v -> etContent.setText(""));
        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSelectLists.size()>0){
                    setResult(100,new Intent().putExtra("data",mSelectLists));
                    finish();
                }else {
                    MyToast.myToast(ShoppingAddActivity.this,"请先选择商品后确定");
                }
            }
        });
    }
    public void selectSku(Sku sku){
        mSelectLists.add(sku);
        tvOk.setText("确定("+mSelectLists.size()+")");
    }
    public void showFragment(int i,String searchWord) {
        if (i==0){
            FragmentUtils.replaceFragment(this,R.id.flameLayout,ProductListsFragment.getInstance(searchWord,null,true,false));
        }else {
            etContent.setText(searchWord);
            if (etContent.getText().toString().length()>0)
            mKeyBoardHelper.closeInputMethodManager();
            FragmentUtils.replaceFragment(this,R.id.flameLayout,ProductListsFragment.getInstance(searchWord,null,true,false));
        }
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        etContent = findViewById(R.id.etContent);
        ivDelete = findViewById(R.id.ivDelete);
        tvOk = findViewById(R.id.tvOk);
    }

    @Override
    public void onBackPressed() {
        onBack();
    }


    /**
     * 返回 测试安卓10 如果app返回桌面再次进入在调用该方法 动画效果失效
     */
    private void onBack(){
        if (borHelperVisible){
            mKeyBoardHelper.closeInputMethodManager();
            return;
        }
        etContent.setText("");
        finish();

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mKeyBoardHelper!=null){
            mKeyBoardHelper.onDestroy();
        }

    }
}
