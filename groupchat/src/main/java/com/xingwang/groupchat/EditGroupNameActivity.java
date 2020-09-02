package com.xingwang.groupchat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.xingwang.groupchat.bean.Group;
import com.xingwang.groupchat.utils.HttpUtil;
import com.xingwang.swip.title.TopTitleView;
import com.xingwang.swip.utils.ActivityManager;
import com.xingwang.swip.utils.Constants;
import com.xingwreslib.beautyreslibrary.GroupNameInfo;
import com.xingwreslib.beautyreslibrary.GroupNameLiveData;

import java.util.HashMap;

public class EditGroupNameActivity extends BaseActivity {

    protected TopTitleView title;
    protected EditText et_content;

    //群信息
    private Group group;
    private HashMap<String,String> params=new HashMap<>();

    private String newName;
    private String oldName;

    public static void getIntent(Context context,Group group) {
        Intent intent = new Intent(context, EditGroupNameActivity.class);
        intent.putExtra(Constants.INTENT_DATA,group);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getLayoutId() {
        return R.layout.groupchat_activity_edit_group_name;
    }

    @Override
    protected void initView() {
        title = findViewById(R.id.title);
        et_content = findViewById(R.id.et_content);

        title.setRightFristText("提交");
        title.setRightFristTextColor(getResources().getColor(R.color.groupchat_white));
        title.setOnRightFirstClickListener(new TopTitleView.OnRightFirstClickListener() {
            @Override
            public void onRightFirstClickListener(View v) {
                newName=et_content.getText().toString().trim();
                if (EmptyUtils.isEmpty(newName)){
                    ToastUtils.showShort("名称不可为空");
                }else if (oldName.equals(newName)){
                    ToastUtils.showShort("请更改内容后再提交");
                }else {
                    editGroupName();
                }
            }
        });
    }

    @Override
    public void initData() {
        group= (Group) getIntent().getSerializableExtra(Constants.INTENT_DATA);
        if (EmptyUtils.isEmpty(group)){
            ToastUtils.showShortSafe("群信息有误!");
            ActivityManager.getInstance().finishActivity();
            return;
        }

        if (group.getLeader(this)){
            title.setRightFristVisible(View.VISIBLE);
        }else {
            title.setRightFristVisible(View.GONE);
        }
        et_content.setFocusable(group.getLeader(this));

        et_content.setText(group.getTitle());
        oldName=group.getTitle();
    }
    //修改群名称
    private void editGroupName(){
        showLoadingDialog();
        params.clear();
        params.put("group_id",group.getStrId());
        params.put("title",newName);
        HttpUtil.post(Constants.GROUP_EDIT, params, new HttpUtil.HttpCallBack() {
            @Override
            public void onFailure(String message) {
                hideLoadingDialog();
                ToastUtils.showShortSafe(message);
            }

            @Override
            public void onSuccess(String json,String tag) {
                hideLoadingDialog();
                ToastUtils.showShortSafe("修改成功!");
                GroupNameLiveData.getInstance().notifyInfoChanged(new GroupNameInfo(group.getId(),newName));
                ActivityManager.getInstance().finishActivity();
            }
        });
    }
}
