package com.xingwang.circle;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.beautydefinelibrary.BeautyDefine;
import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.wang.avi.AVLoadingIndicatorView;
import com.xingwang.circle.adapter.CardDetaildapter;
import com.xingwang.circle.base.BaseActivity;
import com.xingwang.circle.bean.Card;
import com.xingwang.circle.bean.CardBody;
import com.xingwang.circle.bean.CardFileType;
import com.xingwang.circle.bean.CommentBean;
import com.xingwang.circle.bean.CommentRoot;
import com.xingwang.circle.bean.Customer;
import com.xingwang.circle.bean.User;
import com.xingwang.circle.view.CustomerLinearLayoutManager;
import com.xingwang.circle.view.KeyBordHelper;
import com.xingwang.circle.view.MostGridView;
import com.xingwang.circle.view.MostListView;

import com.xingwang.circle.view.MyJzvd;
import com.xingwang.circle.view.MyJzvdStd;
import com.xingwang.circle.view.SwipeMostRefreshListView;
import com.xingwang.swip.SwipeRefreshAdapterView;
import com.xingwang.swip.SwipeRefreshListView;
import com.xingwang.swip.utils.Constants;
import com.xingwang.swip.title.TopTitleView;
import com.xingwang.swip.utils.GlideUtils;
import com.xingwang.swip.utils.HttpUtil;
import com.xingwang.swip.utils.JsonUtils;
import com.xingwang.swip.utils.NoDoubleClickUtils;
import com.xingwang.swip.view.NiceImageView;
import com.xingwreslib.beautyreslibrary.BlogDiggInfo;
import com.xingwreslib.beautyreslibrary.BlogDiggLiveData;
import com.xingwreslib.beautyreslibrary.BlogFavoriteInfo;
import com.xingwreslib.beautyreslibrary.BlogFavoriteLiveData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import cn.jzvd.Jzvd;

import static android.R.layout.simple_list_item_2;

public class CardDetailActivity extends BaseActivity implements View.OnClickListener, OnRefreshListener, OnLoadMoreListener {

    protected TopTitleView title;

    protected SmartRefreshLayout swipe_card;
    protected RecyclerView recycler_card;
    protected MaterialHeader swipe_header;
    //protected MostListView recycler_card;

    private String forwardId = "0";
    private CommentBean commentEd;//被评论的信息

    private CardDetaildapter cardDetaildapter;
    private List<CommentBean> commentBeans = new ArrayList<>();

    /**
     * 评论发送
     */
    protected EditText et_comment;
    protected TextView tv_post_comment;
    protected AVLoadingIndicatorView avi_load;
    protected TextView tv_comment_to;

    private String pId = "";//被评论id
    private String etText;//评论内容
    private int basePos;//基础评论的pos

    //分享
    protected LinearLayout line_share;

    //点赞头像列表
    protected List<String> diggAvatarList = new ArrayList<>();

    private Card card;//帖子
    private CardBody cardBody;//帖子内容
    private String id;
    private HashMap<String, String> params = new HashMap<>();

    protected KeyBordHelper keyBordHelper;
    protected View footerView;//底部布局

    public static Intent getIntent(Context context, String id) {
        Intent intent = new Intent(context, CardDetailActivity.class);
        intent.putExtra(Constants.INTENT_DATA, id);
        context.startActivity(intent);
        return intent;
    }

    public static Intent getIntentForResult(Activity activity, String id, int requestCode, int resultCode) {
        Intent intent = new Intent(activity, CardDetailActivity.class);
        intent.putExtra(Constants.INTENT_DATA, id);
        intent.putExtra(Constants.INTENT_DATA1, resultCode);
        activity.startActivityForResult(intent, requestCode);
        return intent;
    }

    public static Intent getIntentForResult(Activity activity, String id, int requestCode) {
        Intent intent = new Intent(activity, CardDetailActivity.class);
        intent.putExtra(Constants.INTENT_DATA, id);
        activity.startActivityForResult(intent, requestCode);
        return intent;
    }

    @Override
    public int getLayoutId() {
        return R.layout.circle_activity_card_detail;
    }

    @Override
    protected void initView() {
        keyBordHelper = new KeyBordHelper(this);
        keyBordHelper.onCreate();

        footerView = LayoutInflater.from(this).inflate(R.layout.circle_bottom_line, null);

        title = findViewById(R.id.title);

        swipe_card = findViewById(R.id.swipe_card);
        recycler_card = findViewById(R.id.recycler_card);
        swipe_header = findViewById(R.id.swipe_header);

        /**评论发送*/
        et_comment = findViewById(R.id.et_comment);
        tv_post_comment = findViewById(R.id.tv_post_comment);
        avi_load = findViewById(R.id.avi_load);
        tv_comment_to = findViewById(R.id.tv_comment_to);

        line_share = findViewById(R.id.line_share);

        line_share.setOnClickListener(this);
        tv_post_comment.setOnClickListener(this);

        title.setTitleText("帖子详情");

        swipe_header.setColorSchemeResources(R.color.reslib_colorAccent);
        swipe_card.setEnableAutoLoadMore(false);
        swipe_card.setOnLoadMoreListener(this);
        swipe_card.setOnRefreshListener(this);

        keyBordHelper.setOnKeyBoardStatusChangeListener(new KeyBordHelper.OnKeyBoardStatusChangeListener() {
            @Override
            public void OnKeyBoardChanged(boolean visible, int keyBoardHeight) {
                if (visible) {
                    tv_comment_to.setVisibility(View.VISIBLE);
                } else {
                    tv_comment_to.setVisibility(View.GONE);
                }
            }
        });
    }


