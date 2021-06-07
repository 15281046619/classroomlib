package com.xinwang.shoppingcenter.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beautydefinelibrary.BeautyDefine;
import com.beautydefinelibrary.ImagePickerCallBack;
import com.beautydefinelibrary.ImagePickerDefine;
import com.beautydefinelibrary.OpenPageDefine;
import com.beautydefinelibrary.UploadResultCallBack;
import com.xingwreslib.beautyreslibrary.OrderInfo;
import com.xingwreslib.beautyreslibrary.OrderLiveData;
import com.xinwang.bgqbaselib.base.BaseNetActivity;
import com.xinwang.bgqbaselib.dialog.CenterDefineDialog;
import com.xinwang.bgqbaselib.http.ApiParams;
import com.xinwang.bgqbaselib.http.CommonEntity;
import com.xinwang.bgqbaselib.http.HttpCallBack;
import com.xinwang.bgqbaselib.http.HttpUrls;
import com.xinwang.bgqbaselib.utils.Constants;
import com.xinwang.bgqbaselib.utils.GlideUtils;
import com.xinwang.bgqbaselib.utils.GsonUtils;
import com.xinwang.bgqbaselib.utils.MyToast;
import com.xinwang.bgqbaselib.view.CustomToolbar;
import com.xinwang.shoppingcenter.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 商品评价界面
 * Date:2021/6/3
 * Time;10:59
 * author:baiguiqiang
 */
public class ShoppingReviewActivity extends BaseNetActivity implements View.OnClickListener {
    private ImageView icCove,ivScore1,ivScore2,ivScore3,ivScore4,ivScore5;
    private TextView tvTitle,tvScoreDes,tvAdd;
    private EditText etContent;
    private HorizontalScrollView hScrollview;
    private LinearLayout llHead;
    private CheckBox cbTrue;
    private int score=5;//评分
    private String[] scoreDess=new String[]{"非常差","差","一般","好","非常好"};
    private List<String> mSelectPhoto=new ArrayList<>();//选择的图片
    private ImagePickerDefine imagePickerDefine;

    @Override
    protected int layoutResId() {
        return R.layout.activity_shopping_review_shoppingcenter;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
        initData();
        initListener();
    }

    private void initData() {
        submitScore(5);
        GlideUtils.loadAvatar(getIntent().getStringExtra("icon"),R.color.BGPressedClassRoom,icCove);
        tvTitle.setText(getIntent().getStringExtra("title"));
        etContent.post(new Runnable() {
            @Override
            public void run() {
                etContent.requestFocus();
            }
        });
    }

