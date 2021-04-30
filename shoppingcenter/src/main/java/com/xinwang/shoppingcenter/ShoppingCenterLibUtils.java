package com.xinwang.shoppingcenter;

import android.app.Activity;
import android.content.Context;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.RelativeSizeSpan;

import com.beautydefinelibrary.BeautyDefine;
import com.xinwang.bgqbaselib.http.HttpUrls;
import com.xinwang.bgqbaselib.sku.bean.Sku;
import com.xinwang.bgqbaselib.sku.bean.SkuAttribute;
import com.xinwang.bgqbaselib.utils.CommentUtils;
import com.xinwang.bgqbaselib.utils.CountUtil;
import com.xinwang.bgqbaselib.utils.GsonUtils;
import com.xinwang.bgqbaselib.utils.MyToast;
import com.xinwang.bgqbaselib.utils.SharedPreferenceUntils;
import com.xinwang.shoppingcenter.bean.GoodsBean;
import com.xinwang.shoppingcenter.bean.NumberBean;
import com.xinwang.shoppingcenter.bean.SkuBean;
import com.xinwang.shoppingcenter.ui.ShoppingDetailActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;


/**
 * Date:2021/3/23
 * Time;14:10
 * author:baiguiqiang
 */
public class ShoppingCenterLibUtils {
    public static int ORDER_STATE_DFK=1;//待付款 订单
    public static int ORDER_STATE_DSH=2;//待收货 物流
    public static int ORDER_STATE_DPL=3;//待评价 商品
    public static int ORDER_STATE_YWC=4;//已完成 商品
    public static int ORDER_STATE_YQX=5;//已取消 订单
    public static int ORDER_STATE_SH=6;//售后
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
    public static void addShoppingCenter(Activity activity, Sku mBean ){
        if (mBean!=null) {
            String saveGoods = SharedPreferenceUntils.getGoods(activity);
            if (!TextUtils.isEmpty(saveGoods)) {
                List<Sku> mLists = GsonUtils.changeGsonToSafeList(saveGoods, Sku.class);
                int lookPos= -1;//查找pos
                for (int i = 0; i < mLists.size(); i++) {
                    if (mLists.get(i).getId()==null){//sku id为null 没有规格
                        if (mBean.getId()==null&&(mLists.get(i).getGoodId() == mBean.getGoodId())){
                            lookPos = i;
                            break;
                        }
                    }else {
                        if ((mLists.get(i).getId().equals(mBean.getId())) && (mLists.get(i).getGoodId() == mBean.getGoodId())) {
                            lookPos = i;
                            break;
                        }
                    }
                }

                if (lookPos==-1) {
                    mLists.add(0, mBean);
                    EventBus.getDefault().post(new NumberBean(1));
                }else {
                    if(mLists.get(lookPos).getAddSum()+mBean.getAddSum()>mLists.get(lookPos).getStockQuantity()) {
                          MyToast.myCenterFalseToast(activity, "添加失败，购物车库存不足");
                        return;

                    }else {
                        if (mLists.get(lookPos).getAddSum() + mBean.getAddSum() > mLists.get(lookPos).getMaxBugSum()) {
                            MyToast.myCenterFalseToast(activity, "添加失败，已到个人最大购买数目");
                            return;
                        }else
                            mLists.get(lookPos).setAddSum(mLists.get(lookPos).getAddSum() + mBean.getAddSum());
                    }

                }
                SharedPreferenceUntils.saveGoods(activity, GsonUtils.createGsonString(mLists));
            } else {
                List<Sku> mLists = new ArrayList<>();
                mLists.add(mBean);
                EventBus.getDefault().post(new NumberBean(1));
                SharedPreferenceUntils.saveGoods(activity, GsonUtils.createGsonString(mLists));
            }
            MyToast.myCenterSuccessToast(activity, "添加成功，在购物车等亲-");
        }
    }