    @Override
    public void initData() {
        cardDetaildapter = new CardDetaildapter(this);
        //设置布局方式
        recycler_card.setLayoutManager(new CustomerLinearLayoutManager(this));
        //添加分割线
        recycler_card.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recycler_card.setAdapter(cardDetaildapter);

        id = getIntent().getStringExtra(Constants.INTENT_DATA);

        if (EmptyUtils.isEmpty(id)){//uri获取
            Uri uri=getIntent().getData();
            if (EmptyUtils.isEmpty(uri)){
                ToastUtils.showShortSafe("数据错误！");
                this.finish();
            }
            id=uri.getQueryParameter("id");
        }

        showLoadingDialog();
        getCardInfo();

        title.setOnBackClickListener(new TopTitleView.OnBackClickListener() {
            @Override
            public void onBackClickListener() {
                tv_comment_to.setVisibility(View.GONE);
                KeyboardUtils.hideSoftInput(et_comment);
                CardDetailActivity.this.finish();
            }
        });

        cardDetaildapter.setOnPcItemClickListener(new CardDetaildapter.OnPCItemClickListener() {
            @Override
            public void onClick(CommentBean commentBean, int bPos) {

                KeyboardUtils.showSoftInput(et_comment);
                if (bPos == -1) {//此时评论帖子
                    pId = "";
                    tv_comment_to.setText("请输入评论");
                    tv_comment_to.setVisibility(View.VISIBLE);
                    return;
                }

                basePos = bPos;
                commentEd = commentBean;
                tv_comment_to.setText("回复 " + commentBean.getUser().getNickname());
                tv_comment_to.setVisibility(View.VISIBLE);
                pId = commentBean.getId();
            }
        });
    }

    /**
     * 获取帖子详情
     */
    private void getCardInfo() {
        params.clear();
        params.put("id", id);
        HttpUtil.get(Constants.CARD_INFO, params, new HttpUtil.HttpCallBack() {
            @Override
            public void onFailure(String message) {
                hideLoadingDialog();
                ToastUtils.showShortSafe(message);
            }

            @Override
            public void onSuccess(String json) {
                card = JsonUtils.jsonToPojo(json, Card.class);
                if (EmptyUtils.isEmpty(card)) {
                    ToastUtils.showShortSafe("内容解析错误");
                    return;
                }
                cardBody = card.getCardBody();
                if (EmptyUtils.isEmpty(cardBody)) {
                    ToastUtils.showShortSafe("内容解析错误");
                    return;
                }

                //重置评论
                forwardId = "0";
                commentBeans.clear();

                getUsefulComments();
            }
        });
    }

    /**
     * 获取评论
     */
    private void getUsefulComments() {
        params.clear();
        params.put("thread_id", id);

        HttpUtil.get(Constants.USEFUL_COMMENT, params, new HttpUtil.HttpCallBack() {
            @Override
            public void onFailure(String message) {
                hideLoadingDialog();
                ToastUtils.showShortSafe(message);
            }

            @Override
            public void onSuccess(String json) {
                CommentRoot commentRoot = JsonUtils.jsonToPojo(json, CommentRoot.class);
                card.setUsefulComments(commentRoot.getComments());
                getComments();
            }
        });
    }

    /**
     * 获取评论
     */
    private void getComments() {
        params.clear();
        params.put("thread_id", id);
        params.put("forward_div_id", forwardId);
        params.put("num", "10");

        HttpUtil.get(Constants.COMMENT, params, new HttpUtil.HttpCallBack() {
            @Override
            public void onFailure(String message) {
                ToastUtils.showShortSafe(message);
                swipe_card.finishRefresh();
                swipe_card.finishLoadMore();
                hideLoadingDialog();
            }

            @Override
            public void onSuccess(String json) {
                swipe_card.finishRefresh();
                swipe_card.finishLoadMore();
                hideLoadingDialog();

                CommentRoot commentRoot = JsonUtils.jsonToPojo(json, CommentRoot.class);
                if (EmptyUtils.isEmpty(commentRoot)) {
                    if (EmptyUtils.isEmpty(commentBeans)) {
                        ToastUtils.showShortSafe("出错了～～");
                        return;
                    }
                    ToastUtils.showShortSafe("数据解析错误");
                }

                commentBeans.addAll(commentRoot.getComments());

                if (EmptyUtils.isNotEmpty(commentBeans)) {

                    forwardId = commentBeans.get(commentBeans.size() - 1).getId();
                    if (EmptyUtils.isEmpty(commentRoot.getComments())) {//拉取数据未空||发送
                        ToastUtils.showShortSafe("真没有了～");
                    }
                } else {
                    ToastUtils.showShortSafe("还没有评论");
                }
                cardDetaildapter.setDatas(commentBeans, card);
            }
        });
    }

