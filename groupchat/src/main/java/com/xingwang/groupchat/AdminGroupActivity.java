package com.xingwang.groupchat;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.xingwang.groupchat.bean.Group;
import com.xingwang.groupchat.callback.DialogAlertCallback;
import com.xingwang.groupchat.utils.DialogUtils;
import com.xingwang.swip.title.TopTitleView;
import com.xingwang.swip.utils.Constants;

public class AdminGroupActivity extends BaseActivity implements View.OnClickListener {

    protected TopTitleView title;

    protected RelativeLayout re_add_admin;
    protected RelativeLayout re_tran;
    protected RelativeLayout re_block_group;

    private Group group;

    public static void getIntent(Context context, Group group) {
        Intent intent = new Intent(context, AdminGroupActivity.class);
        intent.putExtra(Constants.INTENT_DATA,group);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_admin_group;
    }

    @Override
    protected void initView() {
        title=findViewById(R.id.title);

        re_add_admin=findViewById(R.id.re_add_admin);
        re_tran=findViewById(R.id.re_tran);
        re_block_group=findViewById(R.id.re_block_group);

        re_add_admin.setOnClickListener(this);
        re_tran.setOnClickListener(this);
        re_block_group.setOnClickListener(this);
    }

    @Override
    public void initData() {
        group=(Group)getIntent().getSerializableExtra(Constants.INTENT_DATA);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.re_add_admin) {
            TransferActivity.getIntent(AdminGroupActivity.this,group,Constants.GROUP_ADMIN_FLAG);
        } else if (id == R.id.re_tran) {
            TransferActivity.getIntent(AdminGroupActivity.this,group,Constants.GROUP_TRAN_FLAG);
        } else if (id == R.id.re_block_group) {
            TransferActivity.getIntent(AdminGroupActivity.this,group,Constants.GROUP_BLOCK_FLAG);
        }
    }
}