    /**
     * 服务器返回数据转换sku模块显示数据
     * @return
     */
    public static List<Sku> skuToBean(List<SkuBean.DataBean> dataBeans,GoodsBean.DataBean goodsBean){
        List<Sku> skuList =new ArrayList<>();
        if(dataBeans!=null&&dataBeans.size()>0) {
            int[] prices = {dataBeans.get(0).getPrice(), dataBeans.get(0).getPrice()};//第一个值最小值，第二个值最大值
            int totalSum =0;
            for (int i = 0; i < dataBeans.size(); i++) {
                Sku sku = new Sku();
                sku.setInStock(true);
                sku.setId(dataBeans.get(i).getId() + "");
                sku.setGoodId(dataBeans.get(i).getGoods_id());
                sku.setGoodTitle(goodsBean.getTitle());
                try {
                    sku.setStockQuantity(dataBeans.get(i).getStock());
                    totalSum =totalSum +dataBeans.get(i).getStock();
                    sku.setOriginPrice(dataBeans.get(i).getPrice());
                    if (dataBeans.get(i).getPrice() > prices[0]) {
                        if (dataBeans.get(i).getPrice()> prices[1]) {
                            prices[1] = dataBeans.get(i).getPrice();
                        }
                    } else {
                        prices[0] = dataBeans.get(i).getPrice();
                    }
                } catch (Exception e) {

                }
                sku.setSellingPrice(sku.getOriginPrice());
                if (TextUtils.isEmpty(dataBeans.get(i).getCover()))
                    sku.setMainImage(goodsBean.getCover());
                else
                    sku.setMainImage(dataBeans.get(i).getCover());
                List<SkuAttribute> attributes = new ArrayList<>();
                for (int j = 0; j < goodsBean.getSkus().length; j++) {
                    switch (j) {
                        case 0:
                            attributes.add(new SkuAttribute(goodsBean.getSkus()[j], dataBeans.get(i).getSku0()));
                            break;
                        case 1:
                            attributes.add(new SkuAttribute(goodsBean.getSkus()[j], dataBeans.get(i).getSku1()));
                            break;
                        case 2:
                            attributes.add(new SkuAttribute(goodsBean.getSkus()[j], dataBeans.get(i).getSku2()));
                            break;
                        case 3:
                            attributes.add(new SkuAttribute(goodsBean.getSkus()[j], dataBeans.get(i).getSku3()));
                            break;
                        case 4:
                            attributes.add(new SkuAttribute(goodsBean.getSkus()[j], dataBeans.get(i).getSku4()));
                            break;
                        case 5:
                            attributes.add(new SkuAttribute(goodsBean.getSkus()[j], dataBeans.get(i).getSku5()));
                            break;
                        case 6:
                            attributes.add(new SkuAttribute(goodsBean.getSkus()[j], dataBeans.get(i).getSku6()));
                            break;
                        case 7:
                            attributes.add(new SkuAttribute(goodsBean.getSkus()[j], dataBeans.get(i).getSku7()));
                            break;
                        case 8:
                            attributes.add(new SkuAttribute(goodsBean.getSkus()[j], dataBeans.get(i).getSku8()));
                            break;
                        case 9:
                            attributes.add(new SkuAttribute(goodsBean.getSkus()[j], dataBeans.get(i).getSku9()));
                            break;
                    }

                }
                sku.setAttributes(attributes);
                skuList.add(sku);
            }

            if (prices[0]!=prices[1])
                skuList.get(0).setShowPrice(CountUtil.changeF2Y(prices[0]) + "-" + CountUtil.changeF2Y(prices[1]));
            else
                skuList.get(0).setShowPrice(CountUtil.changeF2Y(prices[0]));
            skuList.get(0).setTotalStock(totalSum);
        }else {//没有规格
            Sku sku =new Sku();
            sku.setGoodTitle(goodsBean.getTitle());
            sku.setGoodId(goodsBean.getId());
            sku.setInStock(true);
            sku.setMainImage(goodsBean.getCover());

            skuList.add(sku);
        }

        return skuList;

    }


    public static SpannableString getPriceSpannable(String content){
        SpannableString spannableString = new SpannableString(content);
        spannableString.setSpan( new RelativeSizeSpan(0.6f),0,1,SpannableString.SPAN_INCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    /**
     * 负数金额
     * @param content
     * @return
     */
    public static SpannableString getPriceSpannableSub(String content){
        SpannableString spannableString = new SpannableString(content);
        spannableString.setSpan( new RelativeSizeSpan(0.6f),0,2,SpannableString.SPAN_INCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    /**
     * 跳转技术老师
     * @param userId -1代表没有对应技术老师
     * @param text
     */
    public static void jumpChat(Activity activity,long userId, String text){
        if (userId==-1){
            MyToast.myLongToast(activity,"你还没有技术老师，请联系客服分配技术老师!");
            CommentUtils.jumpWebBrowser(activity,HttpUrls.URL_CHAT);
        }else
            BeautyDefine.getOpenPageDefine(activity).toPersonalChatText(userId,text);


    }
}
