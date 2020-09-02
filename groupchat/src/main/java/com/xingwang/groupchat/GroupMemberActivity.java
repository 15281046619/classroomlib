package com.xingwang.groupchat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beautydefinelibrary.BeautyDefine;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.xingwang.groupchat.adapter.GroupMemberListAdapter;
import com.xingwang.groupchat.bean.Group;
import com.xingwang.groupchat.bean.Teammate;
import com.xingwang.groupchat.bean.User;
import com.xingwang.groupchat.callback.DialogAlertCallback;
import com.xingwang.groupchat.callback.OnItemClickListener;
import com.xingwang.groupchat.utils.DialogUtils;
import com.xingwang.groupchat.utils.HttpUtil;
import com.xingwang.swip.title.TopTitleView;
import com.xingwang.swip.utils.ActivityManager;
import com.xingwang.swip.utils.Constants;
import com.xingwang.swip.utils.GlideUtils;
import com.xingwang.swip.utils.JsonUtils;
import com.xingwreslib.beautyreslibrary.GroupMemsInfo;
import com.xingwreslib.beautyreslibrary.GroupMemsLiveData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GroupMemberActivity extends BaseActivity implements View.OnClickListener {

    protected RecyclerView lv_list;
    protected TopTitleView title;

    protected LinearLayout line_group_owner;
    //群主头像
    protected ImageView img_manager;
    //群主名字
    protected TextView tv_manager_name;

    private GroupMemberListAdapter memberListAdapter;

    private List<User> memberList = new ArrayList<>();
    //删除用户个数
    private int leaveCount=0;
    //private List<String> selectUserIdList = new ArrayList<>();

    private Group group;//群信息
    private int count=0;//计数
    private HashMap<String,String> params=new HashMap<>();

    private boolean isStop=false;//上传删除群员失败

    //通知群员变化
    private ArrayList<GroupMemsInfo.Mem> leavasList=new ArrayList<>();
    private GroupMemsInfo memsInfo;

    public static void getIntent(Context context, Group group) {
        Intent intent = new Intent(context, GroupMemberActivity.class);
        intent.putExtra(Constants.INTENT_DATA,group);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getLayoutId() {
        return R.layout.groupchat_activity_group_member;
    }

    @Override
    protected void initView() {
        title = findViewById(R.id.title);
        lv_list = findViewById(R.id.lv_list);
        line_group_owner = findViewById(R.id.line_group_owner);
        img_manager = findViewById(R.id.img_manager);
        tv_manager_name = findViewById(R.id.tv_manager_name);

        line_group_owner.setOnClickListener(this);
        lv_list.setLayoutManager( new LinearLayoutManager(this));

        memberListAdapter = new GroupMemberListAdapter(this, memberList);
        lv_list.setAdapter(memberListAdapter);

        //如果add两个，那么按照先后顺序，依次渲染。
        //设置分割线
        lv_list.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

    @Override
    public void initData() {

        group=(Group)getIntent().getSerializableExtra(Constants.INTENT_DATA);

        memsInfo=new GroupMemsInfo(group.getId(),leavasList);

        memberListAdapter.setBoxClickListener(new GroupMemberListAdapter.OnCheckBoxClickListener() {
            @Override
            public void onCheckClick(int position) {
                if (memberList.get(position).isSelect()){
                    leaveCount++;
                    //selectUserIdList.add(memberList.get(position).getStrId());
                }else {
                    leaveCount--;
                    //selectUserIdList.remove(memberList.get(position).getStrId());
                }
                if (leaveCount==0){//此时不删除
                    title.setRightFristVisible(View.GONE);
                }else {
                    title.setRightFristVisible(View.VISIBLE);
                    title.setRightFristText("删除("+leaveCount+")");
                }
            }
        });

        memberListAdapter.setOnItemClickListener(new OnItemClickListener(){
            @Override
            public void onItemClick(int position) {
                BeautyDefine.getOpenPageDefine(GroupMemberActivity.this).toPersonal(memberList.get(position).getId());
                //ToastUtils.showShortSafe("click"+position);
            }
        });

        title.setOnRightFirstClickListener(new TopTitleView.OnRightFirstClickListener() {
            @Override
            public void onRightFirstClickListener(View v) {
                DialogUtils.obtainCommonDialog("确认要选中的用户从本群中移除?", new DialogAlertCallback() {
                    @Override
                    public void sure() {
                        isStop=false;
                        for (User user:memberList){
                            if (user.isSelect()){
                                GroupMemsInfo.Mem me=memsInfo.new Mem(user.getStrId(),user.getNickname(),user.getAvatar());
                                leavasList.add(me);
                                removeGroup(user.getStrId());
                            }
                        }
                    }
                }).show(getSupportFragmentManager());
            }
        });

        getGroupMember();
    }

    //获取群成员
    private void getGroupMember(){
        HttpUtil.get(Constants.GROUP_MEMBER +"?group_id="+ group.getId(), new HttpUtil.HttpCallBack() {
            @Override
            public void onFailure(String message) {
                ToastUtils.showShortSafe(message);
            }

            @Override
            public void onSuccess(String json,String tag) {
                memberList.clear();
                memberList.addAll(JsonUtils.jsonToList(json,User.class));

                for (User user:memberList){
                    if (user.getId()==group.getUser_id()){//群主匹配
                        memberList.remove(user);
                        GlideUtils.loadAvatar(user.getAvatar(),img_manager,getApplicationContext());
                        tv_manager_name.setText(user.getNickname());
                        break;
                    }
                }

                memberListAdapter.setLeader(group.getLeader(GroupMemberActivity.this));
                memberListAdapter.notifyDataSetChanged();
            }
        });
    }

    //移出群聊
    public void removeGroup(String userId){
        showLoadingDialog();
        params.clear();
        params.put("group_id",group.getStrId());
        params.put("user_id",userId);
        HttpUtil.post(Constants.GROUP_MEMBER_REMOVE, params, new HttpUtil.HttpCallBack() {
            @Override
            public void onFailure(String message) {

                if (!isStop){
                    ToastUtils.showShortSafe(message);
                }
                isStop=true;
                hideLoadingDialog();
            }

            @Override
            public void onSuccess(String json, String tag) {

                if (isStop){
                    hideLoadingDialog();
                    return;
                }

                if (++count==leaveCount){
                    hideLoadingDialog();
                    ToastUtils.showShortSafe("移除成功!");
                    GroupMemsLiveData.getInstance().notifyInfoChanged(memsInfo);
                    ActivityManager.getInstance().finishActivity();
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.line_group_owner) {
            BeautyDefine.getOpenPageDefine(GroupMemberActivity.this).toPersonal(group.getUser_id());
        }
    }
}
