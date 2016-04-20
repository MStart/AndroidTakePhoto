package com.mylhyl.takephoto;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;

import java.io.File;

/**
 * Created by hupei on 2016/4/14.
 */
public final class TakePhotoManager {
    private static final String KEY_INTERNAL_SAVED_VIEW_STATE = "internalSavedViewState";
    private static final String EXTRA_TAKE_PHOTO_OPTIONS = "takePhotoOptions";
    public static final int REQUEST_CODE_TAKE_PHOTO = 0x38;
    private static TakePhotoManager mInstance = null;
    private TakePhotoOptions mTakePhotoOptions;
    private TakePhotoResult mCallBack;

    public static TakePhotoManager getInstance() {
        if (mInstance == null) {
            mInstance = new TakePhotoManager();
        }
        return mInstance;
    }

    private TakePhotoManager() {

    }

    public boolean createForResult(Bundle savedInstanceState, TakePhotoResult callback) {
        if (callback == null) {
            throw new IllegalArgumentException("TakePhotoResult is null");
        }
        mCallBack = callback;
        if (savedInstanceState != null) {
            Bundle bundle = savedInstanceState.getBundle(KEY_INTERNAL_SAVED_VIEW_STATE);
            if (bundle != null) {
                mTakePhotoOptions = (TakePhotoOptions) bundle.getSerializable(EXTRA_TAKE_PHOTO_OPTIONS);
                mCallBack.onResult(mTakePhotoOptions);
            }
            return true;
        }
        return false;
    }

    /**
     * 启动相机拍照
     *
     * @param activity
     * @param options  图片参数
     */
    public void request(Activity activity, TakePhotoOptions options) {
        if (checkParams(options)) return;
        try {
            activity.startActivityForResult(getIntent(options.getOriginalFile()), REQUEST_CODE_TAKE_PHOTO);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
            mCallBack.onFailure(e.getMessage());
        }
    }

    /**
     * 启动相机拍照
     *
     * @param options  图片参数
     * @param fragment
     */
    public void request(Fragment fragment, TakePhotoOptions options) {
        if (checkParams(options)) return;
        try {
            fragment.startActivityForResult(getIntent(options.getOriginalFile()), REQUEST_CODE_TAKE_PHOTO);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
            mCallBack.onFailure(e.getMessage());
        }
    }

    private boolean checkParams(TakePhotoOptions options) {
        if (options == null) {
            throw new IllegalArgumentException("TakePhotoOptions is null");
        }
        if (options.getTakePhotoDir() == null) {
            mCallBack.onFailure("创建缓存目录失败，请检查储存设备！");
            return true;
        } else if (options.getOriginalFile() == null) {
            mCallBack.onFailure("创建缓存文件失败，请检查储存设备！");
            return true;
        } else if (!TakePhotoUtil.isDiskAvailable()) {
            mCallBack.onFailure("SD卡空间不足10m，请及时清理！");
            return true;
        }
        mTakePhotoOptions = options;
        return false;
    }

    private Intent getIntent(File file) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        return intent;
    }

    /**
     * 在 Activity 或 Fragment 的 onActivityResult方法调用
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public void activityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_CANCELED
                && requestCode == REQUEST_CODE_TAKE_PHOTO) {
            onTakePhotoDone();//拍照
        }
    }

    private void onTakePhotoDone() {
        try {
            // 生成压缩图
            TakePhoto compressedOptions = mTakePhotoOptions.getCompressedOptions();
            TakePhotoUtil.createImageThumbnail(mTakePhotoOptions.getOriginalFile().getAbsolutePath(),
                    compressedOptions.file.getAbsolutePath(),
                    compressedOptions.width, compressedOptions.height);
            // 生成缩略图
            if (mTakePhotoOptions.isCreateThumbnail()) {
                TakePhoto thumbnailOptions = mTakePhotoOptions.getThumbnailOptions();
                TakePhotoUtil.createImageThumbnail(mTakePhotoOptions.getOriginalFile().getAbsolutePath(),
                        thumbnailOptions.file.getAbsolutePath(),
                        thumbnailOptions.width, thumbnailOptions.height);
            }
            mCallBack.onResult(mTakePhotoOptions);
        } catch (Exception e) {
            e.printStackTrace();
            mCallBack.onFailure(e.getMessage());
        }
    }

    public void saveInstanceState(Bundle outState) {
        if (mTakePhotoOptions != null) {
            Bundle bundle = new Bundle();
            bundle.putSerializable(EXTRA_TAKE_PHOTO_OPTIONS, mTakePhotoOptions);
            if (outState != null)
                outState.putBundle(KEY_INTERNAL_SAVED_VIEW_STATE, bundle);
        }
    }

    public void onClear() {
        mTakePhotoOptions = null;
        mCallBack = null;
    }
}
