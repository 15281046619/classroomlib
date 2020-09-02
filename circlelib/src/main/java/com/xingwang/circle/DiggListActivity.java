package com.xingwang.circle;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;

import com.beautydefinelibrary.BeautyDefine;
import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.xingwang.circle.adapter.DiggListAdapter;
import com.xingwang.circle.base.BaseActivity;
import com.xingwang.circle.bean.Digg;
import com.xingwang.circle.bean.DiggRoot;
import com.xingwang.swip.utils.Constants;
import com.xingwang.swip.StateFrameLayout;
import com.xingwang.swip.SwipeRefreshAdapterView;
import com.xingwang.swip.SwipeRefreshListView;
import com.xingwang.swip.title.TopTitleView;
import com.xingwang.swip.utils.HttpUtil;
import com.xingwang.swip.utils.JsonUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DiggListActivity extends BaseActivity {

    protected TopTitleView title;
    protected StateFrameLayout status_layout;
    protected SwipeRefreshListView list_digg;

    private HashMap<String,String> params=new HashMap<>();
    private List<Digg> diggList=new ArrayList<>();

    private DiggListAdapter diggAdapter;
    private String id;
    private String indexId="0";//索引id

    public static Intent getIntent(Context context, String id) {
        Intent intent = new Intent(context, DiggListActivity.class);
        intent.putExtra(Constants.INTENT_DATA,id);
        context.startActivity(intent);
        return intent;
    }

    @Override
    public int getLayoutId() {
        return R.layout.circle_activity_digg_list;
    }

    @Override
    public void initData() {

        title.setTitleText("点赞列表");

        id=getIntent().getStringExtra(Constants.INTENT_DATA);

        list_digg.setRefreshing(true);
        list_digg.setEnabledLoad(true);
        list_digg.autoRefresh(new int[]{R.color.reslib_colorAccent});
        list_digg.getLoadConfig().setLoadText("");

        diggAdapter=new DiggListAdapter(this,diggList);
        list_digg.setAdapter(diggAdapter);

        list_digg.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BeautyDefine.getOpenPageDefine(DiggListActivity.this).toPersonal(diggList.get(position).getUser_id());
            }
        });

        list_digg.setOnListLoadListener(new SwipeRefreshAdapterView.OnListLoadListener() {
            @Override
            public void onListLoad() {
                getDiggList();
            }
        });

        getDiggList();
    }

    @Override
    protected void initView() {
        title=findViewById(R.id.title);
        status_layout=findViewById(R.id.status_layout);
        list_digg=findViewById(R.id.list_digg);
    }

    //获取点赞列表
    private void getDiggList(){
        params.clear();
        params.put("type","thread");
        params.put("relation_id",id);
        params.put("forward_div_id",indexId);
        params.put("num","10");

        HttpUtil.get(Constants.DIGG_LIST, params, new HttpUtil.HttpCallBack() {
            @Override
            public void onFailure(String message) {
                list_digg.setLoading(false);
                ToastUtils.showShortSafe(message);
            }

            @Override
            public void onSuccess(String json) {

                list_digg.setRefreshing(false);
                list_digg.setEnabled(false);
                list_digg.setLoading(false);

                DiggRoot diggRoot= JsonUtils.jsonToPojo(json,DiggRoot.class);
                if (EmptyUtils.isEmpty(diggRoot)){
                    ToastUtils.showShortSafe("数据错误!");
                    return;
                }
                diggList.addAll(diggRoot.getItems());

                if (EmptyUtils.isEmpty(diggList)){
                    status_layout.empty();
                }else {
                    status_layout.normal();
                    if (EmptyUtils.isEmpty(diggRoot.getItems())){//此时无数据
                        ToastUtils.showShortSafe("真的没有了!");
                        return;
                    }
                    indexId=diggList.get(diggList.size()-1).getId();
                    diggAdapter.notifyDataSetChanged();
                }
            }
        });
    }
}
