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
import com.xingwang.circle.adapter.ThirdLevelListAdapter;
import com.xingwang.circle.base.BaseActivity;
import com.xingwang.circle.bean.Forum;
import com.xingwang.circle.bean.ThirdLevel;
import com.xingwang.swip.title.TopTitleView;
import com.xingwang.swip.utils.Constants;
import com.xingwang.swip.utils.HttpUtil;
import com.xingwang.swip.utils.JsonUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TagSelectActivity extends BaseActivity {

    protected TopTitleView titleView;
    protected LinearLayout line_body;

    protected TextView tv_all_empty;
    //第一层级
    protected ListView list_first_circle;
    protected FirstLevelListAdapter firstAdapter;

    //第二层级
    protected ListView list_second_circle;
    protected TextView tv_second_empty;
    protected SecondLevelListAdapter secondAdapter;

    //第三层级
    protected ListView list_third_circle;
    protected TextView tv_third_empty;
    protected ThirdLevelListAdapter thirdAdapter;

    private List<Forum> forumList=new ArrayList<>();//所有的版块
    private List<Forum> firstLevels=new ArrayList<>();//第一层级
    private List<Forum> secondLevels=new ArrayList<>();//第二层级
    private List<ThirdLevel> thirdLevels=new ArrayList<>();//第三层级

    private Forum selectedForum;//栏目
    private int secondPos=0;//第二层级的位置
    private String categorys="";//栏目

    public static void getIntent(Context context) {
        Intent intent = new Intent(context, TagSelectActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getLayoutId() {
        return R.layout.circle_activity_tag_select;
    }

    @Override
    protected void initView() {
        titleView =findViewById(R.id.title);
        line_body =findViewById(R.id.line_body);
        tv_all_empty =findViewById(R.id.tv_all_empty);

        //第一层级
        list_first_circle =findViewById(R.id.list_first_circle);

        //第二层级
        list_second_circle =findViewById(R.id.list_second_circle);
        tv_second_empty =findViewById(R.id.tv_second_empty);

        //第三层级
        list_third_circle =findViewById(R.id.list_third_circle);
        tv_third_empty =findViewById(R.id.tv_third_empty);

        titleView.setRightFristText("确定", new TopTitleView.OnRightFirstClickListener() {
            @Override
            public void onRightFirstClickListener(View v) {
                if (EmptyUtils.isEmpty(categorys)){
                    ToastUtils.showShortSafe("请选择相关栏目");
                    return;
                }

                Intent intent=new Intent();
                intent.putExtra(Constants.INTENT_DATA,selectedForum);
                intent.putExtra(Constants.INTENT_DATA1,categorys);
                setResult(RESULT_OK,intent);
                TagSelectActivity.this.finish();
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
        titleView.setTitleText("栏目选择");

        //第一层级
        firstAdapter=new FirstLevelListAdapter(this,firstLevels);
        list_first_circle.setAdapter(firstAdapter);

        //第二层级
        secondAdapter=new SecondLevelListAdapter(this,secondLevels);
        list_second_circle.setAdapter(secondAdapter);

        //第三层级
        thirdAdapter=new ThirdLevelListAdapter(this,thirdLevels);
        list_third_circle.setAdapter(thirdAdapter);

        //第一层级item点击
        firstAdapter.setOnTvClickListener(new FirstLevelListAdapter.OnTvClickListener() {
            @Override
            public void onClick(int pos) {
                notifySecondDataChange(pos);
            }
        });

        list_second_circle.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                secondPos=position;
                notifyThirdDataChange(position);
            }
        });

        list_third_circle.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for (ThirdLevel thirdLevel:thirdLevels){
                    thirdLevel.setSelected(false);
                }

                categorys=thirdLevels.get(position).getCategorys();
                thirdLevels.get(position).setSelected(true);
                selectedForum=secondLevels.get(secondPos);

                thirdAdapter.notifyDataSetChanged();
            }
        });

        getForums();
    }


    private void getForums(){
        showLoadingDialog();
        HttpUtil.get(Constants.CIRCLE_FORUM, new HttpUtil.HttpCallBack() {
            @Override
            public void onFailure(String message) {
                ToastUtils.showShortSafe("请求失败");
                hideLoadingDialog();
            }

            @Override
            public void onSuccess(String json) {
                hideLoadingDialog();
                if (EmptyUtils.isEmpty(JsonUtils.jsonToList(json,Forum.class))){
                    line_body.setVisibility(View.GONE);
                    tv_all_empty.setVisibility(View.VISIBLE);
                    return;
                }

                line_body.setVisibility(View.VISIBLE);
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

                if (EmptyUtils.isEmpty(firstLevels)){
                    line_body.setVisibility(View.GONE);
                    tv_all_empty.setVisibility(View.VISIBLE);
                    return;
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

                firstAdapter.notifyDataSetChanged();

                notifySecondDataChange(0);
            }
        });
    }

    //提示二层级数据改变
    private void notifySecondDataChange(int pos){
        secondLevels.clear();
        if (EmptyUtils.isNotEmpty(firstLevels.get(pos).getChildForums())){//此时第二层级存在数据
            secondLevels.addAll(firstLevels.get(pos).getChildForums());
            tv_second_empty.setVisibility(View.GONE);
            list_second_circle.setVisibility(View.VISIBLE);

            notifyThirdDataChange(0);

            secondAdapter.notifyDataSetChanged();
        }else {
            tv_second_empty.setVisibility(View.VISIBLE);
            list_second_circle.setVisibility(View.GONE);
            //第三层级无数据
            tv_third_empty.setVisibility(View.VISIBLE);
            list_third_circle.setVisibility(View.GONE);
        }
    }

    //提示三层级数据改变
    private void notifyThirdDataChange(int pos){
        thirdLevels.clear();
        if (EmptyUtils.isNotEmpty(secondLevels.get(pos).getCategorys())){//此时第三层级存在数据

            for (String item:secondLevels.get(pos).getCategorys()){
                ThirdLevel thirdLevel=new ThirdLevel();
                thirdLevel.setCategorys(item);
                thirdLevel.setpId(secondLevels.get(pos).getId());

                if (EmptyUtils.isNotEmpty(selectedForum)
                        &&selectedForum.getId().equals(thirdLevel.getpId())
                &&categorys.equals(thirdLevel.getCategorys())){
                    thirdLevel.setSelected(true);
                }else {
                    thirdLevel.setSelected(false);
                }

                thirdLevels.add(thirdLevel);
            }

            tv_third_empty.setVisibility(View.GONE);
            list_third_circle.setVisibility(View.VISIBLE);
            thirdAdapter.notifyDataSetChanged();
        }else {
            tv_third_empty.setVisibility(View.VISIBLE);
            list_third_circle.setVisibility(View.GONE);
        }
    }
}
