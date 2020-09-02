package com.xingwang.groupchat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beautydefinelibrary.BeautyDefine;
import com.beautydefinelibrary.MsgBoxDefine;
import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.xingwang.groupchat.adapter.GroupMemberListAdapter;
import com.xingwang.groupchat.adapter.SearchUserAdapter;
import com.xingwang.groupchat.bean.Group;
import com.xingwang.groupchat.bean.User;
import com.xingwang.groupchat.callback.DialogAlertCallback;
import com.xingwang.groupchat.callback.OnItemClickListener;
import com.xingwang.groupchat.utils.DialogUtils;
import com.xingwang.groupchat.utils.HttpUtil;

import com.xingwang.swip.title.TopTitleView;
import com.xingwang.swip.utils.ActivityManager;
import com.xingwang.swip.utils.AsyncTaskUtils;
import com.xingwang.swip.utils.Constants;
import com.xingwang.swip.utils.JsonUtils;
import com.xingwang.swip.utils.asynctask.IDoInBackground;
import com.xingwang.swip.utils.asynctask.IPublishProgress;
import com.xingwreslib.beautyreslibrary.GroupMemsInfo;
import com.xingwreslib.beautyreslibrary.GroupMemsLiveData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SearchUserActivity extends BaseActivity implements View.OnClickListener {

    protected TopTitleView title;
    protected EditText et_search;
    protected ImageView img_empty;
    protected LinearLayout line_recent_contact;
    protected TextView tv_recent_contact;
    protected RecyclerView lv_list;
    protected TextView tv_search;
    protected Button bt_add;
    protected TextView tv_empty;

    private SearchUserAdapter searchUserAdapter;

    //群成员集合
    private List<User> groupUserList = new ArrayList<>();
    //搜索结果集
    private List<User> searchUserList = new ArrayList<>();
    //最近联系人id集合
    private List<String> nearestUserIdList = new ArrayList<>();
    //最近联系人
    private List<User> recentUserList = new ArrayList<>();

    private List<User> addUserList = new ArrayList<>();

    private String searchContent;
    private String groupId;//群id
    private int count=0;//计 联系人添加的个数
    //判断最近联系人信息是否完全获取
    private boolean isStop=false;
    //判断群员添加状态
    private boolean isAddStop=false;

    //通知群员变化
    private ArrayList<GroupMemsInfo.Mem> leavasList=new ArrayList<>();
    private GroupMemsInfo memsInfo;

    private AsyncTaskUtils.Builder<Void, Void, Void> mAsyncTask;

    private HashMap<String,String> params=new HashMap<>();

    public static void getIntent(Context context,String groupId) {
        Intent intent = new Intent(context, SearchUserActivity.class);
         intent.putExtra(Constants.INTENT_DATA,groupId);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public int getLayoutId() {
        return R.layout.groupchat_activity_search_user;
    }

    @Override
    protected void initView() {
        title = findViewById(R.id.title);
        et_search = findViewById(R.id.et_search);
        img_empty = findViewById(R.id.img_empty);
        line_recent_contact = findViewById(R.id.line_recent_contact);
        tv_recent_contact = findViewById(R.id.tv_recent_contact);
        lv_list = findViewById(R.id.lv_list);
        tv_search = findViewById(R.id.tv_search);
        tv_empty = findViewById(R.id.tv_empty);
        bt_add = findViewById(R.id.bt_add);

        tv_search.setOnClickListener(this);
        img_empty.setOnClickListener(this);
        bt_add.setOnClickListener(this);

        lv_list.setLayoutManager( new LinearLayoutManager(this));

        searchUserAdapter = new SearchUserAdapter(this, searchUserList);
        lv_list.setAdapter(searchUserAdapter);

    }

    @Override
    public void initData() {
        showLoadingDialog();
        groupId=getIntent().getStringExtra(Constants.INTENT_DATA);

        memsInfo=new GroupMemsInfo(Long.valueOf(groupId),leavasList);

       /* for (int i=1;i<7;i++){
            nearestUserIdList.add(String.valueOf(i));
        }*/

        BeautyDefine.getMsgBoxDefine().queryDiffUserIds(new MsgBoxDefine.StrListResultListener() {
            @Override
            public void onResult(List<String> list) {
                nearestUserIdList.clear();
                nearestUserIdList.addAll(list);
            }
        });

        getGroupMember();

        searchUserAdapter.setOnItemClickListener(new OnItemClickListener(){
            @Override
            public void onItemClick(int position) {

                if (searchUserList.get(position).isSelect()){
                    addUserList.add(searchUserList.get(position));
                    //addCount++;
                }else {
                    for (User user:addUserList){
                        if (user.getId()==searchUserList.get(position).getId()){//此时该用户已选择
                            addUserList.remove(user);
                            break;
                        }
                    }
                }

                if (addUserList.size()==0){
                    bt_add.setText("立即添加");
                }else {
                    bt_add.setText("立即添加("+addUserList.size()+")");
                }
            }
        });

        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchContent = s.toString();
                if (StringUtils.isEmpty(searchContent)) {
                    searchUserList.clear();
                    searchUserList.addAll(recentUserList);

                    if (EmptyUtils.isEmpty(recentUserList)){//无最近联系人
                        line_recent_contact.setVisibility(View.GONE);
                        tv_empty.setVisibility(View.VISIBLE);
                        tv_empty.setText("无最近联系人");
                    }else {
                        matchSelectMember();
                        line_recent_contact.setVisibility(View.VISIBLE);
                        tv_empty.setVisibility(View.GONE);
                        tv_recent_contact.setVisibility(View.VISIBLE);
                    }

                    searchUserAdapter.notifyDataSetChanged();
                    img_empty.setVisibility(View.GONE);
                } else {
                    img_empty.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        et_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (StringUtils.isEmpty(searchContent)){
                        ToastUtils.showShortSafe("请填写搜索内容!");
                    }else {
                        searchUser();
                    }
                    return true;
                }
                return false;
            }
        });
    }

    //获取群成员
    private void getGroupMember(){
        HttpUtil.get(Constants.GROUP_MEMBER +"?group_id="+groupId, new HttpUtil.HttpCallBack() {
            @Override
            public void onFailure(String message) {
                hideLoadingDialog();
                ToastUtils.showShortSafe(message);
            }

            @Override
            public void onSuccess(String json,String tag) {
                groupUserList.clear();
                groupUserList.addAll(JsonUtils.jsonToList(json,User.class));

                if (EmptyUtils.isEmpty(nearestUserIdList)){
                    line_recent_contact.setVisibility(View.GONE);
                    tv_empty.setVisibility(View.VISIBLE);
                    tv_empty.setText("无最近联系人");
                    hideLoadingDialog();
                    return;
                }
                for (String id:nearestUserIdList){
                    if (isStop){//此时最近联系人获取失败
                        break;
                    }
                    getUserInfo(id);
                }
            }
        });
    }

    //搜索用户
    private void searchUser(){
        showLoadingDialog();
        HttpUtil.get(Constants.SEARCH_USER+"?q="+searchContent,new HttpUtil.HttpCallBack() {
            @Override
            public void onFailure(String message) {
                hideLoadingDialog();
                tv_empty.setVisibility(View.VISIBLE);
                line_recent_contact.setVisibility(View.GONE);
                tv_empty.setText(message);
            }

            @Override
            public void onSuccess(String json,String tag) {
                searchUserList.clear();
                searchUserList.addAll(JsonUtils.jsonToList(json, User.class));

                if (EmptyUtils.isEmpty(searchUserList)){
                    hideLoadingDialog();
                    tv_empty.setVisibility(View.VISIBLE);
                    tv_recent_contact.setVisibility(View.VISIBLE);
                    line_recent_contact.setVisibility(View.GONE);
                    tv_empty.setText("该用户不存在!");
                    return;
                }
                tv_empty.setVisibility(View.GONE);
                tv_recent_contact.setVisibility(View.GONE);
                line_recent_contact.setVisibility(View.VISIBLE);
                matchGroupMember(false);
                matchSelectMember();
            }
        });
    }

    //添加成员
    private void addGroupMember(String id){
        params.clear();
        params.put("group_id",groupId);
        params.put("user_id",id);
        HttpUtil.post(Constants.GROUP_ADD, params, new HttpUtil.HttpCallBack() {
            @Override
            public void onFailure(String message) {
                if (!isAddStop){
                    ToastUtils.showShortSafe(message);
                }
                isAddStop=true;
                hideLoadingDialog();
            }

            @Override
            public void onSuccess(String json, String tag) {

                if (isAddStop){
                    hideLoadingDialog();
                    return;
                }

                if (++count==addUserList.size()){
                    hideLoadingDialog();
                    GroupMemsLiveData.getInstance().notifyInfoChanged(memsInfo);
                    ToastUtils.showShortSafe("添加成功！");
                    ActivityManager.getInstance().finishActivity();
                }
            }
        });
    }

    //匹配群成员 isRecent-true最近联系人 false-搜索
    private void matchGroupMember(boolean isRecent){
        mAsyncTask = AsyncTaskUtils.<Void, Void, Void>newBuilder().setDoInBackground(new IDoInBackground<Void, Void, Void>() {
            @Override
            public Void doInBackground(IPublishProgress<Void> publishProgress, Void... booleans) {

                for (User groupUser : groupUserList) {
                    for (User searchUser : searchUserList) {
                        if (groupUser.getId()==searchUser.getId()) {//该用户在群内
                            searchUser.setGroup(true);
                            break;
                        }
                    }
                }
                return null;
            }
        }).setPostExecute(aBoolean -> {
            hideLoadingDialog();
            tv_empty.setVisibility(View.GONE);
            if (isRecent) {
                tv_recent_contact.setVisibility(View.VISIBLE);
            } else {
                tv_recent_contact.setVisibility(View.GONE);
            }

            line_recent_contact.setVisibility(View.VISIBLE);

            searchUserAdapter.notifyDataSetChanged();
        });
        mAsyncTask.start();
    }

    //匹配结果是否存在选中
    private void matchSelectMember(){
        mAsyncTask = AsyncTaskUtils.<Void, Void, Void>newBuilder().setDoInBackground(new IDoInBackground<Void, Void, Void>() {
            @Override
            public Void doInBackground(IPublishProgress<Void> publishProgress, Void... booleans) {

                for (User selectUser : addUserList) {
                    for (User searchUser : searchUserList) {
                        if (selectUser.getId()==searchUser.getId()) {//该用户在群内
                            searchUser.setSelect(true);
                            break;
                        }
                    }
                }
                return null;
            }
        }).setPostExecute(aBoolean -> {
            searchUserAdapter.notifyDataSetChanged();
        });
        mAsyncTask.start();
    }

    private void getUserInfo(String id){
        HttpUtil.get(Constants.USER_INFO+"?id="+id, new HttpUtil.HttpCallBack() {
            @Override
            public void onFailure(String message) {
                hideLoadingDialog();
                isStop=true;
            }

            @Override
            public void onSuccess(String json, String tag) {
                recentUserList.add(JsonUtils.jsonToPojo(json,User.class));

                if (isStop)
                    hideLoadingDialog();
                if (recentUserList.size()==nearestUserIdList.size()){//此时最近联系人完全获取
                    searchUserList.clear();
                    searchUserList.addAll(recentUserList);
                    matchGroupMember(true);
                    matchSelectMember();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.tv_search) {//搜索
            if (StringUtils.isEmpty(searchContent)) {
                ToastUtils.showShortSafe("请填写搜索内容!");
            } else {
                searchUser();
            }
        } else if (id == R.id.img_empty) {//清空搜索内容
            et_search.setText("");
        } else if (id == R.id.bt_add) {
            if (addUserList.size()==0) {
                ToastUtils.showShortSafe("请选择新用户!");
            } else {
                isAddStop=false;
                for (User user : addUserList) {
                    GroupMemsInfo.Mem me=memsInfo.new Mem(user.getStrId(),user.getNickname(),user.getAvatar());
                    leavasList.add(me);
                    addGroupMember(user.getStrId());
                }
            }
        }
    }
}
