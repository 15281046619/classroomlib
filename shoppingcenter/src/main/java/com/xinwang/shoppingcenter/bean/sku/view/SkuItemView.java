package com.xinwang.shoppingcenter.bean.sku.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.TextView;

import com.xinwang.bgqbaselib.R;
import com.xinwang.bgqbaselib.utils.ScreenUtils;

/**
 * Date:2021/4/8
 * Time;15:24
 * author:baiguiqiang
 */
public class SkuItemView extends TextView {

    private String attributeValue;



    public SkuItemView(Context context) {

        super(context);

        init(context);

    }



    public SkuItemView(Context context, @Nullable AttributeSet attrs) {

        super(context, attrs);

        init(context);

    }



    public SkuItemView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {

        super(context, attrs, defStyleAttr);

        init(context);

    }



    private void init(Context context) {

        setBackgroundResource(R.drawable.sku_item_selector_classroom);
        setTextColor(getResources().getColorStateList(R.color.sku_item_text_selector_classroom));
        setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13);
        setSingleLine();
        setGravity(Gravity.CENTER);
        setPadding(ScreenUtils.dp2PxInt(context, 14),  0, ScreenUtils.dp2PxInt(context, 14), 0);
        setMinWidth(ScreenUtils.dp2PxInt(context, 50));
        setMaxWidth(ScreenUtils.dp2PxInt(context, 200));
        setHeight(ScreenUtils.dp2PxInt(context,50));
    }



    public String getAttributeValue() {

        return attributeValue;

    }



    public void setAttributeValue(String attributeValue) {

        this.attributeValue = attributeValue;

        setText(attributeValue);

    }

}
