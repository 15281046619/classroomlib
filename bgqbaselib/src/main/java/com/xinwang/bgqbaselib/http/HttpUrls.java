package com.xinwang.bgqbaselib.http;

/**
 * Date:2019/8/19
 * Time;15:05
 * author:baiguiqiang
 */
public class HttpUrls {
    // public static   String URL_HOST="http://zyapp.test.xw518.com/";
    public static   String URL_HOST="http://zyapp.app.xw518.com/";
    public static final String URL_NAME="";
    public static String URL_TYPE="zy";
    /*private static final String URL_HOST="http://192.168.65.74/";
    private static final String URL_NAME="xwapp/public/";*/

    public static final String URL_GETAUTHSTR=URL_HOST+URL_NAME+"user/auth/getAuthstr";
    public static final String URL_CHAT="http://kefu.xw518.com/chat.php?c=1";
    public static final String URL_CATEGORYS(){
        return URL_HOST+URL_NAME+"lecture/general/category/lists";
    }
    public static final String URL_GET_INFO(){
        return URL_HOST+URL_NAME+"user/user/my/info";
    }

    /**
     * 扩展信息
     * @return
     */
    public static final String URL_EXTRA_GET(){
        return URL_HOST+URL_NAME+"user/user/extra/get";
    }
    public static final String URL_AD_LISTS(){
        return URL_HOST+URL_NAME+"ad/general/ad/lists";
    }
    public static final String URL_LISTS(){
        return URL_HOST+URL_NAME+"lecture/general/lecture/lists";
    }
    public static final String URL_DETAIL(){
        return URL_HOST+URL_NAME+"lecture/general/lecture/detail";
    }
    public static final String URL_COMMENTLIST(){
        return URL_HOST+URL_NAME+"lecture/general/comment/lists";
    }
    public static final String URL_ORDER_DETAIL(){
        return URL_HOST+URL_NAME+"goods/user/order/detail";
    }
    public static final String URL_COMMENTDETAIL(){
        return URL_HOST+URL_NAME+"lecture/general/comment/detail";
    }
    public static final String URL_PUBLISH(){
        return URL_HOST+URL_NAME+"lecture/user/comment/publish";
    }
    public static final String URL_ISFAVORITE(){
        return URL_HOST+URL_NAME+"favorite/user/api/isFavorite";
    }
    public static final String URL_FAVORITE(){
        return URL_HOST+URL_NAME+"favorite/user/api/favorite";
    }
    public static final String URL_WAYBILL_DETAIL(){
        return URL_HOST+URL_NAME+"goods/user/waybill/detail";
    }
    public static final String URL_WAYBILL_INFO(){
        return URL_HOST+URL_NAME+"goods/user/waybill/get-waybill-info";
    }
    public static final String URL_UNFAVORITE(){
        return URL_HOST+URL_NAME+"favorite/user/api/unFavorite";
    }

    public static final String URL_LIVE_LISTS(){
      return  URL_HOST+URL_NAME+"live/home/live/lists";
    }

    /**
     * 取消订单
     * @return
     */
    public static final String URL_ORDER_CANCEL(){
      return  URL_HOST+URL_NAME+"goods/user/order/cancel";
    }
    /**
     * 评论
     * @return
     */
    public static final String URL_REVIEW_LISTS(){
      return  URL_HOST+URL_NAME+"goods/user/review/create";
    }
    /**
     * 签收
     * @return
     */
    public static final String URL_WAYBILL_SIGN(){
      return  URL_HOST+URL_NAME+"goods/user/waybill/sign";
    }
    public static final String URL_ORDER_GET_PAY(){
      return  URL_HOST+URL_NAME+"goods/user/order/get-pay-param";
    }

    public static final String URL_LIVE_CATEGORY(){
      return  URL_HOST+URL_NAME+"live/home/category/lists";
    }
    public static final String URL_LIVE_DETAIL(){
      return  URL_HOST+URL_NAME+"live/home/live/detail";
    }
    public static final String URL_LIVE_IS_SUBSCRIBE(){
      return  URL_HOST+URL_NAME+"live/user/live/isSubscribe";
    }
    public static final String URL_LIVE_SUBSCRIBE(){
      return  URL_HOST+URL_NAME+"live/user/live/subscribe";
    }
    public static final String URL_LIVE_VOD_LIST(){
      return  URL_HOST+URL_NAME+"live/home/live/vodList";
    }
    public static final String URL_LIVE_PLAY_INFO(){
      return  URL_HOST+URL_NAME+"live/home/live/playInfo";
    }
    public static final String URL_LIVE_ONLINE_COUNT(){
      return  URL_HOST+URL_NAME+"live/home/live/onlineCount";
    }
    public static final String URL_LIVE_CHAT_LISTS(){
      return  URL_HOST+URL_NAME+"live/home/chat/lists";
    }

