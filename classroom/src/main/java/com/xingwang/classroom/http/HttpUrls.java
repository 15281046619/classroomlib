package com.xingwang.classroom.http;

/**
 * Date:2019/8/19
 * Time;15:05
 * author:baiguiqiang
 */
public class HttpUrls {
    private static final String URL_HOST="http://xwapp.xw518.com/";
    private static final String URL_NAME="";

    /*private static final String URL_HOST="http://192.168.65.74/";
    private static final String URL_NAME="xwapp/public/";*/

    public static final String URL_GETAUTHSTR=URL_HOST+URL_NAME+"user/auth/getAuthstr";
    public static final String URL_CATEGORYS=URL_HOST+URL_NAME+"lecture/general/lecture/categorys";
    public static final String URL_AD_LISTS=URL_HOST+URL_NAME+"ad/general/ad/lists";
    public static final String URL_LISTS=URL_HOST+URL_NAME+"lecture/general/lecture/lists";
    public static final String URL_DETAIL=URL_HOST+URL_NAME+"lecture/general/lecture/detail";
    public static final String URL_COMMENTLIST=URL_HOST+URL_NAME+"lecture/general/comment/lists";
    public static final String URL_COMMENTDETAIL=URL_HOST+URL_NAME+"lecture/general/comment/detail";
    public static final String URL_PUBLISH=URL_HOST+URL_NAME+"lecture/user/comment/publish";
    public static final String URL_ISFAVORITE=URL_HOST+URL_NAME+"favorite/user/api/isFavorite";
    public static final String URL_FAVORITE=URL_HOST+URL_NAME+"favorite/user/api/favorite";
    public static final String URL_UNFAVORITE=URL_HOST+URL_NAME+"favorite/user/api/unFavorite";
    public static final String URL_SHARE="http://xwapp.xw518.com/page/share_lecture";//课程分享网页
    //获取通道channel
    public static  String getChannel(int id){
        return "xwapp.zy.lecture.lecture_"+id+".new_comment";
    }
    public static String CHANNEL_WS_URL ="subscribe.test.xw518.com:10002";
}
