package com.xingwang.circle;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beautydefinelibrary.BeautyDefine;
import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.wang.avi.AVLoadingIndicatorView;
import com.xingwang.circle.adapter.ChildCommentAdapter;
import com.xingwang.circle.base.BaseActivity;
import com.xingwang.circle.bean.CommentBean;
import com.xingwang.circle.bean.CommentRoot;
import com.xingwang.swip.utils.ActivityManager;
import com.xingwang.swip.utils.Constants;
import com.xingwang.circle.view.KeyBordHelper;
import com.xingwang.swip.StateFrameLayout;
import com.xingwang.swip.SwipeRefreshAdapterView;
import com.xingwang.swip.SwipeRefreshListView;
import com.xingwang.swip.title.TopTitleView;
import com.xingwang.swip.utils.GlideUtils;
import com.xingwang.swip.utils.HttpUtil;
import com.xingwang.swip.utils.JsonUtils;
import com.xingwang.swip.utils.NoDoubleClickUtils;
import com.xingwang.swip.view.NiceImageView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class CommentInfoActivity extends BaseActivity implements View.OnClickListener , OnRefreshListener, OnLoadMoreListener {

    protected TopTitleView title;
    protected SmartRefreshLayout swipe_comment;
    protected MaterialHeader swipe_header;
    protected RecyclerView recycler_comments;

    protected TextView tv_comment_to;
    protected EditText et_comment;
    protected TextView tv_post_comment;
    protected AVLoadingIndicatorView avi_load;

    //基础评论
    protected LinearLayout line_comment_body;
    protected LinearLayout line_user;
    protected RelativeLayout rela_avatar;
    protected NiceImageView img_head;
    protected LinearLayout line_label;
    protected TextView tv_username;
    protected TextView tv_comment;
    protected TextView tv_time;
    protected TextView tv_card;

    protected ChildCommentAdapter childCommentAdapter;
    protected List<CommentBean> childComments=new ArrayList<>();

    protected CommentBean topComment;//顶层评论

    private String forwardId="0";//最后评论id
    private String backwardId="0";//最前评论id
    private String pId;//被评论id
    private String etText;//评论内容
    protected HashMap<String,String> params=new HashMap<>();

    protected KeyBordHelper keyBordHelper;


    public static Intent getIntent(Context context, CommentBean commentBean) {
        Intent intent = new Intent(context, CommentInfoActivity.class);
        intent.putExtra(Constants.INTENT_DATA,commentBean);
        context.startActivity(intent);
        return intent;
    }
    public static Intent getIntent(Context context, String id) {
        Intent intent = new Intent(context, CommentInfoActivity.class);
        intent.putExtra(Constants.INTENT_DATA1,id);
        context.startActivity(intent);
        return intent;
    }

    @Override
    public int getLayoutId() {
        return R.layout.circle_activity_comment_info;
    }

    @Override
    protected void initView() {
        keyBordHelper=new KeyBordHelper(this);
        keyBordHelper.onCreate();

        title=findViewById(R.id.title);
        swipe_comment=findViewById(R.id.swipe_comment);
        recycler_comments=findViewById(R.id.recycler_comments);
        swipe_header=findViewById(R.id.swipe_header);

        tv_comment_to=findViewById(R.id.tv_comment_to);
        et_comment=findViewById(R.id.et_comment);
        tv_post_comment=findViewById(R.id.tv_post_comment);
        avi_load=findViewById(R.id.avi_load);

        //基础评论
        tv_comment=findViewById(R.id.tv_comment);
        line_comment_body=findViewById(R.id.line_comment_body);
        line_user=findViewById(R.id.line_user);
        line_label=findViewById(R.id.line_label);
        rela_avatar=findViewById(R.id.rela_avatar);
        tv_username=findViewById(R.id.tv_username);
        tv_time=findViewById(R.id.tv_time);
        img_head=findViewById(R.id.img_head);
        tv_card=findViewById(R.id.tv_card);

        line_comment_body.setOnClickListener(this);
        line_user.setOnClickListener(this);
        tv_post_comment.setOnClickListener(this);
        tv_card.setOnClickListener(this);

        title.setTitleText("评论详情");
        title.setOnBackClickListener(new TopTitleView.OnBackClickListener() {
            @Override
            public void onBackClickListener() {
                tv_comment_to.setVisibility(View.GONE);
                KeyboardUtils.hideSoftInput(et_comment);
                CommentInfoActivity.this.finish();
            }
        });

        swipe_header.setColorSchemeResources(R.color.reslib_colorAccent);
        swipe_comment.autoRefresh();
        swipe_comment.setEnableAutoLoadMore(false);
        swipe_comment.setOnLoadMoreListener(this);
        swipe_comment.setOnRefreshListener(this);

       keyBordHelper.setOnKeyBoardStatusChangeListener(new KeyBordHelper.OnKeyBoardStatusChangeListener() {
           @Override
           public void OnKeyBoardChanged(boolean visible, int keyBoardHeight) {
               if (visible){
                   tv_comment_to.setVisibility(View.VISIBLE);
               }else {
                   tv_comment_to.setVisibility(View.GONE);
               }
           }
       });
    }

    @Override
    public void initData() {

        Intent intent=getIntent();

        topComment= (CommentBean) intent.getSerializableExtra(Constants.INTENT_DATA);

        if (EmptyUtils.isEmpty(topComment)){//此时获取评论id

            pId=intent.getStringExtra(Constants.INTENT_DATA1);
            if (EmptyUtils.isEmpty(pId)){//此时uri
                Uri uri=intent.getData();
                if (EmptyUtils.isEmpty(uri)){
                    ToastUtils.showShortSafe("数据错误！");
                    this.finish();
                }
                pId=uri.getQueryParameter("id");
            }
            getCommentDetail();
        }else {
            initTopCommentBean();
        }

        childCommentAdapter=new ChildCommentAdapter(this,childComments);
        recycler_comments.setLayoutManager(new LinearLayoutManager(this));
        //添加分割线
        recycler_comments.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recycler_comments.setAdapter(childCommentAdapter);

        childCommentAdapter.setOnChildItemClickListener(new ChildCommentAdapter.OnChildItemClickListener() {
            @Override
            public void onClick(CommentBean commentBean) {
                tv_comment_to.setText("回复 "+commentBean.getUser().getNickname());
                tv_comment_to.setVisibility(View.VISIBLE);
                KeyboardUtils.showSoftInput(et_comment);
                pId=commentBean.getId();
            }
        });

    }
    //获取评论详情
    private void getCommentDetail(){
        params.clear();
        params.put("id",pId);

        HttpUtil.get(Constants.COMMENT_INFO, params, new HttpUtil.HttpCallBack() {
            @Override
            public void onFailure(String message) {
                ToastUtils.showShortSafe(message);
                swipe_comment.finishRefresh();
            }

            @Override
            public void onSuccess(String json) {
                CommentBean tempComment= JsonUtils.jsonToPojo(json,CommentBean.class);

                if (tempComment.getPid().equals("0")){//此时为基础评论
                    topComment=tempComment;
                    initTopCommentBean();
                }else {//此时为子评论，获取基础评论
                    pId=tempComment.getBid();
                    forwardId=tempComment.getId();//子评论置顶
                    childComments.add(tempComment);//将子评论加入子列表
                    getCommentDetail();
                }
            }
        });
    }

    private void getCommentInfo(){
        params.clear();
        params.put("thread_id",topComment.getThread_id());
        params.put("bid",topComment.getId());
        params.put("forward_div_id",forwardId);
        params.put("num","10");

        HttpUtil.get(Constants.COMMENT, params, new HttpUtil.HttpCallBack() {
            @Override
            public void onFailure(String message) {
                ToastUtils.showShortSafe(message);
                swipe_comment.finishRefresh();
                swipe_comment.finishLoadMore();
            }

            @Override
            public void onSuccess(String json) {
                swipe_comment.finishRefresh();
                swipe_comment.finishLoadMore();

                CommentRoot commentRoot = JsonUtils.jsonToPojo(json,CommentRoot.class);
                if (EmptyUtils.isEmpty(commentRoot)){
                    ToastUtils.showShortSafe("数据解析出错!");
                    return;
                }

                childComments.addAll(commentRoot.getComments());

                if (EmptyUtils.isEmpty(childComments)){
                    ToastUtils.showShortSafe("暂无数据");
                }else {
                    if (EmptyUtils.isEmpty(commentRoot.getComments())){
                        ToastUtils.showShortSafe("无新评论");
                    }

                    forwardId=childComments.get(childComments.size()-1).getId();
                    backwardId=childComments.get(0).getId();
                    childCommentAdapter.setDatas(childComments);

                }
            }
        });
    }

    private void getRefreshCommentInfo(){
        params.clear();
        params.put("thread_id",topComment.getThread_id());
        params.put("bid",topComment.getId());
        params.put("backward_div_id",backwardId);
        params.put("num","10");

        HttpUtil.get(Constants.COMMENT, params, new HttpUtil.HttpCallBack() {
            @Override
            public void onFailure(String message) {
                ToastUtils.showShortSafe(message);
                swipe_comment.finishRefresh();
            }

            @Override
            public void onSuccess(String json) {
                swipe_comment.finishRefresh();

                CommentRoot commentRoot = JsonUtils.jsonToPojo(json,CommentRoot.class);
                if (EmptyUtils.isEmpty(commentRoot)){
                    ToastUtils.showShortSafe("数据解析出错!");
                    return;
                }

                Collections.reverse(commentRoot.getComments());
                childComments.addAll(0,commentRoot.getComments());

                if (EmptyUtils.isEmpty(childComments)){
                    ToastUtils.showShortSafe("暂无数据");
                }else {
                    if (EmptyUtils.isEmpty(commentRoot.getComments())){
                        ToastUtils.showShortSafe("真的没有了!");
                        return;
                    }

                    forwardId=childComments.get(childComments.size()-1).getId();
                    backwardId=childComments.get(0).getId();
                    childCommentAdapter.notifyDataSetChanged();

                }
            }
        });
    }

    private void sendComment(){
        params.clear();
        params.put("thread_id",topComment.getThread_id());
        params.put("pid",pId);
        params.put("body",etText);

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

                tv_comment_to.setVisibility(View.GONE);
                KeyboardUtils.hideSoftInput(et_comment);
                CommentBean sendComment=JsonUtils.jsonToPojo(json,CommentBean.class);

                et_comment.setText("");
                tv_post_comment.setVisibility(View.VISIBLE);
                avi_load.setVisibility(View.GONE);
                avi_load.hide();

                if (sendComment.getState()==0){
                    ToastUtils.showShortSafe("审核中...");
                    return;
                }
                getCommentInfo();
                ToastUtils.showShortSafe("评论成功");
            }
        });
    }

    private void initTopCommentBean(){
        pId=topComment.getId();
        GlideUtils.loadPic(topComment.getUser().getAvatar(),img_head,getApplicationContext());
        tv_username.setText(topComment.getUser().getNickname());
        tv_comment.setText(topComment.getBody());
        tv_time.setText(topComment.getPublish_time());

        String badge=topComment.getUser().getBadge();

        line_label.removeAllViews();

        if (EmptyUtils.isNotEmpty(badge)){
            List<String> badges= Arrays.asList(badge.split(","));

            for (String label:badges){
                line_label.addView(BeautyDefine.getLabelUiFactoryDefine().getLabelUiFactory().getLabelView(this,label));
            }

            if (badge.contains("gov")){
                View govView=BeautyDefine.getBadgeUiFactoryDefine().getBadgeUiFactory().getBadgeView(this,"gov");
                rela_avatar.addView(govView);
            }
        }
        getCommentInfo();
    }

    @Override
    public void onClick(View v) {

        if (EmptyUtils.isEmpty(topComment)){
            ToastUtils.showShortSafe("数据加载中...");
            return;
        }

        if (NoDoubleClickUtils.isDoubleClick()){
            return;
        }

        int i = v.getId();
        if (i == R.id.line_comment_body) {//基础评论
            KeyboardUtils.showSoftInput(et_comment);
           // et_comment.setHint("");
            tv_comment_to.setText("请输入评论");
            tv_comment_to.setVisibility(View.VISIBLE);
            pId = topComment.getId();
        } else if (i == R.id.tv_post_comment) {//发布
            etText = et_comment.getText().toString().trim();
            if (EmptyUtils.isEmpty(etText)) {
                ToastUtils.showShortSafe("评论内容不可为空");
            } else {
                tv_post_comment.setVisibility(View.GONE);
                avi_load.setVisibility(View.VISIBLE);
                avi_load.show();
                sendComment();
            }
        } else if (i == R.id.et_comment) {
            pId =topComment.getId();
            tv_comment_to.setText("请输入评论");
            tv_comment_to.setVisibility(View.VISIBLE);
        }else if (i == R.id.line_user) {//用户详情
            BeautyDefine.getOpenPageDefine(this).toPersonal(Long.parseLong(topComment.getUser_id()));
        }else if (i == R.id.tv_card) {//用户详情
            ActivityManager.getInstance().finishActivity(CardDetailActivity.class);
            CardDetailActivity.getIntent(this,topComment.getThread_id());
            this.finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        keyBordHelper.onDestroy();
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        refreshLayout.getLayout().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (EmptyUtils.isNotEmpty(topComment)){
                    getCommentInfo();
                }else {
                    swipe_comment.finishLoadMore();
                }
            }
        }, 500);
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        refreshLayout.getLayout().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (EmptyUtils.isNotEmpty(topComment)){
                    getRefreshCommentInfo();
                }else {
                    swipe_comment.finishLoadMore();
                }
            }
        }, 1000);
    }
}
