package com.xingwang.circle;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beautydefinelibrary.BeautyDefine;
import com.beautydefinelibrary.ImagePickerCallBack;
import com.beautydefinelibrary.ImagePickerDefine;
import com.beautydefinelibrary.UploadResultCallBack;
import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.xingwang.circle.base.BaseActivity;
import com.xingwang.circle.bean.Card;
import com.xingwang.circle.bean.CardBody;
import com.xingwang.circle.bean.CardFileType;
import com.xingwang.circle.bean.CommentBean;
import com.xingwang.circle.bean.Customer;
import com.xingwang.circle.bean.Forum;
import com.xingwang.swip.utils.Constants;
import com.xingwang.circle.view.BottomSheetDialog;
import com.xingwang.circle.view.photo.SortableNinePhotoLayout;
import com.xingwang.swip.title.TopTitleView;
import com.xingwang.swip.utils.GlideUtils;
import com.xingwang.swip.utils.HttpUtil;
import com.xingwang.swip.utils.JsonUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class PostActivity extends BaseActivity implements SortableNinePhotoLayout.Delegate, EasyPermissions.PermissionCallbacks, View.OnClickListener {

    private static final int RC_CHOOSE_PHOTO = 2;
    private static final int RC_PHOTO_PREVIEW = 3;//图片预览
    private static final int CAMEAR = 100;//图片/视频权限
    private static final int VIDEO = 200;//图片/视频权限
    private static final int TAG_CODE = 300;//获取tag

    protected TopTitleView titleView;
    protected EditText et_add_post;
    protected SortableNinePhotoLayout sort_photos;
    protected RelativeLayout rela_play_video;//视频播放按钮
    protected ImageView img_delete_video;//视频删除
    protected ImageView img_screen;//视频第一帧

    protected TextView tv_tag;//栏目
    protected RelativeLayout rela_select_tag;//栏目选择

    protected List<String> picLocalPaths = new ArrayList<>();//图片本地地址
    protected List<String> picNetPaths = new ArrayList<>();//图片网络地址

    protected List<File> localFiles = new ArrayList<>();//本地文件

    private HashMap<String, String> params = new HashMap<>();

    private String flag = CardFileType.NONE;//判断拍摄视频/图片
    private String forum_id;//版块id
    private String category;//栏目
    private String videoPath;//视频地址
    private String firstFramePath;//视频第一帧地址
    //相册是否显示视频选项 true-显示 false-不显示,默认显示
    private boolean isShowVideo = true;//视频

    private BottomSheetDialog sheetDialog;
    private ImagePickerDefine pickerDefine;

    public static void getIntent(Context context, String forum_id, String category) {
        Intent intent = new Intent(context, PostActivity.class);
        intent.putExtra(Constants.INTENT_DATA, forum_id);
        intent.putExtra(Constants.INTENT_DATA1, category);
        context.startActivity(intent);
    }

    public static void getIntent(Context context) {
        Intent intent = new Intent(context, PostActivity.class);
        context.startActivity(intent);
    }

    public static Intent getIntentForResult(Activity activity, int requestCode, int resultCode) {
        Intent intent = new Intent(activity, PostActivity.class);
        intent.putExtra(Constants.INTENT_DATA, resultCode);
        activity.startActivityForResult(intent, requestCode);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getLayoutId() {
        return R.layout.circle_activity_post;
    }

    @Override
    protected void initView() {

        titleView = (TopTitleView) findViewById(R.id.title);
        et_add_post = (EditText) findViewById(R.id.et_add_post);
        rela_select_tag = (RelativeLayout) findViewById(R.id.rela_select_tag);
        tv_tag = (TextView) findViewById(R.id.tv_tag);
        sort_photos = (SortableNinePhotoLayout) findViewById(R.id.sort_photos);
        rela_play_video = (RelativeLayout) findViewById(R.id.rela_play_video);
        img_delete_video = (ImageView) findViewById(R.id.img_delete_video);
        img_screen = (ImageView) findViewById(R.id.img_screen);
        sort_photos.setDelegate(this);

        img_delete_video.setOnClickListener(this);
        rela_play_video.setOnClickListener(this);
        rela_select_tag.setOnClickListener(this);

        titleView.setRightFristEnable(true);
        titleView.setRightFristText("发表", new TopTitleView.OnRightFirstClickListener() {
            @Override
            public void onRightFirstClickListener(View v) {
                if (EmptyUtils.isEmpty(category)) {
                    ToastUtils.showShortSafe("请选择相关栏目");
                    return;
                }
                initPostCard();
            }
        });

        sheetDialog = BottomSheetDialog.getInstance();
        sheetDialog.setCallback(new BottomSheetDialog.CardDialogCallback() {
            @Override
            public void shotVideo() {
                flag = CardFileType.VIDEO;
                requestCameraPermission();
            }

            @Override
            public void takePhoto() {
                flag = CardFileType.IMG;

                requestCameraPermission();
                sort_photos.setVisibility(View.VISIBLE);
                rela_play_video.setVisibility(View.GONE);
            }
        });
    }

    private void upload(String fileType, CardBody body) {

        File[] arrayFile = localFiles.toArray(new File[localFiles.size()]);

        BeautyDefine.getUploadDefine().upload(arrayFile, new UploadResultCallBack() {
            @Override
            public void onSucceed(String[] strings) {
                picNetPaths.clear();
                picNetPaths.addAll(Arrays.asList(strings));
                switch (fileType) {
                    case CardFileType.VIDEO:
                        body.setVideo(picNetPaths.get(0));
                        postCard(JsonUtils.objectToJson(body));
                        break;
                    case CardFileType.IMG:
                        body.setImgs(picNetPaths);
                        postCard(JsonUtils.objectToJson(body));
                        break;
                    case CardFileType.COVER:
                        body.setCover(picNetPaths.get(0));
                        localFiles.clear();
                        localFiles.add(FileUtils.getFileByPath(videoPath));
                        upload(CardFileType.VIDEO, body);
                        break;
                }
            }

            @Override
            public void onFailure() {
                hideLoadingDialog();
                ToastUtils.showShortSafe("文件上传失败");
            }
        });

    }

    @Override
    public void initData() {
        sort_photos.setMaxItemCount(Constants.MAX_COUNT);
        titleView.setTitleText("发帖子");

        forum_id = getIntent().getStringExtra(Constants.INTENT_DATA);
        category = getIntent().getStringExtra(Constants.INTENT_DATA1);
        pickerDefine = BeautyDefine.getImagePickerDefine(this);

    }

    /**
     * 帖子发送准备工作
     */
    private void initPostCard() {

        if (EmptyUtils.isEmpty(et_add_post.getText().toString())) {
            ToastUtils.showShortSafe("内容不可为空!");
            return;
        }

        showLoadingDialog();

        params.clear();
        CardBody body = new CardBody();
        body.setText(et_add_post.getText().toString());

        params.put("type", flag);
        params.put("forum_id", forum_id);
        params.put("category", category);

        localFiles.clear();
        switch (flag) {
            case CardFileType.VIDEO:
                localFiles.add(FileUtils.getFileByPath(firstFramePath));
                upload(CardFileType.COVER, body);
                break;
            case CardFileType.IMG:

                for (String path : picLocalPaths) {
                    localFiles.add(FileUtils.getFileByPath(path));
                }

                upload(CardFileType.IMG, body);
                break;
            default://此时无文件上传
                postCard(JsonUtils.objectToJson(body));
                break;
        }
    }

    private void postCard(String body) {
        params.put("body", body);
        HttpUtil.post(Constants.CIRCLE_POST, params, new HttpUtil.HttpCallBack() {
            @Override
            public void onFailure(String message) {
                ToastUtils.showShortSafe(message);
                hideLoadingDialog();
            }

            @Override
            public void onSuccess(String json) {
                hideLoadingDialog();
                Card sendCard = JsonUtils.jsonToPojo(json, Card.class);

                if (EmptyUtils.isEmpty(sendCard)) {
                    ToastUtils.showShortSafe("数据解析错误");
                    return;
                }

                if (sendCard.getState() != 1) {
                    ToastUtils.showShortSafe("审核通过后展示");
                    finish();
                    return;
                }

                ToastUtils.showShortSafe("发布完成");

                EventBus.getDefault().post(category);
                setResult(getIntent().getIntExtra(Constants.INTENT_DATA, 100));
                finish();
            }
        });
    }

    @AfterPermissionGranted(CAMEAR)
    private void requestCameraPermission() {
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO};
        if (EasyPermissions.hasPermissions(this, perms)) {
            getFileResult();
        } else {
            EasyPermissions.requestPermissions(PostActivity.this, "需要以下权限:\n\n1.拍照\n\n2.存储读写权限", CAMEAR, perms);
        }
    }

    @Override
    public void onClickAddNinePhotoItem(SortableNinePhotoLayout sortableNinePhotoLayout, View view, int position, ArrayList<String> models) {
        if (EmptyUtils.isEmpty(picLocalPaths)
                && EmptyUtils.isEmpty(videoPath)) {
            resetPicPicker();
        }
        getFileResult();
    }

    @Override
    public void onClickDeleteNinePhotoItem(SortableNinePhotoLayout sortableNinePhotoLayout, View view, int position, String model, ArrayList<String> models) {
        sort_photos.removeItem(position);
        if (EmptyUtils.isEmpty(sort_photos.getData())) {
            resetPicPicker();
        }
    }

    @Override
    public void onClickNinePhotoItem(SortableNinePhotoLayout sortableNinePhotoLayout, View view, int position, String model, ArrayList<String> models) {
        Intent photoPickerPreviewIntent = new PhotoPreviewActivity.IntentBuilder(this)
                .previewPhotos(models) // 当前预览的图片路径集合
                // .selectedPhotos(models) // 当前已选中的图片路径集合
                .maxChooseCount(sort_photos.getMaxItemCount()) // 图片选择张数的最大值
                .currentPosition(position) // 当前预览图片的索引
                .build();
        startActivityForResult(photoPickerPreviewIntent, RC_PHOTO_PREVIEW);
    }

    @Override
    public void onNinePhotoItemExchanged(SortableNinePhotoLayout sortableNinePhotoLayout, int fromPosition, int toPosition, ArrayList<String> models) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        pickerDefine.onActivityResultHanlder(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case RC_CHOOSE_PHOTO://图片选择
                    break;
                case VIDEO://视频
                    videoPath = data.getStringExtra(Constants.INTENT_DATA);
                    if (EmptyUtils.isEmpty(videoPath)) {
                        resetPicPicker();
                    } else {
                        sort_photos.setVisibility(View.GONE);
                        rela_play_video.setVisibility(View.VISIBLE);

                        firstFramePath = data.getStringExtra(Constants.INTENT_DATA1);
                        if (EmptyUtils.isNotEmpty(firstFramePath)) {
                            GlideUtils.loadPic(firstFramePath, img_screen, getApplicationContext());
                            Log.i("post", "frame-->" + firstFramePath);
                        }
                    }
                    Log.i("post", "video-->" + videoPath);
                    break;
                case TAG_CODE://话题选择
                    Forum forum = (Forum) data.getSerializableExtra(Constants.INTENT_DATA);

                    if (EmptyUtils.isNotEmpty(forum)) {
                        forum_id = forum.getId();
                        category = data.getStringExtra(Constants.INTENT_DATA1);
                        tv_tag.setVisibility(View.VISIBLE);
                        tv_tag.setText("#" + forum.getTitle() + "-" + category + "#");
                    } else {
                        tv_tag.setVisibility(View.GONE);
                        ToastUtils.showShortSafe("栏目选择失败，请重新选择");
                    }
                    break;
            }
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
            getFileResult();
        }
    }

    //获取图片/视频选择
    private void getFileResult() {
        switch (flag) {
            case CardFileType.VIDEO:
                startActivityForResult(ShotVideoActivity.class, VIDEO);
                break;
            case CardFileType.IMG:
                pickerDefine.showMultiplePicker(Constants.MAX_COUNT, null,
                        isShowVideo, new ImagePickerCallBack() {
                            @Override
                            public void onResult(List<String> list, ImagePickerDefine.MediaType mediaType, List<String> list1) {

                                if (mediaType == ImagePickerDefine.MediaType.VIDEO &&
                                        EmptyUtils.isNotEmpty(list)) {//此时为视频

                                    sort_photos.setVisibility(View.GONE);
                                    rela_play_video.setVisibility(View.VISIBLE);

                                    videoPath = list.get(0);
                                    if (EmptyUtils.isNotEmpty(list1)) {//封面
                                        firstFramePath = list1.get(0);
                                    }
                                    flag = CardFileType.VIDEO;
                                    GlideUtils.loadPic(firstFramePath, img_screen, getApplicationContext());
                                    return;
                                }
                                //picLocalPaths.clear();
                                picLocalPaths.addAll(list);
                                sort_photos.setData((ArrayList<String>) picLocalPaths);
                                if (EmptyUtils.isEmpty(picLocalPaths)) {
                                    resetPicPicker();
                                } else {
                                    isShowVideo = false;
                                }
                            }

                            @Override
                            public void onCancel() {

                            }
                        });
                break;
            default:
                sheetDialog.showDialog(getSupportFragmentManager());
                break;
        }
    }

    //重置相册选择操作
    private void resetPicPicker() {
        flag = CardFileType.NONE;
        isShowVideo = true;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.rela_play_video) {
            PlayVideoActivity.getIntent(PostActivity.this, videoPath);
        } else if (i == R.id.img_delete_video) {
            sort_photos.setVisibility(View.VISIBLE);
            rela_play_video.setVisibility(View.GONE);
            resetPicPicker();
        } else if (i == R.id.rela_select_tag) {
            startActivityForResult(PartSelctActivity.class, TAG_CODE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        picLocalPaths.clear();
        picNetPaths.clear();
        localFiles.clear();
    }
}