    //发送评论
    private void sendComment() {
        params.clear();
        params.put("thread_id", card.getId());
        params.put("pid", pId);
        params.put("body", etText);

        HttpUtil.post(Constants.SEND_COMMENT, params, new HttpUtil.HttpCallBack() {
            @Override
            public void onFailure(String message) {
                ToastUtils.showShortSafe(message);
                tv_post_comment.setVisibility(View.VISIBLE);
                avi_load.setVisibility(View.GONE);
                avi_load.hide();
            }

            @Override
            public void onSuccess(String json) {

                KeyboardUtils.hideSoftInput(et_comment);
                CommentBean sendComment = JsonUtils.jsonToPojo(json, CommentBean.class);

                tv_comment_to.setVisibility(View.GONE);
                et_comment.setText("");
                tv_post_comment.setVisibility(View.VISIBLE);
                avi_load.setVisibility(View.GONE);
                avi_load.hide();

                if (sendComment.getState() != 1) {
                    ToastUtils.showShortSafe("审核通过后展示");
                    return;
                }
                if (EmptyUtils.isNotEmpty(pId)) {//此时评论回复
                    generateComment(sendComment.getComment_id(), etText);
                } else {
                    getComments();
                }
                ToastUtils.showShortSafe("评论成功");
            }
        });
    }

    //生成评论
    private void generateComment(String id, String etText) {
        CommentBean childComment = new CommentBean();
        childComment.setId(id);
        childComment.setThread_id(card.getId());
        childComment.setBody(etText);
        childComment.setBid(commentEd.getBid());
        childComment.setPid(commentEd.getId());
        childComment.setUser_id(BeautyDefine.getUserInfoDefine(this).getUserId());
        childComment.setTo_user_id(commentEd.getUser_id());
        childComment.setPublish_time(TimeUtils.millis2String(System.currentTimeMillis()));

        User mine = new User();
        mine.setId(Long.valueOf(BeautyDefine.getUserInfoDefine(this).getUserId()));
        mine.setNickname(BeautyDefine.getUserInfoDefine(this).getNickName());
        mine.setAvatar(BeautyDefine.getUserInfoDefine(this).getUserHeadUrl());
        mine.setPhone(BeautyDefine.getUserInfoDefine(this).getPhone());

        childComment.setTo_user(commentEd.getUser());
        childComment.setUser(mine);

        if (commentBeans.get(basePos).isChildComment()) {//有子评论
            commentBeans.get(basePos).getSub_comments().add(childComment);
        } else {//此时无子评论
            List<CommentBean> tempComments = new ArrayList<>();
            tempComments.add(childComment);
            commentBeans.get(basePos).setSub_comments(tempComments);
        }

        cardDetaildapter.notifyItemChanged(basePos + 1, commentBeans.get(basePos));
    }

    @Override
    public void onClick(View v) {

        if (EmptyUtils.isEmpty(card)) {
            ToastUtils.showShortSafe("数据加载中...");
            return;
        }

        if (NoDoubleClickUtils.isDoubleClick()) {
            return;
        }

        int i = v.getId();
        if (i == R.id.tv_post_comment) {//提交
            etText = et_comment.getText().toString().trim();
            if (EmptyUtils.isEmpty(etText)) {
                ToastUtils.showShortSafe("评论内容不可为空");
            } else {
                tv_post_comment.setVisibility(View.GONE);
                avi_load.setVisibility(View.VISIBLE);
                avi_load.show();
                sendComment();
            }
        }
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {

        refreshLayout.getLayout().postDelayed(new Runnable() {
            @Override
            public void run() {
                getCardInfo();
            }
        }, 1000);
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        refreshLayout.getLayout().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (EmptyUtils.isNotEmpty(card)) {
                    getComments();
                }
            }
        }, 1000);
    }

    @Override
    public void onBackPressed() {
        if (MyJzvd.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MyJzvd.FULLSCREEN_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
        MyJzvd.NORMAL_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
    }

    @Override
    protected void onPause() {
        super.onPause();
        MyJzvd.resetAllVideos();
        MyJzvd.FULLSCREEN_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE;
        MyJzvd.NORMAL_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        keyBordHelper.onDestroy();
        diggAvatarList.clear();
    }
}
