package com.xingwang.circle;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.xingwang.circle.adapter.PartAdapter;
import com.xingwang.circle.base.BaseActivity;
import com.xingwang.circle.bean.Forum;
import com.xingwang.circle.bean.OnChildClickListener;
import com.xingwang.circle.bean.ThirdLevel;
import com.xingwang.swip.title.TopTitleView;
import com.xingwang.swip.utils.Constants;
import com.xingwang.swip.utils.HttpUtil;
import com.xingwang.swip.utils.JsonUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 栏目选择
 * */
public class PartSelctActivity extends BaseActivity {

    protected TopTitleView title;
    protected ListView list_forum;
    protected TextView tv_empty;
    protected PartAdapter partAdapter;

    private List<Forum> forumList=new ArrayList<>();//组织后的栏目
    private List<Forum> allForumList=new ArrayList<>();//所有栏目

    private int flag;//标明消息来源

    public static Intent getIntent(Context context,int flag) {
        Intent intent = new Intent(context, PartSelctActivity.class);
        intent.putExtra(Constants.INTENT_DATA,flag);
        context.startActivity(intent);
        return intent;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_part_selct;
    }

    @Override
    protected void initView() {
        title=findViewById(R.id.title);
        list_forum=findViewById(R.id.list_forum);
        tv_empty=findViewById(R.id.tv_empty);
    }

    @Override
    public void initData() {

        flag=getIntent().getIntExtra(Constants.INTENT_DATA,0);

        partAdapter=new PartAdapter(this,forumList);
        list_forum.setAdapter(partAdapter);

        partAdapter.setChildClickListener(new OnChildClickListener() {
            @Override
            public void onItemClick(String categorys) {

            }

            @Override
            public void onForumClick(Forum forum, String categorys) {

                switch (flag){
                    case Constants.ALL_CIRCLE://所有栏目
                        if (EmptyUtils.isEmpty(forum.getCategorys())){
                            ToastUtils.showShortSafe("暂无栏目");
                            return;
                        }
                        CardActivity.getIntent(PartSelctActivity.this, (ArrayList<String>) forum.getCategorys(),forum.getId());
                        break;
                    default://默认帖子
                        Intent intent=new Intent();
                        intent.putExtra(Constants.INTENT_DATA,forum);
                        intent.putExtra(Constants.INTENT_DATA1,categorys);
                        setResult(RESULT_OK,intent);
                        PartSelctActivity.this.finish();
                        break;
                }
            }
        });
        getForums();
    }

    private void getForums(){
        showLoadingDialog();
        HttpUtil.get(Constants.CIRCLE_FORUM, new HttpUtil.HttpCallBack() {
            @Override
            public void onFailure(String message) {
                errorSet(message);
                hideLoadingDialog();
            }

            @Override
            public void onSuccess(String json) {
                hideLoadingDialog();
                allForumList.clear();
                allForumList.addAll(JsonUtils.jsonToList(json,Forum.class));
                if (EmptyUtils.isEmpty(allForumList)){
                    errorSet("数据错误");
                    return;
                }

                forumList.clear();
                Iterator<Forum> iterator = allForumList.iterator();

                while (iterator.hasNext()){
                    Forum forum=iterator.next();
                    if (forum.getPid().equals("0")){//此时为第一层
                        forumList.add(forum);
                        iterator.remove();
                    }
                }

                if (EmptyUtils.isEmpty(forumList)){
                    errorSet("数据结构错误");
                    return;
                }

                tv_empty.setVisibility(View.GONE);
                list_forum.setVisibility(View.VISIBLE);

                //匹配栏目
                for(Forum forum:forumList){
                    partCast(allForumList,forum);
                }

                partAdapter.setDatsAndNotify(forumList);
            }
        });
    }

    /**
     * 匹配栏目
     * @param remianDataList 未匹配的栏目
     * @param parentForum 上一层级集合
     * */
    public void partCast(List<Forum> remianDataList,Forum parentForum){
        if (EmptyUtils.isNotEmpty(remianDataList)){
            //子级团队集合
            Iterator<Forum> iterator=remianDataList.iterator();
            List<Forum> childForums=new ArrayList<>();
            while (iterator.hasNext()){
                Forum forum=iterator.next();
                if (parentForum.getId().equals(forum.getPid())){
                    childForums.add(forum);
                    iterator.remove();
                }
            }
            parentForum.setChildForums(childForums);

            if (EmptyUtils.isNotEmpty(remianDataList)){
                for (Forum forum:childForums){
                    partCast(remianDataList,forum);
                }
            }
        }
    }

    //错误信息设置
    public void errorSet(String errorMg){
        tv_empty.setVisibility(View.VISIBLE);
        tv_empty.setText(errorMg);
        list_forum.setVisibility(View.GONE);
    }
}
