package com.xingwang.groupchat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.blankj.utilcode.util.ToastUtils;
import com.xingwang.groupchat.adapter.LookUpadapter;
import com.xingwang.groupchat.bean.Teammate;
import com.xingwang.groupchat.view.lookup.LookUpBean;
import com.xingwang.groupchat.view.lookup.LookUpRecyclerView;
import com.xingwang.swip.title.TopTitleView;

import java.util.ArrayList;
import java.util.List;

public class BuildGroupActivity extends BaseActivity {

    protected TopTitleView title;
    protected LookUpRecyclerView lv_list;

    private LookUpadapter staffAdapter;
    private List<LookUpBean> staffList = new ArrayList<>();
    //选中的员工
    private List<LookUpBean> staffSelectList = new ArrayList<>();
    //已是群 的成员
    private List<Teammate> groupStaffList = new ArrayList<>();
    //群成员id列表

    public static void getIntent(Context context) {
        Intent intent = new Intent(context, BuildGroupActivity.class);
       // intent.putExtra(Constants.INTENT_DATA,groupId);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getLayoutId() {
        return R.layout.groupchat_activity_build_group;
    }

    @Override
    protected void initView() {
        title = findViewById(R.id.title);
        lv_list = findViewById(R.id.lv_list);

        title.setRightFristText("确定");
        title.setRightFristTextColor(getResources().getColor(R.color.groupchat_text_grey));

        title.setRightFristClick(false);
        title.setOnRightFirstClickListener(v -> ToastUtils.showShortSafe("sure"));
    }

    @Override
    public void initData() {
        staffAdapter = new LookUpadapter(staffList);
    }
}
