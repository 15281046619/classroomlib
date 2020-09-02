package com.xingwang.essay;

import android.content.Context;
import android.content.Intent;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.xingwang.essay.adapter.EssayForumAdapter;
import com.xingwang.essay.adapter.EssayListViewpagerAdapter;
import com.xingwang.essay.base.BaseActivity;
import com.xingwang.essay.bean.Essay;
import com.xingwang.essay.bean.EssayTitle;
import com.xingwang.essay.fragment.EssayListFragment;
import com.xingwang.essay.view.HorizontalListView;
import com.xingwang.swip.title.TopTitleView;
import com.xingwang.swip.utils.AsyncTaskUtils;
import com.xingwang.swip.utils.Constants;
import com.xingwang.swip.utils.HttpUtil;
import com.xingwang.swip.utils.JsonUtils;
import com.xingwang.swip.utils.PopupWindowUtils;
import com.xingwang.swip.utils.asynctask.IDoInBackground;
import com.xingwang.swip.utils.asynctask.IPublishProgress;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 文章列表
 * */
public class EssayListActivity extends BaseActivity {

    protected TopTitleView title;
    protected TabLayout mTabLayout;
    protected ViewPager viewpager;
    protected HorizontalListView hlv_title;

    private EssayForumAdapter forumAdapter;

    private List<Fragment> mFragments=new ArrayList<>();
    private List<EssayTitle> titleList=new ArrayList<>();
    //排序后的标题
    private List<EssayTitle> arrayTitleList=new ArrayList<>();

    private AsyncTaskUtils.Builder<Void, Void, Void> mAsyncTask;

    private int tabPos=0;//fragment位置
    private int childPos=0;//子层级位置
    //传入id是否成功匹配 true-匹配 false-未匹配
    private boolean isIdExist=false;
    private String category_id;//栏目id

    public static Intent getIntent(Context context) {
        Intent intent = new Intent(context, EssayListActivity.class);
        context.startActivity(intent);
        return intent;
    }

    public static Intent getIntent(Context context,String category_id) {
        Intent intent = new Intent(context, EssayListActivity.class);
        intent.putExtra(Constants.INTENT_DATA,category_id);
        context.startActivity(intent);
        return intent;
    }

    @Override
    public int getLayoutId() {
        return R.layout.essay_activity_essay_list;
    }

