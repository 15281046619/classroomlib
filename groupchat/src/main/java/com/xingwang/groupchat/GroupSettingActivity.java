package com.xingwang.groupchat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beautydefinelibrary.BeautyDefine;
import com.beautydefinelibrary.ImagePickerCallBack;
import com.beautydefinelibrary.ImagePickerDefine;
import com.beautydefinelibrary.UploadResultCallBack;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.xingwang.groupchat.adapter.ChatGroupMemberListAdapter;
import com.xingwang.groupchat.bean.Group;
import com.xingwang.groupchat.bean.Teammate;
import com.xingwang.groupchat.bean.User;
import com.xingwang.groupchat.callback.DialogAlertCallback;
import com.xingwang.groupchat.utils.BottomPopWindow;
import com.xingwang.groupchat.utils.DialogUtils;
import com.xingwang.groupchat.utils.HttpUtil;
import com.xingwang.swip.title.TopTitleView;
import com.xingwang.swip.utils.ActivityManager;
import com.xingwang.swip.utils.Constants;
import com.xingwang.swip.utils.GlideUtils;
import com.xingwang.swip.utils.JsonUtils;
import com.xingwreslib.beautyreslibrary.GroupDissolveInfo;
import com.xingwreslib.beautyreslibrary.GroupDissolveLiveData;
import com.xingwreslib.beautyreslibrary.GroupMemsInfo;
import com.xingwreslib.beautyreslibrary.GroupMemsLiveData;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class GroupSettingActivity extends BaseActivity implements View.OnClickListener, EasyPermissions.PermissionCallbacks{

    private static final int CAMEAR = 100;//图片/视频权限

    protected GridView gv_group_member;
    protected TopTitleView title;
    protected TextView tv_group_name;
    //protected TextView tv_group_title;
    protected RelativeLayout re_change_groupname;
    protected TextView tv_group_member_num;
    protected RelativeLayout rl_group_member;
    protected Button bt_leave;

    /**群简介*/
    protected TextView tv_group_intro;
    protected RelativeLayout re_change_intro;
    protected ImageView img_group_head;

    /**群转让*/
    //protected RelativeLayout re_transfer;
    /**群管理*/
    protected RelativeLayout re_admin_group;

    private BottomPopWindow popWindow;//底部弹窗

    private ChatGroupMemberListAdapter memberListAdapter;

    private List<User> userList=new ArrayList<>();

    private ImagePickerDefine pickerDefine;

    //群Id
    private String groupId;
    //群信息
    private Group group;

    private List<File> localFiles=new ArrayList<>();//本地文件

    private HashMap<String,String> params=new HashMap<>();

    public static void getIntent(Context context, String groupId) {
        Intent intent = new Intent(context, GroupSettingActivity.class);
        intent.putExtra(Constants.INTENT_DATA,groupId);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.groupchat_activity_group_setting;
    }

    @Override
    protected void initView() {
        title = findViewById(R.id.title);
        gv_group_member = findViewById(R.id.gv_group_member);
        tv_group_name = findViewById(R.id.tv_group_name);
       // tv_group_title = findViewById(R.id.tv_group_title);
        re_change_groupname = findViewById(R.id.re_change_groupname);
        tv_group_member_num = findViewById(R.id.tv_group_member_num);
        rl_group_member = findViewById(R.id.rl_group_member);
        bt_leave = findViewById(R.id.bt_leave);

        //群简介
        tv_group_intro = findViewById(R.id.tv_group_intro);
        re_change_intro = findViewById(R.id.re_change_intro);
        img_group_head = findViewById(R.id.img_group_head);

        //群转让
       // re_transfer = findViewById(R.id.re_transfer);
        re_admin_group = findViewById(R.id.re_admin_group);

        img_group_head.setOnClickListener(this);
        bt_leave.setOnClickListener(this);
        rl_group_member.setOnClickListener(this);
        re_change_groupname.setOnClickListener(this);
        re_change_intro.setOnClickListener(this);
       // re_transfer.setOnClickListener(this);
        re_admin_group.setOnClickListener(this);

    }

    @Override
    public void initData() {
        groupId=getIntent().getStringExtra(Constants.INTENT_DATA);
        if (EmptyUtils.isEmpty(groupId)){
            ToastUtils.showShortSafe("群ID有误!");
            finish();
            return;
        }

        pickerDefine= BeautyDefine.getImagePickerDefine(this);

        popWindow = new BottomPopWindow(this,true);

        popWindow.setOnMyPopClickEvent(new BottomPopWindow.OnBottomClickEvent() {
            @Override
            public void takePhoto() {
                requestCameraPermission();
            }

            @Override
            public void photoAlbum() {
                requestCameraPermission();
            }

            @Override
            public void cancel() {
            }
        });


        memberListAdapter=new ChatGroupMemberListAdapter(userList,this);
        gv_group_member.setAdapter(memberListAdapter);

        gv_group_member.setOnItemClickListener((adapterView, view, i, l) -> {
            if((userList.size()>=4&&i==4)
                    ||i==userList.size()){//此时添加成员
                SearchUserActivity.getIntent(GroupSettingActivity.this,groupId);
            }else  {//进入个人详情界面
                BeautyDefine.getOpenPageDefine(GroupSettingActivity.this).toPersonal(userList.get(i).getId());
                //ToastUtils.showShortSafe(userList.get(i).getNickname());
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getGroupInfo();
    }

    //获取群信息
    private void getGroupInfo(){
        HttpUtil.get(Constants.GROUP_INFO +"?group_id="+groupId, new HttpUtil.HttpCallBack() {
            @Override
            public void onFailure(String message) {
                ToastUtils.showShortSafe(message);
            }

            @Override
            public void onSuccess(String json,String tag) {
                group= JsonUtils.jsonToPojo(json,Group.class);
                if (EmptyUtils.isEmpty(group)){
                    ToastUtils.showShortSafe("群信息错误!");
                    return;
                }

                tv_group_name.setText(group.getTitle());
               // tv_group_title.setText(group.getTitle());
                tv_group_intro.setText(group.getIntroduction());
                GlideUtils.loadAvatar(group.getAvatar(),img_group_head,getApplicationContext());

                if (group.getLeader(GroupSettingActivity.this)){
                    re_admin_group.setVisibility(View.VISIBLE);
                    bt_leave.setText("解散群聊");
                    img_group_head.setClickable(true);
                }else {
                    re_admin_group.setVisibility(View.GONE);
                    bt_leave.setText("退出群聊");
                    img_group_head.setClickable(false);
                }

                getGroupMember();
            }
        });
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
                userList.clear();
                userList.addAll(JsonUtils.jsonToList(json,User.class));
                memberListAdapter.isLeader(group.getLeader(GroupSettingActivity.this));
                memberListAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.img_group_head) {
            popWindow.show();
        } else if (id == R.id.bt_leave) {
            DialogUtils.obtainCommonDialog("您将失去和群友的联系，确定操作?", new DialogAlertCallback() {
                @Override
                public void sure() {
                    if (group.getLeader(GroupSettingActivity.this)) {//解散群聊
                        quiteGroup(Constants.GROUP_DISMISS);
                    } else {//退出群聊
                        quiteGroup(Constants.GROUP_LEAVE);
                    }
                }
            }).show(getSupportFragmentManager());
        } else if (id == R.id.rl_group_member) {
            GroupMemberActivity.getIntent(GroupSettingActivity.this, group);
        } else if (id == R.id.re_change_groupname) {//修改群名称
            EditGroupNameActivity.getIntent(GroupSettingActivity.this, group);
        } else if (id == R.id.re_change_intro) {//修改群简介
            GroupIntroActivity.getIntent(GroupSettingActivity.this, group);
        } else if (id == R.id.re_admin_group) {
            AdminGroupActivity.getIntent(GroupSettingActivity.this, group);
        }
    }

    @AfterPermissionGranted(CAMEAR)
    private void requestCameraPermission(){
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA,Manifest.permission.RECORD_AUDIO};
        if (EasyPermissions.hasPermissions(this, perms)) {
            getPicResult();
            //editGroupAvatar("https://pic2.zhimg.com/v2-a22e84085461ca29efd5fdc3c71bc13f_r.jpg");
        } else {
            EasyPermissions.requestPermissions(GroupSettingActivity.this, "需要以下权限:\n\n1.拍照\n\n2.存储读写权限", CAMEAR, perms);
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        requestCameraPermission();
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        ToastUtils.showShortSafe("权限授予失败");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==CAMEAR){
            getPicResult();
            //editGroupAvatar("https://pic2.zhimg.com/v2-a22e84085461ca29efd5fdc3c71bc13f_r.jpg");
        }
    }

    //获取图片/视频选择
    private void getPicResult(){
        pickerDefine.showAvtarPicker(new ImagePickerCallBack() {
            @Override
            public void onResult(List<String> list, ImagePickerDefine.MediaType mediaType, List<String> list1) {
                if (EmptyUtils.isNotEmpty(list)){
                    localFiles.clear();
                    localFiles.add(FileUtils.getFileByPath(list.get(0)));
                    uploadPic();
                }
            }

            @Override
            public void onCancel() {

            }
        });
    }

    //上传图片
    private void uploadPic(){
        showLoadingDialog();
        File[] arrayFile=localFiles.toArray(new File[localFiles.size()]);

        BeautyDefine.getUploadDefine().upload(arrayFile, new UploadResultCallBack() {
            @Override
            public void onSucceed(String[] strings) {
                editGroupAvatar(strings[0]);
            }

            @Override
            public void onFailure() {
                hideLoadingDialog();
                ToastUtils.showShortSafe("头像上传失败");
            }
        });
    }

    //修改群名称
    private void editGroupAvatar(String avatar){
        params.clear();
        params.put("group_id",groupId);
        params.put("avatar",avatar);
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
                GlideUtils.loadAvatar(avatar,img_group_head,getApplicationContext());
            }
        });
    }

    //退出/解散群聊
    public void quiteGroup(String url){
        params.clear();
        params.put("group_id",groupId);
        HttpUtil.post(url, params, new HttpUtil.HttpCallBack() {
            @Override
            public void onFailure(String message) {
                hideLoadingDialog();
                ToastUtils.showShortSafe(message);
            }

            @Override
            public void onSuccess(String json, String tag) {
                hideLoadingDialog();
                ToastUtils.showShortSafe("操作成功!");

                if (url.equals(Constants.GROUP_DISMISS)){//解散群聊
                    GroupDissolveLiveData.getInstance().notifyInfoChanged(new GroupDissolveInfo(group.getId(),true));
                }else {//退出群聊

                    ArrayList<GroupMemsInfo.Mem> leavasList=new ArrayList<>();

                    GroupMemsInfo memsInfo=new GroupMemsInfo(group.getId(),leavasList);

                    GroupMemsInfo.Mem me=memsInfo.new Mem(BeautyDefine.getUserInfoDefine(GroupSettingActivity.this).getUserId(),
                            BeautyDefine.getUserInfoDefine(GroupSettingActivity.this).getNickName(),
                            BeautyDefine.getUserInfoDefine(GroupSettingActivity.this).getUserHeadUrl());

                    leavasList.add(me);

                    GroupMemsLiveData.getInstance().notifyInfoChanged(memsInfo);
                }

                ActivityManager.getInstance().finishActivity();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        pickerDefine.onActivityResultHanlder(requestCode,resultCode,data);
    }
}
