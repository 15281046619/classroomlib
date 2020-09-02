package com.xingwang.swip.utils;

import android.os.Environment;

import java.io.File;

public class Constants {

    public static String IP = "http://xielei.test.xw518.com/zyapp.test.xw518.com/public/";
    //public static String IP = "http://zyapp.app.xw518.com/";

    public final static String INTENT_DATA = "data";
    public final static String INTENT_DATA1 = "data1";

    //定义app类型
    public static final String TYPE_ZY ="zy";
    public static final String TYPE_JQ ="jq";
    public static final String TYPE_SC ="sc";
    public static final String TYPE_NY ="ny";

    public final static String VIDEO_PATH = Environment.getExternalStorageDirectory().getPath() + File.separator + "im";

    /**********圈子模块***********/
    public static String HTTP_BBS = IP + "bbs/";
    /**
     * 图片选择最大张数
     */
    public final static int MAX_COUNT = 9;
    /***帖子详情显示的点赞列表*/
    public final static int MAX_DIGG_NUM = 4;
    /**
     * 来源-所有栏目
     */
    public final static int ALL_CIRCLE = 500;
    /**
     * 来源-发帖
     */
    public final static int POST_CARD = 7;

    public static String CIRCLE_FORUM = HTTP_BBS + "general/forum/lists";
    public static String CIRCLE_POST = HTTP_BBS + "user/thread/publish";
    public static String CARD_LIST = HTTP_BBS + "general/thread/lists";
    public static String CARD_INFO = HTTP_BBS + "general/thread/get";//帖子详情

    public static String DIGG = IP + "digg/user/api/digg";//点赞
    public static String UN_DIGG = IP + "digg/user/api/unDigg";//取消点赞

    public static String COLLECT = IP + "favorite/user/api/favorite";//收藏
    public static String UN_COLLECT = IP + "favorite/user/api/unFavorite";//取消收藏

    public static String USEFUL_COMMENT = HTTP_BBS + "general/comment/recommended";//精选评论
    public static String COMMENT = HTTP_BBS + "general/comment/lists";//评论
    public static String COMMENT_INFO = HTTP_BBS + "general/comment/detail";//评论详情

    public static String SEND_COMMENT = HTTP_BBS + "user/comment/publish";//发布评论

    public static String DIGG_LIST = IP + "digg/general/api/lists";//点赞列表


    /*********************文章模块***************/

    public static String HTTP_ESSAY = IP + "article/general/article/";
    public static String HTTP_COLLECT = IP + "favorite/user/api/";

    public static String ESSAY_CATALOG = IP + "article/general/category/lists";
    public static String ESSAY_LIST = HTTP_ESSAY + "lists";
    public static String ESSAY_INFO = HTTP_ESSAY + "detail";

    public static String ESSAY_IS_COLLECT = HTTP_COLLECT + "isFavorite";
    public static String ESSAY_COLLECT = HTTP_COLLECT + "favorite";
    public static String ESSAY_UNCOLLECT = HTTP_COLLECT + "unFavorite";
    public static String ESSAY_SHARE = IP + "page/share_article?id=";

    /**********群聊模块***********/

    public static final int GROUP_ADMIN_FLAG=10;//管理员
    public static final int GROUP_TRAN_FLAG=11;//群转移
    public static final int GROUP_BLOCK_FLAG=12;//屏蔽

    public static String HTTP_GROUP = IP + "group/user/group/";
    //其他
    public static String HTTP_GENERAL = IP + "user/general/user/";

    //获取我加入的群
    public static String GROUP_MY=HTTP_GROUP+"my";
    //获取群成员
    public static String GROUP_MEMBER=HTTP_GROUP+"user";
    //获取群信息
    public static String GROUP_INFO=HTTP_GROUP+"info";
    //修改群信息
    public static String GROUP_EDIT=HTTP_GROUP+"edit";
    //创建群
    public static String GROUP_CREATE=HTTP_GROUP+"create";
    //退出群聊
    public static String GROUP_LEAVE=HTTP_GROUP+"leave";
    //解散群聊
    public static String GROUP_DISMISS=HTTP_GROUP+"dismiss";
    //拉人进群
    public static String GROUP_ADD=HTTP_GROUP+"laren";
    //踢出群聊
    public static String GROUP_MEMBER_REMOVE=HTTP_GROUP+"tiren";
    //转移群
    public static String GROUP_TRANSFER=HTTP_GROUP+"zengsong";

