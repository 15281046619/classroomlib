package com.xinwang.shoppingcenter;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import com.xinwang.bgqbaselib.http.HttpUrls;
import com.xinwang.bgqbaselib.utils.GsonUtils;
import com.xinwang.bgqbaselib.utils.MyToast;
import com.xinwang.bgqbaselib.utils.SharedPreferenceUntils;
import com.xinwang.shoppingcenter.bean.GoodsBean;

import java.util.ArrayList;
import java.util.List;


/**
 * Date:2021/3/23
 * Time;14:10
 * author:baiguiqiang
 */
public class ShoppingCenterLibUtils {
    public static String[] getHotSearch(Context context){

        switch (HttpUrls.URL_TYPE){
            case "zy":
                return context.getResources().getStringArray(R.array.zy_hot_search_array_ShoppingCenter);
            case "jq":
                return context.getResources().getStringArray(R.array.jq_hot_search_array_ShoppingCenter);
            case "sc":
                return context.getResources().getStringArray(R.array.sc_hot_search_array_ShoppingCenter);
            case "ny":
                return context.getResources().getStringArray(R.array.ny_hot_search_array_ShoppingCenter);
        }
        return context.getResources().getStringArray(R.array.zy_hot_search_array_ShoppingCenter);
    }

    /**
     * 添加商品
     */
    public static void addShoppingCenter(Activity activity, GoodsBean.DataBean mBean ){
        if (mBean!=null) {
            String saveGoods = SharedPreferenceUntils.getGoods(activity);
            if (!TextUtils.isEmpty(saveGoods)) {
                List<GoodsBean.DataBean> mLists = GsonUtils.changeGsonToSafeList(saveGoods, GoodsBean.DataBean.class);
                int lookPos= -1;//查找pos
                for (int i = 0; i < mLists.size(); i++) {
                    if (mLists.get(i).getId() == mBean.getId()) {
                        lookPos =i;

                        break;
                    }
                }
                if (lookPos==-1)
                    mLists.add(0, mBean);
                else
                    mLists.get(lookPos).setAddSum(mLists.get(lookPos).getAddSum() + 1);
                SharedPreferenceUntils.saveGoods(activity, GsonUtils.createGsonString(mLists));
            } else {
                List<GoodsBean.DataBean> mLists = new ArrayList<>();
                mLists.add(mBean);
                SharedPreferenceUntils.saveGoods(activity, GsonUtils.createGsonString(mLists));
            }
            MyToast.myCenterSuccessToast(activity, "添加成功，在购物车等亲-");
        }
    }
}
