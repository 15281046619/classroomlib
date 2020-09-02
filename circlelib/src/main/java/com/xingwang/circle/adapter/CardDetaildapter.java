package com.xingwang.circle.adapter;

import android.app.Activity;
import android.content.Context;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beautydefinelibrary.BeautyDefine;
import com.blankj.utilcode.constant.TimeConstants;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;

import com.danikula.videocache.HttpProxyCacheServer;
import com.xingwang.circle.CommentInfoActivity;
import com.xingwang.circle.DiggListActivity;
import com.xingwang.circle.PhotoPreviewActivity;
import com.xingwang.circle.R;
import com.xingwang.circle.bean.Card;
import com.xingwang.circle.bean.CardBody;
import com.xingwang.circle.bean.CardFileType;
import com.xingwang.circle.bean.CommentBean;
import com.xingwang.circle.bean.Customer;
import com.xingwang.circle.bean.Digg;
import com.xingwang.circle.bean.DiggRoot;
import com.xingwang.circle.bean.User;
import com.xingwang.circle.view.CustomerLinearLayoutManager;
import com.xingwang.circle.view.FlowsLayout;
import com.xingwang.circle.view.MostGridView;
import com.xingwang.circle.view.MostListView;
import com.xingwang.circle.view.MyJzvd;
import com.xingwang.circle.view.MyJzvdStd;

import com.xingwang.swip.utils.Constants;
import com.xingwang.swip.utils.GlideUtils;
import com.xingwang.swip.utils.HttpUtil;
import com.xingwang.swip.utils.JsonUtils;
import com.xingwang.swip.utils.NoDoubleClickUtils;
import com.xingwang.swip.view.NiceImageView;
import com.xingwreslib.beautyreslibrary.BeautyVideoCacheSer;
import com.xingwreslib.beautyreslibrary.BlogDiggInfo;
import com.xingwreslib.beautyreslibrary.BlogDiggLiveData;
import com.xingwreslib.beautyreslibrary.BlogFavoriteInfo;
import com.xingwreslib.beautyreslibrary.BlogFavoriteLiveData;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;


/**
 * description:
 * autour: zhaoyun
 * date: 2017/9/1 0001 15:58
 */
