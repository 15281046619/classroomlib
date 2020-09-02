package com.xingwang.groupchat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.beautydefinelibrary.BeautyDefine;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.xingwang.groupchat.adapter.GroupListAdapter;
import com.xingwang.groupchat.bean.Group;
import com.xingwang.groupchat.utils.HttpUtil;
import com.xingwang.swip.title.TopTitleView;
import com.xingwang.swip.utils.Constants;
import com.xingwang.swip.utils.JsonUtils;

import java.util.ArrayList;
import java.util.List;

public class GroupListActivity extends BaseActivity {

    protected TopTitleView title;
    protected ListView list_group;
    protected TextView tv_empty;

    private GroupListAdapter groupAdapter;

    private List<Group> groupList=new ArrayList<>();
    public static void getIntent(Context context) {
        Intent intent = new Intent(context, GroupListActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getLayoutId() {
        return R.layout.groupchat_activity_grouplist;
    }

    @Override
    protected void initView() {
        title = (TopTitleView) findViewById(R.id.title);
        list_group = (ListView) findViewById(R.id.list_group);
        tv_empty = (TextView) findViewById(R.id.tv_empty);

        title.setRightFristImg(R.drawable.groupchat_icon_add, new TopTitleView.OnRightFirstClickListener() {
            @Override
            public void onRightFirstClickListener(View v) {
               CreateGroupActivity.getIntent(GroupListActivity.this);
            }
        });
    }

    @Override
    public void initData() {

        groupAdapter=new GroupListAdapter(this,groupList);
        list_group.setAdapter(groupAdapter);

        list_group.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
               // BeautyDefine.getOpenPageDefine(GroupListActivity.this).toGroupChat(groupList.get(i).getId());
                GroupSettingActivity.getIntent(GroupListActivity.this,String.valueOf(groupList.get(i).getId()));
            }
        });
        showLoadingDialog();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getGroupList();
    }

    private void getGroupList(){
        HttpUtil.get(Constants.GROUP_MY, new HttpUtil.HttpCallBack() {
            @Override
            public void onFailure(String message) {
                tv_empty.setVisibility(View.VISIBLE);
                list_group.setVisibility(View.GONE);
                hideLoadingDialog();
                tv_empty.setText(message);
            }

            @Override
            public void onSuccess(String json,String tag) {
                hideLoadingDialog();
                groupList.clear();
                groupList.addAll(JsonUtils.jsonToList(json,Group.class));
                if (EmptyUtils.isEmpty(groupList)){
                    tv_empty.setVisibility(View.VISIBLE);
                    list_group.setVisibility(View.GONE);
                    return;
                }

                tv_empty.setVisibility(View.GONE);
                list_group.setVisibility(View.VISIBLE);

                groupAdapter.notifyDataSetChanged();
            }
        });
    }
}