    private void initListener() {
        ((CustomToolbar)findViewById(R.id.toolbar)).setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmExit();
            }
        });
        findViewById(R.id.ivSubmit).setOnClickListener(this);
        findViewById(R.id.ivScore1).setOnClickListener(this);
        findViewById(R.id.ivScore2).setOnClickListener(this);
        findViewById(R.id.ivScore3).setOnClickListener(this);
        findViewById(R.id.ivScore4).setOnClickListener(this);
        findViewById(R.id.ivScore5).setOnClickListener(this);
        findViewById(R.id.tvAdd).setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        confirmExit();
    }
    private void  confirmExit(){
        CenterDefineDialog.getInstance("退出后编辑过的内容将不保存").setCallback(new CenterDefineDialog.Callback1<Integer>() {
            @Override
            public void run(Integer integer) {
                finish();
            }
        }).showDialog(getSupportFragmentManager());
    }
    private void initViews() {
        cbTrue =findViewById(R.id.cbTrue);
        icCove =findViewById(R.id.icCove);
        ivScore1 =findViewById(R.id.ivScore1);
        ivScore2 =findViewById(R.id.ivScore2);
        ivScore3 =findViewById(R.id.ivScore3);
        ivScore4 =findViewById(R.id.ivScore4);
        ivScore5 =findViewById(R.id.ivScore5);
        tvTitle =findViewById(R.id.tvTitle);
        tvScoreDes =findViewById(R.id.tvScoreDes);
        tvAdd =findViewById(R.id.tvAdd);
        etContent =findViewById(R.id.etContent);
        llHead =findViewById(R.id.llHead);
        cbTrue =findViewById(R.id.cbTrue);
        hScrollview =findViewById(R.id.hScrollview);
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.ivSubmit){
            submit();
        }else if (v.getId()==R.id.ivScore1){
            submitScore(1);
        }else if (v.getId()==R.id.ivScore2){
            submitScore(2);
        }else if (v.getId()==R.id.ivScore3){
            submitScore(3);
        }else if (v.getId()==R.id.ivScore4){
            submitScore(4);
        }else if (v.getId()==R.id.ivScore5){
            submitScore(5);
        }else if (v.getId()==R.id.tvAdd){
            if (mSelectPhoto.size()<10) {
                requestPermission();
            }else {
                MyToast.myToast(ShoppingReviewActivity.this,"最多上传10张");
            }
        }

    }

    private void requestPermission(){
        requestDangerousPermissions(new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA},100);
    }
    public void requestDangerousPermissions(String[] permissions, int requestCode) {
        if (checkDangerousPermissions(permissions)){
            jumpPic();

            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, requestCode);
        }
    }

    private void jumpPic(){

        imagePickerDefine = BeautyDefine.getImagePickerDefine(this);
        //先不加视频功能
        imagePickerDefine.showMultiplePicker((ArrayList<String>) mSelectPhoto,false, new ImagePickerCallBack() {
            @Override
            public void onResult(List<String> list, ImagePickerDefine.MediaType mediaType, List<String> list1) {
                if (list!=null&&list.size()>0) {
                    mSelectPhoto = list1;
                    showPicList(true);
                }
            }

            @Override
            public void onCancel() {

            }
        });
    }
    private void showPicList(boolean isScrollBottom){
        llHead.removeAllViews();
        for (int i=0;i<mSelectPhoto.size();i++){
            View mRoot = LayoutInflater.from(this).inflate(R.layout.item_photo_review_shoppingcentger, llHead, false);
            ImageView ivPhoto =mRoot.findViewById(R.id.ivPhoto);
            ImageView ivDelete =mRoot.findViewById(R.id.ivDelete);
            GlideUtils.loadAvatar(mSelectPhoto.get(i),R.mipmap.bg_default_placeholder_classroom,ivPhoto);
            int finalI = i;
            ivDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSelectPhoto.remove(finalI);
                    showPicList(false);
                }
            });
            llHead.addView(mRoot);
        }
        if (isScrollBottom)
            llHead.post(new Runnable() {
                @Override
                public void run() {
                    hScrollview.smoothScrollTo(llHead.getMeasuredWidth(),0);
                }
            });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (imagePickerDefine != null)
            imagePickerDefine.onActivityResultHanlder(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 100) {
            for (int grantResult : grantResults) {
                if (grantResult != PackageManager.PERMISSION_GRANTED) {
                    MyToast.myToast(getApplicationContext(), "你拒绝了该权限");
                    return;
                }
            }
            jumpPic();
        }
    }
    /**
     * 检查是否已被授权危险权限
     */
    public boolean checkDangerousPermissions(String[] permissions) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }

        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                return false;
            }
        }
        return true;
    }
    private void submitScore(int pos){
        score =pos;
        ivScore1.setSelected(pos>=1);
        ivScore2.setSelected(pos>=2);
        ivScore3.setSelected(pos>=3);
        ivScore4.setSelected(pos>=4);
        ivScore5.setSelected(pos>=5);
        tvScoreDes.setText(scoreDess[pos-1]);
    }
    private void submit() {
        if (TextUtils.isEmpty(etContent.getText().toString())){
            MyToast.myToast(this,"请评价该商品");
        }else {
            if (mSelectPhoto!=null&&mSelectPhoto.size()>0){
                goUploadPic();
            }else {
                submitData(null);
            }

        }
    }
    private void  submitData(String[] mSelectPhoto){
        ApiParams mApiParams = new ApiParams().with("item_id", getIntent().getStringExtra("id")).with("score", score + "").with("content", etContent.getText().toString())
                .with("anonymous_state", cbTrue.isChecked() ? "1" : "2");
        if (mSelectPhoto!=null)
            mApiParams.with("media", GsonUtils.createGsonString(mSelectPhoto));
        else
            BeautyDefine.getOpenPageDefine(this).progressControl(new OpenPageDefine.ProgressController.Showder("加载中",false));

        requestPost(HttpUrls.URL_REVIEW_LISTS(), mApiParams, CommonEntity.class, new HttpCallBack<CommonEntity>() {
            @Override
            public void onFailure(String message) {
                MyToast.myToast(ShoppingReviewActivity.this,message);
                BeautyDefine.getOpenPageDefine(ShoppingReviewActivity.this).progressControl(new OpenPageDefine.ProgressController.Hider());

            }

            @Override
            public void onSuccess(CommonEntity commonEntity) {
                MyToast.myToast(ShoppingReviewActivity.this,"评价成功");
                OrderLiveData.getInstance().notifyInfoChanged(new OrderInfo(getIntent().getIntExtra("orderId",0), Constants.PAY_STATE_REVIEW));//广播
                BeautyDefine.getOpenPageDefine(ShoppingReviewActivity.this).progressControl(new OpenPageDefine.ProgressController.Hider());

                finish();

            }
        });
    }
    /**
     * 上传图片
     */
    private void goUploadPic() {
        BeautyDefine.getOpenPageDefine(this).progressControl(new OpenPageDefine.ProgressController.Showder("加载中",false));
       File[] file=new File[mSelectPhoto.size()];
       for (int i=0;i<mSelectPhoto.size();i++) {
           file[i] =new File(mSelectPhoto.get(i));
       }
        BeautyDefine.getUploadDefine().upload(file,new UploadResultCallBack(){

            @Override
            public void onSucceed(String[] strings) {
                submitData(strings);
            }

            @Override
            public void onFailure() {

                MyToast.myToast(ShoppingReviewActivity.this,"上传图片失败");
                BeautyDefine.getOpenPageDefine(ShoppingReviewActivity.this).progressControl(new OpenPageDefine.ProgressController.Hider());
            }
        });
    }
}