public class CardDetaildapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Activity context;
    private List<CommentBean> commentList=new ArrayList<>();

    public static final int CARD = 0;
    public static final int COMMENT = 1;
    private int itemType = 1;

    private Card card;
    protected boolean collect;
    protected boolean digg;
    protected HashMap<String,String> params=new HashMap<>();

    protected List<String> diggAvatarList=new ArrayList<>();

    private LayoutInflater inflater;

    private OnPCItemClickListener pcItemClickListener;

    public CardDetaildapter(Activity context) {
        this.context = context;
        inflater=LayoutInflater.from(context);
    }

    public void setDatas(List<CommentBean> datas,Card card) {
        this.commentList.clear();
        this.commentList.addAll(datas);
        this.card=card;
        notifyDataSetChanged();
    }

    public void setOnPcItemClickListener(OnPCItemClickListener pcItemClickListener) {
        this.pcItemClickListener = pcItemClickListener;
    }

    @Override
    public int getItemViewType(int position) {
        getLayoutResId(position);
        return itemType;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        switch (itemType){
            case CARD:
                view = inflater.inflate(R.layout.circle_card_info, viewGroup, false);
                return new CardViewHolder(view);
            case COMMENT:
                view = inflater.inflate(R.layout.circle_comment_item, viewGroup, false);
                return new CommentViewHolder(view);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof CardViewHolder){
            bindViewByCard(((CardViewHolder)viewHolder));
        }else if (viewHolder instanceof CommentViewHolder){
            bindViewByComment((CommentViewHolder)viewHolder,i-1);
        }
    }

    @Override
    public int getItemCount() {
        if (EmptyUtils.isEmpty(card))
            return 0;
        return commentList.size()+1;
    }


    private void bindViewByCard(CardViewHolder viewHolderCard) {

        CardBody cardBody=card.getCardBody();

        //添加标签
        if (EmptyUtils.isEmpty(card.getBadge())){//此时无标签
            viewHolderCard.recycler_tag.setVisibility(View.GONE);
        }else {
            viewHolderCard.recycler_tag.setVisibility(View.VISIBLE);

            List<String> tagList=Arrays.asList(card.getBadge().split(","));

            CardTagAdapter tagAdapter = new CardTagAdapter(context,tagList);
            //设置布局方式
            CustomerLinearLayoutManager customerManager=new CustomerLinearLayoutManager(context);
            customerManager.setOrientation(CustomerLinearLayoutManager.HORIZONTAL);
            viewHolderCard.recycler_tag.setLayoutManager(customerManager);
            viewHolderCard.recycler_tag.setAdapter(tagAdapter);
        }

        addBadge(viewHolderCard.line_label,viewHolderCard.rela_avatar,card.getUser().getBadge());
        //展示帖子
        //头像
        GlideUtils.loadPic(card.getUser().getAvatar(),viewHolderCard.img_head,context.getApplicationContext());
        viewHolderCard.tv_username.setText(card.getUser().getNickname());

        //收藏
        if (card.getIs_favorite()==0){
            collect=false;
            viewHolderCard.img_collects.setImageResource(R.drawable.reslib_ic_star_border);
            viewHolderCard.tv_collect.setText("未收藏");
        }else {
            collect=true;
            viewHolderCard.img_collects.setImageResource(R.drawable.reslib_ic_star);
            viewHolderCard.tv_collect.setText("已收藏");
        }
        //点赞
        if (card.getIs_digg()==0){
            digg=false;
            viewHolderCard. img_thumb.setImageResource(R.drawable.reslib_ic_thumb_up_border);
        }else {
            digg=true;
            viewHolderCard.img_thumb.setImageResource(R.drawable.reslib_ic_thumb_up);
        }

        //精选评论
        if (card.isUsefulComment()){
            viewHolderCard.line_useful_comment.setVisibility(View.VISIBLE);
            viewHolderCard.tv_useful_comment_num.setText("精选评论 "+card.getUsefulComments().size());
            UsefulCommentAdapter usefulCommentAdapter=new UsefulCommentAdapter(context,card.getUsefulComments());
            viewHolderCard.list_useful_comment.setAdapter(usefulCommentAdapter);
        }else {
            viewHolderCard.line_useful_comment.setVisibility(View.GONE);
        }

        //评论
        viewHolderCard.tv_comment_num.setText("评论 "+card.getComment_num());
        //帖子内容
        viewHolderCard.tv_card_text.setText(cardBody.getText());

        viewHolderCard.tv_card_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pcItemClickListener!=null)
                    pcItemClickListener.onClick(null,-1);
            }
        });

        switch (card.getType()){
            case CardFileType.IMG:
                viewHolderCard.line_video.setVisibility(View.GONE);
                viewHolderCard.gv_card_img.setVisibility(View.VISIBLE);

                CardImgAdapter imgAdapter=new CardImgAdapter(cardBody.getImgs(), context);
                viewHolderCard.gv_card_img.setAdapter(imgAdapter);

                viewHolderCard.gv_card_img.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent photoPickerPreviewIntent = new PhotoPreviewActivity.IntentBuilder(context)
                                .previewPhotos((ArrayList<String>) cardBody.getImgs()) // 当前预览的图片路径集合
                                .maxChooseCount(Constants.MAX_COUNT) // 图片选择张数的最大值
                                .currentPosition(position) // 当前预览图片的索引
                                .build();
                        context.startActivity(photoPickerPreviewIntent);
                    }
                });
                break;
            case CardFileType.VIDEO:
                //viewHolderCard.jzt_video.setUp(cardBody.getVideo(),"",MyJzvd.SCREEN_NORMAL);

                HttpProxyCacheServer proxy = BeautyVideoCacheSer.getInstance(context);
                //2.我们播放视频的时候会调用以下代码生成proxyUrl
                String proxyUrl = proxy.getProxyUrl(cardBody.getVideo());

                viewHolderCard.jzt_video.setUp(proxyUrl,"",MyJzvd.SCREEN_NORMAL);
                viewHolderCard.gv_card_img.setVisibility(View.GONE);
                viewHolderCard.line_video.setVisibility(View.VISIBLE);

                GlideUtils.loadPic(cardBody.getCover(),viewHolderCard.jzt_video.thumbImageView,context.getApplicationContext());
                break;
            case CardFileType.NONE:
                viewHolderCard.gv_card_img.setVisibility(View.GONE);
                viewHolderCard.line_video.setVisibility(View.GONE);
                break;
        }

        viewHolderCard.line_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BeautyDefine.getOpenPageDefine(context).toPersonal(card.getCustomer_id());
            }
        });

        viewHolderCard.line_collect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (collect) {
                    diggAndCollect(Constants.UN_COLLECT, CardFileType.UNCOLLECT,viewHolderCard);
                } else {
                    diggAndCollect(Constants.COLLECT, CardFileType.COLLECT,viewHolderCard);
                }
            }
        });

        viewHolderCard.line_digg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DiggListActivity.getIntent(context, card.getId());
            }
        });

        viewHolderCard.img_thumb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NoDoubleClickUtils.isDoubleClick()){
                    return;
                }
                if (digg) {
                    diggAndCollect(Constants.UN_DIGG, CardFileType.UNDIGG,viewHolderCard);
                } else {
                    diggAndCollect(Constants.DIGG, CardFileType.DIGG,viewHolderCard);
                }
            }
        });

        getDiggList(viewHolderCard);

    }

    private void bindViewByComment(CommentViewHolder viewHolderComment,int basePos) {
        CommentBean commentBean=commentList.get(basePos);
        viewHolderComment.tv_comment.setText(commentBean.getBody());
        viewHolderComment.tv_time.setText(commentBean.getPublish_time());

        User customer=commentBean.getUser();
        GlideUtils.loadPic(customer.getAvatar(),viewHolderComment.img_head,context.getApplicationContext());
        viewHolderComment.tv_username.setText(customer.getNickname());

        addBadge(viewHolderComment.line_label,viewHolderComment.rela_avatar,customer.getBadge());


        viewHolderComment.line_comment_body.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pcItemClickListener!=null)
                    pcItemClickListener.onClick(commentBean,basePos);
            }
        });

        //跳转至用户详情
        viewHolderComment.line_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BeautyDefine.getOpenPageDefine(ActivityUtils.getTopActivity()).toPersonal(Long.parseLong(commentBean.getUser_id()));
            }
        });

        if (commentBean.isChildComment()){//此时存在子评论
            viewHolderComment.line_child.setVisibility(View.VISIBLE);
            List<CommentBean> childComments=commentBean.getSub_comments();
            ChildCommentIInfoAdapter childCommentAdapter=new ChildCommentIInfoAdapter(context,childComments);
            viewHolderComment.list_child.setAdapter(childCommentAdapter);

            childCommentAdapter.setOnChildItemClickListener(new ChildCommentIInfoAdapter.OnChildItemClickListener() {
                @Override
                public void onClick(CommentBean commentBean) {
                    if (pcItemClickListener!=null)
                        pcItemClickListener.onClick(commentBean,basePos);
                }
            });

            viewHolderComment.line_all_reply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CommentInfoActivity.getIntent(context,commentBean);
                }
            });

            if (childComments.size()>2){//此时不完全展示
                viewHolderComment.line_all_reply.setVisibility(View.VISIBLE);
                viewHolderComment.tv_view_all_reply.setText("查看其他回复");
            }else {
                viewHolderComment.line_all_reply.setVisibility(View.GONE);
            }

        }else {
            viewHolderComment.line_child.setVisibility(View.GONE);
            viewHolderComment.line_all_reply.setVisibility(View.GONE);
        }
    }


    private void getDiggList(CardViewHolder viewHolderCard) {
        params.clear();
        params.put("type", "thread");
        params.put("relation_id",card.getId());
        params.put("num", String.valueOf(Constants.MAX_DIGG_NUM));

        HttpUtil.get(Constants.DIGG_LIST, params, new HttpUtil.HttpCallBack() {
            @Override
            public void onFailure(String message) {
                ToastUtils.showShortSafe(message);
            }

            @Override
            public void onSuccess(String json) {
                diggAvatarList.clear();

                DiggRoot diggRoot = JsonUtils.jsonToPojo(json, DiggRoot.class);
                if (EmptyUtils.isEmpty(diggRoot)) {
                    return;
                }
                if (EmptyUtils.isNotEmpty(diggRoot.getItems())) {
                    viewHolderCard.line_digg.setVisibility(View.VISIBLE);
                    List<Digg> diggList = diggRoot.getItems();

                    Collections.reverse(diggList);

                    for (Digg digg : diggList) {
                        diggAvatarList.add(digg.getUser().getAvatar());
                    }
                    viewHolderCard.flow_praise.setUrls(diggAvatarList);

                    if (diggRoot.getTotal()>Constants.MAX_DIGG_NUM){//大于4人缩写
                        viewHolderCard.tv_digg_num.setText("等"+diggRoot.getTotal()+"人点赞");
                    }else {
                        viewHolderCard.tv_digg_num.setText(diggRoot.getTotal()+"人点赞");
                    }

                    card.setDigg_num(diggRoot.getTotal());
                } else {
                    viewHolderCard.line_digg.setVisibility(View.GONE);
                }
            }
        });
    }
    /**收藏、点赞操作*/
    private void diggAndCollect(String url,int type,CardViewHolder viewHolderCard){
        params.clear();
        params.put("type","thread");
        params.put("relation_id",card.getId());
        HttpUtil.post(url, params, new HttpUtil.HttpCallBack() {
            @Override
            public void onFailure(String message) {
                ToastUtils.showShortSafe(message);
            }

            @Override
            public void onSuccess(String json) {
                switch (type){
                    case CardFileType.DIGG://点赞
                        getDiggList(viewHolderCard);
                        ToastUtils.showShortSafe("点赞成功");
                        digg=true;
                        viewHolderCard.img_thumb.setImageResource(R.drawable.reslib_ic_thumb_up);
                        break;
                    case CardFileType.UNDIGG://取消点赞
                        getDiggList(viewHolderCard);
                        ToastUtils.showShortSafe("取消点赞");
                        digg=false;
                        viewHolderCard.img_thumb.setImageResource(R.drawable.reslib_ic_thumb_up_border);
                        break;
                    case CardFileType.COLLECT://收藏
                        ToastUtils.showShortSafe("收藏成功");
                        collect=true;
                        viewHolderCard.tv_collect.setText("已收藏");
                        viewHolderCard.img_collects.setImageResource(R.drawable.reslib_ic_star);
                        break;
                    case CardFileType.UNCOLLECT://取消点赞
                        ToastUtils.showShortSafe("取消收藏");
                        collect=false;
                        viewHolderCard.tv_collect.setText("未收藏");
                        viewHolderCard.img_collects.setImageResource(R.drawable.reslib_ic_star_border);
                        break;
                }
                BlogDiggLiveData.getInstance().notifyInfoChanged(new BlogDiggInfo(Long.parseLong(card.getId()),
                        digg,-1));
                BlogFavoriteLiveData.getInstance().notifyInfoChanged(new BlogFavoriteInfo(Long.parseLong(card.getId()),
                        collect, -1));
            }
        });
    }

    class CommentViewHolder extends RecyclerView.ViewHolder {

        protected NiceImageView img_head;
        protected RelativeLayout rela_avatar;
        protected TextView tv_username;
        protected TextView tv_comment;
        protected TextView tv_time;
        protected LinearLayout line_label;
        protected ImageView img_comment;
        protected MostListView list_child;//子评论
        protected LinearLayout line_child;//子评论
        protected TextView tv_view_all_reply;//查看全部评论
        protected LinearLayout line_all_reply;//查看全部评论
        protected LinearLayout line_comment_body;//评论体
        protected LinearLayout line_user;//用户

        public CommentViewHolder(View view) {
            super(view);
            img_head =view.findViewById(R.id.img_head);
            rela_avatar =view.findViewById(R.id.rela_avatar);
            tv_username =view.findViewById(R.id.tv_username);
            tv_comment = view.findViewById(R.id.tv_comment);
            tv_time = view.findViewById(R.id.tv_time);
            line_label = view.findViewById(R.id.line_label);
            img_comment = view.findViewById(R.id.img_comment);
            list_child = view.findViewById(R.id.list_child);
            tv_view_all_reply =  view.findViewById(R.id.tv_view_all_reply);
            line_child = view.findViewById(R.id.line_child);
            line_all_reply = view.findViewById(R.id.line_all_reply);
            line_comment_body =view.findViewById(R.id.line_comment_body);
            line_user =view.findViewById(R.id.line_user);
        }

    }

    class CardViewHolder extends RecyclerView.ViewHolder {

        //标签
        protected RecyclerView recycler_tag;

        protected LinearLayout line_user;
        protected NiceImageView img_head;
        protected RelativeLayout rela_avatar;
        protected LinearLayout line_label;
        protected TextView tv_username;
        protected TextView tv_card_text;
        protected MyJzvdStd jzt_video;
        protected LinearLayout line_video;
        protected MostGridView gv_card_img;

        //收藏
        protected LinearLayout line_collect;
        protected ImageView img_collects;
        protected TextView tv_collect;

        // protected TextView tv_comment_empty;
        protected TextView tv_comment_num;

        //点赞
        protected ImageView img_thumb;
        //点赞列表
        protected FlowsLayout flow_praise;
        protected TextView tv_digg_num;
        protected LinearLayout line_digg;

        //精选评论
        protected LinearLayout line_useful_comment;
        protected TextView tv_useful_comment_num;
        protected MostListView list_useful_comment;

        public CardViewHolder(View convertView) {
            super(convertView);
            recycler_tag=convertView.findViewById(R.id.recycler_tag);

            img_head=convertView.findViewById(R.id.img_head);
            line_user=convertView.findViewById(R.id.line_user);
            line_label=convertView.findViewById(R.id.line_label);
            rela_avatar=convertView.findViewById(R.id.rela_avatar);
            tv_username=convertView.findViewById(R.id.tv_username);
            tv_card_text=convertView.findViewById(R.id.tv_card_text);
            jzt_video=convertView.findViewById(R.id.jzt_video);
            line_video=convertView.findViewById(R.id.line_video);
            gv_card_img=convertView.findViewById(R.id.gv_card_img);

            line_collect=convertView.findViewById(R.id.line_collect);
            img_collects=convertView.findViewById(R.id.img_collects);
            tv_collect=convertView.findViewById(R.id.tv_collect);

            img_thumb=convertView.findViewById(R.id.img_thumb);

            flow_praise=convertView.findViewById(R.id.flow_praise);
            tv_digg_num=convertView.findViewById(R.id.tv_digg_num);
            line_digg=convertView.findViewById(R.id.line_digg);

            tv_comment_num=convertView.findViewById(R.id.tv_comment_num);

            line_useful_comment=convertView.findViewById(R.id.line_useful_comment);
            tv_useful_comment_num=convertView.findViewById(R.id.tv_useful_comment_num);
            list_useful_comment=convertView.findViewById(R.id.list_useful_comment);
        }

    }

    private void getLayoutResId(int pos) {
        switch (pos){
            case 0://帖子详情
                itemType=CARD;
                break;
            default://评论
                itemType=COMMENT;
                break;
        }
    }


    public interface OnPCItemClickListener {
        //basePos-基础评论的pos
        void onClick(CommentBean commentBean, int basePos);
    }

    public void addBadge(LinearLayout line_label,RelativeLayout rela_avatar,String badge){

        if (EmptyUtils.isEmpty(badge))
            return;

        if(rela_avatar.getChildCount()==2){
            rela_avatar.removeViewAt(1);
        }

        line_label.removeAllViews();

        List<String> badges=Arrays.asList(badge.split(","));

        for (String label:badges){
            line_label.addView(BeautyDefine.getLabelUiFactoryDefine().getLabelUiFactory().getLabelView(context,label));
        }

        if (badge.contains("gov")){
            View govView=BeautyDefine.getBadgeUiFactoryDefine().getBadgeUiFactory().getBadgeView(context,"gov");
            rela_avatar.addView(govView);
        }
    }
}
