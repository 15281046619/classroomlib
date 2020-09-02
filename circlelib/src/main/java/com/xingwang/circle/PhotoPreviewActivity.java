package com.xingwang.circle;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPropertyAnimatorListenerAdapter;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import com.xingwang.circle.base.BaseActivity;
import com.xingwang.circle.view.photo.HackyViewPager;
import com.xingwang.circle.view.photo.PhotoViewAttacher;
import com.xingwang.circle.view.photo.adapter.PhotoPageAdapter;
import com.xingwang.swip.title.TopTitleView;

import java.util.ArrayList;

public class PhotoPreviewActivity extends BaseActivity implements PhotoViewAttacher.OnViewTapListener{
    private static final String EXTRA_PREVIEW_PHOTOS = "EXTRA_PREVIEW_PHOTOS";
    private static final String EXTRA_MAX_CHOOSE_COUNT = "EXTRA_MAX_CHOOSE_COUNT";
    private static final String EXTRA_CURRENT_POSITION = "EXTRA_CURRENT_POSITION";

    private HackyViewPager mContentHvp;
    private TopTitleView titleView;

    private ArrayList<String> mSelectedPhotos;
    private PhotoPageAdapter mPhotoPageAdapter;
    private int mMaxChooseCount = 1;

    private boolean mIsHidden = false;
    /**
     * 上一次标题栏显示或隐藏的时间戳
     */
    private long mLastShowHiddenTime;

    @Override
    public int getLayoutId() {
        return R.layout.circle_activity_photo_preview;
    }

    @Override
    protected void initView() {
        mContentHvp = findViewById(R.id.hvp_photo_picker_preview_content);
        titleView = findViewById(R.id.title);
    }

    @Override
    public void initData() {
      /*  titleView.setRightFristText("删除", new TopTitleView.OnRightFirstClickListener() {
            @Override
            public void onRightFirstClickListener(View v) {
                int currentPos=mContentHvp.getCurrentItem();
                String currentPhoto = mPhotoPageAdapter.getItem(currentPos);
                if (mSelectedPhotos.contains(currentPhoto)) {
                    mSelectedPhotos.remove(currentPhoto);

                    if (mSelectedPhotos.size()==0){
                        onBackPressed();
                        return;
                    }

                    mPhotoPageAdapter.notifyDataSetChanged();
                    titleView.setTitleText((mContentHvp.getCurrentItem() + 1) + "/" + mPhotoPageAdapter.getCount());
                }
            }
        });*/

        mContentHvp.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                handlePageSelectedStatus();
            }
        });

        // 获取图片选择的最大张数
        mMaxChooseCount = getIntent().getIntExtra(EXTRA_MAX_CHOOSE_COUNT, 1);
        if (mMaxChooseCount < 1) {
            mMaxChooseCount = 1;
        }

        mSelectedPhotos = getIntent().getStringArrayListExtra(EXTRA_PREVIEW_PHOTOS);
        if (mSelectedPhotos == null) {
            mSelectedPhotos = new ArrayList<>();
        }

        //ArrayList<String> previewPhotos = getIntent().getStringArrayListExtra(EXTRA_PREVIEW_PHOTOS);
        /*if (TextUtils.isEmpty(previewPhotos.get(0))) {
            // 从BGAPhotoPickerActivity跳转过来时，如果有开启拍照功能，则第0项为""
            previewPhotos.remove(0);
        }
*/
        int currentPosition = getIntent().getIntExtra(EXTRA_CURRENT_POSITION, 0);

        mPhotoPageAdapter = new PhotoPageAdapter(this, mSelectedPhotos,this);
        mContentHvp.setAdapter(mPhotoPageAdapter);
        mContentHvp.setCurrentItem(currentPosition);
        titleView.setTitleText((mContentHvp.getCurrentItem() + 1) + "/" + mPhotoPageAdapter.getCount());
        titleView.setRightFristEnable(true);

      /*  titleView.setOnBackClickListener(new TopTitleView.OnBackClickListener() {
            @Override
            public void onBackClickListener() {
                onBackPressed();
            }
        });*/

    }

    public static class IntentBuilder {
        private Intent mIntent;

        public IntentBuilder(Context context) {
            mIntent = new Intent(context, PhotoPreviewActivity.class);
        }

        /**
         * 图片选择张数的最大值
         */
        public IntentBuilder maxChooseCount(int maxChooseCount) {
            mIntent.putExtra(EXTRA_MAX_CHOOSE_COUNT, maxChooseCount);
            return this;
        }

        /**
         * 当前已选中的图片路径集合
         */
      /*  public IntentBuilder selectedPhotos(ArrayList<String> selectedPhotos) {
            mIntent.putStringArrayListExtra(EXTRA_SELECTED_PHOTOS, selectedPhotos);
            return this;
        }*/

        /**
         * 当前预览的图片路径集合
         */
        public IntentBuilder previewPhotos(ArrayList<String> previewPhotos) {
            mIntent.putStringArrayListExtra(EXTRA_PREVIEW_PHOTOS, previewPhotos);
            return this;
        }

        /**
         * 当前预览图片的索引
         */
        public IntentBuilder currentPosition(int currentPosition) {
            mIntent.putExtra(EXTRA_CURRENT_POSITION, currentPosition);
            return this;
        }

        public Intent build() {
            return mIntent;
        }
    }

    /**
     * 获取已选择的图片集合
     *
     * @param intent
     * @return
     */
    public static ArrayList<String> getSelectedPhotos(Intent intent) {
        return intent.getStringArrayListExtra(EXTRA_PREVIEW_PHOTOS);
    }

   /* @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putStringArrayListExtra(EXTRA_PREVIEW_PHOTOS, mSelectedPhotos);
        setResult(RESULT_OK, intent);
        finish();
    }*/

    private void handlePageSelectedStatus() {
        if (mPhotoPageAdapter == null) {
            return;
        }
        titleView.setTitleText((mContentHvp.getCurrentItem() + 1) + "/" + mPhotoPageAdapter.getCount());
    }

    @Override
    public void onViewTap(View view, float x, float y) {
        if (System.currentTimeMillis() - mLastShowHiddenTime > 500) {
            mLastShowHiddenTime = System.currentTimeMillis();
            if (mIsHidden) {
                showTitleBarAndChooseBar();
            } else {
                hiddenToolBarAndChooseBar();
            }
        }
    }

    private void showTitleBarAndChooseBar() {
        if (titleView != null) {
            ViewCompat.animate(titleView).translationY(0).setInterpolator(new DecelerateInterpolator(2)).setListener(new ViewPropertyAnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(View view) {
                    mIsHidden = false;
                }
            }).start();
        }
    }

    private void hiddenToolBarAndChooseBar() {
        if (titleView != null) {
            ViewCompat.animate(titleView).translationY(-titleView.getHeight()).setInterpolator(new DecelerateInterpolator(2)).setListener(new ViewPropertyAnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(View view) {
                    mIsHidden = true;
                }
            }).start();
        }
    }
}
