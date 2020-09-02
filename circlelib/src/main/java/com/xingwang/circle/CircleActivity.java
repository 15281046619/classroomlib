package com.xingwang.circle;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.xingwang.circle.adapter.FirstLevelListAdapter;
import com.xingwang.circle.adapter.SecondLevelListAdapter;
import com.xingwang.circle.base.BaseActivity;
import com.xingwang.circle.bean.Forum;
import com.xingwang.swip.utils.Constants;
import com.xingwang.swip.title.TopTitleView;
import com.xingwang.swip.utils.HttpUtil;
import com.xingwang.swip.utils.JsonUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CircleActivity extends BaseActivity {

    protected TopTitleView titleView;
    protected ListView list_first_circle;
    protected ListView list_second_circle;
    protected TextView tv_empty;
    protected TextView tv_all_empty;
    protected LinearLayout line_content;

    private FirstLevelListAdapter firstLevelListAdapter;
    private SecondLevelListAdapter secondLevelListAdapter;

    private List<Forum> forumList=new ArrayList<>();//所有的版块
    private List<Forum> firstLevels=new ArrayList<>();//第一层级
    private List<Forum> secondLevels=new ArrayList<>();//第二层级

    public static Intent getIntent(Context context) {
        Intent intent = new Intent(context, CircleActivity.class);
        context.startActivity(intent);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getLayoutId() {
        return R.layout.circle_activity_circle;
    }

    @Override
    protected void initView() {
        titleView = (TopTitleView) findViewById(R.id.title);
        list_first_circle = (ListView) findViewById(R.id.list_first_circle);
        list_second_circle = (ListView) findViewById(R.id.list_second_circle);
        tv_empty = (TextView) findViewById(R.id.tv_empty);
        tv_all_empty = (TextView) findViewById(R.id.tv_all_empty);
        line_content = (LinearLayout) findViewById(R.id.line_content);

        titleView.setTitleText("圈子");

        list_second_circle.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (EmptyUtils.isEmpty(secondLevels.get(position).getCategorys())){
                    ToastUtils.showShortSafe("暂无栏目");
                    return;
                }
                CardActivity.getIntent(CircleActivity.this, (ArrayList<String>) secondLevels.get(position).getCategorys(),secondLevels.get(position).getId());
            }
        });

        tv_all_empty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getForums();
            }
        });
    }

    @Override
    public void initData() {
        firstLevelListAdapter=new FirstLevelListAdapter(this,firstLevels);
        list_first_circle.setAdapter(firstLevelListAdapter);
        //第一层级item点击
        firstLevelListAdapter.setOnTvClickListener(new FirstLevelListAdapter.OnTvClickListener() {
            @Override
            public void onClick(int pos) {
               secondLevels.clear();

               if (EmptyUtils.isNotEmpty(firstLevels.get(pos).getChildForums())){//此时有数据

                   tv_empty.setVisibility(View.GONE);
                   list_second_circle.setVisibility(View.VISIBLE);

                   secondLevels.addAll(firstLevels.get(pos).getChildForums());
                   secondLevelListAdapter.notifyDataSetChanged();
               }else {//此时无数据
                   tv_empty.setVisibility(View.VISIBLE);
                   list_second_circle.setVisibility(View.GONE);
               }
            }
        });
        //第二层级
        secondLevelListAdapter=new SecondLevelListAdapter(this,secondLevels);
        list_second_circle.setAdapter(secondLevelListAdapter);

        getForums();
    }

    private void getForums(){
        showLoadingDialog();
        HttpUtil.get(Constants.CIRCLE_FORUM, new HttpUtil.HttpCallBack() {
            @Override
            public void onFailure(String message) {
                ToastUtils.showShortSafe(message);
                hideLoadingDialog();
            }

            @Override
            public void onSuccess(String json) {
                hideLoadingDialog();
                if (EmptyUtils.isEmpty(JsonUtils.jsonToList(json,Forum.class))){
                    line_content.setVisibility(View.GONE);
                    tv_all_empty.setVisibility(View.VISIBLE);
                    return;
                }

                line_content.setVisibility(View.VISIBLE);
                tv_all_empty.setVisibility(View.GONE);

                forumList.clear();
                firstLevels.clear();
                secondLevels.clear();
                forumList.addAll(JsonUtils.jsonToList(json,Forum.class));

                Iterator<Forum> allForums = forumList.iterator();

                //设置父级列表
                while (allForums.hasNext()){
                    Forum forum=allForums.next();
                    if (forum.getPid().equals("0")){//此时为第一层
                        firstLevels.add(forum);
                        allForums.remove();
                    }
                }

                //设置子级列表
                for (Forum pForum:firstLevels){
                    if (forumList.size()!=0){
                        Iterator<Forum> tempForum=forumList.iterator();
                        List<Forum> childForums=new ArrayList<>();
                        while (tempForum.hasNext()){
                            Forum forum=tempForum.next();
                            if (forum.getPid().equals(pForum.getId())){
                                childForums.add(forum);
                                tempForum.remove();
                            }
                        }
                        pForum.setChildForums(childForums);
                    }else {
                        break;
                    }
                }

                if (EmptyUtils.isEmpty(firstLevels)){
                    line_content.setVisibility(View.GONE);
                    tv_all_empty.setVisibility(View.VISIBLE);
                    return;
                }

                firstLevelListAdapter.notifyDataSetChanged();
                if (EmptyUtils.isNotEmpty(firstLevels.get(0).getChildForums())){
                    secondLevels.addAll(firstLevels.get(0).getChildForums());
                    tv_empty.setVisibility(View.GONE);
                    list_second_circle.setVisibility(View.VISIBLE);
                    secondLevelListAdapter.notifyDataSetChanged();
                }else {
                    tv_empty.setVisibility(View.VISIBLE);
                    list_second_circle.setVisibility(View.GONE);
                }

                hideLoadingDialog();
            }
        });
    }
}
