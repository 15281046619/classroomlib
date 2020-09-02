package com.xingwang.groupchat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.xingwang.groupchat.adapter.GroupTransferAdapter;
import com.xingwang.groupchat.bean.Group;
import com.xingwang.groupchat.bean.Teammate;
import com.xingwang.groupchat.bean.User;
import com.xingwang.groupchat.callback.DialogAlertCallback;
import com.xingwang.groupchat.utils.DialogUtils;
import com.xingwang.groupchat.utils.HttpUtil;
import com.xingwang.swip.title.TopTitleView;
import com.xingwang.swip.utils.ActivityManager;
import com.xingwang.swip.utils.Constants;
import com.xingwang.swip.utils.JsonUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TransferActivity extends BaseActivity {

    protected TopTitleView title;
    protected TextView tv_empty;
    protected ListView list_group;

    private GroupTransferAdapter transferAdapter;
    private List<User> memberList = new ArrayList<>();

    private Group group;
    private HashMap<String,String> params=new HashMap<>();

    //功能标识
    private int flag;
    //提示消息
    private String hint;

    public static void getIntent(Context context, Group group,int flag) {
        Intent intent = new Intent(context, TransferActivity.class);
        intent.putExtra(Constants.INTENT_DATA,group);
        intent.putExtra(Constants.INTENT_DATA1,flag);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getLayoutId() {
        return R.layout.groupchat_activity_transfer;
    }

    @Override
    protected void initView() {
        title = findViewById(R.id.title);
        tv_empty = findViewById(R.id.tv_empty);
        list_group = findViewById(R.id.list_group);
    }

    @Override
    public void initData() {

        group=(Group)getIntent().getSerializableExtra(Constants.INTENT_DATA);

        flag=getIntent().getIntExtra(Constants.INTENT_DATA1,0);

        switch (flag){
            case Constants.GROUP_ADMIN_FLAG://管理员
                title.setTitleText("管理员");
                break;
            case Constants.GROUP_TRAN_FLAG://群转让
                title.setTitleText("群转让");
                break;
            case Constants.GROUP_BLOCK_FLAG://群屏蔽
                title.setTitleText("屏蔽");
                break;
        }

        transferAdapter=new GroupTransferAdapter(this,memberList);
        transferAdapter.setFlag(flag);
        list_group.setAdapter(transferAdapter);

        list_group.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {

                if (flag==Constants.GROUP_BLOCK_FLAG){//屏蔽成员消息
                    /*if (memberList.get(i).getIs_manager()==0){
                        blockGroup(memberList.get(i).getStrId(),memberList.get(i).getHiddenFlag());
                    }else {
                        ToastUtils.showShort("不可屏蔽管理员");
                    }*/
                    blockGroup(memberList.get(i).getStrId(),memberList.get(i).getHiddenFlag());
                    return;
                }

                switch (flag){
                    case Constants.GROUP_ADMIN_FLAG://管理员

                        if (memberList.get(i).getIs_manager()==0){//非管理员
                            hint="将设置"+memberList.get(i).getNickname()+"为管理员";
                        }else {
                            hint="将取消"+memberList.get(i).getNickname()+"管理员权限";
                        }

                        break;
                    case Constants.GROUP_TRAN_FLAG://群转让
                        hint="转让给"+memberList.get(i).getNickname()+"后，你将失去群主身份";
                        break;
                }

                DialogUtils.obtainCommonDialog(hint, new DialogAlertCallback() {
                    @Override
                    public void sure() {
                        if (flag==Constants.GROUP_ADMIN_FLAG){//群管理员
                            setAdmin(memberList.get(i).getStrId(),memberList.get(i).getManagerFlag());
                        }else {
                            transferGroup(memberList.get(i).getStrId());
                        }
                    }
                }).show(getSupportFragmentManager());
            }});
        getGroupMember();
    }

    //获取群成员
    private void getGroupMember(){
        HttpUtil.get(Constants.GROUP_MEMBER +"?group_id="+ group.getId(), new HttpUtil.HttpCallBack() {
            @Override
            public void onFailure(String message) {
                hideLoadingDialog();
                ToastUtils.showShortSafe(message);
            }

            @Override
            public void onSuccess(String json,String tag) {
                memberList.clear();
                memberList.addAll(JsonUtils.jsonToList(json, User.class));

                for (User user:memberList){
                    if (user.getId()==group.getUser_id()){//群主匹配
                        memberList.remove(user);
                        break;
                    }
                }

                if (EmptyUtils.isEmpty(memberList)){
                    list_group.setVisibility(View.GONE);
                    tv_empty.setVisibility(View.VISIBLE);
                }else {
                    list_group.setVisibility(View.VISIBLE);
                    tv_empty.setVisibility(View.GONE);
                }
                hideLoadingDialog();
                transferAdapter.notifyDataSetChanged();
            }
        });
    }

    //群转移
    private void transferGroup(String userId){
        showLoadingDialog();
        params.clear();
        params.put("group_id",group.getStrId());
        params.put("user_id",userId);
        HttpUtil.post(Constants.GROUP_TRANSFER, params, new HttpUtil.HttpCallBack() {
            @Override
            public void onFailure(String message) {
                hideLoadingDialog();
                ToastUtils.showShortSafe(message);
            }

            @Override
            public void onSuccess(String json, String tag) {
                hideLoadingDialog();
                ToastUtils.showShortSafe("操作成功!");
                ActivityManager.getInstance().finishActivity();
            }
        });
    }

    //屏蔽群成员消息
    private void blockGroup(String userId,int hidden){
        showLoadingDialog();
        params.clear();
        params.put("group_id",group.getStrId());
        params.put("user_id",userId);
        params.put("is_hidden",String.valueOf(hidden));
        HttpUtil.post(Constants.BLOCK_USER_GROUP, params, new HttpUtil.HttpCallBack() {
            @Override
            public void onFailure(String message) {
                hideLoadingDialog();
                ToastUtils.showShortSafe(message);
            }

            @Override
            public void onSuccess(String json, String tag) {
                getGroupMember();
            }
        });
    }

    //设置群管理员
    private void setAdmin(String userId,int manager){
        showLoadingDialog();
        params.clear();
        params.put("group_id",group.getStrId());
        params.put("user_id",userId);
        params.put("is_manager",String.valueOf(manager));
        HttpUtil.post(Constants.SET_MANAGER_GROUP, params, new HttpUtil.HttpCallBack() {
            @Override
            public void onFailure(String message) {
                hideLoadingDialog();
                ToastUtils.showShortSafe(message);
            }

            @Override
            public void onSuccess(String json, String tag) {
                getGroupMember();
            }
        });
    }
}
