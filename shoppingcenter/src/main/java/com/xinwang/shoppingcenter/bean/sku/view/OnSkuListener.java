package com.xinwang.shoppingcenter.bean.sku.view;

import com.xinwang.shoppingcenter.bean.Sku;
import com.xinwang.shoppingcenter.bean.SkuAttribute;

/**
 * Date:2021/4/8
 * Time;15:22
 * author:baiguiqiang
 */
public interface OnSkuListener {

    /**

     * 属性取消选中

     *

     * @param unselectedAttribute

     */

    void onUnselected(SkuAttribute unselectedAttribute);



    /**

     * 属性选中

     *

     * @param selectAttribute

     */

    void onSelect(SkuAttribute selectAttribute);



    /**

     * sku选中

     *

     * @param sku

     */

    void onSkuSelected(Sku sku);

}