    @Override
    protected void initView() {
        title =  findViewById(R.id.title);
        mTabLayout = findViewById(R.id.tabLayout);
        viewpager = findViewById(R.id.viewpager);
        hlv_title = findViewById(R.id.hlv_title);

        mTabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.reslib_colorPrimary));

        title.setTitleText("文章列表");
    }

    @Override
    public void initData() {

        category_id=getIntent().getStringExtra(Constants.INTENT_DATA);

        if (EmptyUtils.isEmpty(category_id)){
            Uri uri=getIntent().getData();
            if (EmptyUtils.isNotEmpty(uri)){
                category_id=uri.getQueryParameter("tag");
            }
        }

        forumAdapter=new EssayForumAdapter(this);
        hlv_title.setAdapter(forumAdapter);

        hlv_title.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                forumAdapter.setSelectIndex(position);

                int tabPos=viewpager.getCurrentItem();
                EssayListFragment essayListFragment= (EssayListFragment) mFragments.get(tabPos);
                essayListFragment.setTitle(arrayTitleList.get(tabPos).getChildTitles().get(position).getStrId());
            }
        });

        HttpUtil.get(Constants.ESSAY_CATALOG, new HttpUtil.HttpCallBack() {
            @Override
            public void onFailure(String message) {
                ToastUtils.showShortSafe(message);
            }
            @Override
            public void onSuccess(String json) {
                titleList.clear();
                arrayTitleList.clear();
                titleList= JsonUtils.jsonToList(json,EssayTitle.class);

                if (EmptyUtils.isEmpty(titleList)){
                    ToastUtils.showShortSafe("无标题数据!");
                    return;
                }

                mAsyncTask = AsyncTaskUtils.<Void, Void, Void>newBuilder().setDoInBackground(new IDoInBackground<Void, Void, Void>() {
                    @Override
                    public Void doInBackground(IPublishProgress<Void> publishProgress, Void... booleans) {
                        Iterator<EssayTitle> iterator = titleList.iterator();
                        while (iterator.hasNext()){
                            EssayTitle essayTitle=iterator.next();
                            if (essayTitle.isTop()){
                                arrayTitleList.add(essayTitle);
                                iterator.remove();
                            }
                        }

                        for (EssayTitle essayTitle:arrayTitleList){
                            essayTitleCast(titleList,essayTitle);
                        }

                        if (EmptyUtils.isEmpty(category_id))
                            return null;

                        //匹配栏目位置
                        for (int parent=0;parent<arrayTitleList.size();parent++){
                            EssayTitle parentTitle=arrayTitleList.get(parent);
                            if (category_id.equals(parentTitle.getStrId())){//此时与父层级匹配
                                isIdExist=true;
                                tabPos=parent;
                                childPos=0;
                                break;
                            }else if (parentTitle.hasChild()){//此时存在子层级
                                for (int child=0;child<parentTitle.getChildTitles().size();child++){
                                    EssayTitle childTitle=parentTitle.getChildTitles().get(child);
                                    if (category_id.equals(childTitle.getStrId())){//此时与子级匹配
                                        isIdExist=true;
                                        tabPos=parent;
                                        childPos=child;
                                        break;
                                    }
                                }
                            }
                        }

                        return null;
                    }
                }).setPostExecute(aBoolean -> {



                    //for (EssayTitle essayTitle:arrayTitleList){
                    for (int i=0;i<arrayTitleList.size();i++){
                        EssayTitle essayTitle=arrayTitleList.get(i);
                      /*  EssayTitle essayTitle=arrayTitleList.get(i);
                        TabLayout.Tab tab=mTabLayout.newTab();
                        View inflate = View.inflate(EssayListActivity.this, R.layout.essay_tab_forum, null);
                        tab.setCustomView(inflate);
                        TextView textView = inflate.findViewById(R.id.tv_essay_forum);
                        textView.setText(essayTitle.getTitle());
                        inflate.setTag(i);
                        inflate.setOnClickListener(mTabOnClickListener);
                        mTabLayout.addTab(tab);*/
                        mTabLayout.addTab(mTabLayout.newTab().setText(essayTitle.getTitle()));
                        if (isIdExist&&tabPos==i){//此时匹配成功
                            mFragments.add(EssayListFragment.newInstance(category_id));
                            isIdExist=false;//重置
                            continue;
                        }
                        mFragments.add(EssayListFragment.newInstance(essayTitle.getStrId()));
                    }

                    EssayListViewpagerAdapter mViewPagerAdapter = new EssayListViewpagerAdapter(getSupportFragmentManager(), mFragments,arrayTitleList);
                    viewpager.setAdapter(mViewPagerAdapter);

                    mTabLayout.setupWithViewPager(viewpager);

                    //viewpager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));

                    mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                        @Override
                        public void onTabSelected(TabLayout.Tab tab) {
                            showChildList(mTabLayout.getSelectedTabPosition());
                            //viewpager.setCurrentItem(tab.getPosition());
                        }

                        @Override
                        public void onTabUnselected(TabLayout.Tab tab) {

                        }

                        @Override
                        public void onTabReselected(TabLayout.Tab tab) {

                        }
                    });

                    viewpager.setCurrentItem(tabPos);
                    showChildList(tabPos,childPos);
                });
                mAsyncTask.start();
            }
        });
    }

    /**
     * 文章标题分配
     * @param remianDataList 未匹配的团队集合
     * @param parentTitle 上一层级集合
     * */
    public void essayTitleCast(List<EssayTitle> remianDataList,EssayTitle parentTitle){
        if (EmptyUtils.isNotEmpty(remianDataList)){
            Iterator<EssayTitle> iterator=remianDataList.iterator();
            List<EssayTitle> childTitles=new ArrayList<>();
            while (iterator.hasNext()){
                EssayTitle essayTitle=iterator.next();
                if (parentTitle.getId()==essayTitle.getPid()){
                    childTitles.add(essayTitle);
                    iterator.remove();
                }
            }
            parentTitle.setChildTitles(childTitles);

            if (EmptyUtils.isNotEmpty(remianDataList)){
                for (EssayTitle essayTitle:childTitles){
                    essayTitleCast(remianDataList,essayTitle);
                }
            }
        }
    }

    //子层级是否显示
    private void showChildList(int tabPos,int childPos){
        if (arrayTitleList.get(tabPos).hasChild()){//此时有子层级
            hlv_title.setVisibility(View.VISIBLE);
            forumAdapter.setDatas(arrayTitleList.get(tabPos).getChildTitles());
            if (childPos!=-1)//此时传入位置
                forumAdapter.setSelectIndex(childPos);
        }else {
            hlv_title.setVisibility(View.GONE);
        }
    }

    //子层级是否显示
    private void showChildList(int tabPos){
        showChildList(tabPos,-1);
    }

    //tab点击事件
  /*  private View.OnClickListener mTabOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int position= (int) v.getTag();
            if (mTabLayout.getTabAt(position).isSelected()==true){
               ToastUtils.showShortSafe("点击"+position);
            }
            viewpager.setCurrentItem(position);
            TextView tv_essay_forum=v.findViewById(R.id.tv_essay_forum);
            tv_essay_forum.setSelected(mTabLayout.getTabAt(position).isSelected());
        }
    };*/

}
