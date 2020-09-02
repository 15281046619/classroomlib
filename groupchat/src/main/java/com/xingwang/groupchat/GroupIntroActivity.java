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

import java.util.HashMap;

public class GroupIntroActivity extends BaseActivity {
    protected TopTitleView title;
    protected EditText et_content;
    //群信息
    private Group group;
    private HashMap<String,String> params=new HashMap<>();

    private String newIntro;
    private String oldIntro;

    public static void getIntent(Context context,Group group) {
        Intent intent = new Intent(context, GroupIntroActivity.class);
        intent.putExtra(Constants.INTENT_DATA,group);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getLayoutId() {
        return R.layout.groupchat_activity_group_intro;
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
                newIntro=et_content.getText().toString().trim();
                if (EmptyUtils.isEmpty(newIntro)){
                    ToastUtils.showShort("名称不可为空");
                }else if (oldIntro.equals(newIntro)){
                    ToastUtils.showShort("请更改内容后再提交");
                }else {
                    editGroupIntro();
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
            et_content.setHint("请填写简介");
            title.setRightFristVisible(View.VISIBLE);
        }else {
            et_content.setHint("本群暂无介绍");
            title.setRightFristVisible(View.GONE);
        }
        et_content.setFocusable(group.getLeader(this));

        oldIntro=group.getIntroduction();
        et_content.setText(oldIntro);

    }

    //修改群名称
    private void editGroupIntro(){
        showLoadingDialog();
        params.clear();
        params.put("group_id",String.valueOf(group.getId()));
        params.put("introduction",newIntro);
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
                ActivityManager.getInstance().finishActivity();
            }
        });
    }
}
