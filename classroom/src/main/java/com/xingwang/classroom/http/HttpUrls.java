package com.xingwang.classroom.http;

/**
 * Date:2019/8/19
 * Time;15:05
 * author:baiguiqiang
 */
public class HttpUrls {
    // public static   String URL_HOST="http://zyapp.test.xw518.com/";
    public static   String URL_HOST="http://zyapp.app.xw518.com/";
    private static final String URL_NAME="";

    /*private static final String URL_HOST="http://192.168.65.74/";
    private static final String URL_NAME="xwapp/public/";*/

    public static final String URL_GETAUTHSTR=URL_HOST+URL_NAME+"user/auth/getAuthstr";
    public static final String URL_CATEGORYS(){
        return URL_HOST+URL_NAME+"lecture/general/category/lists";
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
    public static final String URL_UNFAVORITE(){
        return URL_HOST+URL_NAME+"favorite/user/api/unFavorite";
    }

    public static final String URL_LIVE_LISTS(){
      return  URL_HOST+URL_NAME+"live/home/live/lists";
    }
    public static final String URL_LIVE_DETAIL(){
      return  URL_HOST+URL_NAME+"live/home/live/detail";
    }
    public static final String URL_LIVE_IS_SUBSCRIBE(){
      return  URL_HOST+URL_NAME+"live/user/live/isSubscribe";
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
    public static final String URL_SHARE(){
        return URL_HOST+"page/share_lecture";//课程分享网页
    }

    public static String CHANNEL="zyapp.lecture.lecture_";//channel
    public static String CHANNEL_WS_URL ="ws://subscribe.app.xw518.com:10101";
    public static String URL_ZHI_BO ="http://zhibo.xw518.com/zhibo/";//直播地址

    //public static final String URL_LIVE_LISTS="http://xielei.test.xw518.com/zyapp.test.xw518.com/public/live/home/live/lists";
}
