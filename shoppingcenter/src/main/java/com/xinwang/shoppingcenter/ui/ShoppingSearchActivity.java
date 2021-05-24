package com.xinwang.shoppingcenter.ui;

import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.xinwang.bgqbaselib.base.BaseActivity;
import com.xinwang.bgqbaselib.utils.CommentUtils;
import com.xinwang.bgqbaselib.utils.FragmentUtils;
import com.xinwang.bgqbaselib.utils.GsonUtils;
import com.xinwang.bgqbaselib.utils.KeyBoardHelper;
import com.xinwang.bgqbaselib.utils.SharedPreferenceUntils;
import com.xinwang.bgqbaselib.view.CustomToolbar;
import com.xinwang.shoppingcenter.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Date:2021/3/22
 * Time;11:21
 * author:baiguiqiang
 */
public class ShoppingSearchActivity extends BaseActivity {
    private CustomToolbar toolbar;
    private EditText etContent;
    private ImageView ivDelete;
    private KeyBoardHelper mKeyBoardHelper;
    private Boolean borHelperVisible =false;
    public List<String> mSearchData;//搜索历史数据
    @Override
    protected int layoutResId() {
        return R.layout.activity_shopping_search_shoppingcenter;
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
        String mContent = SharedPreferenceUntils.getSearchHistory(this);
        mSearchData = GsonUtils.changeGsonToList(mContent, String.class);
        toolbar.setSelected(true);
        if (mSearchData==null){
            mSearchData =new ArrayList<>();
        }
        String search = getIntent().getStringExtra("search");
       if (search!=null) {
            showFragment(1,search);
        }else {
            showFragment(0, null);
            etContent.setSelected(true);
            etContent.requestFocus();
            etContent.post(() -> mKeyBoardHelper.openInputMethodManager(etContent));
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
    }

    public void showFragment(int i,String searchWord) {
        if (i==0){
            FragmentUtils.replaceFragment(this,R.id.flameLayout,new ShoppingSearchHistoryFragment());
        }else {
            etContent.setText(searchWord);
            if (etContent.getText().toString().length()>0)

            goSaveSearch(searchWord); etContent.setSelection(etContent.getText().toString().length());
            mKeyBoardHelper.closeInputMethodManager();
            FragmentUtils.replaceFragment(this,R.id.flameLayout,ProductListsFragment.getInstance(searchWord,null,false));
        }
    }
   private void goSaveSearch(String searchWord){
        for (String item :mSearchData){
            if (item.equals(searchWord)){
                mSearchData.remove(item);
                break;
            }
        }
        mSearchData.add(0,searchWord);
        SharedPreferenceUntils.saveSearchHistory(this,GsonUtils.createGsonString(mSearchData));
   }
    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        etContent = findViewById(R.id.etContent);
        ivDelete = findViewById(R.id.ivDelete);
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
        if (android.os.Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            ColorDrawable colorDrawable = new ColorDrawable();
            colorDrawable.setColor(getResources().getColor(android.R.color.white));
            colorDrawable.setAlpha(0);
            getWindow().setBackgroundDrawable(colorDrawable);
            finishAfterTransition();
        } else {
            finish();
        }
        overridePendingTransition(0,0);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mKeyBoardHelper!=null){
            mKeyBoardHelper.onDestroy();
        }

    }
}
