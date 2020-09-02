package com.xingwang.groupchat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.beautydefinelibrary.BeautyDefine;
import com.beautydefinelibrary.ImagePickerCallBack;
import com.beautydefinelibrary.ImagePickerDefine;
import com.beautydefinelibrary.UploadResultCallBack;
import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.xingwang.groupchat.utils.BottomPopWindow;
import com.xingwang.groupchat.utils.HttpUtil;
import com.xingwang.swip.title.TopTitleView;
import com.xingwang.swip.utils.ActivityManager;
import com.xingwang.swip.utils.Constants;
import com.xingwang.swip.utils.GlideUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class CreateGroupActivity extends BaseActivity implements View.OnClickListener, EasyPermissions.PermissionCallbacks {

    private static final int CAMEAR = 100;//图片/视频权限

    protected TopTitleView title;
    protected ImageView img_add_group_avatar;
    protected EditText edit_group_name;
    protected Button bt_submit;

    private String group_name;
    private String avatar;
    private List<File> localFiles = new ArrayList<>();//本地文件
    private BottomPopWindow popWindow;//底部弹窗

    private ImagePickerDefine pickerDefine;

    private HashMap<String, String> params = new HashMap<>();

    public static void getIntent(Context context) {
        Intent intent = new Intent(context, CreateGroupActivity.class);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getLayoutId() {
        return R.layout.groupchat_activity_create_group;
    }

    @Override
    protected void initView() {
        title = findViewById(R.id.title);
        img_add_group_avatar = findViewById(R.id.img_add_group_avatar);
        edit_group_name = findViewById(R.id.edit_group_name);
        bt_submit = findViewById(R.id.bt_submit);

        bt_submit.setOnClickListener(this);
        img_add_group_avatar.setOnClickListener(this);
    }

    @Override
    public void initData() {
        pickerDefine = BeautyDefine.getImagePickerDefine(this);
        popWindow = new BottomPopWindow(this, true);
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
        // GlideUtils.loadPic("https://pic2.zhimg.com/v2-a22e84085461ca29efd5fdc3c71bc13f_r.jpg", img_add_group_avatar,this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.bt_submit) {
            group_name = edit_group_name.getText().toString().trim();

            if (EmptyUtils.isEmpty(group_name)) {
                ToastUtils.showShortSafe("群昵称不可为空");
                return;
            }

            if (group_name.length() < 2 || group_name.length() > 12) {
                ToastUtils.showShortSafe("群昵称2-12个字");
                return;
            }

          /*  if (EmptyUtils.isEmpty(avatar)) {
                ToastUtils.showShortSafe("请选择群头像！");
                return;
            }*/
            showLoadingDialog();
           // uploadPic();
            createGroup();
        } else if (id == R.id.img_add_group_avatar) {
            popWindow.show();
        }
    }

    @AfterPermissionGranted(CAMEAR)
    private void requestCameraPermission() {
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO};
        if (EasyPermissions.hasPermissions(this, perms)) {
            getPicResult();
        } else {
            EasyPermissions.requestPermissions(CreateGroupActivity.this, "需要以下权限:\n\n1.拍照\n\n2.存储读写权限", CAMEAR, perms);
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
        if (requestCode == CAMEAR) {
            getPicResult();
        }
    }

    //获取图片/视频选择
    private void getPicResult() {
       // ToastUtils.showShortSafe("getPicResult()");
        pickerDefine.showAvtarPicker(new ImagePickerCallBack() {
            @Override
            public void onResult(List<String> list, ImagePickerDefine.MediaType mediaType, List<String> list1) {
                if (EmptyUtils.isNotEmpty(list)) {
                    localFiles.clear();
                    avatar=list.get(0);
                    Log.i("group",avatar);
                    localFiles.add(FileUtils.getFileByPath(avatar));
                    GlideUtils.loadAvatar(avatar,img_add_group_avatar,getApplicationContext(),true);
                }
            }

            @Override
            public void onCancel() {

            }
        });
    }

    //上传图片
    private void uploadPic() {
        File[] arrayFile = localFiles.toArray(new File[localFiles.size()]);

        BeautyDefine.getUploadDefine().upload(arrayFile, new UploadResultCallBack() {
            @Override
            public void onSucceed(String[] strings) {
                avatar = strings[0];
                createGroup();
            }

            @Override
            public void onFailure() {
                hideLoadingDialog();
                ToastUtils.showShortSafe("头像上传失败");
            }
        });
    }

    //创建群
    private void createGroup() {
        params.clear();
        params.put("title", group_name);
        params.put("avatar", "https://pic2.zhimg.com/v2-a22e84085461ca29efd5fdc3c71bc13f_r.jpg");
        //params.put("avatar",avatar);

        HttpUtil.post(Constants.GROUP_CREATE, params, new HttpUtil.HttpCallBack() {
            @Override
            public void onFailure(String message) {
                ToastUtils.showShortSafe(message);
                hideLoadingDialog();
            }

            @Override
            public void onSuccess(String json, String tag) {
                hideLoadingDialog();
                ToastUtils.showShortSafe("创建成功");
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