    /**
     * 商城分类
     * @return
     */
    public static final String URL_GOODS_CATEGORY_LISTS(){
      return  URL_HOST+URL_NAME+"goods/home/category/lists";
    }

    /**
     * 商品评论列表
     * @return
     */
    public static final String URL_GOODS_REVIEW_LISTS(){
      return  URL_HOST+URL_NAME+"goods/home/review/lists";
    }

    /**
     * 获取技术老师信息
     * @return
     */
    public static final String URL_USER_MY_ERP(){
      return  URL_HOST+URL_NAME+"user/user/my/my-erp";
    }
    /**
     *创建订单
     * @return
     */
    public static final String URL_USER_ORDER_CREATE(){
      return  URL_HOST+URL_NAME+"goods/user/order/create";
    }
    /**
     *订单列表
     * @return
     */
    public static final String URL_USER_ORDER_LISTS(){
      return  URL_HOST+URL_NAME+"goods/user/order/lists";
    }
    /**
     *物流列表
     * @return
     */
    public static final String URL_USER_WAYBILL_LISTS(){
      return  URL_HOST+URL_NAME+"goods/user/waybill/lists";
    }
    /**
     *订单商品列表
     * @return
     */
    public static final String URL_USER_ITEM_LISTS(){
      return  URL_HOST+URL_NAME+"goods/user/item/lists";
    }
    /**
     *订单商品详情
     * @return
     */
    public static final String URL_USER_ITEM_DETAIL(){
      return  URL_HOST+URL_NAME+"goods/user/item/detail";
    }

    /**
     * 商城详情
     * @return
     */
    public static final String URL_GOODS_DETAIL(){
      return  URL_HOST+URL_NAME+"goods/home/goods/detail";
    }

    /**
     * 获取优惠劵
     * @return
     */
    public static final String URL_COUPON_LISTS(){
      return  URL_HOST+URL_NAME+"coupon/user/coupon/lists";
    }

    /**
     * 商城列表页面
     * @return
     */
    public static final String URL_GOODS_HOME_LISTS(){
      return  URL_HOST+URL_NAME+"goods/home/goods/lists";
    }

    /**
     * 商品sku属性
     * @return
     */
    public static final String URL_GOODS_HOME_SKU_LISTS(){
      return  URL_HOST+URL_NAME+"goods/home/sku/lists";
    }
    public static final String URL_USER_INFO(){
      return  URL_HOST+URL_NAME+"user/user/my/info";
    }
    public static final String URL_PRICE_HISTORY(){
      return  URL_HOST+URL_NAME+"price/general/api/history";
    }
    public static final String URL_LIVE_CHAT_SEND(){
      return  URL_HOST+URL_NAME+"live/user/chat/send";
    }
    public static final String URL_SHARE(){
        return URL_HOST+"page/share_lecture";//课程分享网页
    }
    public static final String URL_DOWNLOAD(){
        return URL_HOST+"page/download";//app下载
    }

    public static String CHANNEL="lecture.lecture_";//channel
    public static String LIVE_CHANNEL="live.";//直播channel
    public static String CHANNEL_WS_URL ="ws://zyapp.app.xw518.com/subscribe";
    public static String URL_ZHI_BO ="http://zhibo.xw518.com/zhibo/";//直播地址
    //已经弃用 2021 5 11
    public static final String URL_GOOD_LISTS(){
        return  URL_HOST+URL_NAME+"live/home/goods/lists";
    }
    public static final String URL_GOOD2_LISTS(){
        return  URL_HOST+URL_NAME+"live/home/goods2/lists";
    }
    public static final String URL_GIFT_TOPS(){
        return  URL_HOST+URL_NAME+"live/home/gift/tops";
    }
    public static final String URL_GOOD_ORDER(){
        return  URL_HOST+URL_NAME+"live/user/order/add";
    }

}
