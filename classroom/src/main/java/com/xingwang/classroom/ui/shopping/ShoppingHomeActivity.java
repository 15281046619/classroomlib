package com.xingwang.classroom.ui.shopping;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.xingwang.classroom.R;
import com.xingwang.classroom.ui.BaseActivity;

/**
 * Date:2021/3/15
 * Time;9:57
 * author:baiguiqiang
 */
public class ShoppingHomeActivity extends BaseActivity {
    private Fragment mFragment;
    @Override
    protected int layoutResId() {
        return R.layout.activity_shopping_home_classroom;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mFragment==null) {
            mFragment = new ShoppingHomeFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.flameLayout, mFragment).commit();
        }else {
            getSupportFragmentManager().beginTransaction().show(mFragment).commit();
        }
    }
}