    //根据电话号码搜索用户
    public static String SEARCH_USER=HTTP_GENERAL+"search";

    //根据电话号码搜索用户
    public static String USER_INFO=HTTP_GENERAL+"info";
    //群聊屏蔽用户
    public static String BLOCK_USER_GROUP=HTTP_GROUP+"hiddenUser";
    //设置群管理员
    public static String SET_MANAGER_GROUP=HTTP_GROUP+"setManager";

    //重置IP
    public static void reSetIp(String type){
        switch (type){
            case TYPE_ZY:
                IP ="http://zyapp.app.xw518.com/";
                break;
            case TYPE_JQ:
                break;
            case TYPE_SC:
                break;
            case TYPE_NY:
                IP ="http://nyapp.app.xw518.com/";
                break;
        }

        //重置接口地址

        /**********圈子模块***********/
        HTTP_BBS = IP + "bbs/";

        CIRCLE_FORUM = HTTP_BBS + "general/forum/lists";
        CIRCLE_POST = HTTP_BBS + "user/thread/publish";
        CARD_LIST = HTTP_BBS + "general/thread/lists";
        CARD_INFO = HTTP_BBS + "general/thread/get";//帖子详情

        DIGG = IP + "digg/user/api/digg";//点赞
        UN_DIGG = IP + "digg/user/api/unDigg";//取消点赞

        COLLECT = IP + "favorite/user/api/favorite";//收藏
        UN_COLLECT = IP + "favorite/user/api/unFavorite";//取消收藏

        USEFUL_COMMENT = HTTP_BBS + "general/comment/recommended";//精选评论
        COMMENT = HTTP_BBS + "general/comment/lists";//评论
        COMMENT_INFO = HTTP_BBS + "general/comment/detail";//评论详情

        SEND_COMMENT = HTTP_BBS + "user/comment/publish";//发布评论

        DIGG_LIST = IP + "digg/general/api/lists";//点赞列表


        /*********************文章模块***************/

        HTTP_ESSAY = IP + "article/general/article/";
        HTTP_COLLECT = IP + "favorite/user/api/";

        ESSAY_CATALOG = IP + "article/general/category/lists";
        ESSAY_LIST = HTTP_ESSAY + "lists";
        ESSAY_INFO = HTTP_ESSAY + "detail";

        ESSAY_IS_COLLECT = HTTP_COLLECT + "isFavorite";
        ESSAY_COLLECT = HTTP_COLLECT + "favorite";
        ESSAY_UNCOLLECT = HTTP_COLLECT + "unFavorite";
        ESSAY_SHARE = IP + "page/share_article?id=";

        /**********群聊模块***********/
        HTTP_GROUP = IP + "group/user/group/";
        //其他
        HTTP_GENERAL = IP + "user/general/user/";

        //获取我加入的群
        GROUP_MY=HTTP_GROUP+"my";
        //获取群成员
        GROUP_MEMBER=HTTP_GROUP+"user";
        //获取群信息
        GROUP_INFO=HTTP_GROUP+"info";
        //修改群信息
        GROUP_EDIT=HTTP_GROUP+"edit";
        //创建群
        GROUP_CREATE=HTTP_GROUP+"create";
        //退出群聊
        GROUP_LEAVE=HTTP_GROUP+"leave";
        //解散群聊
        GROUP_DISMISS=HTTP_GROUP+"dismiss";
        //拉人进群
        GROUP_ADD=HTTP_GROUP+"laren";
        //踢出群聊
        GROUP_MEMBER_REMOVE=HTTP_GROUP+"tiren";
        //转移群
        GROUP_TRANSFER=HTTP_GROUP+"zengsong";

        //根据电话号码搜索用户
        SEARCH_USER=HTTP_GENERAL+"search";

        //根据电话号码搜索用户
        USER_INFO=HTTP_GENERAL+"info";
    }
}
