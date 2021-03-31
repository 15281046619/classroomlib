package com.xinwang.shoppingcenter.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;


import com.xinwang.bgqbaselib.base.BaseActivity;
import com.xinwang.bgqbaselib.utils.FragmentUtils;
import com.xinwang.shoppingcenter.R;

/**
 * Date:2021/3/15
 * Time;9:57
 * author:baiguiqiang
 */
public class ShoppingHomeActivity extends BaseActivity {
    private Fragment mFragment;
    @Override
    protected int layoutResId() {
        return R.layout.activity_shopping_home_shoppingcenter;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mFragment==null) {
            mFragment = new ShoppingHomeFragment();
        }
        FragmentUtils.showFragment(this,R.id.flameLayout, mFragment);
    }

}
